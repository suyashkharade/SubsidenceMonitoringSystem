package interferogram;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

public class Map1 extends Mapper<LongWritable,Text,LongWritable,DoubleWritable> {
     
	
	
	static int linecount;
	static int pixelcount;
	static  double RLINE; 
	static long keycount;
	
	static int Ilinecount;
	static int Ipixelcount;
	static double ILINE; 
	static long Ikeycount;
	
	static long keys;
	static int multilookL=1;
	static int multilookP=1;
	/* You need to care about this width and height while displaying cropped or any other*/
	static int Fh=-6;//212
	static int Fv=-208;//7
	static int border=0;
	
	 /*Use this for ful scene and comment above and below*/
	//int width=4900;
	//int height=26000;
	
	static int width = 4900-Math.abs(Fh)-border*2;//s 20 20 is border
	static int height =26000-Math.abs(Fv)-border*2;// 1738
	
	static DoubleWritable Outval;
	static String line;
	static StringTokenizer st;
	
	
	protected void setup(Context context) throws IOException, InterruptedException {
	//	multipleOutputs  = new MultipleOutputs<LongWritable,DoubleWritable>(context);
		 keycount=linecount=pixelcount=0;
		 Ikeycount=Ilinecount=Ipixelcount=0;
	 Outval=new DoubleWritable();
	 
	
		
	
	}
	
	
	
	public void map(LongWritable key,Text value,
			Context context) throws IOException, InterruptedException {
		 line=value.toString();
		 st=new StringTokenizer(line);
		
		//DoubleWritable val=new DoubleWritable(Double.parseDouble(st.nextToken()));
		
		
		

 		   
 		  keys=((Long.parseLong(st.nextToken())/multilookL)*(width/multilookP)*4);
 		  keys+=(Long.parseLong(st.nextToken())/multilookP)*4;
 		  RLINE=Integer.parseInt(st.nextToken());
        
 		  //RLINE[pixelcount]+=Integer.parseInt(st.nextToken());
           
 	 	    		   key.set(keys);
 	    		   Outval.set(RLINE);
 	    		  context.write(key,Outval);

 	   	//   multipleOutputs.write(key,Outval),"/app/hadoop/tmp/New_Multilooked_Interferometric_Magnitude311_Slc1_slc2");
 	    		  ILINE=Integer.parseInt(st.nextToken());
 	    	       key.set(keys+1);
 	    	       Outval.set(ILINE);
 	    		  context.write(key,Outval);

 	  

/////////////////////////////
 	

 	   
 	   
 	   ////////////////////
		
		
		
		
		
		
		
	}

}
