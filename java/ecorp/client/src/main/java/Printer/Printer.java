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
        public String[] message = {"ING Bank", "uw saldo:" + " \u20AC" + "250"}; //dingen binnenhalen van informatie transactie ofzo

        public double amount;

        public Printer(double money) {

            amount = money;
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


        public void print (boolean voorbeeld) {


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

                    /*
                    if (noDialog)
                        job.print();
                    else{
                        if (job.printDialog())
                            job.print();
                        }
                    }
                     */
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

            graphics.setFont(new Font("Roboto", Font.BOLD, 18));
            graphics.drawString("PsyBank", 55, 34);

            graphics.setFont(new Font("Roboto", Font.PLAIN, 14));
            graphics.drawString("Dé bank voor u!", 55, 50);

            graphics.drawLine(5, 60, 150, 60);



            graphics.drawLine(5, 240, 150, 240);

            graphics.setFont(new Font("Monospace", Font.PLAIN, 10));
            graphics.drawString("Bedankt en tot ziens", 30, 255);
            graphics.drawString("kijk ook eens op psybank.ml", 10, 270);
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
