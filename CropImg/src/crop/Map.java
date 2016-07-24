package crop;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


public class Map extends Mapper<LongWritable,Text,LongWritable,DoubleWritable>{
	/* Need to set horizontal and vertical offsets according to baseline module output*/
	int Fh=-220,Fv=-13;

	/* Need to set ch according to master or slave file to be crop*/
	char ch='m';
	Text mag;
	Double tmp;
	Double x;
	public void map(LongWritable key, Text value,
			Context context) throws IOException, InterruptedException {
		
		String line=value.toString();
		StringTokenizer st=new StringTokenizer(line);
		
		key.set(Long.parseLong(st.nextToken()));
		
		x=(Double.parseDouble(st.nextToken()));

		if(ch=='m')
		{
				
			if(Fv<0&&Fh<0)
			{	if((key.get()>=4900*20) && (key.get()</*25829*/(26000-20-Math.abs(Fv))*4900) && ((key.get())%4900>/*27*/20) && ((key.get())%4900<=/*4879*/4899-20-Math.abs(Fh)))
				{
					context.write(key,new DoubleWritable(x));
				}
			}
			if(Fv>0&&Fh<0)
					if((key.get()>=4900*(20+Fv)) && (key.get()</*25829*/(26000-20)*4900) && ((key.get())%4900>/*27*/20) && ((key.get())%4900<=/*4879*/4899-20-Math.abs(Fh)))
						context.write(key,new DoubleWritable(x));
			
				if(Fv>0&&Fh>0)
					if((key.get()>=4900*(20+Fv)) && (key.get()</*25829*/26000-20) && ((key.get())%4900>/*27*/20+Math.abs(Fh)) && ((key.get())%4900<=/*4879*/4899-20))
						context.write(key,new DoubleWritable(x));
			//corrected consider '=' sign ((key.get())%4900>=/*27*/20+Math.abs(Fh)) && ((key.get())%4900<=/*4879*/4899-20) 
			//Modify other cases also	
				if(Fh>0&&Fv<0)
					if((key.get()>=4900*20) && (key.get()</*25829*/(26000-20-Math.abs(Fv))*4900) && ((key.get())%4900>=/*27*/20+Math.abs(Fh)) && ((key.get())%4900<=/*4879*/4899-20))
						context.write(key,new DoubleWritable(x));
		}
		if (ch=='s')
		{
			
			if(Fv<0&&Fh<0)
				if((key.get()>=4900*(20+Math.abs(Fv))) && (key.get()</*25829*/(26000-20)*4900) && ((key.get())%4900>/*27*/(20+Math.abs(Fh))) && ((key.get())%4900<=/*4879*/4899-20))
					context.write(key,new DoubleWritable(x));
				
				if(Fv>0&&Fh<0)
					if((key.get()>=4900*20) && (key.get()</*25829*/(26000-20-Math.abs(Fv))*4900) && ((key.get())%4900>/*27*/20+Math.abs(Fh)) && ((key.get())%4900<=/*4879*/4899-20))
						context.write(key,new DoubleWritable(x));
			
				if(Fv>0&&Fh>0)
					if((key.get()>=4900*20) && (key.get()</*25829*/26000-20-Math.abs(Fv)) && ((key.get())%4900>/*27*/20) && ((key.get())%4900<=/*4879*/4899-20-Math.abs(Fh)))
						context.write(key,new DoubleWritable(x));
			
				if(Fh>0&&Fv<0)
				{
					if((key.get()>=4900*(20+Math.abs(Fv))) && (key.get()</*25829*/(26000-20)*4900) && ((key.get())%4900>/*27*/20) && ((key.get())%4900<=/*4879*/4899-20-Math.abs(Fh)))
					{
						context.write(key,new DoubleWritable(x));
					}
				}
		}
		
	}
		
	}