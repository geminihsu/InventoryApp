package spirit.fitness.scanner.shipping;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.Sides;
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
import javax.swing.SwingConstants;
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
import spirit.fitness.scanner.inquiry.QueryResult;
import spirit.fitness.scanner.restful.FGRepositoryImplRetrofit;
import spirit.fitness.scanner.restful.HistoryRepositoryImplRetrofit;
import spirit.fitness.scanner.restful.HttpRestApi;
import spirit.fitness.scanner.restful.OrdersRepositoryImplRetrofit;
import spirit.fitness.scanner.restful.ShippingRepositoryImplRetrofit;
import spirit.fitness.scanner.restful.listener.CustOrderCallBackFunction;
import spirit.fitness.scanner.restful.listener.HistoryCallBackFunction;
import spirit.fitness.scanner.restful.listener.InventoryCallBackFunction;
import spirit.fitness.scanner.util.LocationHelper;
import spirit.fitness.scanner.util.PrinterHelper;
import spirit.fitness.scanner.zonepannel.ZoneMenu;
import spirit.fitness.scanner.model.CustOrderbean;
import spirit.fitness.scanner.model.Historybean;
import spirit.fitness.scanner.model.Itembean;
import spirit.fitness.scanner.model.Locationbean;
import spirit.fitness.scanner.model.Shippingbean;
import spirit.fitness.scanner.receving.ItemsPannel;

public class ShippingConfirm extends JPanel implements ActionListener, PropertyChangeListener {

	public JFrame frame;
	private JFrame orderFrame;

	private JButton scanner;
	private JButton print;
	private JButton report;
	private String salesOrder = "";

	//Key:modelID, value:quality
	private LinkedHashMap<String, Integer> map;
	
	//Key:modelID, value:description
	private TreeMap<String, String> OrderModelmap;

	private int orderTotalCount = 0;
	private List<CustOrderbean> salesOrderList;

	private ProgressMonitor progressMonitor;
	private JButton btnDone;
	private Task task;
	
	private List<Historybean> items;
	private List<Historybean> scanItems;
	
	private String orderItemsInfo ="";
	private String historyItemsInfo ="";
	private boolean isOrderClosed;
	
	private FGRepositoryImplRetrofit fgRepositoryImplRetrofit;
	private OrdersRepositoryImplRetrofit ordersRepositoryImplRetrofit;
	private HistoryRepositoryImplRetrofit historyRepositoryImplRetrofit;

