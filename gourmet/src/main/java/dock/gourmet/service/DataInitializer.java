package dock.gourmet.service;

import dock.gourmet.model.Role;
import dock.gourmet.model.Usuario;
import dock.gourmet.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!usuarioRepository.existsByEmail("admin@gourmet.com")) {
            Usuario admin = Usuario.builder()
                    .nombre("Administrador")
                    .email("admin@gourmet.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .build();
            usuarioRepository.save(admin);
            log.info("Usuario admin creado: admin@gourmet.com / admin123");
        }
    }
}
