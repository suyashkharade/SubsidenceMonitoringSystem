package window;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

public class Map1 extends
		Mapper<LongWritable, Text, LongWritable, DoubleWritable> {

	long startPixelno;
	long pixel1, line1, pixel2, line2;
	long space, val, val1;
	long width, Wpixels, Wlines, Nwin = 1000, WinSizep, WinSizel;
	long height;
	static   long Fh,Fv;
	  
	/* Setup method is just to know space between two windows */
	@Override
	protected void setup(Context context) throws IOException,
			InterruptedException {
		/* you need to set Fh,Fv explicitly*/
		Fh=-217;
		  Fv=-6;
		
		  pixel1=(0-(Fh));
		if(pixel1<0)
			pixel1=0;
		
		pixel1+=20;             //20 for border
		//pixel1 = 27;
		
		line1=(0-(Fv));
		if(line1<0)
			line1=0;
		
		line1+=20;
      	//line1 = 20;
		
		pixel2=(4899-(Fh));
		if(pixel2>4899)
			pixel2=4899;
		
		pixel2-=20;
		//pixel2 = 4879;
		
		line2=(25999-(Fv));
		if(line2>25999)
			line2=25999;
		
		line2-=20;
		//line2 = 25828;
		
		WinSizep = WinSizel = 64;
		startPixelno = (line1) * 4900 + pixel1;

		width = pixel2 - pixel1 + 1;
		height = line2 - line1 + 1;

		
//refer ysrao documentation for start formulaes
		//This everything is to fit windows with adequate space in between
		Wpixels = Math.round(Math.sqrt((Nwin * width) / height));
		Wlines = Math.round(Nwin / Wpixels);

		//val and val1 two because scene can differs in no. of lines and pixels
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
	}

	@Override
	public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		long current;
		long line_no, pixel_no;
		long horizontal_window_no, vertical_window_no;
		String line = value.toString();
		StringTokenizer st = new StringTokenizer(line);
		current = Long.parseLong(st.nextToken());

		line_no = (current / width);
		horizontal_window_no = line_no / (WinSizep + space);
		pixel_no = (current % width);
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

			context.write(key,
					new DoubleWritable(Double.parseDouble(st.nextToken())));

		}

	}

}
