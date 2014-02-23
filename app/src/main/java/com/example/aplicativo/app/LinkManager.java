package com.example.aplicativo.app;


public class LinkManager {

	public static String fixLink(String wrongLink){
		
		String link = "";
		
		if(!wrongLink.contains("http://") && !wrongLink.contains("https://"))
			link += "https://";
		
		if(!wrongLink.contains("www."))
			link += "www.";
		
		link += wrongLink;
		
		if(!wrongLink.contains(".com"))
			link += ".com";
				
		return link;
	}
	
	public static String extractSiteName(String link){
		
		if(link.contains("http://"))
			link = link.replace("http://", "");
		else if(link.contains("https://"))
			link = link.replace("https://", "");
		
		if(link.contains("www."))
			link = link.replace("www.", "");
		
		if(link.contains(".com"))
			link = link.replace(".com", "");
		
		if(link.contains(".br"))
			link = link.replace(".br", "");
		
		String name = "";
				
		name += Character.toUpperCase(link.charAt(0));
		
		for(int i = 1; i < link.length(); i++){
			name += link.charAt(i);
		}
		
		return name;
	}
}
