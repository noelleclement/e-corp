package apis;

class MockDB implements DatabaseInf {

    public int getBalance(String rekeningNr){

        return 0;
    }

    public boolean getWithdraw(String rekeningNr, int amount){

        return false;
    }
}