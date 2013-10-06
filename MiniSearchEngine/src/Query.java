import java.util.ArrayList;
import java.util.HashSet;


public class Query 
{

	private ArrayList<HashSet<String>> query;
	
	Query() 
	{
		query = new ArrayList<HashSet<String>>();
	}
	
	public void addQuery(String[] querys) 
	{
		HashSet<String> tokenSet = new HashSet<String>();
		for(String token: querys) 
		{
			tokenSet.add(token);
		}
		query.add(tokenSet);
	}
	
	public void addQuery(HashSet<String> tokenSet) 
	{
		query.add(tokenSet);
	}
	
	public int numOfQuery() 
	{
		return query.size();
	}
	
	public HashSet<String> getQueryToken(int i) 
	{
		return query.get(i);
	}
	
	public String toString() 
	{
		String output = "";
		
		for(int i = 0; i < query.size(); i++) 
		{
			HashSet<String> tokenSet = new HashSet<String>();
			tokenSet = query.get(i);
			for(String token: tokenSet) 
			{
				output += token + " ";
			}
			output += "\n";
		}
		
		return output;
	}
	
	
}
