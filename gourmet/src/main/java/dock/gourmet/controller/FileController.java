package dock.gourmet.controller;

import dock.gourmet.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;

@RestController
@RequestMapping("/uploads")
@RequiredArgsConstructor
public class FileController {

    private final FileStorageService fileStorageService;

    @GetMapping("/productos/{nombreArchivo}")
    public ResponseEntity<Resource> servirImagen(@PathVariable String nombreArchivo) {
        try {
            Path ruta = fileStorageService.obtenerRuta(nombreArchivo);
            Resource resource = new UrlResource(ruta.toUri());

            if (!resource.exists() || !resource.isReadable())
                return ResponseEntity.notFound().build();

            String contentType = nombreArchivo.endsWith(".png")
                    ? MediaType.IMAGE_PNG_VALUE : MediaType.IMAGE_JPEG_VALUE;

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
