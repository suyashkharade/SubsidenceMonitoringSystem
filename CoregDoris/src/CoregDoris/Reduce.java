
package CoregDoris;

import java.io.IOException;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.*;

public class Reduce extends Reducer<LongWritable,DoubleWritable,LongWritable,DoubleWritable> {
protected void setup(Context context) throws IOException, InterruptedException {
	
}
  public void reduce(LongWritable key,  Iterable<DoubleWritable> values,
				Context context) throws IOException, InterruptedException {
	  	}
	@Override
		protected void cleanup(Context context)
				throws IOException, InterruptedException {
		
		super.cleanup(context);
		}

}
