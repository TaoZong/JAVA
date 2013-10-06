import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class HistoryServlet extends BaseServlet
{
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
		
		out.println("<div id=\"title\" align=\"center\" > ");
		out.println("\t<tr>");
		out.println("\t\t<h2>Search history:</h2>");
		out.println("\t</tr>");
		
		out.println("<table  border=\"0\">");
		out.println("\t<tr>");
		
		if(cookies.containsKey("login") && cookies.get("login").equals("true"))
		{
			if(request.getParameter("view")!= null)
			{
				ArrayList<String> querys = db.getHistory(cookies.get("name"));
				
				for(String query: querys)
				{
					out.println("<br>" + query);
				}
			}
			
			if(request.getParameter("clear")!= null)
			{
				db.clearHistory(cookies.get("name"));
				out.println("<script>alert('History cleared !');setTimeout(\"location='/frontpage'\",1000);</script>");
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
		out.println("</table>");
		finishResponse(out,response);
	}
}
