package Backend;

//import com.opencsv.CSVReader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Hans de Rooij on 28/02/2017.
 */
public class Users extends ArrayList<User> {
    private User currentUser = null;

    public Users() {
        this("users.csv");
    }

    public Users(String fileName) {
        this.retrieveUsers(fileName);
    }



    public boolean checkPin(String pin) {
       /* System.out.println("Checking pin");
        if(currentUser != null) {
            if(currentUser.checkPin(pin)) {
                currentUser.resetPinErrors();
                saveUsers("users.csv");
                return true;
            } else {
                currentUser.incrementPinErrors();
                saveUsers("users.csv");
                return false;
            }
        }
        else
            return false; */
            return true;
    }

    public User findUser(String accountNumber) throws UnexsistingUserException {
        for(User _user:this) {
            System.out.println("Comparing "+accountNumber+" with:" +_user.getAccountNumber());
            if(_user.getAccountNumber().equals(accountNumber)) {
                this.currentUser = _user;
                return _user;
            }
        }
        throw new UnexsistingUserException();
    }

    public void saveUsers(String fileName) {

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            for(User user:this) {
                writer.write(user.getCsvString());
                System.out.println(user.getCsvString());
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void retrieveUsers(String fileName) {
        /*CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(fileName));
            String [] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                // nextLine[] is an array of values from the line
                this.add(new User(nextLine[0],nextLine[1],nextLine[2],Integer.parseInt(nextLine[3])
                        ,Integer.parseInt(nextLine[4])));
                System.out.println("id " + nextLine[0] + "\tnaam " + nextLine[1]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Path file = Paths.get(fileName);
            try {
                Files.write(file, Arrays.asList("\"1\",\"Henk\",\"1234\",\"0\",\"100000\""));
                System.out.println("There wasn't a user file, now there is :)");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public User getCurrentUser() {
        return this.currentUser;
    }
}
