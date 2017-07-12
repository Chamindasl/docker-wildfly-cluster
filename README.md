# Docker-Wildfly-Cluster
Run a wildfly cluster for testing the application's cluster and high-availabilty readyness

## Wildfly Cluster, Wildfly High Availability, Distributed Computing, Distributed Cache, Infinispan, JMS, Queue, Topic, Active MQ,  Singalton EJB, Singalton Timer, MDB, Docker, Docker Compose.

.

## Pre-requiests.
This or higher:
  - Docker version 17.03.1-ce, build c6d412e
  - docker-compose version 1.13.0, build 1719ceb
```
chams@chams-VirtualBox ~/repo/other/tmp/wildfly-docker $ docker --version
Docker version 17.03.1-ce, build c6d412e
chams@chams-VirtualBox ~/repo/other/tmp/wildfly-docker $ docker-compose --version
docker-compose version 1.13.0, build 1719ceb
chams@chams-VirtualBox ~/repo/other/tmp/wildfly-docker $ 
```

## Limitations
* Scale up / down ONLY one at a time.
* For each scale up an env variable need to be set to avoid port conflicts. (may be resloved in future releases. :) )
* Works with docker host only networks

## Clone, Build and Run
1. Checkout the code and go to dir (Terninal T1)
```
git clone https://github.com/Chamindasl/docker-wildfly-cluster.git
cd docker-wildfly-cluster
```
2. Start first wildfly node
```
docker-compose up
```
3. Notice
3.1 Starting
```
wildfly_1  | =========================================================================
wildfly_1  | 
wildfly_1  |   JBoss Bootstrap Environment
wildfly_1  | 
wildfly_1  |   JBOSS_HOME: /opt/jboss/wildfly
wildfly_1  | 
wildfly_1  |   JAVA: /usr/lib/jvm/java/bin/java
wildfly_1  | 
wildfly_1  |   JAVA_OPTS:  -server -Xms64m -Xmx512m -XX:MetaspaceSize=96M -XX:MaxMetaspaceSize=256m -Djava.net.preferIPv4Stack=true -Djboss.modules.system.pkgs=org.jboss.byteman -Djava.awt.headless=true
wildfly_1  | 
wildfly_1  | =========================================================================
wildfly_1  | 
wildfly_1  | 19:47:07,477 INFO  [org.jboss.modules] (main) JBoss Modules version 1.5.2.Final
wildfly_1  | 19:47:08,111 INFO  [org.jboss.msc] (main) JBoss MSC version 1.2.6.Final

```

3.2 Started
```
wildfly_1  | 19:47:36,411 INFO  [org.jboss.as] (Controller Boot Thread) WFLYSRV0025: WildFly Full 10.1.0.Final (WildFly Core 2.2.0.Final) started in 29756ms - Started 679 of 955 services (530 services are lazy, passive or on-demand)
```

3.3 JMS Producer started sending messages and Consumers started consuming messages. 
```
wildfly_1  | 19:47:40,382 INFO  [chams.open.cluster.jms.Producer] (EJB default - 1) Sent 10 messages, Total : 10 
wildfly_1  | 19:47:40,557 INFO  [chams.open.cluster.jms.Consumer] (Thread-0 (ActiveMQ-client-global-threads-885366707)) received 1499888860278
wildfly_1  | 19:47:40,556 INFO  [chams.open.cluster.jms.Consumer] (Thread-1 (ActiveMQ-client-global-threads-885366707)) received 1499888860323
wildfly_1  | 19:47:40,583 INFO  [chams.open.cluster.jms.Consumer] (Thread-2 (ActiveMQ-client-global-threads-885366707)) received 1499888860328
wildfly_1  | 19:47:40,599 INFO  [chams.open.cluster.jms.Consumer] (Thread-3 (ActiveMQ-client-global-threads-885366707)) received 1499888860333
wildfly_1  | 19:47:40,643 INFO  [chams.open.cluster.jms.Consumer] (Thread-4 (ActiveMQ-client-global-threads-885366707)) received 1499888860336
wildfly_1  | 19:47:40,655 INFO  [chams.open.cluster.jms.Consumer] (Thread-5 (ActiveMQ-client-global-threads-885366707)) received 1499888860337
wildfly_1  | 19:47:40,697 INFO  [chams.open.cluster.jms.Consumer] (Thread-6 (ActiveMQ-client-global-threads-885366707)) received 1499888860342
wildfly_1  | 19:47:40,714 INFO  [chams.open.cluster.jms.Consumer] (Thread-1 (ActiveMQ-client-global-threads-885366707)) received 1499888860343
wildfly_1  | 19:47:40,743 INFO  [chams.open.cluster.jms.Consumer] (Thread-7 (ActiveMQ-client-global-threads-885366707)) received 1499888860342
wildfly_1  | 19:47:40,766 INFO  [chams.open.cluster.jms.Consumer] (Thread-0 (ActiveMQ-client-global-threads-885366707)) received 1499888860343

```

