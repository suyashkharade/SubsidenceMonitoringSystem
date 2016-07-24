package readimg1;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileAsBinaryOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat; ;
public class Img1 {

	
	
	static Configuration conf=new Configuration();	
	public static void main(String[] args) throws Exception{
	
		
		 
		 
		 
		
		conf.set("mapred.child.java.opts","-Xmx500m");
		
		Path strFilePath =new Path("/app/hadoop/tmp/MinMax_Phase_Mag.txt");
		
		 FSDataOutputStream dos;
		 FileSystem fs=FileSystem.get(conf);
		 dos=fs.create(strFilePath, true);

	    
		 //min Magnitude
		 dos.writeDouble((double)100000000);
		 //max Magnitude
		 dos.writeDouble((double)0.0);
		 
		//min Phase
		 dos.writeDouble((double)100000000);
		 //max Phase
		 dos.writeDouble((double)0.0);
		 
		 dos.close();
		 fs.close();
		 fs.close();
		
		Job job=new Job(conf,"Img");
		job.setJarByClass(Img1.class);
		
	    
		job.setMapperClass(Map.class);
	    job.setReducerClass(Reduce.class);
	   
	    //job.setNumReduceTasks(2);
	   
	    job.setOutputKeyClass(DoubleWritable.class);
	   job.setOutputValueClass(NullWritable.class);
 
    	   job.setMapOutputKeyClass(LongWritable.class);
        job.setMapOutputValueClass(DoubleWritable.class);

	   job.setInputFormatClass(PixelInputFormat.class);
		
		job.setOutputFormatClass(SequenceFileAsBinaryOutputFormat.class);
	
	
		FileInputFormat.addInputPath(job,new Path(args[0]));
		
       FileOutputFormat.setOutputPath( job, new Path(args[1]));

       job.waitForCompletion(true);
        
   

		}

}