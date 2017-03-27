package Backend;

/**
 * Created by Hans de Rooij on 26/03/2017.
 */
public class Transaction {
    private String IBAN;
    private String transactionId;
    private String CARD_UID;

    public Transaction(String IBAN, String transactionId, String CARD_UID) {
        this.IBAN = IBAN;
        this.transactionId = transactionId;
        this.CARD_UID = CARD_UID;
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getCARD_UID() {
        return CARD_UID;
    }

    public void setCARD_UID(String CARD_UID) {
        this.CARD_UID = CARD_UID;
    }
}
