package apis;

import com.google.gson.*;
/**
 * Created by Joost Jonkers on 24-3-2017.
 */
public class API {

    private Database database;

    public API() {

        this.database = new Database();

    }

    public String parse(String jsonLine) {

        JsonElement jelement = new JsonParser().parse(jsonLine);
        JsonObject object = jelement.getAsJsonObject();
        String type = object.get("type").getAsString();
        if (type == "CONTROLEER_REKENINGNUMMER"){
            return this.controleerRekeningNummer(object);
        } else if (type == "CONTROLEER_PINCODE") {
            return this.controleerPinCode(object);
        } else if (type == "vraagSaldo") {
            return this.vraagSaldo(object);
        } else if ()

    }

    public String controleerRekeningNummer(JsonObject object){

        boolean reknrpasinvoer database.comparePasRekening(object.get("IBAN").getAsString());
            if (reknrpasinvoer){

                JsonObject result = new JsonObject;
                result.addProperty("transactionId", "123");
                result.addProperty("IBAN", object.get("IBAN").getAsString());
                result.addProperty("type", "CORRECT REKENINGNUMMER" );
                result.addProperty("CARD_UID", object.get("CARDUID").getAsString());
                return result.toString();

            } else {

                JsonObject result = new JsonObject;
                result.addProperty("transactionId", "123");
                result.addProperty("IBAN", object.get("IBAN").getAsString());
                result.addProperty("type", "INCORRECT REKENINGNUMMER" );
                result.addProperty("CARD_UID", object.get("CARDUID").getAsString());
                return result.toString();

            }
    }

    public String controleerPinCode(JsonObject object){

        boolean pinCodeInvoer database.comparePincode(object.get("CARD_UID").getAsString(), object.get("pinCode").getAsString());
            if (pinCodeInvoer) {

                JsonObject result = new JsonObject;
                result.addProperty("transactionId", "123");
                result.addProperty("IBAN", object.get("IBAN").getAsString());
                result.addProperty("type", "CORRECTE PINCODE");
                result.addProperty("CARD_UID", object.get("CARD_UID").getAsString());
                return result.toString();

            } else {

                JsonObject result = new JsonObject;
                result.addProperty("transactionId", "123");
                result.addProperty("IBAN", object.get("IBAN").getAsString());
                result.addProperty("type", "INCORRECTE PINCODE");
                result.addProperty("CARD_UID", object.get("CARD_UID").getAsString());
                return result.toString();

        }
    }

    public String vraagSaldo(JsonObject object){

        boolean saldoOpvragen database.getBalance(object.get("IBAN").getAsString());

            JsonObject result = new JsonObject;
            result.addProperty("transactionId", "123");
            result.addProperty("IBAN", object.get("IBAN").getAsString());
            result.addProperty("type", "SALDO_OPVRAGEN");
            return result.toString();

    }

    public String infoSaldo(JsonObject object) {

        boolean saldoInfo database.getBalance(object.get("Saldo").getAsString());

            JsonObject result = new JsonObject;
            result.addProperty("transactionId", "123");
            result.addProperty("IBAN", object.get("IBAN").getAsString());
            result.addProperty("type", "SALDO_INFORMATIE");
            result.addProperty("saldo", "123.45");
            return result.toString();

    }

    public String opnameHoeveelheidVragen(JsonObject object){

        boolean hoeveelheidOpname database.getWithdraw(object.get("amount").getAsString());

            JsonObject result = new JsonObject;
            result.addProperty("transactionId", "123");
            result.addProperty("IBAN", object.get("IBAN").getAsString());
            result.addProperty("type", "GEWENSTE_OPNAME_HOEVEELHEID");
            result.addProperty("hoeveelheid", "12");
            return result.toString();

    }

    public String geldOpname(JsonObject object){

        boolean opnameGeld database.getWithdraw(object.get(amount).getAsInt());

            if (opnameGeld > amount) {

                JsonObject result = new JsonObject;
                result.addProperty("transactionId", "123");
                result.addProperty("IBAN", object.get("IBAN").getAsString());
                result.addProperty("type", "OPNAME_IS_MOGELIJK");
                result.addProperty("hoeveelheid", "12");
                return result.toString();

            } else if {

                JsonObject result = new JsonObject;
                result.addProperty("transactionId", "123");
                result.addProperty("IBAN", object.get("IBAN").getAsString());
                result.addProperty("type", "HOGER_DAN_DAGLIMIET");
                result.addProperty("daglimiet", "110");
                return result.toString();

            } else {

                JsonObject result = new JsonObject;
                result.addProperty("transactionId", "123");
                result.addProperty("IBAN", object.get("IBAN").getAsString());
                result.addProperty("type", "ONTOEREIKEND SALDO");
                result.addProperty("saldo", "123.45");
                return result.toString();

            }
    }

    /*public String transactieSTATUS(JsonObject object){

        boolean statusTransactie database.getWithdraw(object.get("amount").getAsString());

            if (statusTransactie) {

                JsonObject result = new JsonObject;
                result.addProperty("transactionId", "123");
                result.addProperty("IBAN", object.get("IBAN").getAsString());
                result.addProperty("type", "TRANSACTIE_ONDERBROKEN");
                return result.toString();

            } else if {

                JsonObject result = new JsonObject;
                result.addProperty("transactionId", "123");
                result.addProperty("IBAN", object.get("IBAN").getAsString());
                result.addProperty("type", "TRANSACTIE_AFGESLOTEN");
                return result.toString();

            } else if {

                JsonObject result = new JsonObject;
                result.addProperty("transactionId", "123");
                result.addProperty("IBAN", object.get("IBAN").getAsString());
                result.addProperty("type", "TRANSACTIE_TIMEOUT");
                return result.toString();

            } else if {

                JsonObject result = new JsonObject;
                result.addProperty("transactionId", "123");
                result.addProperty("IBAN", object.get("IBAN").getAsString());
                result.addProperty("type", "TRANSACTIENUMMER_ONBEKEND");
                result.addProperty("CARD_UID", "ac30df");
                return result.toString();
            }
            /*





}