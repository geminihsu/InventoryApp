package spirit.fitness.scanner.report;

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
import java.io.File;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

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
import spirit.fitness.scanner.util.ExcelHelper;
import spirit.fitness.scanner.util.LocationHelper;
import spirit.fitness.scanner.util.PrinterHelper;
import spirit.fitness.scanner.zonepannel.ZoneMenu;
import spirit.fitness.scanner.model.Itembean;
import spirit.fitness.scanner.model.Reportbean;

public class DailyReport extends JPanel implements ActionListener, PropertyChangeListener {

	public final static int REPORT = 0;
	public final static int MIN_QUANTITY = 1;

	public JFrame frame;
	private String items;

	private ProgressMonitor progressMonitor;
	private JButton btnDone;
	private Task task;

	private String result;

	private int type;

	public DailyReport(List<Reportbean> data, int _type) {
		type = _type;

		displayTable(data);

	}

	private void displayTable(List<Reportbean> data) {

		String timeStamp = new SimpleDateFormat("yyyy-MM-dd")
				.format(Calendar.getInstance().getTime());

		JFrame.setDefaultLookAndFeelDecorated(false);
	    JDialog.setDefaultLookAndFeelDecorated(false);
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Item Result");
		frame.setLocationRelativeTo(null);
		frame.setBounds(100, 100, 1000, 600);
		


		Object rowDataReport[][] = new Object[data.size()][10];

		for (int i = 0; i < data.size(); i++) {
			for (int j = 0; j < 10; j++) {
				rowDataReport[i][0] = data.get(i).Model;
				rowDataReport[i][1] = data.get(i).FG;
				rowDataReport[i][2] = data.get(i).shipped;
				rowDataReport[i][3] = data.get(i).unshippable;
				rowDataReport[i][4] = data.get(i).onHand;
				rowDataReport[i][5] = data.get(i).zone1;
				rowDataReport[i][6] = data.get(i).zone2;
				rowDataReport[i][7] = data.get(i).returnItem;;
				rowDataReport[i][8] = data.get(i).showRoom;
				rowDataReport[i][9] = data.get(i).total;
			}
		}

		String zone = "";
		
		Object columnNames[] = { "Model#", "FG", "Shipped" ,"Unshippable","OnHand","Zone1"  ,"Zone2","Return","ShowRoom","total"};
		Font font = new Font("Verdana", Font.BOLD, 18);
		final Class[] columnClass = new Class[] { String.class, String.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class };

		DefaultTableModel model = new DefaultTableModel(rowDataReport, columnNames) {
			@Override
			public boolean isCellEditable(int row, int column) {
				
				return false;
			}

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				return columnClass[columnIndex];
			}
		};
		
		Object rowDataMinQuantity[][] = new Object[data.size()][4];

		for (int i = 0; i < data.size(); i++) {
			for (int j = 0; j < 4; j++) {
				rowDataMinQuantity[i][0] = data.get(i).Model;
				rowDataMinQuantity[i][1] = data.get(i).FG;
				rowDataMinQuantity[i][2] = data.get(i).minQuantity;
				rowDataMinQuantity[i][3] = "0/"+data.get(i).onHand;
				
			}
		}

		
		Object modelNames[] = { "Model#", "FG", "Min Quantity", "Zone2 onHand"};
		
		final Class[] modelcolumnClass = new Class[] { String.class, String.class, Integer.class , Integer.class};

		DefaultTableModel quantity = new DefaultTableModel(rowDataMinQuantity, modelNames) {
			@Override
			public boolean isCellEditable(int row, int column) {
				if (column == 2)
					return true;
				return false;
			}

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				return modelcolumnClass[columnIndex];
			}
		};

		JTable table =  new JTable(model);	
		table.getTableHeader().setFont(font);
		table.getTableHeader().setBackground(Constrant.DISPALY_ITEMS_TABLE_COLOR);
		table.setBackground(Constrant.DISPALY_ITEMS_TABLE_COLOR);
		table.setFont(font);
		table.setRowHeight(40);
		
		JTable quantityTable =  new JTable(quantity);	
		quantityTable.getTableHeader().setFont(font);
		quantityTable.getTableHeader().setBackground(Constrant.DISPALY_ITEMS_TABLE_COLOR);
		quantityTable.setBackground(Constrant.DISPALY_ITEMS_TABLE_COLOR);
		quantityTable.setFont(font);
		quantityTable.setRowHeight(40);
		

		JScrollPane scrollPane = new JScrollPane(table);
		
		JScrollPane scrollPaneQuantity = new JScrollPane(quantityTable);
		
		if(type == REPORT)
		{
			frame.setTitle(timeStamp + " Inventory Report.");
			frame.add(scrollPane, BorderLayout.CENTER);
		}else {
			frame.setTitle("Zone 2 Model Quantity.");
			frame.add(scrollPaneQuantity, BorderLayout.CENTER);
		}

		btnDone = new JButton("Export To Excel");
		btnDone.setFont(font);
		btnDone.setBounds(312, 387, 89, 50);

		if(type == MIN_QUANTITY)
			btnDone.setText("Update Model Quantity");
		btnDone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							ExcelHelper exp = new ExcelHelper();
							
							if (type == REPORT) {
								exp.fillData(table,
										new File("C:\\Users\\geminih\\Downloads\\" + timeStamp + "_report.xls"));

								JOptionPane.showMessageDialog(null, "Export " + timeStamp + ".xls' successfully",
										"Message", JOptionPane.INFORMATION_MESSAGE);
							}
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

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName()) {
			int progress = (Integer) evt.getNewValue();
			progressMonitor.setProgress(progress);
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

		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}
