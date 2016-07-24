package display;
import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class Map extends Mapper<LongWritable,Text,LongWritable,DoubleWritable>{
	
	static Double Rmax,Rmin,FileRmin,FileRmax;
	int Fh=-6;
	int Fv=-208;
	int border=0;
	
	/*Use this for ful scene and comment above and below*/
//	  int noOfCols=4900;
	//  int noOfLines=26000;
	int noOfCols=4900;
	  int noOfLines=26000;
	
	/* Set mutilook factors here*/
	int multilookL=5;
	int multilookP=1;

	int width = (noOfCols-Math.abs(Fh)-border*2)/multilookP;// 20 20 is border  is due to cropping error need to figure out in crop)
	int height =(noOfLines-Math.abs(Fv)-border*2)/multilookL;// 1738
	
	//int width=4894/multilookP;
	//int height=5158/multilookL;
	 protected void setup(Context context) throws IOException, InterruptedException {
		String strFilePath = "/app/hadoop/tmp/MinMaxDisplay_temp.txt";
		
		FSDataInputStream din; 
        
		FileSystem fs = FileSystem.get(context.getConfiguration());//.getFileSystem(job);
	   
        din=fs.open(new Path(strFilePath));	
     	
	    //Extract Min Max from file
	        //min Magnitude
	    Rmin=(din.readDouble());
		    //max Magnitude
	    Rmax=(din.readDouble());		 
		 din.close(); 
		// Rmin=(double)context.getConfiguration().getFloat("MINIMUM", 10000000);
		  //Rmax=(double)context.getConfiguration().getFloat("MAXIMUM", 0);
		  
	    System.out.println("In Setup:1:After read values from file input in Map::MAX magnitude"+Rmax+"MIN magnitude"+Rmin);
	     System.out.println("MAX phase"+Rmax+"MIN phase"+Rmin);
    	   
	
    	   
    	 
	}

	@Override
	protected void cleanup(Context context)
	throws IOException, InterruptedException {
		
	 Path strFilePath =new Path("/app/hadoop/tmp/MinMaxDisplay_temp.txt");
		 
		 FileSystem fs = FileSystem.get(context.getConfiguration());//.getFileSystem(job);
		 
		 FSDataInputStream din; 
		
		    din=fs.open(strFilePath);	

		    
			
		 //Write Min Max values modified due to this map to file
		  //As parallel processing is their we have to read file again to look for changed 
		    
		    //min Magnitude
		  FileRmin=din.readDouble();
			 //max Magnitude
	      FileRmax=din.readDouble();		 
			
	      din.close();
		FileRmin=(double)context.getConfiguration().getFloat("MINIMUM", 10000000);
		  FileRmax=(double)context.getConfiguration().getFloat("MAXIMUM", 0);
		 
		
	if(FileRmin<Rmin)
		Rmin=FileRmin;
	if(FileRmax>Rmax)
		Rmax=FileRmax;
			
	//context.getConfiguration().setFloat("MINIMUM",  Rmin.floatValue());
	//context.getConfiguration().setFloat("MAXIMUM", Rmax.floatValue());
    	 

    System.out.println("In Cleanup:1:After read values from file input in Map::MAX magnitude"+Rmax+"MIN magnitude"+Rmin);
     System.out.println("MAX phase"+Rmax+"MIN phase"+Rmin);

     FSDataOutputStream dos;

	 dos=fs.create(strFilePath, true);

	 //min Magnitude
	 dos.writeDouble(Rmin);
	 //max Magnitude
	 dos.writeDouble(Rmax);
	 
	 dos.close();
	   
	}

	
	
	
	
	
	public void map(LongWritable key, Text value,
			Context context) throws IOException, InterruptedException {
		String line=value.toString();
		StringTokenizer st=new StringTokenizer(line);
		key.set(Long.parseLong(st.nextToken())*width);
		key.set(key.get()+Long.parseLong(st.nextToken()));
	        
		DoubleWritable val=new DoubleWritable(Double.parseDouble(st.nextToken()));
		DoubleWritable val1=new DoubleWritable();
		
		if(st.hasMoreElements())
		 val1.set((Double.parseDouble(st.nextToken())));
		else
			 val1.set(0);
		val.set(Math.atan2(val1.get(),val.get()));
	//	if((int)Math.floor(val.get())==0);
		//else
		
		{
		if(val.get()<Rmin)
			Rmin=val.get();
		if(val.get()>Rmax)
			Rmax=val.get();
		}
		context.write(key,val);
	
	
	}
	

}