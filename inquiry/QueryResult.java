package spirit.fitness.scanner.inquiry;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

import spirit.fitness.scanner.common.Constrant;
import spirit.fitness.scanner.model.Itembean;

public class QueryResult implements ActionListener{

	public JFrame frame;
	private JScrollPane scrollPanel;
	public final static int QUERY_MODEL = 0;
	public final static int QUERY_LOCATION = 1;

	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void setContent(int type, List<Itembean> _items) 
	{
		if(type == QUERY_MODEL)
			setModelLayOut(_items);
		else if(type == QUERY_LOCATION)
			setLocationLayOut(_items);
			
	}
	
	
	private void setModelLayOut( List<Itembean> _items) 
    {
		JFrame frame = new JFrame();
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    JPanel panel = new JPanel();    
        // adding panel to frame
	    frame.add(panel, BorderLayout.NORTH);
	    Font font = new Font("Verdana", Font.BOLD, 18);
	    
	    
	    String modelNo = _items.get(0).ModelNo;
	    String title = Constrant.models.get(modelNo).Model;
        // Creating JLabel
	    JLabel modelLabel = new JLabel("Model :                                 "+title);
	  	     
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
        JLabel modelText = new JLabel("                         TOTAL: 50");
        modelText.setFont(font);
        frame.setLocationRelativeTo(null);  
	    frame.setBounds(500, 200, 1000, 600);
        panel.add(modelText);
        
        
        Object rowData[][] = null;
        if(!_items.isEmpty())
        {
        	
        	TreeMap<String,List<Itembean>> map = new TreeMap<String,List<Itembean>>();
        	
        	for(Itembean item : _items) 
        	{
        		if(map.containsKey(item.Location))
        		{
        			List<Itembean> items = map.get(item.Location);
        			items.add(item);
        		}else 
        		{
        			List<Itembean> items = new ArrayList<Itembean>();
        			items.add(item);
        			map.put(item.Location, items);
        		}
        	}
        	
        	rowData = new Object[map.size()][2];
  		    int totalCount = 0;
        	int rowIndex = 0;
        	for(Map.Entry<String, List<Itembean>> location : map.entrySet()) 
        	{
        		for(int j = 0; j < 2; j++) 
 		    	{
 		    		rowData[rowIndex][0] =  location.getKey();
 		    		rowData[rowIndex][1] =  location.getValue().size();
 		    		
 		    	}
        		totalCount +=location.getValue().size();
        		rowIndex++;
        	}
        	
        	modelLabel.setText("MODEL : "+title+"                                ");
        	modelText.setText("                         TOTAL: "+totalCount);
        }else {
        	rowData = new Object[2][2];
        	rowData[0][0] =  "701";
        	rowData[0][1] =  "5";
        	
        	rowData[1][0] =  "702";
        	rowData[0][1] =  "10";
        }
        
  		Object columnNames[] = { "LOCATION", "QUANTITY" };
  		
  		JTable table = new JTable(rowData, columnNames);
  		table.setFont(font);
  		

	    JScrollPane scrollPane = new JScrollPane(table);
	    scrollPane.setBounds(230,200,200,25);
	    frame.add(scrollPane);
	    //frame.add(scrollPane, BorderLayout.CENTER);
	    frame.setVisible(true);
				
    }
	
	private void setLocationLayOut( List<Itembean> _items) 
    {
		JFrame frame = new JFrame();
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setLocationRelativeTo(null);  
	    frame.setBounds(500, 200, 1000, 600);
	    
	    JPanel panel = new JPanel();    
        // adding panel to frame
	    frame.add(panel, BorderLayout.NORTH);
	    Font font = new Font("Verdana", Font.BOLD, 18);
        // Creating JLabel
	    JLabel modelLabel = new JLabel("Location : 002                                ");
	  	     
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
        JLabel modelText = new JLabel("                         TOTAL: 50");
        modelText.setFont(font);
        modelText.setBounds(230,200,200,25);
        panel.add(modelText);
        
        Object rowData[][] = null;
        if(!_items.isEmpty())
        {
        	
        	TreeMap<String,List<Itembean>> map = new TreeMap<String,List<Itembean>>();
        	
        	for(Itembean item : _items) 
        	{
        		if(map.containsKey(item.ModelNo))
        		{
        			List<Itembean> items = map.get(item.ModelNo);
        			items.add(item);
        		}else 
        		{
        			List<Itembean> items = new ArrayList<Itembean>();
        			items.add(item);
        			map.put(item.ModelNo, items);
        		}
        	}
        	
        	rowData = new Object[map.size()][2];
  		    int totalCount = 0;
        	int rowIndex = 0;
        	for(Map.Entry<String, List<Itembean>> location : map.entrySet()) 
        	{
        		for(int j = 0; j < 2; j++) 
 		    	{
 		    		rowData[rowIndex][0] =  Constrant.models.get(location.getKey()).Model;
 		    		rowData[rowIndex][1] =  location.getValue().size();
 		    		
 		    	}
        		totalCount +=location.getValue().size();
        		rowIndex++;
        	}
        	
        	modelLabel.setText("Location : "+_items.get(0).Location+"                                ");
        	modelText.setText("                         TOTAL: "+totalCount);
        }else 
        {
        	rowData = new Object[2][2];
        	rowData[0][0] =  "CT800";
        	rowData[0][1] =  "5";
        	
        	rowData[1][0] =  "CT300";
        	rowData[0][1] =  "5";
        	
      		
      		
        }
        
        Object columnNames[] = { "MODEL", "QUANTITY" };
  		JTable table = new JTable(rowData, columnNames);
  		table.setFont(font);

	    JScrollPane scrollPane = new JScrollPane(table);
	    scrollPane.setBounds(230,200,200,25);
	    frame.add(scrollPane);
	    //frame.add(scrollPane, BorderLayout.CENTER);
	   
	    //frame.setLocationRelativeTo(null);  
	    //frame.setSize(1000, 500);
	    frame.setVisible(true);
			
	    frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	    frame.addWindowListener(new WindowAdapter() {
	      public void windowClosing(WindowEvent e) {
	    	   frame.dispose();
	    	   frame.setVisible(false);
	        }    
	      });
    }

}
