package uk.ac.ncl.csc8019backend.business.payment.dto;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * there was typo in the field name "paymetSucess" in the horsepay. so the typo was intentional to mactch the respose"
 * this class maps the HorsePay Json response;
 * @author Shaik Arbaz
 */

public class HorsePayResponse {
    @JsonProperty ("paymetSuccess")
    private PaymentStatus paymetSuccess;

    public PaymentStatus getPaymetSuccess() { return paymetSuccess; }
// nestesd calss to map the paymetSucess JSON object
    public static class PaymentStatus{
        @JsonProperty("Status")
        private boolean status;

        private String reason;

        public boolean isStatus() {return status;}
        public void setStatus(boolean status) {
            this.status = status;
            }

        public String getReason(){
            return reason;
            }
            
        public void setReason(String reason) {
            this.reason = reason;
            }

    }
         public void setPaymetSuccess(PaymentStatus paymetSuccess) {this.paymetSuccess = paymetSuccess;}
        
}


