package ers_read;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileAsBinaryOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat; ;
public class Img1 {

	
	
	static Configuration conf=new Configuration();	
	public static void main(String[] args) throws Exception{
	
		
		 
		 
		 
		
		conf.set("mapred.child.java.opts","-Xmx1024m");
		
		
				
		Job job=new Job(conf,"Img");
		job.setJarByClass(Img1.class);
		
	    
		job.setMapperClass(Map.class);
	   job.setReducerClass(Reduce.class);
	   
	   // job.setNumReduceTasks(0);
	   
	    job.setOutputKeyClass(Text.class);
	   job.setOutputValueClass(DoubleWritable.class);
 
    	   job.setMapOutputKeyClass(LongWritable.class);
        job.setMapOutputValueClass(DoubleWritable.class);

	   job.setInputFormatClass(PixelInputFormat.class);
		
		job.setOutputFormatClass(TextOutputFormat.class);
	 job.getConfiguration().setFloat("MINIMUM_MAGNITUDE", 100000000f);
	 job.getConfiguration().setFloat("MAXIMUM_MAGNITUDE", 0.0f);
	
	 job.getConfiguration().setFloat("MINIMUM_PHASE", 100000000f);
	 job.getConfiguration().setFloat("MAXIMUM_PHASE", 0.0f);
	
	 FileInputFormat.addInputPath(job,new Path(args[0]));
		
       FileOutputFormat.setOutputPath( job, new Path(args[1]));
//job.getConfiguration().setBoolean("mapreduce.map.output.compress", true);
//job.getConfiguration().setBoolean("mapreduce.output.fileoutputformat.compress", false);
//job.getConfiguration().set("mapred.map.output.compression.codec", "org.apache.hadoop.io.compress.LzoCodec");



       job.waitForCompletion(true);
       
   

		}

}