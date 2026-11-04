package dev.eder.ferreteria.producto.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import dev.eder.ferreteria.producto.entity.UnidadMedida;
import lombok.Builder;

@Builder
public record ProductoResponse(

        Long id,
        String codigoInterno,
        String nombre,
        String marca,
        UnidadMedida unidadMedida,
        String unidadDescripcion, // "Metro"
        boolean permiteDecimales, // true para MTR y KGM
        BigDecimal precioVenta,
        BigDecimal precioCosto,
        BigDecimal margenPorcentaje,
        BigDecimal stock,
        BigDecimal stockMinimo,
        boolean stockBajo,
        boolean activo,
        LocalDateTime fechaRegistro) {

}
