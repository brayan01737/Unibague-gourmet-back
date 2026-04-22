package dock.gourmet.controller;

import dock.gourmet.dto.ProductoDtos.*;
import dock.gourmet.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping("/api/productos")
    public ResponseEntity<List<ProductoResponse>> listarDisponibles() {
        return ResponseEntity.ok(productoService.listarDisponibles());
    }

    @GetMapping("/api/admin/productos")
    public ResponseEntity<List<ProductoResponse>> listarTodos() {
        return ResponseEntity.ok(productoService.listarTodos());
    }

    @PostMapping("/api/admin/productos")
    public ResponseEntity<ProductoResponse> crear(
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("precio") Double precio,
            @RequestParam("categoria") String categoria,
            @RequestParam(value = "disponible", defaultValue = "true") boolean disponible,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) throws IOException {

        ProductoRequest request = new ProductoRequest();
        request.setNombre(nombre);
        request.setDescripcion(descripcion);
        request.setPrecio(precio);
        request.setCategoria(categoria);
        request.setDisponible(disponible);

        return ResponseEntity.ok(productoService.crear(request, imagen));
    }

    @PutMapping("/api/admin/productos/{id}")
    public ResponseEntity<ProductoResponse> actualizar(
            @PathVariable Long id,
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("precio") Double precio,
            @RequestParam("categoria") String categoria,
            @RequestParam(value = "disponible", defaultValue = "true") boolean disponible,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) throws IOException {

        ProductoRequest request = new ProductoRequest();
        request.setNombre(nombre);
        request.setDescripcion(descripcion);
        request.setPrecio(precio);
        request.setCategoria(categoria);
        request.setDisponible(disponible);

        return ResponseEntity.ok(productoService.actualizar(id, request, imagen));
    }

    @DeleteMapping("/api/admin/productos/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.ok().build();
    }
}
