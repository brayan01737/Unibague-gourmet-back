package dock.gourmet.service;

import dock.gourmet.dto.PedidoDtos.*;
import dock.gourmet.model.*;
import dock.gourmet.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public PedidoResponse crear(String emailCliente, CrearPedidoRequest request) {
        Usuario cliente = usuarioRepository.findByEmail(emailCliente)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 1. Calcular total antes de guardar
        double total = request.getItems().stream().mapToDouble(itemReq -> {
            Producto producto = productoRepository.findById(itemReq.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            return producto.getPrecio() * itemReq.getCantidad();
        }).sum();

        // 2. Guardar pedido con total correcto
        Pedido pedido = Pedido.builder()
                .cliente(cliente)
                .total(total)
                .build();
        pedido = pedidoRepository.save(pedido);

        // 3. Guardar cada item vinculado al pedido
        final Pedido pedidoGuardado = pedido;
        List<ItemPedido> items = request.getItems().stream().map(itemReq -> {
            Producto producto = productoRepository.findById(itemReq.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            return ItemPedido.builder()
                    .pedido(pedidoGuardado)
                    .producto(producto)
                    .cantidad(itemReq.getCantidad())
                    .precioUnitario(producto.getPrecio())
                    .build();
        }).toList();

        itemPedidoRepository.saveAll(items);

        // 4. Recargar pedido con items persistidos
        return toResponse(pedidoRepository.findById(pedidoGuardado.getId()).orElseThrow());
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

    @Transactional
    public PedidoResponse cambiarEstado(Long id, String nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        pedido.setEstado(EstadoPedido.valueOf(nuevoEstado));
        return toResponse(pedidoRepository.save(pedido));
    }

    private PedidoResponse toResponse(Pedido p) {
        List<ItemResponse> items = p.getItems().stream().map(i ->
                new ItemResponse(
                        i.getId(),
                        i.getProducto().getNombre(),
                        i.getProducto().getImagenUrl(),
                        i.getCantidad(),
                        i.getPrecioUnitario())
        ).toList();
        return new PedidoResponse(
                p.getId(),
                p.getCliente().getNombre(),
                items,
                p.getTotal(),
                p.getEstado().name(),
                p.getFechaCreacion());
    }
}
