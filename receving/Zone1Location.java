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
import spirit.fitness.scanner.receving.ReturnLocation.ZoneCodeReturnCallBackFunction;

public class Zone1Location implements ActionListener {

	/**
	 * Create the application.
	 */

	private JButton[] btnZoneCode;
	protected JFrame frame;
	private String items;
	private int assignType = 0;

	public Zone1Location(String list, int type) {
		items = list;
		assignType = type;
		if (assignType != -1)
			initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		JFrame.setDefaultLookAndFeelDecorated(false);
	    JDialog.setDefaultLookAndFeelDecorated(false);
		frame = new JFrame("Zone 1 Layout");
		frame.setSize(800, 600);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		Container cp = frame.getContentPane();
		cp.setLayout(new GridLayout(7, 10));

		btnZoneCode = new JButton[70];
		Font font = new Font("Verdana", Font.BOLD, 18);

		int index = 0;

		for (JButton btn : btnZoneCode) {
			String label = "";

			if (index / 10 == 0)
				label = "00";
			else
				label = "0";
			btn = new JButton(label + String.valueOf(index));
			btn.setFont(font);
			final String content = label + String.valueOf(index);
			// btn.addActionListener(this);
			btn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					if (zone1CodeCallBackFunction != null) {
						zone1CodeCallBackFunction.getZoneCode(content);
						frame.dispose();
						frame.setVisible(false);
					} else {
						frame.dispose();
						frame.setVisible(false);

						ItemsPannel window = new ItemsPannel(items, content, assignType);
						window.frame.setVisible(true);
					}

				}
			});
			cp.add(btn);
			index++;

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
		String btn = "";

		if (e.getSource() == btnZoneCode) {

			ItemsPannel window = new ItemsPannel(items, "Return(" + btnZoneCode[0].getText().toString() + ")",
					assignType);
			window.frame.setVisible(true);
		} else if (e.getSource() == btnZoneCode[1]) {
			ItemsPannel window = new ItemsPannel(items, "Return(" + btnZoneCode[1].getText().toString() + ")",
					assignType);
			window.frame.setVisible(true);

		} else if (e.getSource() == btnZoneCode[2]) {
			ItemsPannel window = new ItemsPannel(items, "Return(" + btnZoneCode[2].getText().toString() + ")",
					assignType);
			window.frame.setVisible(true);

		}

		/*
		 * JOptionPane.showMessageDialog(f, "the" + btn,
		 * "problem",JOptionPane.INFORMATION_MESSAGE);
		 */

	}

	// retrieve return code number
	private static Zone1CodeCallBackFunction zone1CodeCallBackFunction;

	public void setZone1CodeCallBackFunction(Zone1CodeCallBackFunction _zone1CodeCallBackFunction) {
		zone1CodeCallBackFunction = _zone1CodeCallBackFunction;

	}
	
	public Zone1CodeCallBackFunction getZone1CodeCallBackFunction() 
	{
		return zone1CodeCallBackFunction;
	}

	public void clearZone1CodeCallBackFunction() {
		zone1CodeCallBackFunction = null;
	}

	public interface Zone1CodeCallBackFunction {
		public void getZoneCode(String code);

	}

}
