package DB;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.junit.Test;

public class MySQLMSTest 
{
	private Connection connect =null;
	private Statement statement=null;
	private ResultSet resultSet=null;
	
	HashMap<String, ClassData> h=new HashMap<String, ClassData>();
	HashMap<String, ClassData> k=new HashMap<String, ClassData>();
	
	
	 MySQLMS ms=new MySQLMS();
	
	
	JPanel jp=new JPanel();
	JScrollPane sp=new JScrollPane();
	
	@Test()
	public void testCollection() 
	{
		Exception ob=null;
		try
		{
			h=ms.Collectiondata();
			assertTrue(h.containsKey("Uptime"));
			assertTrue(h.containsKey("Bytes_sent"));
			assertTrue(h.containsKey("Bytes_received"));
			assertTrue(h.containsKey("Slow_queries"));
			assertNotNull(h);
		}
		catch(Exception e)
		{
			ob=e;
		}
		
		assertNull("got an exception"+ob, ob);
	}

	@Test
	public void testStorenull() 
	{
		try
		{
			ms.Store(null);
			fail("not working");
			
		}
		catch(Exception e)
		{
			System.out.println(e);
			assertTrue(e instanceof NullPointerException);
			System.out.println("success");
		}
	}
	
	@Test
	public void testStore()
	{
		long beforeinsert=0;
		long afterinsert=0;
		try
		{
			Class.forName("com.mysql.jdbc.Driver"); // to connect to mysql driver
			connect=DriverManager.getConnection("jdbc:mysql://localhost:3306/singletondb", "root", "root"); // to connect to singletondb
			statement=connect.createStatement();
			resultSet=statement.executeQuery("select count(*) as c from data");
			
			while(resultSet.next())
			{
				beforeinsert=resultSet.getLong(1);
			}
			
			System.out.println("beforeresult="+beforeinsert);
			h=ms.Collectiondata();
			ms.Store(h);
			
			resultSet=statement.executeQuery("select count(*) as c from data");
			
			while(resultSet.next())
			{
				afterinsert=resultSet.getLong(1);
			}
			
			System.out.println("afterresult="+afterinsert);
			assertTrue(afterinsert>beforeinsert);
		}
		catch(Exception e)
		{
			System.out.println("im inside testing sotre catch");
			System.out.println(e);
			
			assertTrue(e instanceof SQLException);
			System.out.println("success");
		}
	}
	
	@Test
	public void testDrawGraph() 
	{
		try
		{
			jp=null;
			
			JPanel result=ms.DrawGraph();
			
			System.out.println("COLOUR="+result.getBackground());
			
			System.out.println("graph is="+result);
			
			
			assertNotNull(result);
//			assertNotEquals(jp, result);
		}
		catch(Exception e)
		{
			System.out.println("im inside testing testdraw catch");
			System.out.println(e);
			
			if(e instanceof SQLException)
			{
				assertTrue(true);
				System.out.println("Sql exception handled successfully in testgraph");
			}
		}
	}

	@Test
	public void testDoAnalytics() 
	{
		try
		{
			sp=null;
			
			JScrollPane result=ms.DoAnalytics();
			System.out.println("Analytics result"+result);
			assertNotNull(result);

//			assertNotEquals(sp,result);
			
		}
		catch(Exception e)
		{
			System.out.println(e);
			if(e instanceof SQLException)
			{
				assertTrue(true);
				System.out.println("SQLException handled successfully in testgraph");
			}
			if(e instanceof ClassNotFoundException)
			{
				assertTrue(true);
				System.out.println("ClassNotFoundException handled successfully in testgraph");
			}
		}	
	}
	
}
