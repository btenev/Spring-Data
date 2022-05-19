package E_Bills_Payment_System.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.Month;
import java.time.Year;

@Entity
@Table(name = "e_credit_cards")
public class CreditCard extends BillingDetails {
    private final static String BILLING_DETAILS_TYPE = "CREDIT_CARD";

    @Column(name = "card_type", nullable = false)
    private String cardType;

    @Column(nullable = false)
    private Month month;

    @Column(nullable = false)
    private Year year;

    public CreditCard() {
        super();
    }

    public CreditCard(String number, String cardType, Month month, Year year ) {
        super(BILLING_DETAILS_TYPE ,number);
        this.cardType = cardType;
        this.month = month;
        this.year = year;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public Year getYear() {
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }
}
