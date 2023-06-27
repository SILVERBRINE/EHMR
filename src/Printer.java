package src;

import java.awt.print.*;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Printer {
    public static void printComponent(Component component) {
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        printerJob.setPrintable(new Printable() {
            @Override
            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                if (pageIndex > 0) {
                    return Printable.NO_SUCH_PAGE;
                }
                Graphics2D g2d = (Graphics2D) graphics;
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                component.printAll(graphics);
                return Printable.PAGE_EXISTS;
            }
        });
        if (printerJob.printDialog()) {
            try {
                printerJob.print();
            } catch (PrinterException e) {
                e.printStackTrace();
            }
        }
    }
}