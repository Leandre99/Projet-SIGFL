package bj.mef.sigfl_backend.invoice.domain.port;

import java.math.BigDecimal;
import java.util.Currency;

public interface ExchangeRateService {
    BigDecimal getRate(Currency from, Currency to);
}