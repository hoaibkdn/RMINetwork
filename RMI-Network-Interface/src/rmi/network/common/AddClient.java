package rmi.network.common;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;



public class AddClient extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Container con;
	public JTextField tf_host, tf_nameClient;
	public JButton btn_OK;
	public JButton btn_CANCEL;
	public JPanel pnl_field, pnl_btn, pnl_border;
	public static String host_value, name_value;
	
	
	
	public AddClient() {
		super();
		
		con = this.getContentPane();
		this.setSize(300, 200);
		this.setTitle("Add Client");
		con.setLayout(new GridLayout(1, 1));
		
		pnl_border = new JPanel();
		pnl_border.setLayout(new GridLayout(2, 1));
		pnl_border.setBorder(new EmptyBorder(20, 20, 20, 20));
		
		pnl_field = new JPanel();
		pnl_field.setLayout(new GridLayout(3, 2));
		
		JLabel lbl_nameClient = new JLabel("Name");
		JLabel lbl_host = new JLabel("Host");
		
		tf_host = new JTextField();
		tf_nameClient = new JTextField();
		
		pnl_field.add(lbl_nameClient);
		pnl_field.add(tf_nameClient);
		pnl_field.add(lbl_host);
		pnl_field.add(tf_host);

		
		pnl_btn = new JPanel();
		pnl_btn.setBorder(new EmptyBorder(10, 10, 10, 10));
		pnl_btn.setLayout(new FlowLayout());
		
		btn_OK = new JButton("OK");
		btn_CANCEL = new JButton("CANCEL");
		pnl_btn.add(btn_OK);
		pnl_btn.add(btn_CANCEL);
		//btn_OK.addActionListener(this);
		//btn_CANCEL.addActionListener(this);
		
		pnl_border.add(pnl_field);
		pnl_border.add(pnl_btn);
		
		con.add(pnl_border);
		this.setVisible(true);
	}


//	@Override
//	public void actionPerformed(ActionEvent e) {
//		System.out.println("action");
//		String action_event = e.getActionCommand();
//		System.out.println(action_event);
//		switch (action_event) {
//		case "OK":
//			if(!"".equals(tf_host.getText()) && !"".equals(tf_port.getText()) 
//					&& !"".equals(tf_nameClient.getText())){
//				host_value = tf_host.getText();
//				setHost_value(host_value);
//				port_value = Integer.parseInt(tf_port.getText());
//				setPort_value(port_value);
//				name_value = tf_nameClient.getText();
//				setName_value(name_value);
//				//new InforComputer(name_value, host_value, port_value);
//				this.dispose();
//			}
//			else{
//				tf_nameClient.setText("Enter the name");
//				tf_host.setText("Enter the host name");
//				tf_port.setText("Enter the port");
//			}
//			break;
//
//		case "CANCEL":
//			System.exit(0);
//		}
//		
//	}


}

class InforComputer{
	private String host_value, name_value;
	private int port_value;
	
	public InforComputer( String name_value,String host_value, int port_value) {
		this.host_value = host_value;
		this.port_value = port_value;
		this.name_value = name_value;
	}
}
