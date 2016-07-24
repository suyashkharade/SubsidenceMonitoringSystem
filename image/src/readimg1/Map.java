package readimg1;


import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;

public class Map extends Mapper<LongWritable,BytesWritable,LongWritable,DoubleWritable>{
	
	
	 protected void setup(Context context) throws IOException, InterruptedException {
		String strFilePath = "/app/hadoop/tmp/MinMax_Phase_Mag.txt";
		
		FSDataInputStream din; 
        
		FileSystem fs = FileSystem.get(new Configuration());//.getFileSystem(job);
	   
        din=fs.open(new Path(strFilePath));	
     	
	    //Extract Min Max from file
	        //min Magnitude
	    minMag.set(din.readDouble());
		    //max Magnitude
	    maxMag.set(din.readDouble());		 
		    //min Phase
	    minPhase.set(din.readDouble());
            //max Phase
	    maxPhase.set(din.readDouble());		 
		
    	    din.close();
	
    	 System.out.println("In Setup:1:After read values from file input in Map::MAX magnitude"+maxMag.get()+"MIN magnitude"+minMag.get());
	     System.out.println("MAX phase"+maxPhase.get()+"MIN phase"+minPhase.get());

	}

	@Override
	protected void cleanup(Context context)
	throws IOException, InterruptedException {
		
		 Path strFilePath =new Path("/app/hadoop/tmp/MinMax_Phase_Mag.txt");
		 
		 FileSystem fs = FileSystem.get(new Configuration());//.getFileSystem(job);
		 
		 FSDataInputStream din; 
		
		    din=fs.open(strFilePath);	

		    Double RmaxMag,RminMag,RminPhase,RmaxPhase;
			
		 //Write Min Max values modified due to this map to file
		  //As parallel processing is their we have to read file again to look for changed 
		    
		    //min Magnitude
		  RminMag=din.readDouble();
			 //max Magnitude
	      RmaxMag=din.readDouble();		 
			//min Phase
	      RminPhase=din.readDouble();
	        //max Phase
	      RmaxPhase=din.readDouble();	
    
	      din.close();

     System.out.println("In cleanup:1:Befor write values in file :MAX magnitude"+RmaxMag+"MIN magnitude"+RminMag);
     System.out.println("MAX phase"+RmaxPhase+"MIN phase"+RminPhase);

	if(minMag.get()<RminMag)
		RminMag=minMag.get();
	if(maxMag.get()>RmaxMag)
		RmaxMag=maxMag.get();
	if(minPhase.get()<RminPhase)
		RminPhase=minPhase.get();
	if(maxPhase.get()>RmaxPhase)
		RmaxPhase=maxPhase.get();
			
    	 FSDataOutputStream dos;

    	 
	 dos=fs.create(strFilePath, true);

	 //min Magnitude
	 dos.writeDouble(RminMag);
	 //max Magnitude
	 dos.writeDouble(RmaxMag);
	 
	  //min Phase
	 dos.writeDouble(RminPhase);
	 //max Phase
	 dos.writeDouble(RmaxPhase);
	 
	 dos.close();
	
	 System.out.println("In cleanup:2:After write values in file :MAX magnitude"+RmaxMag+"MIN magnitude"+RminMag);
     System.out.println("MAX phase"+RmaxPhase+"MIN phase"+RminPhase);
	   
	}

	
	
	/*lesson 1:In parallel computing It is not shared amongst various Map*/
	public static DoubleWritable minMag=new DoubleWritable(100000000);
	public static DoubleWritable maxMag=new DoubleWritable(0);
	
	public static DoubleWritable minPhase=new DoubleWritable(100000000);
	public static DoubleWritable maxPhase=new DoubleWritable(0);
	
	
	
