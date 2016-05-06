package rmi.network.server;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import rmi.network.common.AddClient;
import rmi.network.interfaces.ClientInterface;
import rmi.network.interfaces.ClientInterface.Task;
import rmi.network.interfaces.ServerInterface;
import rmi.network.server.table.ValuesTable;

public class LANServer extends UnicastRemoteObject implements ServerInterface {

	private String client;
	private HashMap<String, ClientInterface> listenersList;
	private HashMap<String, ServerMessage> messageList;
	private HashMap<String, Timer> timerList;
	private HashMap<String, Long> timeCounter;
	private AddClient addClient;
	String host, nameClient;
	static int port = 6000;
	JFrame frame;
	public LANServer() throws RemoteException {
		super();
		initComponent();
	}
	
	public static void main(String[] args) throws RemoteException {
		LANServer server;
		try{
			server = new LANServer();
			Registry reg = LocateRegistry.createRegistry(port);
			try {
				Naming.bind("rmi://localhost:"+port+"/Remote", server);
				//reg.list();
				//System.out.println(List());
				System.out.println("Server is running");
			} catch (AlreadyBoundException e) {
				e.printStackTrace();
			}
//			Registry registry = LocateRegistry.getRegistry(6000);
//			registry.rebind("Connect", server);
		}catch(IOException e){
			JOptionPane.showMessageDialog(null, "A server is running...",
					"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}

	public void initComponent(){
		listenersList = new HashMap<String, ClientInterface>();
		messageList = new HashMap<String, ServerMessage>();
		timerList = new HashMap<String, Timer>();
		timeCounter = new HashMap<String, Long>();
		
		JLabel onlineLbl = new JLabel("Online:");
		onlTxt = new JTextField(4);
		onlTxt.setText("0");
		JLabel numComputerLbl = new JLabel("Connection:");
		numComputerTxt = new JTextField(4);
		numComputerTxt.setText("0");
		onlTxt.setEditable(false);
		numComputerTxt.setEditable(false);
		
		addClientBtn = new JButton("Add Client");
		
		frame = new JFrame("Server");
		frame.setSize(500, 500);
		frame.setResizable(false);
		
		frame.setLayout(new GridLayout(2,1));
		
		valuesTbl = new ValuesTable();
		//tablePnl = new JPanel();
		//tablePnl.setBorder(new EmptyBorder(40, 40, 40, 40));
		//tablePnl.setMaximumSize(new Dimension(350, 350));
		table = new JTable(valuesTbl);
		table.setBorder(new EmptyBorder(40, 40, 40, 40));
		table.setVisible(true);
		TableColumn column;
		DefaultTableCellRenderer dtcr;
		for(int i = 0; i < table.getColumnCount(); i++){
			column = table.getColumnModel().getColumn(i);
			dtcr = new DefaultTableCellRenderer();
			dtcr.setHorizontalAlignment(SwingConstants.CENTER);
			column.setCellRenderer(dtcr);
		}
		scroll = new JScrollPane(table);
		
		JPanel inforPnl = new JPanel();
		inforPnl.setLayout(new FlowLayout());
		JPanel allBtnPnl = new JPanel();
		allBtnPnl.setLayout(new GridLayout(2, 1));
		
		btnPnl = new JPanel();
		btnPnl.setLayout(new GridLayout(2, 1));
		btnPnl.setBorder(new EmptyBorder(40, 40, 40, 40));
		btnPnl.add(inforPnl);
		btnPnl.add(allBtnPnl);
		inforPnl.add(onlineLbl);
		inforPnl.add(onlTxt);
		inforPnl.add(numComputerLbl);
		inforPnl.add(numComputerTxt);
		inforPnl.add(addClientBtn);
		
		shutDownBtn = new JButton("Shut down");
		restartBtn = new JButton("Restart");
		logOffBtn = new JButton("Log Off");
		lockBtn = new JButton("Lock");
		messageBtn = new JButton("Message");
		disconnectBtn = new JButton("Disconnect");	
		
		allBtnPnl.add(shutDownBtn);
		allBtnPnl.add(restartBtn);
		allBtnPnl.add(logOffBtn);
		allBtnPnl.add(lockBtn);
		allBtnPnl.add(messageBtn);
		allBtnPnl.add(disconnectBtn);
		
		//set number of computers online
		
		frame.add(scroll);
		frame.add(btnPnl);
		frame.setVisible(true);
		
		frame.addWindowListener(new WindowListener() {

			
			@Override
			public void windowClosing(WindowEvent arg0) {
				if(!listenersList.isEmpty()){
					ClientInterface clientLst;
					for(Iterator<ClientInterface> i = listenersList.values().iterator(); i.hasNext();){
						clientLst = i.next();
						try {
							clientLst.setClientInfo("Disconnected");
						} catch (RemoteException e) {
							continue;
						}
					}
				}
			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

		});
		
		
		frame.validate();
		
		shutDownBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = table.getSelectedRow();
				if(row != -1){
					if(valuesTbl.values[row][ValuesTable.STATUS].equals("Online")){
						int choice = JOptionPane.showConfirmDialog(null,
								"Do u want to shut down this computer?", "Shut down", JOptionPane.YES_NO_OPTION);
						if(choice == JOptionPane.YES_OPTION){
							if(valuesTbl.values[row][ValuesTable.NAME] != null){
								notifyAction(Task.SHUTDOWN);
								valuesTbl.values[row][ValuesTable.STATUS] = "Disconnected";
								table.updateUI();							
							}
						}
					}
				}
			}
		});
		
		restartBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int row = table.getSelectedRow();
				if(row != -1){
					if(valuesTbl.values[row][ValuesTable.STATUS].equals("Online")){
						int choice = JOptionPane.showConfirmDialog(null, 
								"Do u wanna restart this computer?", "Restart", JOptionPane.YES_NO_OPTION);
						if(choice == JOptionPane.YES_OPTION){
							if(valuesTbl.values[row][ValuesTable.NAME] != null){
								notifyAction(Task.RESTART);
								valuesTbl.values[row][ValuesTable.STATUS] = "Disconnected";
								table.updateUI();
							}
							
						}
					}
				}
			}
		});
		
		logOffBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int row = table.getSelectedRow();
				if(row != -1){
					if(valuesTbl.values[row][ValuesTable.STATUS].equals("Online")){
						int choice = JOptionPane.showConfirmDialog(null, 
								"Do u wanna Log off this computer?", "Restart", JOptionPane.YES_NO_OPTION);
						if(choice == JOptionPane.YES_OPTION){
							if(valuesTbl.values[row][ValuesTable.NAME] != null){
								notifyAction(Task.LOG_OFF);
								valuesTbl.values[row][ValuesTable.STATUS] = "Available";
								table.updateUI();
							}
							
						}
					}
				}
			}
		});
		
		lockBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int row = table.getSelectedRow();
				if(row != -1){
					if(valuesTbl.values[row][ValuesTable.STATUS].equals("Online")){
						int choice = JOptionPane.showConfirmDialog(null, 
								"Do u wanna Lock this computer?", "Restart", JOptionPane.YES_NO_OPTION);
						if(choice == JOptionPane.YES_OPTION){
							if(valuesTbl.values[row][ValuesTable.NAME] != null){
								notifyAction(Task.LOG_OFF);
								valuesTbl.values[row][ValuesTable.STATUS] = "Available";
								table.updateUI();
							}
							
						}
					}
				}
			}
		});
		
		messageBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int row = table.getSelectedRow();
				if(row != -1){
					String ip = valuesTbl.values[row][ValuesTable.IP_ADDRESS].toString();
					String status = valuesTbl.values[row][ValuesTable.STATUS].toString();
					if(ip != null && status != null && status.equals("Online")){
						messageList.get(ip).setVisible(true);
					}
				}
			}
		});
		
		disconnectBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int row = table.getSelectedRow();
				if(row != -1){
					String ip = valuesTbl.values[row][ValuesTable.IP_ADDRESS].toString();
					String status = valuesTbl.values[row][ValuesTable.STATUS].toString(); 
					if(ip != null && status != null && status.equals("Online")){
						int choice = JOptionPane.showConfirmDialog(null, 
								"Do u wanna disconnect this computer?", "Restart", JOptionPane.YES_NO_OPTION);
						if(choice == JOptionPane.YES_OPTION){
							valuesTbl.values[row][ValuesTable.STATUS] = "Disconnect";
							try {
								UnicastRemoteObject.unexportObject(listenersList.get(ip),true);
							} catch (NoSuchObjectException e) {
								e.printStackTrace();
							}
							table.updateUI();
						}
							
					}
				}
			}
			
		});
	
		addClientBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				addClient = new AddClient();
				
				addClient.btn_OK.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						if(!"".equals(addClient.tf_host.getText()) 
								&& !"".equals(addClient.tf_nameClient.getText())){
							host = addClient.tf_host.getText();
							nameClient = addClient.tf_nameClient.getText();
							try {
								
								setInforClient(nameClient, host, "", "");
							} catch (RemoteException e1) {
								e1.printStackTrace();
							}
							addClient.dispose();
						}
						else{
							addClient.tf_nameClient.setText("Enter the client name");
							addClient.tf_host.setText("Enter the host name");
						}
					}
				});
				
				addClient.btn_CANCEL.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						System.exit(0);
					}
				});
			}
		});
	}
	

	@Override
	public void setInfoOnServer(String name, String ip, String status, String time) throws RemoteException {
		int i;
		//tim dia chi ip phu hop de update infor
		for(i=0; i <valuesTbl.rowCount; i++){
			if(valuesTbl.values[i][ValuesTable.IP_ADDRESS].equals(ip)){
				if(name!=null)valuesTbl.values[i][ValuesTable.NAME] = name;
				if(ip!=null)valuesTbl.values[i][ValuesTable.IP_ADDRESS] = ip;
				if(status!=null)valuesTbl.values[i][ValuesTable.STATUS] = status;
				if(time!=null)valuesTbl.values[i][ValuesTable.TIME] = time;
				break;
			}
		}
		if(i == valuesTbl.rowCount){
			valuesTbl.values[i][ValuesTable.NAME] = name;
			valuesTbl.values[i][ValuesTable.IP_ADDRESS] = ip;
			valuesTbl.values[i][ValuesTable.STATUS] = status;
			if(time!=null)valuesTbl.values[i][ValuesTable.TIME] = time;
			valuesTbl.rowCount++;
		}
		table.updateUI();
		int numCon = valuesTbl.rowCount;
		System.out.println("rowCount: "+ numCon);
		numComputerTxt.setText(String.valueOf(numCon));
		int numOnl = 0;
		for(int j = 0; i<numCon; i++){
			if("Online".equals(valuesTbl.values[j][ValuesTable.STATUS])){
				numOnl++;
			}
			onlTxt.setText(String.valueOf(numOnl));
		}
		System.out.println("online: "+ numCon);
		frame.validate();
	}

	@Override
	public void setInforClient(String name, String ip, String status, String time) throws RemoteException {

		//set infor when add client on server
		System.out.println("row count: "+valuesTbl.rowCount);
		System.out.println("host: "+host+" port: "+port+ " nameClient: "+nameClient);
		for(int i=0; i <=valuesTbl.rowCount; i++){
			if(i == valuesTbl.rowCount){
				valuesTbl.values[i][ValuesTable.NAME] = name;
				valuesTbl.values[i][ValuesTable.IP_ADDRESS] = ip;
				valuesTbl.values[i][ValuesTable.STATUS] = status;
				if(time!=null)valuesTbl.values[i][ValuesTable.TIME] = time;
				valuesTbl.rowCount++;
			}
			table.updateUI();
			int numCon = valuesTbl.rowCount;
			System.out.println("rowCount: "+ numCon);
			numComputerTxt.setText(String.valueOf(numCon));
			int numOnl = 0;
			for(int j = 0; i<numCon; i++){
				if("Online".equals(valuesTbl.values[j][ValuesTable.STATUS])){
					numOnl++;
				}
				onlTxt.setText(String.valueOf(numOnl));
			}
			System.out.println("online: "+ numCon);
			frame.validate();
		}
	}

	@Override
	public String getClientName(String clientName) throws RemoteException {
		return this.client = clientName;
	}

	//check client nay co dang ket noi toi voi ip address hay k
	@Override
	public boolean isConnected(String ip) throws RemoteException {
		if(listenersList.get(ip) != null) return true;
		return false;
	}

	public void notifyAction(Task task){
		//get infor client
		String name = valuesTbl.values[valuesTbl.getRowCount()][ValuesTable.NAME].toString();
		String ipAddress = valuesTbl.values[valuesTbl.getRowCount()][ValuesTable.IP_ADDRESS].toString();
		String status = valuesTbl.values[valuesTbl.getRowCount()][ValuesTable.STATUS].toString();
		String time = valuesTbl.values[valuesTbl.getRowCount()][ValuesTable.TIME].toString();
		ClientInterface client = listenersList.get(ipAddress);
		
		try {
			client.doClientTasks(task);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		try {
			removeClientListener(ipAddress);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JOptionPane.showConfirmDialog(null, "This client has been disconnected!!!",
				"Disconnected", JOptionPane.OK_OPTION);
		try {
			setInfoOnServer(name, ipAddress, "Disconnected", time);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

	/**
	 * @param listener
	 * @param ip
	 * @throws RemoteException
	 */
	public void sendMessage(ServerMessage serverMessage, String ip){
		if(!"".equals(serverMessage.txtTyping.getText()) && serverMessage.txtTyping.getText()!= null){
			String msg = "SERVER" + serverMessage.txtTyping.getText() + "\n\n";
			serverMessage.messageArea.append(msg);
			serverMessage.txtTyping.setText(null);
			serverMessage.txtTyping.requestFocus();
			
			try {
				listenersList.get(ip).showServerMessage(msg);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void addClientListener(ClientInterface listener, String ip) throws RemoteException {
		listenersList.put(ip, listener);
		ServerMessage serverMessage = new ServerMessage("Message "+ client);
		serverMessage.txtTyping.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				sendMessage(serverMessage,ip);
			}
		});
		
		serverMessage.btnSend.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				sendMessage(serverMessage,ip);
			}
		});
		
		//show time online of client
		Timer timeTimer = new Timer(1000, new ActionListener() {

			StringBuffer timerString = new StringBuffer("");

			@Override
			public void actionPerformed(ActionEvent e) {
				int i;
				long hours, mins, secs, timeCount;
				String ip, status;
				for (i = 0; i < valuesTbl.rowCount; i++) {
					timerString.delete(0, timerString.length());
					ip = valuesTbl.values[i][ValuesTable.IP_ADDRESS]
							.toString();
					status = valuesTbl.values[i][ValuesTable.STATUS]
							.toString();
					if ("Online".equals(status) && ip.equals(ip)) {
						timeCount = timeCounter.get(ip);
						timeCounter.put(ip, timeCount + 1);
						hours = timeCount / 3600;
						mins = (timeCount % 3600) / 60;
						secs = timeCount % 60;
						if (hours < 10)
							timerString.append("0");
						timerString.append(hours + ":");
						if (mins < 10)
							timerString.append("0");
						timerString.append(mins + ":");
						if (secs < 10)
							timerString.append("0");
						timerString.append(secs);
						valuesTbl.values[i][ValuesTable.TIME] = timerString;
					}
				}
				
				table.updateUI();
				frame.validate();

				if (!listenersList.isEmpty()) {
					for (String key : listenersList.keySet()) {
						try {
							listenersList.get(key).setClientInfo("Connected");
						} catch (RemoteException e1) {
							try {
								removeClientListener(key);
							} catch (RemoteException e2) {
								e2.printStackTrace();
							}
							try {
								setInfoOnServer(null, key, "Disconnected", null);
							} catch (RemoteException e2) {
								e2.printStackTrace();
							}
						}
					}
				}
			}
		});
		timeTimer.start();
		timerList.put(ip, timeTimer);

		messageList.put(ip, serverMessage);

		timeCounter.put(ip, new Long(0));

	}

	@Override
	public void updateInforClient(String name, String ip, String status, String time) throws RemoteException {
		
	}

	@Override
	public void removeClientListener(String ip) throws RemoteException {
		listenersList.remove(ip);
		messageList.remove(ip);
		timerList.get(ip).stop();
		
	}



	@Override
	public String showClientMessage(String ipAddress, String message) throws RemoteException {
		messageList.get(ipAddress).messageArea.append(message);
		(messageList.get(ipAddress)).setVisible(true);
		return null;
	}


	private JPanel tablePnl;
	private JPanel btnPnl;
	JPanel borderPnl;
	
	private JButton shutDownBtn, restartBtn, logOffBtn,lockBtn, messageBtn,disconnectBtn, addClientBtn;
	private JTextField onlTxt;
	private JTextField numComputerTxt;
	private JTable table;
	private ValuesTable valuesTbl;
	private JScrollPane scroll;
	
	
}