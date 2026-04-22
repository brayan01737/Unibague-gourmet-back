package dock.gourmet.dto;

import lombok.Data;

public class ProductoDtos {

    @Data
    public static class ProductoRequest {
        private String nombre;
        private String descripcion;
        private Double precio;
        private String categoria;
        private String imagenUrl;
        private boolean disponible = true;
    }

    @Data
    public static class ProductoResponse {
        private Long id;
        private String nombre;
        private String descripcion;
        private Double precio;
        private String categoria;
        private String imagenUrl;
        private boolean disponible;

        public ProductoResponse(Long id, String nombre, String descripcion,
                                Double precio, String categoria,
                                String imagenUrl, boolean disponible) {
            this.id = id;
            this.nombre = nombre;
            this.descripcion = descripcion;
            this.precio = precio;
            this.categoria = categoria;
            this.imagenUrl = imagenUrl;
            this.disponible = disponible;
        }
    }
}
