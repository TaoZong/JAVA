
import java.util.HashMap;
import java.util.LinkedList;


public class SearchResult 
{

	private HashMap<String, LinkedList<Result>> result;
	
	SearchResult() 
	{
		result = new HashMap<String, LinkedList<Result>>();
	}
	
	public void addResult(String token, LinkedList<Result> list) 
	{
		
		if(!result.containsKey(token)) 
		{
			result.put(token, new LinkedList<Result>());
		}
		
		LinkedList<Result> tempList = result.get(token);
		tempList.addAll(list);
	}
	
	public int numOfRusult() 
	{
		return result.size();
	}
	
	public String toString() 
	{
		String output = "";
		
		for(String token: result.keySet()) 
		{
			for(Result path: result.get(token)) 
			{
				output += path.getPath() + " ";
			}
			//output += "\r\n";
		}
		
		return output;
	}
	
}
