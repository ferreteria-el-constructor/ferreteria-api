package dev.eder.ferreteria.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class CalculadoraIgv {

    public static BigDecimal TASA_IGV = new BigDecimal("0.18");
    public static BigDecimal FACTOR_IGV = BigDecimal.ONE.add(TASA_IGV);
    public static int DECIMALES_DINERO= 2;
    public static int DECIMALES_CANTIDAD= 3;

    private CalculadoraIgv(){}

    // importe = precio x cantidad - descuento, en céntimos
    // 8.50 x 1.750 = 14.87500 -> 14.88
    public static BigDecimal calcularImporte(BigDecimal precioUnitario, BigDecimal cantidad, BigDecimal descuento){
        BigDecimal bruto = precioUnitario.multiply(cantidad);
        return redondear(bruto.subtract(descuento));
    }

    // Base sin IGV: 14.88 / 1.18 = 12.61
    public static BigDecimal calcularBase(BigDecimal importeIgv){
        return importeIgv.divide(FACTOR_IGV, DECIMALES_DINERO, RoundingMode.HALF_UP);
    }

    // IGV por resta para que base + igv = importe exacto
    public static BigDecimal calcularIgv(BigDecimal importeIgv, BigDecimal base){
        return redondear(importeIgv.subtract(base));
    }

    public static BigDecimal redondear(BigDecimal monto){
        return monto.setScale(DECIMALES_DINERO, RoundingMode.HALF_UP);
    }

    // Cantidades y stock: 3 decimales (1.75 -> 1.750)
    public static BigDecimal redondearCantidad(BigDecimal cantidad){
        return cantidad.setScale(DECIMALES_CANTIDAD, RoundingMode.HALF_UP);
    }
    
    // 2.000 -> true | 2.500 -> false
    public static boolean esEntero(BigDecimal valor){
        return valor.stripTrailingZeros().scale() <= 0;
    }
}
