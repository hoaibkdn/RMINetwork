package rmi.network.server;

import java.util.Hashtable;

import rmi.network.interfaces.ClientInterface;
import rmi.network.interfaces.ServerInterface;

public class ThreadConnection extends Thread {
	ServerInterface serverInterface;
	ClientInterface clientInterface;
	int numOfClient;
	private Hashtable<ClientInterface, ThreadConnection> allConnections = 
			new Hashtable<ClientInterface, ThreadConnection>();
	
	public ThreadConnection() {
	}
	
	public void connectSyn(){
		
	}
	
	
}