3.4. Producer keeps track of Total messages sent
```
 Sent 10 messages, Total : 10
 Sent 10 messages, Total : 20
```


## Scaling up

1.Scale up
FROM ANOTHER TERMINAL (T2)

1.1.  Set N_ID to 1 (First node id is 0. This one is 1)
```
 export N_ID=1
 ```

1.2. Scale up

```
docker-compose scale wildfly=2
```

2.Notice

2.1 T2 
```
Starting dockerwildflycluster_wildfly_1 ... done
Creating dockerwildflycluster_wildfly_2 ... 
Creating dockerwildflycluster_wildfly_2 ... done
```

2.2 T1. N_ID_0 and N_ID_1 logs in Terminal 1
```
wildfly_1  | 20:03:50,037 INFO  [chams.open.cluster.jms.Consumer] (Thread-4 (ActiveMQ-client-global-threads-782264894)) received 1499889830007
wildfly_1  | 20:03:50,060 INFO  [chams.open.cluster.jms.Consumer] (Thread-1 (ActiveMQ-client-global-threads-782264894)) received 1499889830008
wildfly_1  | 20:03:50,063 INFO  [chams.open.cluster.jms.Consumer] (Thread-6 (ActiveMQ-client-global-threads-782264894)) received 1499889830005
wildfly_2  | =========================================================================
wildfly_2  | 
wildfly_2  |   JBoss Bootstrap Environment
wildfly_2  | 
wildfly_2  |   JBOSS_HOME: /opt/jboss/wildfly
wildfly_2  | 
wildfly_2  |   JAVA: /usr/lib/jvm/java/bin/java
wildfly_2  | 
wildfly_2  |   JAVA_OPTS:  -server -Xms64m -Xmx512m -XX:MetaspaceSize=96M -XX:MaxMetaspaceSize=256m -Djava.net.preferIPv4Stack=true -Djboss.modules.system.pkgs=org.jboss.byteman -Djava.awt.headless=true
wildfly_2  | 
```

2.3 N_ID 0 and 1 Recevies new cluster views
```
wildfly_1  | 20:04:12,742 INFO  [org.infinispan.remoting.transport.jgroups.JGroupsTransport] (thread-1,ee,wildfly_0) ISPN000094: Received new cluster view for channel server: [wildfly_0|1] (2) [wildfly_0, wildfly_1]
wildfly_1  | 20:04:12,775 INFO  [org.infinispan.remoting.transport.jgroups.JGroupsTransport] (thread-1,ee,wildfly_0) ISPN000094: Received new cluster view for channel web: [wildfly_0|1] (2) [wildfly_0, wildfly_1]
wildfly_1  | 20:04:12,782 INFO  [org.infinispan.remoting.transport.jgroups.JGroupsTransport] (thread-1,ee,wildfly_0) ISPN000094: Received new cluster view for channel jobSentCacheContainer: [wildfly_0|1] (2) [wildfly_0, wildfly_1]
wildfly_1  | 20:04:12,800 INFO  [org.infinispan.remoting.transport.jgroups.JGroupsTransport] (thread-1,ee,wildfly_0) ISPN000094: Received new cluster view for channel ejb: [wildfly_0|1] (2) [wildfly_0, wildfly_1]
wildfly_1  | 20:04:12,830 INFO  [org.infinispan.remoting.transport.jgroups.JGroupsTransport] (thread-1,ee,wildfly_0) ISPN000094: Received new cluster view for channel hibernate: [wildfly_0|1] (2) [wildfly_0, wildfly_1]

```

