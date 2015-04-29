package test;

import java.io.IOException;

import org.apache.zookeeper.KeeperException;

public class Rmonitor extends operation{
	public static void main(String args[]){
		
		final Rmonitor m = new Rmonitor();
		try {
			m.connect("localhost");
			
	
			do {
			m.list_all("animal");
	
			Thread.sleep(1000);
			System.out.println("---------------------------------------------");
			}while(true);
			
		} catch (IOException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			System.out.println("here is the recursive path "+e.getPath());
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
