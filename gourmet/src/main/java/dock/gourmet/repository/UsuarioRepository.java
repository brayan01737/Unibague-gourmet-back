package dock.gourmet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dock.gourmet.model.Role;
import dock.gourmet.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    List<Usuario> findByRole(Role role);
    boolean existsByEmail(String email);
}
