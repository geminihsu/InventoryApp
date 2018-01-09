package spirit.fitness.scanner.inquiry;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import spirit.fitness.scanner.model.Itembean;
import spirit.fitness.scanner.receving.ItemsPannel;
import spirit.fitness.scanner.restful.FGRepositoryImplRetrofit;

public class QueryPannel implements ActionListener{

	public JFrame frame;

	public QueryPannel() {
		
		initialize();
	      
	}
	
	
	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {
		  JFrame frame = new JFrame("Query Pannel");
	        // Setting the width and height of frame
	        frame.setSize(600, 600);
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setLocationRelativeTo(null);  
	        /* Creating panel. This is same as a div tag in HTML
	         * We can create several panels and add them to specific 
	         * positions in a JFrame. Inside panels we can add text 
	         * fields, buttons and other components.
	         */
	        JPanel panel = new JPanel();    
	        // adding panel to frame
	        frame.add(panel);
	        /* calling user defined method for adding components
	         * to the panel.
	         */
	        placeComponents(panel);

	        // Setting the frame visibility to true
	        frame.setVisible(true);
				
	        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		    frame.addWindowListener(new WindowAdapter() {
		      public void windowClosing(WindowEvent e) {
		    	   frame.dispose();
		    	   frame.setVisible(false);
		        }    
		      });
		   
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	 private static void placeComponents(JPanel panel) {

	        /* We will discuss about layouts in the later sections
	         * of this tutorial. For now we are setting the layout 
	         * to null
	         */
	        panel.setLayout(null);
	        Font font = new Font("Verdana", Font.BOLD, 18);
	        // Creating JLabel
	        JLabel modelLabel = new JLabel("Model");
	        /* This method specifies the location and size
	         * of component. setBounds(x, y, width, height)
	         * here (x,y) are cordinates from the top left 
	         * corner and remaining two arguments are the width
	         * and height of the component.
	         */
	        modelLabel.setBounds(30,200,200,25);
	        modelLabel.setFont(font);
	        panel.add(modelLabel);

	        /* Creating text field where user is supposed to
	         * enter user name.
	         */
	        JTextField modelText = new JTextField(20);
	        modelText.setFont(font);
	        modelText.setBounds(230,200,300,25);
	        panel.add(modelText);

	        // Same process for password label and text field.
	        JLabel locationLabel = new JLabel("Location");
	        locationLabel.setFont(font);
	        locationLabel.setBounds(30,230,200,25);
	        panel.add(locationLabel);

	        /*This is similar to text field but it hides the user
	         * entered data and displays dots instead to protect
	         * the password like we normally see on login screens.
	         */
	        JTextField locationText = new JTextField(20);
	        locationText.setFont(font);
	        locationText.setBounds(230,230,300,25);
	        panel.add(locationText);

	        // Creating login button
	        JButton QueryButton = new JButton("Query");
	        QueryButton.setFont(font);
	        QueryButton.setBounds(220, 300, 200, 25);
	        QueryButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					
					if(modelText.getText().isEmpty()&&locationText.getText().isEmpty())
						JOptionPane.showMessageDialog(null, "Please enter correct infomation!");
					else		
					{
					
						EventQueue.invokeLater(new Runnable() {
		  					public void run() {
		  						try {
		  							
		  							passQueryResult(modelText.getText().toString(),locationText.getText().toString());
		  						
		  						} catch (Exception e) {
		  							e.printStackTrace();
		  						}
		  					}
		  				});		
						
					}
		  						
				}
			});
	        panel.add(QueryButton);
	    }

	private static void passQueryResult(String modelNo, String Location) {
		QueryResult window = null;
		List<Itembean> items = null;
		
		int queryType = (modelNo.equals("") && !Location.equals(""))? QueryResult.QUERY_LOCATION :QueryResult.QUERY_MODEL;
		
		if(queryType == QueryResult.QUERY_LOCATION) 
		{
			FGRepositoryImplRetrofit fgInventory = new FGRepositoryImplRetrofit();
			
			try {
				items = (ArrayList<Itembean>)fgInventory.getItemsByLocation(Integer.valueOf(Location));			
				window = new QueryResult();
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
					
			
		}else if(queryType == QueryResult.QUERY_MODEL) 
		{
			if(modelNo.length() > 6) 
			{
				//from scanner
				modelNo = modelNo.substring(0, 6);
			}
			
			FGRepositoryImplRetrofit fgInventory = new FGRepositoryImplRetrofit();
			
			try {
				items = (ArrayList<Itembean>)fgInventory.getItemsByModel(Integer.valueOf(modelNo));
				window = new QueryResult();
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			
		}
		
		if(items.size() == 0)
			JOptionPane.showMessageDialog(null, "No Items");
		else {
			if(queryType == QueryResult.QUERY_LOCATION) 
				window.setContent(QueryResult.QUERY_LOCATION,items);
			else
				window.setContent(QueryResult.QUERY_MODEL,items);
			
			window.frame.setVisible(true);
		}
	}

}
