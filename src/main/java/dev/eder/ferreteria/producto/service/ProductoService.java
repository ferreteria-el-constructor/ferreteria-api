package dev.eder.ferreteria.producto.service;

import java.util.List;

import dev.eder.ferreteria.producto.dto.ProductoRequest;
import dev.eder.ferreteria.producto.dto.ProductoResponse;

public interface ProductoService {

    List<ProductoResponse> listar();

    List<ProductoResponse> buscarActivos(String texto);

    ProductoResponse buscarPorId(Long id);

    ProductoResponse crear(ProductoRequest request);

    ProductoResponse actualizar(Long id, ProductoRequest request);

    void desactivar(Long id);
}
