import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class FrontServlet extends BaseServlet 
{
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	{
		PrintWriter out = prepareResponse("", response);
		
		out.println("<div id=\"title\" align=\"center\" > ");
		out.println("<img src=\"http://www.cs.usfca.edu/~ghe/cs212/final/logo.jpg\" alt=\"USER INFO\">");
		out.println("</div>"); 
		
		Map<String, String> cookies = getCookieMap(request);
		if(cookies.containsKey("login") && cookies.get("login").equals("true"))
		{
			String username = cookies.get("name");
			
			//search
			out.println("<form action=\"/search\" method=\"post\">");
			out.println("<div id=\"searchline\" align=\"center\" >");
			out.println("\t\tSearch for:");
			out.println("\t\t<input type=\"text\" name=\"query\" size=\"90\" maxlength=\"200\">");
			out.println("</p></div>"); 
			out.println("<div id=\"buttons\" align=\"center\">"); 
			out.println("<p><input type=\"submit\" value=\"         Search         \"></p>");
			out.println("</div>"); 
			out.println("</form>");
			
			out.println("<br>");
			out.println("<div id=\"suggest\" align=\"center\" >");
			out.println("Suggested querys: " + db.getSuggest());
			out.println("</div>"); 
			out.println("<br>");
			
			
			out.println("<div id=\"pic\" align=\"center\" >");
			out.println("<table border=\"0\">");
			out.println("<tr>");
			out.println("<td>");
			
			//change password
			out.println("<form action=\"/modifyuser?username="+username+"\"method=\"post\">");
			out.println("Account Maintenance:");
			out.println("<table border=\"0\">");
			out.println("\t<tr>");
			out.println("\t\t<td>New Password:</td>");
			out.println("\t\t<td><input type=\"password\" name=\"npass\" size=\"30\"></td>");
			out.println("\t</tr>");
			out.println("\t<tr>");
			out.println("\t\t<td>Commit Password:</td>");
			out.println("\t\t<td><input type=\"password\" name=\"commit\" size=\"30\"></td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("\t\t<input type=\"submit\" value=\"Modify\"/>");
			out.println("\t\t<input type=\"submit\" value=\"Logout\" name=\"Logout\"/>\t\t");
			out.println("</form>");
			out.println("<br><br>");
			
			//history
			out.println("<form action=\"/history?username="+username+"\"method=\"post\">");
			out.println("Search history maintenance:");
			out.println("<table border=\"0\">");
			out.println("\t<tr>");
			out.println("\t\t<td>\t\t<input type=\"submit\" value=\"Clear History\" name=\"clear\"/>");
			out.println("\t\t\t\t<input type=\"submit\" value=\"View History\" name=\"view\"/>\t\t");
			out.println("\t</tr>");
			out.println("</table>");
			out.println("</form>");
			out.println("<br><br>");
			
			//visited pages
			out.println("<form action=\"/visited?username="+username+"\"method=\"post\">");
			out.println("Visited record maintenance:");
			out.println("<table border=\"0\">");
			out.println("\t<tr>");
			out.println("\t\t<td><input type=\"submit\" value=\"Clear visite Record\" name=\"clear\"/>");
			out.println("\t\t<input type=\"submit\" value=\"View visite Record\" name=\"view\"/>\t\t");
			out.println("\t</tr>");
			out.println("</table>");
			out.println("</form>");
			out.println("<br><br>");
			
			//add seed
			out.println("<form action=\"/addseed?username="+username+"\"method=\"post\">");
			out.println("Add new search seed here:");
			out.println("<table border=\"0\">");
			out.println("\t<tr>");
			out.println("\t\t<td>New Seed:</td>");
			out.println("\t\t<td><input type=\"text\" name=\"newSeed\" size=\"30\"></td>");
			out.println("\t</tr>");
			out.println("</table>");
			out.println("\t\t<input type=\"submit\" value=\"Add Seed\"/>\t\t");
			out.println("</form>");
			
			out.println("</td>");
			
			out.println("<td>");
			out.println("<img src=\"http://www.cs.usfca.edu/~ghe/cs212/final/login.jpg\" alt=\"USER INFO\">");
			out.println("</td>");
			
			out.println("</tr>");
			out.println("</table>");
			out.println("</div>");
			
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
