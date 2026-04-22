package dock.gourmet.controller;

import dock.gourmet.dto.PedidoDtos.*;
import dock.gourmet.model.EstadoPedido;
import dock.gourmet.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    // ── CLIENTE ──────────────────────────────────────────
    @PostMapping("/api/cliente/pedidos")
    public ResponseEntity<PedidoResponse> crear(
            @RequestBody CrearPedidoRequest request,
            Authentication auth) {
        return ResponseEntity.ok(pedidoService.crear(auth.getName(), request));
    }

    @GetMapping("/api/cliente/pedidos")
    public ResponseEntity<List<PedidoResponse>> misPedidos(Authentication auth) {
        return ResponseEntity.ok(pedidoService.listarPorCliente(auth.getName()));
    }

    // ── CHEF ─────────────────────────────────────────────
    @GetMapping("/api/chef/pedidos")
    public ResponseEntity<List<PedidoResponse>> listarTodos() {
        return ResponseEntity.ok(pedidoService.listarTodos());
    }

    @PutMapping("/api/chef/pedidos/{id}/estado")
    public ResponseEntity<PedidoResponse> cambiarEstado(
            @PathVariable Long id,
            @RequestBody CambiarEstadoRequest request) {
        return ResponseEntity.ok(pedidoService.cambiarEstado(id, request.getEstado()));
    }

    // ── ADMIN ─────────────────────────────────────────────
    @GetMapping("/api/admin/pedidos")
    public ResponseEntity<List<PedidoResponse>> listarAdmin() {
        return ResponseEntity.ok(pedidoService.listarTodos());
    }

    @GetMapping("/api/admin/pedidos/estado/{estado}")
    public ResponseEntity<List<PedidoResponse>> listarPorEstado(@PathVariable EstadoPedido estado) {
        return ResponseEntity.ok(pedidoService.listarPorEstado(estado));
    }
}
