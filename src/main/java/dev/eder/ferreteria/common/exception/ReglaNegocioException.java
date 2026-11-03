package dev.eder.ferreteria.common.exception;

// Termino en 409
public class ReglaNegocioException extends RuntimeException{

    public ReglaNegocioException (String mensaje){
        super(mensaje);
    }
    
}
