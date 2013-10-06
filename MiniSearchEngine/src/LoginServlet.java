import java.io.*;
import java.util.Map;

import javax.servlet.http.*;

/* LoginServlet
 * Provides and response to login form.
 */
public class LoginServlet extends BaseServlet 
{
	/* doGet(HttpServletRequest, HttpServletResponse)
	 * Provides login form to user, and displays error warnings if
	 * login failed. Also clears cookies if user wants to logout.
	 */	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	{
		// prepare HTTP response (see BaseServlet)
		PrintWriter out = prepareResponse("", response);
		
		out.println("<div id=\"title\" align=\"center\" > ");
		out.println("<img src=\"http://www.cs.usfca.edu/~ghe/cs212/final/logo.jpg\" alt=\"USER INFO\">");
		out.println("</div>"); 
		
		// check if error occurred
		if(request.getParameter("error") != null)
		{
			// display error message
			out.println("<p style=\"color: red;\">Error, incorrect username or password. Please try to log in again.</p>");
		}
		
		// check if new user created
		if(request.getParameter("newuser") != null)
		{
			// display welcome message
			out.println("<p>Success! Login with your new username and password below.</p>");
		}

		// check if logout requested
		if(request.getParameter("logout") != null)
		{
			// get all cookies associated with request
			Cookie[] cookies = request.getCookies();
			
			// for each cookie returned
			for(Cookie cookie : cookies)
			{
				// set max age to zero to erase cookie
				cookie.setMaxAge(0);
				
				// add cookie with max age setting to response
				response.addCookie(cookie);
			}
			
			// print logout message
			out.println("<p>Successfully logged out.</p>");
		}

		// print login form
		out.println("<form action=\"/login\" method=\"post\">");
		out.println("<div id=\"logtable\" align=\"center\" >");
		out.println("<table border=\"0\">");
		out.println("\t<tr>");
		out.println("\t\t<td>Usename:</td>");
		out.println("\t\t<td><input type=\"text\" name=\"user\" size=\"30\"></td>");
		out.println("\t</tr>");
		out.println("\t<tr>");
		out.println("\t\t<td>Password:</td>");
		out.println("\t\t<td><input type=\"password\" name=\"pass\" size=\"30\"></td>");
		out.println("</tr>");
		out.println("</table>");
		out.println("<p><input type=\"submit\" value=\"Login\"></p>");
		out.println("</div>");
		out.println("</form>");
		
		out.println("<div id=\"regist\" align=\"center\" >");
		out.println("<p>(<a href=\"/register\">new user? register here.</a>)</p>");
		out.println("</div>");
		
		out.println("<div id=\"title\" align=\"center\" > ");
		out.println("<img src=\"http://www.cs.usfca.edu/~ghe/cs212/final/login.jpg\" alt=\"USER INFO\">");
		out.println("</div>"); 
		
		// finish HTTP response (see BaseServlet)
		finishResponse(out, response);
	}
	
	/* doPost(HttpServletRequest, HttpServletResponse)
	 * Handles login requests. Checks to see if user and password are correct.
	 */		
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	{
		// get parameters from post request
		String user = request.getParameter("user");
		String pass = request.getParameter("pass");
		
		boolean status = false;
		
		// test if parameters are null
		if(user != null && pass != null)
		{
			// if not null, check user/pass against database
			System.out.println("Checking user " + user + " for correct password...");
			status = db.checkLogin(user, pass);
		}
		else
		{
			status = false;
		}
		
		try
		{
			// if login was successful
			if(status)
			{
				// add login cookies to track user session
				response.addCookie(new Cookie("login", "true"));
				response.addCookie(new Cookie("name", user));
				
				// redirect user to favorites page
				System.out.println("Login succeeded. Redirecting...");
				response.sendRedirect(response.encodeRedirectURL("/frontpage"));
			}
			else
			{
				// indicate login did not succeed (overwrites any cookies
				// from previous sessions)
				response.addCookie(new Cookie("login", "false"));
				response.addCookie(new Cookie("name", ""));

				// redirect user to login page
				System.out.println("Login failed. Redirecting...");
				response.sendRedirect(response.encodeRedirectURL("/login?error=true"));
			}
		}
		catch(Exception ex)
		{
			System.out.println("Encountered exception.");
		}
	}	
}
