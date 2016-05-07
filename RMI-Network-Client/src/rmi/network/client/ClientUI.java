package rmi.network.client;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import rmi.network.bean.client;
import rmi.network.common.AddClient;
import rmi.network.interfaces.ClientInterface;
import rmi.network.interfaces.ServerInterface;

public class ClientUI extends UnicastRemoteObject implements ClientInterface {

	protected ClientUI() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String clientName;
	private static String ipAddress;
	private static ServerInterface server;
	private static ClientMessage clientMessage;
	private static ClientInterface thisClient;
	private AddClient addClient;
	private client aclient;
	static int port = 1990;
	private String host;
	private static boolean isconnected = false;
	
	
	private static final int NAME = 0;
	private static final int IP_ADDRESS = 1;
	private static final int STATUS = 2;
	private static final int TIME = 3;
	
	private JFrame frame;
	private JPanel fieldPnl;
	private JPanel btnPnl;
	private static JButton connectBtn;
	private static JButton messageBtn;
	private static JButton lockBtn;
	private static JTextField[] inforTxt = new JTextField[4];
	
	private static Timer timer;
	private long timeCount, mins, hours, seconds;
	private StringBuffer timeBuffer;
	
	public void initComponent() throws RemoteException{
		thisClient = new ClientUI();
		frame = new JFrame("Client");
		frame.setSize(300, 300);;
		frame.setLayout(new GridLayout(2, 1));
		frame.setLocation(200, 100);
		
		
		fieldPnl = new JPanel();
		fieldPnl.setLayout(new GridLayout(4, 2));
		JLabel[] inforLbl = new JLabel[4] ;
		for(int i =0; i< inforTxt.length; i++ ){
			inforLbl[i] = new JLabel();
			inforTxt[i] = new JTextField();
			inforTxt[i].setEditable(false);
			fieldPnl.add(inforLbl[i]);
			fieldPnl.add(inforTxt[i]);
		}
		inforTxt[TIME].setText("00:00:00");
		inforLbl[STATUS].setText("Status");
		inforLbl[NAME].setText("Name");
		inforLbl[IP_ADDRESS].setText("Ip Address");
		inforLbl[TIME].setText("Online time");
		
		fieldPnl.setBorder(new EmptyBorder(20, 20, 20, 20));
		
		btnPnl = new JPanel();
		btnPnl.setLayout(new FlowLayout());
		btnPnl.setBorder(new EmptyBorder(20, 20, 20, 20));
		connectBtn = new JButton("Connect");
		messageBtn = new JButton("Message");
		lockBtn = new JButton("Lock");
		btnPnl.add(connectBtn);
		btnPnl.add(messageBtn);
		btnPnl.add(lockBtn);
		
		frame.add(fieldPnl);
		frame.add(btnPnl);
		frame.setVisible(true);
		
		clientMessage = new ClientMessage("Message");
		
		//send message via button
		clientMessage.btnSend.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!"".equals(clientMessage.txtTyping.getText())
						&& clientMessage.txtTyping.getText() != null) {
					String ms = clientMessage.txtTyping.getText();

					try {
						server.showClientMessage(ipAddress, clientName + ": " + ms
								+ "\n\n");
					} catch (RemoteException e1) {
						JOptionPane.showMessageDialog(null,
								"The server is not running...", "Error",
								JOptionPane.ERROR_MESSAGE);
						clientMessage.dispose();
					}
					clientMessage.messageArea.append("Me: " + ms + "\n\n");
					clientMessage.txtTyping.setText(null);
					clientMessage.txtTyping.requestFocus();
				}
			}
		});
		
		//send message by Enter btn on textfield
		clientMessage.txtTyping.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!"".equals(clientMessage.txtTyping.getText())
						&& clientMessage.txtTyping.getText() != null) {
					String ms = clientMessage.txtTyping.getText();

					try {
						server.showClientMessage(ipAddress, clientName + ": " + ms
								+ "\n\n");
					} catch (RemoteException e1) {
						JOptionPane.showMessageDialog(null,
								"The server is not running...", "Error",
								JOptionPane.ERROR_MESSAGE);
						clientMessage.dispose();
					}
					clientMessage.messageArea.append("Me: " + ms + "\n\n");
					clientMessage.txtTyping.setText(null);
					clientMessage.txtTyping.requestFocus();
				}
			}
		});
		
		//action listener of btn on main client
		connectBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					connectClient();
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		//block
		lockBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int option = JOptionPane.showConfirmDialog(null,
						"Do you want to lock this compputer?", "Alarm",
						JOptionPane.ERROR_MESSAGE);
				if (option == JOptionPane.YES_OPTION)
					try {
						doClientTasks(Task.LOCK);
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
			}
		});
		
		//connect message
		messageBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if ("Connected".equals(inforTxt[STATUS].getText()))
					clientMessage.setVisible(true);
				else
					JOptionPane.showMessageDialog(null,
							"You have to connect to server first!!", "Error",
							JOptionPane.ERROR_MESSAGE);
			}
		});
		
		frame.validate();
		timeCount = 0;
		timeBuffer = new StringBuffer();
		timer = new Timer(1000, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				timeBuffer.delete(0, timeBuffer.length());
				timeCount++;
				hours = timeCount / 3600;
				mins = (timeCount % 3600) / 60;
				seconds = timeCount % 60;

				if (hours < 10)
					timeBuffer.append("0");
				timeBuffer.append(hours + ":");
				if (mins < 10)
					timeBuffer.append("0");
				timeBuffer.append(mins + ":");
				if (seconds < 10)
					timeBuffer.append("0");
				timeBuffer.append(seconds);
				inforTxt[TIME].setText(timeBuffer.toString());

				try {
					server.isConnected(ipAddress);
				} catch (RemoteException e1) {
					try {
						setClientInfo("Disconnected");
					} catch (RemoteException e2) {
						e2.printStackTrace();
					}
				}
			}
		});
	}
	@Override
	public void doClientTasks(Task task) throws RemoteException {
		String command ="";
		try{
			switch (task) {
			case SHUTDOWN:
				command = "shutdown -s -f -t 0";
				break;

			case RESTART:
				command = "shutdown -r -f -t 0";
				break;
				
			case LOCK:
				command = "rundll32.exe user32.dll,LockWorkStation";
				break;
				
			case LOG_OFF:
				command = "shutdown -l -f";
				break;
			}
			Runtime.getRuntime().exec(command);
		}catch(Exception e){
			JOptionPane.showConfirmDialog(null, "Fail to execute this task");
		}
	}

	@Override
	public void setClientInfo(String status) throws RemoteException {
		inforTxt[STATUS].setText(status);
		connectBtn.setEnabled(false);
		if("Disconnected".equals(connectBtn)){
			
			inforTxt[STATUS].setForeground(Color.red);
			connectBtn.setEnabled(true);
			timer.stop();
			timeCount = 0;
		}
		
	}

	@Override
	public void showServerMessage(String message) throws RemoteException {
		if (!clientMessage.isActive())
			clientMessage.setVisible(true);
		clientMessage.messageArea.append(message);
	}

	@Override
	public void setTime(int status) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public client getClient() throws RemoteException {
		aclient = new client();
		aclient.setClientName(clientName);
		aclient.setIpAddress(ipAddress);
		return aclient;
	}
	
	public void connectClient() throws RemoteException{
		addClient = new AddClient();
		clientName = LANClient.clientName;
		System.out.println("name: "+clientName);
		ipAddress = LANClient.ipAddress;
		addClient.tf_nameClient.setText(clientName);
		addClient.tf_nameClient.setEditable(true);
		System.out.println("ip Address: "+ipAddress);
		
		addClient.btn_OK.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!"".equals(addClient.tf_host.getText()) 
						&& !"".equals(addClient.tf_nameClient.getText())){
					host = addClient.tf_host.getText();
					isconnected = true;
					
					try {
						server = (ServerInterface)Naming.lookup("rmi://"+host+":"+port+"/Remote" );
						inforTxt[IP_ADDRESS].setText(ipAddress);
						inforTxt[STATUS].setText("Connected");
						inforTxt[STATUS].setForeground(Color.green);
						inforTxt[NAME].setText(clientName);
						
						server.registerClient(thisClient,ipAddress);
						server.setInfoOnServer(clientName, ipAddress, "Online", "00:00:00");
						server.getClientName(clientName);
						server.addClientListener(thisClient, ipAddress);
						timer.start();
					} catch (MalformedURLException | RemoteException | NotBoundException e1) {
						inforTxt[STATUS].setText("Disconnected");
						inforTxt[STATUS].setForeground(Color.red);
						connectBtn.setEnabled(true);
						int key = JOptionPane.showConfirmDialog(null,
								"Do you want to enter a new server address",
								"No server is running...", JOptionPane.YES_NO_OPTION);
						if (key == JOptionPane.YES_OPTION) {
							host = JOptionPane.showInputDialog(null,
									"Enter server ipAddress", "Error",
									JOptionPane.OK_OPTION);
						} else
							System.exit(0);
					}
					System.out.println("host: "+host+" nameClient: "+clientName);
					System.out.println("ip from client: "+ ipAddress);
					
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
	@Override
	public boolean checkConnect() throws RemoteException {
		return isconnected;
	}
}
