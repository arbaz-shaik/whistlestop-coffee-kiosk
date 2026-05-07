package uk.ac.ncl.csc8019backend.business.payment;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import uk.ac.ncl.csc8019backend.business.payment.dto.HorsePayResponse;
import uk.ac.ncl.csc8019backend.business.payment.exceptions.HorsePayException;

@ExtendWith(MockitoExtension.class)
public class HorsePayClientTest {

    @Mock
    RestTemplate restTemplate;

    HorsePayClient client;

    // need this because @Value fields aren't injected without spring context
    @BeforeEach
    void setup() throws Exception {
        client = new HorsePayClient(restTemplate);
        injectField(client, "apiUrl", "http://test.com/pay");
        injectField(client, "storeId", "Team07");
    }

    @Test
    void successfulPayment() {
        HorsePayResponse mock = new HorsePayResponse();
        HorsePayResponse.PaymentStatus s = new HorsePayResponse.PaymentStatus();
        s.setStatus(true);
        s.setReason("payment successful");
        mock.setPaymetSuccess(s);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST),
                any(HttpEntity.class), eq(HorsePayResponse.class)))
                .thenReturn(new ResponseEntity<>(mock, HttpStatus.OK));

        HorsePayResponse res = client.processPayment("WCK-CF3838-123", new BigDecimal("10.99"));

        assertNotNull(res);
        assertTrue(res.getPaymetSuccess().isStatus());
    }

    @Test
    void apiUnreachable_throws() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST),
                any(HttpEntity.class), eq(HorsePayResponse.class)))
                .thenThrow(new RestClientException("Connection refused"));

        assertThrows(HorsePayException.class,
                () -> client.processPayment("WCK-CF3838-123", new BigDecimal("10.99")));
    }

    @Test
    void emptyResponseBody_throws() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST),
                any(HttpEntity.class), eq(HorsePayResponse.class)))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        assertThrows(HorsePayException.class,
                () -> client.processPayment("WCK-CF3838-123", new BigDecimal("10.99")));
    }

    private void injectField(Object obj, String name, Object value) throws Exception {
        Field f = obj.getClass().getDeclaredField(name);
        f.setAccessible(true);
        f.set(obj, value);
    }
}