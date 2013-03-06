Tweet Poller
============

Periodically fetch tweets from a specified data store.

Required
--------

* JRE 7
* Apache Commons: commons-configuration-1.6, commons-lang-2.5, commons-logging-1.1.1
* Gson: gson-2.2.2
* Guava: guava-11.0.2
* Hadoop hadoop-auth-2.0.0-cdh4.0.0, hadoop-common-2.0.0-cdh4.0.0
* HBase: hbase-0.92.1-cdh4.0.0-security
* HBaseWD
* Log4J: log4j-1.2.16
* orderly
* Simple Logging Facade for Java (SLF4J): slf4j-api-1.6.1, slf4j-log4j12-1.6.1
* Zookeeper: zookeeper-3.4.3-cdh4.0.0
* All required libraries are located in the `lib/` folder.

How to Run
----------

Polling `1`-minute window real-time (`0` min lagged time) tweets from HBase `plr_sg_tweet_live` table every `1` minute:

`java -cp poller.jar:lib/* poller.HBasePoller plr_sg_tweet_live 0 1 1`

Polling `1`-minute window real-time (`0` min lagged time) tweets from PalanteerDev Rest API `http://research.larc.smu.edu.sg:8080/PalanteerDevApi/rest/v1/tweets/search` endpoint every `1` minute:

`java -cp poller.jar:lib/* poller.RestApiPoller http://research.larc.smu.edu.sg:8080/PalanteerDevApi/rest/v1/tweets/search 0 1 1`
