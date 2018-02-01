package spirit.fitness.scanner.zonepannel;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import spirit.fitness.scanner.common.Constrant;
import spirit.fitness.scanner.receving.ItemsPannel;
import spirit.fitness.scanner.zonepannel.Zone2Location.Zone2CodeCallBackFunction;

public class ZoneMenu implements ActionListener {

	/**
	 * Create the application.
	 */

	private JButton btnZone1, btnZone2, btnReturn, btnDisplayRoom;
	public JFrame frame;
	private String items;
	private int assignType;

	public ZoneMenu(String content, int type) {
		items = content;
		assignType = type;

		if (assignType != -1)
			initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		//JFrame.setDefaultLookAndFeelDecorated(false);
	    //JDialog.setDefaultLookAndFeelDecorated(false);
		frame = new JFrame("FG Inventory App");
		frame.setSize(600, 300);
		frame.setLocationRelativeTo(null);
		frame.setUndecorated (true);
		frame.setResizable(false);
		frame.setVisible(true);
		
		JPanel borderedPanel = new JPanel();

	    //Use any border you want, eg a nice blue one
	    borderedPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Constrant.FRAME_BORDER_BACKGROUN_COLOR));

	    frame.setContentPane(borderedPanel);
		
		Container cp = frame.getContentPane();
		cp.setLayout(new GridLayout(0, 2));

		Font font = new Font("Verdana", Font.BOLD, 25);
		btnZone1 = new JButton("Zone 1");
		btnZone1.setFont(font);
		btnZone2 = new JButton("Zone 2");
		btnZone2.setFont(font);
		btnReturn = new JButton("Return");
		btnReturn.setFont(font);
		btnDisplayRoom = new JButton("Show Room");
		btnDisplayRoom.setFont(font);

		btnZone1.setMnemonic('O');
		btnZone2.setMnemonic('C');
		btnReturn.setMnemonic('Q');
		btnZone1.addActionListener(this);
		btnZone2.addActionListener(this);
		btnReturn.addActionListener(this);
		btnDisplayRoom.addActionListener(this);

		cp.add(btnZone1);
		cp.add(btnZone2);
		cp.add(btnReturn);
		cp.add(btnDisplayRoom);

		
		JPanel exitControl = new JPanel();
		exitControl.setLayout(new GridLayout(0, 3));
		JButton exit = new JButton(new AbstractAction("Prev"){

			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				frame.setVisible(false);
				
				ItemsPannel window = new ItemsPannel(ItemsPannel.RECEVING);
				window.frame.setVisible(true);
			}
		});
		
		JButton backButton = new JButton(new AbstractAction("Exit") {

			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				frame.setVisible(false);
			}
		});
		backButton.setBounds(500,20,50,50);
		backButton.setFont(font);
		backButton.setBackground(Constrant.BUTTON_BACKGROUN_COLOR);
		
		exit.setBounds(0,20,50,50);
		exit.setFont(font);
		exit.setBackground(Constrant.BUTTON_BACKGROUN_COLOR);
		backButton.setBounds(0,20,50,50);
		backButton.setFont(font);
		
		exitControl.add(exit);
		exitControl.add(backButton);
		
		
		
		exitControl.setBackground(Constrant.TABLE_COLOR);
		frame.getContentPane().setBackground(Constrant.TABLE_COLOR);
		frame.getContentPane().add(exitControl);
		
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
		String btn = "";
		if (e.getSource() == btnZone1) {
			this.frame.setVisible(false);
			this.frame.dispose();

			Zone1Location window = new Zone1Location(items, assignType);
			window.frame.setVisible(true);

		} else if (e.getSource() == btnZone2) {

			this.frame.setVisible(false);
			this.frame.dispose();

			Zone2Location window = new Zone2Location(items, assignType);
			window.frame.setVisible(true);

		} else if (e.getSource() == btnReturn) {
			this.frame.setVisible(false);
			this.frame.dispose();

			ReturnLocation window = new ReturnLocation(items, assignType);
			window.frame.setVisible(true);

		} else if (e.getSource() == btnDisplayRoom) {
			if (showRoomCodeCallBackFunction != null) {
				this.frame.setVisible(false);
				this.frame.dispose();
				showRoomCodeCallBackFunction.getZoneCode("888");
			} else {
				this.frame.setVisible(false);
				this.frame.dispose();

				ItemsPannel window = new ItemsPannel(items, "888", assignType);
				window.dialogFrame.setVisible(true);
			}

		}

	}

	// retrieve return code number
	private static ShowRoomCodeCallBackFunction showRoomCodeCallBackFunction;

	public void setShowRoomCodeCallBackFunction(ShowRoomCodeCallBackFunction _showRoomCodeCallBackFunction) {
		showRoomCodeCallBackFunction = _showRoomCodeCallBackFunction;

	}

	public ShowRoomCodeCallBackFunction getShowRoomCodeCallBackFunction() 
	{
		return showRoomCodeCallBackFunction;
	}

	public void clearShowRoomCodeCallBackFunction() {
		showRoomCodeCallBackFunction = null;
	}

	public interface ShowRoomCodeCallBackFunction {
		public void getZoneCode(String code);

	}

}
