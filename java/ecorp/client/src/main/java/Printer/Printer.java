/**
 * Created by Noelle on 31-03-17.
 */


    //package printen;
package Printer;

import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;



    public class Printer {

        private PageFormat page;
        //public String[] message = {"ING Bank", "uw saldo:" + " \u20AC" + "250"}; //dingen binnenhalen van informatie transactie ofzo

        public String transactieID;
        public String rekeningNr;
        public String datumtijd;
        public String tien;
        public String twintig;
        public String vijftig;
        public String bedrag;


        public Printer(int transactieID_, String rekeningNr_, String datumtijd_, int bedrag_, int tien_, int twintig_, int vijftig_ ) {

            transactieID = Integer.toString(transactieID_);
            rekeningNr = rekeningNr_;
            datumtijd = datumtijd_;             //letop hier wordt er rekening mee gehouden dat datumtijd overal in server door wordt gegeven als String
            tien = Integer.toString(tien_);
            twintig = Integer.toString(twintig_);
            vijftig = Integer.toString(vijftig_);
            bedrag = Integer.toString(bedrag_);

        }

        public static void main (String[] args){

        }

        public void preview() {
            JFrame frame = new JFrame();

            frame.setVisible(true);
            frame.setTitle("Preview van label 99014");
            frame.setSize(300, 400);
            frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
            frame.add(new PrintPreview());
        }

        public void print (boolean voorbeeld ) {





            if (voorbeeld) {
                this.preview();
            }

            if (!voorbeeld) {
                PrinterJob pj = PrinterJob.getPrinterJob();
                page = new PageFormat();
                Paper paper = new Paper();

                paper.setImageableArea(0, 0, 160, 290);
                paper.setSize(160, 290);
                page.setPaper(paper);
                pj.setPrintable(new OutPrintable(), page);

                try {
                    pj.print();

                }
                catch(PrinterException e) {
                    System.out.println("Error while printing: " + e.getMessage());
                }

            }


        }


        public void createPrint (Graphics2D graphics) {
            graphics.setColor(Color.white);
            graphics.fillRect(0, 0, 160, 280);

            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            graphics.setColor(Color.black);

            Image img = new ImageIcon("/ecorp/client/src/main/java/Printer/445110_1.jpg").getImage();
            graphics.drawImage(img, 55, 10, 50, 50, null);

            /*
            graphics.setFont(new Font("Roboto", Font.BOLD, 18));
            graphics.drawString("E-corp", 45, 34);
            */

            graphics.setFont(new Font("Roboto", Font.PLAIN, 8));
            graphics.drawString("Together we can change the world", 13, 70);

            graphics.drawLine(10, 80, 150, 80);

            //transactieid
            graphics.setFont(new Font("Bodoni MT", Font.PLAIN, 8));
            graphics.drawString("TransactieNr: ", 20, 110);

            graphics.setFont(new Font("Dialog", Font.PLAIN, 8));
            graphics.drawString(transactieID, 80, 110);

            //rekeningnr xxxxxxx2834
            graphics.setFont(new Font("Bodoni MT", Font.PLAIN, 8));
            graphics.drawString("RekeningNr: ", 20, 130);

            graphics.setFont(new Font("Dialog", Font.PLAIN, 8));
            graphics.drawString("xxxxxxx"+rekeningNr.substring(7), 80, 130);

            //datum/tijd
            graphics.setFont(new Font("Bodoni MT", Font.PLAIN, 8));
            graphics.drawString("Datum: ", 20, 150);

            graphics.setFont(new Font("Dialog", Font.PLAIN, 8));
            graphics.drawString(datumtijd, 80, 150);

            //biljetten
            graphics.setFont(new Font("Bodoni MT", Font.PLAIN, 8));
            graphics.drawString("Biljetten: ", 20, 170);

            graphics.setFont(new Font("Dialog", Font.PLAIN, 8));
            graphics.drawString("10: "+tien+", 20: "+twintig+", 50: "+vijftig, 80, 170);

            //bedrag
            graphics.setFont(new Font("Bodoni MT", Font.BOLD, 10));
            graphics.drawString("Totaal: ", 15, 200);

            graphics.setFont(new Font("Dialog", Font.BOLD, 10));
            graphics.drawString(bedrag, 80, 200);





            graphics.drawLine(10, 260, 150, 260);

            graphics.setFont(new Font("Monospace", Font.PLAIN, 10));
            graphics.drawString("Bedankt en tot ziens", 30, 273);
    }


    class OutPrintable implements Printable {

        public int print(Graphics g, PageFormat pf, int pageIndex) {
            if (pageIndex != 0) {
                return NO_SUCH_PAGE;
            }

            Graphics2D graphics = (Graphics2D) g;
            createPrint(graphics);

            return PAGE_EXISTS;
        }
    }

    class PrintPreview extends JPanel {
        public void paintComponent(Graphics g) {

            super.paintComponent(g);
            Graphics2D graphics = (Graphics2D) g;
            createPrint(graphics);
        }
    }
}
