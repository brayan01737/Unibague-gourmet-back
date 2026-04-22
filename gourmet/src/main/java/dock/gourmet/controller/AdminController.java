package dock.gourmet.controller;

import dock.gourmet.dto.AuthDtos.*;
import dock.gourmet.model.Role;
import dock.gourmet.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UsuarioService usuarioService;

    @PostMapping("/usuarios")
    public ResponseEntity<UsuarioResponse> crearUsuario(@RequestBody CrearUsuarioRequest request) {
        return ResponseEntity.ok(usuarioService.crearUsuario(request));
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    @GetMapping("/usuarios/rol/{role}")
    public ResponseEntity<List<UsuarioResponse>> listarPorRol(@PathVariable Role role) {
        return ResponseEntity.ok(usuarioService.listarPorRol(role));
    }

    @PutMapping("/usuarios/{id}/toggle")
    public ResponseEntity<Void> toggleActivo(@PathVariable Long id) {
        usuarioService.toggleActivo(id);
        return ResponseEntity.ok().build();
    }
}
