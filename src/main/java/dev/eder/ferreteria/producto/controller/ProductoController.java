package dev.eder.ferreteria.producto.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.eder.ferreteria.common.dto.ApiRespuesta;
import dev.eder.ferreteria.producto.dto.ProductoRequest;
import dev.eder.ferreteria.producto.dto.ProductoResponse;
import dev.eder.ferreteria.producto.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping
    public ResponseEntity<ApiRespuesta<List<ProductoResponse>>> listar() {
        List<ProductoResponse> productos = productoService.listar();
        return ResponseEntity.ok(new ApiRespuesta<>("Productos encontrados: " + productos.size(), productos));
    }

    @GetMapping("/buscar")
    public ResponseEntity<ApiRespuesta<List<ProductoResponse>>> buscar(@RequestParam(defaultValue = "") String texto) {
        List<ProductoResponse> productos = productoService.buscarActivos(texto);
        return ResponseEntity.ok(new ApiRespuesta<>("Resultados: " + productos.size(), productos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiRespuesta<ProductoResponse>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiRespuesta<>("Producto encontrado", productoService.buscarPorId(id)));
    }

    @PostMapping
    public ResponseEntity<ApiRespuesta<ProductoResponse>> crear(@Valid @RequestBody ProductoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiRespuesta<>("Producto creado correctamente", productoService.crear(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiRespuesta<ProductoResponse>> actualizar(@PathVariable Long id,
            @Valid @RequestBody ProductoRequest request) {
        return ResponseEntity.ok(new ApiRespuesta<>("Producto actualizado", productoService.actualizar(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id){
        productoService.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}
