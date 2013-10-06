import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ModifyUserServlet extends BaseServlet
{
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	{
		Map<String, String> cookies = getCookieMap(request);
		
		PrintWriter out = prepareResponse("Modifying", response);
		
		if(request.getParameter("Logout")!=null)
		{
			System.out.println("11");
			Cookie[] cookies2 = request.getCookies();
			if (cookies2 != null){
				for(Cookie cookie : cookies2)
				{
					// set max age to zero to erase cookie
					cookie.setMaxAge(0);
					
					// add cookie with max age setting to response
					response.addCookie(cookie);
				}
				out.println("<script>alert('Logout Success !!');setTimeout(\"location='/login'\",1000);</script>");
			}
			else{
				
				try
				{
					System.out.println("Not logged in. Redirecting...");
					out.println("<script>alert('Please Login First !');setTimeout(\"location='/login'\",1000);</script>");
				}
				catch(Exception ex)
				{
				}
			}
			return;
		}
		
		if(cookies.containsKey("login") && cookies.get("login").equals("true"))
		{
			String npass = request.getParameter("npass");
			if(!(npass == null) && npass.equals(request.getParameter("commit")))
			{
				String user = request.getParameter("username");
				
				db.changePassword(user, npass);
				
				out.println("<script>alert('New password saved.');setTimeout(\"location='/login'\",1000);</script>");
			}
			else
			{
				out.println("<script>alert('New password and commit doesn't match !');setTimeout(\"location='/frontpage'\",1000);</script>");
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
		finishResponse(out,response);
	}
}
