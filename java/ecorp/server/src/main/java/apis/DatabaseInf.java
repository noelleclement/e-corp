package apis;

public interface DatabaseInf{

    int getBalance(String rekeningNr);
    boolean getWithdraw(String rekeningNr, int amount);

}