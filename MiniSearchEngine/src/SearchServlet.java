import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class SearchServlet extends BaseServlet
{
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	{
		PrintWriter out = prepareResponse("", response);
		
		Map<String, String> cookies = getCookieMap(request);
		if(cookies.containsKey("login") && cookies.get("login").equals("true"))
		{
			String query = request.getParameter("query");
						
			out.println("<div id=\"title\" align=\"center\" > ");
			out.println("<img src=\"http://www.cs.usfca.edu/~ghe/cs212/final/logo.jpg\" alt=\"USER INFO\">");
			out.println("</div>"); 
			
			out.println("<div id=\"title\" align=\"center\" > ");
			out.println("<form action=\"/frontpage\">");
			out.println("\t\t<input type=\"submit\" value=\"Go back\"/>");
			out.println("</form>");
			out.println("</div>");
			
			out.println("\t<tr>");
			out.println("\t\t<h2> Rusult for:"+query+"</h2>");
			out.println("\t</tr>");
			
			out.println("<table  border=\"0\">");
			
			QueryBuilder qb = new QueryBuilder(query);
			qb.build();
			
			Searcher se = new Searcher(qb.getQuery(), Driver.index);
			se.search();
			
			SearchResult re = se.getResult();
			
			String linkString = re.toString();
			String[] links = linkString.split(" ");
			
			out.println("\t<tr>");
			
			for(String link: links)
			{
				String snippet = db.getSnippet(link);
				link = "<li><a href=\"/visited?url="+ link + "\"> " + link + "</a><br>";
				out.println(link);
				out.println("<i>"+snippet+"</i><br>");
				out.println("\t</tr>");
				out.println("\t</p>");
			}
			
			out.println("</table>");
			
			String user = cookies.get("name");
			
			//System.out.println(query);
			//System.out.println(user);
			
			db.saveHistory(user, query);	
			db.addSuggest(query);
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
		
		finishResponse(out,response);
	}

}
