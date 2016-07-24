package phaseunwrapp;

import java.io.IOException;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;

import org.apache.hadoop.mapreduce.Reducer;

public class Reduce extends  Reducer<LongWritable,DoubleWritable,LongWritable,DoubleWritable> {
	/* You need to care about this width and height while displaying cropped or any other*/
	static int Fh=-217;
	static int Fv=-6;
	static int border=20;
	
	 /*Use this for ful scene and comment above and below*/
	//int width=4900;
	//int height=26000;
	
	static int width = 4900-Math.abs(Fh)-border*2;// 20 20 is border 
	static int height =26000-Math.abs(Fv)-border*2;// 1738
	
	
	static int count=0; 
	static long keys[]=new long[height];
	static double val[]=new double[height];
	
	public void compute()
	{
		double diff;
	 for(int i=1;i<height;i++)
	 {
		 diff=val[i]-val[i-1];
		 if(diff>Math.PI)
		 {
			 for(int j=i;j<height;j++)
			 {
			   val[j]-=2*Math.PI;
			 }	 
		 }
		 else if(diff< -Math.PI)
		 {
			 for(int j=i;j<height;j++)
			 {
			   val[j]+=2*Math.PI;
			 }
		 }
		 
		 
	 }
		
	}
	
	  public void reduce(LongWritable key, Iterable<DoubleWritable> values,
				Context context) throws IOException, InterruptedException {
		  
		  keys[count]=key.get();

			val[count]=values.iterator().next().get();

			count++;
			if(count==height)
			{
				compute();
	          	count=0;
	          	for(int j=0;j<height;j++)
				 {
				   context.write(new LongWritable((keys[j]%height)*width+keys[j]/height),new DoubleWritable(val[j]));
				 }
			}	
		

	
	}

}
