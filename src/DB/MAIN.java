package DB;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;


public class MAIN
{

	static JPanel jp2=new JPanel();
	
	public static void main(String[] args) 
	{
		try
		{
		
		JFrame frame=new JFrame();
		JPanel jp1=new JPanel();		
		JPanel jp3=new JPanel();
		JScrollPane sp=new JScrollPane();
		JTabbedPane jtp=new JTabbedPane();
		
		JButton bstart = new JButton();
		JButton bstop = new JButton();
		JButton brefresh = new JButton();
		bstart.setBounds(50,100,95,30);
		bstop.setBounds(50,100,95,30);
		brefresh.setBounds(50,100,95,30);
        bstart.setText("START");
        bstop.setText("STOP");
        brefresh.setText("GET GRAPH");
		
		
		MySQLMS monitorService=MySQLMS.getinstance();
		
		Thread t=new Thread(monitorService);
		
		frame.setTitle("DATABASE_MONITOR");
		
		
		bstart.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				t.start();
				System.out.println("*********STARTED***********");
			}
		});
		
		bstop.addActionListener(new ActionListener() 
		{
			
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				
				t.stop();
				System.out.println("*********STOPPED***********");
				
			}
		});
		
		brefresh.addActionListener(new ActionListener() 
		{
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				jtp.removeAll();
				try 
				{
					jp2=monitorService.DrawGraph();
					System.out.println("*********GRAPH GENERATED***********");
					
					frame.getContentPane().add(jtp);
					frame.setLayout(new GridLayout(1,4));
					
					jtp.addTab("Monitor", jp1);
					jtp.addTab("Graph Tab",jp2);
					jtp.addTab("Analytics", jp3);
					jtp.setSelectedIndex(1);
					
					frame.pack();
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setVisible(true);
					
				} catch (Exception e1) 
				{
					System.out.println(e);
				}
			}
		});
		
		
		jp1.add(bstart);
		jp1.add(bstop);
		jp1.add(brefresh);
		
		frame.getContentPane().add(jtp);
		frame.setLayout(new GridLayout(1,4));
		
		//sp=monitorService.DoAnalytics();
		jp3.add(sp);
		
		jtp.addTab("Monitor Tab",jp1);
		jtp.addTab("Graph Tab",jp2);
		jtp.addTab("Analytics Tab",jp3);
		
		
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	catch(Exception e)
	{
		System.out.println("Inside main catch");
		System.out.println(e);
	}
		
	}

}
