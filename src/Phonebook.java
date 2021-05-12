//ServerClient App
//Author: Maksim Zakharau, 256629 
//Data: January 2021;

import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

public class Phonebook {
 ConcurrentHashMap<String,String> pblist;
 
 public Phonebook() {
	pblist = new ConcurrentHashMap<String, String>();
 }

 public String get(String name) {
	 try {
	 String info =  pblist.get(name);
	 return info;
	 }
	 catch(Exception e){
		 return "Error, incorrect data";	 
	 }
	 
 }
 public void put(String name, String numb) {
	  pblist.put(name, numb);
 }
 
 public void replace(String name, String newnumb) {
	 pblist.replace(name, pblist.get(name), newnumb);
 }
 
 public void delete(String name) {
	 pblist.remove(name);
 }
 

 public String getlist() {
	String info = ""; 
	Enumeration <String> keys = pblist.keys();
	while (keys.hasMoreElements()) {
		info+=keys.nextElement();
		info+=";";
	}
	return info;
 }
}
