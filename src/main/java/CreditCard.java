/**
 * Class representing typical credit card
 * which has card number and security code.
 */
public class CreditCard {

    private String cardNumber;
    private String securityCode;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    CreditCard(String number, Object code) {
        this.cardNumber = number;
        this.securityCode = (String) code;
    }
}
