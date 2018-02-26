package spirit.fitness.scanner.util;
/**
 * Class: Example4
 * <p>
 * 
 * Example of using the TextLayout class to format a text paragraph.
 * <p>
 * 
 * @author Jean-Pierre Dube <jpdube@videotron.ca>
 * @version 1.0
 * @since 1.0
 */

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.AttributedString;

public class PrinterHelper {
  /*public static void main(String[] args) {

    //PrinterHelper example = new PrinterHelper("test");
    //System.exit(0);
  }*/

  //--- Private instances declarations
  private final static int POINTS_PER_INCH = 1;

  public PrinterHelper() 
  {
	  
  }
  public PrinterHelper(String content) {

    //--- Create a new PrinterJob object
    PrinterJob printJob = PrinterJob.getPrinterJob();

    //--- Create a new book to add pages to
    Book book = new Book();

    //--- Add the cover page using the default page format for this print
    // job
    //book.append(new IntroPage(), printJob.defaultPage());

    //--- Add the document page using a portrait page format
    PageFormat documentPageFormat = new PageFormat();
    documentPageFormat.setOrientation(PageFormat.PORTRAIT);
    book.append(new Document(content), documentPageFormat);

    //--- Tell the printJob to use the book as the pageable object
    printJob.setPageable(book);

    //--- Show the print dialog box. If the user click the
    //--- print button we then proceed to print else we cancel
    //--- the process.
    if (printJob.printDialog()) {
      try {
        printJob.print();
      } catch (Exception PrintException) {
        PrintException.printStackTrace();
      }
    }
  }

  public void printItems(String content) 
  {
	  PrinterJob job = PrinterJob.getPrinterJob();
	  
	    // Set the Page to A4 size
	     
	    PageFormat pf = new PageFormat();
	    Paper paperSize = new Paper();
	    paperSize.setSize(594.936, 841.536);
	    pf.setOrientation(PageFormat.LANDSCAPE);
	   
	 
	    job.setPrintable(new PrintFunction(content));
	    boolean doPrint = job.printDialog();
	    if (doPrint)
	    {
	        try
	        {
	            job.print();
	        }
	        catch (PrinterException e)
	        {
	          
	        }
	    }
  }
  
	public void printTable(String content) {
		PrinterJob job = PrinterJob.getPrinterJob();
		job.setPrintable(new PrintTable(content));
		boolean ok = job.printDialog();
		if (ok) {
			try {
				job.print();
			} catch (PrinterException ex) {
				/* The job did not successfully complete */
			}
		}
	}
  
  /**
   * Class: IntroPage
   * <p>
   * 
   * This class defines the painter for the cover page by implementing the
   * Printable interface.
   * <p>
   * 
   * @author Jean-Pierre Dube <jpdube@videotron.ca>
   * @version 1.0
   * @since 1.0
   * @see Printable
   */
  private class IntroPage implements Printable {

    /**
     * Method: print
     * <p>
     * 
     * @param g
     *            a value of type Graphics
     * @param pageFormat
     *            a value of type PageFormat
     * @param page
     *            a value of type int
     * @return a value of type int
     */
    public int print(Graphics g, PageFormat pageFormat, int page) {

      //--- Create the Graphics2D object
      Graphics2D g2d = (Graphics2D) g;

      //--- Translate the origin to 0,0 for the top left corner
      g2d.translate(pageFormat.getImageableX(), pageFormat
          .getImageableY());

      //--- Set the default drawing color to black
      g2d.setPaint(Color.black);

      //--- Draw a border arround the page
      Rectangle2D.Double border = new Rectangle2D.Double(0, 0, pageFormat
          .getImageableWidth(), pageFormat.getImageableHeight());
      g2d.draw(border);

      //--- Print the title
      String titleText = "Printing in Java Part 2, Example 4";
      Font titleFont = new Font("helvetica", Font.BOLD, 18);
      g2d.setFont(titleFont);

      //--- Compute the horizontal center of the page
      FontMetrics fontMetrics = g2d.getFontMetrics();
      double titleX = (pageFormat.getImageableWidth() / 2)
          - (fontMetrics.stringWidth(titleText) / 2);
      double titleY = 3 * POINTS_PER_INCH;
      g2d.drawString(titleText, (int) titleX, (int) titleY);

      return (PAGE_EXISTS);
    }
  }

  /**
   * Class: Document
   * <p>
   * 
   * This class is the painter for the document content.
   * <p>
   * 
   * 
   * @author Jean-Pierre Dube <jpdube@videotron.ca>
   * @version 1.0
   * @since 1.0
   * @see Printable
   */
  private class Document implements Printable {

	  
	 private String content;
	 
