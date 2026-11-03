package dev.eder.ferreteria.common.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import dev.eder.ferreteria.common.dto.ErrorRespuesta;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400: validación del record (también "items[0].cantidad")
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorRespuesta> manejarValidacion(MethodArgumentNotValidException ex) {
        Map<String, String> detalles = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> detalles.put(error.getField(), error.getDefaultMessage()));
        return armarRespuesta(HttpStatus.BAD_REQUEST, "Hay datos invalidos en la peticion", detalles);
    }

    // 400: JSON roto o enum que no existe
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorRespuesta> manejarJsonInvalido(HttpMessageNotReadableException ex) {
        return armarRespuesta(HttpStatus.BAD_REQUEST, "El JSON enviado no es valido o tiene un valor no permitido",
                Map.of());
    }

    // 400: falta un ?parametro= obligatorio
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorRespuesta> manejarParametroFaltante(MissingServletRequestParameterException ex) {
        return armarRespuesta(HttpStatus.BAD_REQUEST, "Falta un parametro obligatorio",
                Map.of(ex.getParameterName(), "Es obligatorio"));
    }

    // 400: parámetro de URL con tipo inválido (/api/productos/abc)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorRespuesta> manejarParametroInvalido(MethodArgumentTypeMismatchException ex) {
        return armarRespuesta(HttpStatus.BAD_REQUEST, "Un parametro tiene un valor no permitido",
                Map.of(ex.getName(), "Valor no vaido" + ex.getValue()));
    }

    // 404: el recurso no existe
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorRespuesta> manejarNoEcontrado(RecursoNoEncontradoException ex) {
        return armarRespuesta(HttpStatus.NOT_FOUND, ex.getMessage(), Map.of());
    }

    // 404: la ruta no existe
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorRespuesta> manejarRutaInexistente(NoResourceFoundException ex) {
        return armarRespuesta(HttpStatus.NOT_FOUND, "La ruta " + ex.getResourcePath() + "no existe", Map.of());
    }

    // 409: regla de negocio
    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<ErrorRespuesta> manejarReglaNegocio(ReglaNegocioException ex) {
        return armarRespuesta(HttpStatus.CONFLICT, ex.getMessage(), Map.of());
    }

    // 409: la BD rechazó un duplicado
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorRespuesta> manejarDuplicado(DataIntegrityViolationException ex) {
        return armarRespuesta(HttpStatus.CONFLICT, "El registro choca con otro ya existente. Vuelve a intetarlo",
                Map.of());
    }

    // 500: cualquier otro
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRespuesta> manejarGenaral(Exception ex){
        log.error("Error no encontrado", ex);
        return armarRespuesta(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrio un error inesperado", Map.of());
    }

    private ResponseEntity<ErrorRespuesta> armarRespuesta(HttpStatus status, String mensaje,
            Map<String, String> detalles) {
        ErrorRespuesta body = new ErrorRespuesta(status.value(), mensaje, detalles, LocalDateTime.now());
        return ResponseEntity.status(status).body(body);
    }
}
