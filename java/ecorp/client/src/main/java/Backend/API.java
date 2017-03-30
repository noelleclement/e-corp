package Backend;

import SSL.SSLClient;
import com.google.gson.*;

/**
 * Created by Hans de Rooij on 24/03/2017.
 */
public class API {
    public JsonResponses.ControleerRekeningnummer isCorrectCard(String accountNumber, String passNumber) {
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
        if(object.get("type").getAsString().equals("CORRECTE_PINCODE")) {
            return new JsonResponses.CorrectePincode(object.get("type").getAsString(),
                    object.get("IBAN").getAsString(),
                    object.get("transactionId").getAsString(),
                    object.get("CARD_UID").getAsString());
        }else if(object.get("type").getAsString().equals("INCORRECTE_PINCODE")) {
            return new JsonResponses.IncorrectePincode(object.get("type").getAsString(),
                    object.get("IBAN").getAsString(),
                    object.get("transactionId").getAsString(),
                    object.get("CARD_UID").getAsString(),
                    object.get("pogingen").getAsInt());
        }else if(object.get("type").getAsString().equals("MAXIMAAL_AANTAL_POGINGEN")) {
            return new JsonResponses.PincodePogingenOverschreven(object.get("type").getAsString(),
                    object.get("IBAN").getAsString(),
                    object.get("transactionId").getAsString(),
                    object.get("CARD_UID").getAsString());
        }else{
            System.out.println("Onbekende reactie op pincodecheck");
            return new JsonResponses.PincodePogingenOverschreven(object.get("type").getAsString(),
                    object.get("IBAN").getAsString(),
                    object.get("transactionId").getAsString(),
                    object.get("CARD_UID").getAsString());
        }
    }

    public JsonResponses.SaldoInformatie saldoOpvragen(String transactionID, String IBAN) {
        JsonObject object = new JsonObject();
        object.addProperty("transactionId", transactionID);
        object.addProperty("IBAN", IBAN);
        object.addProperty("type","SALDO_OPVRAGEN");
        String reaction = getSSLReaction(object.toString());
        JsonElement element = new JsonParser().parse(reaction);
        object = element.getAsJsonObject();
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
        JsonElement element = new JsonParser().parse(reaction);
        object = element.getAsJsonObject();

        if(object.get("type").getAsString().equals("OPNAME_IS_MOGELIJK")) {
            return new JsonResponses.GeldopnameMogelijk(object.get("transactionId").getAsString(),
                    object.get("IBAN").getAsString(),
                    object.get("hoeveelheid").getAsInt());
        }else if(object.get("type").getAsString().equals("HOGER_DAN_DAGLIMIET")) {
            return new JsonResponses.OpnameHogerDanDaglimiet(object.get("transactionID").getAsString(),
                    object.get("IBAN").getAsString(),
                    object.get("daglimiet").getAsInt());
        }else if(object.get("type").getAsString().equals("ONTOEREIKEND_SALDO")) {
            return new JsonResponses.OntoereikendSaldo(object.get("transactionId").getAsString(),
                    object.get("IBAN").getAsString(),
                    object.get("saldo").getAsDouble());
        }else{
            System.out.println("Onbekende reactie op opnamehoeveelheid");
            return new JsonResponses.OntoereikendSaldo(object.get("transactionId").getAsString(),
                    object.get("IBAN").getAsString(),
                    object.get("saldo").getAsDouble());
        }
    }
    private String getSSLReaction(String json) {
        System.out.println("Sending this to the server:"+json);
        SSLClient ssl = new SSLClient(json);
        ssl.start();
        while(ssl.isAlive()) {}
        System.out.println("Server reaction:"+ssl.getReaction());
        return ssl.getReaction();
    }
}
