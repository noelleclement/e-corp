/**
 * Created by Joost Jonkers on 24-3-2017.
 */
public class API {

    public String parse(String jsonLine) {
        JsonElement jelement = new JsonParser().parse(jsonLine);
        JsonObject  jobject = jelement.getAsJsonObject();
        String result = jobject.get("translatedText").getAsString();
        return result;

        {



}


