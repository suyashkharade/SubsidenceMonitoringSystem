package window;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;

public class Driver {

	public static void main(String[] args) throws Exception {

		Configuration conf = new Configuration();

		conf.setLong("cnt", 0);

		Job job = new Job(conf, "Windowing");
		job.setJarByClass(Driver.class);
		job.setMapperClass(Map1.class);
		job.setMapperClass(Map2.class);

		job.setReducerClass(Reduce.class);

		// job.setNumReduceTasks(0);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		job.setCombinerClass(Reducer.class);

		job.setMapOutputKeyClass(LongWritable.class);
		job.setMapOutputValueClass(Text.class);

		job.setInputFormatClass(TextInputFormat.class);

		job.setOutputFormatClass(TextOutputFormat.class);

		//FileInputFormat.addInputPath(job, new Path(args[0]));
		MultipleInputs.addInputPath(job, new Path(args[0]),TextInputFormat.class,Map1.class);
		MultipleInputs.addInputPath(job, new Path(args[1]),TextInputFormat.class,Map2.class);
	
		FileOutputFormat.setOutputPath(job, new Path(args[2]));

		job.waitForCompletion(true);

	}

}
