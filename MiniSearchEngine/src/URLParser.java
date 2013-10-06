import java.util.regex.*;

public class URLParser
{
	private static String regex = "http://([^/]+)(/.*)?";
	
	private Pattern pattern;
	private Matcher matcher;
	
	private String url;
	private String domain;
	private String resource;

	private boolean status;
	
	public URLParser(String url)
	{	
		try
		{
			pattern = Pattern.compile(regex);
		}
		catch(PatternSyntaxException ex)
		{
			pattern = null;
			System.err.println("Error: Unable to compile URL regex!"); 
		}
		
		status = parse(url);
	}

	private boolean parse(String url)
	{
		boolean okay = false;
		
		try
		{
			if(pattern != null && url != null)
			{
				this.url = url;		
				matcher = pattern.matcher(this.url);
				
				if(matcher.matches() && matcher.groupCount() == 2)
				{
					if(matcher.group(1) != null)
					{
						domain = matcher.group(1);
						
						if(matcher.group(2) != null)
						{
							resource = matcher.group(2);
						}
						else
						{
							resource = "/";
						}
						
						okay = true;
					}
				}			
			}
		}
		catch(Exception ex)
		{
			System.err.println("Error: Encountered exception while parsing URL!");
		}
		
		return okay;
	}
	
	public String getDomain()
	{
		if(status)
			return domain;
		else
			return null;
	}
	
	public String getResource()
	{
		if(status)
			return resource;
		else
			return null;
	}
}