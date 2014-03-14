package com.example.aplicativo.app;


public class LinkManager {

	public static final String linkPattern = new String("^(http|https|ftp)\\://([a-zA-Z0-9\\.\\-]+(\\:[a-zA-Z0-9\\.&amp;%\\$\\-]+)*@)*((25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])|localhost|([a-zA-Z0-9\\-]+\\.)*[a-zA-Z0-9\\-]+\\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(\\:[0-9]+)*(/($|[a-zA-Z0-9\\.\\,\\?\\'\\\\+&amp;%\\$#\\=~_\\-]+))*$");


	public static String parseLinks(String s){
		String[] phrase = s.split("( +|(\n)+)");
		String htmlText = "";

		for(String word : phrase){
			htmlText += (isLink(word) ?
					"\n<a href=\"" + word + "\">" + word + "</a>" : word) + " ";
		}

		return htmlText.trim();
	}

	public static boolean isLink(String s){
		return s.matches(linkPattern);
	}

	public static boolean verifyHasLink(String s){
		String[] phrase = s.split("( +|(\n)+)");

		for(String word : phrase){
			if(isLink(word))
				return true;
		}

		return false;
	}

	public static String fixLink(String wrongLink){

		wrongLink = wrongLink.toLowerCase().trim();
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

		link = link.toLowerCase().trim();

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
