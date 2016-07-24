package goldstein;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.CombineFileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import com.sun.media.sound.FFT;


public class DriverGoldstein {

	static Configuration conf=new Configuration();
	static Job job;
 static int width=4900-6;
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		
		job=new Job(conf,"Goldstein");
		job.setJarByClass(DriverGoldstein.class);
		conf.set("mapred.child.java.opts","-Xmx1024m");
	    
		job.setMapperClass(Map.class);
	  job.setReducerClass(Reduce.class);
	   job.setCombinerClass(Combiner.class);
	   //job.setNumReduceTasks(0);
	   
	    job.setOutputKeyClass(Text.class);
	   job.setOutputValueClass(Text.class);
 
    	   job.setMapOutputKeyClass(LongWritable.class);
        job.setMapOutputValueClass(Text.class);

	   job.setInputFormatClass(NLineInputFormat.class);
		
		job.setOutputFormatClass(TextOutputFormat.class);
	
	job.getConfiguration().setInt("mapreduce.input.lineinputformat.linespermap",width*32);//496
	job.getConfiguration().setInt("mapreduce.input.lineinputformat.overlaplines",width*12);//depends on overlap gods
	
	FileInputFormat.addInputPath(job,new Path(args[0]));
		
       FileOutputFormat.setOutputPath( job, new Path(args[1]));

       job.waitForCompletion(true);
       
       
       //private CombineFileSplit split;

  
}

}
