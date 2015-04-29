package test;

//cc ConnectionWatcher A helper class that waits for the connection to ZooKeeper to be established

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;

// vv ConnectionWatcher
/*
 * the getchildren() only trigger watch once the children changed. 
 * */
public class ConnectionWatcher implements Watcher {
  
  private static final int SESSION_TIMEOUT = 5000;
  protected static ZooKeeper zk;
  private CountDownLatch connectedSignal = new CountDownLatch(1);
  protected String path[];
  static int i = 0;
	
	static zkDS z ;  
	


  public void connect(String hosts) throws IOException, InterruptedException {
		
	    zk = new ZooKeeper(hosts, SESSION_TIMEOUT, this);
	    z = new zkDS();
	   
	    connectedSignal.await();
	  }

  
  public void process(WatchedEvent event) { 
	  
    
    System.out.println("Something happened in the path : "+event.getPath());

    if (event.getState() == KeeperState.SyncConnected) {
        connectedSignal.countDown();
      }
    
    
    	if(event.getPath() != null){
    	String path[] = event.getPath().split("/");
    	/**
    		for example: something happened in /animal/server3_G/server3
    		so path[0-2] are: null animal server3_G 
    		why do we need to do this?
    		because: when a node was deleted, we can find which one was deleted recursively.
    			we use the former part of server3_G as the mark of the node.
    			and we can split the server3_G to find the exact node that was deleted before.
    	*/
    	String [] childPath = path[2].split("_");
    	//split server3_G to server3 and G
    	
    	String finalPath = event.getPath()+"/"+childPath[0];
    	
    	
   		try {
			this.recreateServer(path[1], path[2], childPath[0]);
			
			//this.createFresh(event.getPath());
		} catch (IOException | InterruptedException | KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
//   		catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    	}
    	
      

} 

  public void close() throws InterruptedException {
    zk.close();
  }
  /**
 * @throws InterruptedException 
 * @throws KeeperException 
 * @throws IOException 
 * @throws ClassNotFoundException 
   * 
   * this method determine which one has the most fresh under one persistent node. 
   * hierarchy:
   * 			persistent node
   * 				|
   * 			---------
   * 			|	|	|
   * 			A	C	B
   * A.B.C are all ephemeral node and have exactly same data except Runner name
   * when A died, we can use this method to 
   * recreate another node with the most fresh data. 
   * 
   * */
  
  public void createFresh(String eventPath) throws ClassNotFoundException, IOException, KeeperException, InterruptedException{
	  System.out.println("The data in the Runner: "+
				Serializer.deserialize(zk.getData(eventPath, false, null)).getLeader());			
		
		List<String> child = zk.getChildren("/animal", false);
		System.out.println(child);
		List<String> childY = new ArrayList<String>();
		
		//prepare for comparing which one is the most fresh.
		
		for(int i = 0;i < child.size(); i++){
		
			if(zk.getChildren("/animal/"+child.get(i), false).isEmpty())
			{
				//there is no child in the persistent 
				System.out.println("/animal/"+child.get(i)+" is empty!");

			}
			
			else{
			//store all the path of child for the group that is not empty.
			childY.add("/animal/"+child.get(i));
			}					
					
		}
		
		//print the stamp time of alive runner 
		for(int i = 0 ; i < childY.size();i++){
			System.out.println(Serializer.deserialize(zk.getData(childY.get(i), false, null)).getLeader()+"      "+
					Serializer.deserialize(zk.getData(childY.get(i), false, null)).getNodeTimeStamp());
		}
		
		//find path of  the most fresh
		String mostFreshPath = "initilization";	//store the path of  the most fresh 
		Long tempTime = (long) 0;		//temporarily store the time.
		int tempI;		//temporarily store the index of childY
		for(int i = 0; i < childY.size();i++){
			if(tempTime  < Serializer.deserialize(zk.getData(childY.get(i), false, null)).getNodeTimeStamp()){
				tempTime = Serializer.deserialize(zk.getData(childY.get(i), false, null)).getNodeTimeStamp();
				tempI = i;
				mostFreshPath = Serializer.deserialize(zk.getData(childY.get(i), false, null)).getLeader();
			
			}
		}
		
		System.out.printf("+++++++++++++++++\n Recreating the child %s with %s data \n+++++++++++++++++\n",eventPath,mostFreshPath);
		
		recreateFreshServer(mostFreshPath, eventPath);

	  
  }
  /**
	 * recreate the server that was deleted.
	 * 
	 * parameter: 1. dataServerPath : the most fresh server, we need to use its data.
	 * parameter: 2. createPath : the path we need to create the server.
	 * 
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @throws KeeperException 
	 * */
  
  public static void recreateFreshServer(String dataServerPath,String createPath) throws KeeperException, InterruptedException{
	  try {
		  
		  	  
		  
		  
		  String path [] = createPath.split("/");
		  //path: animal serverX_G
		  String Subpath [] = path[2].split("_");
		  
		  
		  System.out.printf("%s %s %s is creating \n",path[1],path[2],Subpath[0]);
		  
		  
		  ProcessBuilder proc = new ProcessBuilder("java","-jar",
				  "/Users/huazhe/Desktop/Runner_server/Runner.jar",path[1],path[2],Subpath[0]);
		  proc.start();
		  
		  
		  while(true){
			  // run until the creating node created!
			 if( zk.exists("/"+path[1]+"/"+path[2]+"/"+Subpath[0], false) != null){
				 
				 System.out.printf("******* %s ******* \n","I am setting the data into the node!");
				 zk.setData(createPath+"/"+Subpath[0], zk.getData(dataServerPath, false, null), -1);
				 break;
			 }
			  
		  }
		  
		
		
		 
		
		
		
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
  /**
	 * recreate the server that was deleted.
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @throws KeeperException 
	 * */
	public static void recreateServer(String groupName,String firstChild,String secondChild) throws IOException, InterruptedException, KeeperException{
		
		System.out.println("Server "+secondChild+" is recreating!");
		ProcessBuilder proc = new ProcessBuilder("java","-jar","/Users/huazhe/desktop/Runner_server/Runner.jar",groupName,firstChild,secondChild);
		try {
			Process run = proc.start();
			i++;

		
		} catch (IOException e) {
			e.printStackTrace();
		} 

		
	}
  


}
// ^^ ConnectionWatcher

