//ServerClient App
//Author: Maksim Zakharau, 256629 
//Data: January 2021;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;


class PhonebookClient extends JFrame implements ActionListener, Runnable{
	
	private static final long serialVersionUID = 1L;
	
	public static void main(String[] args) {
		String name;
		String host;
		
		host = JOptionPane.showInputDialog("Server adress");
		name = JOptionPane.showInputDialog("Client name");
		if (name != null && !name.equals("")) {
			new PhonebookClient(name, host);
		}
	}
	
		
		
	private JTextField commandField = new JTextField(20);
	private JTextArea  textArea     = new JTextArea(15,20);
	
	static final int SERVER_PORT = 1337;
	private String name;
	private String serverHost;
	private Socket socket;
	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;
	
	PhonebookClient(String name, String host) {
		super(name);
		this.name = name;
		this.serverHost = host;
		setSize(300, 400);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				try {
					outputStream.close();
					inputStream.close();
					socket.close();
				} catch (IOException e) {
					System.out.println(e);
				}
			}
			@Override
			public void windowClosed(WindowEvent event) {
				windowClosing(event);
			}
		});
		JPanel panel = new JPanel();
		JLabel commandLabel = new JLabel("Command:");
		
		JLabel textAreaLabel = new JLabel("			Dialogue:");
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		panel.add(commandLabel);

		panel.add(commandField);
	
		commandField.addActionListener(this);
		
		panel.add(textAreaLabel);
		JScrollPane scroll_bars = new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		panel.add(scroll_bars);
		setContentPane(panel);
		setVisible(true);
		new Thread(this).start(); 
	}
	
	synchronized public void printReceivedMessage(String message){
		String text = textArea.getText();
 		textArea.setText(text + "Server:" + message + "\n");
	}
	

	public void actionPerformed(ActionEvent event){ 
		String message;
	  Object source = event.getSource();
	  if (source==commandField)
	  {
		try{ 
	  	message = commandField.getText();
	  	outputStream.writeObject(message);
  		 if (message.equals("exit")){
  		 		inputStream.close();
  		 		outputStream.close();
  		 		socket.close();
	  		 	setVisible(false);
	  		 	dispose();
	  		 	return;
	  		 }
	  	}catch(IOException e)
	  	{ System.out.println("Client exception "+e);
	  		}
	  }
	  repaint();
	}
	
	public void run(){
		if (serverHost.equals("")) {
			serverHost = "localhost";
		}
		try{
	  		socket = new Socket(serverHost, SERVER_PORT);
	  		inputStream = new ObjectInputStream(socket.getInputStream());
	  		outputStream = new ObjectOutputStream(socket.getOutputStream());
	  		outputStream.writeObject(name);
	  	} catch(IOException e){ 
		   	JOptionPane.showMessageDialog(null, "Cant initialize correct connection for server");
		   	setVisible(false);
		   	dispose();  
		    return;
		 }
		 try{
		 	while(true){
		 		String message = (String)inputStream.readObject();
		 		printReceivedMessage(message);
		 		if(message.equals("exit")){
		 			inputStream.close();
	  		 		outputStream.close();
	  		 		socket.close();
	  		 		setVisible(false);
	  		 		dispose();
	  		 		break;
		 		}
		 	}
		 } catch(Exception e){
		   	JOptionPane.showMessageDialog(null, "Client connection interrupted");
		   	setVisible(false);
		   	dispose();
		 }	
	}
	
} 