
public class IndexBuilder implements UrlSetAndLock
{

	//private Index index;
	
	IndexBuilder() 
	{
		//index = new Index();
	}
		
	/*
	 * add token and path to index using lock class 
	 */
	public void addFile(String temp, String url, String counter) 
	{
		builderLock.getWriteLock();
		Driver.index.addFile(temp, url, counter);
		builderLock.releaseWriteLock();
	}
		
	//public Index getIndex() {
		//return index;
	//}
	
	public void build(String url, String text) 
	{
		int counter = 0;

		String temp1 = text.replaceAll("[^A-Za-z0-9 ]", "");
		String[] temp2 = temp1.split(" ");

		for(String token: temp2)
		{
			if(!token.equals("")) 
			{
				counter ++;
				String temp = token.toLowerCase();
				addFile(temp, url, String.valueOf(counter));
			}
		}

	}

}
