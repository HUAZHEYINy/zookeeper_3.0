package test;

import java.io.IOException;

import org.apache.zookeeper.KeeperException;


/**
 * this main.java goal is to create the root node. /animal
 * and run five runner.
 * for run the runner, you should change the 
 * ProcessBuilder parameter
 * new ProcessBuilder
 * 			("java","-jar",
 * 				"/your/path/of/Runner.jar","animal","server1_G","server1")
 * the last two parameter is another two node under the root node.
 * for example.after run process, we should have this hierarchy in our zookeeper server
 *"/animal/server1_G/server1"
 *note: the server1_G is a persistent node. server1 is ephemeral node.
 * * */
public class main extends operation{
	public static void main(String args[]){
	
		main t = new main();
				try {
					
					//connect to the zookeeper server we setup on localhost.
					t.connect("localhost");
										
				} catch ( InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(t.checkExisting("/animal")){
				try {
					t.create("animal");
				} catch ( InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (KeeperException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
		//run the outside executable jar file.	
		ProcessBuilder proc = new ProcessBuilder("java","-jar","/Users/huazhe/desktop/Runner_server/Runner.jar","animal","server1_G","server1");
		ProcessBuilder proc1 = new ProcessBuilder("java","-jar","/Users/huazhe/desktop/Runner_server/Runner.jar","animal","server2_G","server2");
		ProcessBuilder proc2 = new ProcessBuilder("java","-jar","/Users/huazhe/desktop/Runner_server/Runner.jar","animal","server3_G","server3");
		ProcessBuilder proc3 = new ProcessBuilder("java","-jar","/Users/huazhe/desktop/Runner_server/Runner.jar","animal","server4_G","server4");
		ProcessBuilder proc4 = new ProcessBuilder("java","-jar","/Users/huazhe/desktop/Runner_server/Runner.jar","animal","server5_G","server5");

			try {
				Process run = proc.start();
				Process run1 = proc1.start();
				Process run2 = proc2.start();
				Process run3 = proc3.start();
				Process run4 = proc4.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Process Main completed!");
			
	}
	
}
