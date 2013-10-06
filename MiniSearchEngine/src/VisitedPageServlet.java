import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class VisitedPageServlet extends BaseServlet
{

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	{
		Map<String, String> cookies = getCookieMap(request);
		
		String url = request.getParameter("url");
		String user = cookies.get("name");
		
		
		db.saveVisitedPage(user, url);
		
		try 
		{
			//System.out.println(url);
			response.sendRedirect(response.encodeRedirectURL(url));
		} 
		catch (Exception ex) 
		{
			System.out.println("Encountered exception.");
		}
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	{
		Map<String, String> cookies = getCookieMap(request);
		
		PrintWriter out = prepareResponse("", response);
		
		out.println("<div id=\"title\" align=\"center\" > ");
		out.println("<img src=\"http://www.cs.usfca.edu/~ghe/cs212/final/logo.jpg\" alt=\"USER INFO\">");
		out.println("</div>");
		
		out.println("<div id=\"title\" align=\"center\" > ");
		out.println("<form action=\"/frontpage\">");
		out.println("\t\t<input type=\"submit\" value=\"Go back\"/>");
		out.println("</form>");
		out.println("</div>");
		
		out.println("<div id=\"record\" align=\"center\" > ");
		out.println("\t<tr>");
		out.println("\t\t<h2>Visited record:</h2>");
		out.println("\t</tr>");
		
		if(cookies.containsKey("login") && cookies.get("login").equals("true"))
		{			
			String url = request.getParameter("url");
			String user = cookies.get("name");
			
			//if(request.getParameter("url")!= null)
			//{
			//	db.saveVisitedPage(user, url);
			//	try 
			//	{
			//		response.sendRedirect(response.encodeRedirectURL(url));
			//	} 
			//	catch (Exception ex) 
			//	{
			//		System.out.println("Encountered exception.");
			//	}
		//	}
			
			if(request.getParameter("clear")!= null)
			{
				db.clearVisitedPage(user);
				out.println("<script>alert('Visited record cleared !');setTimeout(\"location='/frontpage'\",1000);</script>");
			}
		
			if(request.getParameter("view")!= null)
			{
				ArrayList<String> links = db.getVisitedPage(user);
				
				//out.println("Records:");
				
				for(String link: links)
				{
					//System.out.println(link);
					out.println("<li><a href=\"" + link + "\">" + link + "</a>");
					out.println("<br>");
				}
			}
									
		}
		else
		{
			try
			{
				System.out.println("Not logged in. Redirecting...");
				out.println("<script>alert('Please Login First !');setTimeout(\"location='/login'\",1000);</script>");
			}
			catch(Exception ex)
			{
				
			}
		}
		
		out.println("</div>");
		finishResponse(out,response);
	}
}
