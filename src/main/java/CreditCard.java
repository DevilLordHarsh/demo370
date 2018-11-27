/**
 * Class representing typical credit card
 * which has card number and security code.
 */
public class CreditCard {

    private String cardNumber;
    private String cardVerificationValue;
    private String expiry;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardVerificationValue() {
        return cardVerificationValue;
    }

    public void setCardVerificationValue(String cardVerificationValue) {
        this.cardVerificationValue = cardVerificationValue;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    CreditCard(String number, String cvv, String expiry) {
        this.cardNumber = number;
        this.cardVerificationValue = cvv;
        this.expiry = expiry;
    }
}
