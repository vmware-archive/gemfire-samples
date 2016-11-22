##Walk Through
###Setup and Start a Local Cluster
1. Be sure you have a GemFire installation, a Java JDK (not JRE) and a working Maven installation
2. Export the GEMFIRE_HOME and the JAVA_HOME environment variables to point to GemFire and the JDK
respectively.  $GEMFIRE_HOME/bin/gfsh should exist and $JAVA_HOME/bin/java should exist.
3. You will also need python 2.6x.  Install the 'netifaces' and 'faker' packages
4. Review the cluster definition file: gemfire-manager/cluster.json.  By default the gemfire
cluster will store its files in /tmp/gemfire.  Change the cluster-home setting if you wish to
put the files somewhere else.
5. Start the cluster:

   ```bash
   python gemfire-manager/cluster.py start
   ```

6. Verify the cluster is up using the list members command.  You should see one locator and three servers.
   ```bash
   (.venv) MacBook-Pro-2:gemfire_samples rmay$ python gfsh.py list members
   
   (1) Executing - connect --locator="localhost[10000]"
   
   Connecting to Locator at [host=localhost, port=10000] ..
   Connecting to Manager at [host=192.168.1.109, port=11099] ..
   Successfully connected to: [host=192.168.1.109, port=11099]
      
   (2) Executing - list members
   
   Member Count : 4
   Coordinator  : locator (localhost(locator:9376:locator)<v0>:2112)
   
    Name   | Id
   ------- | ----------------------------------------
   locator | localhost(locator:9376:locator)<v0>:2112
   server1 | 192.168.1.109(server1:9411)<v1>:5886
   server2 | 192.168.1.109(server2:9412)<v1>:51927
   server3 | 192.168.1.109(server3:9410)<v1>:58115
   ```
   
### Test the Loader
The loader is a python program that uses the REST API to load data in JSON format.
1. Review load-events.py.  You can set the count and sleep time to whatever values you desire.
2. Run the loader.

   ```bash
   python load-events.py
   ```

### Build and Run the CQ Client
1. Build the CQ Client
   ```bash
   cd queryevents
   mvn clean package
   ```
   
2. Review the configuration file: queryevents/src/main/resources/context.xml (logging config. is log4j2.xml)

3. Review the event handler: queryevents/src/main/java/io/pivotal/pde/sample/SampleCqEventListener.java

4. Start the event listener

   ```bash
   python durablecq.py
   ```

##Durable CQ Tests
1. Nominal case: start the listener, add some data
   
   Events are received
   
2. Start listener, add data, close listener but keep adding data, restart listener
   
   Events generated while listener is down are received when listener starts
   
3. Start listener, add data, stop primary server while continuing to load data

   Events continue to be delivered
   
4. Start listener, add data, stop listener, stop primary server while loading data, restart listener

   All events are delivered when the listener restarts.  Subscription queue failover happens even when the listener is not connected.
   
5. Start listener, add events, stop listener while events continue to queue, stop the whole cluster, start the cluster, start the listener

   __Enqueue events are not preserved across cluster restart__
   
6. Start listener, add events, stop all servers (not locators), start the cluster, start the loader

   Listener reconnects and Cq is re-established, event delivery resumes.  _Events in the queue at the time of
   cluster shutdown are lost_.
   
7. Start listener, add events, stop the whole cluster (including locators), start the cluster, start the loader

   Listener reconnects and Cq is re-established, event delivery resumes.  _Events in the queue at the time of
   cluster shutdown are lost._
   
8. Start listener, add data, kill listener (as opposed to friendly shutdown)  but keep adding data, restart listener

   Events generated while listener is down are received when listener starts
   



