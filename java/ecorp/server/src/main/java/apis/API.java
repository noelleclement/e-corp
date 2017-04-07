package apis;

import com.google.gson.*;

/**
 * Created by Joost Jonkers on 24-3-2017.
 */
public class API {

    private static DatabaseInf database;

    public API(DatabaseInf database) {

        this.database = database;

    }

    public String parse(String jsonLine) {

        JsonElement jelement = new JsonParser().parse(jsonLine);
        JsonObject object = jelement.getAsJsonObject();
        String type = object.get("type").getAsString();
        System.out.println(type);
        if (type.equals("CONTROLEER_REKENINGNUMMER")) {
            return this.controleerRekeningNummer(object);
        } else if (type.equals("CONTROLEER_PINCODE")) {
            return this.controleerPinCode(object);
        } else if (type.equals("SALDO_OPVRAGEN")) {
            return this.vraagSaldo(object);
        } else if (type.equals("GEWENSTE_OPNAME_HOEVEELHEID")) {
            return this.opnameHoeveelheidVragen(object);
        } else if (type.equals("GELD_OPNEMEN")) {
            return this.geldOpname(object);

        } else {
            System.out.println("Onbekend ding");
            return "";
        }

    }

    public String controleerRekeningNummer(JsonObject object){
        System.out.println("Controleer reknr");
        boolean reknrpasinvoer = database.checkPasRekening(object.get("IBAN").getAsString(), object.get("CARD_UID").getAsString());

        if (reknrpasinvoer){
            System.out.println("Goedrekjnr");
            JsonObject result = new JsonObject();
            result.addProperty("transactionId", "123");
            result.addProperty("IBAN", object.get("IBAN").getAsString());
            result.addProperty("type", "CORRECT_REKENINGNUMMER" );
            result.addProperty("CARD_UID", object.get("CARD_UID").getAsString());
            System.out.println(result);
            return result.toString();

        } else if(database.getGeblokkeerd(object.get("CARD_UID").getAsString())) {
            System.out.println("Fout pasnr");
            JsonObject result = new JsonObject();
            result.addProperty("transactionId", "123");
            result.addProperty("IBAN", object.get("IBAN").getAsString());
            result.addProperty("type", "PAS_GEBLOKKEERD" );
            result.addProperty("CARD_UID", object.get("CARD_UID").getAsString());

            System.out.println(result.toString());
            return result.toString();
        }else{
            System.out.println("Fout reknr");
            JsonObject result = new JsonObject();
            result.addProperty("transactionId", "123");
            result.addProperty("IBAN", object.get("IBAN").getAsString());
            result.addProperty("type", "INCORRECT_REKENINGNUMMER" );
            result.addProperty("CARD_UID", object.get("CARD_UID").getAsString());
            //TODO carduid from request

            System.out.println("Fout reknr");
                System.out.println(result.toString());
                return result.toString();

        }
    }

    public String controleerPinCode(JsonObject object){

        boolean pinCodeInvoer = database.checkPincode(object.get("CARD_UID").getAsString(), object.get("pinCode").getAsString());
        if(pinCodeInvoer) {
            JsonObject result = new JsonObject();
            result.addProperty("transactionId", "123");
            result.addProperty("IBAN", object.get("IBAN").getAsString());
            result.addProperty("type", "CORRECTE_PINCODE");
            result.addProperty("CARD_UID", object.get("CARD_UID").getAsString());
            return result.toString();
        } else {

            JsonObject result = new JsonObject();
            if(database.getGeblokkeerd(object.get("CARD_UID").getAsString())) {
                result.addProperty("transactionId", "123");
                result.addProperty("IBAN", object.get("IBAN").getAsString());
                result.addProperty("type", "MAXIMAAL_AANTAL_POGINGEN");
                result.addProperty("CARD_UID", object.get("CARD_UID").getAsString());
                result.addProperty("pogingen", 0);
            } else {
                result.addProperty("transactionId", "123");
                result.addProperty("IBAN", object.get("IBAN").getAsString());
                result.addProperty("type", "INCORRECTE_PINCODE");
                result.addProperty("CARD_UID", object.get("CARD_UID").getAsString());
                result.addProperty("pogingen", database.getFoutief(object.get("CARD_UID").getAsString()));
            }
            return result.toString();
        }
    }

    public String vraagSaldo(JsonObject object){

        double saldoOpvragen = database.getSaldo(object.get("IBAN").getAsString());

            JsonObject result = new JsonObject();
            result.addProperty("transactionId", "123");
            result.addProperty("IBAN", object.get("IBAN").getAsString());
            result.addProperty("type", "SALDO_INFORMATIE");
            result.addProperty("saldo", saldoOpvragen);
            return result.toString();

    }

    public String opnameHoeveelheidVragen(JsonObject object){
        int hoeveelheidOpname = database.withdrawPossible(object.get("IBAN").getAsString(),
                object.get("CARD_UID").getAsString(),
                object.get("hoeveelheid").getAsInt());
        System.out.println(hoeveelheidOpname);
        JsonObject result = new JsonObject();
        if(hoeveelheidOpname==3) {
            result.addProperty("transactionId", "123");
            result.addProperty("IBAN", object.get("IBAN").getAsString());
            result.addProperty("type", "OPNAME_IS_MOGELIJK");
            result.addProperty("hoeveelheid", object.get("hoeveelheid").getAsBigInteger());
            return result.toString();

        }else if(hoeveelheidOpname==1){
            result.addProperty("transactionId", "123");
            result.addProperty("IBAN", object.get("IBAN").getAsString());
            result.addProperty("type", "PAS_GEBLOKKEERD");
            result.addProperty("CARD_UID", object.get("CARD_UID").getAsString());
            return result.toString();
        }else if(hoeveelheidOpname==2) {
            result.addProperty("transactionId", "123");
            result.addProperty("IBAN", object.get("IBAN").getAsString());
            result.addProperty("type", "HOGER_DAN_DAGLIMIET");
            result.addProperty("saldo", database.getDagTotaal(object.get("CARD_UID").getAsString()));
            return result.toString();
        }else if(hoeveelheidOpname==0) {
            result.addProperty("transactionId", "123");
            result.addProperty("IBAN", object.get("IBAN").getAsString());
            result.addProperty("type", "ONTOEREIKEND_SALDO");
            result.addProperty("saldo", database.getSaldo(object.get("IBAN").getAsString()));
            return result.toString();
        }
        return "{kak:1}";
    }

    public String geldOpname(JsonObject object){

        boolean opnameGeld = database.withDraw(object.get("IBAN").getAsString(),
                object.get("CARD_UID").getAsString(),
                object.get("amount").getAsInt());

        if (opnameGeld) {
            System.out.println("Geldopname Mogelijk");
            JsonObject result = new JsonObject();
            result.addProperty("transactionId", "123");
            result.addProperty("IBAN", object.get("IBAN").getAsString());
            result.addProperty("type", "OPNAME_IS_MOGELIJK");
            result.addProperty("hoeveelheid", object.get("amount").getAsInt());
            return result.toString();
        } else {
            System.out.println("Geldopname niet Mogelijk");
            JsonObject result = new JsonObject();
            result.addProperty("transactionId", "123");
            result.addProperty("IBAN", object.get("IBAN").getAsString());
            result.addProperty("type", "ONTOEREIKEND_SALDO");
            result.addProperty("saldo", database.getSaldo(object.get("IBAN").getAsString()));
            return result.toString();

        }
    }
}