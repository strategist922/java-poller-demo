HBase Tweet Poller
==================

Periodically fetch tweets from a specified HBase table.

Required
--------

* JRE 7
* All required libraries are located in the `lib/` folder.

How to Run
----------

`java -cp hbasepoller.jar:lib/* poller.FetcherTimerTask plr_sg_tweet_live 0 1 1`
