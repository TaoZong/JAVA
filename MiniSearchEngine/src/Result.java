
public class Result 
{
	
	private String path;
	private int counter;
	
	public Result(String path, int counter) 
	{
		this.counter = counter;
		this.path = path;
	}
	
	public int getCounter() 
	{
		return counter;
	}
	
	public String getPath() 
	{
		return path;
	}
	
	public void counterAdd(int add) 
	{
		counter += add;
	}

}
