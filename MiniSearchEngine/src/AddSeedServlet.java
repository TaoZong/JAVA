import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class AddSeedServlet extends BaseServlet implements UrlSetAndLock
{
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	{
		PrintWriter out = prepareResponse("Crawling, please wait...", response);
		
		System.out.println("here");
		
		String seed = request.getParameter("newSeed");
			
		urlSet.clear();
		
		WebCrawler crawler = new WebCrawler(seed, 10);
		
		try 
		{
			response.sendRedirect(response.encodeRedirectURL("/frontpage"));
		} 
		catch (Exception ex) 
		{

		}
		
		finishResponse(out,response);
	}
}
