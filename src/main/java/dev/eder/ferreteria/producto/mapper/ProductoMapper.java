package dev.eder.ferreteria.producto.mapper;

import java.math.BigDecimal;
import java.math.RoundingMode;


import dev.eder.ferreteria.common.util.CalculadoraIgv;
import dev.eder.ferreteria.producto.dto.ProductoRequest;
import dev.eder.ferreteria.producto.dto.ProductoResponse;
import dev.eder.ferreteria.producto.entity.Producto;

public final class ProductoMapper {

    private static final BigDecimal CIEN = new BigDecimal("100");

    private ProductoMapper() {
    }

    public static Producto toEntity(ProductoRequest request) {
        Producto producto = new Producto();
        actualizarEntidad(request, producto);
        return producto;
    }

    // El código lo decide el service
    public static void actualizarEntidad(ProductoRequest request, Producto producto) {
        producto.setNombre(request.nombre().trim().toUpperCase());
        producto.setMarca(textoOpcional(request.marca()));
        producto.setUnidadMedida(request.unidadMedida());
        producto.setPrecioVenta(request.precioVenta());
        producto.setPrecioCosto(request.precioCosto() == null ? BigDecimal.ZERO : request.precioCosto());
        producto.setStock(CalculadoraIgv.redondearCantidad(request.stock()));
        producto.setStockMinimo(CalculadoraIgv.redondearCantidad(request.stockMinimo()));
        producto.setActivo(request.activo() == null || request.activo());
    }

    public static ProductoResponse toResponse(Producto producto) {
        return ProductoResponse.builder()
                .id(producto.getId())
                .codigoInterno(producto.getCodigoInterno())
                .nombre(producto.getNombre())
                .marca(producto.getMarca())
                .unidadMedida(producto.getUnidadMedida())
                .unidadDescripcion(producto.getUnidadMedida().getDescripcion())
                .permiteDecimales(producto.getUnidadMedida().isPermiteDecimales())
                .precioVenta(producto.getPrecioVenta())
                .precioCosto(producto.getPrecioCosto())
                .margenPorcentaje(calcularMargen(producto.getPrecioVenta(), producto.getPrecioCosto()))
                .stock(producto.getStock())
                .stockMinimo(producto.getStockMinimo())
                // stock <= stockMinimo, escrito con compareTo
                .stockBajo(producto.getStock().compareTo(producto.getStockMinimo()) <= 0)
                .activo(producto.getActivo())
                .fechaRegistro(producto.getFechaRegistro())
                .build();
    }

    public static String normalizarCodigo(String codigo){
        if (codigo == null || codigo.isBlank()) {
            return null;
        }
        
        return codigo.trim().toUpperCase();
    }

    public static BigDecimal calcularMargen(BigDecimal venta, BigDecimal costo){
        if (costo.compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }

        return venta.subtract(costo)
            .divide(costo,4,RoundingMode.HALF_UP)
            .multiply(CIEN)
            .setScale(2, RoundingMode.HALF_UP);
    }

    private static String textoOpcional(String texto) {
        return (texto == null || texto.isBlank() ? null : texto.trim().toUpperCase());
    }
}
