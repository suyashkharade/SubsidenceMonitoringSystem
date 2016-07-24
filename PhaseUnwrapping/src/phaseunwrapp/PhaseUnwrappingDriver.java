package phaseunwrapp;
import org.apache.hadoop.conf.Configuration;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Reducer;

import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.NLineInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;



public class PhaseUnwrappingDriver {
			
	/* You need to care about this width and height while displaying cropped or any other*/
	static int Fh=-217;
	static int Fv=-6;
	static int border=20;
	
	 /*Use this for ful scene and comment above and below*/
	//int width=4900;
	//int height=26000;
	
	static int width = 4900-Math.abs(Fh)-border*2;// 20 20 is border 
	static int height =26000-Math.abs(Fv)-border*2;// 1738
	
	public static void main(String[] args) throws Exception{
	
		Configuration conf=new Configuration();
		Job job=new Job(conf,"phaseunwrap");
		
		//1
		job.setJarByClass(PhaseUnwrappingDriver.class);
	    job.setMapperClass(Map.class);
	    job.setReducerClass(Reduce.class);
	   
     //job.setNumReduceTasks(0);
	   
	    job.setOutputKeyClass(LongWritable.class);
	   job.setOutputValueClass(DoubleWritable.class);
 
	   job.setMapOutputKeyClass(LongWritable.class);
	   job.setMapOutputValueClass(DoubleWritable.class);
	   
	   job.setInputFormatClass(NLineInputFormat.class); 
	   /* IMP:this linespermap factor need to be set carefully as no-of windows varies */ 
		job.getConfiguration().setInt("mapreduce.input.lineinputformat.linespermap",width*1000);
		job.setOutputFormatClass(TextOutputFormat.class);
		
		FileInputFormat.addInputPath(job, new Path(args[0]));
	//	MultipleInputs.addInputPath(job, new Path(args[1]), NLineInputFormat.class,Map2.class);
		//MultipleInputs.addInputPath(job, new Path(args[0]), NLineInputFormat.class,Map1.class);
		
        FileOutputFormat.setOutputPath( job, new Path(args[1]));
        job.waitForCompletion(true);
       
        }

}
