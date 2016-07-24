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
	static  double RLINE[]; 
	static long keycount;
	
	static int Ilinecount;
	static int Ipixelcount;
	static double ILINE[]; 
	static long Ikeycount;
	
	static long keys[];
	static int multilookL=5;
	static int multilookP=1;
	/* You need to care about this width and height while displaying cropped or any other*/
	static int Fh=-8;//212
	static int Fv=-208;//7
	static int border=0;
	
	 /*Use this for ful scene and comment above and below*/
	//int width=4900;
	//int height=26000;
	
	static int width = 4900-Math.abs(Fh)-border*2;//s 20 20 is border
	static int height =26000-Math.abs(Fv)-border*2;// 1738
	
	static DoubleWritable Outval;
	
	
	protected void setup(Context context) throws IOException, InterruptedException {
	//	multipleOutputs  = new MultipleOutputs<LongWritable,DoubleWritable>(context);
		 keycount=linecount=pixelcount=0;
		 Ikeycount=Ilinecount=Ipixelcount=0;
	  RLINE=new double[width];
	  ILINE=new double[width];
	  keys=new long[width];
	 Outval=new DoubleWritable();
	
	}
	
	
	
	public void map(LongWritable key,Text value,
			Context context) throws IOException, InterruptedException {
		String line=value.toString();
		StringTokenizer st=new StringTokenizer(line);
		
		//DoubleWritable val=new DoubleWritable(Double.parseDouble(st.nextToken()));
		
		
		

	     
 	   if(linecount==0)
 	   {
 		   
 		  keys[pixelcount]=((Long.parseLong(st.nextToken())/multilookL)*(width/multilookP)*4);
 		  keys[pixelcount]+=(Long.parseLong(st.nextToken())/multilookP)*4;
 		  RLINE[pixelcount]=Integer.parseInt(st.nextToken());
            
 	   }
 	   else
 	   {
 		   st.nextToken();st.nextToken();
 		  RLINE[pixelcount]+=Integer.parseInt(st.nextToken());
           
 	   }
 	   pixelcount++;
 	
 	   if(pixelcount==width)
 	   {	
 		   linecount++;
 	       pixelcount=0;
 	       if(linecount==multilookL)
 	       {
 	    	   
 	    	    for(int i=0;i<(width/multilookP);i++)
 	    	    {
 	    		   RLINE[i]=RLINE[i*multilookP];
 	    		   
 	    		   for(int j=1;j<multilookP;j++)
 	    		   {
 	    			   RLINE[i]+=RLINE[(i*multilookP)+j];
 	    		   }
 	    		   
 	    		   RLINE[i]=(double)RLINE[i]/(multilookP*multilookL);
 	    		   
 	    		   
 	    		   
 	    		   key.set(keys[i]);
 	    		   Outval.set(RLINE[i]);
 	   	//   multipleOutputs.write(key,Outval),"/app/hadoop/tmp/New_Multilooked_Interferometric_Magnitude311_Slc1_slc2");

 	    		  context.write(key,Outval);
 	    	    }

 	    	    linecount=0;
 	       }
 	  
 	   }

/////////////////////////////
 	  if(Ilinecount==0)
	   {
		   
		  ILINE[Ipixelcount]=-Integer.parseInt(st.nextToken());
           
	   }
	   else
	   {
		  ILINE[Ipixelcount]+=-Integer.parseInt(st.nextToken());
          
	   }
	   Ipixelcount++;
	
	   if(Ipixelcount==width)
	   {	
		   Ilinecount++;
	       Ipixelcount=0;
	       if(Ilinecount==multilookL)
	       {
	    	   
	    	    for(int i=0;i<(width/multilookP);i++)
	    	    {
	    		   ILINE[i]=ILINE[i*multilookP];
	    		   
	    		   for(int j=1;j<multilookP;j++)
	    		   {
	    			   ILINE[i]+=ILINE[(i*multilookP)+j];
	    		   }
	    		   
	    		   ILINE[i]/=(multilookP*multilookL);
	    		   
	    		   
	    		   
	    		   key.set(keys[i]+1);
	    		   Outval.set(ILINE[i]);
	   	//   multipleOutputs.write(key,Outval),"/app/hadoop/tmp/New_Multilooked_Interferometric_Magnitude311_Slc1_slc2");

	    		  context.write(key,Outval);
	    	    }

	    	    Ilinecount=0;
	       }
	  
	   }

 	   
 	   
 	   ////////////////////
		
		
		
		
		
		
		
	}

}
