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
public class RestApiPoller extends TimerTask{

	Fetcher fetcher = null;
	
	public RestApiPoller(Fetcher fetcher) throws IOException{
		this.fetcher = fetcher;
	}
	
	public void run(){
		try {
			fetcher.fetch();
			
//			for(Tweet tw: ((RestApiTweetFetcher) fetcher).getCurFetch()){
//				System.out.println(tw.getPublishedTimeGmtStr() + "\t" + tw.getContent().replaceAll("\n", ""));
//			}
			
			System.out.println(fetcher.getCurFetch().size() + " tweets retrieved in the last " + fetcher.getIntervalMinute() + " minutes (" + fetcher.getLagMinute() + " minutes lagged time).");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args){
		if(args.length < 4){
			System.out.println("Require at least 4 arguments: 1) API endpoint; 2) retrieval lag (minute); 3) retrieval interval (minute); and 4) polling frequency (minute)");
			System.out.println("E.g.: http://research.larc.smu.edu.sg:8080/PalanteerDevApi/rest/v1/tweets/search 0 1 1");
			System.out.println("The 5th argument (query string) is optional.");
			System.exit(1);
		}
		try {
			Timer timer = new Timer();
			Fetcher restFetcher = null;
			String q = null;

			if(args.length == 5) q = args[4];
			
			restFetcher = new RestApiTweetFetcher(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]), q);
			
			TimerTask tt = new RestApiPoller(restFetcher);
			timer.scheduleAtFixedRate(tt, new Date(), Integer.parseInt(args[3])*60*1000);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
}
