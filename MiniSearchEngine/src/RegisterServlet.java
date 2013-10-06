import java.io.*;
import javax.servlet.http.*;

/* RegisterServlet
 * Provides registration form, and handles form response.
 */
public class RegisterServlet extends BaseServlet 
{
	/* doGet(HttpServletRequest, HttpServletResponse)
	 * Provides registration form to user, and displays error warnings if
	 * registration failed.
	 */		
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	{
		PrintWriter out = prepareResponse("Register New User", response);
		
		// get error type from request
		String error = request.getParameter("error");
		
		// if error
		if(error != null)
		{
			//output different messages based on error value
			if(error.equals("nulls"))
			{
				out.println("<p style=\"color: red;\">Error, the username and password fields may not be blank.</p>");				
			}
			else if(error.equals("sql"))
			{
				out.println("<p style=\"color: red;\">Error, unable to complete registration. Please make sure you enter less than 30 characters for the username and password.</p>");
			}
			else if(error.equals("duplicate"))
			{		
				out.println("<p style=\"color: red;\">Error, username already taken. Please select another.</p>");
			}
			else
			{
				out.println("<p style=\"color: red;\">Error, unable to complete registration. Please try again.</p>");
			}
		}

		// print registration form
		out.println("<form action=\"/register\" method=\"post\">");
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
		out.println("<p><input type=\"submit\" value=\"Register\"></p>");
		out.println("</form>");
		
		// finish HTTP response (see BaseServlet)
		finishResponse(out, response);
	}

	/* doPost(HttpServletRequest, HttpServletResponse)
	 * Handles registration requests. Checks to see if user already exists.
	 */	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	{
		PrintWriter out = prepareResponse("Register New User", response);
		
		String newuser = request.getParameter("user");
		String newpass = request.getParameter("pass");
		
		Status status = Status.OK;
		
		if(checkString(newuser) && checkString(newpass))
		{
			status = db.registerUser(newuser, newpass);
		}
		else
		{
			status = Status.NULL_VALUES;
		}
		
		try
		{
			if(status == Status.OK)
			{	
				System.out.println("Redirecting user to login page.");
				response.sendRedirect(response.encodeRedirectURL("/login?newuser=true"));
			}
			else
			{
				String params = "?error=";
				
				// send different error values depending on status code
				switch(status) {
				case NULL_VALUES:
					params += "nulls";
					break;
				
				case DUPLICATE_USER:
					params += "duplicate";
					break;
					
				case SQL_ERROR:
					params += "sql";
					break;
					
				default:
					params += "true";
				}
				
				System.out.println("Redirecting user to registration page.");
				response.sendRedirect(response.encodeRedirectURL("/register" + params));
			}
		}
		catch(Exception ex)
		{
			System.out.println("Exception encountered while processing registration post.");
		}
	}
}