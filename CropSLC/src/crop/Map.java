package crop;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;


public class Map extends Mapper<LongWritable,Text,LongWritable,Text>{
	/* Need to set horizontal and vertical offsets according to baseline module output*/
	int Fv=-217,Fh=-6;

	/* Need to set ch according to master or slave file to be crop*/
	static char ch='m';
	Text mag;
	Double tmp;
	Double x;
	static int startLine=0;
	static int endLine=25999;
	static int startPixel=0;
	static int endPixel=4899;
	static int Newwidth;
	static Text txtval;
	static int mL0,mLN,mP0,mPN;
	 protected void setup(Context context) throws IOException, InterruptedException {
		 
		if(ch=='m')
		{
			Fv=-Fv;
			Fh=-Fh;
		}
		   mL0=startLine-Fv;
			if(mL0<startLine)
				mL0=startLine;
		
			mLN=endLine-Fv;
			
			if(mLN>endLine)
				mLN=endLine;
			
			mP0=startPixel-Fh;
			if(mP0<startPixel)
				mP0=startPixel;
			
			mPN=endPixel-Fh;

			if(mPN>endPixel)
				mPN=endPixel;
			
			Newwidth=mPN-mP0+1;
			txtval=new Text();
			 
		 }
	public void map(LongWritable key, Text value,
			Context context) throws IOException, InterruptedException {
		
		String line=value.toString();
		StringTokenizer st=new StringTokenizer(line);
		
			int	lineNo=(Integer.parseInt(st.nextToken()));
             
			if(lineNo>=mL0 && lineNo <=mLN)
			{	
				int	pixelNo=(Integer.parseInt(st.nextToken()));
	             	
		        if(pixelNo>=mP0 && pixelNo<=mPN)
		        {	
				lineNo-=mL0;
				pixelNo-=mP0;
				key.set(lineNo*Newwidth*2+pixelNo*2);
				txtval.set(st.nextToken()+"\t");
					txtval.set(txtval.toString()+st.nextToken());
		        	context.write(key,txtval);
					
             	}
			}
	}
}