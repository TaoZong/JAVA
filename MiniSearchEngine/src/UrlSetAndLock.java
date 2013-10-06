import java.util.HashSet;


public interface UrlSetAndLock 
{
	HashSet<String> urlSet = new HashSet<String>();
	
	RWLock linkLock = new RWLock();
	RWLock builderLock = new RWLock();
}
