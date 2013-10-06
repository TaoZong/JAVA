import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.ServletHandler;

public class Driver 
{
	
	// default server port number
	private static int PORT = 8080;
	
	public static Index index = new Index();
	
	/* main(String[])
	 * Starts up web server and servlet handler on specified port.
	 */
	public static void main(String[] args)
	{
		// create server on specified port
		Server server = new Server(PORT);
		
		//String seed = "http://www.cs.usfca.edu/~sjengle/courses/fall2010/cs212/lab4/test.html";
		String seed = "";
		if(args[0].equals("-w")){
			try{
				seed = args[1];
			}
			catch(ArrayIndexOutOfBoundsException e) {
				System.out.println("Argusments wrong, dirpath can not found.");
				return;
			}
		}
		
		WebCrawler crawler = new WebCrawler(seed, 10);
		
		// create servlet handler
		ServletHandler handler = new ServletHandler();
		server.setHandler(handler);

		// setup servlet mappings
		handler.addServletWithMapping("LoginServlet", "/login");
		handler.addServletWithMapping("RegisterServlet", "/register");
		handler.addServletWithMapping("SearchServlet", "/search");
		handler.addServletWithMapping("FrontServlet", "/frontpage");
		handler.addServletWithMapping("ModifyUserServlet", "/modifyuser");
		handler.addServletWithMapping("HistoryServlet","/history");
		handler.addServletWithMapping("VisitedPageServlet", "/visited");
		handler.addServletWithMapping("AddSeedServlet", "/addseed");
		
		// try/catch exceptions when starting/running server
		try 
		{
			// start server
			server.start();
			System.out.println("Server started on port " + PORT + ".");
			
			// wait for server to exit
			server.join();
			System.out.println("Exiting...");
		}
		catch(Exception ex)
		{
			// if unable to start or run server, immediately exit
			System.out.println("Interrupted while running server.");
			System.exit(-1);
		}
	}
}