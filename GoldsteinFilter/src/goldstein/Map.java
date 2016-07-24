package goldstein;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.*;
import org.jblas.ComplexDoubleMatrix;


public class Map extends Mapper<LongWritable, Text,LongWritable,Text>{
static int Tileheight;
static	int width=4900-6;
static	ComplexDoubleMatrix data;
static long keys[][];
public static final String LINES_PER_MAP = "mapreduce.input.lineinputformat.linespermap";
public static final String OVERLAP_LINES=  "mapreduce.input.lineinputformat.overlaplines";
static int numLines;
static int overlapLines;
static int linecount;
static int pixelcount;
static long startkey;
static PhaseFilter goldstein;
Text txtkey,txtval;
LongWritable longkey;
long count;
static boolean window_out;
protected void setup(Context context) throws IOException, InterruptedException {
	numLines=context.getConfiguration().getInt(LINES_PER_MAP, 1);
    overlapLines=context.getConfiguration().getInt(OVERLAP_LINES, 1);	
  System.out.println(" num lines"+numLines+" width"+width);
    count=0;
    linecount=pixelcount=0;    
    Tileheight=numLines/width;
    data = new ComplexDoubleMatrix(Tileheight,width);
longkey=new LongWritable();
txtval=new Text();
window_out=false;

}
protected void cleanup(Context context) throws IOException, InterruptedException {
System.out.println("Count="+count);
if(window_out==false)//required if last tile does not occupy filtering window size
{
	//double kernelArray[]={1,1,1,1,1};
	
	//
	  // float goldstAlpha=0.7f;
	   //goldstein = new PhaseFilter("goldstein", data, Math.lolinecount, 0,
		//		kernelArray, goldstAlpha);

	for (int i = 0; i < linecount; i++)
	{	
      for(int j=0;j<width;j++)
      {	
    	  
       //   values.set(goldstein.getData().getRow(i).imag().get()get(i, j).toString().trim());
    	  // values.set(""+goldstein.getData().get(i, j).real()+"\t"+goldstein.getData().get(i, j).imag());
		  longkey.set(startkey);
    	  txtval.set(data.get(i,j).real()+"\t"+data.get(i, j).imag());   
    	  context.write(longkey,txtval);
    	 startkey++;
        //  key.set(key.get()+1);
          count++;
      }
	}
}

System.out.println("Count="+count);

}
	public void map(LongWritable key, Text values,
			Context context) throws IOException, InterruptedException {
	String line=values.toString();
	StringTokenizer st=new StringTokenizer(line);
       
	 
	   if(linecount==0 && pixelcount==0)
	   {   startkey=Long.parseLong(st.nextToken());
	      startkey=startkey*width+Long.parseLong(st.nextToken());   
	      key.set(startkey);
	   }
	   else
	   {   st.nextToken();st.nextToken();
	   
	   }
	   
	   data.put(linecount, pixelcount,Double.parseDouble(st.nextToken()),Double.parseDouble(st.nextToken()));
	
	   pixelcount++;
	  if(pixelcount==width)
	  {
		  linecount++;
		  if(linecount==Tileheight)
		  {
				init();
				System.out.println("Copying done...!");

				goldstein.filter();
				
				System.out.println("Copying done...!");
				for (int i = 0; i < 15; i++)
				{	
					System.out.println(goldstein.getData().get(0, i));
					System.out.println("Absolute "+Math.sqrt(Math.pow(goldstein.getData().get(0, i).real(),2)+Math.pow(goldstein.getData().get(0, i).imag(),2)));
				
				}
               
				key.set(startkey);
				for (int i = 0; i < Tileheight; i++)
				{	
			      for(int j=0;j<width;j++)
			      {	
			    	  
			       //   values.set(goldstein.getData().getRow(i).imag().get()get(i, j).toString().trim());
			    	   values.set(""+goldstein.getData().get(i, j).real()+"\t"+goldstein.getData().get(i, j).imag());
					     
			    	  //txtkey.set((startkey/width)+"\t"+(startkey%width));  for testing
			          context.write(key,values);
			    	// startkey++;
			          key.set(key.get()+1);
			          count++;
			      }
				}
             window_out=true;
				linecount=0;	  
		  }
		  pixelcount=0;
		//context.getConfiguration().
		  
	  }
	
		
	
	
	
	}
	
	
	

	public static void init() throws IOException {

	double kernelArray[]={1,1,1,1,1};
	
			
   float goldstAlpha=0.7f;
   goldstein = new PhaseFilter("goldstein", data, 32, 12,
			kernelArray, goldstAlpha);
	
}

	public static int getNumLinesPerSplit(JobContext job) {
		return job.getConfiguration().getInt(LINES_PER_MAP, 1);
	}
	public static int getOverlapLinesPerSplit(JobContext job) {
		return job.getConfiguration().getInt(OVERLAP_LINES, 1);
	}

	
	
}
