package rmi.network.client;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;

/**
 * @author Hoai Truong
 */
public class LANClient {
	
	private static LANClient thisClient;
	private static ClientUI clientUI;
	public static String clientName, ipAddress;
	public static void main(String[] args) throws RemoteException{
		thisClient = new LANClient();
	}

	public LANClient() throws RemoteException{
		clientUI = new ClientUI();
		clientUI.initComponent();
		try {
			clientName = InetAddress.getLocalHost().getHostName();
			ipAddress = InetAddress.getLocalHost().getHostAddress();
			System.out.println("client name: "+ clientName);
		} catch (UnknownHostException e) {
			System.out.println("unknow name");
			e.printStackTrace();
		}
	}
}
