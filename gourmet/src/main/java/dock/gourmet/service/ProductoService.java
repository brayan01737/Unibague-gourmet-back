package dock.gourmet.service;

import dock.gourmet.dto.ProductoDtos.*;
import dock.gourmet.model.Producto;
import dock.gourmet.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final FileStorageService fileStorageService;

    public List<ProductoResponse> listarDisponibles() {
        return productoRepository.findByDisponibleTrue().stream()
                .map(this::toResponse).toList();
    }

    public List<ProductoResponse> listarTodos() {
        return productoRepository.findAll().stream()
                .map(this::toResponse).toList();
    }

    public ProductoResponse crear(ProductoRequest request, MultipartFile imagen) throws IOException {
        String imagenUrl = (imagen != null && !imagen.isEmpty())
                ? fileStorageService.guardar(imagen) : null;

        Producto producto = Producto.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .precio(request.getPrecio())
                .categoria(request.getCategoria())
                .imagenUrl(imagenUrl)
                .disponible(request.isDisponible())
                .build();

        return toResponse(productoRepository.save(producto));
    }

    public ProductoResponse actualizar(Long id, ProductoRequest request, MultipartFile imagen) throws IOException {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        producto.setCategoria(request.getCategoria());
        producto.setDisponible(request.isDisponible());

        if (imagen != null && !imagen.isEmpty()) {
            // Elimina la imagen anterior antes de guardar la nueva
            fileStorageService.eliminar(producto.getImagenUrl());
            producto.setImagenUrl(fileStorageService.guardar(imagen));
        }

        return toResponse(productoRepository.save(producto));
    }

    public void eliminar(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        fileStorageService.eliminar(producto.getImagenUrl());
        productoRepository.delete(producto);
    }

    private ProductoResponse toResponse(Producto p) {
        return new ProductoResponse(p.getId(), p.getNombre(), p.getDescripcion(),
                p.getPrecio(), p.getCategoria(), p.getImagenUrl(), p.isDisponible());
    }
}
