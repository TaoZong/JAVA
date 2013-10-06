
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;


public class QueryBuilder 
{
	
	private Query query;
	private String queryString;
	
	QueryBuilder(String querys) 
	{
		query = new Query();
		queryString = querys;
	}
	
	public Query getQuery() 
	{
		return query;
	}
	
	public void build() 
	{
				
		String temp1 = queryString.replaceAll("[^A-Za-z0-9 ]", "");
		String[] temp2 = temp1.split(" ");
		HashSet<String> tokenSet = new HashSet<String>(); 
				
		for(String token: temp2)
		{		
			if(!token.equals("")) 
			{					
				String temp = token.toLowerCase();
				tokenSet.add(temp);
					
			}
		}
				
		query.addQuery(tokenSet);
						
	
	}
	
}
