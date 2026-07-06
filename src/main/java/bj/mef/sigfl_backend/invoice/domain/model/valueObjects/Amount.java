package bj.mef.sigfl_backend.invoice.domain.model.valueObjects;

import java.math.BigDecimal;

public class Amount {

    private BigDecimal amount;

    public Amount(BigDecimal amount){
        this.amount = amount;
    }
}
