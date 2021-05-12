//ServerClient App
//Author: Maksim Zakharau, 256629 
//Data: January 2021;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JOptionPane;

public class ClientThread implements Runnable {
	private Socket socket;
	private String name;
	private PhonebookServer server;
	
	
	private ObjectOutputStream outputStream = null;
	
	ClientThread(String prototypeDisplayValue){
		name = prototypeDisplayValue;
	}
	
	ClientThread(PhonebookServer serv, Socket socket) { 
		server = serv;
	  	this.socket = socket;
	  	new Thread(this).start();  
	}
	
	public String getName(){ return name; }
	
	public String toString(){ return name; }
	
	public void sendMessage(String message){
		try {
			outputStream.writeObject(message);
			if (message.equals("exit")){
				socket.close();
				socket = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String[] splitter(String message) {
		String[] array = message.split(" ");
		return array;
		
	}
	

	
	public void run(){  
		String message;
	   	try( ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
	   		 ObjectInputStream input = new ObjectInputStream(socket.getInputStream()); )
	   	{
	   		outputStream = output;
	   		name = (String)input.readObject();
	   		boolean clientconnected = true;
			while(clientconnected){
				message = (String)input.readObject();
				String command = splitter(message)[0];
				switch (command.toUpperCase()) {
				case "GET": {
					try {
						String key = splitter(message)[1];
						String answer = server.pb.get(key);
						sendMessage("OK " + answer);
						break;
					} catch (Exception e) {
						sendMessage("Error");
						JOptionPane.showMessageDialog(server, e.getMessage(), "Error", 0);
					}

				}

				case "PUT": {
					try {
						String key = splitter(message)[1];
						String value = splitter(message)[2];
						server.pb.put(key, value);
						sendMessage("OK");
						break;

					} catch (Exception e) {
						sendMessage("Error");
						JOptionPane.showMessageDialog(server, e.getMessage(), "Error", 0);
					}

				}

				case "DELETE": {
					try {
						String key = splitter(message)[1];
						server.pb.delete(key);
						sendMessage("OK");
						break;

					} catch (Exception e) {
						sendMessage("Error");
						JOptionPane.showMessageDialog(server, e.getMessage(), "Error", 0);
					}

				}

				case "REPLACE": {
					try {
						String key = splitter(message)[1];
						String value = splitter(message)[2];
						server.pb.replace(key, value);
						sendMessage("OK");
						break;

					} catch (Exception e) {
						sendMessage("Error");
						JOptionPane.showMessageDialog(server, e.getMessage(), "Error", 0);
					}
					
				}
				
				case "LIST": {
					try {
						String answer = server.pb.getlist();
						sendMessage("OK " + answer);
						break;

					} catch (Exception e) {
						sendMessage("Error");
						JOptionPane.showMessageDialog(server, e.getMessage(), "Error", 0);
					}
				}
				
				case "CLOSE":{
					clientconnected = false;
					break;
				}
				default: 
					sendMessage("You need to input some command!");
				}
				PhonebookServer.printReceivedCommand(this,message);
			}
			socket.close();
			output.close();
			input.close();
			socket = null;
	   	} catch(Exception e) {
	   		JOptionPane.showMessageDialog(server, e.getMessage(), "Error", 0);
	   	}
	}
	
} 

