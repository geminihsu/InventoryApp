package spirit.fitness.scanner.receving;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

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
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import com.mashape.unirest.http.HttpClientHelper;
import com.mashape.unirest.http.HttpResponse;

import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.http1.Http1Codec;
import spirit.fitness.scanner.AppMenu;
import spirit.fitness.scanner.common.Constrant;
import spirit.fitness.scanner.common.HttpRequestCode;
import spirit.fitness.scanner.restful.FGRepositoryImplRetrofit;
import spirit.fitness.scanner.restful.HttpRestApi;
import spirit.fitness.scanner.restful.ShippingRepositoryImplRetrofit;
import spirit.fitness.scanner.restful.listener.InventoryCallBackFunction;
import spirit.fitness.scanner.util.LocationHelper;
import spirit.fitness.scanner.util.PrinterHelper;
import spirit.fitness.scanner.zonepannel.ZoneMenu;
import spirit.fitness.scanner.model.Itembean;

public class ItemsPannel extends JPanel implements ActionListener, PropertyChangeListener {

	public final static int RECEVING = 0;
	public final static int MOVING = 1;
	public final static int SHIPPING = 2;

	public JFrame frame;
	private String items;
	private int assignType;

	private ProgressMonitor progressMonitor;
	private JButton btnDone;
	private Task task;

	private String result ="";
	
	private FGRepositoryImplRetrofit fgRepository;

	public ItemsPannel(int type) {
		assignType = type;
		initialize(type);

	}

