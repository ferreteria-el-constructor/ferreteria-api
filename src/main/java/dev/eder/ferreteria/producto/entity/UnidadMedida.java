package dev.eder.ferreteria.producto.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter 
@RequiredArgsConstructor 
public enum UnidadMedida {

    NIU("Unidad",false),
    MTR("Metro", true),
    KGM("Kilogramo", true);

    private final String descripcion;
    private final boolean permiteDecimales;

}
