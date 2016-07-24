package ers_read;


import java.io.IOException;
import java.text.DecimalFormat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.mapreduce.Mapper;

public class Map extends Mapper<LongWritable,BytesWritable,LongWritable,IntWritable>{
	
		

	/*lesson 1:In parallel computing It is not shared amongst various Map*/
	public static Double minMag;
	public static Double maxMag;
	
	public static Double minPhase;
	public static Double maxPhase;
	 public static DecimalFormat dfMag,dfPhase;
	public static int width;
	 static IntWritable real;
      static IntWritable imaginary;
	
static  LongWritable tempkey;

	
	protected void setup(Context context) throws IOException, InterruptedException {

		 width=4900;
	//Extract Min Max from file
	        //min Magnitude
	    minMag=(double) (context.getConfiguration().getFloat("MINIMUM_MAGNITUDE", 1000000));
		    //max Magnitude
	    maxMag=(double) (context.getConfiguration().getFloat("MAXIMUM_MAGNITUDE", 0));		 
		    //min Phase
	    minPhase=(double) (context.getConfiguration().getFloat("MINIMUM_PHASE", 1000000));
            //max Phase
	    maxPhase=(double) (context.getConfiguration().getFloat("MAXIMUM_PHASE", 0));		 
	    
	    dfMag = new DecimalFormat("#.#####");
	dfPhase=new DecimalFormat("#.##########");
    	 System.out.println("Width=="+width+"In Setup:1:After read values from file input in Map::MAX magnitude"+maxMag+"MIN magnitude"+minMag);
	     System.out.println("MAX phase"+maxPhase+"MIN phase"+minPhase);
        
	}

