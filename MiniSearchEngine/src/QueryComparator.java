import java.util.Comparator;


public class QueryComparator implements Comparator<Result>
{

	public int compare(Result a, Result b) 
	{
		
		int result = 0;
		
		if(a.getCounter() > b.getCounter()) 
		{
			result = -1;
		}
		else if(b.getCounter() > a.getCounter()) 
		{
			result = 1;
		}
		
		return result;
	}

}
