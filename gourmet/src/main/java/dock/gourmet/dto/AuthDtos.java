package dock.gourmet.dto;

import dock.gourmet.model.Role;
import lombok.Data;

public class AuthDtos {

    @Data
    public static class LoginRequest {
        private String email;
        private String password;
    }

    @Data
    public static class CrearUsuarioRequest {
        private String nombre;
        private String email;
        private String password;
        private Role role;
    }

    @Data
    public static class AuthResponse {
        private String token;
        private String nombre;
        private String email;
        private String role;

        public AuthResponse(String token, String nombre, String email, String role) {
            this.token = token;
            this.nombre = nombre;
            this.email = email;
            this.role = role;
        }
    }

    @Data
    public static class UsuarioResponse {
        private Long id;
        private String nombre;
        private String email;
        private String role;
        private boolean activo;

        public UsuarioResponse(Long id, String nombre, String email, String role, boolean activo) {
            this.id = id;
            this.nombre = nombre;
            this.email = email;
            this.role = role;
            this.activo = activo;
        }
    }
}
