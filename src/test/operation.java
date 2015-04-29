package test;

import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;

public class operation extends ConnectionWatcher implements Watcher{
	
	/**
	 * create a parent path.
	 * @throws InterruptedException 
	 * @throws KeeperException 
	 * */
	public void create(String groupName) throws KeeperException, InterruptedException{
		String path= "/" + groupName;
		
		String createdPath = zk.create(path, null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		System.out.println("Created " + createdPath);
	}
	/**
	 * check whether existing
	 * 
	 * */
	public boolean checkExisting(String groupName){
		try {
			if(zk.exists(groupName, false) != null)
			return true;
			else
				return false;
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * join child into parent.
	 * @throws InterruptedException 
	 * @throws KeeperException 
	 * */
	public void join_Ephemeral(String groupName,String childName) throws KeeperException, InterruptedException{
		String grouppath = "/" + groupName;
		String childpath = "/" + childName;
		String path = grouppath+childpath;
		
		String createdpath = zk.create(path, null, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		System.out.println("Created " + createdpath);
	}	
	/**
	 * join child into parent.
	 * @throws InterruptedException 
	 * @throws KeeperException 
	 * */
	public void join_Persistent(String groupName,String childName) throws KeeperException, InterruptedException{
		
		String grouppath = "/" + groupName;
		String childpath = "/" + childName;
		String path = grouppath+childpath;
		System.out.println(path);

		String createdpath = zk.create(path, null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		System.out.println("Created " + createdpath);
	}	
	/**
	 * delete operation.
	 * @throws KeeperException 
	 * @throws InterruptedException 
	 * */
	
	public void delete(String groupName,String childName) throws InterruptedException, KeeperException{
		String grouppath = "/" + groupName;
		String childpath = "/" + childName;
		
		String path = grouppath + childpath;
		
		zk.delete(path, -1);
		
		System.out.println(path + " was deleted!");
 	}
	
	/**
	 * delete operation. delete with the given path
	 * @throws KeeperException 
	 * @throws InterruptedException 
	 * */
	
	public void deleteAll(String groupName) throws InterruptedException, KeeperException{
		String grouppath = "/" + groupName;
		
		String path = grouppath ;
		
		zk.delete(path, -1);
		
		System.out.println(path + " was deleted!");
 	}
	
	/**
	 * list the children of current node.
	 * @throws InterruptedException 
	 * @throws KeeperException 
	 * */
	
	public void list(String groupName) throws KeeperException, InterruptedException{

		String grouppath = "/" + groupName;
		
		try{
		List<String> children = zk.getChildren(grouppath, true);
		
		if(children.isEmpty())
		{
			System.out.println(" No child in group "+ groupName);
		}
		else{
			System.out.println("Children are in " + groupName);
			
		for(String child:children){
			System.out.println(child);
		}
		}
		}catch(KeeperException.NoNodeException e){
			System.out.println("Group " + groupName +"does not exist!");
		}
	}
	
	/**
	 * return the children on the current path
	 * */
	public List<String> Returnlist(String groupName) throws KeeperException, InterruptedException{

		String grouppath = "/" + groupName;
		
		try{
		List<String> children = zk.getChildren(grouppath, true);
		
		if(children.isEmpty())
		{
			System.out.println(" No child in group "+ groupName);
		
		}
		else{
			System.out.println("Return completely!");
			return children;
		}
		}catch(KeeperException.NoNodeException e){
			System.out.println("Group " + groupName +" does not exist!");
		}
		return null;
	}
	
	
	
	/**
	 * input: a node
	 * output : all children of the node
	 * list operation.
	 * @throws InterruptedException 
	 * @throws KeeperException 
	 * */
	
	public void list_all(String groupName) throws KeeperException,
			InterruptedException {
		String grouppath = "/" + groupName;

		try {
			List<String> children = zk.getChildren(grouppath, false);

			for (int i = 0; i < children.size(); i++) {

				System.out.print("/" + children.get(i));

				String path = grouppath + "/" + children.get(i);

				List<String> subChildren = zk.getChildren(path, true);
				if (subChildren.isEmpty()) {
					
					System.out.println(" No child in group " + children.get(i));
				} else {
					System.out.println("/" + subChildren.get(0));
				}

			}
		} catch (KeeperException.NoNodeException e) {
			System.out.println("Group " + groupName + " does not exist!");
		}
	}
	

	
}
