package E_Bills_Payment_System.entities;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class BillingDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private int Id;

    @Column(name = "billing_details_type")
    private String billingDetailsType;

    private String number;

    @ManyToOne
    private User user;

    protected BillingDetails() {}

    protected BillingDetails(String billingDetailsType, String number) {
        this.billingDetailsType = billingDetailsType;
        this.number = number;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getBillingDetailsType() {
        return billingDetailsType;
    }

    public void setBillingDetailsType(String billingDetailsType) {
        this.billingDetailsType = billingDetailsType;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