2.4 T1. N_ID 0 and 1 started consuming messages in load balanced manner (Only N_ID 0 (Leader / Master ) is producing msgs)
```
wildfly_1  | 20:04:50,009 INFO  [chams.open.cluster.jms.Producer] (EJB default - 5) Sent 10 messages, Total : 350 
wildfly_1  | 20:04:50,034 INFO  [chams.open.cluster.jms.Consumer] (Thread-2 (ActiveMQ-client-global-threads-782264894)) received 1499889890008
wildfly_1  | 20:04:50,049 INFO  [chams.open.cluster.jms.Consumer] (Thread-4 (ActiveMQ-client-global-threads-782264894)) received 1499889890007
wildfly_1  | 20:04:50,042 INFO  [chams.open.cluster.jms.Consumer] (Thread-5 (ActiveMQ-client-global-threads-782264894)) received 1499889890007
wildfly_1  | 20:04:50,041 INFO  [chams.open.cluster.jms.Consumer] (Thread-6 (ActiveMQ-client-global-threads-782264894)) received 1499889890006
wildfly_1  | 20:04:50,034 INFO  [chams.open.cluster.jms.Consumer] (Thread-1 (ActiveMQ-client-global-threads-782264894)) received 1499889890006
wildfly_2  | 20:04:50,149 INFO  [chams.open.cluster.jms.Consumer] (Thread-1 (ActiveMQ-client-global-threads-90318281)) received 1499889890006
wildfly_2  | 20:04:50,182 INFO  [chams.open.cluster.jms.Consumer] (Thread-7 (ActiveMQ-client-global-threads-90318281)) received 1499889890005
wildfly_2  | 20:04:50,191 INFO  [chams.open.cluster.jms.Consumer] (Thread-4 (ActiveMQ-client-global-threads-90318281)) received 1499889890007
wildfly_2  | 20:04:50,187 INFO  [chams.open.cluster.jms.Consumer] (Thread-0 (ActiveMQ-client-global-threads-90318281)) received 1499889890006
wildfly_2  | 20:04:50,230 INFO  [chams.open.cluster.jms.Consumer] (Thread-6 (ActiveMQ-client-global-threads-90318281)) received 1499889890008

```

## Scaling up further
1. Scale up
FROM ANOTHER TERMINAL (T3 to N)

1.1 Set N_ID to 2 
```
export N_ID=2
docker-compose scale wildfly=3
```

2. Notice

New Cluster views (above 2.3)
Further load balancing for consumers. (above 2.4)

## High Availability - My Favorite Part

1. Kill N_ID 0 (Leader)
FROM TERMINAL X
```
docker stop dockerwildflycluster_wildfly_1
```

2. Notice

2.1 New Leader Election
N_ID_1 became the new Leader
```
wildfly_1  | 20:32:00,046 INFO  [chams.open.cluster.jms.Producer] (EJB default - 2) Sent 10 messages, Total : 410 
wildfly_2  | 20:32:00,124 INFO  [chams.open.cluster.jms.Consumer] (Thread-3 (ActiveMQ-client-global-threads-1504106724)) received 1499891520011
wildfly_1  | 20:32:00,142 INFO  [chams.open.cluster.jms.Consumer] (Thread-4 (ActiveMQ-client-global-threads-336450273)) received 1499891520016
wildfly_2  | 20:32:00,172 INFO  [chams.open.cluster.jms.Consumer] (Thread-5 (ActiveMQ-client-global-threads-1504106724)) received 1499891520016
wildfly_1  | 20:32:00,174 INFO  [chams.open.cluster.jms.Consumer] (Thread-5 (ActiveMQ-client-global-threads-336450273)) received 1499891520010
wildfly_1  | 20:32:00,169 INFO  [chams.open.cluster.jms.Consumer] (Thread-6 (ActiveMQ-client-global-threads-336450273)) received 1499891520044

...

wildfly_2  | 20:35:20,155 INFO  [chams.open.cluster.jms.Producer] (EJB default - 8) Sent 10 messages, Total : 420 
wildfly_2  | 20:35:20,199 INFO  [chams.open.cluster.jms.Consumer] (Thread-3 (ActiveMQ-client-global-threads-1504106724)) received 1499891720128
wildfly_2  | 20:35:20,228 INFO  [chams.open.cluster.jms.Consumer] (Thread-5 (ActiveMQ-client-global-threads-1504106724)) received 1499891720145
wildfly_3  | 20:35:20,230 INFO  [chams.open.cluster.jms.Consumer] (Thread-4 (ActiveMQ-client-global-threads-693860771)) received 1499891720144

```

2.2 Distributed Cache with Infinispan
Notice that N_ID_0 Stoped at Total Total : 410 and New leader starts where old leader stoped Total : 420 
```
wildfly_1  | 20:32:00,046 INFO  [chams.open.cluster.jms.Producer] (EJB default - 2) Sent 10 messages, Total : 410 
wildfly_2  | 20:35:20,155 INFO  [chams.open.cluster.jms.Producer] (EJB default - 8) Sent 10 messages, Total : 420 
```

## Check the code
:)
