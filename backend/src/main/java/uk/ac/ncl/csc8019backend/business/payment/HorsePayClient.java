 package uk.ac.ncl.csc8019backend.business.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
 import java.time.format.DateTimeFormatter;
 import java.util.HashMap;
 import java.util.Map;

 import org.springframework.beans.factory.annotation.Value;
 import org.springframework.http.HttpEntity;
 import org.springframework.http.HttpHeaders;
 import org.springframework.http.HttpMethod;
 import org.springframework.http.MediaType;
 import org.springframework.http.ResponseEntity;
 import org.springframework.stereotype.Component;
 import org.springframework.web.client.RestClientException;
 import org.springframework.web.client.RestTemplate;

 import uk.ac.ncl.csc8019backend.business.payment.dto.HorsePayResponse;
 import uk.ac.ncl.csc8019backend.business.payment.exceptions.HorsePayException;


/**
 * HTTP client for communicating with the HorsePay;
 * sends payment request and responses are parsed
 *  there were Typos in university dummy payment system  (eg, "forcePaymentSatusReturnType" instead of "forcePaymentStatusReturnType") 
 * i kept them to matcth expected format ;
 * @author Shaik Arbaz
 */

 @Component
 public class HorsePayClient {
    private final RestTemplate restTemplate;

    @Value("${horsepay.api.url}")
    private String apiUrl;

    @Value("${horsepay.store.id}")
    private String storeId;

    @Value("${horsepay.force.success:#{null}}")
    private Boolean forceSuccess;

    public HorsePayClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    /**
     * sends payment request to payment gateway 
     * and return response
     * @param amount total amount in gbp
     * @param customerID unique customer identifier 
     * @throws  HorsePayException when API returns an empty response or unreachable
     * @return  HorsepayResponse containing status and reason
     * 
     * @author Shaik Arbaz

     */
    public HorsePayResponse processPayment(String customerID, BigDecimal amount) {
        Map<String, Object> requestBody = new HashMap <>();
        LocalDateTime now = LocalDateTime.now();

        requestBody.put("storeID", storeId);
        requestBody.put("customerID", customerID);
        requestBody.put("date",now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        requestBody.put("time",now.format(DateTimeFormatter.ofPattern("HH:mm")));
        requestBody.put("timeZone", "GMT");
        requestBody.put("transactionAmount", amount);
        requestBody.put("currencyCode","GBP");

        if(forceSuccess != null) {
            requestBody.put("forcePaymentSatusReturnType", forceSuccess);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>>request = new HttpEntity<>(requestBody, headers);
        
        //Send Post requwat to HorsePay API
        
        try{
            ResponseEntity<HorsePayResponse> response = restTemplate.exchange(
                apiUrl, HttpMethod.POST, request, HorsePayResponse.class
            );
            if(response.getBody() == null || response.getBody().getPaymetSuccess()== null) {
                throw new HorsePayException("HorsePay returned an empty response");
            }
            return response.getBody();
        } catch (RestClientException e) {
            throw new HorsePayException("Failed to connect to HorsePay", e);
        }
    }
 }