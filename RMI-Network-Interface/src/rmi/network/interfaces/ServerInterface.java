
package rmi.network.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;


/**
 * @author Hoai Truong
 */
public interface ServerInterface extends Remote{

	void setInforClient(String name, String ip, String status, String time) throws RemoteException;
	void setInfoOnServer(String name, String ip, String status, String time)
			throws RemoteException;

	public void registerClient(ClientInterface clientInterface, String ip) throws RemoteException;
	String getClientName(String clientName) throws RemoteException;

	boolean isConnected(String ip) throws RemoteException;

	public void addClientListener(ClientInterface listener, String ip)
			throws java.rmi.RemoteException;

	public void removeClientListener(String ip) throws RemoteException;

	public void showClientMessage(String ipAddress, String message)
			throws RemoteException;
	
	public boolean checkConnectRequest() throws RemoteException;
}
