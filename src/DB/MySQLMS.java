package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.jdbc.JDBCCategoryDataset;


public class MySQLMS implements MonitorService,Runnable
{
	private Connection connect =null;
	private Statement statement=null;
	private ResultSet resultSet=null;
	private PreparedStatement preparedStatement=null;
	
	private static MySQLMS obj=null;
	
	public static MySQLMS getinstance()
	{
		if(obj == null)
		{
			obj=new MySQLMS();
		}
		return obj;
	}
	
	public void run()
	{
		while(true)
		{
			synchronized (obj) 
			{	
		try 
		{
			obj.wait(5000);
			Store(Collectiondata());
		} 
		catch (Exception e) 
		{
			System.out.println(e);
		}
			}
		}
	}
	
	public HashMap<String,ClassData> Collectiondata() throws Exception
	{
		try
		{
			
			HashMap<String,ClassData> map=new HashMap<String,ClassData>();
			System.out.println("Im implementing Collection!");
			
			Class.forName("com.mysql.jdbc.Driver"); // to connect to mysql driver
			
			connect=DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql", "root", "root"); //to connect to specific database using mysql credentials
			statement=connect.createStatement();
			
			int count=0;
			String[] metrics= {"Uptime","Bytes_sent","Bytes_received","Slow_queries"}; // queries chunks
			
			while(count<metrics.length)
			{
				resultSet=statement.executeQuery("show global status like '"+metrics[count]+"'");
				long time=System.currentTimeMillis();
				while(resultSet.next())
				{
					System.out.println("inside result");
					String s=resultSet.getString("Variable_name");
					Double d=resultSet.getDouble("Value");
					System.out.println(time);
					System.out.println(s);
					System.out.println(d);
					System.out.println("***********");
					ClassData c=new ClassData(time,d,s);
					map.put(s,c);
					count+=1;
					
				}
			}
			connect.close();
			return map;
		}
		catch(Exception e) 
		{
			System.out.println("going inside collection catch!");
			System.out.println(e);
			throw e;
		}
	}
	
	public void Store(HashMap<String,ClassData> h) throws Exception
	{
		try
		{	
			
			System.out.println("Im implementing Store!");
			int count=0;
			String[] metrics= {"Uptime","Bytes_sent","Bytes_received","Slow_queries"};
			Class.forName("com.mysql.jdbc.Driver"); // to connect to mysql driver
			connect=DriverManager.getConnection("jdbc:mysql://localhost:3306/singletondb", "root", "root"); // to connect to singletondb
			while(count<metrics.length)
			{
				System.out.println("***********");
				System.out.println(count);
				ClassData c=h.get(metrics[count]);
				System.out.println(c.time);
				System.out.println(c.s);
				System.out.println(c.datapoint);
				preparedStatement=connect.prepareStatement("insert into singletondb.DATA values (?,?,?,?)");
				preparedStatement.setString(1,c.s);
				preparedStatement.setLong(2,c.time);
				preparedStatement.setDouble(3,c.datapoint);
				preparedStatement.setString(4,"MYSQL");
				preparedStatement.executeUpdate();
				count+=1;
			}
			System.out.println("/***********/");
			connect.close();
		}
		catch(Exception e)
		{
			System.out.println("inside store catch");
			throw e;
		}
	}
	
