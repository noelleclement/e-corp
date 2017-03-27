package Backend;

import com.google.gson.JsonElement;

/**
 * Created by Hans de Rooij on 24/03/2017.
 */
public class JsonResponse {
    public JsonResponse(String type, String IBAN) {
        this.IBAN = IBAN;
        this.type = type;
    }
    public String type;
    public String IBAN;
    public String transaction_id
}
