package display;


import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.NLineInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
public class DisplayDriver {

	
	
	public static void main(String[] args) throws  IOException, ClassNotFoundException, InterruptedException {
		
		
		int Fh=-6;
		int Fv=-208;
		int border=0;
		
		/*Use this for ful scene and comment above and below*/
//		  int noOfCols=4900;
		//  int noOfLines=26000;
		int noOfCols=4900;
		  int noOfLines=26000;
		
		/* Set mutilook factors here*/
		int multilookL=5;
		int multilookP=1;

		int width = (noOfCols-Math.abs(Fh)-border*2)/multilookP;// 20 20 is border  is due to cropping error need to figure out in crop)
		int height =(noOfLines-Math.abs(Fv)-border*2)/multilookL;// 1738
	//	int width=4894/multilookL;
		//int height=5158/multilookP;
		 
		Configuration conf=new Configuration();	
			 
		 
		
		conf.set("mapred.child.java.opts","-Xmx500m");
		
		Path strFilePath =new Path("/app/hadoop/tmp/MinMaxDisplay_temp.txt");
		
		 FSDataOutputStream dos;
		 FileSystem fs=FileSystem.get(conf);
		 dos=fs.create(strFilePath, true);

	    
		 //min Magnitude
		 dos.writeDouble((double)100000000);
		 //max Magnitude
		 dos.writeDouble((double)0.0);
		 
		 
		 dos.close();
		 fs.close();
		
		Job job=new Job(conf,"Display");
		job.setJarByClass(DisplayDriver.class);
		
	    
	job.setMapperClass(Map.class);
	  job.setReducerClass(Reduce.class);
	   
	    //job.setNumReduceTasks(2);
	   
	    job.setOutputKeyClass(DoubleWritable.class);
	   job.setOutputValueClass(NullWritable.class);
 
    	   job.setMapOutputKeyClass(LongWritable.class);
        job.setMapOutputValueClass(DoubleWritable.class);

	   job.setInputFormatClass(NLineInputFormat.class);
	
		job.setOutputFormatClass(TextOutputFormat.class);
	
		job.getConfiguration().setInt("mapreduce.input.lineinputformat.linespermap",(width*height/10));
		
		job.getConfiguration().setFloat("MINIMUM", 100000000f);
		 job.getConfiguration().setFloat("MAXIMUM", 0.0f);
		
		FileInputFormat.addInputPath(job,new Path(args[0]));
		
       FileOutputFormat.setOutputPath( job, new Path(args[1]));

       job.waitForCompletion(true);
        
   

		}

}