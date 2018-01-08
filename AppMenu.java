package spirit.fitness.scanner;

import java.awt.Color;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import spirit.fitness.scanner.common.Constrant;
import spirit.fitness.scanner.inquiry.QueryPannel;
import spirit.fitness.scanner.inquiry.QueryResult;
import spirit.fitness.scanner.model.Itembean;
import spirit.fitness.scanner.model.Modelbean;
import spirit.fitness.scanner.receving.ItemsPannel;
import spirit.fitness.scanner.restful.FGRepositoryImplRetrofit;
import spirit.fitness.scanner.restful.ModelRepositoryImplRetrofit;

public class AppMenu  implements ActionListener{

	/**
	 * Create the application.
	 */
	
	private  JButton btnRecving,btnMoving,btnInQuiry,btnShipping,btnReport,btnLoading;
	private JFrame frame;
	public AppMenu() {
		initialize();
		loadModel();
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AppMenu window = new AppMenu();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
			
		 JFrame.setDefaultLookAndFeelDecorated(true);
		    JDialog.setDefaultLookAndFeelDecorated(true);
		    frame=new JFrame("FG Inventory App");
		    frame.setSize(1000,600); 
		    frame.setLocationRelativeTo(null);  
		    frame.setVisible(true); 
		    Container cp=frame.getContentPane();
		    cp.setLayout(new GridLayout(0,3));
		    
		    Font font = new Font("Verdana", Font.BOLD, 30);
		    btnRecving=new JButton("Receving");  
		    btnRecving.setFont(font);
		    btnMoving=new JButton("Moving");   
		    btnMoving.setFont(font);
		    btnInQuiry=new JButton("InQuiry");  
		    btnInQuiry.setFont(font);
		    btnShipping=new JButton("Shipping");
		    btnShipping.setFont(font);
		    btnReport=new JButton("Report");
		    btnReport.setFont(font);
		    btnLoading=new JButton("Loading");
		    btnLoading.setFont(font);
		    //btnRecving.setBounds(20,20,100,40);
		    //btnMoving.setBounds(150,20,100,40);
		    //btnInQuiry.setBounds(280,20,100,40);
		    //btnShipping.setBounds(410,20,100,40);
		    btnRecving.setMnemonic('O');  
		    btnMoving.setMnemonic('C');
		    btnInQuiry.setMnemonic('Q');
		    btnRecving.addActionListener(this);
		    btnMoving.addActionListener(this);
		    btnInQuiry.addActionListener(this);
		    cp.add(btnRecving);
		    cp.add(btnMoving);
		    cp.add(btnInQuiry);
		    cp.add(btnShipping);
		    cp.add(btnReport);
		    //cp.add(btnLoading);
		    frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		    frame.addWindowListener(new WindowAdapter() {
		      public void windowClosing(WindowEvent e) {
		        int result=JOptionPane.showConfirmDialog(frame,
		                   "Do you want to close the app?",
		                   "The app will be close",
		                   JOptionPane.YES_NO_OPTION,
		                   JOptionPane.WARNING_MESSAGE);
		        if (result==JOptionPane.YES_OPTION) {System.exit(0);}
		        }    
		      });
	
}

	@Override
	public void actionPerformed(ActionEvent e) {
		 String btn="";
		    if (e.getSource()==btnRecving) 
		    {
		    	//btnRecving.setEnabled(false);
		    	ItemsPannel window = new ItemsPannel(ItemsPannel.RECEVING);
		    	window.frame.setVisible(true);
		    }
		    else if (e.getSource()==btnMoving) 
		    {
		    	ItemsPannel window = new ItemsPannel(ItemsPannel.MOVING);
		    	window.frame.setVisible(true);
		    }
		    else if (e.getSource()==btnInQuiry)
		    {
		    	QueryPannel window = new QueryPannel();
		    	window.frame.setVisible(true);
		    	
		    } else if (e.getSource()==btnLoading)
		    {
		    	
		    	
		    }
		    
		   /* JOptionPane.showMessageDialog(f,
		      "the" + btn,
		      "problem",JOptionPane.INFORMATION_MESSAGE);
*/
		
	}
	
	//Loading Models data from Server 
	private void loadModel() 
	{
		//loading model and location information from Server
    	ModelRepositoryImplRetrofit fgModels = new ModelRepositoryImplRetrofit();
		
		try {
			List<Modelbean> items = (ArrayList<Modelbean>)fgModels.getAllItems();			
		   
			HashMap<String,Modelbean> map = new HashMap<>();
			for(Modelbean i : items) 
		    {
				map.put(i.ModelNo, i);
		    }
			
			Constrant.models = map;
			
		} catch (NumberFormatException x) {
			// TODO Auto-generated catch block
			x.printStackTrace();
		} catch (Exception x) {
			// TODO Auto-generated catch block
			x.printStackTrace();
		}
	}

}
