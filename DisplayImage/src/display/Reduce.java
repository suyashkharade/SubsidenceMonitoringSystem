package display;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Reducer;

public class Reduce extends  Reducer<LongWritable,DoubleWritable,LongWritable,DoubleWritable> {
	
	/* You need to care about this width and height while displaying cropped or any other*/
	//int Fh=-217;
	//int Fv=-6;
	//int border=20;
	
	int Fh=-6;
	int Fv=-208;
	int border=0;
	
	/*Use this for ful scene and comment above and below*/
	 // int noOfCols=4900;
	  //int noOfLines=26000;
	int noOfCols=4900;
	int noOfLines=26000;
	/* Set mutilook factors here*/
	int multilookL=5;
	int multilookP=1;
	static int CMAP[][]=new int[3][256];
	static boolean MinMaxRead=false;
	int width = (noOfCols-Math.abs(Fh)-border*2)/multilookP;// 20 20 is border  is due to cropping error need to figure out in crop)
	int height =(noOfLines-Math.abs(Fv)-border*2)/multilookL;// 1738
	
	//int width=4894/multilookL;
	//int height=5158/multilookP;
	int h,w;
	
	BufferedImage im = new BufferedImage(width, height,
	BufferedImage.TYPE_3BYTE_BGR);
	// We need its raster to set the pixels' values.
	WritableRaster raster = im.getRaster();
 
	static long[] RegionCount=new long[101];
	// Put the pixels on the raster, using values between 0 and 255.
	
	 static Double Rmax,Rmin;
		
	// Store the image using the PNG format.
	//@Override
	protected void cleanup(Context context)
	throws IOException, InterruptedException {
		ImageIO.write(im, "PNG", new File(
	"/home/aniket/slc1/Filtered_multirefsub_colored_phase.png"));
	
		

for(int i=0;i<3;i++)
for(int j=0;j<256;j++)
  System.out.println(i+" "+j+" "+(int)CMAP[i][j]);
	System.out.println("Total count ="+count);
	for(int i=0;i<10;i++)
		 System.out.println(RegionCount[i]);
	
		
	}
	
	//@Override
	 protected void setup(Context context) throws IOException, InterruptedException {
	
		
		 
		 Path strFilePath =new Path("/app/hadoop/tmp/MinMaxDisplay_temp.txt");
		 FileSystem fs = FileSystem.get(context.getConfiguration());//.getFileSystem(job);
		 FSDataInputStream din; 
			din=fs.open(strFilePath);	
			 //min Magnitude
		Rmin=din.readDouble();
			 //max Magnitude
	Rmax=din.readDouble();		 
	 din.close();
	 count=0;
	  //Rmin=0.;
	 //Rmax=2000.;
	 System.out.println("Rmin="+Rmin+"  Rmax="+Rmax);
	BuildColorMap();
}	 
	/////***********

		
		
		/////********
		
		

	 
	 
		     
	
	 static int map;
	 static long count; 
	  public void reduce(LongWritable key, Iterable<DoubleWritable> values,
			Context context) throws IOException, InterruptedException {

		  if(MinMaxRead==false)
		  {
		//	  Rmin=(double)context.getConfiguration().getFloat("MINIMUM", 10000000);
			//  Rmax=(double)context.getConfiguration().getFloat("MAXIMUM", 0);
			  Path strFilePath =new Path("/app/hadoop/tmp/MinMaxDisplay_temp.txt");
				 FileSystem fs = FileSystem.get(context.getConfiguration());//.getFileSystem(job);
				 FSDataInputStream din; 
					din=fs.open(strFilePath);	
					 //min Magnitude
				Rmin=din.readDouble();
					 //max Magnitude
			Rmax=din.readDouble();		 
			 din.close();
			  MinMaxRead=true;
		  }	  
		  
          Double val=values.iterator().next().get();
		 
        // RegionCount[(int)(((val-(Rmin))/(Rmax-Rmin))*10)]++;
          
          map=Math.abs((int)((((val-Rmin)/(Rmax-Rmin))*255)));
          if(map>255)
        	  map=255;
          //map=Math.abs((int)((((val-Rmin)/(Rmax-Rmin))*255)));
		    
          //  if(key.get()%width==0)//testing log
			//  System.out.println((key.get()/width)+"  map="+map);//testing log
		  
	//	  if((key.get()/width)<3350||(key.get()/width)>4000)
		    w=(int) ((key.get())%width);
			 h=(int) ((key.get())/width);
		
			 
    		raster.setSample(w, h,2,CMAP[0][map]);
			//  else
		  raster.setSample(w, h, 1, CMAP[1][map]);
		  raster.setSample(w, h, 0, CMAP[2][map]);
		  //System.out.println((key.get()/width)+"  "+(key.get()%width)+"  map="+map);//testing log  
		  
		  
			  //raster.setSample(w, h, 0, map);
		 count++;
		  }
		  
	  
	  
	  public static void BuildColorMap()
	  {
	  		int maplength=256; 
	  		double m = maplength-1;				// 255
	  	     double n = Math.floor(((m+1)/4)+0.5);
	  	     double k      = 0.;
	  	     for (int ii=0; ii< (int)(0.5*n); ++ii)
	  		{
	  		  k++;
	  		  double y    = (k+n/2)/n;
	  		  CMAP[0][ii] = 0;
	  		  CMAP[1][ii] = 0;
	  		  CMAP[2][ii] = (byte)(m*y)&0xff;
	  		}
	  	     k = 0.;
	  	     for (int ii=(int)(0.5*n); ii<(int)(1.5*n); ++ii)
	  		{
	  		  k++;
	  		  double x    = k/n;
	  		  CMAP[0][ii] = 0;
	  		  CMAP[1][ii] =(byte)(m*x)&0xff;
	  		  CMAP[2][ii] = (byte)m&0xff;
	  		}
	  	   k = 0.;
	  	   double k2 = n;
	  	   for (int ii=(int)(1.5*n); ii<(int)(2.5*n); ++ii)
	  	     {
	  	     k++;
	  	     double x    = k/n;
	  	     double x2   = k2/n;
	  	     CMAP[0][ii] = (byte)(m*x)&0xff;
	  	     CMAP[1][ii] = (byte)(m)&0xff;
	  	     CMAP[2][ii] = (byte)(m*x2)&0xff;
	  	     k2--;
	  	     }
	  	   k2 = n;
	  	   for (int ii=(int)(2.5*n); ii<(int)(3.5*n); ++ii)
	  	     {
	  	     double x2   = k2/n;
	  	     CMAP[0][ii] = (byte)(m)&0xff;
	  	     CMAP[1][ii] = ((byte)(m*x2))&0xff;
	  	     CMAP[2][ii] = (byte)(0)&0xff;
	  	     k2--;
	  	     }
	  	   k2 = n;
	  	   for (int ii=(int)(3.5*n); ii<maplength; ++ii)
	  	     {
	  	     double y2   = (k2+n/2)/n;
	  	     CMAP[0][ii] = (byte)(m*y2)&0xff;
	  	     CMAP[1][ii] = (byte)0&0xff;
	  	     CMAP[2][ii] = 0;
	  	     k2--;
	  	     }

	  }
	  
	  
 }
