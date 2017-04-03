/**
 * Created by Noelle on 31-03-17.
 */


    //package printen;

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

        private PageFormat mPageFormat;
        public String[] message = {"ING Bank", "uw saldo:" + " \u20AC" + "250"}; //dingen binnenhalen van informatie transactie ofzo

        public double amount;

        public Printer(double money) {
            amount = money;
        }

        public void preview() {
            JFrame frame = new JFrame();

            frame.setTitle("Preview van label 99014");
            frame.setSize(300, 400);
            frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
            frame.setVisible(true);

            frame.add(new PrintPreview());
        }


        public void print (boolean voorbeeld) {
            Printer ps = new Printer();
            if (voorbeeld) {
                ps.preview();
            }

            if (!voorbeeld) {
                PrinterJob pj = PrinterJob.getPrinterJob();
                PageFormat page = new PageFormat();
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

            /*
            Image img = new ImageIcon("c:/logo.gif").getImage();
            graphics.drawImage(img, 25, 15, 36, 36, null);
            */

            graphics.setColor(Color.black);
            graphics.setFont(new Font("Monospaced", Font.BOLD, 25));
            graphics.drawString(message[0], 25, 70);

			graphics.setFont(new Font("Monospaced", Font.BOLD, 12));
			graphics.drawString(message[1], 15, 100);
			graphics.drawLine(10, 280, 150, 280);
			//dingen toevoegen wat uitgeprint moet worden (string message)
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
