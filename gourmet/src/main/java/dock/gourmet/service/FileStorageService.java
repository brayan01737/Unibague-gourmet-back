package dock.gourmet.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
public class FileStorageService {

    @Value("${app.storage.location}")
    private String storageLocation;

    @Value("${app.storage.base-url}")
    private String baseUrl;

    private static final Set<String> EXTENSIONES_PERMITIDAS = Set.of(".jpg", ".jpeg", ".png", ".webp");
    private static final long MAX_BYTES = 5 * 1024 * 1024; // 5MB

    @PostConstruct
    public void init() {
        try {
            Path dir = Paths.get(storageLocation, "productos");
            Files.createDirectories(dir);
            log.info("Carpeta de imágenes lista: {}", dir.toAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear la carpeta de almacenamiento", e);
        }
    }

    public String guardar(MultipartFile archivo) throws IOException {
        validar(archivo);

        String extension = obtenerExtension(archivo.getOriginalFilename());
        String nombreArchivo = UUID.randomUUID() + extension;
        Path destino = Paths.get(storageLocation, "productos", nombreArchivo);

        Files.copy(archivo.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);
        log.info("Imagen guardada: {}", destino.toAbsolutePath());

        return baseUrl + "/uploads/productos/" + nombreArchivo;
    }

    public void eliminar(String urlImagen) {
        if (urlImagen == null || urlImagen.isBlank()) return;
        try {
            String nombreArchivo = urlImagen.substring(urlImagen.lastIndexOf('/') + 1);
            Path archivo = Paths.get(storageLocation, "productos", nombreArchivo);
            Files.deleteIfExists(archivo);
            log.info("Imagen eliminada: {}", archivo.toAbsolutePath());
        } catch (IOException e) {
            log.warn("No se pudo eliminar la imagen: {}", urlImagen);
        }
    }

    public Path obtenerRuta(String nombreArchivo) {
        return Paths.get(storageLocation, "productos", nombreArchivo);
    }

    private void validar(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty())
            throw new RuntimeException("El archivo está vacío");

        if (archivo.getSize() > MAX_BYTES)
            throw new RuntimeException("El archivo supera el tamaño máximo de 5MB");

        String extension = obtenerExtension(archivo.getOriginalFilename());
        if (!EXTENSIONES_PERMITIDAS.contains(extension.toLowerCase()))
            throw new RuntimeException("Formato no permitido. Use: jpg, jpeg, png, webp");
    }

    private String obtenerExtension(String nombreArchivo) {
        if (nombreArchivo == null || !nombreArchivo.contains("."))
            return ".jpg";
        return nombreArchivo.substring(nombreArchivo.lastIndexOf('.'));
    }
}
