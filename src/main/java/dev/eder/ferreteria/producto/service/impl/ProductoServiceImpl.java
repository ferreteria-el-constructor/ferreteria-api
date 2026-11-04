package dev.eder.ferreteria.producto.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.eder.ferreteria.common.exception.RecursoNoEncontradoException;
import dev.eder.ferreteria.common.exception.ReglaNegocioException;
import dev.eder.ferreteria.producto.dto.ProductoRequest;
import dev.eder.ferreteria.producto.dto.ProductoResponse;
import dev.eder.ferreteria.producto.entity.Producto;
import dev.eder.ferreteria.producto.mapper.ProductoMapper;
import dev.eder.ferreteria.producto.repository.ProductoRepository;
import dev.eder.ferreteria.producto.service.ProductoService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private static final int MAX_RESULTADOS = 20;

    private final ProductoRepository productoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> listar() {
        return productoRepository.findAllByOrderByNombreAsc()
                .stream()
                .map(ProductoMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> buscarActivos(String texto) {
        String filtro = texto == null ? "" : texto.trim();
        return productoRepository.buscarActivo(filtro, PageRequest.of(0, MAX_RESULTADOS))
                .stream()
                .map(ProductoMapper::toResponse)
                .toList();
    }


    @Override 
    @Transactional(readOnly = true)
    public ProductoResponse buscarPorId(Long id){
        return ProductoMapper.toResponse(buscarProducto(id));
    }

    @Override
    @Transactional
    public ProductoResponse crear(ProductoRequest request) {
        // 1 normalizar
        String codigo = ProductoMapper.normalizarCodigo(request.codigoInterno());

        // 2 codigo manual o automatoca
        if (codigo == null) {
            codigo = generarCodigoUnico();
        } else if (productoRepository.existsByCodigoInterno(codigo)) {
            throw new ReglaNegocioException(" Ya existe un producto con el codigo" + codigo);
        }

        // 3 reglas
        
        validarReglas(request);

        // 4 Mapper y 5 guardar
        Producto producto = ProductoMapper.toEntity(request);
        producto.setCodigoInterno(codigo);
        return ProductoMapper.toResponse(productoRepository.save(producto));
    }

    @Override
    @Transactional
    public ProductoResponse actualizar(Long id, ProductoRequest request) {
        Producto producto = buscarProducto(id);

        String codigo = ProductoMapper.normalizarCodigo(request.codigoInterno());
        if (codigo == null) {
            codigo = producto.getCodigoInterno();
        }
        if (productoRepository.existsByCodigoInternoAndIdNot(codigo, id)) {
            throw new ReglaNegocioException("Ya existe otro producto con el código " + codigo);
        }
        validarReglas(request);

        ProductoMapper.actualizarEntidad(request, producto);
        producto.setCodigoInterno(codigo);
        return ProductoMapper.toResponse(productoRepository.save(producto));
    }

    @Override 
    @Transactional 
    public void desactivar(Long id){
        Producto producto = buscarProducto(id);
        producto.setActivo(false);
    }


    //---------------------------------------------------------------------------------------------

    private Producto buscarProducto(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un producto con id " + id));
    }

    private void validarReglas(ProductoRequest request) {
        // Costo no mayor que venta
        BigDecimal costo = request.precioCosto() == null ? BigDecimal.ZERO : request.precioCosto();
        if (costo.compareTo(request.precioVenta()) > 0) {
            throw new ReglaNegocioException("El precio de costo (S/ " + costo
                    + ") no puede ser mayor que el precio de venta (S/ " + request.precioVenta() + ")");
        }
    }

    private String generarCodigoUnico() {
        String codigo;
        do {
            codigo = "P" + UUID.randomUUID().toString().replace("-", "").substring(0, 7).toUpperCase();
        } while (productoRepository.existsByCodigoInterno(codigo));
        return codigo;
    }

}
