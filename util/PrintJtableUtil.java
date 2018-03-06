package spirit.fitness.scanner.util;

import java.awt.print.PrinterException;
import java.text.MessageFormat;

import javax.naming.Context;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;

public class PrintJtableUtil
{
	/**
     * Print the table result.
     */
    public static void printTableResult(String headerStr, String footerStr,boolean fitWidth, boolean showPrintDialog, boolean interactive, JTable table, JFrame content) {
        /* Fetch printing properties from the GUI components */

        MessageFormat header = null;
        
        /* if we should print a header */
       // if (headerBox.isSelected()) {
            /* create a MessageFormat around the header text */
            header = new MessageFormat(headerStr);
       // }

        MessageFormat footer = null;
        
        /* if we should print a footer */
        //if (footerBox.isSelected()) {
            /* create a MessageFormat around the footer text */
            footer = new MessageFormat(footerStr);
        //}

       
        /* determine the print mode */
        JTable.PrintMode mode = fitWidth ? JTable.PrintMode.FIT_WIDTH
                                         : JTable.PrintMode.NORMAL;

        try {
            /* print the table */
            boolean complete = table.print(mode, header, footer,
                                                 showPrintDialog, null,
                                                 interactive, null);

            /* if printing completes */
            if (complete) {
                /* show a success message */
                JOptionPane.showMessageDialog(content,
                                              "Printing Complete",
                                              "Printing Result",
                                              JOptionPane.INFORMATION_MESSAGE);
            } else {
                /* show a message indicating that printing was cancelled */
                JOptionPane.showMessageDialog(content,
                                              "Printing Cancelled",
                                              "Printing Result",
                                              JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (PrinterException pe) {
            /* Printing failed, report to the user */
            JOptionPane.showMessageDialog(content,
                                          "Printing Failed: " + pe.getMessage(),
                                          "Printing Result",
                                          JOptionPane.ERROR_MESSAGE);
        }
    }
	

}