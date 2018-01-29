package DB;

import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

public interface MonitorService 
{
	public HashMap<String, ClassData> Collectiondata() throws Exception;
	
	public void Store(HashMap<String, ClassData> Hash) throws Exception;
	
	public JPanel DrawGraph() throws Exception;
	
	public JScrollPane DoAnalytics();
	
//	public void DrawAnalytics();
}
