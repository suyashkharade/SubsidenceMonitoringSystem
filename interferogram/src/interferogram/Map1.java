package interferogram;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class Map1 extends Mapper<LongWritable,Text,LongWritable,DoubleWritable> {
     
	
	
	public void map(LongWritable key,Text value,
			Context context) throws IOException, InterruptedException {
		String line=value.toString();
		StringTokenizer st=new StringTokenizer(line);
		key.set(Long.parseLong(st.nextToken()));
		key.set(key.get()*2);
		DoubleWritable val=new DoubleWritable(Double.parseDouble(st.nextToken()));
		
		
		
		context.write(key,val);
		
		
	}

}
