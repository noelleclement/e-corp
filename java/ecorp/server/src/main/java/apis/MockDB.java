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

}