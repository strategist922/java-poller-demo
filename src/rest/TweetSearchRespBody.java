package rest;

import java.util.List;

public class TweetSearchRespBody{
	private List<Tweet> data;
	private String q;
	private int page;
	private int rpp;
	
	public List<Tweet> getData(){
		return data;
	}
	
	public String getQ(){
		return q;
	}
	
	public int getPage(){
		return page;
	}
	
	public int getRpp(){
		return rpp;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("{data:[\n");
		
		for(Tweet entry: data){
			sb.append(entry.toString()+"\n");
		}
		
		sb.append("]\n");
		sb.append("{q:"+q+"}\n");
		sb.append("{page:"+page+"}\n");
		sb.append("{rpp:"+rpp+"}\n");
		return sb.toString();
	}
}
