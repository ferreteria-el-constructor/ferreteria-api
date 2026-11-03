package dev.eder.ferreteria.common.dto;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorRespuesta(
    int status,
    String mensaje,
    Map<String, String> detalles,
    LocalDateTime fecha
) {

}
