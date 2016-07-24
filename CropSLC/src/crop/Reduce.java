package crop;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;

public class Reduce extends Reducer<LongWritable,Text,Text,Text> {
 static long count=0;
 static Text txtkey;
 static int Fv=-217;
 static int Fh=-6;
 static int width;
 Text Combined_value;
 
 protected void setup(Context context) throws IOException, InterruptedException {
	width=4900-Math.abs(Fh);
	 txtkey=new Text();
	 Combined_value=new Text();
System.out.println("Total count="+count);	 
 }
	public void reduce(LongWritable key, Iterable<Text> values,
				Context context) throws IOException, InterruptedException {
 
		 if(key.get()%2==0)
		 {
		   Combined_value.set(values.iterator().next());	 
		 } 
		 else
		 {	
			 key.set(key.get()/2);
		   txtkey.set(""+key.get()/width+"\t"+key.get()%width);
		   
		   Combined_value.set(Combined_value+"\t"+values.iterator().next());
		   context.write(txtkey,Combined_value);
         count++;
	 
		 }
		 }

}
