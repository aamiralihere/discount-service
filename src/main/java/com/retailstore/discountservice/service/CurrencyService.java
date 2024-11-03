package com.retailstore.discountservice.service;import com.retailstore.discountservice.client.CurrencyExchangeClient;import com.retailstore.discountservice.exception.CurrencyNotFoundException;import com.retailstore.discountservice.exception.UnsupportedCurrencyException;import lombok.extern.slf4j.Slf4j;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.beans.factory.annotation.Value;import org.springframework.cache.annotation.Cacheable;import org.springframework.stereotype.Service;import java.util.Map;import java.util.Optional;@Service@Slf4jpublic class CurrencyService {    private final CurrencyExchangeClient currencyExchangeClient;    @Value("${feign.client.currency-exchange-client.key}")    private String apiKey;    @Autowired    public CurrencyService(CurrencyExchangeClient currencyExchangeClient) {        this.currencyExchangeClient = currencyExchangeClient;    }    @Cacheable(value = "currencyExchangeCache", key = "#originalCurrency + '_' + #targetCurrency")    public double getExchangeRate(String originalCurrency, String targetCurrency) {        log.info("Going to get exchange rates for originalCurrency = " + originalCurrency);        Map<String, Object> exchangeRates = currencyExchangeClient.getExchangeRates(originalCurrency, apiKey);        // Check for error in response        String result = (String) exchangeRates.get("result");        if ("error".equals(result)) {            String errorType = (String) exchangeRates.get("error-type");            throw new UnsupportedCurrencyException("Error retrieving exchange rates for " + originalCurrency + " : " + errorType);        }        // Extract rates if the response is successful        Map<String, Object> rates = (Map<String, Object>) exchangeRates.get("rates");        Object rateValue = Optional.ofNullable(rates)                .map(r -> r.get(targetCurrency))                .orElseThrow(() -> new CurrencyNotFoundException("Conversion rate not available for: " + targetCurrency));        // Check the type of the rateValue and convert to double if necessary        if (rateValue instanceof Integer) {            return ((Integer) rateValue).doubleValue();        } else if (rateValue instanceof Double) {            log.info("Returning exchange rate for targetCurrency = " + targetCurrency);            return (Double) rateValue;        } else {            throw new CurrencyNotFoundException("Conversion rate is not a valid number for: " + targetCurrency);        }    }}