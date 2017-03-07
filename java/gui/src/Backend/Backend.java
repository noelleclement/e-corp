package Backend;

/**
 * Created by Hans de Rooij on 27/02/2017.
 */
public class Backend {
    public Users users;
    public Backend() {
        users = new Users();
    }

    public boolean checkPin(String pin) {
            return users.checkPin(pin);
    }

    public User getCurrentUser() {
        return users.getCurrentUser();
    }
}