	@Override
	protected void cleanup(Context context)
	throws IOException, InterruptedException {
		
		  real=new IntWritable();
	       imaginary=new IntWritable();
	  	
	       tempkey=new LongWritable();
	   
	   	

		    Double RmaxMag,RminMag,RminPhase,RmaxPhase;
			
		 //Write Min Max values modified due to this map to file
		  //As parallel processing is their we have to read file again to look for changed 
		    
		    //min Magnitude
		  RminMag=(double) (context.getConfiguration().getFloat("MINIMUM_MAGNITUDE", 1000000));
			 //max Magnitude
	      RmaxMag=(double) (context.getConfiguration().getFloat("MAXIMUM_MAGNITUDE", 0));		 
			//min Phase
	      RminPhase=(double) (context.getConfiguration().getFloat("MINIMUM_PHASE", 1000000));
	        //max Phase
	      RmaxPhase=(double) (context.getConfiguration().getFloat("MAXIMUM_PHASE", 0));	
    


	if(minMag<RminMag)
		RminMag=minMag;
	if(maxMag>RmaxMag)
		RmaxMag=maxMag;
	if(minPhase<RminPhase)
		RminPhase=minPhase;
	if(maxPhase>RmaxPhase)
		RmaxPhase=maxPhase;
	
	
	
	
	
	context.getConfiguration().setFloat("MINIMUM_MAGNITUDE",RminMag.floatValue());

	context.getConfiguration().setFloat("MAXIMUM_MAGNITUDE",RmaxMag.floatValue());
	

	context.getConfiguration().setFloat("MINIMUM_PHASE",RminPhase.floatValue());
	context.getConfiguration().setFloat("MAXIMUM_PHASE",RmaxPhase.floatValue());
	
	 System.out.println("In cleanup:2:After write values in file :MAX magnitude"+RmaxMag+"MIN magnitude"+RminMag);
     System.out.println("MAX phase"+RmaxPhase+"MIN phase"+RminPhase);
	   
	}

	
	
	
	public void map(LongWritable key, BytesWritable value,
			Context context) throws IOException, InterruptedException {
		
		    ////******DATA RECORDS****///
	  
	
	//  DoubleWritable mag = new DoubleWritable();
    	//DoubleWritable phase = new DoubleWritable();
	
		//String str1,str2;
	 
	     //str2=" "; 
         
	     int j=0;
          	 short k=0;
         int index=0;
        
         tempkey.set(key.get());
         int convtemp;
         // Byte array to store input split
           byte[] temp=new byte[value.getLength()];
         temp=value.getBytes();
         
        
         for(int m=1;m<=value.getLength()/(19612);m++)
     	  { 	
        
           	
       // 	 str1="----******-----   DATA RECORD "+m+"  ------******----\n";
	       
           	    //1-4 bytes in record
     //      	str1=str1+" Record sequence number  :";

           	convtemp=0;
            for(int d=3;d>=0;d--)
            	convtemp|=((int)temp[index++]&0xff)<<8*d;

           
	       // System.out.println(str1+convtemp);
	    
            
	            //5th byte in record
     	    /*str1="1st Record subtype code";
	        str2="";
    	    str2=str2+(((temp[index]))&0xff);
	        */
    	    //System.out.println(str1+str2);
	     	
	           index++;
                
	        	//6th byte in record
	      	 /*str1="Record subtype code";
	         str2="";
	    	 str2=str2+(((temp[index]))&0xff);
	       	 */
	    	 //System.out.println(str1+str2);
	        	  
	           index++;
	            
	            //7th byte in record
	          /*str1="2nd Record subtype code";
	          str2="";
	          str2=str2+(((temp[index]))&0xff);
		      */
	          //System.out.println(str1+str2);
		          	
		    	index++;
		            
		        //8th byte in record
		       /*str1="3rd Record subtype code";
		       str2	="";
	           str2=str2+(((temp[index]))&0xff);
		       */
	           //System.out.println(str1+str2);
		          	
		         index++;
		       
		//       str1="  Length of this record     (nominal)";
		  
		         convtemp=0;
		        for(int d=3;d>=0;d--)
		           convtemp|=((int)temp[index++]&0xff)<<8*d;
         
		        //System.out.println(str1+convtemp);
		         
		          	
                   //    str1="";
                       //19612 subtraction specififes unuseful bytes slc data initially read format 
                       //key.set(((tempkey.get()-19612)/19612)*2+(m-1)*2);
                       
                       //ACTUAL DATA
                      for(j=0;j<4900;j++)
                  	  {
                    	  key.set(((tempkey.get()-19612)/19612)*4900*2+(m-1)*4900*2+j*2);
                    	   
		         // 	   str1=str1+"\tSample "+(j+1)+":"; 
		          
		          	   //Real value
		          	    k=0;
		           		k=(short) ((short)temp[index]&0xff);
 		          		k<<=8; 
    		          	   k|=((short)temp[index+1]&0xff);
		          	//	str1=str1+k;
		                real.set(k);   

	 		          		k= (short)((short) temp[index+2]&0xff);
//	 		          		k&=0x0ff;
	 		          		k<<=8; 
	 		          		k|=((short)temp[index+3]&0xff);	//k=~k;
			          		imaginary.set(k);


		 		        	
 		          		index+=4; 		 
    
			          		
		 //	Magnitude calculation
		//1 2 2015          	mag.set( Math.sqrt((double)(Math.pow(real.get(),2))+(double)(Math.pow(imaginary.get(),2))));
	
//	          		if(mag.get()>maxMag)
	//	          			maxMag=mag.get();
		          		
		//          		if(mag.get()<minMag)
		  //        			minMag=mag.get();
		          		
		 //Phase calculation
		          	//1 2 2015biggest mistake that I used atan function instead atan2 phase.set((double) Math.atan(((double)(imaginary.get())/real.get())));
		       //   		phase.set(Math.atan2((double)imaginary.get(),(double)real.get()));
		
		        			
		         // 		if(phase.get()>maxPhase)
		          	//		maxPhase=phase.get();
		          		
		          		//if(phase.get()<minPhase)
		          	      //         minPhase=phase.get();
		          	
		   //       		mag.set(Double.parseDouble(dfMag.format(mag.get())));
		     //   		phase.set(Double.parseDouble(dfPhase.format(phase.get())));
		      		  
		          		context.write(key,real);
		          		key.set(key.get()+1);
		         		context.write(key,imaginary);
		  
		                     	
		          			          		
		            	 }//for
                      
     	  
     	  
        	  }   
      
     	 }
		
	}