	public ShippingConfirm() {

		// initialize();
		exceuteCallback();
		orderInfo();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void scanInfo(String salesOrder) {

		progressMonitor = new ProgressMonitor(ShippingConfirm.this, "Please wait...", "", 0, 100);
		
		JFrame.setDefaultLookAndFeelDecorated(false);
		JDialog.setDefaultLookAndFeelDecorated(false);
		frame = new JFrame("Shipping Pannel");
		// Setting the width and height of frame
		frame.setSize(800, 800);
		frame.setLocationRelativeTo(null);

		JPanel panel = new JPanel();
		panel.setBackground(Constrant.BACKGROUN_COLOR);
		// adding panel to frame
		frame.add(panel);

		scanPannel(panel, salesOrder);

		frame.setBackground(Color.WHITE);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {

				frame.dispose();
				frame.setVisible(false);
			}
		});

	}

	private void displayOrderInfo() {

		salesOrder = salesOrderList.get(0).salesOrder;
		String date = salesOrderList.get(0).shippingDate;
		String billToTitle = salesOrderList.get(0).bill_to;
		String custPO = salesOrderList.get(0).customerPO;
		String shippToAdddress = salesOrderList.get(0).shipToAddress;
		String shippToCity = salesOrderList.get(0).shipToCity;
		String shippToCountry = salesOrderList.get(0).shipToCountry;
		String shippToState = salesOrderList.get(0).shipToState;
		String shippToZip = salesOrderList.get(0).shipToZipCode;
		String shippToVia = salesOrderList.get(0).shipVia;

		isOrderClosed = salesOrderList.get(0).closed;
		// Locationbean title = Constrant.locations.get(locationbead);

		orderFrame = new JFrame();
		orderFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		orderFrame.setLocationRelativeTo(null);
		orderFrame.setBounds(100, 50, 1050, 800);
		orderFrame.setTitle("Display Order number:" + salesOrder);
		orderFrame.getContentPane().setLayout(null);

		Font font = new Font("Verdana", Font.BOLD, 18);

		JLabel orderLabel = new JLabel("SO Number :" + salesOrder);

		orderLabel.setBounds(30, 0, 500, 50);
		orderLabel.setFont(font);
		orderLabel.setBackground(Constrant.BACKGROUN_COLOR);
		orderFrame.getContentPane().add(orderLabel);

		String dateStr = (date == null ? "" : date.substring(0, 10));

		JLabel transcactiondate = new JLabel("TransactionDate :" + dateStr);

		transcactiondate.setBounds(30, 40, 400, 50);
		transcactiondate.setFont(font);
		transcactiondate.setBackground(Constrant.BACKGROUN_COLOR);
		orderFrame.getContentPane().add(transcactiondate);

		JLabel billTo = new JLabel("Bill To :" + billToTitle);

		billTo.setBounds(30, 70, 600, 50);
		billTo.setFont(font);
		billTo.setBackground(Constrant.BACKGROUN_COLOR);
		orderFrame.getContentPane().add(billTo);

		JLabel custNo = new JLabel("Cust PO# :" + custPO);

		custNo.setBounds(30, 90, 400, 50);
		custNo.setFont(font);
		custNo.setBackground(Constrant.BACKGROUN_COLOR);
		orderFrame.getContentPane().add(custNo);

		// JLabel shipTo = new JLabel("Ship To :"+ "FITNESS OUTLET\n1067 INDUSTRY
		// DR\nSEATTLE WA \n98188");

		JLabel shipTo = new JLabel("<html>Ship To <p style='margin-left:100'>" + billToTitle + "<br/>" + shippToAdddress
				+ "<br/>" + shippToCountry + "  " + shippToState + "<br/>" + shippToZip + "</html>",
				SwingConstants.LEFT);

		shipTo.setBounds(30, 100, 600, 200);
		shipTo.setFont(font);
		shipTo.setBackground(Constrant.BACKGROUN_COLOR);
		orderFrame.getContentPane().add(shipTo);

		JLabel ShipVia = new JLabel("Ship Via: " + shippToVia);

		ShipVia.setBounds(30, 240, 600, 50);
		ShipVia.setFont(font);
		ShipVia.setBackground(Constrant.BACKGROUN_COLOR);
		orderFrame.getContentPane().add(ShipVia);

		// ScrollPane for Result
		JScrollPane scrollZonePane = new JScrollPane();

		scrollZonePane.setBackground(Constrant.TABLE_COLOR);
		orderFrame.getContentPane().add(scrollZonePane);

		JLabel total = new JLabel("Total: ");

		total.setBounds(900, 450, 90, 50);
		total.setFont(font);
		total.setBackground(Constrant.BACKGROUN_COLOR);
		orderFrame.getContentPane().add(total);

		if (!salesOrderList.isEmpty()) {

			map = new LinkedHashMap<String, Integer>();
			OrderModelmap = new TreeMap<String, String>();

			for (CustOrderbean item : salesOrderList) {
				int count = Integer.valueOf(item.quantity);
				System.out.println("item.ItemID:" + item.ItemID);

				if (item.ItemID != null) {
					if (map.containsKey(item.ItemID)) {
						
						if (!item.ItemID.contains("PL")) 
						{
							count += map.get(item.ItemID);
							map.put(item.ItemID, count);
							orderTotalCount += count;
						}
					} else {

						map.put(item.ItemID, count);
						if (!item.ItemID.contains("PL"))
							orderTotalCount += count;
					}
				}
			}

			final Object[][] orderModelItems = new Object[map.size()][3];
			String orderModelInfo = "";
			int rowIndex = 0;
			for (Map.Entry<String, Integer> location : map.entrySet()) {
				for (int j = 0; j < 3; j++) {
					orderModelItems[rowIndex][0] = location.getValue();
					orderModelItems[rowIndex][1] = salesOrderList.get(rowIndex).ItemID;
					orderModelItems[rowIndex][2] = salesOrderList.get(rowIndex).description;
					OrderModelmap.put(salesOrderList.get(rowIndex).ItemID, salesOrderList.get(rowIndex).description);
				}

				rowIndex++;
			}

			// modelLabel.setText("SalesOrder : "+_items.get(0).SalesOrder);
			// modelText.setText(" TOTAL: "+totalCount);
			final Class[] columnClass = new Class[] { Integer.class, String.class, String.class };

			Object columnNames[] = { "Qty", "ItemID", "Description" };
			DefaultTableModel model = new DefaultTableModel(orderModelItems, columnNames) {
				@Override
				public boolean isCellEditable(int row, int column) {
					if(String.valueOf(orderModelItems[row][1]).indexOf("P") == -1)
						queryByModel(String.valueOf(orderModelItems[row][1]));
					return false;
				}

				@Override
				public Class<?> getColumnClass(int columnIndex) {
					return columnClass[columnIndex];
				}
			};

			JTable table = new JTable(model);
			table.getTableHeader().setBackground(Constrant.TABLE_COLOR);
			table.getTableHeader().setFont(font);

			table.setBackground(Constrant.TABLE_COLOR);
			table.setRowHeight(40);
			table.setFont(font);
			scrollZonePane.setBounds(33, 280, 1000, 50 * orderModelItems.length + 20);
			scrollZonePane.setViewportView(table);

			total.setBounds(900, 280+50 * orderModelItems.length , 90, 50);
			total.setText("Total : " + String.valueOf(orderTotalCount));
			orderFrame.add(scrollZonePane);

			// modelLabel.setText("SalesOrder : "+_items.get(0).SalesOrder);
			// modelText.setText(" TOTAL: "+totalCount);

			total.setText("Total : " + String.valueOf(orderTotalCount));

			scanner = new JButton("Scan");
			scanner.setFont(font);
			scanner.setBounds(900, 280+50 * orderModelItems.length + 50, 90, 50);

			print = new JButton("Print");
			print.setFont(font);
			print.setBounds(900, 500, 120, 50);

			print.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String shipToAddress = billToTitle +"\n              "+ shippToAdddress + "\n              " + shippToCity +"  " +shippToState + "\n              " + shippToZip + "    "+shippToCountry;
					printer(salesOrder,date,billToTitle,shipToAddress,orderItemsInfo,historyItemsInfo);
				}
			});

			scanner.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					scanInfo(salesOrder);
				}
			});

			if (!isOrderClosed)
				orderFrame.getContentPane().add(scanner);
			else {
				// display the items
				orderFrame.getContentPane().add(print);
				List<Historybean> items= getShippgingItems(salesOrder, frame);

				for(Historybean h : items) 
				{
					historyItemsInfo += h.SN +"\n";
				}
				rowIndex = 0;
				for (Map.Entry<String, Integer> location : map.entrySet()) {
						orderItemsInfo +=  location.getValue() +"    " + salesOrderList.get(rowIndex).ItemID +"       "+ salesOrderList.get(rowIndex).description + "  "+ items.get(0).trackingNo +"\n";
				        rowIndex++;
				}
			}
		}

		orderFrame.getContentPane().setBackground(Constrant.BACKGROUN_COLOR);
		orderFrame.setVisible(true);

		orderFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		orderFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				orderFrame.dispose();
				orderFrame.setVisible(false);
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	private void querySalesOrder(String orderNo) {

	
		try {
			salesOrderList = (ArrayList<CustOrderbean>) ordersRepositoryImplRetrofit.getItemsBySalesOrderNo(orderNo);

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	
	}

	private void queryByModel(String modelNo) {
	
		try {
			List<Itembean> itemsQuery = fgRepositoryImplRetrofit.getItemsByModel(Integer.valueOf(modelNo));
			
			if(!itemsQuery.isEmpty())
			{
				QueryResult window = new QueryResult();
				window.setContent(QueryResult.QUERY_MODEL,itemsQuery);
			}else
				JOptionPane.showMessageDialog(null, "No Items");
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// update salesOrder date and tracking number
	private List<CustOrderbean> updateSalesOrder() {
		
		List<CustOrderbean> items = null;
		try {
			items = (ArrayList<CustOrderbean>) ordersRepositoryImplRetrofit.updateItem(salesOrderList);
			if (!items.isEmpty()) {
				System.out.println(items.get(0).salesOrder);

			}

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return items;
	}

	

	// Insert item into shipping table
	private void shippingItems(List<Historybean> datas) {
		
		try {
			items = (ArrayList<Historybean>) historyRepositoryImplRetrofit.createItem(datas);
			System.out.println("insert items:"+items.size());
			if(!items.isEmpty())
				getShippgingItems(salesOrder, frame);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private List<Historybean> getShippgingItems(String salesOrder, JFrame frame) {
		List<Historybean> items = null;
		try {
			items = historyRepositoryImplRetrofit.getItemsBySalesOrder(salesOrder);
			System.out.println(""+items.size());
			if (!items.isEmpty()) {
				System.out.println(items.get(0).SN);
				orderFrame.add(getShippgingItemsJScrollPane(items));
				orderFrame.remove(scanner);
				orderFrame.add(print);
				
				orderFrame.invalidate();
				orderFrame.validate();
				orderFrame.repaint();
			}

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return items;
	}

	private JScrollPane getShippgingItemsJScrollPane(List<Historybean> list) {
		Font font = new Font("Verdana", Font.BOLD, 18);
		JScrollPane scrollSNPane = new JScrollPane();
		final Class[] itemColumnClass = new Class[] { String.class };
		final Object[][] orderSNItems = new Object[list.size()][1];
		Object snColumnNames[] = { "SN" };

		for (int i = 0; i < list.size(); i++) {
			orderSNItems[i][0] = list.get(i).SN;
		}

		
		DefaultTableModel snModel = new DefaultTableModel(orderSNItems, snColumnNames) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				return itemColumnClass[columnIndex];
			}
		};

		JTable snItems = new JTable(snModel);
		snItems.getTableHeader().setBackground(Constrant.TABLE_COLOR);
		snItems.getTableHeader().setFont(font);

		snItems.setBackground(Constrant.TABLE_COLOR);
		snItems.setRowHeight(40);
		snItems.setFont(font);

		scrollSNPane.setBounds(33, 500, 300, 50 * orderSNItems.length + 20);
		scrollSNPane.setViewportView(snItems);

		return scrollSNPane;

	}

	public void orderInfo() {

		JFrame.setDefaultLookAndFeelDecorated(false);
		JDialog.setDefaultLookAndFeelDecorated(false);
		frame = new JFrame("Query Pannel");
		// Setting the width and height of frame
		frame.setSize(700, 600);
		frame.setLocationRelativeTo(null);

		JPanel panel = new JPanel();
		panel.setBackground(Constrant.BACKGROUN_COLOR);
		// adding panel to frame
		frame.add(panel);

		placeComponents(panel);

		frame.setBackground(Color.WHITE);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {

				frame.dispose();
				frame.setVisible(false);
			}
		});

	}

	private void placeComponents(JPanel panel) {

		panel.setLayout(null);
		Font font = new Font("Verdana", Font.BOLD, 18);
		// Creating JLabel
		JLabel modelLabel = new JLabel("SO Number:");

		modelLabel.setBounds(100, 100, 200, 25);
		modelLabel.setFont(font);
		panel.add(modelLabel);

		JTextField salesOrderNo = new JTextField(20);
		salesOrderNo.setText("");
		salesOrderNo.setFont(font);
		salesOrderNo.setBounds(230, 100, 320, 50);
		panel.add(salesOrderNo);

		// Creating Query button
		JButton QueryButton = new JButton("Find");
		QueryButton.setFont(font);
		QueryButton.setBounds(230, 250, 150, 50);
		QueryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (salesOrderNo.getText().equals(""))
					JOptionPane.showMessageDialog(null, "Please enter Sales Order Number.");
				else {
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							try {

								querySalesOrder(salesOrderNo.getText().toString().trim());

							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				}

			}
		});

		panel.add(QueryButton);

		// Creating Query button
		JButton ResetButton = new JButton("Reset");
		ResetButton.setFont(font);
		ResetButton.setBounds(400, 250, 150, 50);
		ResetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				salesOrderNo.setText("");
			}
		});

		panel.add(ResetButton);
	}

	public void displayShippingResult(String salesOrder, String date, String pro, String content) {

		JFrame.setDefaultLookAndFeelDecorated(false);
		JDialog.setDefaultLookAndFeelDecorated(false);
		frame = new JFrame("Query Pannel");
		// Setting the width and height of frame
		frame.setSize(800, 600);
		frame.setLocationRelativeTo(null);

		JPanel panel = new JPanel();
		panel.setBackground(Constrant.BACKGROUN_COLOR);
		// adding panel to frame
		frame.add(panel);

		shippingResult(panel, salesOrder, date, pro, content);

		frame.setBackground(Color.WHITE);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {

				frame.dispose();
				frame.setVisible(false);
			}
		});

	}

	private void shippingResult(JPanel panel, String salesOrder, String date, String pro, String items) {

		String[] itemList = items.split("\n");
		panel.setLayout(null);
		Font font = new Font("Verdana", Font.BOLD, 18);
		// Creating JLabel
		JLabel dateLabel = new JLabel("TransactionDate : " + date.substring(0, 10));

		dateLabel.setBounds(50, 20, 400, 25);
		dateLabel.setFont(font);
		panel.add(dateLabel);

		JLabel modelLabel = new JLabel("Pro # : " + pro);

		modelLabel.setBounds(50, 40, 300, 25);
		modelLabel.setFont(font);
		panel.add(modelLabel);

		JLabel totalCount = new JLabel("Quantity : ");

		totalCount.setBounds(50, 60, 300, 25);
		totalCount.setFont(font);
		panel.add(totalCount);
		// ScrollPane for Result
		JScrollPane scrollZonePane = new JScrollPane();

		scrollZonePane.setBackground(Constrant.TABLE_COLOR);
		// panel.add(scrollZonePane);

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
			for (int j = 0; j < 2; j++) {
				rowData[i][0] = ((String) sortedList.get(i)).substring(0, 6);
				rowData[i][1] = sortedList.get(i);
			}
		}

		totalCount.setText("Quantity : " + sortedList.size());

		Object columnNames[] = {"ItemID", "Serial Number" };
		final Class[] columnClass = new Class[] { String.class, String.class};

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
		table.getTableHeader().setBackground(Constrant.TABLE_COLOR);
		table.setBackground(Constrant.TABLE_COLOR);
		table.setFont(font);
		table.setRowHeight(40);

		scrollZonePane.setBounds(33, 100, 750, 50 * rowData.length + 20);
		scrollZonePane.setViewportView(table);

		panel.add(scrollZonePane);

		// Creating Query button
		report = new JButton("Report");
		report.setFont(font);
		report.setBounds(600, 400, 150, 50);
		report.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				EventQueue.invokeLater(new Runnable() {
					public void run() {
						report.setEnabled(false);
						scanItems = new ArrayList<Historybean>();
						String SalesOrder = salesOrderList.get(0).salesOrder;
						String date = salesOrderList.get(0).shippingDate;
						String billToTitle = salesOrderList.get(0).bill_to;
						String custPO = salesOrderList.get(0).customerPO;
						String shippToAdddress = salesOrderList.get(0).shipToAddress;
						String shippToCity = salesOrderList.get(0).shipToCity;
						String shippToCountry = salesOrderList.get(0).shipToCountry;
						String shippToState = salesOrderList.get(0).shipToState;
						String shippToZip = salesOrderList.get(0).shipToZipCode;
						String shippToVia = salesOrderList.get(0).shipVia;

						
						String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
								.format(Calendar.getInstance().getTime());
						for (String item : itemList) {
							Historybean _item = new Historybean();

							_item.SN = item;
							_item.date = timeStamp;
							_item.location = "999";
							_item.modelNo = item.substring(0, 6);
							_item.date = date;
							_item.salesOrder = salesOrder;
							_item.trackingNo = pro;
							_item.billTo = billToTitle;
							_item.custPO = custPO;
							_item.shipCity = shippToCity;
							_item.shipState = shippToState;
							_item.shipVia = shippToVia;
							_item.IsPurchaseImported = false;

							
							scanItems.add(_item);

						}

						for (int i = 0; i < salesOrderList.size(); i++) {
							CustOrderbean order = salesOrderList.get(i);
							order.shippingDate = timeStamp;
							order.closed = true;
							// order.TrackingNo = proNumber.getText().toString();
							salesOrderList.set(i, order);
						}
						
						int rowIndex = 0;
						for (Map.Entry<String, Integer> location : map.entrySet()) {
								orderItemsInfo +=  location.getValue() +"    " + salesOrderList.get(rowIndex).ItemID +"       "+ salesOrderList.get(rowIndex).description + "  "+ scanItems.get(0).trackingNo +"\n";
						        rowIndex++;
						}
						
						for(Historybean h : scanItems) 
						{
							historyItemsInfo += h.SN +"\n";
						}
		
						displayLoadingBar();
						updateSalesOrder();
						

					}
				});

			}
		});

		panel.add(report);
	}

	private void scanPannel(JPanel panel, String salesOrder) {

		panel.setLayout(null);
		Font font = new Font("Verdana", Font.BOLD, 18);
		// Creating JLabel
		JLabel shippingLabel = new JLabel("TransactionDate : ");

		shippingLabel.setBounds(50, 20, 200, 25);
		shippingLabel.setFont(font);
		panel.add(shippingLabel);

		JTextField shippingDate = new JTextField(20);
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		shippingDate.setText(timeStamp);
		shippingDate.setFont(font);
		shippingDate.setBounds(250, 20, 400, 50);
		panel.add(shippingDate);

		// Creating JLabel
		JLabel proLabel = new JLabel("PRO # ");

		proLabel.setBounds(50, 80, 200, 25);
		proLabel.setFont(font);
		panel.add(proLabel);

		JTextField proNumber = new JTextField(20);
		proNumber.setText("");
		proNumber.setFont(font);
		proNumber.setBounds(250, 70, 400, 50);
		panel.add(proNumber);

		// Creating JLabel
		JLabel modelLabel = new JLabel("Total : " + "0" + "/" + String.valueOf(orderTotalCount));

		modelLabel.setBounds(50, 150, 200, 25);
		modelLabel.setFont(font);
		panel.add(modelLabel);

		JTextArea inputSN = new JTextArea(20, 15);
		String content = "";

		inputSN.setText(content);
		JScrollPane scrollPanel1 = new JScrollPane(inputSN);
		scrollPanel1.setBounds(250, 150, 250, 500);
		inputSN.setFont(font);
		panel.add(scrollPanel1);

		// Creating Query button
		JButton queryButton = new JButton("Add");
		queryButton.setFont(font);
		queryButton.setBounds(500, 150, 150, 50);
		queryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean verifyOrder = checkOrder(inputSN.getText().toString());

				if (verifyOrder) {
					frame.dispose();
					frame.setVisible(false);

					displayShippingResult(salesOrder, shippingDate.getText().toString(), proNumber.getText().toString(),
							inputSN.getText().toString());
				}
			}
		});

		panel.add(queryButton);

		// Creating Reset button
		JButton resetButton = new JButton("Reset");
		resetButton.setFont(font);
		resetButton.setBounds(500, 220, 150, 50);
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				inputSN.setText("");
			}
		});

		panel.add(resetButton);

		
	}
	
	private void exceuteCallback() {

		fgRepositoryImplRetrofit = new FGRepositoryImplRetrofit();
		fgRepositoryImplRetrofit.setinventoryServiceCallBackFunction(new InventoryCallBackFunction() {

			@Override
			public void resultCode(int code) {
				if (code == HttpRequestCode.HTTP_REQUEST_INSERT_DATABASE_ERROR) {
					JOptionPane.showMessageDialog(null, "Items already exit.");
					
				}

			}

			@Override
			public void getInventoryItems(List<Itembean> _items) {
				
			}

			

		});
		
		ordersRepositoryImplRetrofit = new OrdersRepositoryImplRetrofit();
		ordersRepositoryImplRetrofit.setCustOrderServiceCallBackFunction(new CustOrderCallBackFunction() {

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
			public void updateSalesOrder(List<CustOrderbean> orders) {

				if (orders.isEmpty())
					JOptionPane.showMessageDialog(null, "The sales order doesn't exit !");
				else {
					frame.dispose();
					frame.setVisible(false);

					if (!orders.isEmpty() && !salesOrder.equals("")) {
						System.out.println("update sucess");
						EventQueue.invokeLater(new Runnable() {
							public void run() {
								try {
									if (!isOrderClosed)
										shippingItems(scanItems);
									else
										getShippgingItems(orders.get(0).salesOrder, frame);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
					} else {
						salesOrderList = orders;
						displayOrderInfo();
					}

				}
			}

		});
		
		historyRepositoryImplRetrofit = new HistoryRepositoryImplRetrofit();
		historyRepositoryImplRetrofit.setHistoryServiceCallBackFunction(new HistoryCallBackFunction() {

			@Override
			public void resultCode(int code) {
				if (code == HttpRequestCode.HTTP_REQUEST_INSERT_DATABASE_ERROR) {
					JOptionPane.showMessageDialog(null, "Items already exit.");
					task.cancel(true);
					progressMonitor.close();
				}
				
			}

			@Override
			public void getHistoryItems(List<Historybean> items) {
				if (!items.isEmpty() && task != null) {
					progressMonitor.close();
					task.done();
					frame.dispose();
					frame.setVisible(false);

					JOptionPane.showMessageDialog(null, "Insert Data Success!");
				}
				
			}

			
		});
		

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
				report.setEnabled(true);
			}
		}

	}

	private boolean checkOrder(String content) {

		String[] sn = content.toString().split("\n");

		if (sn.length != orderTotalCount) {
			JOptionPane.showMessageDialog(null, "Quantity Error! Please confirm the items quantiy.");
			return false;
		}

		for (String s : sn) {
			String modelNo = "";
			if (s.length() != 16) {
				JOptionPane.showMessageDialog(null, "Please enter correct data.");
				return false;
			} else {
				modelNo = s.substring(0, 6);

				if (map.get(modelNo) == null) {
					JOptionPane.showMessageDialog(null, "Model Error!");
					return false;
				}
			}

		}

		return true;
	}

	private void printer(String saleOrder,String date, String billTo, String shipTo, String modelInfo, String itemsInfo) {
		
		String content = "Sales Order : " + saleOrder +"\n"
						+"TransactionDate : " + date +"\n"
						+"Bill To : " + billTo +"\n"
						+"Ship To : " + shipTo +"\n"
						+"------------------------------------------------------------------------------------------------------\n"
						+"Total Item         Model                   PRO#\n"
						+"------------------------------------------------------------------------------------------------------\n"
						+ modelInfo
						+"------------------------------------------------------------------------------------------------------\n"
						+ itemsInfo;
		
		PrinterHelper print = new PrinterHelper();
		print.printItems(content);

	}

	private void displayLoadingBar() {
		progressMonitor.setProgress(0);

		task = new Task();
		task.addPropertyChangeListener(ShippingConfirm.this);
		task.execute();
		report.setEnabled(false);
		//btnDone.setEnabled(false);
	}

	class Task extends SwingWorker<Void, Void> {
		@Override
		public Void doInBackground() {
			Random random = new Random();
			int progress = 0;
			setProgress(0);
			try {
				Thread.sleep(500);
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
			// btnDone.setEnabled(true);
			progressMonitor.close();
			// if (!items.isEmpty()) {
			// System.out.println(items.get(0).SN);

			
		
		}
	}
}
