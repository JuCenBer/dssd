package grupo16.dssd.api_cloud.config;

import grupo16.dssd.api_cloud.models.Role;
import grupo16.dssd.api_cloud.models.User;
import grupo16.dssd.api_cloud.repositories.UserRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class UsersInit {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Bean
    public CommandLineRunner initUsers() {
        return args -> {

            // ONG_SOL
            createUserIfNotExists(User.builder()
                    .nombreOng("ONG UNLP")
                    .username("virginie.jomphe")
                    .apiKey(passwordEncoder.encode("virginie.jomphe"))
                    .compromisosColaboracion(List.of())
                    .pedidosColaboracion(List.of())
                    .role(Role.ONG_SOL)
                    .build());

            createUserIfNotExists(User.builder()
                    .nombreOng("ONG INFO")
                    .username("walter.bates")
                    .apiKey(passwordEncoder.encode("walter.bates"))
                    .compromisosColaboracion(List.of())
                    .pedidosColaboracion(List.of())
                    .role(Role.ONG_SOL)
                    .build());


            // ONG_COL
            createUserIfNotExists(User.builder()
                    .nombreOng("ONG UNLP")
                    .username("thorsten.hartmann")
                    .apiKey(passwordEncoder.encode("thorsten.hartmann"))
                    .compromisosColaboracion(List.of())
                    .pedidosColaboracion(List.of())
                    .role(Role.ONG_COL)
                    .build());

            createUserIfNotExists(User.builder()
                    .nombreOng("ONG INFO")
                    .username("william.jobs")
                    .apiKey(passwordEncoder.encode("william.jobs"))
                    .compromisosColaboracion(List.of())
                    .pedidosColaboracion(List.of())
                    .role(Role.ONG_COL)
                    .build());


            // DIRECTIVO
            createUserIfNotExists(User.builder()
                    .nombreOng("ProjectPlanning")
                    .username("thomas.wallis")
                    .apiKey(passwordEncoder.encode("thomas.wallis"))
                    .compromisosColaboracion(List.of())
                    .pedidosColaboracion(List.of())
                    .role(Role.DIRECTIVO)
                    .build());

            createUserIfNotExists(User.builder()
                    .nombreOng("ProjectPlanning")
                    .username("zachary.williamson")
                    .apiKey(passwordEncoder.encode("zachary.williamson"))
                    .compromisosColaboracion(List.of())
                    .pedidosColaboracion(List.of())
                    .role(Role.DIRECTIVO)
                    .build());

            // SISTEM
            createUserIfNotExists(User.builder()
                    .nombreOng("Sistema Back")
                    .username("pp.back")
                    .apiKey(passwordEncoder.encode("pp.back"))
                    .compromisosColaboracion(List.of())
                    .pedidosColaboracion(List.of())
                    .role(Role.SISTEMA)
                    .build());


            System.out.println(">>> Usuarios inicializados");
        };
    }

    private void createUserIfNotExists(User user) {

        userRepository.findByUsername(user.getUsername())
                .ifPresentOrElse(
                        u -> { /* Ya existe, no hacer nada */ },
                        () -> {
                            userRepository.save(user);}
                );
    }


}
