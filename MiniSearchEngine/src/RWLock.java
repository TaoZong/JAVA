
public class RWLock 
{
	private static volatile int writer;
	private static volatile int reader;
	
	public RWLock()
	{
		reader = 0;
		writer = 0;
	}
	
	public synchronized void getWriteLock(){
		
		while(writer > 0 || reader > 0){
			try 
			{
				this.wait(12);
			} 
			catch (InterruptedException e) 
			{
				System.out.println("InterruptedException ");
			}
		}		
		writer++ ;		
	}
	
	public synchronized void releaseWriteLock(){
		if(writer>0)
		{
			writer--;
			this.notifyAll();
		}			
		
	}
	
	public synchronized void getReadLock(){
		
		while(writer != 0){
			try 
			{				
				this.wait();
			} 
			catch (InterruptedException e) 
			{
				System.out.println("IntreeuptedException ");
			}
		}
		reader++ ;
		
	}
	public synchronized void releaseReadLock()
	{
		
		reader-- ;
		this.notifyAll();
	}

}
