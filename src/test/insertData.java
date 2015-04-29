package test;

import java.io.IOException;

import org.apache.zookeeper.KeeperException;

/**
 * this method we set data through serialization for node. 
 * the data structure is zkDS. in the zkDS.java
 * 
 * */

public class insertData extends operation{
	
	 
	public static void main(String args[]){
		byte [] data ;
		data = new byte[1];
		data [0] = 0;
		try {
			insertData i = new insertData();
			i.connect("localhost");
			zkDS zNew = new zkDS();
			//zNew = Serializer.deserialize(zk.getData("/animal/server2_G/server2", false, null));
			zNew.setNodeTimeStamp(System.currentTimeMillis());
			
			zk.setData("/animal/server2_G/server2", Serializer.serialize(zNew), -1);
		
			//zk.setData("/animal/server2_G/server2", data, -1);
						System.out.println(zNew.getNodeTimeStamp());
			
		} catch (KeeperException | InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
