import SSL.SSLClient;
import com.google.gson.*;

import java.util.Observable;

/**
 * Created by Hans de Rooij on 24/03/2017.
 */
public class API {
    public JsonResponse isCorrectCard(String accountNumber, String passNumber) {
        JsonObject object = new JsonObject();
        object.addProperty("type","CONTROLEER_REKENINGNUMMER");
        object.addProperty("IBAN",accountNumber);
        object.addProperty("CARD_UID", passNumber);
        String reaction = getSSLReaction(object.toString());
        JsonElement element = new JsonParser().parse(reaction);
        object = element.getAsJsonObject();

        return new JsonResponses.ControleerRekeningnummer(object.get("type").getAsString(),
                object.get("IBAN").getAsString(),
                "TransactiIdEen", //object.get("transactionId").getAsString(),
                object.get("CARD_UID").getAsString());
    }

    public JsonResponse checkPinCode(String transactionID, String IBAN, String pinCode, String card_uid) {
        JsonObject object = new JsonObject();
        object.addProperty("transactionId", transactionID);
        object.addProperty("IBAN", IBAN);
        object.addProperty("type", "CONTROLEER_PINCODE");
        object.addProperty("pinCode", pinCode);
        object.addProperty("CARD_UID", card_uid);
        String reaction = getSSLReaction(object.toString());
        JsonElement element = new JsonParser().parse(reaction);
        object = element.getAsJsonObject();
        switch (object.get("type").getAsString()) {
            case "CORRECTE_PINCODE":
                return new JsonResponses.CorrectePincode(object.get("type").getAsString(),
                        object.get("IBAN").getAsString(),
                        object.get("transactionId").getAsString(),
                        object.get("CARD_UID").getAsString());
                break;
            case "INCORRECT_PINCODE":
                return new JsonResponses.IncorrectePincode(object.get("type").getAsString(),
                        object.get("IBAN").getAsString(),
                        object.get("transactionId").getAsString(),
                        object.get("CARD_UID").getAsString(),
                        object.get("pogingen").getAsInt());
                break;
            case "MAXIMAAL_AANTAL_POGINGEN":
                return new JsonResponses.PincodePogingenOverschreven(object.get("type").getAsString(),
                        object.get("IBAN").getAsString(),
                        object.get("transactionId").getAsString(),
                        object.get("CARD_UID").getAsString());
                break;
        }
    }

    public JsonResponse saldoOpvragen(String transactionID, String IBAN) {
        JsonObject object = new JsonObject();
        object.addProperty("transactionId", transactionID);
        object.addProperty("IBAN", IBAN);
        object.addProperty("type","SALDO_OPVRAGEN");
        String reaction = getSSLReaction(object.toString());
        object = new JsonParser().parse(reaction);
        return new JsonResponses.SaldoInformatie(object.get("type").getAsString(),
                object.get("IBAN").getAsString(),
                object.get("transactionId").getAsString(),
                object.get("saldo").getAsDouble());
    }

    public JsonResponse gewensteOpnameHoeveelheid(String transactionID, String IBAN, int hoeveelheid) {
        JsonObject object = new JsonObject();
        object.addProperty("type", "GEWENSTE_OPNAME_HOEVEELHEID");
        object.addProperty("trasactionId", transactionID);
        object.addProperty("IBAN", IBAN);
        object.addProperty("hoeveelheid", hoeveelheid);
        String reaction = getSSLReaction(object.toString());
        object = new JsonParser().parse(reaction);

    }
    private String getSSLReaction(String json) {
        System.out.println("Sending this to the server:"+json);
        SSLClient ssl = new SSLClient(json);
        ssl.start();
        while(ssl.isAlive()) {}
        //System.out.println(ssl.getReaction());
        return ssl.getReaction();
    }
}
