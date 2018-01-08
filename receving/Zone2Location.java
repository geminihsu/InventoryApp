package spirit.fitness.scanner.receving;

import java.awt.Color;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import spirit.fitness.scanner.receving.ItemsPannel;

public class Zone2Location  implements ActionListener{

	/**
	 * Create the application.
	 */
	
	private  JButton[] btnZoneCode;
	protected JFrame frame;
	private String items;
	private int assignType;

	public Zone2Location(String list, int type) {
		items = list;
		assignType = type;
		initialize();
	}


	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
			
		 JFrame.setDefaultLookAndFeelDecorated(true);
		    JDialog.setDefaultLookAndFeelDecorated(true);
		    frame=new JFrame("Zone 2 Layout");
		    frame.setSize(600,600); 
		    frame.setLocationRelativeTo(null);  
		    frame.setVisible(true); 
		    Container cp=frame.getContentPane();
		    cp.setLayout(new GridLayout(0,2));
		    
		    btnZoneCode = new JButton[38];
		    Font font = new Font("Verdana", Font.BOLD, 18);
		    
		    int index = 701;
		    for(JButton btn : btnZoneCode) 
		    {
		    	if(index == 891)
		    		index++;
		    	
		    	btn=new JButton(String.valueOf(index));    
		    	btn.setFont(font);
		    	btn.addActionListener(this);
		    	cp.add(btn);
		    	
		    	final String content = String.valueOf(index);
		    	//btn.addActionListener(this);
		    	btn.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						frame.dispose();
				    	frame.setVisible(false);
				    	
						ItemsPannel window = new ItemsPannel(items,content,assignType);
				    	window.frame.setVisible(true);				
						
					}});
		    	
		    	if(index == 722 || index == 772) 
		    	{
		    		btn.setEnabled(false);
		    	}
		    	
		    	
		    	index++;
		    	if(index % 10 > 2) 
		    	{
		    		index = index + 10;
		    		index = index / 10;
		    		index = index * 10 + 1;
		    		
		    		if(index == 881)
		    			index++;
		    	}
		    	
		    }
		   
		   
		   
		    
		   
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
		 String btn="";
		 	
		    if (e.getSource()==btnZoneCode) 
		    {
		    	
		    	ItemsPannel window = new ItemsPannel(items,"Return("+btnZoneCode[0].getText().toString()+")",assignType);
		    	window.frame.setVisible(true);
		    }
		    else if (e.getSource()==btnZoneCode[1]) 
		    {
		    	ItemsPannel window = new ItemsPannel(items,"Return("+btnZoneCode[1].getText().toString()+")",assignType);
		    	window.frame.setVisible(true);
		    	
		    }else if (e.getSource()==btnZoneCode[2])
		    {
		    	ItemsPannel window = new ItemsPannel(items,"Return("+btnZoneCode[2].getText().toString()+")",assignType);
		    	window.frame.setVisible(true);
		    	
		    }
		    
		   /* JOptionPane.showMessageDialog(f,
		      "the" + btn,
		      "problem",JOptionPane.INFORMATION_MESSAGE);
*/
		
	}

}
