package Backend;

import SSL.SSLClient;
import com.google.gson.*;

/**
 * Created by Hans de Rooij on 29/05/2017.
 */
public class LandAPI {

    //bijgewerkt
    public JsonResponses.ControleerRekeningnummer isCorrectCard(String IBAN, String passNumber) {
        JsonObject object = new JsonObject();
        object.addProperty("RequestCode", 0);
        JsonObject data = new JsonObject();
        data.addProperty("Card_UID", passNumber);
        data.addProperty("IBAN", IBAN);
        object.add("RequestData", data);
        String reaction = getSSLReaction(object.toString());
        JsonElement element = new JsonParser().parse(reaction);
        object = element.getAsJsonObject();
        data = object.get("ResponseData").getAsJsonArray().get(0).getAsJsonObject();
        System.out.println(data);
        return new JsonResponses.ControleerRekeningnummer(
                object.get("ResponseCode").getAsString(),
                IBAN,
                "TransactiIdEen", //object.get("transactionId").getAsString(),
                data.get("Card_UID").getAsString(),
                data.get("CustomerId").getAsInt(),
                data.get("Account_Nr").getAsInt());
    }
    //bijgewerkt
    public JsonResponse checkPinCode(String transactionID, String IBAN, String pinCode, String card_uid, int CustomerId, int AccountNr) {
        System.out.println("pincode:"+pinCode);
        JsonObject object = new JsonObject();
        object.addProperty("RequestCode", 1);
        JsonObject data = new JsonObject();
            data.addProperty("Card_UID", card_uid);
            data.addProperty("IBAN", IBAN);
            data.addProperty("PinCode", Integer.parseInt(pinCode));
            data.addProperty("CustomerId", CustomerId);
        object.add("RequestData", data);
        String reaction = getSSLReaction(object.toString());
        JsonElement element = new JsonParser().parse(reaction);

        object = element.getAsJsonObject().get("ResponseData").getAsJsonArray().get(0).getAsJsonObject();

        if(object.get("ResultCode").getAsInt() == 1) {
            return new JsonResponses.CorrectePincode(
                    "CORRECTE_PINCODE",
                    IBAN,
                    transactionID,
                    card_uid);
        }else if(object.get("ResultCode").getAsInt() == 0) {

            return new JsonResponses.IncorrectePincode(
                    "INCORRECTE_PINCODE",
                    IBAN,
                    "123",
                    card_uid,
                    this.getFailedPinAttempts(AccountNr, CustomerId, IBAN, card_uid));
        }else if(object.get("ResultCode").getAsInt() == 2) {
            return new JsonResponses.PincodePogingenOverschreven(
                    "MAXIMAAL_AANTAL_POGINGEN",
                    IBAN,
                    transactionID,
                    card_uid);
        }else{
            System.out.println("Onbekende reactie op pincodecheck");
            return new JsonResponses.PincodePogingenOverschreven(object.get("type").getAsString(),
                    object.get("IBAN").getAsString(),
                    object.get("transactionId").getAsString(),
                    object.get("CARD_UID").getAsString());
        }
    }
    //bijgewerkt
    private int getFailedPinAttempts(int accountNumber, int customerId, String IBAN, String card_uid) {
        JsonObject object = new JsonObject();
        object.addProperty("RequestCode", 8);
        JsonObject data = new JsonObject();
            data.addProperty("AccountNr", accountNumber);
            data.addProperty("CustomerId", customerId);
            data.addProperty("Card_UID", card_uid);
            data.addProperty("IBAN", IBAN);
        object.add("RequestData", data);
        JsonElement response = new JsonParser().parse(getSSLReaction(object.toString()));
        return response.getAsJsonObject().get("ResponseData").getAsJsonArray().get(0).getAsJsonObject().get("Attempts").getAsInt();
    }
    //bijgewerkt
    public JsonResponses.SaldoInformatie saldoOpvragen(int accountNr, String IBAN) {
        JsonObject object = new JsonObject();
        object.addProperty("RequestCode", 5);
        JsonObject data = new JsonObject();
            data.addProperty("IBAN", IBAN);
            data.addProperty("AccountNr", accountNr);
        object.add("RequestData", data);
        String reaction = getSSLReaction(object.toString());
        JsonElement element = new JsonParser().parse(reaction);
        object = element.getAsJsonObject().get("ResponseData").getAsJsonArray().get(0).getAsJsonObject();
        return new JsonResponses.SaldoInformatie("5",
                IBAN,
                Integer.toString(accountNr),
                object.get("Balance").getAsDouble());
    }
    //onnodig tegenwoordig
    public JsonResponse gewensteOpnameHoeveelheid(String transactionID, String IBAN, int hoeveelheid, String CARD_uid, int accountNr) {
        JsonObject object = new JsonObject();
        object.addProperty("RequestCode", 5);
        JsonObject data = new JsonObject();
            data.addProperty("IBAN", IBAN);
            data.addProperty("AccountNr", accountNr);
        object.add("RequestData", data);
        String reaction = getSSLReaction(object.toString());
        JsonElement element = new JsonParser().parse(reaction);
        object = element.getAsJsonObject().get("ResponseData").getAsJsonArray().get(0).getAsJsonObject();
        if((object.get("Balance").getAsInt() - hoeveelheid) > 0) {
            object = new JsonObject();
            object.addProperty("RequestCode", 6);
            data = new JsonObject();
            data.addProperty("IBAN", IBAN);
            data.addProperty("AccountNr", accountNr);
            object.add("RequestData", data);
            reaction = getSSLReaction(object.toString());
            element = new JsonParser().parse(reaction);
            object = element.getAsJsonObject().get("ResponseData").getAsJsonArray().get(0).getAsJsonObject();
            if((object.get("Remaining").getAsDouble()) - hoeveelheid < 0) {
                return new JsonResponses.OpnameHogerDanDaglimiet(
                        "123",
                        IBAN,
                        object.get("Remaining").getAsDouble());
            } else {
                return new JsonResponses.GeldopnameMogelijk(
                        "123",
                        IBAN,
                        hoeveelheid);
            }
        }else {
            return new JsonResponses.OntoereikendSaldo(
                    "123",
                    IBAN,
                    object.get("Balance").getAsDouble());
        }

    }
    //bijgewerkt
    public double getDaglimietRemaining(String IBAN, int accountNr) {
        JsonObject object = new JsonObject();
        object.addProperty("RequestCode", 6);
        JsonObject data = new JsonObject();
        data.addProperty("IBAN", IBAN);
        data.addProperty("AccountNr", accountNr);
        object.add("RequestData", data);
        String reaction = getSSLReaction(object.toString());
        JsonElement element = new JsonParser().parse(reaction);
        object = element.getAsJsonObject().get("ResponseData").getAsJsonArray().get(0).getAsJsonObject();
        return object.get("Remaining").getAsDouble();
    }

