#Overview#

This project uses python to automate running a local gemfire cluster.

#Requirements#

Each managed host must meet the following prerequisites:
* A JDK
* A GemFire Installation
* Python 2.6+
* the "netifaces" python package.  This usually can be installed as follows:
`pip install netifaces`.  


#Local Cluster Walk Through#

1. Define the GEMFIRE and JAVA_HOME environment variables to point to your
gemfire installation and an appropriate jdk.
2. Examine the cluster configuration file: _cluster.json_.
3. If you wish to change the location used by the cluster for local storage,
you may do so by modifying the _cluster-home_ setting in _cluster.json_.
4. Start the cluster:
```
    python cluster.py start
```
5. Verify the cluster is running by accessing pulse at: http://localhost:17070/pulse
6. Note: the locator is listening on port 10000, the jmx-manager is on port 11099
7. Check the status of a member:
```
    python cluster.py status server1
```
Note that the member name is determined by its key in the _processes_ dictionary
under each host.
8. stop and start a member
```
    python cluster.py stop server2
    python cluster.py start server2
```
9. stop the whole cluster
```
    python cluster.py stop
```
Note that this does not stop locators.
10. stop the locator
```
    python cluster.py stop locator
```
    
    
# The Cluster Configuration File#

The cluster config file is hierarchical in
nature.  This is so that settings that are common to all members can be shared
and do not need to be repeated.

Members look up their setting starting with the most specific portion of the
hierarchy and proceeding to the most general. The lookup algorithm is detailed
below.


1.  __host and process specific__ (see for example the _server-port_ settings in
    the sample below)
2. if there are no host and process specific settings, check __host__ settings
    ( the sample has no setting at the host level but they would be found inside of
    _hosts["localhost"]["host_properties"]_ )
3. if there are no host specific settings, check the __global settings for the
    process type__. (In the example, _conserve_sockets_ is set at this level).
    There is one section that applies to all data nodes (_datanode-properties_),
    and one that applies to locators (_locator-properties_).
4. Lastly, look in _global-properties_

#### Additional notes about the cluster configuration file ####
* place holders of the form ${ENV_VAR} can be used to pull in values from the
environment
* the __hosts__ section is a dictionary of each host in the cluster.  The key of
each dictionary entry must match the host name of the host as reported by the
_hostname_ command. _localhost_ is a special key that matches every host and
is useful for setting up local clusters.
* the _processes_ section within each host is a dictionary of processes that should
run on that host.  The key to the dictionary entry is used as the gemfire process
name (i.e. it is passed to gfsh with the --name option). _Each process must have
a name that is unique in the whole cluster_, not just on the host.
* All of the settings you can place in  _gemfire.properties_ are understood by
the scripts and can be used in the cluster configuration file.  There is special
logic built in to the scripts to understand options that can only be passed as
arguments to gfsh.  For example: _server-bind-address_ and _classpath_. The scripts
figure out which settings need to be passed as _--J=-Dgemfire.setting_ and which
are passed directly to gfsh as _--setting_.
* Place JVM gc options, arbitrary -Ds and other settings unrelated to gemfire in
the _jvm-options_ setting, wich is a list.
* _bind-address_ can be an interface name, for example "eth0".
* for remote clusters, each host has an "ssh" object which specifies all of
the information needed to connect to that host.

