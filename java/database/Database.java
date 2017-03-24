package nl.hro.sitde.ecorp2.server;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;


/**
 * Created by Noelle on 22-03-17.
 */
public class Database {

    private static /*final*/ Logger logger; //met final foutcode
    private Connection con;
    private String host;
    private String uName;
    private String uPass;
    private ResultSet rs;

    public Database(){

        logger = LoggerFactory.getLogger(Database.class);
        con = null;
        host = "jdbc:mysql://localhost:3306/E-corp";
        uName = "root";
        uPass = "pandabeer1";

        this.connect();

    }

    private void connect() {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(host, uName, uPass);
            logger.info("Connection to the database is established");
        }
        catch (Exception e) {
            //moet nog meerdere exceptions voor alle aparte fouten die kunnen opkomen, voor elke aparte error andere log namelijk
            logger.error("Connection to database failed", e);
        }


    }

    public int getBalance (String rekeningNr){
        //nu (22-3) staat saldo nog als int in database
        int saldo = 0;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT saldo"
                                                            + "FROM E-corp.klant"
                                                            + "WHERE E-corp.klant.rekeningnr = ?");
            ps.setString(1, rekeningNr);
            rs = ps.executeQuery();
            rs.next();
            saldo = rs.getInt("saldo");
            logger.debug("Saldo = " + saldo); //als handig kan ook rekeningnr etc toegevoegd worden
        }
        catch (SQLException e) {
            logger.error("Execution of query select saldo failed", e); //moet nog wat uitgebreider
        }
        return 0;
    }

    public boolean withDraw (String rekeningNr, int amount){
        //nu (22-3) staat saldo nog als int in database

        try {
            int saldo = getBalance(rekeningNr);
            if (saldo > amount) {
                //pinnen
                logger.debug("withDraw with Rekeningnr "+rekeningNr, "saldo: "+ saldo);

                PreparedStatement ps = con.prepareStatement("UPDATE klant"
                        + "SET saldo = ?"
                        + "WHERE rekeningnr = ?");
                //werd ook al in getBalance gebruikt maar hoe werkt dat ? met next enzo

                ps.setInt(1, (saldo-amount));
                ps.setString(2, rekeningNr);
                //wat is die parameterindex

                boolean result = ps.execute();

                if (logger.isDebugEnabled()){
                    logger.debug("Nieuwe saldo: {}", getBalance(rekeningNr));
                }


                return result;
            }

            logger.debug("Saldo is ontoereikend");
            return false;
        }
        catch (SQLException e){
            logger.error("Execution of query withdraw failed", e);
        }

        return false;



}
