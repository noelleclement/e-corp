import com.google.gson.*;

/**
 * Created by Hans de Rooij on 24/03/2017.
 */
public class API {
    public static boolean isCorrectCard(String accountNumber, String passNumber) {
        Gson gson = new Gson();
        String json = gson.toJson(new Object(){private String hoi = "hoi";});
        System.out.println(json);
        return true;
    }
}
