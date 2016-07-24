
package fft;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.Reducer.Context;

public class Reduce extends Reducer<LongWritable,DoubleWritable,LongWritable,DoubleWritable> {
static long count=0,win_no=0;
static double avgr1[]=new double[1000];
static double avgi1[]=new double[1000];
static double avgr2[]=new double[1000];
static double avgi2[]=new double[1000];
static double factor[]=new double[1000];
static double sumnu=0,sumd1=0,sumd2=0;
static double tmp;
static double max_corelation,min_corelation; // 7/1/2015
static double tmpreal1,tmpimg1,tmpreal2,tmpimg2;
protected void setup(Context context) throws IOException, InterruptedException {

 max_corelation=-9999;
 min_corelation=9999;
}
  public void reduce(LongWritable key,  Iterable<DoubleWritable> values,
				Context context) throws IOException, InterruptedException {

	  
		  
	  if(key.get()<0)
			{
				if(-key.get()%4==1)
					avgr1[(int)(-key.get()/4)]= values.iterator().next().get();
				
				if(-key.get()%4==2)
					avgi1[(int)(-key.get()/4)]= values.iterator().next().get();
				
				if(-key.get()%4==3)
					avgr2[(int)(-key.get()/4)]= values.iterator().next().get();
				
				if(-key.get()%4==0)
					avgi2[((int)(-key.get()/4))-1]= values.iterator().next().get();
				
			}
			else
			{
				if(count<4096*4)
				{
					count++;
					if(key.get()%4==0)
						tmpreal1=values.iterator().next().get()-avgr1[(int) (key.get()/4/64/64)];
					if(key.get()%4==1)
					{
						tmpimg1=values.iterator().next().get()-avgi1[(int) (key.get()/4/64/64)];
						tmp=Math.sqrt(tmpreal1*tmpreal1+tmpimg1*tmpimg1);
						sumd1=sumd1+tmp*tmp;
					}
					if(key.get()%4==2)
						tmpreal2=values.iterator().next().get()-avgr2[(int) (key.get()/4/64/64)];

					if(key.get()%4==3)
					{
						tmpimg2=values.iterator().next().get()-avgi2[(int) (key.get()/4/64/64)];
						tmp=Math.sqrt(tmpreal2*tmpreal2+tmpimg2*tmpimg2);
						sumnu=sumnu+tmpreal1*tmpreal2-tmpimg1*tmpimg2;
						sumd2=sumd2+tmp*tmp;
					}
					if(count==4096*4)
					{
						count=0;
						context.write(new LongWritable(win_no++), new DoubleWritable(sumnu/Math.sqrt(sumd1*sumd2)));
						
						if(max_corelation<(sumnu/Math.sqrt(sumd1*sumd2)))
							max_corelation=(sumnu/Math.sqrt(sumd1*sumd2));
						if(min_corelation>(sumnu/Math.sqrt(sumd1*sumd2)))
							 min_corelation=(sumnu/Math.sqrt(sumd1*sumd2));
						sumnu=sumd2=sumd1=0;
						
					}
				}	
			}
		//context.write(key,new DoubleWritable(values.iterator().next().get()));
	  //count++;
	  	}
	@Override
		protected void cleanup(Context context)
				throws IOException, InterruptedException {
		System.out.println("Here is the chunk");
		for(int i=0;i<1000;i++)
		{
			System.out.println(avgr1[i]+"\t"+avgi1[i]+"\t"+avgr2[i]+"\t"+avgi2[i])	;
		}
		System.out.println("Max corelation = "+max_corelation+" Min Corelation = "+min_corelation);
		super.cleanup(context);;
		}

}
