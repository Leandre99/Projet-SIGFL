package bj.mef.sigfl_backend.invoice.domain.exception;

public class InvoiceNotFoundException extends RuntimeException{

    public InvoiceNotFoundException(String message){
        super(message);
    }
}
