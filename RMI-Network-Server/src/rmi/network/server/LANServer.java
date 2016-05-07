package rmi.network.server;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class LANServer{

	private ServerClientUI serverClientUI;

	String host, nameClient;
	static int port = 1990;
	JFrame frame;
	public LANServer() throws RemoteException {
		super();
		serverClientUI = new ServerClientUI();
		serverClientUI.initComponentServer();
	}
	
	public static void main(String[] args) throws RemoteException {
		ServerClientUI server;
		//LANServer server;
		try{
			//server = new ServerClientUI();
			server = new ServerClientUI();
			server.initComponentServer();
			LocateRegistry.createRegistry(port);
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
	
}
