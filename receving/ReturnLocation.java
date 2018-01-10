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
import spirit.fitness.scanner.receving.Zone1Location.Zone1CodeCallBackFunction;
import spirit.fitness.scanner.receving.Zone2Location.Zone2CodeCallBackFunction;

public class ReturnLocation implements ActionListener {

	/**
	 * Create the application.
	 */

	private JButton btnReturn1, btnReturn2, btnReturn3;
	protected JFrame frame;
	private String items;
	private int assignType = 0;

	public ReturnLocation(String content, int type) {
		items = content;
		assignType = type;
		if (assignType != -1)
			initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		frame = new JFrame("FG Inventory App");
		frame.setSize(600, 300);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		Container cp = frame.getContentPane();
		cp.setLayout(new GridLayout(0, 1));

		Font font = new Font("Verdana", Font.BOLD, 25);
		btnReturn1 = new JButton("881");
		btnReturn1.setFont(font);
		btnReturn2 = new JButton("891");
		btnReturn2.setFont(font);
		btnReturn3 = new JButton("901");
		btnReturn3.setFont(font);

		btnReturn1.setMnemonic('O');
		btnReturn2.setMnemonic('C');
		btnReturn3.setMnemonic('Q');
		btnReturn1.addActionListener(this);
		btnReturn2.addActionListener(this);
		btnReturn3.addActionListener(this);
		cp.add(btnReturn1);
		cp.add(btnReturn2);
		cp.add(btnReturn3);

		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int result = JOptionPane.showConfirmDialog(frame, "Do you want to close the app?",
						"The app will be close", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (result == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String btn = "";
		this.frame.setVisible(false);
		this.frame.dispose();

		if (e.getSource() == btnReturn1) {
			if (zoneCodeReturnCallBackFunction != null) {
				zoneCodeReturnCallBackFunction.getZoneCode(btnReturn1.getText().toString());
			} else {
				ItemsPannel window = new ItemsPannel(items, btnReturn1.getText().toString(), assignType);
				window.frame.setVisible(true);
			}
		} else if (e.getSource() == btnReturn2) {
			if (zoneCodeReturnCallBackFunction != null) {
				zoneCodeReturnCallBackFunction.getZoneCode(btnReturn2.getText().toString());
			} else {
				ItemsPannel window = new ItemsPannel(items, btnReturn2.getText().toString(), assignType);
				window.frame.setVisible(true);
			}

		} else if (e.getSource() == btnReturn3) {
			if (zoneCodeReturnCallBackFunction != null) {
				zoneCodeReturnCallBackFunction.getZoneCode(btnReturn3.getText().toString());
			} else {
				ItemsPannel window = new ItemsPannel(items, btnReturn3.getText().toString(), assignType);
				window.frame.setVisible(true);
			}
		}

		/*
		 * JOptionPane.showMessageDialog(f, "the" + btn,
		 * "problem",JOptionPane.INFORMATION_MESSAGE);
		 */

	}

	// retrieve return code number
	private static ZoneCodeReturnCallBackFunction zoneCodeReturnCallBackFunction;

	public void setZoneCodeReturnCallBackFunction(ZoneCodeReturnCallBackFunction _zoneCodeReturnCallBackFunction) {
		zoneCodeReturnCallBackFunction = _zoneCodeReturnCallBackFunction;

	}

	public ZoneCodeReturnCallBackFunction getZoneCodeReturnCallBackFunction() 
	{
		return zoneCodeReturnCallBackFunction;
	}

	public void clearZoneCodeReturnCallBackFunction() {
		zoneCodeReturnCallBackFunction = null;
	}

	public interface ZoneCodeReturnCallBackFunction {
		public void getZoneCode(String code);

	}

}
