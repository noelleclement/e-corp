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
}
