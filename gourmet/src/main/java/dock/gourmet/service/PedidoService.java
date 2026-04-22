package dock.gourmet.service;

import dock.gourmet.dto.PedidoDtos.*;
import dock.gourmet.model.*;
import dock.gourmet.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    public PedidoResponse crear(String emailCliente, CrearPedidoRequest request) {
        Usuario cliente = usuarioRepository.findByEmail(emailCliente)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Pedido pedido = Pedido.builder()
                .cliente(cliente)
                .total(0.0)
                .build();

        pedido = pedidoRepository.save(pedido);

        final Pedido pedidoFinal = pedido;
        List<ItemPedido> items = request.getItems().stream().map(itemReq -> {
            Producto producto = productoRepository.findById(itemReq.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            return ItemPedido.builder()
                    .pedido(pedidoFinal)
                    .producto(producto)
                    .cantidad(itemReq.getCantidad())
                    .precioUnitario(producto.getPrecio())
                    .build();
        }).toList();

        double total = items.stream()
                .mapToDouble(i -> i.getPrecioUnitario() * i.getCantidad())
                .sum();

        pedido.setItems(items);
        pedido.setTotal(total);
        return toResponse(pedidoRepository.save(pedido));
    }

    public List<PedidoResponse> listarTodos() {
        return pedidoRepository.findAllByOrderByFechaCreacionDesc()
                .stream().map(this::toResponse).toList();
    }

    public List<PedidoResponse> listarPorCliente(String emailCliente) {
        Usuario cliente = usuarioRepository.findByEmail(emailCliente)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return pedidoRepository.findByCliente(cliente)
                .stream().map(this::toResponse).toList();
    }

    public List<PedidoResponse> listarPorEstado(EstadoPedido estado) {
        return pedidoRepository.findByEstado(estado)
                .stream().map(this::toResponse).toList();
    }

    public PedidoResponse cambiarEstado(Long id, String nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        pedido.setEstado(EstadoPedido.valueOf(nuevoEstado));
        return toResponse(pedidoRepository.save(pedido));
    }

    private PedidoResponse toResponse(Pedido p) {
        List<ItemResponse> items = p.getItems().stream().map(i ->
                new ItemResponse(i.getId(), i.getProducto().getNombre(),
                        i.getProducto().getImagenUrl(),
                        i.getCantidad(), i.getPrecioUnitario())
        ).toList();
        return new PedidoResponse(p.getId(), p.getCliente().getNombre(),
                items, p.getTotal(), p.getEstado().name(), p.getFechaCreacion());
    }
}
