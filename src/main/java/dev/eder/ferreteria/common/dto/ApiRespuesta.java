package dev.eder.ferreteria.common.dto;

public record ApiRespuesta<T>(
    String mensaje,
    T data
) {

}
