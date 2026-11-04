package dev.eder.ferreteria.producto.dto;


import java.math.BigDecimal;

import dev.eder.ferreteria.producto.entity.UnidadMedida;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ProductoRequest(
    @Pattern(regexp = "^$|^[A-Za-z0-9-]{4,12}$",
        message = "El codigo debe tener de 4 a 12 letras, numeros o guiones"
    )
    String codigoInterno,

    @NotBlank(message = "EL nombre es obligatorio")
    @Size(max = 150, message = "El nombre no puede pasar de 150 caracteres")
    String nombre,

    @Size(max = 80, message = "La marca no puede pasar de 80 caracteres")
    String marca,

    @NotNull(message = "La unidad de medida es oblogatorio (NIU, MTR o KGM)")
    UnidadMedida unidadMedida,

    @NotNull(message = "El precio venta es obligatorio")
    @DecimalMin(value = "0.01",message = "El precio de venta debe ser mayor a 0")
    @Digits(integer = 8, fraction = 2, message = "El precio admite hasta 2 decimales ")
    BigDecimal precioVenta,

    @DecimalMin(value = "0.00", message = "El costo no puede ser negativo")
    @Digits(integer = 8, fraction = 2, message = "El costo admite hasta decimales 2 decimales")
    BigDecimal precioCosto,

    @NotNull(message = "El stock es obligatorio")
    @DecimalMin(value = "0.000", message = "El stock no puede ser negativo")
    @Digits(integer = 9, fraction = 3, message = "El stock admite hasta 3 decimales")
    BigDecimal stock,

    @NotNull(message = "El stock minimo es obligatorio")
    @DecimalMin(value = "0.000", message = "El stock minimo no puede ser negativo")
    @Digits(integer = 9, fraction = 3, message = "El stock minimo admite hasta 3 decimales")
    BigDecimal stockMinimo,

    Boolean activo


) {

}