	public void map(LongWritable key, BytesWritable value,
			Context context) throws IOException, InterruptedException {
		
		    ////******DATA RECORDS****///
	  
	
	  DoubleWritable mag = new DoubleWritable();
		DoubleWritable phase = new DoubleWritable();
	
		String str1,str2;
	 
	     str2=" "; 
         
	     int j=0;
          	 short k=0;
         int index=0;
        
        DoubleWritable real=new DoubleWritable();
         DoubleWritable imaginary=new DoubleWritable();
  	
         LongWritable tempkey=new LongWritable(key.get());
   
         int convtemp;
         // Byte array to store input split
           byte[] temp=new byte[value.getLength()];
         temp=value.getBytes();
      	
         for(int m=1;m<=value.getLength()/(19612);m++)
     	  { 	
           	str1="----******-----   DATA RECORD "+m+"  ------******----\n";
	       
           	    //1-4 bytes in record
           	str1=str1+" Record sequence number  :";

           	convtemp=0;
            for(int d=3;d>=0;d--)
            	convtemp|=((int)temp[index++]&0xff)<<8*d;

           
	        System.out.println(str1+convtemp);
	    
            
	            //5th byte in record
     	    str1="1st Record subtype code";
	        str2="";
    	    str2=str2+(((temp[index]))&0xff);
	        System.out.println(str1+str2);
	     	
	           index++;
                
	        	//6th byte in record
	      	 str1="Record subtype code";
	         str2="";
	    	 str2=str2+(((temp[index]))&0xff);
	       	 System.out.println(str1+str2);
	        	  
	           index++;
	            
	            //7th byte in record
	          str1="2nd Record subtype code";
	          str2="";
	          str2=str2+(((temp[index]))&0xff);
		      System.out.println(str1+str2);
		          	
		    	index++;
		            
		        //8th byte in record
		       str1="3rd Record subtype code";
		       str2	="";
	           str2=str2+(((temp[index]))&0xff);
		       System.out.println(str1+str2);
		          	
		         index++;
		       
		       str1="  Length of this record     (nominal)";
		      	convtemp=0;
		        for(int d=3;d>=0;d--)
		           convtemp|=((int)temp[index++]&0xff)<<8*d;
         
		        System.out.println(str1+convtemp);
		         
		          	
                       str1="";
		        
                       //ACTUAL DATA
                       for(j=0;j<4900;j++)
                  	  {
                    	   //important key generation for pixel number
                    	   key.set(((tempkey.get()-19612)/19612)*4900*2+(m-1)*4900*2+j*2);
                    	   
		          	   str1=str1+"\tSample "+(j+1)+":"; 
		          
		          	   //Real value
		          	    k=0;
		           		k=(short) ((short)temp[index]&0xff);
 		          		k<<=8; 
   		          	   k|=((short)temp[index+1]&0xff);
		          		str1=str1+k;
		                real.set(k);   

	 		          		k= (short) temp[index+2];
	 		          		k&=0x0ff;
	 		          		k<<=8; 
	 		          		k|=((short)temp[index+3]&0xff);	//k=~k;
			          		imaginary.set(k);


//		 		          	 System.out.println("Imag k= "+k);
		 		        	
		 		          		index+=4; 		 
    
	
     /*		          		k=temp[index];
		          		k&=0x0ff;
		          		k<<=8; 
		          		k|=temp[index+1];

		          		str1=str1+k;
		                real.set(k);   
		          
		                k=0;
		          		k=temp[index+2];
		          		k&=0x0ff;
		          		k<<=8;
		          		k|=temp[index+3];
		          		imaginary.set(k);
	*/	          	
		          		
		 //	Magnitude calculation
		//1 2    2015      	mag.set( Math.sqrt((double)(Math.pow(real.get(),2))+(double)(Math.pow(imaginary.get(),2))));
	
	          		if(mag.get()>maxMag.get())
		          			maxMag.set(mag.get());
		          		
		          		if(mag.get()<minMag.get())
		          			minMag.set(mag.get());
		          		
		 //Phase calculation
		             	       		
		//1 2 2015biggest mistake that I used atan function instead atan2 phase.set((double) Math.atan(((double)(imaginary.get())/real.get())));
		          		phase.set(Math.atan2((double)imaginary.get(),(double)real.get()));
			        	
		          			
		          		if(phase.get()>maxPhase.get())
		          			maxPhase.set(phase.get());
		          		
		          		if(phase.get()<minPhase.get())
		          	               minPhase.set(phase.get());
		          	
//		          		context.write(key,mag);
		          		key.set(key.get()+1);
		         		context.write(key,phase);
		          		
		          	
		          		
		          	  
		          		str1="";
		          			
		          		
		          		
		          	  }//for
     	  }   
      
     
      // System.out.println("MAX magnitude"+maxMag.get()+"MIN magnitude"+minMag.get());
       // System.out.println("MAX phase"+maxPhase.get()+"MIN phase"+minPhase.get());
		          	 }
		
	}


