package poller;

import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Periodically fetch current tweets from a specified HBase table
 * @author aek
 *
 */
public class HBasePoller extends TimerTask{

	Fetcher fetcher = null;
	
	public HBasePoller(Fetcher fetcher) throws IOException{
		this.fetcher = fetcher;
	}
	
	public void run(){
		try {
			fetcher.fetch();
//			Gson gs = new Gson();
//			Tweet[] tweets = gs.fromJson(fetcher.getCurFetchJSONString(), Tweet[].class);
			
//			for(Tweet tw: tweets){
//				System.out.println(tw.getPublishedTimeGmtStr() + "\t" + tw.getContent().replaceAll("\n", ""));	
//			}

			System.out.println(fetcher.getCurFetch().size() + " tweets retrieved in the last " + fetcher.getIntervalMinute() + " minutes (" + fetcher.getLagMinute() + " minutes lagged time).");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		if(args.length != 4){
			System.out.println("Require 4 arguments: 1) HTable name; 2) retrieval lag (minute); 3) retrieval interval (minute); and 4) polling frequency (minute)");
			System.out.println("E.g.: plr_sg_tweet_live 0 1 1");
			System.exit(1);
		}
		try {
			Fetcher hbaseFetcher = new HBaseTweetFetcher(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]));
			TimerTask tt = new HBasePoller(hbaseFetcher);
			Timer timer = new Timer();
			timer.scheduleAtFixedRate(tt, new Date(), Integer.parseInt(args[3])*60*1000);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
