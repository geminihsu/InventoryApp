package spirit.fitness.scanner.receving;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
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

import spirit.fitness.scanner.AppMenu;
import spirit.fitness.scanner.restful.FGRepositoryImplRetrofit;
import spirit.fitness.scanner.restful.HttpRestApi;
import spirit.fitness.scanner.util.LocationHelper;
import spirit.fitness.scanner.model.Itembean;

public class ItemsPannel implements ActionListener{

	public final static int RECEVING = 0;
	public final static int MOVING = 1;
	
	public JFrame frame;
	private String items;

	public ItemsPannel(int type) {
		initialize(type);
	      
	}
	
	public ItemsPannel(String content,String location,int type) {

		//initialize();
		displayTable(content,location,type);  
	}
	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize(int type) {
		 	JFrame frame = new JFrame();
		 	frame.setTitle("Item List");
		    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    frame.setLocationRelativeTo(null);  
			frame.setBounds(500, 200, 400, 670);
		    JPanel basic = new JPanel();
		    
			Font font = new Font("Verdana", Font.BOLD, 20);
			TitledBorder title = new TitledBorder("Please start scanner barcode:");
			title.setTitleFont(font);

			JLabel ltotal = new JLabel("Total: 0");
  		 	ltotal.setFont(font);
  		 	ltotal.setBounds(0, 387, 89, 23);
  		 	basic.add(ltotal);

			basic.setBorder(title);
		
			
			
			

			JTextArea inputSN = new JTextArea(20,15);
			String content = "";
			
			for(int i = 1; i < 10; i++) 
			{
				content+="158012171100350"+i +"\n";
			}
			
			for(int i = 10; i < 50; i++) 
			{
				content+="15801217110035"+i +"\n";
			}
			//inputSN.setText("12312312312312313212312312123121111111111111111111111111111111\n12312312312312313212312312123121111111111111111111111111111111\n12312312312312313212312312123121111111111111111111111111111111\n12312312312312313212312312123121111111111111111111111111111111\n12312312312312313212312312123121111111111111111111111111111111\n12312312312312313212312312123121111111111111111111111111111111\n12312312312312313212312312123121111111111111111111111111111111\n12312312312312313212312312123121111111111111111111111111111111\n12312312312312313212312312123121111111111111111111111111111111\n12312312312312313212312312123121111111111111111111111111111111\n12312312312312313212312312123121111111111111111111111111111111\n12312312312312313212312312123121111111111111111111111111111111\n12312312312312313212312312123121111111111111111111111111111111\n12312312312312313212312312123121111111111111111111111111111111\n12312312312312313212312312123121111111111111111111111111111111\n12312312312312313212312312123121111111111111111111111111111111\n12312312312312313212312312123121111111111111111111111111111111\n12312312312312313212312312123121111111111111111111111111111111\n12312312312312313212312312123121111111111111111111111111111111\n12312312312312313212312312123121111111111111111111111111111111\n12312312312312313212312312123121111111111111111111111111111111\n");
			
			inputSN.setText(content);
			inputSN.getDocument().addDocumentListener(new DocumentListener() {

		        @Override
		        public void removeUpdate(DocumentEvent e) {
		        	ltotal.setText("Total: "+inputSN.getLineCount());
		        }

		        @Override
		        public void insertUpdate(DocumentEvent e) {
		        	String[] item = inputSN.getText().toString().split("\n");
		        	if(item.length>1)
		        		ltotal.setText("Total: "+item.length);
		        	else
		        		ltotal.setText("0");
		        	if(e.getLength() >1 && e.getLength()<16) 
		        	{
		        		JOptionPane.showMessageDialog(null, "Serial Number Error!");
		        	}
		        	
                    System.out.println(e.getLength());
		        }

		        @Override
		        public void changedUpdate(DocumentEvent arg0) {
		        
		        }
		    });
			
			JScrollPane  scrollPanel1 = new JScrollPane(inputSN);
			inputSN.setFont(font);
			//scrollPanel1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);  
			
			inputSN.setBounds(0, 387,50, 23);
			basic.add(scrollPanel1);
			
			
			JButton btnDone = new JButton("Assign");
			btnDone.setFont(font);
			
			btnDone.setBounds(312, 387, 89, 23);
			basic.add(btnDone);
			
			
			JButton btnDefault = new JButton("Default");
			btnDefault.setFont(font);
			
			btnDefault.setBounds(512, 387, 89, 23);
			basic.add(btnDefault);
			
			JButton btnRest = new JButton("Rest");
			btnRest.setFont(font);
			
			btnRest.setBounds(712, 387, 89, 23);
			basic.add(btnRest);
			
			btnRest.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					inputSN.setText("");
					ltotal.setText("Total: 0");
				
				}
			});
			
			btnDefault.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if(inputSN.getText().isEmpty()) 
					{
						JOptionPane.showMessageDialog(null, "Please scan bar code item!");	
					}
					else
					{
						frame.setVisible(false); 
						frame.dispose();
			    	
						displayTable(inputSN.getText().toString(),"000",type);
					}
				
				}
			});
			btnDone.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
				
				if(inputSN.getText().isEmpty())
		  		    JOptionPane.showMessageDialog(null, "Please scan bar code item!");	
				else {
					items = inputSN.getText().toString();
					frame.setVisible(false); 
			    	frame.dispose();
			    	
					ZoneMenu window = new ZoneMenu(items,type);
				    window.frame.setVisible(true);
				     // prompt the user to enter their name
//				    String location = JOptionPane.showInputDialog(frame, "Please enter the location code");
//				    if(!location.isEmpty()) 
//				    {
//				    	displayTable(location);
//				    }
				}
				}
			});
			
			
			frame.getContentPane().add(basic);
			//frame.setSize(500, 500);
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
	
	private void displayTable(String items,String location,int type) 
	{
		
		 	JFrame frame = new JFrame();
		    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    frame.setLocationRelativeTo(null);  
		    frame.setBounds(500, 200, 1000, 600);
		    String[] itemList = items.split("\n");
		    
		    //avoid repeat serial number
		    HashSet<String> set = new HashSet<String>();
		    
		    for(String s : itemList) 
		    {
		    		set.add(s);
		    }
		    List sortedList = new ArrayList(set);
		    //sort the all serial number ascending order
		    Collections.sort(sortedList);
		    
		    Object rowData[][] = new Object[sortedList.size()][3];
		    
		    for(int i = 0; i <sortedList.size(); i++ ) 
		    {
		    	for(int j = 0; j < 3; j++) 
		    	{
		    		rowData[i][0] =  sortedList.get(i);
		    		rowData[i][1] =  ((String) sortedList.get(i)).substring(0, 7);
		    		rowData[i][2] =  location;
		    	}
		    }
		    
		    Object columnNames[] = { "Serial Number", "Model", "Location"+"("+LocationHelper.DisplayZoneCode(LocationHelper.MapZoneCode(location))+")"};
	  		Font font = new Font("Verdana", Font.BOLD, 18);
	  		JTable table = new JTable(rowData, columnNames);
	  		table.setFont(font);

		    JScrollPane scrollPane = new JScrollPane(table);
		    frame.add(scrollPane, BorderLayout.CENTER);
		    
		    JButton btnDone = new JButton("Submit");
		    btnDone.setFont(font);
  		 	btnDone.setBounds(312, 387, 89, 23);
  		 	
  		 	
  		 	
  		 	btnDone.addActionListener(new ActionListener() {
  				public void actionPerformed(ActionEvent e) {
  					
  									
  					 //String book = "{\"Seq\":"+91+",\"SN\":\"1858151709001848\",\"Date\":\"2017-12-13 16:14:02.343\",\"Location\":\"051\",\"ModelNo\":\"185815\"}";
  				 // String result = "{\"Seq\":"+92+",\"SN\":\"1858151709001848\",\"Date\":\"2017-12-13 16:14:02.343\",\"Location\":\"051\",\"ModelNo\":\"185815\"}";
  				 
  				
		  		
  		    	EventQueue.invokeLater(new Runnable() {
  					public void run() {
  						try {
  							
  							List<Itembean> items = new ArrayList<Itembean>();
  							int index =148;
  							String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()); 
  					  		for(String item : itemList) 
  					  		 {
  					  		    Itembean _item = new Itembean();
  					  		    _item.seq = index;
  					  		    _item.SN  = item;
  					  		    _item.date = timeStamp;
  					  		    _item.Location = location;
  					  		    _item.ModelNo = item.substring(0,6); 
  					  		    items.add(_item);
  					  		    index++;
  					  		 } 
  					  		
  							FGRepositoryImplRetrofit fgRepository = new FGRepositoryImplRetrofit();
  							
//  					     
  							
  							if(type == RECEVING) {
  								String fg = fgRepository.createItem(items).get(0).SN;
  							
  								if(!fg.isEmpty()) 
  								{
  								 frame.dispose();
  								 frame.setVisible(false);
  								 JOptionPane.showMessageDialog(null, "Insert Data Success!");
  								}
  							}else if(type == MOVING) {
  								String fg = fgRepository.updateItem(items).get(0).SN;
  							
  								if(!fg.isEmpty()) 
  								{
  								 frame.dispose();
  								 frame.setVisible(false);
  								 JOptionPane.showMessageDialog(null, "Update Data Success!");
  								}
  							}
  							
  						} catch (Exception e) {
  							e.printStackTrace();
  						}
  					}
  				});
  				  //HttpRestApi.postData(result);
  				}
  			});
  		    frame.add(btnDone, BorderLayout.SOUTH);
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
