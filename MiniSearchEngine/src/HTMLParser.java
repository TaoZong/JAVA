import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class HTMLParser implements UrlSetAndLock{
	
	private String url = "";
	private String texts = "";
	public HashSet<String> links;
	
	public HTMLParser(String text, String domain)
	{
		url = domain;
		
		//if(!(url.endsWith("html")&&url.endsWith("htm")&&url.endsWith("/")))
			//url = url + "/";
		
		texts = text;
		links = new HashSet<String>();
		parse();
	}
	
	
	
	/*
	 * read the HTML file line by line and find information needed.
	 */
	public void parse()
	{
			
		getURL(texts);
			
		texts = removeScript(texts);
			
		texts = removeStyle(texts);
			
		texts = removeTags(texts);
		
		texts = removeEscapeSe(texts);
		
	}


	
	public String getTexts()
	{
		return texts;
	}
	
	/*
	 * Get url from html text.Can read relative links.
	 */
	public void getURL(String input)
	{
		String linkReg = "<\\s*a\\s+[^href]*href\\s*=\\s*\"([^\"]+)\"[^>]*>";
		Pattern linkPattern = Pattern.compile(linkReg, Pattern.CASE_INSENSITIVE);
		Matcher matcher = linkPattern.matcher(input);
		
		while(matcher.find())
		{
			//get links
			String sb = matcher.group(1);	
			
			//if link is valid
			if(sb.endsWith("html")||sb.endsWith("htm")||sb.endsWith("/"))
			{				
				//add absolute links
				if(sb.startsWith("http"))
				{
					linkLock.getWriteLock();
					if(!urlSet.contains(sb)&&urlSet.size() < 30)
					{
						urlSet.add(sb);
						links.add(sb);
					}
					linkLock.releaseWriteLock();
				}
				//add relative links
				else if(!sb.contains(":"))
				{
					sb = "http://" + url +sb;
					linkLock.getWriteLock();
					if(!urlSet.contains(sb)&&urlSet.size() < 30)
					{
						urlSet.add(sb);
						links.add(sb);
					}
					linkLock.releaseWriteLock();
				}
				
			}
		}
	}
	
	
	/*
	 * remove all scripts
	 */
	public String removeScript(String input)
	{
		String scriptReg = "<\\s*script([^>]*)\\s*>([^<]*)</script([^>]*)\\s*>";
		Pattern linkPattern = Pattern.compile(scriptReg, Pattern.CASE_INSENSITIVE);
		Matcher matcher =linkPattern.matcher(input);
	
		return matcher.replaceAll("");
	}
	
	/*
	 * remove all style texts
	 */
	public String removeStyle(String input)
	{
		String styleReg = "<\\s*style([^>]*)\\s*>([^<]*)</style([^>]*)\\s*>";
		Pattern linkPattern = Pattern.compile(styleReg, Pattern.CASE_INSENSITIVE);
		Matcher matcher =linkPattern.matcher(input);
	
		return matcher.replaceAll("");
	}
	
	/*
	 * remove all tags
	 */
	public String removeTags(String input)
	{
		String scriptReg = "<([^>]*)>";
		Pattern linkPattern = Pattern.compile(scriptReg, Pattern.CASE_INSENSITIVE);
		Matcher matcher =linkPattern.matcher(input);
		
		return matcher.replaceAll("");
	}
	
	
	/*
	 * remove all escape sequences for special characters
	 */
	public String removeEscapeSe(String input)
	{
		String escapeSeReg = "&([^;]*);";
		Pattern linkPattern = Pattern.compile(escapeSeReg, Pattern.CASE_INSENSITIVE);
		Matcher matcher =linkPattern.matcher(input);
		
		return matcher.replaceAll("");
		
	}
		

}
