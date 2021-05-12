//ServerClient App
//Author: Maksim Zakharau, 256629 
//Data: January 2021;


import java.io.IOException;


import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import javax.swing.ScrollPaneConstants;

class PhonebookServer extends JFrame implements Runnable{
	
	private static final long serialVersionUID = 1L;
	final int SERVER_PORT = 1337; 
	public static void main(String [] args){
		new PhonebookServer();
	}
	
	protected Phonebook pb;
	private JLabel textAreaLabel = new JLabel("DIALOGUE");
	private static JTextArea  pbArea  = new JTextArea(15,20);
	private JScrollPane scroll = new JScrollPane(pbArea,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	
	
	
	PhonebookServer() {
		super("SERWER");
	  	setSize(300,340);
	  	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	  	JPanel panel = new JPanel();
	  	pbArea.setLineWrap(true);
	  	pbArea.setWrapStyleWord(true);
	  	panel.add(textAreaLabel);
	  	pbArea.setEditable(false);
	  	panel.add(scroll);
	  	setContentPane(panel);
	  	setVisible(true);
	  	new Thread(this).start();
		pb = new Phonebook();
	}
	
	
	synchronized public static void printReceivedCommand(ClientThread client, String message){
		String text = pbArea.getText();
		String[] newmessage = ClientThread.splitter(message);
		pbArea.setText(client.getName() + ": " + newmessage[0] + "\n" + text);
	}
	
@Override	
public void run() {
	boolean socket_created = false;
	
	
	try (ServerSocket server = new ServerSocket(SERVER_PORT)) {
		String host = InetAddress.getLocalHost().getHostName();
		System.out.println("Host: " + host);
		socket_created = true;
		

		while (true) {  
			Socket socket = server.accept();
			if (socket != null) {
				new ClientThread(this, socket);
			}
		}
	} catch (IOException e) {
		System.out.println(e);
		if (!socket_created) {
			JOptionPane.showMessageDialog(null, "Cannot create server socket");
			System.exit(0);
		} else {
			JOptionPane.showMessageDialog(null, "SERVER ERROR: Cannot link the client");
		}
	}
}


	
}

