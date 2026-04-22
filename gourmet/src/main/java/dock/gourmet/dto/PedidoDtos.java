package dock.gourmet.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

public class PedidoDtos {

    @Data
    public static class ItemRequest {
        private Long productoId;
        private Integer cantidad;
    }

    @Data
    public static class CrearPedidoRequest {
        private List<ItemRequest> items;
    }

    @Data
    public static class CambiarEstadoRequest {
        private String estado;
    }

    @Data
    public static class ItemResponse {
        private Long id;
        private String productoNombre;
        private String productoImagenUrl;
        private Integer cantidad;
        private Double precioUnitario;
        private Double subtotal;

        public ItemResponse(Long id, String productoNombre, String productoImagenUrl,
                            Integer cantidad, Double precioUnitario) {
            this.id = id;
            this.productoNombre = productoNombre;
            this.productoImagenUrl = productoImagenUrl;
            this.cantidad = cantidad;
            this.precioUnitario = precioUnitario;
            this.subtotal = precioUnitario * cantidad;
        }
    }

    @Data
    public static class PedidoResponse {
        private Long id;
        private String clienteNombre;
        private List<ItemResponse> items;
        private Double total;
        private String estado;
        private LocalDateTime fechaCreacion;

        public PedidoResponse(Long id, String clienteNombre, List<ItemResponse> items,
                               Double total, String estado, LocalDateTime fechaCreacion) {
            this.id = id;
            this.clienteNombre = clienteNombre;
            this.items = items;
            this.total = total;
            this.estado = estado;
            this.fechaCreacion = fechaCreacion;
        }
    }
}
