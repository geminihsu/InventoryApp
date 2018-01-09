package spirit.fitness.scanner.inquiry;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
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
import javax.swing.table.DefaultTableModel;

import spirit.fitness.scanner.common.Constrant;
import spirit.fitness.scanner.model.Itembean;
import spirit.fitness.scanner.model.Locationbean;
import spirit.fitness.scanner.restful.FGRepositoryImplRetrofit;

public class QueryResult implements ActionListener{

	public JFrame frame;
	private JScrollPane scrollPanel;
	public final static int QUERY_MODEL = 0;
	public final static int QUERY_LOCATION = 1;
	
	public static boolean isQueryRepeat = false;

	
	/*    --------- Query by model -------- */
	//key:zone code, value:total items in the zone
	private Map<Integer,LinkedHashMap<String,List<Itembean>>> zoneMap;
	
	//key:zone code, value:total items in the zone
	private Map<Integer,Integer> zoneCount;
	
	//key:location,value:items
	private LinkedHashMap<String,List<Itembean>> map;
	
	
	private int queryModelCount = 0;
	
	/*    --------- Query by address -------- */
	//key:location title, value:location number
	private Map<String,String> modelMapingNumber;
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void setContent(int type, List<Itembean> _items) 
	{
		if(type == QUERY_MODEL)
			setModelLayOut1(_items);
		else if(type == QUERY_LOCATION)
			setLocationLayOut(_items);
			
	}
	
	

