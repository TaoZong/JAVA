import java.io.*;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.*;


/* BaseServlet
 * Provides consistent functions and HTML code for other servlets.
 * Extended by FavoriteServlet, LoginServlet, and RegisterServlet.
 */

public class BaseServlet extends HttpServlet 
{	

	
	// create a single shared instance of the database handler
	// only one handler should ever be active per server
	protected static DatabaseHandler db = new DatabaseHandler();
	
	/* prepareResponse(String, HttpServletResponse)
	 * Gets PrintWriter from response, and writes initial HTML code.
	 * Returns reference to the PrintWriter.
	 */
	protected PrintWriter prepareResponse(String title, HttpServletResponse response)
	{
		// create PrintWriter object
		PrintWriter out = null;
	
		// try/catch for any io errors
		try
		{
			// get writer from HTTP response
			out = response.getWriter();

			// set basic response properties
			response.setContentType("text/html");
			response.setCharacterEncoding("utf-8");

			// write out starting HTML
			out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">\n");
			out.println("<html>");
			out.println("");
			out.println("<head>");
			out.println("\t<title>" + title + "</title>" );
			out.println("</head>");
			out.println("");
			out.println("<body>");
			out.println("");
			out.println("<h1>" + title + "</h1>");
			out.println("");	
		} 
		catch(IOException ex)
		{
			System.out.println("Exception encountered preparing response.");
		}
		
		return out;
	}
	
	/* Finishes an HTTP response, writing the necessary HTML to complete
	 * the web page and setting the status to SC_OK.
	 */
	protected void finishResponse(PrintWriter out, HttpServletResponse response)
	{
		// if the PrintWriter is null, do nothing
		if(out == null) return;
		
		// otherwise, write out the finishing HTML
		out.println("");
		out.println("</body>");
		out.println("</html>");
		out.flush();
		
		// set status to SC_OK to indicate successful response
		response.setStatus(HttpServletResponse.SC_OK);
	}
	
	/* getCookieMap(HttpServletRequest)
	 * Gets array of cookies from HTTP request, and stores results in a map.
	 * Returns a map of cookie names to values.
	 */
	protected Map<String, String> getCookieMap(HttpServletRequest request)
	{
		// create storage map
		HashMap<String, String> map = new HashMap<String, String>();
		
		// get cookie array from request
		Cookie[] cookies = request.getCookies();
		
		// test if null (no cookies)
		if(cookies != null)
		{
			// if not null, parse each cookie
			for(Cookie cookie : cookies)
			{
				// use cookie name as the map key, 
				// and cookie value as map value
				map.put(cookie.getName(), cookie.getValue());
			}
		}
		
		return map;
	}
	
	/* boolean checkString(String)
	 * Checks whether the specified String is null or empty.
	 * Returns false if null or empty, true otherwise.
	 */
	public static boolean checkString(String text)
	{
		// test if text is null first,
		// then trim string and test if empty
		if(text != null && !text.trim().isEmpty())
		{
			return true;
		}
		else
		{
			return false;
		}
	}	
}
