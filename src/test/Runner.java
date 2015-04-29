package test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.KeeperException;

public class Runner extends operation{
	
	public static String groupName = "animal";
	public static String firstChild = "server1_G";
	public static String secondChild = "server1";
	
	/**
	 * register into the node and run
	 * after random time exit.
	 * */
	public static void main(String args[]){
		
		Runner s = new Runner();
		try {
			s.connect("localhost");
			
		
			z.setLeader("/"+args[0]+"/"+args[1]+"/"+args[2]);
			z.setNodeTimeStamp(System.currentTimeMillis());
			System.out.println(args[2]+" time is : "+z.getNodeTimeStamp());
			
			List<String> children = new ArrayList<String>();
			children = s.Returnlist(args[0]);

			if(children == null){
				System.out.println(" i am null");
				s.join_Persistent(args[0], args[1]);
			}
			else{
			if(!children.contains(args[1]))
				{
				s.join_Persistent(args[0], args[1]);
				}
			}
			System.out.println(children);
			s.join_Ephemeral(args[0]+"/"+args[1],args[2]);

			Thread.sleep(12000);
			} catch (IOException e) {
			
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (KeeperException e) {
			
			e.printStackTrace();
		}
		
		// store the timestamp etc information into zookeeper.
		try {
			if(args[2] != null ){
				byte[] b = args[2].getBytes();
				zk.setData("/"+args[0]+"/"+args[1]+"/"+args[2], b, -1);
			}
			else{
			zk.setData("/"+args[0]+"/"+args[1]+"/"+args[2], Serializer.serialize(z), -1);
			zk.setData("/"+args[0]+"/"+args[1]+"/"+args[2], Serializer.serialize(z), -1);
			}
			
		} catch (KeeperException | InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
