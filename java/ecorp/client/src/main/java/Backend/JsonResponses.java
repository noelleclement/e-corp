package Backend;

import java.util.Date;

/**
 * Created by Hans de Rooij on 24/03/2017.
 */
public class JsonResponses {
    public static class ControleerRekeningnummer extends JsonResponse {
        public String transaction_id;
        public String card_uid;

        public ControleerRekeningnummer(String type, String IBAN, String transaction_id, String card_uid) {
            super(type, IBAN);
            this.transaction_id = transaction_id;
            this.card_uid = card_uid;
        }
    }

    public static class CorrectePincode extends JsonResponse {
        public String transaction_id;
        public String card_uid;

        public CorrectePincode(String type, String IBAN, String transaction_id, String card_uid) {
            super(type, IBAN);
            this.transaction_id = transaction_id;
            this.card_uid = card_uid;
        }
    }

    public static class IncorrectePincode extends JsonResponse {
        public String transaction_id;
        public String card_uid;
        public int pogingen;

        public IncorrectePincode(String type, String IBAN, String transaction_id, String card_uid, int pogingen) {
            super(type, IBAN);
            this.transaction_id = transaction_id;
            this.card_uid = card_uid;
            this.pogingen = pogingen;
        }
    }

    public static class PincodePogingenOverschreven extends JsonResponse {
        public String transaction_id;
        public String card_uid;

        public PincodePogingenOverschreven(String type, String IBAN, String transaction_id, String card_uid) {
            super(type, IBAN);
            this.transaction_id = transaction_id;
            this.card_uid = card_uid;
        }
    }

    public static class SaldoInformatie extends JsonResponse {
        public String transactionId;
        public double saldo;

        public SaldoInformatie(String type, String IBAN, String transactionId, double saldo) {
            super(type, IBAN);
            this.transactionId = transactionId;
            this.saldo = saldo;
        }
    }

    public static class GeldopnameMogelijk extends JsonResponse {
        public int hoeveelheid;

        public GeldopnameMogelijk(String transaction_id, String IBAN, int hoeveelheid) {
            super(type, IBAN);
            this.type = "OPNAME_IS_MOGELIJK";
            this.transaction_id = transaction_id;
            this.hoeveelheid = hoeveelheid;
        }
    }

    public static class OpnameHogerDanDaglimiet extends JsonResponse {
        public int daglimiet;

        public GeldopnameMogelijk(String transaction_id, String IBAN, int daglimiet) {
            super(type, IBAN);
            this.type = "HOGER_DAN_DAGLIMIET";
            this.transaction_id = transaction_id;
            this.daglimiet = daglimiet;
        }
    }

    public static class OntoereikendSaldo extends JsonResponse {
        public double saldo;

        public GeldopnameMogelijk(String transaction_id, String IBAN, double saldo) {
            super(type, IBAN);
            this.type = "ONTOEREIKEND_SALDO";
            this.transaction_id = transaction_id;
            this.saldo = saldo;
        }
    }
}
