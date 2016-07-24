package look;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.NLineInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;



public class Multilook_Driver {

	static Configuration conf=new Configuration();
	static Job job;
	static int multilookL;
	static int multilookP;
	/* You need to care about this width and height while displaying cropped or any other*/
	static int Fh=-6;//212
	static int Fv=-208;//7
	static int border=0;
	
	 /*Use this for ful scene and comment above and below*/
	//int width=4900;
	//int height=26000;
	
	static int width = 4900-Math.abs(Fh)-border*2;//s 20 20 is border
	static int height =26000-Math.abs(Fv)-border*2;// 1738
	//static int height=5158;
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		
	
		job=new Job(conf,"multilook");	
		
		//1
		job.setJarByClass(Multilook_Driver.class);
	    //job.setMapperClass(Map1.class);
	   job.setReducerClass(Reduce.class);
	   
     //job.setNumReduceTasks(0);
	   
	    job.setOutputKeyClass(Text.class);
	   job.setOutputValueClass(Text.class);
 
	   job.setMapOutputKeyClass(LongWritable.class);
	   job.setMapOutputValueClass(DoubleWritable.class);
	    
	   multilookL=5;
	   multilookP=1;
    int noOfMappers=10;
	   //   	job.getConfiguration().setInt("interferrogram.Interferogram_Driver.multilookL",5);
		job.getConfiguration().setInt("mapreduce.input.lineinputformat.linespermap",width*multilookL*(height/(noOfMappers*multilookL)));
		job.setInputFormatClass(NLineInputFormat.class);
	   	job.setOutputFormatClass(TextOutputFormat.class);
		
		MultipleInputs.addInputPath(job, new Path(args[0]), NLineInputFormat.class,Map1.class);
        //MultipleInputs.addInputPath(job, new Path(args[2]), NLineInputFormat.class,Map3.class);
	    //MultipleInputs.addInputPath(job, new Path(args[3]), NLineInputFormat.class,Map4.class);
        FileOutputFormat.setOutputPath( job, new Path(args[1]));
        job.waitForCompletion(true);
       
     }

 }


