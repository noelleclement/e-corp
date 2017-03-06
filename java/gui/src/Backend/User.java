package Backend;

/**
 * Created by Hans de Rooij on 27/02/2017.
 */
public class User {
    private int accountNumber;
    private String name;

    public User(int accountNumber, String name) {
        this.accountNumber = accountNumber;
        this.name = name;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getName() {
        return name;
    }
}
