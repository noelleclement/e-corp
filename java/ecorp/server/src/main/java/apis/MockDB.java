package apis;

public class MockDB implements DatabaseInf {

    public double getBalance(String rekeningNr){

        return 500;
    }

    public boolean getWithdraw(String rekeningNr, int amount){

        return amount<=500;
    }

    @Override
    public boolean comparePasRekening(String rekeningNr, String pasNr) {

        return true;
    }

    @Override
    public int comparePincode(String pasNr, String pincode) {
        return pincode.equals("1234")?0:1;
    }

    @Override
    public boolean withdrawpossible(String rekeningNr, int amount) {
        return false;
    }

    @Override
    public boolean withdrawPossible(String rekeningNr, int amount) {
        return amount<=500;
    }

    @Override
    public double getSaldo(String rekeningNr) {
        return 0;
    }

    @Override
    public boolean checkSaldo(String rekeningNr, int amount) {
        return false;
    }

    @Override
    public int withdrawPossible(String rekeningNr, String pasNr, int amount) {
        return 0;
    }

    @Override
    public boolean withDraw(String rekeningNr, String pasNr, int amount) {
        return false;
    }

    @Override
    public boolean getGeblokkeerd(String pasNr) {
        return false;
    }

    @Override
    public boolean setGeblokkeerd(String pasNr) {
        return false;
    }

    @Override
    public int getFoutief(String pasNr) {
        return 0;
    }

    @Override
    public boolean setFoutief(String pasNr, boolean pincodeTrue) {
        return false;
    }

    @Override
    public boolean checkDaglimiet(String pasNr, int amount) {
        return false;
    }

    @Override
    public double getDagTotaal(String pasNr) {
        return 0;
    }

    @Override
    public boolean checkPasRekening(String rekeningNr, String pasNr) {
        return false;
    }

    @Override
    public boolean checkPincode(String pasNr, String pincode) {
        return false;
    }
}