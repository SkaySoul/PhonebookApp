//ServerClient App
//Author: Maksim Zakharau, 256629 
//Data: January 2021;


class MainTestApplication {
	
	public static void main(String [] args){
		new PhonebookServer();
	  	new PhonebookClient("User1","localhost");
		new PhonebookClient("User2","localhost");
	}
	
}
