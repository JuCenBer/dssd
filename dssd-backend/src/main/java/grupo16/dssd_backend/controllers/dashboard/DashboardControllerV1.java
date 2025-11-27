package grupo16.dssd_backend.controllers.dashboard;

import grupo16.dssd_backend.helpers.BonitaSessionHolder;
import grupo16.dssd_backend.models.Role;
import grupo16.dssd_backend.services.dashboard.I_DashboardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardControllerV1 implements I_DashboardController{

    private final I_DashboardService dashboardService;
    private final BonitaSessionHolder bonitaSessionHolder;

    public DashboardControllerV1(I_DashboardService dashboardService, BonitaSessionHolder bonitaSessionHolder){
        this.dashboardService = dashboardService;
        this.bonitaSessionHolder = bonitaSessionHolder;
    }

    private boolean isGerente(){
        return BonitaSessionHolder.getBonitaSession().role() == Role.DIRECTIVO;
    }

    @PostMapping
    public ResponseEntity getAlgo(){
        if (!this.isGerente()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();



        return ResponseEntity.ok("");
    }
}
