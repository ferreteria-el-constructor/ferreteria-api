package dev.eder.ferreteria.common.exception;

// Termino en 404
public class RecursoNoEncontradoException extends RuntimeException{

    public RecursoNoEncontradoException(String mensaje){
        super(mensaje);
    }
}