	 public Document(String _content)
	 {
		 content = _content;
	 }
    /**
     * Method: print
     * <p>
     * 
     * @param g
     *            a value of type Graphics
     * @param pageFormat
     *            a value of type PageFormat
     * @param page
     *            a value of type int
     * @return a value of type int
     * 
     */
    public int print(Graphics g, PageFormat pageFormat, int page) {

      
      //--- Create the Graphics2D object
      Graphics2D g2d = (Graphics2D) g;

      //--- Translate the origin to 0,0 for the top left corner
      g2d.translate(pageFormat.getImageableX(), pageFormat
          .getImageableY());

      //--- Set the drawing color to black
      g2d.setPaint(Color.black);

      //--- Draw a border arround the page using a 12 point border
      /*g2d.setStroke(new BasicStroke(4));
      Rectangle2D.Double border = new Rectangle2D.Double(0, 0, pageFormat
          .getImageableWidth(), pageFormat.getImageableHeight());

      g2d.draw(border);*/

      //--- Create a string and assign the text
      String text = new String();
      text += content;
      //--- Create a point object to set the top left corner of the
      // TextLayout object
      Point2D.Double pen = new Point2D.Double(0.25 * POINTS_PER_INCH,
          0.25 * POINTS_PER_INCH);

      //--- Set the width of the TextLayout box
      double width = 350;//each character 6.25, total 16 character. 16*2.5

      //--- Create an attributed string from the text string. We are
      // creating an
      //--- attributed string because the LineBreakMeasurer needs an
      // Iterator as
      //--- parameter.
      AttributedString paragraphText = new AttributedString(text);

      //--- Set the font for this text
      paragraphText.addAttribute(TextAttribute.FONT, new Font("serif",
          Font.PLAIN, 12));

      //--- Create a LineBreakMeasurer to wrap the text for the
      // TextLayout object
      //--- Note the second parameter, the FontRendereContext. I have set
      // the second
      //--- parameter antiAlised to true and the third parameter
      // useFractionalMetrics
      //--- to true to get the best possible output
      LineBreakMeasurer lineBreaker = new LineBreakMeasurer(paragraphText
          .getIterator(), new FontRenderContext(null, true, true));

      //--- Create the TextLayout object
      TextLayout layout;

      //--- LineBreakMeasurer will wrap each line to correct length and
      //--- return it as a TextLayout object
      while ((layout = lineBreaker.nextLayout((float) width)) != null) {

        //--- Align the Y pen to the ascend of the font, remember that
        //--- the ascend is origin (0, 0) of a font. Refer to figure 1
        pen.y += layout.getAscent();

        //--- Draw the line of text
        layout.draw(g2d, (float) pen.x, (float) pen.y);

        //--- Move the pen to the next position adding the descent and
        //--- the leading of the font
        pen.y += layout.getDescent() + layout.getLeading();
      }

      
      //--- Validate the page
      if (page >= 1)
    	  return (NO_SUCH_PAGE);
      else
    	  return (PAGE_EXISTS);
    }
  }
  
  public class PrintFunction implements Printable
  {
   
      private String printData;
   
      public PrintFunction(String printDataIn)
      {
          this.printData = printDataIn;
      }
   
      @Override
      public int print(Graphics g, PageFormat pf, int page) throws PrinterException
      {
          //If the print job should only have one page, uncomment the below.
          if (page > 0)
          {
              return NO_SUCH_PAGE;
          }
   
          // Adding the "Imageable" to the x and y puts the margins on the page.
          // To make it safe for printing.
   
          Graphics2D g2d = (Graphics2D) g;
          int x = (int) pf.getImageableX();
          int y = (int) pf.getImageableY();
          g2d.translate(x, y);
   
          // Calculate the line height
          Font font = new Font("Serif", Font.PLAIN, 12);
          FontMetrics metrics = g.getFontMetrics(font);
          int lineHeight = metrics.getHeight();
   
          BufferedReader br = new BufferedReader(new StringReader(printData));
   
          // Draw the page:
          try
          {
              String line;
              // Just a safety net in case no margin was added.
              x += 50;
              y += 50;
              while ((line = br.readLine()) != null)
              {
                  y += lineHeight;
                  g2d.drawString(line, x, y);
              }
          }
          catch (IOException e)
          {
              //
          }
   
          return PAGE_EXISTS;
      }
  }
  
	public class PrintTable implements Printable {

		int[] pageBreaks; // array of page break line positions.

		/* Synthesise some sample lines of text */
		String[] textLines;

		public PrintTable(String content) 
		{
			if (textLines == null) {
				textLines = content.split("\n");
			}
		}

		@Override
		public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {

			Font font = new Font("Serif", Font.PLAIN, 10);
			FontMetrics metrics = g.getFontMetrics(font);
			int lineHeight = metrics.getHeight();

			if (pageBreaks == null) {
				int linesPerPage = (int) (pf.getImageableHeight() / lineHeight);
				int numBreaks = (textLines.length - 1) / linesPerPage;
				pageBreaks = new int[numBreaks];
				for (int b = 0; b < numBreaks; b++) {
					pageBreaks[b] = (b + 1) * linesPerPage;
				}
			}

			if (pageIndex > pageBreaks.length) {
				return NO_SUCH_PAGE;
			}

			/*
			 * User (0,0) is typically outside the imageable area, so we must translate by
			 * the X and Y values in the PageFormat to avoid clipping Since we are drawing
			 * text we
			 */
			Graphics2D g2d = (Graphics2D) g;
			g2d.translate(pf.getImageableX(), pf.getImageableY());

			/*
			 * Draw each line that is on this page. Increment 'y' position by lineHeight for
			 * each line.
			 */
			int y = 0;
			int start = (pageIndex == 0) ? 0 : pageBreaks[pageIndex - 1];
			int end = (pageIndex == pageBreaks.length) ? textLines.length : pageBreaks[pageIndex];
			for (int line = start; line < end; line++) {
				y += lineHeight;
				g.drawString(textLines[line], 0, y);
			}

			/* tell the caller that this page is part of the printed document */
			return PAGE_EXISTS;
		}

	}
  
} // Example4


