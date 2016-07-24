package interferogram;

import java.io.IOException;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;


public class Reduce extends Reducer<LongWritable,DoubleWritable, LongWritable, DoubleWritable> {
	
	private static MultipleOutputs<LongWritable,DoubleWritable> multipleOutputs;
	static int linecount;
	static int pixelcount;
	static double LINE[]; 
	static long keycount;
	
	static int phaselinecount;
	static int phasepixelcount;
	static double PLINE[]; 
	static long phasekeycount;
	
	
	static int multilookL=5;
	static int multilookP=1;
	/* You need to care about this width and height while displaying cropped or any other*/
	static int Fh=-217;
	static int Fv=-6;
	static int border=20;
	
	 /*Use this for ful scene and comment above and below*/
	//int width=4900;
	//int height=26000;
	
	static int width = 4900-Math.abs(Fh)-border*2;//s 20 20 is border
	static int height =26000-Math.abs(Fv)-border*2;// 1738
	
	static double D1[],D2[],Nreal[],Nimag[];
	static double coherence;
	static double mag1,mag2;
	protected void setup(Context context) throws IOException, InterruptedException {
		multipleOutputs  = new MultipleOutputs<LongWritable,DoubleWritable>(context);
		 keycount=linecount=pixelcount=0;
		 phasekeycount=phaselinecount=phasepixelcount=0;
	  LINE=new double[width];
	  PLINE=new double[width];
	  D1=new double[width];
      D2=new double[width];
      Nreal=new double[width];
      Nimag=new double[width];
	}
	
	public void reduce(LongWritable key, Iterable<DoubleWritable> values,
			Context context) throws IOException, InterruptedException {
	
	

		if((key.get()%2)==0)
		{
			mag1 = values.iterator().next().get();
			mag2 = values.iterator().next().get();
			// process value
	       Double out_mag=mag1*mag2;
	     //  key.set(key.get()/2);
	       
	    
	     
	    	   if(linecount==0)
	    	   {	
	    		   LINE[pixelcount]=out_mag;
	               D1[pixelcount]=mag1*mag1; //for coherence
	               D2[pixelcount]=mag2*mag2; //for coherence
	               
	    	   }
	    	   else
	    	   {
	    		   LINE[pixelcount]+=out_mag; 
	    		   D1[pixelcount]+=mag1*mag1; //for coherence
	               D2[pixelcount]+=mag2*mag2;  //for coherence
	               
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
	    	    		   LINE[i]=LINE[i*multilookP];
	    	    		   D1[i]=D1[i*multilookP];//for coherence
	    	    		   D2[i]=D2[i*multilookP];//for coherence
	    	    		   
	    	    		   for(int j=1;j<multilookP;j++)
	    	    		   {
	    	    			   LINE[i]+=LINE[(i*multilookP)+j];
	    	    			   D1[i]+=D1[(i*multilookP)+j]; //for coherence
	    	    			   D2[i]+=D2[(i*multilookP)+j]; //for coherence
	    	    		   }
	    	    		   
	    	    		   LINE[i]/=(multilookP*multilookL);
	    	    		   D1[i]/=(multilookP*multilookL);//for coherence
	    	    		   D2[i]/=(multilookP*multilookL);//for coherence
	    	    		   
	    	    		   
	    	    		   
	    	    		   key.set(keycount);
	    	    		   
	    	   // 		   multipleOutputs.write(key,new DoubleWritable(LINE[i]),"/app/hadoop/tmp/New_Multilooked_Interferometric_Magnitude_Slc1_slc2");
	    	    		   keycount++;    
	    	    	    }

	    	    	    linecount=0;
	    	       }
	    	  
	    	   }

	    	//   multipleOutputs.write(key,new DoubleWritable(out_mag),"/app/hadoop/tmp/New_Interferometric_Magnitude_Slc1_slc2");
	       
		}
		
		else
		{
			Double phase1=values.iterator().next().get();
			Double phase2=values.iterator().next().get();
			Double out_phase=phase1+phase2;
			key.set(key.get()/2);
			
			   if(phaselinecount==0)
	    	   {	
	    		   PLINE[phasepixelcount]=out_phase;
	               Nreal[phasepixelcount]=mag1*mag2*Math.cos(out_phase); //for coherence
	               Nimag[phasepixelcount]=mag1*mag2*Math.sin(out_phase); //for coherence
	    	   }
	    	   else
	    	   {	  PLINE[phasepixelcount]+=out_phase; 
          	    	   Nreal[phasepixelcount]+=mag1*mag2*Math.cos(out_phase); //for coherence
                     Nimag[phasepixelcount]+=mag1*mag2*Math.sin(out_phase);   //for coherence
	    	   
	    	   }
	    	   phasepixelcount++;
	    	
	    	   if(phasepixelcount==width)
	    	   {	
	    		   phaselinecount++;
	    	       phasepixelcount=0;
	    	       if(phaselinecount==multilookL)
	    	       {
	    	    	   
	    	    	    for(int i=0;i<(width/multilookP);i++)
	    	    	    {
	    	    		   PLINE[i]=PLINE[i*multilookP];
	    	    		   Nreal[i]=Nreal[i*multilookP]; //for coherence
	    	    		   Nimag[i]=Nimag[i*multilookP]; //for coherence
	    	    		   
	    	    		   for(int j=1;j<multilookP;j++)
	    	    		   { 
	    	    			  PLINE[i]+=PLINE[(i*multilookP)+j];
	    	    			  Nreal[i]+=Nreal[(i*multilookP)+j]; //for coherence
	    	    			  Nimag[i]+=Nimag[(i*multilookP)+j]; //for coherence
		    	    		   
	    	    		   }
	    	    		   
	    	    		   PLINE[i]/=(multilookP*multilookL);
	    	    		   Nreal[i]/=(multilookP*multilookL);  //for coherence
	    	    		   Nimag[i]/=(multilookP*multilookL);  //for coherence
	    	    		   
	    	    	       	  //for coherence 
	    	    	       
	    	    	       coherence=(double)(Math.sqrt(Nreal[i]*Nreal[i]+Nimag[i]*Nimag[i])/Math.sqrt(D1[i]*D2[i])); //for coherence
	    	    	       
	    	    	       key.set(phasekeycount);
	    	    		  // multipleOutputs.write(key,new DoubleWritable(PLINE[i]),"/app/hadoop/tmp/New_Multilooked_Interferometric_Phase_Slc1_slc2");
	    	    	       
	    	    	       multipleOutputs.write(key,new DoubleWritable(coherence),"/app/hadoop/tmp/New_FullReadNline_Multilooked_Coherence_New_Slc1_slc2"); //for coherence
		    	    		
	    	    		   phasekeycount++;    
	    	    	     
	    	    	    }

	    	    	    phaselinecount=0;
	    	       }
	    	  
	    	   }
			
			
			
			
			
			
			//multipleOutputs.write(key,new DoubleWritable(out_phase),"/app/hadoop/tmp/New_Interferometric_Phase_Slc1_slc2");
		}
	}
}