	private void setModelLayOut1( List<Itembean> _items) 
    {
		String modelNo = _items.get(0).ModelNo;
		String title = Constrant.models.get(modelNo).Model;
		
		JFrame frame = new JFrame();
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setLocationRelativeTo(null);  
		frame.setBounds(100, 100, 780, 800);
		frame.setTitle("Display the all model:" + title);
		frame.getContentPane().setLayout(null);
		
	
		Font font = new Font("Verdana", Font.BOLD, 18);
	    
		JLabel modelLabel = new JLabel("MODEL :"+title);
  	     
	    modelLabel.setBounds(30,0,500,50);
	    modelLabel.setFont(font);
	    frame.getContentPane().add(modelLabel);
	    
	    
	    JLabel quantity = new JLabel("QUANTITY :"+20);
 	     
	    quantity.setBounds(400,0,300,50);
	    quantity.setFont(font);
	    frame.getContentPane().add(quantity);
	    
	    //display Zone 2
	    JLabel zone2 = new JLabel("Zone 2");
	     
	    zone2.setBounds(30,40,300,50);
	    zone2.setFont(font);
	    frame.getContentPane().add(zone2);
	    
	    //display Zone 1
	    JLabel zone1 = new JLabel("Zone 1");
	     
	    zone1.setBounds(30,200,300,50);
	    zone1.setFont(font);
	    frame.getContentPane().add(zone1);
	    
	    //display Zone 3
	    JLabel zone3 = new JLabel("Return");
	     
	    zone3.setBounds(30,360,300,50);
	    zone3.setFont(font);
	    frame.getContentPane().add(zone3);
	    
	    //display Zone 4
	    JLabel zone4 = new JLabel("Show Room");
	     
	    zone4.setBounds(30,360,300,50);
	    zone4.setFont(font);
	    frame.getContentPane().add(zone4);
	    
		// ScrollPane for Zone1
		JScrollPane scrollZone1Pane = new JScrollPane();
		scrollZone1Pane.setBounds(33, 91, 700, 100);
		frame.getContentPane().add(scrollZone1Pane);
		
		// ScrollPane for Zone2
		JScrollPane scrollZone2Pane = new JScrollPane();
		scrollZone2Pane.setBounds(33, 250, 700,100);
		frame.getContentPane().add(scrollZone2Pane);
		
		// ScrollPane for Zone3
		JScrollPane scrollZone3Pane = new JScrollPane();
		scrollZone3Pane.setBounds(33, 300, 700,100);
		frame.getContentPane().add(scrollZone3Pane);
		
		// ScrollPane for Zone3
		JScrollPane scrollZone4Pane = new JScrollPane();
		scrollZone4Pane.setBounds(33, 300, 700,100);
		frame.getContentPane().add(scrollZone4Pane);
		
		// Table
		
		 
		 final Class[] columnClass = new Class[] {  String.class, Integer.class};
		 Object columnNames[] = { "LOCATION", "QUANTITY"};
		
		 if(!_items.isEmpty())
	        {	
	    	 	parseData(_items);
	    		modelLabel.setText("MODEL : "+title+"                                ");
	        }
	     
		 
		final Object[][] zone1Data = (Object[][]) putDataToTable(1); 	 		
	    DefaultTableModel model1 = new DefaultTableModel(zone1Data, columnNames) {
	  	      @Override
	  	       public boolean isCellEditable(int row, int column)
	  	       {
	  	    	  			Object location = zone1Data[row][0];
	  	    	            List<Itembean> items = queryLocation(String.valueOf(location));
	  	    	            setLocationLayOut(items);
	  	            		return false;
	  	       }
	  	      @Override
	  	      public Class<?> getColumnClass(int columnIndex)
	  	      {
	  	            return columnClass[columnIndex];
	  	            }
	  	      };
	  	      
	  	 final Object[][] zone2Data = (Object[][]) putDataToTable(2); 	 		
		 DefaultTableModel model2 = new DefaultTableModel(zone2Data, columnNames) {
		  	      @Override
		  	       public boolean isCellEditable(int row, int column)
		  	       {
		  	    	  		Object location = zone1Data[row][0];
		  	    	  		List<Itembean> items = queryLocation(String.valueOf(location));
		  	    	  		setLocationLayOut(items);
		  	            		return false;
		  	       }
		  	      @Override
		  	      public Class<?> getColumnClass(int columnIndex)
		  	      {
		  	            return columnClass[columnIndex];
		  	            }
		  	      };
		 
		 final Object[][] zone3Data = (Object[][]) putDataToTable(3); 	 		
		 DefaultTableModel model3 = new DefaultTableModel(zone3Data, columnNames) {
				  @Override
				  public boolean isCellEditable(int row, int column)
				  {
					  		Object location = zone1Data[row][0];
	    	            	List<Itembean> items = queryLocation(String.valueOf(location));
	    	            	setLocationLayOut(items);
				  	            		return false;
				  }
				  @Override
				  public Class<?> getColumnClass(int columnIndex)
				  {
				  	      return columnClass[columnIndex];
				  	      }
				   };
	     final Object[][] zone4Data = (Object[][]) putDataToTable(4); 	 		
					 DefaultTableModel model4 = new DefaultTableModel(zone4Data, columnNames) {
				   @Override
				   public boolean isCellEditable(int row, int column)
				   {
					   		Object location = zone1Data[row][0];
					   		List<Itembean> items = queryLocation(String.valueOf(location));
					   		setLocationLayOut(items);
							  	        return false;
				   }
				   @Override
				   public Class<?> getColumnClass(int columnIndex)
				   {
							  	      return columnClass[columnIndex];
				   }
				   };
	  	
		if(zone1Data != null) {
			zone1.setText("Zone 1 : "+ zoneCount.get(1));
			scrollZone1Pane.setBounds(33, 250, 700, 40*zone1Data.length);	      
			JTable zone1Table = new JTable(model1);
			zone1Table.setFont(font);
			scrollZone1Pane.setViewportView(zone1Table);
		}else 
		{
			scrollZone1Pane.setVisible(false);
			zone1.setVisible(false);
		}
		
		if(zone2Data != null) {
			zone2.setText("Zone 2 : "+ zoneCount.get(2));
			scrollZone2Pane.setBounds(33, 91, 700, 40*zone2Data.length);	
			JTable zone2Table = new JTable(model2);
			zone2Table.setFont(font);
			scrollZone2Pane.setViewportView(zone2Table);
		}else {
			scrollZone2Pane.setVisible(false);
			zone2.setVisible(false);
		}
		
		if(zone3Data != null) {
			zone3.setText("Return : "+ zoneCount.get(3));
			scrollZone3Pane.setBounds(33, 420, 700, 40*zone3Data.length);	
			JTable zone3Table = new JTable(model3);
			zone3Table.setFont(font);
			scrollZone3Pane.setViewportView(zone3Table);
		}else {
			scrollZone3Pane.setVisible(false);
			zone3.setVisible(false);
		}
		
		if(zone4Data != null) {
			zone4.setText("Show Room : "+ zoneCount.get(4));
			scrollZone4Pane.setBounds(33, 420, 700, 40*zone4Data.length);	
			JTable zone4Table = new JTable(model4);
			zone4Table.setFont(font);
			scrollZone4Pane.setViewportView(zone4Table);
		}else {
			scrollZone4Pane.setVisible(false);
			zone4.setVisible(false);
		}
		
		quantity.setText("QUANTITY :"+queryModelCount);
		frame.setVisible(true);
		
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		    frame.addWindowListener(new WindowAdapter() {
		      public void windowClosing(WindowEvent e) {
		    	   frame.dispose();
		    	   frame.setVisible(false);
		        }    
		      });
    }
	
