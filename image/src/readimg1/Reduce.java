package readimg1;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

public class Reduce extends  Reducer<LongWritable,DoubleWritable,LongWritable,DoubleWritable> {
	
	private MultipleOutputs<LongWritable,DoubleWritable> multipleOutputs;
	
	int width = 4900;// 3312
	int height =26000;// 1738
	int h,w;
	BufferedImage im = new BufferedImage(width, height,
	BufferedImage.TYPE_BYTE_GRAY);
	// We need its raster to set the pixels' values.
	WritableRaster raster = im.getRaster();
	
	BufferedImage ip = new BufferedImage(width, height,
			BufferedImage.TYPE_BYTE_GRAY);
			// We need its raster to set the pixels' values.
			WritableRaster praster = ip.getRaster();
	// Put the pixels on the raster, using values between 0 and 255.
	
	 static Double RmaxMag,RminMag,RminPhase,RmaxPhase;
		
	// Store the image using the PNG format.
	//@Override
	protected void cleanup(Context context)
	throws IOException, InterruptedException {
		multipleOutputs.close();
	
         
		ImageIO.write(im, "PNG", new File(
	"/app/hadoop/tmp/slc1/Magimg_ers.png"));
	ImageIO.write(ip, "PNG", new File(
			"/app/hadoop/tmp/slc1/Phaseimg_ers.png"));
	
	Path strFilePath =new Path("/app/hadoop/tmp/MinMax_Phase_Mag.txt");
	
	FileSystem fs = FileSystem.get(Img1.conf);
	fs.delete(strFilePath);
	
	}
	
	//@Override
	 protected void setup(Context context) throws IOException, InterruptedException {
		multipleOutputs  = new MultipleOutputs<LongWritable,DoubleWritable>(context);
		Path strFilePath =new Path("/app/hadoop/tmp/MinMax_Phase_Mag.txt");
		 FileSystem fs = FileSystem.get(Img1.conf);//.getFileSystem(job);
		 FSDataInputStream din; 
			din=fs.open(strFilePath);	
			 //min Magnitude
		RminMag=din.readDouble();
			 //max Magnitude
	RmaxMag=din.readDouble();		 
			//min Phase
	RminPhase=din.readDouble();
	//max Phase
	RmaxPhase=din.readDouble();	
	 din.close();
	 
		
		     
	 }
	
	  Integer mag;
	  Integer phase;
	 
	  public void reduce(LongWritable key, Iterable<DoubleWritable> values,
			Context context) throws IOException, InterruptedException {
		
	
		  // repltace KeyType with the real type of your key
	
			Double value1=values.iterator().next().get();  
			
	//mag=(int)((((value1-0.0)/(80250.84-0.0))*255));
     if(key.get()%2==0)
     {	
    	 mag=(int)((((value1-RminMag)/(RmaxMag-RminMag))*255));
        w=(int) ((key.get()/2)%4900);
		h=(int) ((key.get()/2)/4900);
	
		raster.setSample(w, h, 0,mag);
		key.set(key.get()/2);
		
			//	multipleOutputs.write(key,new DoubleWritable(RmaxMag),"/media/ubuntu/691fda02-7f4f-4c71-9555-b0a10426333b/Maxmag");

			multipleOutputs.write(key,new DoubleWritable(value1),"/app/hadoop/tmp/slc1/Magnitude_ersDH");
	  }	
     else
     {
			//phase=(int)((((value2-(-1.5707963))/(3.145926))*255));
			phase=(int)(((value1-RminPhase)/(RmaxPhase-RminPhase))*255);
			 w=(int) ((key.get()/2)%4900);
				h=(int) ((key.get()/2)/4900);
				praster.setSample(w, h, 0,phase);
				key.set(key.get()/2);
				
		multipleOutputs.write(key,new DoubleWritable(value1),"/app/hadoop/tmp/slc1/Phase_ersDH");
     }		
			
		
	}

}
