package apis;

import com.google.gson.*;
/**
 * Created by Joost Jonkers on 24-3-2017.
 */
public class API {

    private DatabaseInf database;

    public API() {

        this.database = new MockDB();

    }

    public String parse(String jsonLine) {

        JsonElement jelement = new JsonParser().parse(jsonLine);
        JsonObject object = jelement.getAsJsonObject();
        String type = object.get("type").getAsString();
        System.out.println(type);
        if (type.equals("CONTROLEER_REKENINGNUMMER")){
            return this.controleerRekeningNummer(object);
        } else if (type.equals("CONTROLEER_PINCODE")) {
            return this.controleerPinCode(object);
        } else if (type.equals("SALDO_OPVRAGEN")) {
            return this.vraagSaldo(object);
        } else if (type.equals("GEWENSTE_OPNAME_HOEVEELHEID")) {
            return this.opnameHoeveelheidVragen(object);
        } else {
            System.out.println("Onbekend ding");
            return "";
        }

    }

    public String controleerRekeningNummer(JsonObject object){
        System.out.println("Controleer reknr");
        boolean reknrpasinvoer = database.comparePasRekening(object.get("IBAN").getAsString(), object.get("CARD_UID").getAsString());

        if (reknrpasinvoer){
                System.out.println("Goedrekjnr");
                JsonObject result = new JsonObject();
                result.addProperty("transactionId", "123");
                result.addProperty("IBAN", object.get("IBAN").getAsString());
                result.addProperty("type", "CORRECT_REKENINGNUMMER" );
                result.addProperty("CARD_UID", object.get("CARD_UID").getAsString());
                System.out.println(result);
                return result.toString();

            } else {
                System.out.println("Fout reknr");
                JsonObject result = new JsonObject();
                result.addProperty("transactionId", "123");
                result.addProperty("IBAN", object.get("IBAN").getAsString());
                result.addProperty("type", "INCORRECT_REKENINGNUMMER" );
                result.addProperty("CARD_UID", "0");//object.get("CARDUID").getAsString());
            //TODO carduid from request

            System.out.println("Fout reknr");
                System.out.println(result.toString());
                return result.toString();

            }
    }

    public String controleerPinCode(JsonObject object){

        boolean pinCodeInvoer = database.comparePincode(object.get("CARD_UID").getAsString(), object.get("pinCode").getAsString());
            if (pinCodeInvoer) {

                JsonObject result = new JsonObject();
                result.addProperty("transactionId", "123");
                result.addProperty("IBAN", object.get("IBAN").getAsString());
                result.addProperty("type", "CORRECTE_PINCODE");
                result.addProperty("CARD_UID", object.get("CARD_UID").getAsString());
                return result.toString();

            } else {

                JsonObject result = new JsonObject();
                result.addProperty("transactionId", "123");
                result.addProperty("IBAN", object.get("IBAN").getAsString());
                result.addProperty("type", "INCORRECTE_PINCODE");
                result.addProperty("CARD_UID", object.get("CARD_UID").getAsString());
                result.addProperty("pogingen", 2);
                return result.toString();

        }
    }

    public String vraagSaldo(JsonObject object){

        double saldoOpvragen = database.getBalance(object.get("IBAN").getAsString());

            JsonObject result = new JsonObject();
            result.addProperty("transactionId", "123");
            result.addProperty("IBAN", object.get("IBAN").getAsString());
            result.addProperty("type", "SALDO_INFORMATIE");
            result.addProperty("saldo", saldoOpvragen);
            return result.toString();

    }

    public String infoSaldo(JsonObject object) {

        double saldoInfo = database.getBalance(object.get("Saldo").getAsString());

            JsonObject result = new JsonObject();
            result.addProperty("transactionId", "123");
            result.addProperty("IBAN", object.get("IBAN").getAsString());
            result.addProperty("type", "SALDO_INFORMATIE");
            result.addProperty("saldo", "123.45");
            return result.toString();

    }

    public String opnameHoeveelheidVragen(JsonObject object){

        boolean hoeveelheidOpname = database.withdrawPossible(object.get("IBAN").getAsString(),object.get("hoeveelheid").getAsInt());
        if(hoeveelheidOpname) {
            JsonObject result = new JsonObject();
            result.addProperty("transactionId", "123");
            result.addProperty("IBAN", object.get("IBAN").getAsString());
            result.addProperty("type", "OPNAME_IS_MOGELIJK");
            result.addProperty("hoeveelheid", object.get("hoeveelheid").getAsBigInteger());
            return result.toString();
        }else {

            JsonObject result = new JsonObject();
            result.addProperty("transactionId", "123");
            result.addProperty("IBAN", object.get("IBAN").getAsString());
            result.addProperty("type", "ONTOEREIKEND_SALDO");
            result.addProperty("saldo", database.getBalance(object.get("IBAN").getAsString()));
            return result.toString();
        }

    }

    public String geldOpname(JsonObject object){

        boolean opnameGeld = database.getWithdraw(object.get("IBAN").getAsString(), object.get("amount").getAsInt());

        if (opnameGeld) {

            JsonObject result = new JsonObject();
            result.addProperty("transactionId", "123");
            result.addProperty("IBAN", object.get("IBAN").getAsString());
            result.addProperty("type", "OPNAME_IS_MOGELIJK");
            result.addProperty("hoeveelheid", "12");
            return result.toString();

        /*} else if {

            JsonObject result = new JsonObject;
            result.addProperty("transactionId", "123");
            result.addProperty("IBAN", object.get("IBAN").getAsString());
            result.addProperty("type", "HOGER_DAN_DAGLIMIET");
            result.addProperty("daglimiet", "110");
            return result.toString();

        */} else {

            JsonObject result = new JsonObject();
            result.addProperty("transactionId", "123");
            result.addProperty("IBAN", object.get("IBAN").getAsString());
            result.addProperty("type", "ONTOEREIKEND_SALDO");
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
            */





}