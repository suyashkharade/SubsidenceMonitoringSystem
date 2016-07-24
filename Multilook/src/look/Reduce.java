package look;

import java.io.IOException;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;


public class Reduce extends Reducer<LongWritable,DoubleWritable, Text, Text> {
	
	private static MultipleOutputs<Text,Text> multipleOutputs;
	
	
	static int multilookL=5;
	static int multilookP=1;
	/* You need to care about this width and height while displaying cropped or any other*/
	static int Fh=-6;//212
	static int Fv=-208;//7
	static int border=0;
	
	 /*Use this for ful scene and comment above and below*/
	//int width=4900;
	//int height=26000;
	
	static int MultiLookwidth = (4900-Math.abs(Fh)-border*2)/multilookP;//s 20 20 is border
	static int MultiLookheight =(26000-Math.abs(Fv)-border*2)/multilookL;// 1738

	//static int MultiLookwidth = (4894)/multilookP;//s 20 20 is border
//	static int MultiLookheight =(5158)/multilookL;// 1738

	static double magnitude,phase;
	static double a,b,c,d;
	static Text txtkey;
	static Text txtval;
	protected void setup(Context context) throws IOException, InterruptedException {
		multipleOutputs  = new MultipleOutputs<Text,Text>(context);
		txtkey=new Text();
		txtval=new Text();
	}
	
	public void reduce(LongWritable key, Iterable<DoubleWritable> values,
			Context context) throws IOException, InterruptedException {
	
	

		if((key.get()%2)==0)
		{
	       
       a=values.iterator().next().get();
	            
		}
		
		else if((key.get()%2)==1) 
		{
			b=values.iterator().next().get();
			    	  
			key.set(key.get()/2);
			txtkey.set(""+key.get()/MultiLookwidth+"\t"+key.get()%MultiLookwidth);
			txtval.set(""+a+"\t"+b);
		context.write(txtkey, txtval);
			magnitude=Math.sqrt(a*a+b*b);
			txtval.set(""+magnitude);
		//	multipleOutputs.write(txtkey,txtval,"/app/hadoop/temp/New_Multilook_Interferometric_Magnitude_Slc1_slc2_01_02_2015");
			phase=Math.atan2(b, a);
		//	txtval.set(""+phase);	
	//	multipleOutputs.write(txtkey,txtval,"/app/hadoop/temp/filtered_Phase_Slc1_slc2_10_02_2015");
			
		
		}
		
			
				
			
			
			
	
	
	}
}

