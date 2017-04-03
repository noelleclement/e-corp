package apis;

public interface DatabaseInf {

    double getSaldo(String rekeningNr);
    //saldo opvragen database


    boolean checkSaldo(String rekeningNr, int amount);
    //checken of saldo>amount
    //true als goed, false als te weinig


    int withdrawPossible (String rekeningNr, String pasNr, int amount);
    //checkSaldo(), getGeblokkeerd(), checkDaglimiet()
    //hier zouden we wel met int kunnen werken voor als iets niet boolean terug geeft
    //0 checkSaldo false, 1 getGeblokkeerd true, 2 checkDaglimiet false, 3 alles goed

    boolean withDraw(String rekeningNr, String pasNr, int amount);
    //withdrawPossible() als 0 1 of 2 doet withdrawPossible om reden te checken, withdrawPossible() 3 withdraw


    boolean getGeblokkeerd(String pasNr);
    //geblokkeerd aanvragen bij database
    //true als geblokkeerd, false als niet geblokkeerd

    boolean setGeblokkeerd(String pasNr);
    //pas blokkeren
    //true als gelukt, false als niet gelukt

    int getFoutief(String pasNr);
    //foutieve pogingen aanvragen bij database
    //return aantal foutieve pogingen

    boolean setFoutief (String pasNr, boolean pincodeTrue);
    //foutief +1 als pincodeTrue false en als getFoutief() 0 of 1,
    //      als getFoutief() 2 foutief op 3 en setGeblokkeerd() pas blokkeren,
    //foutief 0 als pincodeTrue true


    boolean checkDaglimiet (String pasNr, int amount);
    //getDaglimiet()>amount
    //true als goed, false als te weinig

    double getDagTotaal (String pasNr);
    //500- transacties uit database met pasnr van deze dag
    //return daglimiet over


    boolean checkPasRekening(String rekeningNr, String pasNr);
    //checken of combi in database
    //true als bestaat, false als niet bestaat

    boolean checkPincode(String pasNr, String pincode);
    //checken of pincode klopt
    //goed setFoutief() 0 boolean true
    //fout setFoutief() boolean false


    //boolean bij setters want je wilt bevestiging of t gelukt is of niet

    //TODO alle transactie dingen

}