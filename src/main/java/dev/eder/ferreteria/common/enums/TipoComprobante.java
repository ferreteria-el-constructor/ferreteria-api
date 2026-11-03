package dev.eder.ferreteria.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter 
@RequiredArgsConstructor 
public enum TipoComprobante {

    FACTURA("01", "F"),
    BOLETA("03", "B");

    private final String codigoSunat;
    private final String prefijoSerie;
}
