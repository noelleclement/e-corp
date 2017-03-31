package apis;

public class MockDB implements DatabaseInf {

    public double getBalance(String rekeningNr){

        return 0;
    }

    public boolean getWithdraw(String rekeningNr, int amount){

        return false;
    }

    @Override
    public boolean comparePasRekening(String rekeningNr, String PasNr) {
        return true;
    }

    @Override
    public boolean comparePincode(String PasNr, String pinCode) {
        return true;
    }

    @Override
    public boolean withdrawPossible(String rekeningNr, int amount) {
        return false;
    }


}