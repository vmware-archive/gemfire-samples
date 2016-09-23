#Walk Through#

_Start the Local Cluster_

See the instructions in the "gemfire-manager" folder.
* check out pulse at http://localhost:17070/pulse (admin/admin)
* check out the REST API at http://localhost:10180/gemfire-api/docs

_Set Up the Event Region_
   
```$GEMFIRE/bin/gfsh -e "connect --locator=localhost[10000]" -e "create region --name=event --type=PARTITION_REDUNDANT"```

as a convenience, "gfsh.py" will call the gemfire script and connect automatically:

`python gfsh.py create region --name=event --type=PARTITION_REDUNDANT`

   
Note that this definition will persist across cluster restarts.

To destroy the region: `python gfsh.py destroy region --name=event`

_Load Some Event Data_

The loader uses the rest API to load some fake event data
* install the python "Faker" package: `pip install Faker`
* run the loader: `python event_loader.py`
* note that you can now query the data using Pulse.  Try, for example, `select * from /event where state='GA'`

_Build the Server Side Jar_
```
cd queryeventsserver
mvn package
```

_Deploy the Jar to the GemFire Cluster_

```
python gfsh.py deploy --jar=queryeventsserver/target/queryevents-1.0-SNAPSHOT.jar 
```

_Build the Query Client_

```
cd queryevents
mvn package
```

_Run the Query Client_

The query client program takes 2 arguments, the event type and the state.  The Total
of the counts of all matching entries is returned.  An example invocation is below.

```
(.venv) MacBook-Pro-2:gemfire_samples rmay$ python query.py A FL
1879
```





   
