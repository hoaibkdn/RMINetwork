/**
 * 
 */
package rmi.network.interfaces;
import java.rmi.Remote;
import java.rmi.RemoteException;

import javax.swing.ImageIcon;

import rmi.network.bean.client;
/**
 * @author Hoai Truong
 */
public interface ClientInterface extends Remote {

	public static enum Task {
		SHUTDOWN, RESTART, LOG_OFF, LOCK
	};

	void doClientTasks(Task task) throws RemoteException;

	void setClientInfo(String status) throws RemoteException;

	String showServerMessage(String message) throws RemoteException;

	void setTime(int status) throws RemoteException;

	ImageIcon sendImages() throws RemoteException;
	
	public client getClient() throws RemoteException;
}
