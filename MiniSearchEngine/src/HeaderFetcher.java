
public class HeaderFetcher extends Fetcher
{
	public HeaderFetcher(String url) throws Exception 
	{
		super(url);
	}

	protected String craftRequest()
	{
		String request = "HEAD " + this.resource + " HTTP/1.1\n";
		request += "Host: " + this.domain + "\n";
		request += "\n";
		
		return request;
	}
	

}