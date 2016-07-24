package ers_read;

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
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

public class Reduce extends  Reducer<LongWritable,DoubleWritable,Text,DoubleWritable> {
	
	private MultipleOutputs<Text, DoubleWritable> multipleOutputs;
	
static	int width = 4900;// 3312
static	int height =26000;// 1738
static Text txtkey;	
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
	
	 static Double maxMag,minMag,minPhase,maxPhase;
		
	// Store the image using the PNG format.
	//@Override
	protected void cleanup(Context context)
	throws IOException, InterruptedException {
		multipleOutputs.close();
	
		//Extract Min Max from file
        //min Magnitude
    minMag=(double) (context.getConfiguration().getFloat("MINIMUM_MAGNITUDE", 1000000));
	    //max Magnitude
    maxMag=(double) (context.getConfiguration().getFloat("MAXIMUM_MAGNITUDE", 0));		 
	    //min Phase
    minPhase=(double) (context.getConfiguration().getFloat("MINIMUM_PHASE", 1000000));
        //max Phase
    maxPhase=(double) (context.getConfiguration().getFloat("MINIMUM_PHASE", 0));		 

    System.out.println("In Setup:1:After read values from file input in Map::MAX magnitude"+maxMag+"MIN magnitude"+minMag);
    System.out.println("MAX phase"+maxPhase+"MIN phase"+minPhase);

		
	}
	
	//@Override
	 protected void setup(Context context) throws IOException, InterruptedException {
		multipleOutputs  = new MultipleOutputs<Text,DoubleWritable>(context);
	txtkey=new Text();
	 
		
		     
	 }
	
	  Integer mag;
	  Integer phase;
	 
	  public void reduce(LongWritable key, Iterable<DoubleWritable> values,
			Context context) throws IOException, InterruptedException {
		
	
		  // repltace KeyType with the real type of your key
	
			//Double value1=values.iterator().next().get();  
			
	//mag=(int)((((value1-0.0)/(80250.84-0.0))*255));
     if(key.get()%2==0)
     {	
/*    	mag=(int)((((value1-RminMag)/(RmaxMag-RminMag))*255));
        w=(int) ((key.get()/2)%4900);
		h=(int) ((key.get()/2)/4900);
	
		raster.setSample(w, h, 0,mag);
		
		
*/
    	 key.set(key.get()/2);
    	 txtkey.set(""+key.get()/width+"\t"+key.get()%width);
    	 multipleOutputs.write(txtkey,values.iterator().next(),"/app/hadoop/tmp/slc1/Magnitude_ersDH012");
	  }	
     else
     {
			//phase=(int)((((value2-(-1.5707963))/(3.145926))*255));
			/*phase=(int)(((value1-RminPhase)/(RmaxPhase-RminPhase))*255);
			 w=(int) ((key.get()/2)%4900);
				h=(int) ((key.get()/2)/4900);
				praster.setSample(w, h, 0,phase);
				*/
    	 key.set(key.get()/2);
    	 txtkey.set(""+key.get()/width+"\t"+key.get()%width);
    	 
    	 multipleOutputs.write(txtkey,values.iterator().next(),"/app/hadoop/tmp/slc1/Phase_ersDH012");
     }		
			
		
	}

}
