package GUI;

/**
 * Created by Hans de Rooij on 10/03/2017.
 */
public class WithdrawAmountConfirmScreen extends ButtonScreen {
    private int amount;
    public WithdrawAmountConfirmScreen(int amount) {
        this.rightButtons[0].setIdentifier("A");
        this.rightButtons[0].setText("Ja - met bon");
        this.rightButtons[1].setIdentifier("B");
        this.rightButtons[1].setText("Ja - zonder bon");
        this.rightButtons[2].setIdentifier("C");
        this.rightButtons[2].setText("Nee");
        this.mainTextLabel.setText("<html>Wilt u<br>"+amount+"<br>opnemen?");
        this.amount = amount;
    }

    public int getDesiredAmount() {
        return this.amount;
    }

    public void ontoereikendSaldo() {
        this.mainTextLabel.setText("<html>Uw saldo is niet toereikend<br> voor deze transactie</html>");
        this.rightButtons[0].setIdentifier("");
        this.rightButtons[0].setText("");
        this.rightButtons[1].setText("Ga terug");
    }

    public void hogerDanDaglimiet() {
        this.mainTextLabel.setText("<html>U gaat met het voorgestelde<br> bedrag over uw daglimiet heen</html>");
        this.rightButtons[0].setIdentifier("");
        this.rightButtons[0].setText("");
        this.rightButtons[1].setText("Ga terug");
    }
}
