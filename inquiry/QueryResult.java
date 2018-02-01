package spirit.fitness.scanner.inquiry;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
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
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import spirit.fitness.scanner.common.Constrant;
import spirit.fitness.scanner.common.HttpRequestCode;
import spirit.fitness.scanner.model.Itembean;
import spirit.fitness.scanner.model.Locationbean;
import spirit.fitness.scanner.restful.FGRepositoryImplRetrofit;
import spirit.fitness.scanner.restful.listener.InventoryCallBackFunction;
import spirit.fitness.scanner.zonepannel.ZoneMenu;

public class QueryResult {

	public JFrame resultFrame, colFrame;
	private JScrollPane scrollPanel;
	public final static int QUERY_MODEL = 0;
	public final static int QUERY_LOCATION = 1;
	public final static int QUERY_MODEL_LOCATION = 2;

	
	public static boolean isQueryRepeat = false;

	/* --------- Query by model -------- */
	// key:zone code, value:total items in the zone
	private Map<Integer, LinkedHashMap<String, List<Itembean>>> zoneMap;

	// key:zone code, value:total items in the zone
	private Map<Integer, Integer> zoneCount;

	// key:location,value:items
	private LinkedHashMap<String, List<Itembean>> map;

	private int queryModelCount = 0;

	/* --------- Query by address -------- */
	// key:location title, value:location number
	private Map<String, String> modelMapingNumber;

	private FGRepositoryImplRetrofit fgInventory;
	private int queryType;

	private JTable zone1Table,zone2Table,zone3Table,zone4Table;

	public void setContent(int type, List<Itembean> _items) {
		JFrame.setDefaultLookAndFeelDecorated(false);
		JDialog.setDefaultLookAndFeelDecorated(false);
		exceuteCallback();
		queryType = type;

		setContentLayOut(_items);

	}

