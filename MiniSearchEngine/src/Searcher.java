import java.util.Collections;
import java.util.LinkedList;


public class Searcher 
{
	
	private SearchResult result;
	private Query query;
	private Index index;
	
	Searcher(Query query, Index index) 
	{
		result = new SearchResult();
		this.index = index;
		this.query = query;
	}
	
	public SearchResult getResult() 
	{
		return result;
	}
	
	/*
	 * Main searcher method. Already implemented partial search. 
	 */
	public void search() 
	{
		for(int i = 0; i < query.numOfQuery(); i++) 
		{
			
			LinkedList<Result> tempList = new LinkedList<Result>();
			
			//build templist for each querytoken 
			for (String queryToken: query.getQueryToken(i)) 
			{
				if(index.getToken().contains(queryToken)) 
				{
										
					for(String path: index.getPath(queryToken)) 
					{
						int counter = 0;
						counter += index.getPosition(queryToken, path).length;
						tempList.add(new Result(path, counter));
					}
					
				}
	
				String partialKey = queryToken;
				try
				{
					while(index.getTree().higherKey(partialKey).startsWith(queryToken)) 
					{
						for(String path: index.getPath(index.getTree().higherKey(partialKey))) 
						{
							int counter = 0;
							counter += index.getPosition(queryToken, path).length;
							tempList.add(new Result(path, counter));
						}
						partialKey = index.getTree().higherKey(partialKey);
					}
				}
				catch(NullPointerException np) 
				{
					
				}

			}
			
			//add counter of the same file
			for(int j = 0; j < tempList.size(); j++)
			{
				for(int k = tempList.size() - 1 ; k > j ; k--) 
				{
					if(tempList.get(j).getPath().equals(tempList.get(k).getPath())) 
					{
						tempList.get(j).counterAdd(tempList.get(k).getCounter());
						tempList.remove(k);
					}
				}		
			}
			
			//sort query
			Collections.sort(tempList, new QueryComparator());
			
			String tempString = "";
			
			for(String token: query.getQueryToken(i)) 
			{
				tempString += token + " ";
			}
			
			result.addResult(tempString, tempList);
		}
	}
	
}
