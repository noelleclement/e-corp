package apis;

public class MockDB implements DatabaseInf {

    public double getBalance(String rekeningNr){

        return 500;
    }

    public boolean getWithdraw(String rekeningNr, int amount){

        return amount<=500;
    }


    public boolean comparePasRekening(String rekeningNr, String pasNr) {

        return true;
    }


    public int comparePincode(String pasNr, String pincode) {
        return 0;
    }


    public boolean withdrawpossible(String rekeningNr, int amount) {
        return false;
    }


    public boolean withdrawPossible(String rekeningNr, int amount) {
        return amount<=500;
    }


    public double getSaldo(String rekeningNr) {
        return 0;
    }


    public boolean checkSaldo(String rekeningNr, int amount) {
        return false;
    }


    public int withdrawPossible(String rekeningNr, String pasNr, int amount) {
        return 0;
    }


    public boolean withDraw(String rekeningNr, String pasNr, int amount) {
        return false;
    }


    public boolean getGeblokkeerd(String pasNr) {
        return false;
    }


    public boolean setGeblokkeerd(String pasNr) {
        return false;
    }


    public int getFoutief(String pasNr) {
        return 0;
    }


    public boolean setFoutief(String pasNr, boolean pincodeTrue) {
        return false;
    }


    public boolean checkDaglimiet(String pasNr, int amount) {
        return false;
    }


    public double getDagTotaal(String pasNr) {
        return 0;
    }


    public boolean checkPasRekening(String rekeningNr, String pasNr) {
        return false;
    }


    public boolean checkPincode(String pasNr, String pincode) {
        return false;
    }
}