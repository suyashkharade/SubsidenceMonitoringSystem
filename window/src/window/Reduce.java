package window;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.*;

public class Reduce extends
		Reducer<LongWritable, DoubleWritable, LongWritable, DoubleWritable> {
	static long count = 0;

	@Override
	public void reduce(LongWritable key, Iterable<DoubleWritable> values,
			Context context) throws IOException, InterruptedException {

		// if( (key.get()>4900*20) && (key.get()<25830*4880) &&
		// ((key.get())%4900>28) && ((key.get())%4900<4880) )

		context.write(key, new DoubleWritable(values.iterator().next().get()));
		// count++;
	}

}
