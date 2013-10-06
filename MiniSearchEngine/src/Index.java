import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Set;


public class Index {
	
	private TreeMap<String, HashMap<String, ArrayList<String>>> index;
	
	Index() {
		index = new TreeMap<String, HashMap<String, ArrayList<String>>>();
	}
	
	/*
	 * add one token
	 */
	public void addToken(String token) {
			index.put(token, new HashMap<String, ArrayList<String>>());
	}
	
	/*
	 * add a word with path
	 */
	public void addFile(String token, String path, String i) {
		if(!index.containsKey(token)){
			addToken(token);
		}
		
		HashMap<String, ArrayList<String>> fileMap = index.get(token);
		
		if(!fileMap.containsKey(path)) {
			fileMap.put(path, new ArrayList<String>());
		}
		
		ArrayList<String> list = fileMap.get(path);
		list.add(i);

	}
	
	public Set<String> getToken() {
		return index.keySet();
	}
	
	public String[] getPosition(String token, String path) {
		HashMap<String, ArrayList<String>> map = index.get(token);
		ArrayList<String> list = map.get(path);
		return list.toArray(new String[0]);
	}
	
	public String[] getPath(String token) {
		HashMap<String, ArrayList<String>> map = index.get(token);
		Set<String> paths = map.keySet();
		return paths.toArray(new String[0]);
	}
	
	public TreeMap<String, HashMap<String, ArrayList<String>>> getTree() {
		return index;
	}
	
	/*
	 * output one token
	 */
	public String outputToken(String token) {
		String output = "";
		
		if(!index.containsKey(token)) {
			System.out.println("Target token dosen't exist!");
		}
		else {
			output += token + "\r\n";
			for(String file: getPath(token)) {
				for(String position: getPosition(token, file)) {
					output += "(" + file + " " + position + ")" + "\r\n";
				}
			}
		}
		return output; 
	}
	
	/*
	 * output whole index
	 */
	public String outputAll() {
		String output = "";
		
		Set<String> tokens = index.keySet();
		for(String token: tokens) {
			output += outputToken(token) + "\r\n";
		}
		
		return output;
	}
	
	
}
