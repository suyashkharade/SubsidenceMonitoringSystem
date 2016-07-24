package phaseunwrapp;

import java.io.IOException;
import java.util.StringTokenizer;


import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class Map extends Mapper<LongWritable,Text,LongWritable,DoubleWritable>{
	
	
	/* You need to care about this width and height while displaying cropped or any other*/
	static int Fh=-217;
	static int Fv=-6;
	static int border=20;
	
	 /*Use this for ful scene and comment above and below*/
	//int width=4900;
	//int height=26000;
	
	static int width = 4900-Math.abs(Fh)-border*2;//s 20 20 is border
	static int height =26000-Math.abs(Fv)-border*2;// 1738
	
	
	static int count=0; 
	static long keys[]=new long[width];
	static double val[]=new double[width];
	//static double outval[]=new double[width];
	
	public void compute()
	{
		double diff;
	 for(int i=1;i<width;i++)
	 {
		 diff=val[i]-val[i-1];
		 if(diff>Math.PI)
		 {
			 for(int j=i;j<width;j++)
			 {
			   val[j]-=2*Math.PI;
			 }	 
		 }
		 else if(diff< -Math.PI)
		 {
			 for(int j=i;j<width;j++)
			 {
			   val[j]+=2*Math.PI;
			 }
		 }
		 
		 
	 }
		
	}
	public void map(LongWritable key,Text value,
			Context context) throws IOException, InterruptedException {
	
	
		String line=value.toString();
		StringTokenizer st=new StringTokenizer(line);
		keys[count]=(Long.parseLong(st.nextToken()));

		val[count]=(Double.parseDouble(st.nextToken()));

		count++;
		if(count==width)
		{
			compute();
          	count=0;
          	for(int j=0;j<width;j++)
			 {
			   context.write(new LongWritable((keys[j]%width)*height+keys[j]/width),new DoubleWritable(val[j]));
			 }
		}	
	
	
	
	
	}

}