	private void parseData(List<Itembean> _items) 
	{
		zoneMap = new HashMap<Integer,LinkedHashMap<String,List<Itembean>>>();
        
    	zoneCount = new HashMap<Integer,Integer>();
           
    	//key:location, value:quantity
    	map = new LinkedHashMap<String,List<Itembean>>();
    	
  
		for(Itembean item : _items) 
    	{
    		if(map.containsKey(item.Location))
    		{
    			List<Itembean> items = map.get(item.Location);
    			items.add(item);
    			map.put(item.Location, items);
    		}else 
    		{
    			List<Itembean> items = new ArrayList<Itembean>();
    			items.add(item);
    			map.put(item.Location, items);
    		}
    	}
    	
    	for(Map.Entry<String, List<Itembean>> location : map.entrySet()) 
    	{
    		Locationbean bean = Constrant.locations.get(location.getKey());
    		
    		if(zoneMap.containsKey(bean.ZoneCode))
    		{
    			LinkedHashMap<String,List<Itembean>> items = zoneMap.get(bean.ZoneCode);
    			items.put(location.getKey(),location.getValue());
    			zoneMap.put(bean.ZoneCode, items);
    			zoneCount.put(bean.ZoneCode,zoneCount.get(bean.ZoneCode)+location.getValue().size());
    		}else 
    		{
    			LinkedHashMap<String,List<Itembean>> items = new LinkedHashMap<String,List<Itembean>>();
    			items.put(location.getKey(),location.getValue());
    			zoneMap.put(bean.ZoneCode, items);
    			zoneCount.put(bean.ZoneCode, location.getValue().size());
    		}
    	}
    	
    	
	}
	
	
	private  Object putDataToTable(int code) 
	{
		LinkedHashMap<String,List<Itembean>> zoneData = zoneMap.get(code);
       
		if(zoneData == null)
	 	    return null;
		Object rowData[][] = new Object[zoneData.size()][2];
    	int rowIndex = 0;
    	
    		
    	for(Map.Entry<String, List<Itembean>> location : zoneData.entrySet()) 
    	{
    		for(int j = 0; j < 2; j++) 
		    	{
		    		rowData[rowIndex][0] =  location.getKey();
		    		rowData[rowIndex][1] =  location.getValue().size();
		    		
		    	}
    		queryModelCount +=location.getValue().size();
    		rowIndex++;
    	}
    	return rowData;
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
        
       
        if(!_items.isEmpty())
        {
        	
        	TreeMap<String,List<Itembean>> map = new TreeMap<String,List<Itembean>>();
        	modelMapingNumber =  new HashMap<String,String>();
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
        	
        	final Object[][] rowData = new Object[map.size()][2];
  		    int totalCount = 0;
        	int rowIndex = 0;
        	for(Map.Entry<String, List<Itembean>> location : map.entrySet()) 
        	{
        		for(int j = 0; j < 2; j++) 
 		    	{
 		    		rowData[rowIndex][0] =  Constrant.models.get(location.getKey()).Model;
 		    		rowData[rowIndex][1] =  location.getValue().size();
 		    		modelMapingNumber.put(Constrant.models.get(location.getKey()).Model, location.getKey());
 		    	}
        		totalCount +=location.getValue().size();
        		rowIndex++;
        	}
        	
        	modelLabel.setText("Location : "+_items.get(0).Location+"( Zone "+Constrant.locations.get(_items.get(0).Location).ZoneCode+")                                ");
        	modelText.setText("                         TOTAL: "+totalCount);
        	final Class[] columnClass = new Class[] {  String.class, Integer.class};
     		
        	Object columnNames[] = { "MODEL", "QUANTITY" };
            DefaultTableModel model = new DefaultTableModel(rowData, columnNames) {
     			   @Override
     			   public boolean isCellEditable(int row, int column)
     			   {
     				   		Object model = modelMapingNumber.get(rowData[row][0]);
     				   		List<Itembean> items =queryModel(String.valueOf(model));
     				   		setModelLayOut1(items);
     						  	        return false;
     			   }
     			   @Override
     			   public Class<?> getColumnClass(int columnIndex)
     			   {
     						  	      return columnClass[columnIndex];
     			   }
     			   };
             
           
       		JTable table = new JTable(model);
       		table.setFont(font);
       		JScrollPane scrollPane = new JScrollPane(table);
       		scrollPane.setBounds(230,200,200,25);
       		frame.add(scrollPane);
        }
        
       

	  
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
	
   private List<Itembean> queryLocation(String Location) 
   {
	   FGRepositoryImplRetrofit fgInventory = new FGRepositoryImplRetrofit();
	   List<Itembean> items = null;
		try {
			items = (ArrayList<Itembean>)fgInventory.getItemsByLocation(Integer.valueOf(Location));			
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return items;
   }
   
   private List<Itembean> queryModel(String modelNo) 
   {
	   FGRepositoryImplRetrofit fgInventory = new FGRepositoryImplRetrofit();
	   List<Itembean> items = null;
		try {
			items = (ArrayList<Itembean>)fgInventory.getItemsByModel(Integer.valueOf(modelNo));			
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return items;
   }

}
