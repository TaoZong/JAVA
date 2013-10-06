
public class HTMLFetcher extends Fetcher
{
	public HTMLFetcher(String url) throws Exception 
	{
		super(url);
	}

	protected String craftRequest()
	{
		return "GET " + resource;
	}
	

}