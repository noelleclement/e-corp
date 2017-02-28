package Backend;

import com.opencsv.CSVReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Hans de Rooij on 28/02/2017.
 */
public class Users extends ArrayList<User> {
    public Users() {
        this.retrieveUsers();
    }

    public void retrieveUsers() {
        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader("users.csv"));
            String [] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                // nextLine[] is an array of values from the line
                this.add(new User(Integer.parseInt(nextLine[0]),nextLine[1]));
                System.out.println("id " + nextLine[0] + "\tnaam " + nextLine[1]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Path file = Paths.get("users.csv");
            try {
                Files.write(file, Arrays.asList("\"1\",\"Henk\""));
                System.out.println("There wasn't a user file, now there is :)");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
