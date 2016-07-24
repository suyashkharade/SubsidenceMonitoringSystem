package crop;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

public class Map extends Mapper<LongWritable,Text,LongWritable,DoubleWritable>{
	int Fh=-217,Fv=-6;
	char ch='s';
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
			{	if((key.get()>=(4900*20)) && (key.get()<(26000-20-Math.abs(Fv))*4900) && ((key.get())%4900>=20) && ((key.get())%4900<(4900-20-Math.abs(Fh))))
				{
					context.write(key,new DoubleWritable(x));
				}
			}
			if(Fv>0&&Fh<0)
					if((key.get()>=(4900*(20+Fv))) && (key.get()<(26000-20)*4900) && ((key.get())%4900>=20) && ((key.get())%4900<(4900-20-Math.abs(Fh))))
						context.write(key,new DoubleWritable(x));
			
				if(Fv>0&&Fh>0)
					if((key.get()>=(4900*(20+Fv))) && (key.get()<(26000-20)) && ((key.get())%4900>=(20+Math.abs(Fh))) && ((key.get())%4900<(4900-20)))
						context.write(key,new DoubleWritable(x));
			//corrected consider '=' sign ((key.get())%4900>=/*27*/20+Math.abs(Fh)) && ((key.get())%4900<=/*4879*/4899-20) 
			//Modify other cases also	
				if(Fh>0&&Fv<0)
					if((key.get()>=(4900*20)) && (key.get()<((26000-20-Math.abs(Fv))*4900)) && ((key.get())%4900>=(20+Math.abs(Fh))) && ((key.get())%4900<(4900-20)))
						context.write(key,new DoubleWritable(x));
		}
		if (ch=='s')
		{
			
			if(Fv<0&&Fh<0)
				if((key.get()>=(4900*(20+Math.abs(Fv)))) && (key.get()<((26000-20)*4900)) && ((key.get())%4900>=(20+Math.abs(Fh))) && ((key.get())%4900<(4900-20)))
					context.write(key,new DoubleWritable(x));
				
				if(Fv>0&&Fh<0)
					if((key.get()>=(4900*20)) && (key.get()<(26000-20-Math.abs(Fv))*4900) && ((key.get())%4900>=(20+Math.abs(Fh))) && ((key.get())%4900<(4900-20)))
						context.write(key,new DoubleWritable(x));
			
				if(Fv>0&&Fh>0)
					if((key.get()>=(4900*20)) && (key.get()<26000-20-Math.abs(Fv)) && ((key.get())%4900>=20) && ((key.get())%4900<4900-20-Math.abs(Fh)))
						context.write(key,new DoubleWritable(x));
			
				if(Fh>0&&Fv<0)
				{
					if((key.get()>=(4900*(20+Math.abs(Fv)))) && (key.get()<((26000-20)*4900)) && ((key.get())%4900>=20) && ((key.get())%4900<(4900-20-Math.abs(Fh))))
					{
						context.write(key,new DoubleWritable(x));
					}
				}
		}
		
	}
		
	}