    public JsonResponse geldOpnemen(String transactionID, String IBAN, int hoeveelheid, String CARD_UID, int accountNr, int customerId) {
        JsonObject object = new JsonObject();
        object.addProperty("RequestCode", 3);
        JsonObject data = new JsonObject();
            data.addProperty("AccountNr", accountNr);
            data.addProperty("CustomerId", customerId);
            data.addProperty("Amount", hoeveelheid);
            data.addProperty("IBAN", IBAN);
            data.addProperty("Card_UID", CARD_UID);
            data.addProperty("Location", "EVIL ATM -Wijnhaven 101-");
        object.add("RequestData", data);
        String result = getSSLReaction(object.toString());
        JsonElement element = new JsonParser().parse(result);
        object = element.getAsJsonObject().get("ResponseData").getAsJsonArray().get(0).getAsJsonObject();

        if(object.get("ResultCode").getAsInt() == 1) {
            return new JsonResponses.GeldopnameMogelijk(transactionID,
                    IBAN,
                    hoeveelheid);
        }else if(object.get("ResultCode").getAsInt() == 0) {
            return new JsonResponses.OpnameHogerDanDaglimiet(object.get("transactionID").getAsString(),
                    object.get("IBAN").getAsString(),
                    0);
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
        SSLClient ssl = new SSLClient(json);
        ssl.start();
        System.out.println("Sending this to the server:"+json);
        while(ssl.isAlive()) {}
        String reactie = ssl.getReaction();
        System.out.println("Server reaction:"+reactie);
        return reactie;
    }
}
