package goldstein;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.Mapper.Context;

public class Combiner extends Reducer<LongWritable, Text, LongWritable,Text > {
static int width=4900-6;
Text txtkey;

protected void setup(Context context) throws IOException, InterruptedException {
txtkey=new Text();
count=0;
}
long count;
protected void closeup(Context context) throws IOException, InterruptedException {
System.out.println("Combiner Count="+count);
}
	 public void reduce(LongWritable key, Iterable<Text> values,
				Context context) throws IOException, InterruptedException {
	// replace KeyType with the real type of your key
	   //txtkey.set(key.get()/width+"\t"+key.get()%width);
		 context.write(key,values.iterator().next());

		//while (values.iterator().hasNext()) {
			// replace ValueType with the real type of your value
	count++;
			// process value
		//}
	}

}
