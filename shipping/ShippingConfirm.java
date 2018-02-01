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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ProgressMonitor;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

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
import spirit.fitness.scanner.util.LoadingFrameHelper;
import spirit.fitness.scanner.util.LocationHelper;
import spirit.fitness.scanner.util.PrintTableUtil;
import spirit.fitness.scanner.util.PrinterHelper;
import spirit.fitness.scanner.zonepannel.ZoneMenu;
import spirit.fitness.scanner.model.CustOrderbean;
import spirit.fitness.scanner.model.Historybean;
import spirit.fitness.scanner.model.Itembean;
import spirit.fitness.scanner.model.Locationbean;
import spirit.fitness.scanner.model.PickingItem;
import spirit.fitness.scanner.model.Shippingbean;
import spirit.fitness.scanner.receving.ItemsPannel;

public class ShippingConfirm {

	private static final String TEXT_SUBMIT = "text-submit";
	private static final String INSERT_BREAK = "insert-break";
	public JFrame frame;
	private JFrame orderFrame;
	private JFrame scanResultFrame;

	private JPanel orderDisplayPanel;

	private JButton scanner;
	private JButton print;
	private JButton report;
	private JButton prev;
	private JButton exit;

	private String salesOrder = "";
	// private String orderItemsInfo ="";
	private String historyItemsInfo = "";
	private boolean isOrderClosed;

	private String prevContent = "";

	private boolean isPackingList = false;

	// Key:modelID, value:quality
	private LinkedHashMap<String, Integer> map;

	// Key:modelID, value:description
	private TreeMap<String, String> OrderModelmap;

	// Key:modelID, value:response items
	private LinkedHashMap<String, List<Itembean>> inventoryModelmap;
	
	// Key:Location, value:quantity
	private LinkedHashMap<String, LinkedHashMap<String, Integer>> locMap = new LinkedHashMap<String, LinkedHashMap<String, Integer>>();
	
	// Key:modelID, value:all location and quantityInfo
	private LinkedHashMap<String, String> modelMapLocationInfo = new LinkedHashMap<String, String>();

	private int orderTotalCount = 0;
	// private List<CustOrderbean> salesOrderList;
	private List<CustOrderbean> salesOrderList;
	private List<PickingItem> pickingItems;

	private JProgressBar loading;
	private LoadingFrameHelper loadingframe;
	private JTable packingTable;
	private DefaultTableModel packingModel;

	private List<Historybean> items;
	private List<Historybean> scanItems;

	private FGRepositoryImplRetrofit fgRepositoryImplRetrofit;
	private OrdersRepositoryImplRetrofit ordersRepositoryImplRetrofit;
	private HistoryRepositoryImplRetrofit historyRepositoryImplRetrofit;

	private List<String> resultModelItem = new ArrayList<String>();

	public ShippingConfirm(boolean _isPackingList) {

		// initialize();
		isPackingList = _isPackingList;
		exceuteCallback();
		orderInfo();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void scanInfo(String salesOrder) {

		scanResultFrame = new JFrame("");
		// Setting the width and height of frame
		// scanResultFrame.setSize(600, 800);
		scanResultFrame.setBounds(30, 10, 550, 800);
		scanResultFrame.setUndecorated(true);
		scanResultFrame.setResizable(false);

		JPanel scanDisplayPanel = new JPanel();
		scanDisplayPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Constrant.FRAME_BORDER_BACKGROUN_COLOR));

		scanDisplayPanel.setBackground(Constrant.BACKGROUN_COLOR);
		// adding panel to frame
		scanResultFrame.add(scanDisplayPanel);

		scanPannel(scanDisplayPanel, salesOrder);

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

	private void displayOrderInfo(List<Itembean> items) {

		// Locationbean title = Constrant.locations.get(locationbead);

		if (orderFrame == null) {
			orderFrame = new JFrame();
			orderFrame.setBounds(100, 50, 1050, 800);
			orderFrame.setLocationRelativeTo(null);
			orderFrame.setUndecorated(true);
			orderFrame.setResizable(false);

			orderDisplayPanel = new JPanel();
			orderDisplayPanel
					.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Constrant.FRAME_BORDER_BACKGROUN_COLOR));