	private void setContentLayOut(List<Itembean> _items) {
		String modelNo = _items.get(0).ModelNo;
		String title = Constrant.models.get(modelNo).Model;

		resultFrame = new JFrame("");
		// Setting the width and height of frame
		resultFrame.setSize(780, 600);
		resultFrame.setLocationRelativeTo(null);
		resultFrame.setUndecorated(true);
		resultFrame.setResizable(false);

		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Constrant.FRAME_BORDER_BACKGROUN_COLOR));
		panel.setBackground(Constrant.BACKGROUN_COLOR);
		// adding panel to frame
		resultFrame.add(panel);

		// set up model panel
		if (queryType == QUERY_MODEL)
			modelPannel(panel, _items, title);
		else
			locationPannel(panel, _items);

		resultFrame.setBackground(Color.WHITE);
		resultFrame.setVisible(true);

		resultFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		resultFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {

				resultFrame.dispose();
				resultFrame.setVisible(false);
			}
		});
	}

	private void setContentQueryLayOut(List<Itembean> _items) {
		String modelNo = _items.get(0).ModelNo;
		String title = Constrant.models.get(modelNo).Model;

		colFrame = new JFrame("");
		// Setting the width and height of frame
		colFrame.setSize(780, 600);
		colFrame.setLocationRelativeTo(null);
		colFrame.setUndecorated(true);
		colFrame.setResizable(false);

		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Constrant.FRAME_BORDER_BACKGROUN_COLOR));
		panel.setBackground(Constrant.BACKGROUN_COLOR);
		// adding panel to frame
		colFrame.add(panel);

		// set up model panel
		if (queryType == QUERY_MODEL)
			modelPannel(panel, _items, title);
		else
			locationPannel(panel, _items);

		colFrame.setBackground(Color.WHITE);
		colFrame.setVisible(true);

		colFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		colFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {

				resultFrame.dispose();
				resultFrame.setVisible(false);
			}
		});
	}

	private void modelPannel(JPanel panel, List<Itembean> _items, String title) {
		panel.setLayout(null);
		JLabel modelLabel = new JLabel("MODEL :" + title);

		Font font = new Font("Verdana", Font.BOLD, 18);
		modelLabel.setBounds(30, 0, 500, 50);
		modelLabel.setFont(font);
		modelLabel.setBackground(Constrant.BACKGROUN_COLOR);
		panel.add(modelLabel);

		JLabel quantity = new JLabel("QUANTITY :" + 20);

		quantity.setBounds(400, 0, 300, 50);
		quantity.setFont(font);
		quantity.setBackground(Constrant.BACKGROUN_COLOR);
		panel.add(quantity);

		// display Zone 2
		JLabel zone2 = new JLabel("Zone 2");

		zone2.setBounds(30, 40, 300, 50);
		zone2.setFont(font);
		zone2.setBackground(Constrant.BACKGROUN_COLOR);
		panel.add(zone2);

		// display Zone 1
		JLabel zone1 = new JLabel("Zone 1");

		zone1.setBounds(30, 200, 300, 50);
		zone1.setFont(font);
		zone1.setBackground(Constrant.BACKGROUN_COLOR);
		panel.add(zone1);

		// display Zone 3
		JLabel zone3 = new JLabel("Return");

		zone3.setBounds(30, 360, 300, 50);
		zone3.setFont(font);
		zone3.setBackground(Constrant.BACKGROUN_COLOR);
		panel.add(zone3);

		// display Zone 4
		JLabel zone4 = new JLabel("Show Room");

		zone4.setBounds(30, 360, 300, 50);
		zone4.setFont(font);
		zone4.setBackground(Constrant.BACKGROUN_COLOR);
		panel.add(zone4);

		// ScrollPane for Zone1
		JScrollPane scrollZone1Pane = new JScrollPane();
		scrollZone1Pane.setBounds(33, 91, 700, 200);
		panel.add(scrollZone1Pane);

		// ScrollPane for Zone2
		JScrollPane scrollZone2Pane = new JScrollPane();
		scrollZone2Pane.setBounds(33, 250, 700, 200);
		panel.add(scrollZone2Pane);

		// ScrollPane for Zone3
		JScrollPane scrollZone3Pane = new JScrollPane();
		scrollZone3Pane.setBounds(33, 300, 700, 200);
		panel.add(scrollZone3Pane);

		// ScrollPane for Zone3
		JScrollPane scrollZone4Pane = new JScrollPane();
		scrollZone4Pane.setBounds(33, 300, 700, 200);
		panel.add(scrollZone4Pane);

		// Table

		final Class[] columnClass = new Class[] { String.class, Integer.class };
		Object columnNames[] = { "LOCATION", "QUANTITY" };

		if (!_items.isEmpty()) {
			parseData(_items);
			modelLabel.setText("MODEL : " + title + "                                ");
		}

		Object[][] zone1Data = (Object[][]) putDataToTable(1);
		DefaultTableModel model1 = new DefaultTableModel(zone1Data, columnNames) {
			@Override
			public boolean isCellEditable(int row, int column) {
				if (!isQueryRepeat) {
					zone1Table.clearSelection();
					isQueryRepeat = true;
					Object location = zone1Data[row][0];
					queryType = QUERY_LOCATION;
					queryLocation(String.valueOf(location));
				}
				return false;
			}

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				return columnClass[columnIndex];
			}
		};

		final Object[][] zone2Data = (Object[][]) putDataToTable(2);
		DefaultTableModel model2 = new DefaultTableModel(zone2Data, columnNames) {
			@Override
			public boolean isCellEditable(int row, int column) {
				if (!isQueryRepeat) {
					zone2Table.clearSelection();
					isQueryRepeat = true;
					Object location = zone2Data[row][0];
					queryType = QUERY_LOCATION;
					queryLocation(String.valueOf(location));
				}
				return false;
			}

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				return columnClass[columnIndex];
			}
		};

		final Object[][] zone3Data = (Object[][]) putDataToTable(3);
		DefaultTableModel model3 = new DefaultTableModel(zone3Data, columnNames) {
			@Override
			public boolean isCellEditable(int row, int column) {
				if (!isQueryRepeat) {
					zone3Table.clearSelection();
					isQueryRepeat = true;
					Object location = zone3Data[row][0];
					queryType = QUERY_LOCATION;
					queryLocation(String.valueOf(location));
				}
				return false;
			}

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				return columnClass[columnIndex];
			}
		};
		final Object[][] zone4Data = (Object[][]) putDataToTable(4);
		DefaultTableModel model4 = new DefaultTableModel(zone4Data, columnNames) {
			@Override
			public boolean isCellEditable(int row, int column) {
				if (!isQueryRepeat) {
					zone4Table.clearSelection();
					isQueryRepeat = true;
					Object location = zone4Data[row][0];
					queryType = QUERY_LOCATION;
					queryLocation(String.valueOf(location));
				}
				return false;
			}

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				return columnClass[columnIndex];
			}
		};

		if (zone1Data != null) {
			zone1.setText("Zone 1 : " + zoneCount.get(1));
			scrollZone1Pane.setBounds(33, 250, 700, 50 * zone1Data.length + 20);
			scrollZone1Pane.setBackground(Constrant.BACKGROUN_COLOR);

			zone1Table = new JTable(model1);
			//zone1Table.setCellSelectionEnabled(false);
			//zone1Table.setColumnSelectionAllowed(false);
	
			zone1Table.getTableHeader().setBackground(Constrant.TABLE_COLOR);
			zone1Table.getTableHeader().setFont(font);
			zone1Table.setBackground(Constrant.TABLE_COLOR);
			zone1Table.setRowHeight(40);
			zone1Table.setFont(font);
			scrollZone1Pane.setViewportView(zone1Table);
		} else {
			scrollZone1Pane.setVisible(false);
			zone1.setVisible(false);
		}

		if (zone2Data != null) {
			zone2.setText("Zone 2 : " + zoneCount.get(2));
			scrollZone2Pane.setBounds(33, 91, 700, 50 * zone2Data.length + 20);
			scrollZone2Pane.setBackground(Constrant.TABLE_COLOR);

			zone2Table = new JTable(model2);
			//zone2Table.setCellSelectionEnabled(false);
			//zone2Table.setColumnSelectionAllowed(false);
			
			
			zone2Table.getTableHeader().setBackground(Constrant.TABLE_COLOR);
			zone2Table.getTableHeader().setFont(font);
			zone2Table.setBackground(Constrant.TABLE_COLOR);
			zone2Table.setRowHeight(40);
			zone2Table.setFont(font);
			scrollZone2Pane.setViewportView(zone2Table);
		} else {
			scrollZone2Pane.setVisible(false);
			zone2.setVisible(false);
		}

		if (zone3Data != null) {
			zone3.setText("Return : " + zoneCount.get(3));
			scrollZone3Pane.setBounds(33, 420, 700, 50 * zone3Data.length + 20);
			scrollZone3Pane.setBackground(Constrant.TABLE_COLOR);

			zone3Table = new JTable(model3);
			zone3Table.getTableHeader().setBackground(Constrant.TABLE_COLOR);
			zone3Table.getTableHeader().setFont(font);
			zone3Table.setBackground(Constrant.TABLE_COLOR);
			zone3Table.setRowHeight(40);
			zone3Table.setFont(font);
			scrollZone3Pane.setViewportView(zone3Table);
		} else {
			scrollZone3Pane.setVisible(false);
			zone3.setVisible(false);
		}

		if (zone4Data != null) {
			zone4.setText("Show Room : " + zoneCount.get(4));
			scrollZone4Pane.setBounds(33, 420, 700, 50 * zone4Data.length + 20);
			scrollZone4Pane.setBackground(Constrant.TABLE_COLOR);

			zone4Table = new JTable(model4);
			zone4Table.getTableHeader().setBackground(Constrant.TABLE_COLOR);
			zone4Table.getTableHeader().setFont(font);
			zone4Table.setBackground(Constrant.TABLE_COLOR);
			zone4Table.setRowHeight(40);
			zone4Table.setFont(font);
			scrollZone4Pane.setViewportView(zone4Table);
		} else {
			scrollZone4Pane.setVisible(false);
			zone4.setVisible(false);
		}

		quantity.setText("QUANTITY :" + queryModelCount);
		panel.setBackground(Constrant.BACKGROUN_COLOR);

		JButton prev = new JButton(new AbstractAction("Prev") {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (isQueryRepeat && colFrame != null) {
					colFrame.dispose();
					colFrame.setVisible(false);
				} else if (!isQueryRepeat && resultFrame != null) {
					resultFrame.dispose();
					resultFrame.setVisible(false);

					QueryPannel window = new QueryPannel();
					window.frame.setVisible(true);
				}

				isQueryRepeat = false;
			}
		});

		JButton exit = new JButton(new AbstractAction("Exit") {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (!isQueryRepeat && resultFrame != null) {
					resultFrame.dispose();
					resultFrame.setVisible(false);
				}
				
				if(colFrame != null) 
				{
					colFrame.dispose();
					colFrame.setVisible(false);
				}
				isQueryRepeat = false;
			}
		});

		prev.setFont(font);
		exit.setFont(font);
		prev.setBounds(33, 520, 100, 50);
		exit.setBounds(150, 520, 100, 50);
		panel.add(prev);
	    panel.add(exit);

	}

	private void locationPannel(JPanel panel, List<Itembean> _items) {
		modelMapingNumber = new HashMap<String, String>();
		String locationbean = _items.get(0).Location;
		Locationbean title = Constrant.locations.get(locationbean);

		panel.setLayout(null);

		Font font = new Font("Verdana", Font.BOLD, 18);
		// Creating JLabel
		JLabel modelLabel = new JLabel("Location : " + locationbean);

		modelLabel.setBounds(30, 30, 400, 25);
		modelLabel.setFont(font);
		panel.add(modelLabel);

		JLabel modelText = new JLabel("TOTAL: 50");
		modelText.setFont(font);
		modelText.setBounds(500, 30, 300, 25);
		panel.add(modelText);

		// ScrollPane for Result
		JScrollPane scrollZonePane = new JScrollPane();

		scrollZonePane.setBackground(Constrant.TABLE_COLOR);
		panel.add(scrollZonePane);

		if (!_items.isEmpty()) {

			TreeMap<String, List<Itembean>> map = new TreeMap<String, List<Itembean>>();

			for (Itembean item : _items) {

				String key = String.valueOf(item.ModelNo);
				if (key.equals("131100") || key.equals("160053") ||key.equals("155012") || key.equals("155112") ||  key.equals("168812") || key.equals("160086")
						|| key.equals("611541") || key.equals("900571")
						|| key.equals("951113") || key.equals(""))
					continue;
				
				if (map.containsKey(item.ModelNo)) {
					List<Itembean> items = map.get(item.ModelNo);
					items.add(item);
				} else {
					List<Itembean> items = new ArrayList<Itembean>();
					items.add(item);
					map.put(item.ModelNo, items);
				}
			}

			final Object[][] rowData = new Object[map.size()][2];
			int totalCount = 0;
			int rowIndex = 0;

			for (Map.Entry<String, List<Itembean>> location : map.entrySet()) {
				for (int j = 0; j < 2; j++) {

					System.out.println(location.getKey());
					rowData[rowIndex][0] = Constrant.models.get(location.getKey()).Model;
					rowData[rowIndex][1] = location.getValue().size();

					modelMapingNumber.put(Constrant.models.get(location.getKey()).Model, location.getKey());
				}
				totalCount += location.getValue().size();
				rowIndex++;
			}

			modelLabel.setText("Location : " + _items.get(0).Location + "( Zone "
					+ Constrant.locations.get(_items.get(0).Location).ZoneCode + ")");
			modelText.setText(" TOTAL: " + totalCount);
			final Class[] columnClass = new Class[] { String.class, Integer.class };

			Object columnNames[] = { "MODEL", "QUANTITY" };
			DefaultTableModel model = new DefaultTableModel(rowData, columnNames) {
				@Override
				public boolean isCellEditable(int row, int column) {
					if (!isQueryRepeat) {
						isQueryRepeat = true;
						queryType = QUERY_MODEL;
						Object model = modelMapingNumber.get(rowData[row][0]);
						queryModel(String.valueOf(model));
					}
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

			int heigh = 0;

			if (50 * rowData.length + 20 > 400)
				heigh = 400;
			else
				heigh = 50 * rowData.length + 20;
			scrollZonePane.setBounds(33, 91, 700, heigh);
			scrollZonePane.setViewportView(table);

			panel.add(scrollZonePane);

		}

		JButton prev = new JButton(new AbstractAction("Prev") {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (isQueryRepeat && colFrame != null) {
					colFrame.dispose();
					colFrame.setVisible(false);
				} else if (!isQueryRepeat && resultFrame != null) {
					resultFrame.dispose();
					resultFrame.setVisible(false);

					QueryPannel window = new QueryPannel();
					window.frame.setVisible(true);
				}
				isQueryRepeat = false;

			}
		});

		JButton exit = new JButton(new AbstractAction("Exit") {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (!isQueryRepeat && resultFrame != null) {
					resultFrame.dispose();
					resultFrame.setVisible(false);
				}
				
				if(colFrame != null) 
				{
					colFrame.dispose();
					colFrame.setVisible(false);
				}

				isQueryRepeat = false;
			}
		});

		prev.setFont(font);
		exit.setFont(font);
		prev.setBounds(33, 520, 100, 50);
		exit.setBounds(150, 520, 100, 50);
		panel.add(prev);
	    panel.add(exit);

	}

	private void parseData(List<Itembean> _items) {
		zoneMap = new HashMap<Integer, LinkedHashMap<String, List<Itembean>>>();

		zoneCount = new HashMap<Integer, Integer>();

		// key:location, value:quantity
		map = new LinkedHashMap<String, List<Itembean>>();

		for (Itembean item : _items) {
					
			if (map.containsKey(item.Location)) {
				List<Itembean> items = map.get(item.Location);
				items.add(item);
				map.put(item.Location, items);
			} else {
				List<Itembean> items = new ArrayList<Itembean>();
				items.add(item);
				map.put(item.Location, items);
			}
		}

		for (Map.Entry<String, List<Itembean>> location : map.entrySet()) {
			Locationbean bean = Constrant.locations.get(location.getKey());

			String key = String.valueOf(bean.ZoneCode);
			if (key.trim().equals(""))
				continue;

			if (zoneMap.containsKey(bean.ZoneCode)) {
				LinkedHashMap<String, List<Itembean>> items = zoneMap.get(bean.ZoneCode);
				items.put(location.getKey(), location.getValue());
				zoneMap.put(bean.ZoneCode, items);
				zoneCount.put(bean.ZoneCode, zoneCount.get(bean.ZoneCode) + location.getValue().size());
			} else {
				LinkedHashMap<String, List<Itembean>> items = new LinkedHashMap<String, List<Itembean>>();
				items.put(location.getKey(), location.getValue());
				zoneMap.put(bean.ZoneCode, items);
				zoneCount.put(bean.ZoneCode, location.getValue().size());
			}
		}

	}

	private Object putDataToTable(int code) {
		LinkedHashMap<String, List<Itembean>> zoneData = zoneMap.get(code);

		if (zoneData == null)
			return null;
		Object rowData[][] = new Object[zoneData.size()][2];
		int rowIndex = 0;

		for (Map.Entry<String, List<Itembean>> location : zoneData.entrySet()) {
			
			for (int j = 0; j < 2; j++) {
				
				rowData[rowIndex][0] = location.getKey();
				rowData[rowIndex][1] = location.getValue().size();

			}
			queryModelCount += location.getValue().size();
			rowIndex++;
		}
		return rowData;
	}

	private void exceuteCallback() {

		fgInventory = new FGRepositoryImplRetrofit();
		fgInventory.setinventoryServiceCallBackFunction(new InventoryCallBackFunction() {

			@Override
			public void resultCode(int code) {
				// TODO Auto-generated method stub
				if (code == HttpRequestCode.HTTP_REQUEST_INSERT_DATABASE_ERROR) {

				}
			}

			@Override
			public void getInventoryItems(List<Itembean> items) {
				if (!items.isEmpty()) {
					System.out.println(queryType);
					setContentQueryLayOut(items);
				}
			}
		});

	}

	private void queryLocation(String Location) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					fgInventory.getItemsByLocation(Integer.valueOf(Location));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	private void queryModel(String modelNo) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					fgInventory.getItemsByModel(Integer.valueOf(modelNo));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

}
