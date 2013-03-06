package rest;

public class Tweet{
	private String content;
	private String screenName;
	private long userId;
	private long statusId;
	private String publishedTimeGMTStr;
	
	public String getContent(){
		return content;
	}
	
	public String getScreenName(){
		return screenName;
	}
	
	public long getUserId(){
		return userId;
	}
	
	public long getStatusId(){
		return statusId;
	}
	
	public String getPublishedTimeGmtStr(){
		return publishedTimeGMTStr;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("{content:"+content+"}\n");
		sb.append("{screenName:"+screenName+"}\n");
		sb.append("{userId:"+userId+"}\n");
		sb.append("{statusId:"+statusId+"}\n");
		sb.append("{publishedTimeGMTStr:"+publishedTimeGMTStr+"}\n");
		return sb.toString();
	}
}
