package bj.mef.sigfl_backend.invoice.domain.model.valueObjects;

public class InvoiceId {

    private final String value;

    public InvoiceId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Invalid id");
        }
        this.value = value;
    }

    public String value() {
        return value;
    }
}
