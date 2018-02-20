package spirit.fitness.scanner.receving;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import spirit.fitness.scanner.common.Constrant;
import spirit.fitness.scanner.common.HttpRequestCode;
import spirit.fitness.scanner.restful.FGRepositoryImplRetrofit;
import spirit.fitness.scanner.restful.listener.InventoryCallBackFunction;
import spirit.fitness.scanner.util.LoadingFrameHelper;
import spirit.fitness.scanner.util.LocationHelper;
import spirit.fitness.scanner.util.PrinterHelper;
import spirit.fitness.scanner.zonepannel.ZoneMenu;
import spirit.fitness.scanner.model.Itembean;
import spirit.fitness.scanner.model.Modelbean;
import spirit.fitness.string.tableview.*;

public class ItemsPannel {

	public final static int RECEVING = 0;
	public final static int MOVING = 1;

	private static final String TEXT_SUBMIT = "text-submit";
	private static final String INSERT_BREAK = "insert-break";

	public JFrame frame;
	public JFrame scanResultFrame;
	public JFrame dialogFrame;
	private JTextArea inputSN;
	private String items;
	private String result;
	private int assignType;

	private JButton btnDone;

	private JProgressBar loading;
	private LoadingFrameHelper loadingframe;

	private FGRepositoryImplRetrofit fgRepository;

	public ItemsPannel(int type) {
		assignType = type;
		// initialize(type);
		scanInfo("",type);

	}

	public ItemsPannel(String content, String location, int type) {
		assignType = type;
		// displayTable(content, location, type);
		displayScanResultFrame(content, location, type);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void scanInfo(String prevTxt,int type) {

		scanResultFrame = new JFrame("");
		// Setting the width and height of frame
		scanResultFrame.setSize(330, 750);
		scanResultFrame.setLocationRelativeTo(null);
		scanResultFrame.setUndecorated(true);
		scanResultFrame.setResizable(false);

		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Constrant.FRAME_BORDER_BACKGROUN_COLOR));
		panel.setBackground(Constrant.BACKGROUN_COLOR);
		// adding panel to frame
		scanResultFrame.add(panel);

		scanPannel(panel,prevTxt, type);

		scanResultFrame.setBackground(Color.WHITE);
		scanResultFrame.setVisible(true);

		scanResultFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		scanResultFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {

				scanResultFrame.dispose();
				scanResultFrame.setVisible(false);
			}
		});

	}

	private void scanPannel(JPanel panel,String prevTxt, int type) {

		panel.setLayout(null);
		Font font = new Font("Verdana", Font.BOLD, 18);

		JLabel ltotal = new JLabel("Total: 0");
		ltotal.setFont(font);
		ltotal.setBounds(35, 550, 200, 50);
		panel.add(ltotal);

		inputSN = new JTextArea(20, 15);
		String content = "";

		for (int i = 1; i < 10; i++) {
			content += "158012130811120" + i + "\n";
		}

		for (int i = 10; i < 50; i++) {
			if (i == 20)
				continue;
			content += "15801213081112" + i + "\n";
		}
		content += "1580121308111250\n";

		inputSN.setText(prevTxt);

		InputMap input = inputSN.getInputMap();
		KeyStroke enter = KeyStroke.getKeyStroke("ENTER");
		KeyStroke shiftEnter = KeyStroke.getKeyStroke("shift ENTER");
		input.put(shiftEnter, INSERT_BREAK); // input.get(enter)) = "insert-break"
		input.put(enter, TEXT_SUBMIT);

		HashSet<String> set = new HashSet<String>();
		ActionMap actions = inputSN.getActionMap();
		actions.put(TEXT_SUBMIT, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// submitText();
				// System.out.println(TEXT_SUBMIT);
				inputSN.setText(inputSN.getText().toString() + "\n");
				String prev = inputSN.getText().toString();
				String[] item = inputSN.getText().toString().split("\n");

				boolean lenError = false;
				if (!set.contains(item[item.length - 1]) && item[item.length - 1].length() == 16) {

					set.add(item[item.length - 1]);
				} else {
					lenError = true;
					prev = prev.substring(0, prev.length() - (item[item.length - 1].length()) - 1);

				}

				if (lenError) {

					inputSN.setText(prev);
					// ltotal.setForeground(Color.RED);
					// ltotal.setText("<html>"+item[item.length-1] +"</br> size Error.</html>");
				} else {
					ltotal.setForeground(Color.BLACK);
					ltotal.setText("Total: " + set.size());
				}
			}
		});

		
		JScrollPane scrollPanel1 = new JScrollPane(inputSN);
		scrollPanel1.setBounds(35, 50, 265, 500);
		inputSN.setFont(font);
		panel.add(scrollPanel1);

		// Creating Reset button
		JButton defaultButton = new JButton("000");
		defaultButton.setFont(font);
		defaultButton.setBounds(35, 600, 125, 50);
		defaultButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (inputSN.getText().isEmpty())
					JOptionPane.showMessageDialog(null, "Please scan serial number.");
				else {
					
					displayScanResultFrame(inputSN.getText().toString(), "000", type);
				}
			}
		});

		panel.add(defaultButton);

		// Creating Assign Location button
		JButton locateButton = new JButton("Location");
		locateButton.setFont(font);
		locateButton.setBounds(175, 600, 125, 50);
		locateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (inputSN.getText().isEmpty())
					JOptionPane.showMessageDialog(null, "Please scan serial number.");
				else {

					items = inputSN.getText().toString();
					scanResultFrame.setVisible(false);
					scanResultFrame.dispose();

					if (type == MOVING) {
						loadingframe = new LoadingFrameHelper("Checking data...");
						loading = loadingframe.loadingSample("Checking data...");

						String[] itemList = items.split("\n");
						
						if(itemList.length == 0 && !inputSN.getText().toString().equals(""))
						{
							itemList = new String[0];
							itemList[0] = inputSN.getText().toString();
						}	
							
						List<Itembean> items = new ArrayList<Itembean>();

						String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
								.format(Calendar.getInstance().getTime());
						for (String item : itemList) {
							Itembean _item = new Itembean();

							_item.SN = item;
							_item.ModelNo = item.substring(0, 6);
							items.add(_item);

						}
						exceuteCallback();
						checkItemExits(items);
					} else {
						ZoneMenu window = new ZoneMenu(items, type);
						window.frame.setVisible(true);
					}

				}
			}
		});

		panel.add(locateButton);

		// Creating Reset button
		JButton clearButton = new JButton("Clear");
		clearButton.setFont(font);
		clearButton.setBounds(35, 665, 125, 50);
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int result = -1;
				if (inputSN.getText().toString().length() > 0) {
					result = JOptionPane.showConfirmDialog(frame, "Do you want to clear all items?", "",
							JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					if (result == JOptionPane.YES_OPTION) {
						inputSN.setText("");
						set.clear();
						ltotal.setForeground(Color.BLACK);
						ltotal.setText("Total : 0");
					}
				}

			}
		});

		panel.add(clearButton);

		// Creating Query button
		JButton exitButton = new JButton("Exit");
		exitButton.setFont(font);
		exitButton.setBounds(175, 665, 125, 50);
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				scanResultFrame.dispose();
				scanResultFrame.setVisible(false);
			}
		});

		panel.add(exitButton);

	}

	private void exceuteCallback() {

		fgRepository = new FGRepositoryImplRetrofit();
		fgRepository.setinventoryServiceCallBackFunction(new InventoryCallBackFunction() {

			@Override
			public void resultCode(int code) {
				// TODO Auto-generated method stub
				if (code == HttpRequestCode.HTTP_REQUEST_INSERT_DATABASE_ERROR) {
					JOptionPane.showMessageDialog(null, "Items already exit.");
					loadingframe.setVisible(false);
					loadingframe.dispose();

					dialogFrame.dispose();
					dialogFrame.setVisible(false);
				}
			}

			@Override
			public void getInventoryItems(List<Itembean> items) {
				
				
				
				if (!items.isEmpty()) {
					// progressMonitor.close();
					// task.done();
					loading.setValue(50);
					loadingframe.setVisible(false);
					loadingframe.dispose();

					if (assignType == RECEVING) {
						JOptionPane.showMessageDialog(null, "Insert Data Success!");

					}
					
					if (assignType == MOVING) {
						JOptionPane.showMessageDialog(null, "Update Data Success!");

					}

					if (scanResultFrame != null) {
						scanResultFrame.dispose();
						scanResultFrame.setVisible(false);
					}

					if (dialogFrame != null) {
						dialogFrame.dispose();
						dialogFrame.setVisible(false);
					}
				}
			}

			@Override
			public void checkInventoryItems(List<Itembean> items) {
				if (assignType == MOVING) {
					loadingframe.setVisible(false);
					loadingframe.dispose();
					// JOptionPane.showMessageDialog(null, "Update Data Success!");
					if (items.size() == 0) {
						
						ZoneMenu window = new ZoneMenu(inputSN.getText().toString(), MOVING);
						window.frame.setVisible(true);
					} else {
						checkScanResultFrame(items);
					}
				}
				
			}
		});

	}

	// display scan items by table
	private void displayTable(String content, String location, int type) {

		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Item Result");
		frame.setLocationRelativeTo(null);
		frame.setBounds(100, 100, 1000, 600);
		String[] itemList = content.split("\n");

		// avoid repeat serial number
		HashSet<String> set = new HashSet<String>();

		for (String s : itemList) {
			set.add(s);
		}
		List sortedList = new ArrayList(set);
		// sort the all serial number ascending order
		Collections.sort(sortedList);

		Object rowData[][] = new Object[sortedList.size()][3];

		for (int i = 0; i < sortedList.size(); i++) {
			for (int j = 0; j < 3; j++) {
				rowData[i][0] = sortedList.get(i);
				rowData[i][1] = ((String) sortedList.get(i)).substring(0, 7);
				rowData[i][2] = location;
			}
		}

		String zone = "";
		if (location.equals(String.valueOf(Constrant.ZONE_CODE_SHIPPING)))
			zone = "Shipping";
		else
			zone = LocationHelper.DisplayZoneCode(LocationHelper.MapZoneCode(location));
		Object columnNames[] = { "Serial Number", "Model", "Location" + "(" + zone + ")" };
		Font font = new Font("Verdana", Font.BOLD, 18);
		final Class[] columnClass = new Class[] { String.class, String.class, String.class };

		DefaultTableModel model = new DefaultTableModel(rowData, columnNames) {
			@Override
			public boolean isCellEditable(int row, int column) {

				return false;
			}

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				return columnClass[columnIndex];
			}
		};

		JTable table = new JTable(model);
		table.getTableHeader().setFont(font);
		table.getTableHeader().setBackground(Constrant.DISPALY_ITEMS_TABLE_COLOR);
		table.setBackground(Constrant.DISPALY_ITEMS_TABLE_COLOR);
		table.setFont(font);
		table.setRowHeight(40);

		JScrollPane scrollPane = new JScrollPane(table);
		frame.add(scrollPane, BorderLayout.CENTER);

		btnDone = new JButton("Submit");
		btnDone.setFont(font);
		btnDone.setBounds(312, 387, 89, 50);

		btnDone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// String book =
				// "{\"Seq\":"+91+",\"SN\":\"1858151709001848\",\"Date\":\"2017-12-13
				// 16:14:02.343\",\"Location\":\"051\",\"ModelNo\":\"185815\"}";
				// String result =
				// "{\"Seq\":"+92+",\"SN\":\"1858151709001848\",\"Date\":\"2017-12-13
				// 16:14:02.343\",\"Location\":\"051\",\"ModelNo\":\"185815\"}";
				btnDone.setEnabled(false);

				// progressMonitor = new ProgressMonitor(ItemsPannel.this, "Please wait...", "",
				// 0, 100);

				List<Itembean> items = new ArrayList<Itembean>();

				String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
				for (String item : itemList) {
					Itembean _item = new Itembean();

					_item.SN = item;
					_item.date = timeStamp;
					_item.Location = location;
					_item.ModelNo = item.substring(0, 6);
					items.add(_item);

				}

				if (type == RECEVING) {
					PrinterHelper print = new PrinterHelper();
					print.printItems(content);
				}

				loadingframe = new LoadingFrameHelper("Add data...");
				loading = loadingframe.loadingSample("Add data...");

				exceuteCallback();
				// displayLoadingBar();

				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {

							submitServer(type, items);

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				// HttpRestApi.postData(result);
			}
		});
		frame.add(btnDone, BorderLayout.SOUTH);
		// frame.setLocationRelativeTo(null);
		// frame.setSize(1000, 500);
		frame.setVisible(true);

		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {

				frame.dispose();
				frame.setVisible(false);

			}
		});

	}

	private void displayScanResultFrame(String content, String location, int type) {

		String[] itemList = content.split("\n");
		HashSet<String> set = new HashSet<String>();

		for (String s : itemList) {
			set.add(s);
		}
		List sortedList = new ArrayList(set);
		// sort the all serial number ascending order
		Collections.sort(sortedList);

		List<Integer> noContinue = new ArrayList<Integer>();

		int startIndex = Integer.valueOf(((String) sortedList.get(0)).substring(10, 16));
		String modelNo = ((String) sortedList.get(0)).substring(0, 6);
		int skip = 0;
		/*
		 * for (int i = 0; i < sortedList.size(); i++) { if (Integer.valueOf(((String)
		 * sortedList.get(i)).substring(10, 16)) == startIndex) startIndex = startIndex
		 * + 1; else { skip = startIndex; if (((String) sortedList.get(i)).substring(0,
		 * 6).endsWith(modelNo)) { startIndex = Integer.valueOf(((String)
		 * sortedList.get(i)).substring(10, 16));
		 * noContinue.add(Integer.valueOf(((String) sortedList.get(i)).substring(10,
		 * 16))); startIndex = startIndex + 1; } } }
		 */

		String result = "";
		if (noContinue.size() == 0 && sortedList.size() == 1) {
			result = "SN : " + Integer.valueOf(((String) sortedList.get(0)).substring(10, 16));
		} else
			result = "SN : " + Integer.valueOf(((String) sortedList.get(0)).substring(10, 16)) + "~"
					+ Integer.valueOf(((String) sortedList.get(sortedList.size() - 1)).substring(10, 16));
		// else if (noContinue.size() == 0 && sortedList.size() > 1)
		// result = "SN : " + Integer.valueOf(((String) sortedList.get(0)).substring(10,
		// 16)) + "~"
		// + Integer.valueOf(((String) sortedList.get(sortedList.size() -
		// 1)).substring(10, 16));
		// else {
		// result = "SN : " + Integer.valueOf(((String) sortedList.get(0)).substring(10,
		// 16)) + "~" + (skip - 1);

		// int noContinueStartIndex = noContinue.get(0);
		// result += " <br/>"+((String) sortedList.get(0)).substring(0,10)
		// +noContinueStartIndex +"~";
		/*
		 * for (int i = 1; i < noContinue.size(); i++) { result += noContinue.get(i) -
		 * 1; }
		 */

		// result += "," + (noContinue.get(noContinue.size() - 1) + "~"
		// + Integer.valueOf(((String) sortedList.get(sortedList.size() -
		// 1)).substring(10, 16)));
		// }
		dialogFrame = new JFrame("Query Pannel");
		// Setting the width and height of frame
		dialogFrame.setSize(600, 400);
		dialogFrame.setLocationRelativeTo(null);
		dialogFrame.setUndecorated(true);
		dialogFrame.setResizable(false);

		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Constrant.FRAME_BORDER_BACKGROUN_COLOR));

		panel.setBackground(Constrant.BACKGROUN_COLOR);
		// adding panel to frame
		dialogFrame.add(panel);

		panel.setLayout(null);
		Font font = new Font("Verdana", Font.BOLD, 18);
		Modelbean model = Constrant.models.get((((String) sortedList.get(0)).substring(0, 6)));

		String zoneCode = LocationHelper.DisplayZoneCode(LocationHelper.MapZoneCode(location));
		// Creating JLabel
		JLabel modelLabel = new JLabel("<html>Do you want to assign all items :" + " <br/>" + "Model :" + model.ModelNo
				+ "(" + ((String) sortedList.get(0)).substring(0, 6) + ") <br/>" + "Total : " + sortedList.size()
				+ " <br/>" + result + " <br/>" + "to location " + "[" + zoneCode + "][" + location + "] ?</html>");

		/*
		 * This method specifies the location and size of component. setBounds(x, y,
		 * width, height) here (x,y) are cordinates from the top left corner and
		 * remaining two arguments are the width and height of the component.
		 */
		modelLabel.setBounds(30, 0, 500, 200);
		modelLabel.setFont(font);
		panel.add(modelLabel);

		JButton ok = new JButton("Confirm");
		ok.setBounds(50, 330, 150, 50);
		ok.setFont(font);
		panel.add(ok);

		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// String book =
				// "{\"Seq\":"+91+",\"SN\":\"1858151709001848\",\"Date\":\"2017-12-13
				// 16:14:02.343\",\"Location\":\"051\",\"ModelNo\":\"185815\"}";
				// String result =
				// "{\"Seq\":"+92+",\"SN\":\"1858151709001848\",\"Date\":\"2017-12-13
				// 16:14:02.343\",\"Location\":\"051\",\"ModelNo\":\"185815\"}";
				ok.setEnabled(false);

				// progressMonitor = new ProgressMonitor(ItemsPannel.this, "Please wait...", "",
				// 0, 100);

				List<Itembean> items = new ArrayList<Itembean>();

				String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
				for (String item : itemList) {
					Itembean _item = new Itembean();

					_item.SN = item;
					_item.date = timeStamp;
					_item.Location = location;
					_item.ModelNo = item.substring(0, 6);
					items.add(_item);

				}

				if (type == RECEVING) {
					PrinterHelper print = new PrinterHelper();
					print.printItems(content);
				}

				loadingframe = new LoadingFrameHelper("Add data...");
				loading = loadingframe.loadingSample("Add data...");

				exceuteCallback();
				// displayLoadingBar();

				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {

							submitServer(type, items);

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				// HttpRestApi.postData(result);
			}
		});

		JButton prev = new JButton("Prev");
		prev.setBounds(220, 330, 150, 50);
		prev.setFont(font);
		panel.add(prev);

		prev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialogFrame.dispose();
				dialogFrame.setVisible(false);
				dialogFrame = null;
				scanInfo(content,assignType);
			}
		});
		
		JButton cancel = new JButton("Exit");
		cancel.setBounds(400, 330, 150, 50);
		cancel.setFont(font);
		panel.add(cancel);

		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialogFrame.dispose();
				dialogFrame.setVisible(false);
			}
		});

		// frame.setUndecorated(true);
		// frame.getRootPane().setWindowDecorationStyle(JRootPane.COLOR_CHOOSER_DIALOG);
		dialogFrame.setBackground(Color.WHITE);
		dialogFrame.setVisible(true);
		// frame.setDefaultLookAndFeelDecorated(true);
		dialogFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		dialogFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {

				dialogFrame.dispose();
				dialogFrame.setVisible(false);
			}
		});
	}

	private void checkScanResultFrame(List<Itembean> _items) {

		JFrame dialogFrame = new JFrame("Check Serial number");
		// Setting the width and height of frame
		dialogFrame.setSize(600, 400);
		dialogFrame.setLocationRelativeTo(null);
		dialogFrame.setUndecorated(true);
		dialogFrame.setResizable(false);

		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Constrant.FRAME_BORDER_BACKGROUN_COLOR));

		panel.setBackground(Constrant.BACKGROUN_COLOR);
		// adding panel to frame
		dialogFrame.add(panel);

		panel.setLayout(null);
		Font font = new Font("Verdana", Font.BOLD, 18);

		String content = "";
		
		
		
		for(Itembean i : _items) 
		{
			content += ""+i.SN +"<br/>";
			
		}
		
		
	
		// Creating JLabel
		JLabel modelLabel = new JLabel("<html>These serial number does not exits :" + " <br/>"+content+"<html>");

		/*
		 * This method specifies the location and size of component. setBounds(x, y,
		 * width, height) here (x,y) are cordinates from the top left corner and
		 * remaining two arguments are the width and height of the component.
		 */
		modelLabel.setBounds(30, 0, 500, 200);
		modelLabel.setFont(font);
		panel.add(modelLabel);

		JButton ok = new JButton("OK");
		ok.setBounds(200, 330, 200, 50);
		ok.setFont(font);
		panel.add(ok);

		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				dialogFrame.setVisible(false);
				dialogFrame.dispose();
				
				if(scanResultFrame != null) 
				{
					String[] checkItem = items.split("\n");
					String updateTxt = "";
					for(String s : checkItem) 
					{
						for(Itembean i : _items) 
						{
							if(i.SN.equals(s))
								continue;
							updateTxt +=s+"\n";
						}
					}
					
					inputSN.setText(items);
					scanResultFrame.setVisible(true);
				}

			}
		});

		

		// frame.setUndecorated(true);
		// frame.getRootPane().setWindowDecorationStyle(JRootPane.COLOR_CHOOSER_DIALOG);
		dialogFrame.setBackground(Color.WHITE);
		dialogFrame.setVisible(true);
		// frame.setDefaultLookAndFeelDecorated(true);
		dialogFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		dialogFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {

				dialogFrame.dispose();
				dialogFrame.setVisible(false);
			}
		});
	}

	private void submitServer(int type, List<Itembean> items) {
		String fg;
		try {
			if (type == RECEVING) {
				result = fgRepository.createItem(items).get(0).SN;
			} else if (type == MOVING) {
				result = fgRepository.updateItem(items).get(0).SN;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void checkItemExits(List<Itembean> items) {
		try {
			fgRepository.getItemsLocationBySNList(items);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
