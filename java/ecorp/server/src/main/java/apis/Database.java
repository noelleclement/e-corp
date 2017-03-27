package apis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;


/**
 * Created by Noelle on 22-03-17.
 */

public class Database {

    private static final Logger logger = LoggerFactory.getLogger(Database.class); //met final foutcode
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

    public int getBalance(String rekeningNr) {
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






    public boolean withDraw(String rekeningNr, int amount) {
        //TODO nu (22-3) staat saldo nog als int in database

        try {
            int saldo = getBalance(rekeningNr);
            if (saldo > amount) {
                //pinnen
                logger.debug("withDraw with Rekeningnr " + rekeningNr, "saldo: " + saldo);

                PreparedStatement ps = con.prepareStatement("UPDATE klant "
                        + "SET saldo = ? "
                        + "WHERE rekeningnr = ?");
                //werd ook al in getBalance gebruikt maar hoe werkt dat ? met next enzo

                ps.setInt(1, (saldo - amount));
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


    //TODO kijken of deze compareRekeningNr methode beetje sense maakt
    public boolean compareRekeningNr(String rekeningNr) {
        //TODO varchar in sql goed als string in java?


        try{
            PreparedStatement ps = con.prepareCall("SELECT rekeningNr"
                                                    + "FROM E-corp.klant"
                                                    + "WHERE E-corp.klant.rekeningNr = ?");
            ps.setString(1,rekeningNr);
            rs = ps.executeQuery();
            if (rs.next()){
                return true;
            }

            logger.debug("rekeningNr bestaat niet");
            return false;
        }
        catch (SQLException e){
            logger.error("Execution of query select rekeningNr failed", e);
        }

        return false;
    }

    //TODO kijken of deze comparePasNr methode beetje sense maakt
    public boolean comparePasNr (int pasNr) {
        //TODO int in java goed als ? in sql?
        //TODO letop: nog geen pas tabel


        try{
            PreparedStatement ps = con.prepareCall("SELECT pasNr"
                                                    + "FROM E-corp.pas"
                                                    + "WHERE E-corp.pas.pasNr = ?");
            ps.setInt(1, pasNr);
            rs = ps.executeQuery();
            if (rs.next()){
                return true;
            }

            logger.debug("pasNr bestaat niet");
            return false;
        }
        catch (SQLException e){
            logger.error("Execution of query select pasNr failed", e);
        }

        return false;
    }

    public int getPincode (int pasNr){
        //TODO letop nog geen pas tabel in database
        //TODO iets doen met dat 't max 4 cijfers mogen zijn of moet dat in API?
        //TODO moet er nog niets qua veiligheid dat je niet zomaar pincode ertussenuit kunt opvragen?

        int pincode = 0000;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT pincode "
                                                        + "FROM E-corp.pas "
                                                        + "WHERE E-corp.pas.pasNr = ?");
            ps.setString(1, pasNr);
            rs = ps.executeQuery();
            rs.next();
            pincode = rs.getInt("saldo");
            logger.debug("Saldo = " + pincode); //als handig kan ook pas/nr etc toegevoegd worden
        } catch (SQLException e) {
            logger.error("Execution of query getPincode failed", e); //moet nog wat uitgebreider
        }
        return pincode;
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
