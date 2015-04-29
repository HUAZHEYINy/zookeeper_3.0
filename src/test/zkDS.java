package test;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * this class is creating a data structure for saving data.
 * */
public class zkDS implements Serializable{
	
		String leader;						//the string is the path of the node.
	
		Long nodeTimeStamp;
	Long currentTime;			//the current time.
	public String getLeader() {
		return leader;
	}
	public void setLeader(String leader) {
		this.leader = leader;
	}
	public Long getNodeTimeStamp() {
		return nodeTimeStamp;
	}
	public void setNodeTimeStamp(Long nodeTimeStamp) {
		this.nodeTimeStamp = nodeTimeStamp;
	}
	public Long getCurrentTime() {
		return currentTime;
	}
	public void setCurrentTime(Long currentTime) {
		this.currentTime = currentTime;
	}
	
	
}
