package uk.ac.ncl.csc8019backend.business.payment;

 import org.springframework.beans.factory.annotation.Value;
 import org.springframework.context.annotation.Bean;
 import org.springframework.context.annotation.Configuration;
 import org.springframework.http.client.SimpleClientHttpRequestFactory;
 import org.springframework.web.client.RestTemplate;

 /**
  * payment module Spring configuration class
  * RestTemplate bean is cresated using configurable timeout for API calls
  * @author Shaik Arbaz
  */

 @Configuration
 public class PaymentConfig {
    @Value("${horsepay.timeout: 5000}")
    private int timeout;

    /**
     * Creates a RestTemplate with r connect and read timouts
     * @return configured RESt Template bean
     */

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(timeout);
        factory.setReadTimeout(timeout);
        return new RestTemplate(factory);
    }
 }