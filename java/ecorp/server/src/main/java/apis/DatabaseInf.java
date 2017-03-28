package apis;

public interface DatabaseInf{

    int getBalance(String rekeningNr);
    boolean getWithdraw(String rekeningNr, int amount);
    boolean comparePasRekening(String rekeningNr, String PasNr);
    boolean comparePincode(String PasNr, String pinCode);
    boolean withdrawPossible(String rekeningNr, int amount);

}