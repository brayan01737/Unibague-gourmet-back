package dock.gourmet.repository;

import dock.gourmet.model.EstadoPedido;
import dock.gourmet.model.Pedido;
import dock.gourmet.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByCliente(Usuario cliente);
    List<Pedido> findByEstado(EstadoPedido estado);
    List<Pedido> findAllByOrderByFechaCreacionDesc();
}