	public ItemsPannel(String content, String location, int type) {
		assignType = type;
		// initialize();
		displayTable(content, location, type);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize(int type) {
		JFrame.setDefaultLookAndFeelDecorated(false);
		frame = new JFrame();
		frame.setTitle("Item List");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setBounds(500, 100, 400, 700);
		JPanel basic = new JPanel();
		basic.setBackground(new java.awt.Color(235, 240, 255));

		Font font = new Font("Verdana", Font.BOLD, 20);
		TitledBorder title = new TitledBorder("Please start scanner barcode:");
		title.setTitleFont(font);

		JLabel ltotal = new JLabel("Total: 0");
		ltotal.setFont(font);
		ltotal.setBounds(0, 387, 89, 23);
		basic.add(ltotal);

		basic.setBorder(title);

		JTextArea inputSN = new JTextArea(20, 15);
		String content = "";

		 for (int i = 1; i < 10; i++) {
		 content += "158012130811120" + i + "\n";
		 }

		for (int i = 10; i < 50; i++) {
			content += "15801213081112" + i + "\n";
		}

		// for (int i = 3; i < 5; i++) {
		// content += "158012171100384" + i + "\n";
		// }

		// inputSN.setText("12312312312312313212312312123121111111111111111111111111111111\n12312312312312313212312312123121111111111111111111111111111111\n12312312312312313212312312123121111111111111111111111111111111\n12312312312312313212312312123121111111111111111111111111111111\n12312312312312313212312312123121111111111111111111111111111111\n12312312312312313212312312123121111111111111111111111111111111\n12312312312312313212312312123121111111111111111111111111111111\n12312312312312313212312312123121111111111111111111111111111111\n12312312312312313212312312123121111111111111111111111111111111\n12312312312312313212312312123121111111111111111111111111111111\n12312312312312313212312312123121111111111111111111111111111111\n12312312312312313212312312123121111111111111111111111111111111\n12312312312312313212312312123121111111111111111111111111111111\n12312312312312313212312312123121111111111111111111111111111111\n12312312312312313212312312123121111111111111111111111111111111\n12312312312312313212312312123121111111111111111111111111111111\n12312312312312313212312312123121111111111111111111111111111111\n12312312312312313212312312123121111111111111111111111111111111\n12312312312312313212312312123121111111111111111111111111111111\n12312312312312313212312312123121111111111111111111111111111111\n12312312312312313212312312123121111111111111111111111111111111\n");

		//inputSN.setText(content);
		inputSN.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				ltotal.setText("Total: " + inputSN.getLineCount());
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				String[] item = inputSN.getText().toString().split("\n");
				if (item.length > 1)
					ltotal.setText("Total: " + item.length);
				else
					ltotal.setText("0");
				if (e.getLength() > 1 && e.getLength() < 16) {
					JOptionPane.showMessageDialog(null, "Serial Number Error!");
				}

				//System.out.println(e.getLength());
			}

			@Override
			public void changedUpdate(DocumentEvent arg0) {

			}
		});

		JScrollPane scrollPanel1 = new JScrollPane(inputSN);
		inputSN.setFont(font);
		// scrollPanel1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		inputSN.setBounds(0, 387, 50, 23);
		basic.add(scrollPanel1);

		btnDone = new JButton("Assign");
		btnDone.setFont(font);

		if (type == SHIPPING)
			btnDone.setText("Shipping");

		btnDone.setBounds(312, 387, 89, 23);
		basic.add(btnDone);

		JButton btnDefault = new JButton("Default");
		btnDefault.setFont(font);

		btnDefault.setBounds(512, 387, 89, 23);
		if (type != SHIPPING)
			basic.add(btnDefault);

		JButton btnRest = new JButton("Reset");
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
				if (inputSN.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please scan bar code item!");
				} else {
					frame.setVisible(false);
					frame.dispose();

					displayTable(inputSN.getText().toString(), "000", type);
				}

			}
		});
		btnDone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (inputSN.getText().isEmpty())
					JOptionPane.showMessageDialog(null, "Please scan bar code item!");
				else {
					items = inputSN.getText().toString();

					if (type == SHIPPING) {
						displayTable(items, String.valueOf(Constrant.ZONE_CODE_SHIPPING), SHIPPING);
					} else {

						frame.setVisible(false);
						frame.dispose();

						ZoneMenu window = new ZoneMenu(items, type);
						window.frame.setVisible(true);
					}
				}
			}
		});

		frame.getContentPane().add(basic);
		// frame.setSize(500, 500);
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
	
	private void exceuteCallback() {
		
		fgRepository = new FGRepositoryImplRetrofit();
		fgRepository.setinventoryServiceCallBackFunction(new InventoryCallBackFunction() {

			@Override
			public void resultCode(int code) {
				// TODO Auto-generated method stub
				if (code == HttpRequestCode.HTTP_REQUEST_INSERT_DATABASE_ERROR) {
					JOptionPane.showMessageDialog(null, "Items already exit.");
					task.cancel(true);
					progressMonitor.close();
				}
			}

			@Override
			public void getInventoryItems(List<Itembean> items) {
				if (!items.isEmpty()) {
					 progressMonitor.close();
					 task.done();
					if (assignType == RECEVING) {

						frame.dispose();
						frame.setVisible(false);
						JOptionPane.showMessageDialog(null, "Insert Data Success!");

					} else if (assignType == MOVING) {

						frame.dispose();
						frame.setVisible(false);
						JOptionPane.showMessageDialog(null, "Update Data Success!");

					} 
				}
			}
		});
		
	}

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

				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							btnDone.setEnabled(false);
							
							progressMonitor = new ProgressMonitor(ItemsPannel.this, "Please wait...", "", 0, 100);
							
							List<Itembean> items = new ArrayList<Itembean>();

							String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
									.format(Calendar.getInstance().getTime());
							for (String item : itemList) {
								Itembean _item = new Itembean();

								_item.SN = item;
								_item.date = timeStamp;
								_item.Location = location;
								_item.ModelNo = item.substring(0, 6);
								items.add(_item);

							}
							
							if(type == RECEVING) {
								PrinterHelper print = new PrinterHelper();
								print.printItems(content);
							}
							exceuteCallback();
							displayLoadingBar();
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

	private void displayLoadingBar() {
	    progressMonitor.setProgress(0);

		task = new Task();
		task.addPropertyChangeListener(ItemsPannel.this);
		task.execute();
		btnDone.setEnabled(false);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName()) {
			int progress = (Integer) evt.getNewValue();
			//progressMonitor.setProgress(progress);
			String message = String.format("Completed %d%%.\n", progress);
			progressMonitor.setNote(message);

			if (progressMonitor.isCanceled() || task.isDone()) {
				Toolkit.getDefaultToolkit().beep();
				if (progressMonitor.isCanceled()) {
					task.cancel(true);
					// taskOutput.append("Task canceled.\n");
				} else {
					// taskOutput.append("Task completed.\n");
				}
				btnDone.setEnabled(true);
			}
		}

	}

	class Task extends SwingWorker<Void, Void> {
		@Override
		public Void doInBackground() {
			Random random = new Random();
			int progress = 0;
			setProgress(0);
			try {
				Thread.sleep(1000);
				while (progress < 100 && !isCancelled()) {
					// Sleep for up to one second.
					Thread.sleep(random.nextInt(1000));
					// Make random progress.
					progress += random.nextInt(10);
					setProgress(Math.min(progress, 100));
				}
			} catch (InterruptedException ignore) {
			}
			return null;
		}

		@Override
		public void done() {
			Toolkit.getDefaultToolkit().beep();
			btnDone.setEnabled(true);
			progressMonitor.close();
			
		}
	}
}
