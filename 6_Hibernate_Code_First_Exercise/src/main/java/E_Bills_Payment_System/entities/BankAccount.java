package E_Bills_Payment_System.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "e_bank_accounts")
public class BankAccount extends BillingDetails{
    private final static String BILLING_DETAILS_TYPE = "BANK_ACCOUNT";

    @Column(name = "bank_name", nullable = false, length = 60)
    private String bankName;

    @Column(name = "swift_code", nullable = false, length = 50)
    private String swiftCode;

    public BankAccount() {
        super();
    }

    public BankAccount(String billingDetailsType, String number,String bankName, String swiftCode ) {
        super(billingDetailsType, number);
        this.bankName = bankName;
        this.swiftCode = swiftCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }
}
