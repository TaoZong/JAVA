


public class WebCrawler extends BaseServlet implements UrlSetAndLock{

	private IndexBuilder ib;
	private WorkQueue threadPool;
	private static volatile int pending;
	private String url; 
	private static Object lockPending = new Object();
	
	WebCrawler(String url, int numThreads)
	{
		this.url = url;
		ib = new IndexBuilder();
		threadPool = new WorkQueue(numThreads);
		pending = 0;
		
		crawl(url);
	}
	
	public IndexBuilder getBuilder()
	{
		return ib;
	}
	
	public synchronized int getPending()
	{
		return pending;
	}
	
	public void crawl(String url)
	{
		synchronized(lockPending)
		{
			urlSet.add(url);
		}
		
		threadPool.execute(new WebWorker(url));		
		
		long startTime = System.currentTimeMillis();
		
		while(getPending() > 0)
		{
			if(System.currentTimeMillis() - startTime > 200)
			{
				startTime = System.currentTimeMillis();
			}
		}
		
	}
	
	
	private class WebWorker implements Runnable
	{
		private Fetcher fetcher;
		private String path = "";
		
		WebWorker(String url) 
		{
			path = url;
		
			synchronized(lockPending)
			{
				pending++;
			}

		}
		

		
		public void run()
		{	
	
			try
			{
				fetcher = new HTMLFetcher(path);			
			}
			catch(Exception e)
			{
			}
			
			HTMLParser htmlParser = new HTMLParser(fetcher.getHtmlText(), fetcher.domain);
			//HTMLParser htmlParser = new HTMLParser(fetcher.getHtmlText(), path);
				
			String sb = htmlParser.getTexts();
			String sb2 = getSubContent(sb);
			//System.out.println(sb);
			
			db.saveSnippet(path, sb2);
			
			if(!htmlParser.links.isEmpty())
			{
				for(String link: htmlParser.links)
				{
					threadPool.execute(new WebWorker(link));
				}
			}
			
			ib.build(path, htmlParser.getTexts());
			
			synchronized(lockPending)
			{
				pending--;
			}

		}
		
		public String getSubContent(String context){
			String content="";
			int count=0;
			String[] characters = context.trim().split("\\s");
			for(int i=0;i<characters.length;i++){
				characters[i]=characters[i].replaceAll("[^\\p{Alnum}]", "");
				if(!characters[i].equals(""))
				{	
					//characters[i]=characters[i].toLowerCase();
					content+=characters[i];
					count++;
				}
				content+=" ";
				if(count>50){
					break;
				}
			}
			return content;
		}
	}
}