			orderDisplayPanel.setBackground(Constrant.BACKGROUN_COLOR);
			// adding panel to frame
			orderFrame.add(orderDisplayPanel);
		}
		if (!isPackingList)
			placeOrderInfo();
		else
			placeOrderInfoAndInventoryLocation();

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
	
	private void displayPickingOrderInfo(List<Itembean> items) {

		// Locationbean title = Constrant.locations.get(locationbead);

		orderFrame = new JFrame();
		orderFrame.setBounds(100, 50, 1050, 800);
		orderFrame.setLocationRelativeTo(null);
		orderFrame.setUndecorated(true);
		orderFrame.setResizable(false);

		orderDisplayPanel = new JPanel();
		orderDisplayPanel
				.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Constrant.FRAME_BORDER_BACKGROUN_COLOR));

		orderDisplayPanel.setBackground(Constrant.BACKGROUN_COLOR);
		// adding panel to frame
		orderFrame.add(orderDisplayPanel);

		placeOrderInfoAndInventoryLocation();

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

	private void querySalesOrder(String orderNo) {

		try {
			ordersRepositoryImplRetrofit.getItemsBySalesOrderNo(orderNo);

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

			if (!isPackingList) {
				if (!itemsQuery.isEmpty()) {
					QueryResult window = new QueryResult();
					window.setContent(QueryResult.QUERY_MODEL, itemsQuery);
				} else
					JOptionPane.showMessageDialog(null, "No Items");
			}

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void queryByModelAndCount(String modelNo, int count) {

		try {
			List<Itembean> itemsQuery = fgRepositoryImplRetrofit.getItemsByModelAndCount(Integer.valueOf(modelNo),
					count);

			if (!isPackingList) {
				if (!itemsQuery.isEmpty()) {
					QueryResult window = new QueryResult();
					window.setContent(QueryResult.QUERY_MODEL, itemsQuery);
				} else
					JOptionPane.showMessageDialog(null, "No Items");
			}

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void queryByModelAndDate(String modelNo, String date) {

		try {
			List<Itembean> itemsQuery = fgRepositoryImplRetrofit.getItemsByModelAndDate(Integer.valueOf(modelNo), date);

			if (!isPackingList) {
				if (!itemsQuery.isEmpty()) {
					QueryResult window = new QueryResult();
					window.setContent(QueryResult.QUERY_MODEL, itemsQuery);
				} else
					JOptionPane.showMessageDialog(null, "No Items");
			}

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
			if (!items.isEmpty())
				getShippgingItems(salesOrder);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private List<Historybean> getShippgingItems(String salesOrder) {
		List<Historybean> items = null;
		try {
			items = historyRepositoryImplRetrofit.getItemsBySalesOrder(salesOrder);
			if (!items.isEmpty()) {

				if (scanResultFrame != null) {
					scanResultFrame.dispose();
					scanResultFrame.setVisible(false);
				}

				JScrollPane JScrollPane = getShippgingItemsJScrollPane(items);
				JScrollPane.setBounds(30, 500, 400, 50 * items.size() + 20);
				orderDisplayPanel.add(JScrollPane);
				orderDisplayPanel.remove(scanner);
				orderDisplayPanel.add(print);

				
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

		frame = new JFrame("Query Pannel");
		// Setting the width and height of frame
		frame.setSize(700, 400);
		frame.setLocationRelativeTo(null);
		frame.setUndecorated(true);
		frame.setResizable(false);

		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Constrant.FRAME_BORDER_BACKGROUN_COLOR));

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
		QueryButton.setBounds(230, 180, 150, 50);
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
		JButton ResetButton = new JButton("Clear");
		ResetButton.setFont(font);
		ResetButton.setBounds(400, 180, 150, 50);
		ResetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				salesOrderNo.setText("");
			}
		});

		panel.add(ResetButton);

		// Creating Query button
		JButton exitButton = new JButton("Exit");
		exitButton.setFont(font);
		exitButton.setBounds(230, 250, 320, 50);
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
				frame.setVisible(false);
			}
		});

		panel.add(exitButton);
	}

	private void placeOrderInfo() {

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
		orderDisplayPanel.setLayout(null);

		Font font = new Font("Verdana", Font.BOLD, 18);

		placeOrderInfoDetail();
		// ScrollPane for Result
		JScrollPane scrollZonePane = new JScrollPane();

		scrollZonePane.setBackground(Constrant.TABLE_COLOR);
		orderDisplayPanel.add(scrollZonePane);

		JLabel total = new JLabel("Total: ");

		total.setBounds(900, 450, 90, 50);
		total.setFont(font);
		total.setBackground(Constrant.BACKGROUN_COLOR);
		orderDisplayPanel.add(total);

		if (!salesOrderList.isEmpty()) {

			int colsize = 3;
			final Object[][] orderModelItems = new Object[map.size()][colsize];

			int rowIndex = 0;
			for (Map.Entry<String, Integer> location : map.entrySet()) {
				for (int j = 0; j < colsize; j++) {
					orderModelItems[rowIndex][0] = location.getValue();
					orderModelItems[rowIndex][1] = salesOrderList.get(rowIndex).ItemID;
					orderModelItems[rowIndex][2] = salesOrderList.get(rowIndex).description;

					if (isPackingList) {
						orderModelItems[rowIndex][3] = salesOrderList.get(rowIndex).description;
					}
					OrderModelmap.put(salesOrderList.get(rowIndex).ItemID, salesOrderList.get(rowIndex).description);
				}

				rowIndex++;
			}

			// modelLabel.setText("SalesOrder : "+_items.get(0).SalesOrder);

			final Class[] columnClass = new Class[] { Integer.class, String.class, String.class };

			Object columnNames[] = { "Qty", "ItemID", "Description" };

			DefaultTableModel model = new DefaultTableModel(orderModelItems, columnNames) {
				@Override
				public boolean isCellEditable(int row, int column) {
					// if (String.valueOf(orderModelItems[row][1]).indexOf("P") == -1)
					// queryByModel(String.valueOf(orderModelItems[row][1]));
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
			TableColumn quantity = table.getColumnModel().getColumn(0);
			quantity.setPreferredWidth(20);
			TableColumn itemId = table.getColumnModel().getColumn(1);
			itemId.setPreferredWidth(50);
			TableColumn modelTitle = table.getColumnModel().getColumn(2);
			modelTitle.setPreferredWidth(500);

			table.setBackground(Constrant.TABLE_COLOR);
			table.setRowHeight(40);
			table.setFont(font);

			int tableSize = 50 * orderModelItems.length + 20;
			if (tableSize > 300)
				tableSize = 300;
			scrollZonePane.setBounds(40, 280, 970, tableSize);

			scrollZonePane.setViewportView(table);

			int txtHeight = 280 + 50 * orderModelItems.length;
			if (txtHeight > 580)
				txtHeight = 580;

			total.setBounds(900, txtHeight, 200, 50);
			total.setText("Total : " + String.valueOf(orderTotalCount));
			orderDisplayPanel.add(scrollZonePane);

			// modelLabel.setText("SalesOrder : "+_items.get(0).SalesOrder);
			// modelText.setText(" TOTAL: "+totalCount);

			total.setText("Total : " + String.valueOf(orderTotalCount));

			int buttonHeight = 280 + 50 * orderModelItems.length + 50;
			if (buttonHeight > 630)
				buttonHeight = 630;

			scanner = new JButton("Scan");
			scanner.setFont(font);
			scanner.setBounds(820, buttonHeight, 190, 50);

			print = new JButton("Print");
			print.setFont(font);
			print.setBounds(820, buttonHeight, 190, 50);

			prev = new JButton("Prev");
			prev.setFont(font);
			prev.setBounds(820, 700, 90, 50);

			exit = new JButton("Exit");
			exit.setFont(font);
			exit.setBounds(920, 700, 90, 50);

			print.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String shipToAddress = billToTitle + "\n              " + shippToAdddress + "\n              "
							+ shippToCity + "  " + shippToState + "\n              " + shippToZip + "    "
							+ shippToCountry;
					printer(salesOrder, date, billToTitle, shipToAddress, historyItemsInfo);
				}
			});

			scanner.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					scanner.setEnabled(false);
					scanInfo(salesOrder);
				}
			});

			prev.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (scanResultFrame != null) {
						scanResultFrame.dispose();
						scanResultFrame.setVisible(false);
					}
					orderFrame.dispose();
					orderFrame.setVisible(false);

					ShippingConfirm window = new ShippingConfirm(false);
					window.frame.setVisible(true);
				}
			});

			exit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (scanResultFrame != null) {
						scanResultFrame.dispose();
						scanResultFrame.setVisible(false);
					}

					orderFrame.dispose();
					orderFrame.setVisible(false);
				}
			});

			orderDisplayPanel.add(prev);
			orderDisplayPanel.add(exit);

			if (!isPackingList) {
				if (!isOrderClosed)
					orderDisplayPanel.add(scanner);
				else {
					// display the items
					orderDisplayPanel.add(print);

					List<Historybean> items = getShippgingItems(salesOrder);

					for (Historybean h : items) {
						historyItemsInfo += h.SN + "\n";
					}
					rowIndex = 0;
					for (Map.Entry<String, Integer> location : map.entrySet()) {
						// orderItemsInfo += location.getValue() +" " +
						// salesOrderList.get(rowIndex).ItemID +" "+
						// salesOrderList.get(rowIndex).description + " "+ items.get(0).trackingNo
						// +"\n";
						resultModelItem.add(location.getValue() + "\n" + salesOrderList.get(rowIndex).ItemID + "\n"
								+ salesOrderList.get(rowIndex).description + "\n" + items.get(0).trackingNo + "\n");
						rowIndex++;
					}
				}
			}
		}

	}

	private void placeOrderInfoAndInventoryLocation() {

		
		salesOrder = salesOrderList.get(0).salesOrder;

		isOrderClosed = salesOrderList.get(0).closed;
		orderDisplayPanel.setLayout(null);

		Font font = new Font("Verdana", Font.BOLD, 18);

		// ScrollPane for Result
		JScrollPane scrollZonePane = new JScrollPane();

		scrollZonePane.setBackground(Constrant.TABLE_COLOR);
		orderDisplayPanel.add(scrollZonePane);

		JLabel total = new JLabel("Total: ");

		total.setBounds(900, 450, 90, 50);
		total.setFont(font);
		total.setBackground(Constrant.BACKGROUN_COLOR);
		orderDisplayPanel.add(total);

		if (!salesOrderList.isEmpty()) {

			fillModelMapLocation();

			// Display the all items into table

			int colsize = 4;

			
			final Object[][] orderModelItems = new Object[pickingItems.size()][colsize];

			int rowIndex = 0;
			for (PickingItem item : pickingItems) {
				String[] zoneCode = null;
				String zoneDes = "";
				HashMap<String, Integer> loc = locMap.get(item.modelID);
				for (int j = 0; j < colsize; j++) {
					orderModelItems[rowIndex][0] = loc.get(item.location);
					orderModelItems[rowIndex][1] = item.modelID;
					orderModelItems[rowIndex][2] = item.modelDes;

					orderModelItems[rowIndex][3] = "  ["+LocationHelper.DisplayZoneCode(LocationHelper.MapZoneCode(item.location))+"] " +item.location;

				}
				rowIndex++;
			}

			// modelLabel.setText("SalesOrder : "+_items.get(0).SalesOrder);
			// modelText.setText(" TOTAL: "+totalCount);
			final Class[] packingColumnClass = new Class[] { Integer.class, String.class, String.class, String.class };

			Object packingColumnNames[] = { "Qty", "ItemID", "Description", "Location" };

			DefaultTableModel packingModel = new DefaultTableModel(orderModelItems, packingColumnNames) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}

				@Override
				public Class<?> getColumnClass(int columnIndex) {
					return packingColumnClass[columnIndex];
				}
			};

			// if(packingTable == null)
			JTable packingTable = new JTable(packingModel);
			// else
			// packingModel.fireTableDataChanged();
			TableColumn quantity = packingTable.getColumnModel().getColumn(0);
			quantity.setPreferredWidth(20);
			TableColumn itemId = packingTable.getColumnModel().getColumn(1);
			itemId.setPreferredWidth(50);
			TableColumn modelTitle = packingTable.getColumnModel().getColumn(2);
			modelTitle.setPreferredWidth(500);
			TableColumn locationCol = packingTable.getColumnModel().getColumn(3);
			locationCol.setPreferredWidth(100);
			packingTable.setCellSelectionEnabled(false);
			packingTable.setColumnSelectionAllowed(false);
			packingTable.setFocusable(false);

			packingTable.getTableHeader().setBackground(Constrant.TABLE_COLOR);
			packingTable.getTableHeader().setFont(font);

			packingTable.setBackground(Constrant.TABLE_COLOR);
			packingTable.setRowHeight(40);
			packingTable.setFont(font);

			int tableSize = 50 * orderModelItems.length + 20;
			if (tableSize > 400)
				tableSize = 400;

			scrollZonePane.setBounds(33, 20, 900, tableSize);

			scrollZonePane.setViewportView(packingTable);

			int txtSize = 280 + 50 * orderModelItems.length;
			if (txtSize > 430)
				txtSize = 430;
			total.setBounds(30, txtSize, 200, 50);
			total.setText("Total : " + String.valueOf(orderTotalCount));
			orderDisplayPanel.add(scrollZonePane);

			// modelLabel.setText("SalesOrder : "+_items.get(0).SalesOrder);
			// modelText.setText(" TOTAL: "+totalCount);

			total.setText("Total : " + String.valueOf(orderTotalCount));

			prev = new JButton("Prev");
			prev.setFont(font);
			prev.setBounds(820, 700, 90, 50);

			exit = new JButton("Exit");
			exit.setFont(font);
			exit.setBounds(920, 700, 90, 50);

			prev.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (scanResultFrame != null) {
						scanResultFrame.dispose();
						scanResultFrame.setVisible(false);
					}
					orderFrame.dispose();
					orderFrame.setVisible(false);

					ShippingConfirm window = new ShippingConfirm(true);
					window.frame.setVisible(true);
				}
			});

			exit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (scanResultFrame != null) {
						scanResultFrame.dispose();
						scanResultFrame.setVisible(false);
					}

					orderFrame.dispose();
					orderFrame.setVisible(false);
				}
			});

			orderDisplayPanel.add(prev);
			orderDisplayPanel.add(exit);

		}

	}

	private void placeOrderInfoDetail() {

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

		Font font = new Font("Verdana", Font.BOLD, 18);
		JLabel orderLabel = new JLabel("SO Number :" + salesOrder);

		orderLabel.setBounds(40, 0, 500, 50);
		orderLabel.setFont(font);
		orderLabel.setBackground(Constrant.BACKGROUN_COLOR);
		orderDisplayPanel.add(orderLabel);

		String dateStr = (date == null ? "" : date.substring(0, 10));

		JLabel transcactiondate = new JLabel("TransactionDate :" + dateStr);

		transcactiondate.setBounds(40, 40, 400, 50);
		transcactiondate.setFont(font);
		transcactiondate.setBackground(Constrant.BACKGROUN_COLOR);
		orderDisplayPanel.add(transcactiondate);

		JLabel billTo = new JLabel("Bill To :" + billToTitle);

		billTo.setBounds(40, 70, 600, 50);
		billTo.setFont(font);
		billTo.setBackground(Constrant.BACKGROUN_COLOR);
		orderDisplayPanel.add(billTo);

		JLabel custNo = new JLabel("Cust PO# :" + custPO);

		custNo.setBounds(40, 90, 400, 50);
		custNo.setFont(font);
		custNo.setBackground(Constrant.BACKGROUN_COLOR);
		orderDisplayPanel.add(custNo);

		// JLabel shipTo = new JLabel("Ship To :"+ "FITNESS OUTLET\n1067 INDUSTRY
		// DR\nSEATTLE WA \n98188");

		JLabel shipTo = new JLabel("<html>Ship To <p style='margin-left:100'>" + billToTitle + "<br/>" + shippToAdddress
				+ "<br/>" + shippToCountry + "  " + shippToState + "<br/>" + shippToZip + "</html>",
				SwingConstants.LEFT);

		shipTo.setBounds(40, 100, 600, 200);
		shipTo.setFont(font);
		shipTo.setBackground(Constrant.BACKGROUN_COLOR);
		orderDisplayPanel.add(shipTo);

		JLabel ShipVia = new JLabel("Ship Via: " + shippToVia);

		ShipVia.setBounds(40, 240, 600, 50);
		ShipVia.setFont(font);
		ShipVia.setBackground(Constrant.BACKGROUN_COLOR);
		orderDisplayPanel.add(ShipVia);

	}

	public void displayShippingResult(String salesOrder, String date, String pro, String content) {

		JFrame.setDefaultLookAndFeelDecorated(false);
		JDialog.setDefaultLookAndFeelDecorated(false);
		frame = new JFrame("Query Pannel");
		// Setting the width and height of frame
		frame.setSize(800, 630);
		frame.setLocationRelativeTo(null);
		frame.setUndecorated(true);
		frame.setResizable(false);

		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Constrant.FRAME_BORDER_BACKGROUN_COLOR));

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
			for (int j = 0; j < 3; j++) {
				String modelNo = ((String) sortedList.get(i)).substring(0, 6);
				rowData[i][0] = modelNo;
				rowData[i][1] = Constrant.models.get(modelNo).Model;
				rowData[i][2] = sortedList.get(i);
			}
		}

		totalCount.setText("Quantity : " + sortedList.size());

		Object columnNames[] = { "ItemID", "Model", "Serial Number" };
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
		table.getTableHeader().setBackground(Constrant.TABLE_COLOR);
		table.setBackground(Constrant.TABLE_COLOR);
		table.setFont(font);
		table.setRowHeight(40);

		scrollZonePane.setBounds(33, 100, 750, 50 * rowData.length + 20);
		scrollZonePane.setViewportView(table);

		panel.add(scrollZonePane);

		// Creating Report button
		report = new JButton("Report");
		report.setFont(font);
		report.setBounds(600, 400, 180, 50);
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
							// orderItemsInfo += location.getValue() +" " +
							// salesOrderList.get(rowIndex).ItemID +" "+
							// salesOrderList.get(rowIndex).description + " "+ scanItems.get(0).trackingNo
							// +"\n";
							rowIndex++;
						}

						for (Historybean h : scanItems) {
							historyItemsInfo += h.SN + "\n";
						}

						// displayLoadingBar();
						loadingframe = new LoadingFrameHelper();
						loading = loadingframe.loadingSample("Report data...");

						updateSalesOrder();

					}
				});

			}
		});

		panel.add(report);

		// Creating Report button
		JButton prev = new JButton("Prev");
		prev.setFont(font);
		prev.setBounds(600, 500, 80, 50);
		prev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				frame.dispose();
				frame.setVisible(false);
				scanInfo(salesOrder);

			}
		});

		panel.add(prev);

		// Creating Report button
		JButton exit = new JButton("Exit");
		exit.setFont(font);
		exit.setBounds(700, 500, 80, 50);
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				scanner.setEnabled(true);
				frame.dispose();
				frame.setVisible(false);
			}
		});

		panel.add(exit);
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
		shippingDate.setBounds(250, 20, 250, 50);
		panel.add(shippingDate);

		// Creating JLabel
		JLabel proLabel = new JLabel("PRO # ");

		proLabel.setBounds(50, 80, 200, 25);
		proLabel.setFont(font);
		panel.add(proLabel);

		JTextField proNumber = new JTextField(20);
		scanResultFrame.addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e) {
				proNumber.requestFocus();
			}
		});

		proNumber.setText("");
		proNumber.setFont(font);
		proNumber.setBounds(250, 70, 250, 50);
		panel.add(proNumber);

		JTextArea inputSN = new JTextArea(20, 15);
		String content = "";
		inputSN.setText(prevContent);
		String[] item = prevContent.split("\n");

		int len = 0;
		if (!inputSN.getText().toString().equals("")) {

			len = item.length;
		}
		// Creating JLabel
		JLabel modelLabel = new JLabel("Total : " + len + "/" + String.valueOf(orderTotalCount));

		modelLabel.setBounds(50, 150, 200, 100);
		modelLabel.setFont(font);
		panel.add(modelLabel);

		JScrollPane scrollPanel1 = new JScrollPane(inputSN);
		scrollPanel1.setBounds(250, 150, 250, 500);
		inputSN.setFont(font);

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
				inputSN.setText(inputSN.getText().toString() + "\n");
				String prev = inputSN.getText().toString();
				String[] item = inputSN.getText().toString().split("\n");

				boolean lenError = false;
				if (!set.contains(item[item.length - 1]) && item[item.length - 1].length() == 16
						&& set.size() <= orderTotalCount &&map.containsKey(item[item.length - 1].substring(0,6))) {

					set.add(item[item.length - 1]);
				} else {
					lenError = true;
					prev = prev.substring(0, prev.length() - (item[item.length - 1].length()) - 1);

				}

				if (lenError) {

					inputSN.setText(prev);
				} else {

					modelLabel.setForeground(Color.BLACK);
					modelLabel.setText("Total: " + set.size() + "/" + String.valueOf(orderTotalCount));

				}
			}
		});
		
		panel.add(scrollPanel1);

		InputMap inputPro = proNumber.getInputMap();
		KeyStroke enterPro = KeyStroke.getKeyStroke("ENTER");
		KeyStroke shiftEnterPro = KeyStroke.getKeyStroke("shift ENTER");
		inputPro.put(enterPro, INSERT_BREAK); // input.get(enter)) = "insert-break"
		inputPro.put(shiftEnterPro, TEXT_SUBMIT);
		ActionMap proAction = proNumber.getActionMap();
		proAction.put(INSERT_BREAK, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				inputSN.grabFocus();
			}
		});
		

		// Creating Query button
		JButton queryButton = new JButton("Add");
		queryButton.setFont(font);
		queryButton.setBounds(250, 670, 110, 50);
		queryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean verifyOrder = checkOrder(inputSN.getText().toString());
				prevContent = inputSN.getText().toString();
				if (verifyOrder) {
					scanResultFrame.dispose();
					scanResultFrame.setVisible(false);

					displayShippingResult(salesOrder, shippingDate.getText().toString(), proNumber.getText().toString(),
							inputSN.getText().toString());
				}
			}
		});

		panel.add(queryButton);

		// Creating clear button
		JButton resetButton = new JButton("Clear");
		resetButton.setFont(font);
		resetButton.setBounds(380, 670, 120, 50);
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int result = -1;
				if (inputSN.getText().toString().length() > 0) {
					result = JOptionPane.showConfirmDialog(frame, "Do you want to clear the all item?", "",
							JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					if (result == JOptionPane.YES_OPTION) {
						inputSN.setText("");
						modelLabel.setText("Total: " + "0/" + String.valueOf(orderTotalCount));
						modelLabel.setForeground(Color.BLACK);
					}
				}
			}
		});

		panel.add(resetButton);

		// Creating clear button
		JButton exitButton = new JButton("Exit");
		exitButton.setFont(font);
		exitButton.setBounds(50, 670, 180, 50);
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				scanner.setEnabled(true);
				scanResultFrame.dispose();
				scanResultFrame.setVisible(false);
			}
		});

		panel.add(exitButton);

	}

	private void exceuteCallback() {

		// it callback will be run when query the model from inventory table
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
				if (!_items.isEmpty() && isPackingList) {

					for (Itembean i : _items) {
						if (inventoryModelmap.containsKey(i.ModelNo)) {
							List<Itembean> list = inventoryModelmap.get(i.ModelNo);
							list.add(i);
							inventoryModelmap.put(i.ModelNo, list);
						} else {
							List<Itembean> list = new ArrayList<Itembean>();
							list.add(i);
							inventoryModelmap.put(i.ModelNo, list);
						}
					}

					 //System.out.println("call back, map.size():" + map.size());
					 //System.out.println("call back, inventoryModelmap.size():" +
					 //inventoryModelmap.size());
					if (inventoryModelmap.size() == map.size()) {
						if (loading != null)
							loading.setValue(100);

						if (loadingframe != null) {
							loadingframe.setVisible(false);
							loadingframe.dispose();
						}
						
						if(orderFrame == null)
							displayPickingOrderInfo(_items);
					}
				}
			}

		});

		ordersRepositoryImplRetrofit = new OrdersRepositoryImplRetrofit();
		ordersRepositoryImplRetrofit.setCustOrderServiceCallBackFunction(new CustOrderCallBackFunction() {

			@Override
			public void resultCode(int code) {
				// TODO Auto-generated method stub
				if (code == HttpRequestCode.HTTP_REQUEST_INSERT_DATABASE_ERROR) {
					JOptionPane.showMessageDialog(null, "Items already exit.");

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
						// System.out.println("update sucess");
						EventQueue.invokeLater(new Runnable() {
							public void run() {
								try {
									loading.setValue(50);
									if (!isOrderClosed)
										shippingItems(scanItems);
									else
										getShippgingItems(orders.get(0).salesOrder);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
					} else {
						pickingItems = new ArrayList<PickingItem>();
						salesOrderList = new ArrayList<CustOrderbean>();
						map = new LinkedHashMap<String, Integer>();
						OrderModelmap = new TreeMap<String, String>();
						salesOrderList = orders;
						for (CustOrderbean item : salesOrderList) {
							int count = Integer.valueOf(item.quantity);
							// System.out.println("item.ItemID:" + item.ItemID);

							if (item.ItemID != null) {
								if (map.containsKey(item.ItemID)) {

									if (!item.ItemID.contains("PL")) {
										count += map.get(item.ItemID);
										map.put(item.ItemID, count);
										orderTotalCount += count;
									}
								} else {

									if (!item.ItemID.contains("PL")) {
										orderTotalCount += count;
										map.put(item.ItemID, count);
										OrderModelmap.put(item.ItemID, item.description);
									}
								}
							}
						}

						if (!isPackingList) {
							displayOrderInfo(null);
						} else if (!salesOrderList.get(0).closed) {
							inventoryModelmap = new LinkedHashMap<String, List<Itembean>>();
							loadingframe = new LoadingFrameHelper();
							loading = loadingframe.loadingSample("Loading data...");

							EventQueue.invokeLater(new Runnable() {
								public void run() {
									try {
										loading.setValue(50);
										for (CustOrderbean _order : orders) {
											if (_order.ItemID == null || _order.ItemID.indexOf("PL") != -1)
												continue;
											else
												queryByModelAndCount(_order.ItemID, map.get(_order.ItemID));
											
											// queryByModel(_order.ItemID);
											// queryByModel(_order.ItemID);
											// queryByModelAndDate(_order.ItemID, "2015-01-01");
										}

									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							});
						} else
							JOptionPane.showMessageDialog(null, "The sales order is closed !");
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

				}

			}

			@Override
			public void getHistoryItems(List<Historybean> items) {
				if (!items.isEmpty() && !isOrderClosed) {
					// progressMonitor.close();
					// task.done();
					isOrderClosed = true;

					loading.setValue(50);
					loadingframe.setVisible(false);
					loadingframe.dispose();

					frame.dispose();
					frame.setVisible(false);

					JOptionPane.showMessageDialog(null, "Insert Data Success!");
				}

			}

		});

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

	private void fillModelMapLocation() {
	

		for (Map.Entry<String, List<Itembean>> location : inventoryModelmap.entrySet()) {
			LinkedHashMap<String, Integer> locModelMap = new LinkedHashMap<String, Integer>();
			
			String locInfo = "";
			int orderCount = map.get(location.getKey());
			String model = location.getKey();
			for (Itembean item : location.getValue()) {

				if (locModelMap.containsKey(item.Location)) {
					int itemlist = locModelMap.get(item.Location);
					if (itemlist < orderCount) {
						itemlist = itemlist + 1;
						locModelMap.put(item.Location, itemlist);
					} else
						break;
				} else {
					PickingItem pickItem = new PickingItem();
					pickItem.quantity = 1;
					pickItem.modelID = item.ModelNo;
					pickItem.modelDes =OrderModelmap.get(pickItem.modelID);
					pickItem.location = item.Location;
					pickingItems.add(pickItem);
					System.out.println(location.getKey());
					locModelMap.put(item.Location, 1);
				}

			}
			
			locMap.put(model, locModelMap);
			int zone2Cnt = 0;
			
			/*for (Map.Entry<String, Integer> code : locMap.entrySet()) {
				if (LocationHelper.MapZoneCode(code.getKey()) == 2) {
					zone2Cnt +=code.getValue();
					// locInfo = "[" + code.getKey() + "]:" + code.getValue() + "," + locInfo;
					// } else {
					locInfo += "[Zone 2]" + "[" + code.getKey() + "]:" + code.getValue() + ",";
				}
				
				if(zone2Cnt < orderCount && LocationHelper.MapZoneCode(code.getKey()) == 1) 
				{
					zone2Cnt +=code.getValue();
					// locInfo = "[" + code.getKey() + "]:" + code.getValue() + "," + locInfo;
					// } else {
					locInfo += "[Zone 1]" + "[" + code.getKey() + "]:" + code.getValue() + ",";
					//map.put(location.getKey()+"1", code.getValue());
				}
			}

			if (locInfo.length() > 0)
				locInfo = locInfo.substring(0, locInfo.length() - 1);
			else {
				for (Map.Entry<String, Integer> code : locMap.entrySet()) {
					if (LocationHelper.MapZoneCode(code.getKey()) != 2) {
						// locInfo = "[" + code.getKey() + "]:" + code.getValue() + "," + locInfo;
						// } else {
						locInfo += "[" + LocationHelper.DisplayZoneCode(LocationHelper.MapZoneCode(code.getKey())) + "]"
								+ "[" + code.getKey() + "]:" + code.getValue() + ",";
					}
				}

				locInfo = locInfo.substring(0, locInfo.length() - 1);
			}*/

			//modelMapLocationInfo.put(location.getKey(), locInfo);
		}

	}

	private void printer(String saleOrder, String date, String billTo, String shipTo, String itemsInfo) {

		String content = "Sales Order : " + saleOrder + "\n" + "TransactionDate : " + date + "\n" + "Bill To : "
				+ billTo + "\n" + "Ship To : " + shipTo + "\n";
		List<String> headersList = Arrays.asList("Total", "Item", "Model", "PRO#");

		List<List<String>> rowsList = new ArrayList<List<String>>();
		for (String s : resultModelItem) {
			String[] rowdata = s.split("\n");
			rowsList.add(Arrays.asList(rowdata));
		}

		// String result = PrintTableUtil.printReport(headersList, rowsList);
		String result = PrintTableUtil.printReport(headersList, rowsList);
		content += result + itemsInfo;
		// System.out.println(content);

		PrinterHelper print = new PrinterHelper();
		print.printItems(content);

	}

}
