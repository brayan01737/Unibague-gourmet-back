package dock.gourmet.service;

import dock.gourmet.dto.AuthDtos.*;
import dock.gourmet.model.Role;
import dock.gourmet.model.Usuario;
import dock.gourmet.repository.UsuarioRepository;
import dock.gourmet.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        if (!usuario.isActivo())
            throw new RuntimeException("Usuario inactivo");

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword()))
            throw new RuntimeException("Credenciales inválidas");

        String token = jwtUtil.generateToken(usuario.getEmail(), usuario.getRole().name());
        return new AuthResponse(token, usuario.getNombre(), usuario.getEmail(), usuario.getRole().name());
    }

    public UsuarioResponse crearUsuario(CrearUsuarioRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail()))
            throw new RuntimeException("El email ya está registrado");

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        usuario = usuarioRepository.save(usuario);
        return new UsuarioResponse(usuario.getId(), usuario.getNombre(),
                usuario.getEmail(), usuario.getRole().name(), usuario.isActivo());
    }

    public List<UsuarioResponse> listarUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(u -> new UsuarioResponse(u.getId(), u.getNombre(),
                        u.getEmail(), u.getRole().name(), u.isActivo()))
                .toList();
    }

    public List<UsuarioResponse> listarPorRol(Role role) {
        return usuarioRepository.findByRole(role).stream()
                .map(u -> new UsuarioResponse(u.getId(), u.getNombre(),
                        u.getEmail(), u.getRole().name(), u.isActivo()))
                .toList();
    }

    public void toggleActivo(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setActivo(!usuario.isActivo());
        usuarioRepository.save(usuario);
    }
}
