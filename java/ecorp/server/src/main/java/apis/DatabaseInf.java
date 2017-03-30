package apis;

public interface DatabaseInf {

    int getBalance(String rekeningNr);
        //double return
    boolean getWithdraw(String rekeningNr, int amount);
        //zie notes
    boolean comparePasRekening(String rekeningNr, int pasNr);
        // klopt (0)
        // klopt niet (1)
        // pas geblokkeerd (2)
        //dus met ints
    boolean comparePincode(int pasNr, int pincode); //
        // (boolean: geslaagd)
            // (boolean: niet geslaagd + reden (check in database): errorcode
                    // (onjuiste pincode + aantal pogingen)
                    // (aantal pogingen overschreden + in database pas geblokkeerd)
    //      )
    // withdrawpossible(String rekeningNr, String amount);
            //opname mogelijk
            //saldo niet toereikend + saldo
            //daglimiet overschreden + wat van daglimiet nog over


    //sowieso bij alles kijken of variabelen overeenkomen met erd


}