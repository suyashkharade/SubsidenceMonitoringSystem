package fft;


import org.apache.hadoop.conf.Configuration;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Job;

import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.NLineInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;



public class driver {
			
	public static void main(String[] args) throws Exception{
	
		Configuration conf=new Configuration();
		/*Path strFilePath =new Path("/app/hadoop/tmp/ERSwindows");
		
		FSDataOutputStream dos;
		 FileSystem fs=FileSystem.get(conf);
		 dos=fs.create(strFilePath, true);
		dos.close();
		*/
		Job job=new Job(conf,"fft");
		
		//1
		job.setJarByClass(driver.class);
	    job.setMapperClass(Map1.class);
	   job.setReducerClass(Reduce.class);
	   
     //job.setNumReduceTasks(0);
	   
	    job.setOutputKeyClass(LongWritable.class);
	   job.setOutputValueClass(DoubleWritable.class);
 
	   job.setMapOutputKeyClass(LongWritable.class);
	   job.setMapOutputValueClass(DoubleWritable.class);
	   
	    
	   /* IMP:this linespermap factor need to be set carefully as no-of windows varies */ 
		job.getConfiguration().setInt("mapreduce.input.lineinputformat.linespermap", 64*64*100);
		job.setOutputFormatClass(TextOutputFormat.class);
		
		//FileInputFormat.addInputPath(job, new Path(args[0]));
		MultipleInputs.addInputPath(job, new Path(args[1]), NLineInputFormat.class,Map2.class);
		MultipleInputs.addInputPath(job, new Path(args[0]), NLineInputFormat.class,Map1.class);
		
		//FileInputFormat.addInputPath(job, null);
        FileOutputFormat.setOutputPath( job, new Path(args[2]));
        job.waitForCompletion(true);
       
        }

}
/*
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapreduce.Job;



public class driver {

	public static void main(String[] args) {
		
		Path magFile=new Path("/app/hadoop/tmp/Magnitude12.txt-r-00000");
		Configuration conf=new Configuration();
		Job job=new Job(conf,"Img");
		job.setJarByClass(driver.class);
		
	    
		job.setMapperClass(map.class);
	    job.setReducerClass(Reduce.class);

		// TODO: specify output types
		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(IntWritable.class);

		// TODO: specify input and output DIRECTORIES (not files)
		conf.setInputFormat(new Path("src"));
		conf.setOutputPath(new Path("out"));

		// TODO: specify a mapper
		conf.setMapperClass(org.apache.hadoop.mapred.lib.IdentityMapper.class);

		// TODO: specify a reducer
		conf.setReducerClass(org.apache.hadoop.mapred.lib.IdentityReducer.class);

		client.setConf(conf);
		try {
			JobClient.runJob(conf);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
*/