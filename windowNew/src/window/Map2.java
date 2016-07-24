package window;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

public class Map2 extends
		Mapper<LongWritable, Text, LongWritable, Text> {

	int startPixelno,startLineno,endLineno,endPixelno;
	int pixel1, line1, pixel2, line2;
	int space, val, val1;
	int width, Wpixels, Wlines, Nwin = 25, WinSizep, WinSizel;
	int height;
	int deltaLines,deltaPixels;
	int 		horizontal_window_no,vertical_window_no;
	static   int Fh,Fv;
	Text cplxValue;
	long count;
	char ch='s';
	int border;
	/* Setup method is just to know space between two windows */
	@Override
	protected void setup(Context context) throws IOException,
			InterruptedException {
		/* you need to set Fh,Fv explicitly*/
		cplxValue=new Text();
		Fv=-217;
		  Fh=-6;
		  border=20;
			if(ch=='m')
			{
				Fv=-Fv;
				Fh=-Fh;
			}
		  
		  pixel1=(0-(Fh));
		if(pixel1<0)
			pixel1=0;
		
		pixel1+=border;             //20 for border
		//pixel1 = 27;
		
		line1=(0-(Fv));
		if(line1<0)
			line1=0;
		
		line1+=border;
      	//line1 = 20;
		
		pixel2=(4899-(Fh));
		if(pixel2>4899)
			pixel2=4899;
		
		pixel2-=border;
		//pixel2 = 4879;
		
		line2=(25999-(Fv));
		if(line2>25999)
			line2=25999;
		
		line2-=border;
		//line2 = 25828;
		
		WinSizep = 128;
		WinSizel = 512;
		startLineno=line1;
		startPixelno = pixel1;
		endLineno=line2;
		endPixelno=pixel2;

		width = pixel2 - pixel1 + 1;
		height = line2 - line1 + 1;

		
//refer ysrao documentation for start formulaes
		//This everything is to fit windows with adequate space in between
		Wpixels =(int) Math.round(Math.sqrt((Nwin * width) / height)); 
		Wlines = (int) Math.ceil(((float)Nwin / Wpixels));
		 deltaLines =Math.round(height/(Wlines)); //Mistake in ysrao as Wlines-1 has taken but we ceiled Wlines...
		 deltaPixels=Math.round(width*Wlines/(Nwin-1));
		 
		 System.out.println("Width"+width+"Height"+height+"Wpix"+Wpixels+"Wline"+Wlines+"deltaL"+deltaLines+"deltaP"+deltaPixels+"endL"+endLineno+"endP"+endPixelno);

/*		//val and val1 two because scene can differs in no. of lines and pixels
	val = width - WinSizep * Wpixels;
		val = Math.round(val / Wpixels / WinSizep);
		val = val * 10;       //I found this 10 factor important by studying full scene problems to have large space

		val1 = height - Wlines * WinSizel;
		val1 = Math.round(val1 / Wlines * WinSizel);
		val1 = val1 * 10;

		//choose minimum between val and val1 as space
		space = Math.min(val, val1);
		System.out.println("Space betwn winndows "+space);
		super.setup(context);
	05/02/2015
	*/
	}

	@Override
	public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		//long current;
	long line_no, pixel_no;
		
		String line = value.toString();
		StringTokenizer st = new StringTokenizer(line);
		//current = Long.parseLong(st.nextToken());
		line_no = Long.parseLong(st.nextToken());
		pixel_no = Long.parseLong(st.nextToken());
		
		if(line_no>=startLineno && line_no<=endLineno)
		{		
		  if(pixel_no>=startPixelno && pixel_no<=endPixelno && ((line_no-startLineno)%deltaLines)<=(WinSizel-1))
		  {
		    if(((pixel_no-startPixelno)%deltaPixels)<=(WinSizep-1))
		    {
		    	horizontal_window_no = (int) ((line_no-startLineno)/ deltaLines);
				vertical_window_no = (int) ((pixel_no-startPixelno) / deltaPixels);
               if((horizontal_window_no*Wpixels + vertical_window_no)<Nwin)//As starts from 0 and 0 to 24
				{	
		    	key.set((horizontal_window_no * WinSizel * WinSizep * Wpixels)*2
						+ (vertical_window_no * WinSizel * WinSizep)*2
						+ (((line_no-startLineno) % deltaLines)*WinSizep)*2
						+ ((pixel_no-startPixelno)% deltaPixels)*2+1);
		//	 key.set(line_no*4900+pixel_no);
				cplxValue.set(st.nextToken()+"\t"+st.nextToken());
				context.write(key,
						cplxValue);
            
                }
		   }	
		
		}
		/*
		line_no = Long.parseLong(st.nextToken());//(current / width);
		horizontal_window_no = line_no / (WinSizep + space);
		pixel_no = Long.parseLong(st.nextToken());
		vertical_window_no = pixel_no / (WinSizel + space);

		if (line_no % (WinSizel + space) < WinSizel
				&& pixel_no % (WinSizep + space) < WinSizep
				&& horizontal_window_no < Wlines
				&& vertical_window_no < Wpixels) {
			
			// formula to store key according to windows
			key.set(horizontal_window_no * WinSizel * WinSizep * Wpixels
					+ vertical_window_no * WinSizel * WinSizep
					+ (line_no % (WinSizel + space)) * WinSizep
					+ (pixel_no % (WinSizep + space)));
			cplxValue.set(st.nextToken()+"\t"+st.nextToken());
			context.write(key,
					cplxValue);
05/02/2015
*/
		}

	}

}
