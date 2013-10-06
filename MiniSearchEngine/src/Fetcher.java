import java.io.*;
import java.net.*;

public abstract class Fetcher 
{
	protected static final int PORT = 80;
	protected String domain;
	protected String resource;
	private String htmltext = "";

	public Fetcher(String url) throws Exception 
	{
		URLParser parser = new URLParser(url);
		domain = parser.getDomain();
		resource = parser.getResource();
		
		fetch();
	}

	protected abstract String craftRequest();
	
	public void fetch() throws Exception 
	{
		if (resource == null || domain == null)
		{
			System.err.printf("There is no domain or resource to fetch.\n");
			return;
		}
		
		Socket socket = new Socket(domain, PORT);
		//System.out.println(domain + ":" + PORT);
		
		PrintWriter writer = new PrintWriter(socket.getOutputStream());
		
		BufferedReader reader = 
			new BufferedReader(
					new InputStreamReader(socket.getInputStream()));

		String request = craftRequest();
		//System.out.println(request);
		
		writer.println(request);
		writer.flush();

		String line;
		
		boolean readflag=false;
		
		while ((line = reader.readLine()) != null) 
		{			
			if(line.trim().length()==0)
			{
				readflag=true;
			}
			if(readflag)
			{
				line+="\n";
				htmltext += line + " ";
			}				
			//System.out.println(line);
		}
		
		//System.out.println("read finish");
		
		reader.close();
		writer.close();
		socket.close();
		
		//System.out.println("[done]");
	}
	
	public String getHtmlText()
	{
		return htmltext;		
	}
}