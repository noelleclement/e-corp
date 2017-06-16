package apis;

import com.google.gson.*;
import jdk.nashorn.internal.ir.debug.JSONWriter;

/**
 * Created by Hans de Rooij on 08/06/2017.
 */
public class LandApi {

    private static DatabaseInf database;

    public LandApi(DatabaseInf database) {

        this.database = database;

    }

    public String parse(String jsonLine) {

        JsonElement jelement = new JsonParser().parse(jsonLine);
        JsonObject object = jelement.getAsJsonObject();
        String type = "hoi";
        if (object.get("RequestCode").getAsInt() == 0) {
            return this.controleerRekeningNummer(object);
        } else if (object.get("RequestCode").getAsInt() == 1) {
            return this.controleerPinCode(object);
        } else if (object.get("RequestCode").getAsInt() == 5) {
            return this.vraagSaldo(object);
        } else if (object.get("RequestCode").getAsInt() == 6) {
            return this.vraagDaglimiet(object);
        } else if (object.get("RequestCode").getAsInt() == 8) {
            return this.pincodePogingen(object);
        } else if (type.equals("GEWENSTE_OPNAME_HOEVEELHEID")) {
            return this.opnameHoeveelheidVragen(object);
        } else if (type.equals("GELD_OPNEMEN")) {
            return this.geldOpname(object);

        } else {
            System.out.println("Onbekend ding");
            return "";
        }

    }



    private String pincodePogingen(JsonObject object) {
        object = object.get("RequestData").getAsJsonObject();
        int pogingen = database.getFoutief(object.get("Card_UID").getAsString());
        JsonObject result = new JsonObject();
        result.addProperty("RequestCode", 8);
        JsonObject data = new JsonObject();
            data.addProperty("Attempts", pogingen);
        JsonArray array = new JsonArray();
        array.add(data);
        result.add("ResponseData", array);
        return result.toString();
    }
    //Bijgewerkt
    public String controleerRekeningNummer(JsonObject object){
        System.out.println("Controleer reknr");
        boolean reknrpasinvoer = database.checkPasRekening(
                object.get("RequestData").getAsJsonObject().get("IBAN").getAsString(),
                object.get("RequestData").getAsJsonObject().get("Card_UID").getAsString());

        if (reknrpasinvoer){
            if(database.getGeblokkeerd(
                    object.get("RequestData").getAsJsonObject().get("Card_UID").getAsString())) {
                /*System.out.println("pas geblokkeerd");
                JsonObject result = new JsonObject();
                result.addProperty("RequestCode", 0);
                result.addProperty("ResponseCode", 0);
                String data = "{\"Card_UID:\"\"\",\"IBAN\":,\"Account_Nr\":0}";
                result.addProperty("ResponseData", data);
                System.out.println(result.toString());
                return result.toString();*/
                System.out.println("geblokkeerde pas");
                String result = "{RequestCode:0,ResponseCode:0,ResponseData:[{\"Card_UID\":\"\",\"IBAN\":-1,\"Account_Nr\":-1,\"CustomerId\":-1}]}";
                return result;
            } else {
                System.out.println("Goedrekjnr");
                JsonObject result = new JsonObject();
                result.addProperty("RequestCode", 0);
                result.addProperty("ResponseCode", 0);
                JsonObject data = new JsonObject();
                data.addProperty("Card_UID", object.get("RequestData").getAsJsonObject().get("Card_UID").getAsString());
                data.addProperty("IBAN", object.get("RequestData").getAsJsonObject().get("IBAN").getAsString());
                data.addProperty("Account_Nr", 0);
                data.addProperty("CustomerId", 666);
                JsonArray array = new JsonArray();
                array.add(data);
                result.add("ResponseData", array);
                System.out.println(result);
                return result.toString();
            }

        }else{
            System.out.println("Fout reknr");
            String result = "{RequestCode:0,ResponseCode:0,ResponseData:[{\"Card_UID\":\"\",\"IBAN\":-1,\"Account_Nr\":-1,\"CustomerId\":-1}]}";
            return result;

        }
    }
    //Bijgewerkt
    public String controleerPinCode(JsonObject object){
        System.out.println("Pincode");
        boolean pinCodeInvoer = database.checkPincode(
                object.get("RequestData").getAsJsonObject().get("Card_UID").getAsString(),
                object.get("RequestData").getAsJsonObject().get("PinCode").getAsString());
        if(pinCodeInvoer) {
            System.out.println("Correcte Pincode");
            JsonObject result = new JsonObject();
            result.addProperty("RequestCode", 1);
            result.addProperty("ResultCode", 0);
            JsonObject data = new JsonObject();
                data.addProperty("ResultCode", 1);
            JsonArray array = new JsonArray();
            array.add(data);
            result.add("ResponseData", array);
            return result.toString();
        } else {
            System.out.println("incorrecte pincode");
            JsonObject result = new JsonObject();
            if(database.getGeblokkeerd(
                    object.get("RequestData").getAsJsonObject().get("Card_UID").getAsString())) {
                result.addProperty("RequestCode", 1);
                result.addProperty("ResultCode", 0);
                JsonObject data = new JsonObject();
                    data.addProperty("ResultCode", 2);
                JsonArray array = new JsonArray();
                array.add(data);
                result.add("ResponseData", array);
            } else {
                result.addProperty("RequestCode", 1);
                result.addProperty("ResponseCode", 0);
                JsonObject data = new JsonObject();
                    data.addProperty("ResultCode", 0);
                JsonArray array = new JsonArray();
                array.add(data);
                result.add("ResponseData", array);
            }
            return result.toString();
        }
    }
    //Bijgewerkt
    public String vraagSaldo(JsonObject object){
        object = object.get("RequestData").getAsJsonObject();
        double saldoOpvragen = database.getSaldo(object.get("IBAN").getAsString());

        JsonObject result = new JsonObject();
        result.addProperty("RequestCode", 5);
        result.addProperty("ResultCode", 0);
        JsonObject data = new JsonObject();
            data.addProperty("Balance", saldoOpvragen);
        JsonArray array = new JsonArray();
        array.add(data);
        result.add("ResponseData", array);
        return result.toString();

    }

    private String vraagDaglimiet(JsonObject object) {
        object = object.get("RequestData").getAsJsonObject();
        double daglimiet = 500 - database.getDagTotaal(object.get("IBAN").getAsString());
        object = new JsonObject();
        object.addProperty("RequestCode", 6);
        JsonObject data = new JsonObject();
            data.addProperty("Remaining", daglimiet);
        JsonArray array = new JsonArray();
        array.add(data);
        object.add("ResponseData", array);
        return object.toString();
    }
    //ouderwets man
    public String opnameHoeveelheidVragen(JsonObject object){
        int hoeveelheidOpname = database.withdrawPossible(object.get("IBAN").getAsString(),
                object.get("CARD_UID").getAsString(),
                object.get("hoeveelheid").getAsInt());
        System.out.println(hoeveelheidOpname);
        JsonObject result = new JsonObject();
        if(hoeveelheidOpname==3) {
            result.addProperty("transactionId", database.getLatestTransactieID(object.get("CARD_UID").getAsString()));
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
            result.addProperty("dagtotaal", database.getDagTotaal(object.get("CARD_UID").getAsString()));
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
            result.addProperty("transactionId", database.getLatestTransactieID(object.get("CARD_UID").getAsString()));
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
