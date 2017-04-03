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

    private Database() {

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
                                                            + "FROM E-corp.rekening "
                                                            + "WHERE E-corp.rekening.rekeningnr = ?");
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

<<<<<<< HEAD
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
=======
    public int comparePincode (String pasNr, String pincode){
        //TODO int in java goed als ? in sql?
        //TODO letop: nog geen pas tabel
        //TODO loggers toevoegen

        try{
            PreparedStatement ps = con.prepareCall("SELECT pincode"
                    + "FROM E-corp.pas"
                    + "WHERE E-corp.pas.pasNr = ?");
            ps.setInt(1, Integer.parseInt(pasNr));
            rs = ps.executeQuery();
            if (ps.getResultSet().getString("pincode") == pincode){

                return 0;
            }

            logger.debug("pincode niet correct");
            return 1;
        }
        catch (SQLException e){
            logger.error("Execution of query pincode compare failed", e);
        }

        return 1;
>>>>>>> 1b8395ae7de6bb4218322c4878a8839a6f0ec1bd

            if (logger.isDebugEnabled()) {
                logger.debug("getGeblokkeerd geeft true: pas is geblokkeerd");
            }

<<<<<<< HEAD
            return 1;
=======
    public boolean withdrawpossible(String rekeningNr, int amount) {
        return false;
    }

    public boolean withdrawPossible(String rekeningNr, int amount) {
        return false;
    }
>>>>>>> 1b8395ae7de6bb4218322c4878a8839a6f0ec1bd

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
            if (withdrawPossible(rekeningNr, pasNr, amount)==3) {
                //pinnen
                logger.debug("withDraw with Rekeningnr " + rekeningNr, "saldo: " + saldo);

                PreparedStatement ps = con.prepareStatement("UPDATE klant "
                                                                + "SET saldo = ? "
                                                                + "WHERE rekeningnr = ?");

                ps.setDouble(1, (saldo - amount));
                ps.setString(2, rekeningNr);

                boolean result = ps.execute();

                if (logger.isDebugEnabled()) {
                    logger.debug("Nieuwe saldo: ", getSaldo(rekeningNr));
                }


                return result;
            }

            logger.debug("Saldo is ontoereikend, gebruik withdrawPossible() voor reden");
            return false;
        } catch (SQLException e) {
            logger.error("Execution of query withdraw failed", e);
        }

        return false;
    }

<<<<<<< HEAD


    public boolean getGeblokkeerd(String pasNr) {
        try{
            PreparedStatement ps = con.prepareStatement("SELECT geblokkeerd"
                                                            + "FROM E-corp.pas"
                                                            + "WHERE E-corp.pas.pasNr = ?");
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
            PreparedStatement ps = con.prepareStatement("UPDATE pas"
                                                            + "SET geblokkeerd = ?"
                                                            + "WHERE pasNr = ?");

            ps.setInt(1, 1);
            ps.setString(2, pasNr);

            boolean result = ps.execute();

            if (logger.isDebugEnabled()) {
                logger.debug("Nieuwe waarde 'geblokkeerd'", getGeblokkeerd(pasNr));
            }

            return result;

        }catch(SQLException e){
            logger.error("Execution of query setGeblokkeerd failed", e);
        }

        return false;
    }


    public int getFoutief(String pasNr) {
        try{
            PreparedStatement ps = con.prepareStatement( "SELECT fpoging"
                                                            + "FROM E-corp.pas"
                                                            + "WHERE E-corp.pas.pasNr = ?");
            ps.setString(1, pasNr);
            rs = ps.executeQuery();
            rs.next();

            int result = rs.getInt("fpoging");

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
                PreparedStatement ps = con.prepareStatement("UPDATE pas"
                                                                + "SET fpoging = ?"
                                                                + "WHERE pasNr = ?");

                ps.setInt(1, 0);
                ps.setString(2, pasNr);

                boolean result = ps.execute();

                if (logger.isDebugEnabled()) {
                    logger.debug("Nieuwe waarde 'fpoging'", getFoutief(pasNr));
                }

                return result;

            }


            if(!pincodeTrue){
                int aantalFout = getFoutief(pasNr);

                if(aantalFout == 0 || aantalFout == 1){
                    //set fpoging aantalFout+1
                    PreparedStatement ps = con.prepareStatement("UPDATE pas"
                                                                    + "SET fpoging = ?"
                                                                    + "WHERE pasNr = ?");

                    ps.setInt(1, (aantalFout+1));
                    ps.setString(2, pasNr);

                    boolean result = ps.execute();

                    if (logger.isDebugEnabled()) {
                        logger.debug("Nieuwe waarde 'fpoging'", getFoutief(pasNr));
                    }


                    return result;
                }


                if(aantalFout == 2){
                    //set fpoging 3
                    PreparedStatement ps = con.prepareStatement("UPDATE pas"
                                                                    + "SET fpoging = ?"
                                                                    + "WHERE pasNr = ?");

                    ps.setInt(1, 3);
                    ps.setString(2, pasNr);

                    boolean result = ps.execute();

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

            PreparedStatement ps = con.prepareStatement("SELECT SUM(hoeveelheid) as SUMamount"
                                                            + "FROM E-corp.transactie"
                                                            + "WHERE E-corp.transactie.pasNr = ? AND DATE(E-corp.transactie.datumtijd) = ?");

            ps.setString(1, pasNr);
            ps.setString(2, todaySt);    //gaat dit goed?
            rs = ps.executeQuery();
            double result = rs.getDouble("SUMamount");

            return result;


        }catch(SQLException e){
            logger.error("Execution of query getDaglimiet failed", e);
        }
        return 0.0;
    }


    public boolean checkPasRekening(String rekeningNr, String pasNr) {
        try{
            PreparedStatement ps = con.prepareCall("SELECT *"
                                                    + "FROM E-corp.pas"
                                                    + "WHERE E-corp.pas.rekeningNr = ? AND E-corp.pas.pasNr = ?");
            ps.setString(1, rekeningNr);
            ps.setString(2, pasNr);
            rs = ps.executeQuery();
            if (rs.next()){
                return true;
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
            PreparedStatement ps = con.prepareCall("SELECT pincode"
                                                        + "FROM E-corp.pas"
                                                        + "WHERE E-corp.pas.pasNr = ?");
            ps.setString(1,pasNr);
            rs = ps.executeQuery();
            String psPincode = rs.getString("pincode");

            if (psPincode == pincode){         //kan dit? is dit efficient?
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













=======
    public boolean comparePasRekening(String rekeningNr, int pasNr) {
        return false;
    }
>>>>>>> 1b8395ae7de6bb4218322c4878a8839a6f0ec1bd



    //TODO elke try meer exception catches speciaal voor die try
}
