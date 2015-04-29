# zookeeper_3.0  **** Please feel free let me know, if there are any errors or discovers from you. ***
utilize zookeeper to survive died server

Start Zookeeper 
1.Download : Go to the following link for downloading. 
	https://zookeeper.apache.org  (Download the stable one, this report is present by zookeeper.3.4.6)

2.After downloaded, we should configure it. 
	Open:  /zookeeper3.4.6/conf/zoo.cfg 

For testing we only run it in our local machine. Copy and paste the following content into this file. (you also can find the tutorial in https://zookeeper.apache.org/doc/trunk/zookeeperStarted.html

	tickTime=2000
	dataDir=/your path/where/you/want/to/store/data/in-memory/databse
	clientPort=2181

3.Start Zookeeper server.
In the Command Line(CMD), run the following command to start the zookeeper server in your own machine. 
/zookeeper3.4.6/bin/zkServer.sh start
4.The maven dependency for Eclipse:

	org.apache.zookeeper
	zookeeper
	3.3.2

Run the program
        1.Export the Runner.java As executable jar file. 
        2.In the main.java, change the some parameter as the comments in the main.java. 
        3.First function. And then run the main.java and then Run the Rmonitor.java
In the console window, you can monitor what node exist zookeeper. 
And after certain of time, the Runner program automatically die, and then the program recreate the Runner what was deleted/died.  

	
5.For another function, you can create three or more  ephemeral nodes but different name under one persistent node, another than in the insertData.java, change the parameter of setData() to your own path to set data for the ephemeral.  Run the main.java(for create three or more nodes under one persistent node, you also should change some parameter in main.java) and then Run the Rmonitor.java. after monitoring, you should use insertData.java to handle the data. (Figure 2.)

6.What is the result from step 4?
After the one of the node died, the program will select the most fresh data from the rest of the node under the persistent and recreate one with this fresh data.
Discover More 
	In the above, we are all talking about running and handling zookeeper on one machine, what about communicate between different machine?
	After some research, I figure it out how communicate between server and client. The Client and server communicate over TCP. This requires that the client simply know the server’s host and port. In general, your zookeeper servers bind to some private network interface. For instance, in our zoo.conf configuration file might contain a line like the following:

	clientPort=2181
	server.1=123.456.789.1:2888:3888

The first portion of the server.1 section 123.456.789.1 is the host to which the ZooKeeper server will bind. As long as this host is not the loop back interface (i.e. localhost or 127.0.0.1) you should be able to connect to that host from another machine on the client port 2181. So, for instance, in Java I create a new ZkClient that points to that host and port:
ZkClient client = new ZkClient("123.456.789.1:2181");
Basically, we can connect to the zookeeper in another computer that can be implement by the above code. And then implement the function what you want. 
The Maven dependency : 
	com.101tec
	zkclient
	0.4
javadoc : http://javadox.com/com.101tec/zkclient/0.4/org/I0Itec/zkclient/ZkClient.html

