package rmi.network.server;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import com.sun.corba.se.spi.activation.Server;

import rmi.network.interfaces.ClientInterface;

public class LANServer extends Thread{


	String host, nameClient;
	ClientInterface clientInterface;
	private static ServerClientUI server;

	int port = 1990;
	public LANServer() throws RemoteException {
		super();
		server = new ServerClientUI();
		server.initComponentServer();
		start();
		while(true){
			if(server.checkConnectRequest())
				start();
		}
	}
	
	public static void main(String[] args) throws RemoteException {
		LANServer lanServer = new LANServer();
		
	}

	@Override
	public void run() {
		try {
			LocateRegistry.createRegistry(port);
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
		try {
			Naming.bind("rmi://localhost:"+port+"/Remote", server);
		} catch (MalformedURLException | AlreadyBoundException | RemoteException e) {
			e.printStackTrace();
		}
	}

}
