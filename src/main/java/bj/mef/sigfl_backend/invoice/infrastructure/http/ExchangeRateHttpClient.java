package bj.mef.sigfl_backend.invoice.infrastructure.http;

import java.math.BigDecimal;

public class ExchangeRateHttpClient {
    private final WebClient webClient;

    public ExchangeRateHttpClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public BigDecimal fetchRate(String from, String to) {

        String url = "https://api.exchangerate.com/latest?from=" + from + "&to=" + to;

        return webClient.get(url);
    }
}
