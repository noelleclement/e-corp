package apis;

public interface DatabaseInf {

    double getBalance(String rekeningNr);
    boolean getWithdraw(String rekeningNr, int amount);
    boolean withdrawPossible(String rekeningNr, int amount);
    boolean comparePasRekening(String rekeningNr, String PasNr);

    boolean comparePincode(String PasNr, String pinCode);


}