package fft;

import java.io.IOException;
import java.util.StringTokenizer;


import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

public class Map1 extends Mapper<LongWritable,Text,LongWritable,DoubleWritable>{
	
	double temp;
	static int points;
	static double twiddlereal[]=new double[64];	//real part of twiddle matrix
	static double twiddleimg[]=new double[64];	//imaginary part of twiddle matrix
	static int count=0;
	static double mat[][]=new double[64][64];	//real part of window
	static double mati[][]=new double[64][64];  //imaginary part of window
	static long keys[]=new long[64*64];
	static int window_no=0;
	static int k;
	static double matreal[]=new double[64];		//real part of fft
	static double matimg[]=new double[64];		//imaginary part of fft
	static double tmpreal[]=new double[64];		//real part of 2nd stage of fft
	static double tmpimg[]=new double[64];		//imaginary part of 2nd stage of fft
	static double avg1,avg2;
	

		
	void transpose(double a[][])
	{
		for(int i=0;i<64;i++)
			for(int j=i;j<64;j++)
			{
				if(i<j)
				{
					temp=a[i][j];
					a[i][j]=a[j][i];
					a[j][i]=temp;
				}
			}
	}
	
	void genbutterfly(int start,int points)
	{
	int m=64/points;
		for(int j=0,i=start;i<points/2;i++,j=j+m)
		{
			tmpreal[i]=matreal[i]+matreal[i+points/2];
			tmpimg[i]=matimg[i]+matimg[i+points/2];

			tmpreal[i+points/2]=matreal[i]-matreal[i+points/2];
			tmpimg[i+points/2]=matimg[i]-matimg[i+points/2];
			
			tmpreal[i+points/2]=matreal[i+points/2]*twiddlereal[j];
			tmpreal[i+points/2]=matimg[i+points/2]*twiddleimg[j];
			
			tmpimg[i+points/2]=matreal[i+points/2]*twiddleimg[j];
			tmpimg[i+points/2]=matimg[i+points/2]*twiddlereal[j];
		}
		for(int i=start;i<points/2;i++)
		{
			matreal[i]=tmpreal[i];
			matimg[i]=tmpimg[i];
		}
	}
	/*void finale()
	{
	int start;
	points=64;
	
	for(int i=0;i<Math.log10(64)/Math.log10(2);i++)
	{  
		//points=64;
		
		start=0;
		
		for(int j=0;j<Math.pow(2, i);j++)
		{
			
			genbutterfly(start,points);
			start=start+points;		
		
		}
		
		points=points/2;
		
	}
	
	}*/
	@Override
	protected void setup(Context context)
			throws IOException, InterruptedException {
		
		for(int i=0;i<32;i++)
		{
			twiddlereal[i]=Math.cos(-(i*Math.PI/32));
			twiddleimg[i]=Math.sin(-(i*Math.PI/32));
		}
		super.setup(context);
		/*InputSplit split = context.getInputSplit();
		Class<? extends InputSplit> splitClass = split.getClass();

		FileSplit fileSplit = null;
		if (splitClass.equals(FileSplit.class)) {
		    fileSplit = (FileSplit) split;
		} else if (splitClass.getName().equals(
		        "org.apache.hadoop.mapreduce.lib.input.TaggedInputSplit")) {
		    // begin reflection hackery...

		    try {
		        Method getInputSplitMethod = splitClass
		                .getDeclaredMethod("getInputSplit");
		        getInputSplitMethod.setAccessible(true);
		        fileSplit = (FileSplit) getInputSplitMethod.invoke(split);
		    } catch (Exception e) {
		        // wrap and re-throw error
		        throw new IOException(e);
		    }

		    // end reflection hackery
		}
*/	
	}
	
	
	
	public void map(LongWritable key, Text value,
			Context context) throws IOException, InterruptedException {

		String line=value.toString();
		StringTokenizer st=new StringTokenizer(line);
		keys[count]=Long.parseLong(st.nextToken());
		k=(int)(keys[count]%(long)(64*64));
		mat[k/64][k%64]=Double.parseDouble(st.nextToken());
		count++;
		
		Complex y[]=new Complex[64];
		if(count==4096)
		{
			
		count=0;
		Complex x[]=new Complex[64];
		for(int j=0;j<64;j++)
		{
			for (int i = 0; i < 64; i++) {
	            //x[i] = new Complex(i, 0);
	            x[i] = new Complex(mat[j][i], 0);
	        }
			y=FFT.fft(x);
			for(int i=0;i<64;i++)
			{
				mat[j][i]=y[i].re();
				mati[j][i]=y[i].im();
			}
		}
		transpose(mat);
		transpose(mati);
		for(int j=0;j<64;j++)
		{
			for (int i = 0; i < 64; i++) {
	            //x[i] = new Complex(i, 0);
	            x[i] = new Complex(mat[j][i],mati[j][i] );
	        }
			y=FFT.fft(x);
			for(int i=0;i<64;i++)
			{
				mat[j][i]=y[i].re();
				mati[j][i]=y[i].im();
			}
		}
		transpose(mat);
		transpose(mati);
		
		int cnt=0;
		avg1=avg2=0;
		for(int i=0;i<64;i++)
		{
			for(int j=0;j<64;j++)
			{
			context.write(new LongWritable(4*keys[cnt]), new DoubleWritable(mat[i][j]));
			avg1=avg1+mat[i][j];
			context.write(new LongWritable(4*keys[cnt]+1), new DoubleWritable(mati[i][j]));
			avg2=avg2+mati[i][j];
			cnt++;
			}
		}
		context.write(new LongWritable(-(4*(keys[cnt-1]/4096)+1)), new DoubleWritable(avg1/4096));
		context.write(new LongWritable(-(4*(keys[cnt-1]/4096)+2)), new DoubleWritable(avg2/4096));
		/*
		   for(int row_no=0;row_no<64;row_no++)
			{
				for(int i=0;i<64;i++)
				{
					matreal[i]=mat[i][row_no];
					matimg[i]=0;
				}
				
				finale();
				
				for(int i=0;i<64;i++)
				{
					mat[i][row_no]=matreal[i];
					mati[i][row_no]=matimg[i];
				}
			}
			
			transpose(mat);
			transpose(mati);
			
			for(int row_no=0;row_no<64;row_no++)
			{
				for(int i=0;i<64;i++)
				{
					matreal[i]=mat[i][row_no];
					matimg[i]=0;
				}
				
				finale();
				
				for(int i=0;i<64;i++)
				{
					mat[i][row_no]=matreal[i];
					mati[i][row_no]=matimg[i];
				}
			}
			count=0;
			transpose(mat);
			transpose(mati);
			
			int cnt=0;
			avg1=avg2=0;
			for(int i=0;i<64;i++)
			{
				for(int j=0;j<64;j++)
				{
				context.write(new LongWritable(keys[cnt]), new DoubleWritable(mat[i][j]));
				avg1=avg1+mat[i][j];
				context.write(new LongWritable(keys[cnt]), new DoubleWritable(mati[i][j]));
				avg2=avg2+mati[i][j];
				cnt++;
				}
			}
			context.write(new LongWritable(-keys[cnt-1]/4096), new DoubleWritable(avg1/4096));
			context.write(new LongWritable(-keys[cnt-1]/4096), new DoubleWritable(avg2/4096));
		
		 */
		
		}		
	}
}