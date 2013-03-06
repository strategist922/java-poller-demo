package poller;

import java.util.List;

public interface Fetcher {
	
	public void fetch() throws Exception;
	
	public int getLagMinute();
	
	public int getIntervalMinute();
	
	public List getCurFetch();
	
	public String getCurFetchJSONString();
	
}
