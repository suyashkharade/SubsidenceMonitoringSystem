package CoregDoris;


import org.apache.hadoop.conf.Configuration;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;

import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class driver {
			static int linesperwin=512,pixelsperwin=128;
	public static void main(String[] args) throws Exception{
	
		Configuration conf=new Configuration();
		Job job=new Job(conf,"coreg");
		job.setJarByClass(driver.class);
	    job.setMapperClass(Map.class);
	    
	    job.setNumReduceTasks(0);
	    
	    job.setInputFormatClass(org.apache.hadoop.mapreduce.lib.input.NLineInputFormat.class);
	    job.getConfiguration().setInt("mapreduce.input.lineinputformat.linespermap", linesperwin*pixelsperwin);
	    job.setOutputKeyClass(LongWritable.class);
	    job.setOutputValueClass(Text.class);
	    job.setMapOutputKeyClass(LongWritable.class);
	    job.setMapOutputValueClass(Text.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath( job, new Path(args[1]));
        job.waitForCompletion(true);
       
        }

}
