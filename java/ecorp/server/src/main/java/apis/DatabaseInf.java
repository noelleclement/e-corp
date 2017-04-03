package apis;

public interface DatabaseInf {

    double getBalance(String rekeningNr);
        //double return
    boolean getWithdraw(String rekeningNr, int amount);
        //zie notes
    boolean comparePasRekening(String rekeningNr, String pasNr);
        // klopt (0)
        // klopt niet (1)
        // pas geblokkeerd (2)
        //dus met ints
    int comparePincode(String pasNr, String pincode); //
        // (boolean: geslaagd)
            // (boolean: niet geslaagd + reden (check in database): errorcode
                    // (onjuiste pincode + aantal pogingen)
                    // (aantal pogingen overschreden + in database pas geblokkeerd)
    //      )
    boolean withdrawpossible(String rekeningNr, int amount);

    boolean withdrawPossible(String rekeningNr, int amount);
    //opname mogelijk
            //saldo niet toereikend + saldo
            //daglimiet overschreden + wat van daglimiet nog over


    //sowieso bij alles kijken of variabelen overeenkomen met erd


}