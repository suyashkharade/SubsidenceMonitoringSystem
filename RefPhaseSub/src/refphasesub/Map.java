package refphasesub;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


public class Map extends Mapper<LongWritable,Text,LongWritable,Text>{
	/* Need to set horizontal and vertical offsets according to baseline module output*/
	int Fh=-6;
	int Fv=-208;
	int border=0;
	
	/*Use this for ful scene and comment above and below*/
	int noOfCols=4900;
	  int noOfLines=26000;
	
	/* Set mutilook factors here*/
	int multilookL=1;
	int multilookP=1;

	int width = (noOfCols-Math.abs(Fh)-border*2)/multilookP;// 20 20 is border  is due to cropping error need to figure out in crop)
	int height =(noOfLines-Math.abs(Fv)-border*2)/multilookL;// 1738
	
	double[] Acoeff;
	Text txtval;
	 
	 protected void setup(Context context) throws IOException, InterruptedException {
		   Path strFilePath =new Path("/app/hadoop/tmp/RefPhaseEqnCoeff_temp.txt");
			int degree=5;
		   int no_coeff_unknowns=(((degree+1)*(degree+1)+(degree+1))/2);
			 Acoeff=new double[no_coeff_unknowns];
			 txtval=new Text();
			 FileSystem fs=FileSystem.get(context.getConfiguration());
			 FSDataInputStream din; 
				
			    din=fs.open(strFilePath);	

																
		    for(int i=0;i<no_coeff_unknowns;i++)
		    {			 Acoeff[i]=din.readDouble();
		    System.out.println("A"+i+"=="+Acoeff[i]);
		    }
			 		 
			 din.close();
			// fs.close();
	 
			 Acoeff[0]=11208.5251;
				Acoeff[1]=9.02380686;		
				Acoeff[2]=642.290448;		    
				Acoeff[3]=-0.156357983;
				Acoeff[4]=-0.247212965;
				Acoeff[5]=-34.3360742;
				Acoeff[6]=-0.0121050044;		
				Acoeff[7]=-0.00268905423;		    
				Acoeff[8]=0.0185629965;
				Acoeff[9]=2.59577292;
						Acoeff[10]= 0.0000343117815;
								Acoeff[11]=-0.000367955814;		
								Acoeff[12]=-0.0000340803322;		    
								Acoeff[13]=-0.00269708920;
								Acoeff[14]=-0.267711415;
								Acoeff[15]=0.0000208722968;
								Acoeff[16]=	-0.0000294609908;		
								Acoeff[17]=0.0000212877063;		    
								Acoeff[18]= 0.0000805567349;
								Acoeff[19]=0.000383114947;		
										Acoeff[20]= 0.0297956013;							    	    
					    
			 
		 }
	public void map(LongWritable key, Text value,
			Context context) throws IOException, InterruptedException {
		
		String line=value.toString();
		StringTokenizer st=new StringTokenizer(line);
		
			//int	xline=(Integer.parseInt(st.nextToken()));
			double	xline=(Double.parseDouble(st.nextToken()));
	             
				double	ypixel=(Double.parseDouble(st.nextToken()));
	             	

				double a=Double.parseDouble(st.nextToken());
				
				double b=Double.parseDouble(st.nextToken());
				
				key.set((int)xline*width+(int)ypixel);
               
			//	xline=xline*multilookL+multilookL/2;
				//ypixel=ypixel*multilookP;
				  //scaling of X & Y
				xline-=(0.5*26000);
				xline/=(0.25*26000);
				ypixel-=(0.5*4900);
				ypixel/=(0.25*4900);

				double Rphi=1*Acoeff[0];
						Rphi+=Acoeff[1]*xline;
				Rphi+=Acoeff[2]*ypixel;
				Rphi+=Acoeff[3]*xline*xline;
				Rphi+=Acoeff[4]*xline*ypixel;
				Rphi+=Acoeff[5]*ypixel*ypixel;
				Rphi+=Acoeff[6]*Math.pow(xline,3);
				Rphi+=Acoeff[7]*xline*xline*ypixel;
				Rphi+=Acoeff[8]*xline*ypixel*ypixel;
				Rphi+=Acoeff[9]*Math.pow(ypixel,3);
				Rphi+=Acoeff[10]*Math.pow(xline,4);
				Rphi+=Acoeff[11]*Math.pow(xline,3)*ypixel;
				Rphi+=Acoeff[12]*Math.pow(xline,2)*Math.pow(ypixel,2);
				Rphi+=Acoeff[13]*xline*Math.pow(ypixel,3);
				Rphi+=Acoeff[14]*Math.pow(ypixel,4);
				Rphi+=Acoeff[15]*Math.pow(xline,5);
				 Rphi+=Acoeff[16]*Math.pow(xline,4)*ypixel;
				 Rphi+=Acoeff[17]*Math.pow(xline,3)*Math.pow(ypixel,2);
				 Rphi+=Acoeff[18]*Math.pow(xline,2)*Math.pow(ypixel,3);
				 Rphi+=Acoeff[19]*xline*Math.pow(ypixel,4);
				 Rphi+=Acoeff[20]*Math.pow(ypixel,5);
				 double c=Math.cos(Rphi);
				 double d=Math.sin(Rphi);
			//	System.out.println("Rph"+Rphi+"  c="+"  d"+d);
					
				 /*Complex Conjugate Multiplication  (a+bi).(c-di)   */
				 double Real=a*c+b*d;
				 double Imag=b*c-a*d; 
				 txtval.set(""+Real+"\t"+Imag);
		        	context.write(key,txtval);
					
             	}
			
	}