	public JPanel DrawGraph() throws Exception
	{
		JPanel jp2=new JPanel();
		try
		{
			System.out.println("Im implementing drawGraph!");
			JFreeChart upchart=ChartFactory.createLineChart("UP TIME GRAPH","Time","Metric",getDataFromDB("Uptime"),PlotOrientation.VERTICAL,true, true, false);
			JFreeChart Bsentchart=ChartFactory.createLineChart("BYTES SENT GRAPH","Time","Metric",getDataFromDB("Bytes_sent"),PlotOrientation.VERTICAL,true, true, false);
			JFreeChart Breceivedchart=ChartFactory.createLineChart("BYTES RECEIVED GRAPH","Time","Metric",getDataFromDB("Bytes_received"),PlotOrientation.VERTICAL,true, true, false);
			JFreeChart Slowchart=ChartFactory.createLineChart("SLOW QUERIES GRAPH","Time","Metric",getDataFromDB("slow_queries"),PlotOrientation.VERTICAL,true, true, false);
			
			ChartPanel upPanel = new ChartPanel( upchart );
			ChartPanel bsentPanel = new ChartPanel( Bsentchart );
			ChartPanel breceivedPanel = new ChartPanel( Breceivedchart );
			ChartPanel slowPanel = new ChartPanel( Slowchart );
			
			upPanel.setPreferredSize(new java.awt.Dimension( 550,350 ) );
		    bsentPanel.setPreferredSize(new java.awt.Dimension( 550,350 ) );
		    breceivedPanel.setPreferredSize(new java.awt.Dimension( 550,350 ) );
		    slowPanel.setPreferredSize(new java.awt.Dimension( 550,350 ) );
			
			
			jp2.add(upPanel);
			jp2.add(bsentPanel);
			jp2.add(breceivedPanel);
			jp2.add(slowPanel);
			return jp2;
		}
		catch(Exception e)
		{
			System.out.println("Im in draw graph catch");
			System.out.println(e);
			throw e;
		}
	}

	public JScrollPane DoAnalytics()
	{
		System.out.println("Im implementing doAnalytics!");
		 double diff;
		 double prev=52428880;
		 double MIN=Integer.MAX_VALUE;
		 double MAX=0;
		 double totalavg=0;
		 double AVG=0;
		 double totalres=53000000;
		 double lastres = 0;
		 double result;
		 double avalres=0;
		
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			connect=DriverManager.getConnection("jdbc:mysql://localhost:3306/singletondb", "root", "root"); // to connect to singletondb
			int flag=0;
			for(int offset=1;offset<=105120;offset+=288)
			{
				statement=connect.createStatement();
				resultSet=statement.executeQuery("select datapoint from hugedata LIMIT "+1*offset+",288");
				int i=1;
				AVG=0;
				if(flag==0)
				{
					resultSet.next();
					flag=1;
				}
				while(resultSet.next())
				{
					result=resultSet.getDouble("datapoint");
					diff=result-prev;
					System.out.println(diff);
					System.out.println(i);
					if(diff<MIN )
					{
						MIN=diff;
					}
					if(diff>MAX)
					{
						MAX=diff;
					}
					AVG+=diff;
					i++;
					prev=result;
				}
				AVG=AVG/288;
				System.out.println("************");
				System.out.println(offset);
				System.out.println("************");
				totalavg+=AVG;
			}
			totalavg=totalavg/365;
			totalavg*=288;
			MIN*=288;
			MAX*=288;
			resultSet=statement.executeQuery("select datapoint from hugedata LIMIT 105119,1");
			while(resultSet.next())
			{
				lastres=resultSet.getDouble("datapoint");
			}
			avalres=totalres-lastres;
			System.out.println("************");
			MAX=avalres/MAX;
			totalavg=avalres/totalavg;
			MIN=avalres/MIN;
			System.out.println("MIN DAYS="+MAX);
			System.out.println("AVG DAYS="+totalavg);
			System.out.println("MAX DAYS="+MIN);
			System.out.println("************");
			connect.close();
			Double data[][]= {{MAX,totalavg,MIN}};
			String column[]= {"MIN DAYS","AVG DAYS","MAX DAYS"};
			JTable jt=new JTable(data,column);
			jt.setBounds(30,40,200,300);
			JScrollPane sp=new JScrollPane(jt);
			return sp;
		}
		catch(Exception e)
		{
			System.out.println("Inside analytics catch");
			System.out.println(e);
		}
		return null;
	}
	
	
	private JDBCCategoryDataset getDataFromDB(String param) throws Exception
	{
		try
		{
			JDBCCategoryDataset dataset = new JDBCCategoryDataset("jdbc:mysql://localhost:3306/singletondb","com.mysql.jdbc.Driver", "root","root");
	        dataset.executeQuery("select timeinms,datapoint from DATA where mname='"+param+"'");
	        return dataset;
		}
		catch(Exception e)
		{
			System.out.println("inside database catch");
			System.out.println(e);
			throw e;
		}
	}
}
