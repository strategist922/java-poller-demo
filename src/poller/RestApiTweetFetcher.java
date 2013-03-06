package poller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import rest.Tweet;
import rest.TweetSearchRespBody;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class RestApiTweetFetcher implements Fetcher {
	
	private final int NUM_FETCHERS = 5;
	private final String resourceName;
	private int lagMinute = 1;
	private int intervalMinute = 1;
	private int pageNo = 1;
	private String query = null;
	private DateFormat utcDf = null;
	private List<Tweet> curFetch = null;
	
	public RestApiTweetFetcher(String uri, int lagMinute, int intervalMinute, String query){
		this.resourceName = uri;
		this.lagMinute = lagMinute;
		this.intervalMinute = intervalMinute;
		this.query = query;
		utcDf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:00'Z'");
		utcDf.setTimeZone(TimeZone.getTimeZone("UTC"));
	}
	
	public RestApiTweetFetcher(String uri, int lagMinute, int intervalMinute){
		this(uri, lagMinute, intervalMinute, null);
	}
	
	public void fetch(){
		this.pageNo = 1;
		this.curFetch = Lists.newArrayList();
		Date end = new Date(new Date().getTime()  - (this.lagMinute*60*1000));
		Date start = new Date(end.getTime() - (this.intervalMinute*60*1000));
		String uriStr = null;
		Thread[] workers = new Thread[NUM_FETCHERS];
		for (int i = 0; i < workers.length; i++) {
			uriStr = this.resourceName + "?start=" + utcDf.format(start)
					+ "&end=" + utcDf.format(end) + "&page=" + (pageNo++);
			
			if(this.query == null) uriStr += "&q=*";
			else uriStr += "&q=" + this.query;
			
			workers[i] = new Thread(new RequestWorker(uriStr, this.curFetch));
			workers[i].start();
		}
		
		// Wait for all threads to finish
		for (int i = 0; i < workers.length; i++) {
			try {
				workers[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public int getPageNo(){
		return this.pageNo;
	}
	
	public void setPageNo(int pageNo){
		this.pageNo = pageNo;
	}
	
	@Override
	public int getLagMinute() {
		return this.lagMinute;
	}

	@Override
	public int getIntervalMinute() {
		return this.intervalMinute;
	}

	@Override
	public List<Tweet> getCurFetch() {
		return this.curFetch;
	}

	@Override
	public String getCurFetchJSONString(){
		return new Gson().toJson(this.curFetch);
	}
}

class RequestWorker implements Runnable {
	
	private List<Tweet> fetch = null;
	private String uriStr = null;
	
	public RequestWorker(String uriStr, List<Tweet> refFetch) {
		this.uriStr = uriStr;
		this.fetch = refFetch;
	}

	public void run() {
		URL url = null;
		HttpURLConnection conn = null;
		BufferedReader br = null;
		String line = null;
		StringBuilder resp = null;
		Gson gs = null;
		TweetSearchRespBody data = null;
		
		try {
			url = new URL(this.uriStr);
			conn = (HttpURLConnection) url.openConnection();
			
			if(conn.getResponseCode()!=200){
				throw new IOException(conn.getResponseMessage());
			}

			br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			resp = new StringBuilder();

			while ((line = br.readLine()) != null) {
				resp.append(line);
			}
			
			br.close();
			conn.disconnect();
			gs = new Gson();
			data = gs.fromJson(resp.toString(), new TypeToken<TweetSearchRespBody>() {}.getType());
//			System.out.println(this.uriStr+": "+data.getData().size()+" tweets");
			fetch.addAll(data.getData());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
