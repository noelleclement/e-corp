package apis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;


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

    public boolean comparePasRekening (String rekeningNr, String pasNr){
        //TODO varchar in sql goed als string in java?
        //TODO int in java goed als ? in sql?
        //TODO letop: nog geen pas tabel
        //TODO loggers toevoegen

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
            return false;
        }
        catch (SQLException e){
            logger.error("Execution of query compare pas & rekeningnr from pastable failed", e);
        }

        return false;

    }

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

    }

    public boolean withdrawpossible(String rekeningNr, int amount) {
        return false;
    }

    public boolean withdrawPossible(String rekeningNr, int amount) {
        return false;
    }

    public double getBalance(String rekeningNr) {
        //TODO nu (22-3) staat saldo nog als int in database
        int saldo = 0;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT saldo "
                    + "FROM E-corp.klant "
                    + "WHERE E-corp.klant.rekeningnr = ?");
            ps.setString(1, rekeningNr);
            rs = ps.executeQuery();
            rs.next();
            saldo = rs.getInt("saldo");
            logger.debug("Saldo = " + saldo); //als handig kan ook rekeningnr etc toegevoegd worden
        } catch (SQLException e) {
            logger.error("Execution of query select saldo failed", e); //moet nog wat uitgebreider
        }
        return saldo;
    }

    //TODO info saldo?
    


    public boolean getWithdraw(String rekeningNr, int amount) {
        //TODO nu (22-3) staat saldo nog als int in database

        try {
            double saldo = getBalance(rekeningNr);
            if (saldo > amount) {
                //pinnen
                logger.debug("withDraw with Rekeningnr " + rekeningNr, "saldo: " + saldo);

                PreparedStatement ps = con.prepareStatement("UPDATE klant "
                        + "SET saldo = ? "
                        + "WHERE rekeningnr = ?");
                //werd ook al in getBalance gebruikt maar hoe werkt dat ? met next enzo

                ps.setInt(1, ((int)saldo - amount));
                ps.setString(2, rekeningNr);
                //wat is die parameterindex

                boolean result = ps.execute();

                if (logger.isDebugEnabled()) {
                    logger.debug("Nieuwe saldo: ", getBalance(rekeningNr));
                }


                return result;
            }

            logger.debug("Saldo is ontoereikend");
            return false;
        } catch (SQLException e) {
            logger.error("Execution of query withdraw failed", e);
        }

        return false;
    }

    public boolean comparePasRekening(String rekeningNr, int pasNr) {
        return false;
    }


    // rekeningnr info opvragen {check of rekeningnr in database} http://stackoverflow.com/questions/11288557/how-do-i-tell-if-a-row-exists-in-a-table
    // pasnr info opvragen {check of pasnr in database } http://stackoverflow.com/questions/11288557/how-do-i-tell-if-a-row-exists-in-a-table
    // pincode opvragen { get pincode where pasnr}
    //TODO pincode foutief verhogen {set pincode_fout oid +1 }
    //TODO pincode foutief weer nul {set pincode_fout oid 0}
    //TODO pincode foutief opvragen {get pincode_fout}
    //TODO iets met daglimiet (daglimiet in rekeningnr?) {get daglimiet where rekeningnr oid?}

    //TODO via command line de tabellen maken van database
    //TODO elke try meer exception catches speciaal voor die try
}
