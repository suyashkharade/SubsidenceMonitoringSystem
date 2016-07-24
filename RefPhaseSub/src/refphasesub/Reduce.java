package refphasesub;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;

public class Reduce extends Reducer<LongWritable,Text,Text,Text> {
 static long count=0;
 static Text txtkey;
 static int Fv=-208;
 static int Fh=-6;
 static int width;
 protected void setup(Context context) throws IOException, InterruptedException {
	width=4900-Math.abs(Fh);
	 txtkey=new Text();
System.out.println("Total count="+count);	 
 }
	public void reduce(LongWritable key, Iterable<Text> values,
				Context context) throws IOException, InterruptedException {

		txtkey.set(""+key.get()/width+"\t"+key.get()%width);
		context.write(txtkey,values.iterator().next());
        count++;
	 }
	
}
