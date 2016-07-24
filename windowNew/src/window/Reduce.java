package window;

import java.io.IOException;

import org.apache.commons.logging.impl.Log4JLogger;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.Mapper.Context;

public class Reduce extends
		Reducer<LongWritable, Text, Text, Text> {
	static long count = 0;
	Text txtKey;
    Text master_value;
    Text total_value; 
	/* Setup method is just to know space between two windows */
	@Override
	protected void setup(Context context) throws IOException,
			InterruptedException {
   txtKey=new Text();
   master_value=new Text();
   total_value=new Text();
   
	}@Override
	protected void cleanup(Context context)
			throws IOException, InterruptedException {
	   System.out.println("Count"+count);
			
	}
	public void reduce(LongWritable key, Iterable<Text> values,
			Context context) throws IOException, InterruptedException {

			
		if(key.get()%2==0)
			master_value.set(values.iterator().next());
		else
		{
			key.set(key.get()/2);
		    txtKey.set(""+key.get());
		    total_value.set(master_value+"\t"+values.iterator().next());
			context.write(txtKey,total_value);
		}	
		 count++;
	}

}
