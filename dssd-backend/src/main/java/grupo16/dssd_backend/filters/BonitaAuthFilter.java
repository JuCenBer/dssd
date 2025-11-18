package grupo16.dssd_backend.filters;


import grupo16.dssd_backend.dtos.BonitaSession;
import grupo16.dssd_backend.helpers.BonitaSessionHolder;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
public class BonitaAuthFilter implements Filter {

    private static final Set<String> EXCLUDED_URIS = Set.of(
            "/login"
    );

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            filterChain.doFilter(req, res);
            return;
        }

        String path = req.getRequestURI();

        if (isExcluded(path)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        try {
            BonitaSession session = BonitaSessionHolder.getBonitaSession();
            if (session == null) {
                sendUnauthorized(res);
                return;
            }
        } catch (IllegalStateException ex) {
            sendUnauthorized(res);
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean isExcluded(String path) {
        return EXCLUDED_URIS.stream().anyMatch(path::contains);
    }

    private void sendUnauthorized(HttpServletResponse res) throws IOException {
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        res.setContentType("application/json");
        res.getWriter().write("""
            { "message": "Usuario no autenticado" }
        """);
    }
}
