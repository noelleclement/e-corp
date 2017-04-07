package apis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;


/**
 * Created by Noelle on 22-03-17.
 */

public class Database implements DatabaseInf{

    private static final Logger logger = LoggerFactory.getLogger(Database.class);
    private Connection con;
    private String host;
    private String uName;
    private String uPass;
    private ResultSet rs;

    public Database() {

        this.con = null;
        this.host = "jdbc:mysql://localhost:3306/E-corp";
        this.uName = "root";
        this.uPass = "pandabeer1";

        this.connect();

    }

    private void connect() {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(host, uName, uPass);
            logger.info("Connection to the database is established");
        } catch (Exception e) {
            //moet nog meerdere exceptions voor alle aparte fouten die kunnen opkomen, voor elke aparte error andere log namelijk
            logger.error("Connection to database failed", e);
        }


    }

    public double getSaldo(String rekeningNr){
        double saldo = 0;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT saldo "
                                                            + "FROM rekening "
                                                            + "WHERE rekening.rekeningnr = ?");
            ps.setString(1, rekeningNr);
            rs = ps.executeQuery();
            rs.next();
            saldo = rs.getDouble("saldo");

            logger.debug("Saldo = " + saldo); //als handig kan ook rekeningnr etc toegevoegd worden

        } catch (SQLException e) {
            logger.error("Execution of query select saldo failed", e); //moet nog wat uitgebreider
        }
        return saldo;

    }


    public boolean checkSaldo(String rekeningNr, int amount){
        double saldo = getSaldo(rekeningNr);
        if (saldo > amount){
            return true;
        }
        return false;

    }

    /**
     *
     * @param rekeningNr
     * @param pasNr
     * @param amount
     * @return [0:saldo te laag|1:pas geblokkeerd|2:over daglimiet|3:top]
     */

    public int withdrawPossible(String rekeningNr, String pasNr, int amount){

        boolean result = checkSaldo(rekeningNr, amount);
        if(!result){

            if (logger.isDebugEnabled()) {
                logger.debug("checksaldo geeft false: saldo te weinig");
            }

            return 0;

        }

        result = getGeblokkeerd(pasNr);
        if(result){

            if (logger.isDebugEnabled()) {
                logger.debug("getGeblokkeerd geeft true: pas is geblokkeerd");
            }

            return 1;

        }

        result = checkDaglimiet(pasNr, amount);
        if(!result){

            if (logger.isDebugEnabled()) {
                logger.debug("checkDagLimiet geeft false: aanvraag is meer dan overgebleven daglimiet genoeg");
            }

            return 2;

        }

        return 3;
    }


    public boolean withDraw(String rekeningNr, String pasNr, int amount){

        try {
            double saldo = getSaldo(rekeningNr);
            boolean result2 = true;
            if (withdrawPossible(rekeningNr, pasNr, amount)==3) {
                //pinnen
                logger.debug("withDraw with Rekeningnr " + rekeningNr, "saldo: " + saldo);

                PreparedStatement ps = con.prepareStatement("UPDATE rekening "
                                                                + "SET saldo = ? "
                                                                + "WHERE rekeningNr = ?");

                ps.setDouble(1, (saldo - amount));
                ps.setString(2, rekeningNr);


                int resultex = ps.executeUpdate();
                if (resultex >= 1) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Nieuwe waarde 'fpoging'", getFoutief(pasNr));
                    }
                    return true;
                }
            }

            logger.debug("Saldo is ontoereikend, gebruik withdrawPossible() voor reden");
            return false;
        } catch (SQLException e) {
            logger.error("Execution of query withdraw failed", e);
        }

        return false;
    }



    public boolean getGeblokkeerd(String pasNr) {
        try{
            PreparedStatement ps = con.prepareStatement("SELECT geblokkeerd "
                                                            + "FROM pas "
                                                            + "WHERE pas.pasNr = ?");
            ps.setString(1, pasNr);
            rs = ps.executeQuery();
            rs.next();
            int geblokkeerd = rs.getInt("geblokkeerd");

            if(geblokkeerd==0){
                return false;                                   // pas niet geblokkeerd
            }


        } catch (SQLException e) {
            logger.error("Execution of query getGeblokkeerd failed", e);
        }

        return true;
    }


    public boolean setGeblokkeerd(String pasNr) {
        try{
            PreparedStatement ps = con.prepareStatement("UPDATE pas "
                                                            + "SET geblokkeerd = ? "
                                                            + "WHERE pasNr = ?");

            ps.setInt(1, 1);
            ps.setString(2, pasNr);

            int resultex = ps.executeUpdate();
            if (resultex >= 1){
                if (logger.isDebugEnabled()) {
                    logger.debug("Nieuwe waarde 'geblokkeerd'", getGeblokkeerd(pasNr));
                }
                return true;
            }

            return false;

        }catch(SQLException e){
            logger.error("Execution of query setGeblokkeerd failed", e);
        }

        return false;
    }


    public int getFoutief(String pasNr) {
        try{
            PreparedStatement ps = con.prepareStatement( "SELECT fpoging "
                                                            + "FROM pas "
                                                            + "WHERE pas.pasNr = ?");
            ps.setString(1, pasNr);
            rs = ps.executeQuery();
            rs.next();

            int result = rs.getInt("fpoging");
            System.out.println("aantal pogingen getFoutief opgehaald: "+result);
            return result;



        }catch(SQLException e){
            logger.error("Execution of query getFoutief failed", e);
        }

        return 0;
    }


    public boolean setFoutief(String pasNr, boolean pincodeTrue) {
        try{
            if(pincodeTrue){
                //set fpoging op 0
                PreparedStatement ps = con.prepareStatement("UPDATE pas "
                                                                + "SET fpoging = ?  "
                                                                + "WHERE pasNr = ?");

                ps.setInt(1, 0);
                ps.setString(2, pasNr);

                int resultex = ps.executeUpdate();
                if (resultex >= 1){
                    if (logger.isDebugEnabled()) {
                        logger.debug("Nieuwe waarde 'fpoging'", getFoutief(pasNr));
                    }
                    return true;
                }

                return false;

            }


            if(!pincodeTrue){
                int aantalFout = getFoutief(pasNr);

                if(aantalFout == 0 || aantalFout == 1){
                    //set fpoging aantalFout+1
                    PreparedStatement ps = con.prepareStatement("UPDATE pas "
                                                                    + "SET fpoging = ? "
                                                                    + "WHERE pasNr = ?");

                    ps.setInt(1, (aantalFout+1));
                    ps.setString(2, pasNr);

                    int resultex = ps.executeUpdate();
                    if (resultex >= 1){
                        if (logger.isDebugEnabled()) {
                            logger.debug("Nieuwe waarde 'fpoging'", getFoutief(pasNr));
                        }
                        return true;
                    }

                    return false;
                }


                if(aantalFout==2){
                    //set fpoging 3
                    PreparedStatement ps = con.prepareStatement("UPDATE pas "
                                                                    + "SET fpoging = ? "
                                                                    + "WHERE pasNr = ?");

                    ps.setInt(1, 3);
                    ps.setString(2, pasNr);

                    int resultex = ps.executeUpdate();
                    if (resultex >= 1){
                        boolean result = true;
                        if (logger.isDebugEnabled()) {
                            logger.debug("Nieuwe waarde 'fpoging'", getFoutief(pasNr));
                        }

                        //set geblokkeerd
                        result = setGeblokkeerd(pasNr);

                        if (logger.isDebugEnabled()) {
                            logger.debug("Nieuwe waarde 'geblokkeerd'", getGeblokkeerd(pasNr));
                        }

                        return result;
                    }

                    return false;


                }
            }


        }catch(SQLException e){
            logger.error("Execution of query setFoutief failed", e);
        }

        return false;
    }


    public boolean checkDaglimiet(String pasNr, int amount) {
        double dagtotaal = getDagTotaal(pasNr);

        double daglimiet = 500.0-dagtotaal;
        if(daglimiet>amount){
            return true;
        }
        return false;
    }


    public double getDagTotaal(String pasNr) {
        try{
            LocalDate todayLD = LocalDate.now();
            String todaySt = todayLD.toString();

            PreparedStatement ps = con.prepareStatement("SELECT SUM(hoeveelheid) as SUMamount "
                                                            + "FROM transactie "
                                                            + "WHERE transactie.pasNr = ? AND DATE(transactie.datumtijd) = ?");

            ps.setString(1, pasNr);
            ps.setString(2, todaySt);    //gaat dit goed?
            rs = ps.executeQuery();
            rs.next();
            double result = rs.getDouble("SUMamount");

            return result;


        }catch(SQLException e){
            logger.error("Execution of query getDaglimiet failed", e);
        }
        return 0.0;
    }


    public boolean checkPasRekening(String rekeningNr, String pasNr) {
        try{
            PreparedStatement ps = con.prepareCall("SELECT * "
                                                    + "FROM pas "
                                                    + "WHERE pas.rekeningNr = ? AND pas.pasNr = ?");
            ps.setString(1, rekeningNr);
            ps.setString(2, pasNr);
            rs = ps.executeQuery();
            if (rs.next()){
                if(!getGeblokkeerd(pasNr)){
                    return true;
                }


            }

            logger.debug("pasNr rekeningNr combi bestaat niet");
        }
        catch (SQLException e){
            logger.error("Execution of query compare pas & rekeningnr from pasTable failed", e);
        }

        return false;
    }

    public boolean checkPincode(String pasNr, String pincode) {
        try{
            PreparedStatement ps = con.prepareCall("SELECT pincode "
                                                        + "FROM pas "
                                                        + "WHERE pas.pasNr = ?");
            ps.setString(1,pasNr);
            rs = ps.executeQuery();
            rs.next();
            String psPincode = rs.getString("pincode");

            if (psPincode.equals(pincode)){         //kan dit? is dit efficient?
                setFoutief(pasNr, true);
                return true;
            }
            else{
                setFoutief(pasNr, false);
            }

            logger.debug("pincode niet correct");
        }
        catch (SQLException e){
            logger.error("Execution of query pincode compare failed", e);
        }

        return false;
    }








    //TODO elke try meer exception catches speciaal voor die try
}
