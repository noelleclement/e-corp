package Backend;

/**
 * Created by Hans de Rooij on 27/02/2017.
 */
public class User {
    private String accountNumber;
    private String name;
    private String pin;
    private int pinErrors;

    public User(String accountNumber, String name, String pin, int pinErrors) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.pin = pin;
        this.pinErrors = pinErrors;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getName() {
        return name;
    }

    public boolean checkPin(String pin) {
        System.out.println("Comparing "+this.pin+" with "+pin);
        if(this.pin.equals(pin)) {
            System.out.println("YThey matched");
            return true;
        } else {
            this.pinErrors++;
            return false;
        }
    }
    public String getCsvString() {
        return "\""+this.accountNumber+"\",\"" +
                    this.name+"\",\"" +
                    this.pin+"\",\"" +
                    this.pinErrors+"\"";
    }

    public int getPinErrors() {
        return pinErrors;
    }

    public void incrementPinErrors() {
        this.pinErrors = this.pinErrors +1;
    }

    public void resetPinErrors() {
        this.pinErrors = 0;
    }
}
