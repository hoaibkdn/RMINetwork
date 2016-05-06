package rmi.network.server;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

public class ServerMessage extends JFrame{
	
	private static final long serialVersionUID = 1L;
	public JTextArea messageArea;
	public JTextField txtTyping;
	public JScrollPane scroll;
	public JButton btnSend;
	public JLabel isTyping;

	public ServerMessage(String str) {

		super(str);
		this.setSize(480, 400);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setLayout(null);
		this.addWindowFocusListener(new WindowFocusListener() {

			@Override
			public void windowLostFocus(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowGainedFocus(WindowEvent e) {
				txtTyping.requestFocus();
			}
		});

		isTyping = new JLabel("");
		isTyping.setBounds(20, 10, 250, 25);
		this.add(isTyping);

		this.messageArea = new JTextArea("");
		this.messageArea.setBounds(20, 50, 430, 250);
		this.messageArea.setWrapStyleWord(true);
		this.messageArea.setLineWrap(true);
		this.messageArea.setEditable(false);

		DefaultCaret caret = (DefaultCaret) messageArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		this.scroll = new JScrollPane(messageArea,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.scroll.setVisible(true);
		this.scroll.setBounds(20, 50, 430, 250);

		this.add(scroll);

		JLabel lsd = new JLabel("Send");
		lsd.setBounds(20, 325, 50, 25);
		this.add(lsd);

		this.txtTyping = new JTextField("");
		this.txtTyping.setBounds(70, 325, 290, 25);
		this.add(txtTyping);

		btnSend = new JButton("Send");
		btnSend.setBounds(370, 325, 80, 25);
		this.add(btnSend);

		setResizable(false);
	}
	public static void main(String[] args) {
		new ServerMessage("Server Message");
	}
}
