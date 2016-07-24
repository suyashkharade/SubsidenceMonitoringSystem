
package refphase;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;

public class Previous {

	
	
	
	
	//static double A[][]=new double[5][4],LT[][]=new double[4][4],AT[][]=new double[4][5],ATA[][]=new double[4][4],L[][]=new double[4][4];
	
	static byte[] buf=new byte[20000];
	static double a[][]=new double [4][1];
	static double b[][]=new double [4][1];
	static double c[][]=new double [4][1];
	static double d[][]=new double [4][1];
	static double e[][]=new double [4][1];
	static double f[][]=new double [4][1];
	
	static double slave_a[][]=new double [4][1];
	static double slave_b[][]=new double [4][1];
	static double slave_c[][]=new double [4][1];
	static double slave_d[][]=new double [4][1];
	static double slave_e[][]=new double [4][1];
	static double slave_f[][]=new double [4][1];
	
	static double master_centre_lat,master_centre_long;
	static  int master_centerline,master_centerpixel;
	static	double master_SF,master_PRF,master_dR;
	static	double master_tr0,master_ta0;
	static double master_norm_temp;
	
	static double slave_centre_lat,slave_centre_long;
	static  int slave_centerline,slave_centerpixel;
	static	double slave_SF,slave_PRF,slave_dR;
	static	double slave_tr0,slave_ta0;
	static double slave_norm_temp;
	
	static double mx,my,mz,sx,sy,sz,px,py,pz;
	static double wavelength; 
	static int lines=26000,pixels=4900;
	static double SOL=299792458.0;
	public static void main(String args[]) throws FileNotFoundException, IOException
	{
	
		ReadMasterOrbit();
		ReadSlaveOrbit();
		
		//////
		
    	Double a_temp,b_temp,c_temp,d_temp,e_temp,f_temp,g_temp,h_temp,i_temp;
		Double x0,y0,z0,x1,y1,z1;
		Double determinant;
		Double doppler,range,elipsoid;
		Double [][]inverse=new Double[3][3];
		Double []fx0=new Double[3];
		
        Double major,minor,ecc,phi,lambada,N,Nh,height;
        Double x,y,z,vx,vy,vz;
        Double master_tac,master_trc=0.0;
        Double slave_tac,slave_trc=0.0;       
                      

                
                //Initialize major ,minor axis according to file
                height=0.0;
                major=6378137.000;//6378144.00;
                minor=6356752.3141;//6356759.0;
                System.out.println("lat  in file ===="+master_centre_lat);    

                phi=Math.PI*master_centre_lat/180;
                lambada=Math.PI*master_centre_long/180;
                System.out.println("phiiiiiiiiii"+master_centre_long+" ===="+phi);    

 
                          
                
                
                ecc=(1-(Math.pow(minor,2)/Math.pow(major,2)));
                N=major/(Math.pow((1-(Math.pow(ecc,2)*Math.pow(Math.sin(phi),2))), 0.5));
                Nh=N+height;
           
                
        

                //ground point 423127.92175588186   5838633.42124944   2532144.556908548 
                //xyzvx 149082.7179526584	6605669.689256542	2762407.639426618	1747.4107626354905	2811.7001838423876	-6791.2364710350685	T azimuth 16.292544602407993T masterline -2.9167783731701322E7T range no.2626.110056926864 R1  850047.7950000067

               //tac ====17386.820546653315
               // trc ====0.005581358244725738
                
      ///       for  loop for N points will start here  
int Npoints=1;
int degree=5;
int wl,wp;
int deltaX,deltaY;
wp=(int) Math.sqrt(Npoints*pixels/(lines));
if(wp<=0)
	wp=1;

wl=(int) Math.ceil(Npoints/wp);
deltaX=lines/(wl);
//deltaY=(pixels*wl)/(Npoints-1);
deltaY=0;
int horizontal_pixel_no=0;
int line=1,pixel=4900;//calculation is 1-26000 based

double Arefphase[]=new double[Npoints];
int Axy[][]=new int[Npoints][2];
int no_coeff_unknowns=(((degree+1)*(degree+1)+(degree+1))/2);

Array2DRowRealMatrix A=new Array2DRowRealMatrix(Npoints,no_coeff_unknowns);
Array2DRowRealMatrix AT ;
Array2DRowRealMatrix refphi=new Array2DRowRealMatrix(Npoints,1);
//Array2DRowRealMatrix u=new Array2DRowRealMatrix(Npoints,coeff_unknowns);
int tmp=0;
  for(int iter=0;iter<Npoints;iter++)                
  {                      
                    
	  
	                 master_tac=master_ta0+(line-1)/master_PRF;
                  
                     
                     System.out.println("tac ===="+master_tac);
                 //    master_SF/=2;
                         master_trc=(master_tr0)*Math.pow(10,-3)+(pixel)/(master_SF*1000000);
                        
                //         master_trc/=2;
                         System.out.println("trc ===="+master_trc);
                         master_tac+=master_trc/2;

                         System.out.println("master tac"+master_tac+" temp scale"+master_norm_temp);
                
                         //Scaled tac
                         master_tac=(master_tac-master_norm_temp);
                
                         System.out.println("master tac"+master_tac);
                         
                         x0=Nh*Math.cos(phi)*Math.cos(lambada);
                         y0=Nh*Math.cos(phi)*Math.sin(lambada);
                         z0=(Nh-(Math.pow(ecc,2)*N))*Math.sin(phi);
                         

                         System.out.println("ground point "+x0+"   "+y0+"   "+z0+"   ");

                          x=a[0][0]+a[1][0]*master_tac+a[2][0]*master_tac*master_tac+a[3][0]*master_tac*master_tac*master_tac;
                         
                          y=b[0][0]+b[1][0]*master_tac+b[2][0]*master_tac*master_tac+b[3][0]*master_tac*master_tac*master_tac;
                          z=c[0][0]+c[1][0]*master_tac+c[2][0]*master_tac*master_tac+c[3][0]*master_tac*master_tac*master_tac;
                        
                         vx=a[1][0]+2*a[2][0]*master_tac+3*a[3][0]*master_tac*master_tac;
                         vy=b[1][0]+2*b[2][0]*master_tac+3*b[3][0]*master_tac*master_tac;
                         vz=c[1][0]+2*c[2][0]*master_tac+3*c[3][0]*master_tac*master_tac;
                         
                         System.out.println("Test x  "+x+" "+y+" "+z+" "+vx+" "+vy+" "+vz+" ");
                         System.out.println("a==="+a[0][0]);
                  
                         
                         
                       x1=y1=z1=0.0;
                
  mx=x;
  my=y;
  mz=z;
	
        
     
         int l=0;
        do
        {
             System.out.println("Iteration "+l++);
      
             range=(Math.pow(x0, 2)+Math.pow(y0, 2)+Math.pow(z0, 2)-(2*(x)*x0)-(2*y*y0)-(2*(z)*z0)+(Math.pow(x,2))+Math.pow(y,2)+Math.pow(z,2)- Math.pow((SOL*master_trc)/2,2));/*Math.pow(850047.795,2)); Math((3*Math(10,8 )*0.005602506333333333)/2,2));*/
		doppler=(vx*(x-x0)+vy*(y-y0)+vz*(z-z0));
		elipsoid=((Math.pow(x0, 2)/(Math.pow(major,2)))+(Math.pow(y0, 2)/(Math.pow(major,2)))+(Math.pow(z0, 2)/(Math.pow(minor,2))))-1.00;             
             
		 System.out.println(x0+"   "+y0+"   "+z0+"   ");
   
		
		 System.out.println("Doppler"+doppler+"   "+"Range"+range+"     "+"elipsoid"+elipsoid);
		
		 fx0[0]=range;
		
		fx0[1]=doppler;
		fx0[2]=elipsoid;
		
                
                
                   //For Jacobian and inverse
		
		  	   a_temp=(2*x0-2*x);
               b_temp=2*y0-2*y;
               c_temp=2*z0-2*z;
               d_temp=-vx;
               e_temp=-vy;
               f_temp=-vz;
               g_temp=2*x0/(major*major);//(4.068072088*Math.pow(10, 13));
               h_temp=2*y0/(major*major);//(4.068072088*Math.pow(10, 13));
               i_temp=2*z0/(minor*minor);//(4.040838498*Math.pow(10, 13)); 
		 

		   determinant=(double)((a_temp*(e_temp*i_temp-f_temp*h_temp))-(b_temp*(d_temp*i_temp-f_temp*g_temp))+(c_temp*(d_temp*h_temp-e_temp*g_temp)));
			

			System.out.println("det "+determinant);
				
		      
			inverse[0][0]=(1/determinant)*((e_temp*i_temp-f_temp*h_temp));
		        inverse[0][1]=(1/determinant)*(-(b_temp*i_temp-c_temp*h_temp));
		        inverse[0][2]=(1/determinant)*((b_temp*f_temp-c_temp*e_temp));
		        inverse[1][0]=(1/determinant)*(-(d_temp*i_temp-f_temp*g_temp));
		        inverse[1][1]=(1/determinant)*((a_temp*i_temp-c_temp*g_temp));
		        inverse[1][2]=(1/determinant)*(-(a_temp*f_temp-c_temp*d_temp));
		        inverse[2][0]=(1/determinant)*((d_temp*h_temp-e_temp*g_temp));
		        inverse[2][1]=(1/determinant)*(-(a_temp*h_temp-b_temp*g_temp));
		        inverse[2][2]=(1/determinant)*((a_temp*e_temp-b_temp*d_temp));

                
        for(int M=0;M<3;M++)
		{
        	double master_temp=0.0;
			for(int n=0;n<3;n++)
			{
				master_temp+=inverse[M][n]*fx0[n];
			   
			}
			
      
			if(M==0)
             x1=x0-master_temp;
            
			else if(M==1)
			 y1=y0-master_temp;

			else if(M==2)
			z1=z0-master_temp;
 
			
		}
        
            
        System.out.println("x1="+x1);
    	System.out.println("y1="+y1);
    	System.out.println("z1="+z1);
    	
    	System.out.println("Percentage Error");
    	
    	System.out.println("For x1="+(Math.abs(((x1-x0)/x1)*100)));
    	System.out.println("For y1="+(Math.abs(((y1-y0)/y1)*100)));
    	System.out.println("For z1="+(Math.abs(((z1-z0)/z1)*100)));
        
        x0=x1;
        y0=y1;
        
       //if(perz>1)
          z0=z1;
        
          
          
  }while(l<30);//(perx>1||perx<0)||(perx>1||perx<0));
        

        
        System.out.println("Ground point :x0="+x0+" y0="+y0+" z0="+z0);
        px=x0;
        py=y0;
        pz=z0;
   
   /////For slave..........
   
   
   
   //ground point 423127.92175588186   5838633.42124944   2532144.556908548 
   //xyzvx 149082.7179526584	6605669.689256542	2762407.639426618	1747.4107626354905	2811.7001838423876	-6791.2364710350685	T azimuth 16.292544602407993T masterline -2.9167783731701322E7T range no.2626.110056926864 R1  850047.7950000067

   
   slave_tac=slave_ta0+(line-1)/slave_PRF;
   System.out.println("tac ===="+slave_tac);
   slave_trc=(slave_tr0)*Math.pow(10,-3)+(pixel-1)/(2*slave_SF*1000000);
   System.out.println("trc ===="+slave_trc);
 
   //  slave_tac+=slave_trc; //check if req.
   //Scaled tac
   slave_tac=(slave_tac-slave_norm_temp);
          


//Ground point :x0=428058.1781461849 y0=5840939.894982393 z0=2517576.4973233296          
//calculated lat phiiiiiiiiii ====23.24948553039226
//t is 16.292544605832518      






double per,t1,t,ft,ftd,i0,i1,i2,i3,i4,i5;
t=slave_tac;

i5=3*(slave_a[3][0]*slave_a[3][0]+slave_b[3][0]*slave_b[3][0]+slave_c[3][0]*slave_c[3][0]);
i4=5*(slave_a[2][0]*slave_a[3][0]+slave_b[2][0]*slave_b[3][0]+slave_c[2][0]*slave_c[3][0]);
i3=4*(slave_a[1][0]*slave_a[3][0]+slave_b[1][0]*slave_b[3][0]+slave_c[1][0]*slave_c[3][0])+2*(slave_a[2][0]*slave_a[2][0]+slave_b[2][0]*slave_b[2][0]+slave_c[2][0]*slave_c[2][0]);
i2=3*(slave_a[1][0]*slave_a[2][0]+slave_a[0][0]*slave_a[3][0]+slave_b[1][0]*slave_b[2][0]+slave_b[0][0]*slave_b[3][0]+slave_c[1][0]*slave_c[2][0]+slave_c[0][0]*slave_c[3][0])-3*(slave_a[3][0]*x0+slave_b[3][0]*y0+slave_c[3][0]*z0);
i1=slave_a[1][0]*slave_a[1][0]+2*slave_a[0][0]*slave_a[2][0]+slave_b[1][0]*slave_b[1][0]+2*slave_b[0][0]*slave_b[2][0]+slave_c[1][0]*slave_c[1][0]+2*slave_c[0][0]*slave_c[2][0]-2*(slave_a[2][0]*x0+slave_b[2][0]*y0+slave_c[2][0]*z0);
i0=slave_a[0][0]*slave_a[1][0]+slave_b[0][0]*slave_b[1][0]+slave_c[0][0]*slave_c[1][0]-(slave_a[1][0]*x0+slave_b[1][0]*y0+slave_c[1][0]*z0);
       
System.out.println("t is "+t);
for(int i=0;i<=20;i++)
{
ft=i5*Math.pow(t, 5)+i4*Math.pow(t, 4)+i3*Math.pow(t, 3)+i2*Math.pow(t, 2)+i1*t+i0;

ftd=5*i5*Math.pow(t, 4)+4*i4*Math.pow(t, 3)+3*i3*Math.pow(t, 2)+2*i2*t+i1;
t1=t-(ft/ftd);

per=((t1-t)/t)*100;

t=t1;

System.out.println("t : "+t+"Percentage error in t "+per+"   ft "+ft);
}


x1=slave_a[0][0]+slave_a[1][0]*t+slave_a[2][0]*t*t+slave_a[3][0]*t*t*t;

y1=slave_b[0][0]+slave_b[1][0]*t+slave_b[2][0]*t*t+slave_b[3][0]*t*t*t;
z1=slave_c[0][0]+slave_c[1][0]*t+slave_c[2][0]*t*t+slave_c[3][0]*t*t*t;
double vx1 = slave_a[1][0]+2*slave_a[2][0]*t+3*slave_a[3][0]*t*t;
double vy1 = slave_b[1][0]+2*slave_b[2][0]*t+3*slave_b[3][0]*t*t;
double vz1 = slave_c[1][0]+2*slave_c[2][0]*t+3*slave_c[3][0]*t*t;

System.out.println("dop"+(vx1*(x1-x0)+vy1*(y1-y0)+vz1*(z1-z0)));
System.out.println("t : "+t+" tac "+slave_tac);
     	
sx=x1;
sy=y1;
sz=z1;


             //range eqn solving for range offset computation
            double R;
            R=Math.sqrt((x1-x0)*(x1-x0)+(y1-y0)*(y1-y0)+(z1-z0)*(z1-z0));
            
            System.out.println("xyzvx "+x1+"\t"+y1+"\t"+z1+"\t"+vx1+"\t"+vy1+"\t"+vz1+"\t");

System.out.println("center"+slave_tac+" "+slave_trc);
System.out.println("mx="+mx+"my="+my+"mz="+mz);
System.out.println("sx="+sx+"sy="+sy+"sz="+sz);
System.out.println("px="+px+"py="+py+"pz="+pz);
double B=Math.sqrt((mx-sx)*(mx-sx)+(my-sy)*(my-sy)+(mz-sz)*(mz-sz));


//master_trc=Math.sqrt((mx-px)*(mx-px)+(my-py)*(my-py)+(mz-pz)*(mz-pz))/(299792458);
slave_trc=Math.sqrt((sx-px)*(sx-px)+(sy-py)*(sy-py)+(sz-pz)*(sz-pz))/(SOL);
master_trc/=2;
double Bpar=(SOL)*(master_trc-slave_trc);//Math.sqrt((mx-px)*(mx-px)+(my-py)*(my-py)+(mz-pz)*(mz-pz))-Math.sqrt((sx-px)*(sx-px)+(sy-py)*(sy-py)+(sz-pz)*(sz-pz));
double refphase=(-4*Math.PI)*Bpar/(0.0565646);//wavelength;
System.out.println("Phase ="+(-4*Math.PI)*Math.sqrt((mx-px)*(mx-px)+(my-py)*(my-py)+(mz-pz)*(mz-pz))/wavelength);
System.out.println("Baseline="+B+"B par"+Bpar+"line"+line+"Pixel"+pixel+"Reference phase"+refphase+"Radar Wavelength "+wavelength);
System.out.println("master trc="+master_trc+"slave trc="+slave_trc);

Arefphase[iter]=refphase;
Axy[iter][0]=line;
Axy[iter][1]=pixel;







































































double xline,ypixel;
xline=line;
ypixel=pixel;

xline-=(0.5*26000);
xline/=(0.25*26000);
 //Normalization
//xline=(2*(xline-1)/(0.25*(lines-1)))-2;
ypixel-=(0.5*4900);
ypixel/=(0.25*4900);
//ypixel=(2*(ypixel-1)/(0.25*(pixels-1)))-2;

//Normalization Method Alternative
	//xline=(2*(xline-1)/(0.25*(lines-1)))-2;

	//ypixel=(2*(ypixel-1)/(0.25*(pixels-1)))-2;


  //Select next point
pixel+=deltaY;

if(pixel>pixels) //when pixel Y value exceeds then shift to next line
{
	pixel=1;
	line+=deltaX;
	horizontal_pixel_no=0;
}

System.out.println("line ==="+xline+"pixel"+ypixel+ "line="+line+ "Pixel="+pixel);
 A.setEntry(iter,0, 1);
 A.setEntry(iter,1, xline);
 A.setEntry(iter,2, ypixel);
 A.setEntry(iter,3, xline*xline);
 A.setEntry(iter,4, xline*ypixel);
 A.setEntry(iter,5, ypixel*ypixel);
 A.setEntry(iter,6, Math.pow(xline, 3));
 A.setEntry(iter,7, xline*xline*ypixel);
 A.setEntry(iter,8, xline*ypixel*ypixel);
 A.setEntry(iter,9, Math.pow(ypixel,3));
 A.setEntry(iter,10, Math.pow(xline,4));
 A.setEntry(iter,11, Math.pow(xline,3)*ypixel);
 A.setEntry(iter,12,Math.pow(xline,2)*Math.pow(ypixel,2));
 A.setEntry(iter,13,xline*Math.pow(ypixel,3));
 A.setEntry(iter,14,Math.pow(ypixel,4));
 A.setEntry(iter,15,Math.pow(xline,5));
 A.setEntry(iter,16,Math.pow(xline,4)*ypixel);
 A.setEntry(iter,17,Math.pow(xline,3)*Math.pow(ypixel,2));
 A.setEntry(iter,18,Math.pow(xline,2)*Math.pow(ypixel,3));
 A.setEntry(iter,19,xline*Math.pow(ypixel,4));
 A.setEntry(iter,20,Math.pow(ypixel,5));
 
}
 AT =new Array2DRowRealMatrix(no_coeff_unknowns,Npoints);
 Array2DRowRealMatrix Qx=new Array2DRowRealMatrix(no_coeff_unknowns,no_coeff_unknowns);
 
  //System.out.println(" "+A.getRowDimension()+A.getColumnDimension()+ "aa"+A);
  
  
  AT=(Array2DRowRealMatrix) A.transpose();
 Qx=AT.multiply(A);
// System.out.println("Attt"+" "+AT.getRowDimension()+AT.getColumnDimension()+ "aa");

 System.out.println( Qx.getColumnDimension()+ " "+Qx.getRowDimension()+"qqxx"+Qx);
/*
 Array2DRowRealMatrix Q=new Array2DRowRealMatrix(4,4);
 Q.addToEntry(0, 0, 5);
 Q.addToEntry(0, 1, 0);
 Q.addToEntry(0, 2, 6.55292);
 Q.addToEntry(0, 3, 0);
 Q.addToEntry(1, 0, 0);
 Q.addToEntry(1, 1, 6.55292);
 Q.addToEntry(1, 2, 0);
 Q.addToEntry(1, 3, 14.5673);
 Q.addToEntry(2, 0, 6.55292);
 Q.addToEntry(2, 1, 0);
 Q.addToEntry(2, 2, 14.5673);
 Q.addToEntry(2, 3, 0);
 Q.addToEntry(3, 0, 0);
 Q.addToEntry(3, 1, 14.5673);
 Q.addToEntry(3, 2, 0);
 Q.addToEntry(3, 3, 36.41);
*/
 System.out.println("deltaX="+deltaX+"deltaY="+deltaY);
 CholeskyDecomposition choles=new CholeskyDecomposition((RealMatrix) Qx,1*Math.pow(10,30),-100);
 
  Array2DRowRealMatrix YrefPhase=new Array2DRowRealMatrix(Arefphase); 
  ArrayRealVector b=new ArrayRealVector(AT.multiply(YrefPhase).getColumnVector(0)); //b=ATY
  
// b.addToEntry(0,29379016.43);
 //b.addToEntry(1,-274307.647);
// b.addToEntry(2,38501732.96);
 //b.addToEntry(3,-611149.314342);
/*
 b.addToEntry(0,18762843.6);
 b.addToEntry(1,409725.2761);
 b.addToEntry(2,24589055.36);
 b.addToEntry(3,910830.65);
*/
  System.out.println("b"+b);
 b=(ArrayRealVector) choles.getSolver().solve(b);
 System.out.println("deltaX="+deltaX+"deltaY="+deltaY+"x "+b);
 
//  System.out.println("det"+choles.getDeterminant()+"LLLLLLL"+choles.getL());
   System.out.println("master tac="+master_trc+"slave tac="+slave_trc+"L matrix "+choles.getL());
  //System.out.println(AT);
  //System.out.println(AT.multiply(A));
  //////////////
   
   
   
   
  // System.out.println("A matrix"+A);
   
  
   
   
   
   
   
   
		
		////
		
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	static void ReadSlaveOrbit() throws FileNotFoundException, IOException
	{
		 int len;
		 double t,interval;
			 double x[][]=new double[5][1];
			 double y[][]=new double[5][1];
			 double z[][]=new double[5][1];
			 double vx[][]=new double[5][1];
			 double vy[][]=new double[5][1];
			 double vz[][]=new double[5][1];
			 
			 
			double A[][]=new double[5][4],LT[][]=new double[4][4],AT[][]=new double[4][5],ATA[][]=new double[4][4],L[][]=new double[4][4];

		
		/* 1. Edit master leader file path here */
		
        try (DataInputStream br = new DataInputStream(new FileInputStream("/media/aniket/Study/ProjectASMA/DhanbadDataCD/SLC2/LEADER.DAT"))) {
            len=br.read(buf);
            System.out.println("Length "+len);
            
           
            wavelength=Double.parseDouble(readChar(1220,16).trim());
            System.out.println("Radar Wavelength "+wavelength);
            System.out.printf("\nProcessed scene centre geodetic latitude defined as positive to the north  of the equator and negative to the south (deg)"+readChar(836,16));
            slave_centre_lat=Double.parseDouble(readChar(836,16).trim());
            
            System.out.printf("\nProcessed scene centre geodetic longitude defined as positive to the east of the prime meridian and negative to the west. (deg.)"+readChar(852,16));
            slave_centre_long=Double.parseDouble(readChar(852,16).trim());
            
            
            System.out.printf("\nScene center line number "+readChar(1044,8));
            
            slave_centerline=Integer.parseInt(readChar(1044,8).trim());
            
            System.out.printf("\nScene center pixel number "+readChar(1052,8));
            
            slave_centerpixel=Integer.parseInt(readChar(1052,8).trim());
            
              
            System.out.printf("\nSampling rate (MHz) "+readChar(1430,16));
            slave_SF=Double.parseDouble(readChar(1430,16));
            
            
                          
            System.out.printf("\nNominal PRF (Hz)"+readChar(1654,16));
            slave_PRF=Double.parseDouble(readChar(1654,16));
            
           
/**************************************************************************************************************/                
            System.out.printf("\nPixel spacing ( in range )(meters) "+readChar(1703,16));
            
            slave_dR=Double.parseDouble(readChar(1703,16));
            
             
            System.out.print("\nZero-doppler range time of first range pixel "+readChar(2486,16));
            slave_tr0=Double.parseDouble(readChar(2486,16));
            
            System.out.print("\nZero-doppler range time of center range pixel "+readChar(2502,16));
            
            System.out.print("\nZero-doppler range time of last range pixel "+readChar(2518,16));
            
            System.out.print("\nZero-doppler azimuth time of first azimuth pixel "+readChar(2534,24));
            
            String str1=readChar(2534,24);
             int hr,min;
             
             String shr="",smin="",ssec="";
           
             double sec;
           
            int ind;
           
            for(ind=0;str1.charAt(ind)!=':';ind++)
            {}
           
            	
            shr=(str1.substring(ind-2,ind));
            smin=(str1.substring(ind+1, ind+3)); 
            
            for(ind=ind+1;str1.charAt(ind)!=':';ind++)
            {}
            System.out.println(ind);

            ssec=(str1.substring(ind+1,str1.length())); 	
            	
            //ssec.trim();
        
            
        	sec=Double.parseDouble(ssec);
            hr=Integer.parseInt(shr.trim());
            min=Integer.parseInt(smin.trim());
        	
            slave_ta0=hr*60*60+min*60+sec;

            System.out.println(sec+" "+hr+" "+min+" "+slave_ta0+" "+slave_tr0);
                         	
            System.out.print("\nZero-doppler azimuth time of center azimuth pixel "+readChar(2558,24));
            
            System.out.print("\nZero-doppler azimuth time of last azimuth pixel "+readChar(2582,24));
            

/*******************************************************************************************/                
            System.out.println("\n\nRADIOMETRIC COMPENSATION DATA RECORD\n");
            ///Sequence number THREE
            
            ///Sequence number FOUR
            
            
            System.out.println("Number of data points (up to 64)"+readChar(4366,4));
            
            System.out.println("Year  of data point. (YYYY)"+readChar(4370,4));
            
            System.out.println("Month of data point. ($$MM)"+readChar(4374,4));
            
            System.out.println("Day   of data point. ($$DD)"+readChar(4378,4));
            
            System.out.println("Day in the year (GMT)"+readChar(4382,4));
            
            System.out.println("Seconds of day (GMT) of data"+readChar(4386,22));
            
            t=Double.parseDouble(readChar(4386,22));
            
            System.out.println("Time interval between DATA points (sec)"+readChar(4408,22));
            
            interval=Double.parseDouble(readChar(4408,22));
            
            int i,j,k,p,q,r;
            i=j=k=p=q=r=0;
            System.out.println("l-st  data  point  position  vector  as latitude, longitude and altitude for airborne sensor platform, and as (X,Y,Z) coordinates for spaceborne sensor platform in a reference system such as CTS (meters))"+readChar(4612,22));
            
            x[i++][0]=Double.parseDouble(readChar(4612,22));
            
            System.out.println("l-st  data  point  position  vector  as latitude, longitude and altitude for airborne sensor platform, and as (X,Y,Z) coordinates for spaceborne sensor platform in a reference system such as CTS (meters)"+readChar(4634,22));
            
            y[j++][0]=Double.parseDouble(readChar(4634,22));
            
            System.out.println("l-st  data  point  position  vector  as latitude, longitude and altitude for airborne sensor platform, and as (X,Y,Z) coordinates for spaceborne sensor platform in a reference system such as CTS (meters)"+readChar(4656,22));
            
            z[k++][0]=Double.parseDouble(readChar(4656,22));
            
            System.out.println("lst data point velocity vector in airborne coordinates (meters/second & degrees/second)for airborne sensor platform or (X ,Y ,Z ) in a reference system such as CTS for spaceborne sensor platforms "+readChar(4678,22));
            
            vx[p++][0]=Double.parseDouble(readChar(4678,22));
            
            System.out.println("lst data point velocity vector in airborne coordinates (meters/second & degrees/second)for airborne sensor platform or (X ,Y ,Z ) in a reference system such as CTS for spaceborne sensor platforms "+readChar(4700,22));
            
            vy[q++][0]=Double.parseDouble(readChar(4700,22));
            
            System.out.println("lst data point velocity vector in airborne coordinates (meters/second & degrees/second)for airborne sensor platform or (X ,Y ,Z ) in a reference system such as CTS for spaceborne sensor platforms "+readChar(4722,22));
            
            vz[r++][0]=Double.parseDouble(readChar(4722,22));
            
            System.out.println("2-nd  data  point  position  vector  as latitude, longitude and altitude for airborne sensor platform, and as (X,Y,Z) coordinates for spaceborne sensor platform in a reference system such as CTS (meters))"+readChar(4744,22));
            
            System.out.print("i="+i);
            x[i++][0]=Double.parseDouble(readChar(4744,22));
            
            
            System.out.println("2-nd  data  point  position  vector  as latitude, longitude and altitude for airborne sensor platform, and as (X,Y,Z) coordinates for spaceborne sensor platform in a reference system such as CTS (meters)"+readChar(4766,22));
            
            y[j++][0]=Double.parseDouble(readChar(4766,22));
            
            System.out.println("2-nd  data  point  position  vector  as latitude, longitude and altitude for airborne sensor platform, and as (X,Y,Z) coordinates for spaceborne sensor platform in a reference system such as CTS (meters)"+readChar(4788,22));
            
            z[k++][0]=Double.parseDouble(readChar(4788,22));
            
            System.out.println("2-nd data point velocity vector in airborne coordinates (meters/second & degrees/second)for airborne sensor platform or (X ,Y ,Z ) in a reference system such as CTS for spaceborne sensor platforms "+readChar(4810,22));
            
            vx[p++][0]=Double.parseDouble(readChar(4810,22));
            
            System.out.println("2-nd data point velocity vector in airborne coordinates (meters/second & degrees/second)for airborne sensor platform or (X ,Y ,Z ) in a reference system such as CTS for spaceborne sensor platforms "+readChar(4832,22));
            
            vy[q++][0]=Double.parseDouble(readChar(4832,22));
            
            System.out.println("2-nd data point velocity vector in airborne coordinates (meters/second & degrees/second)for airborne sensor platform or (X ,Y ,Z ) in a reference system such as CTS for spaceborne sensor platforms "+readChar(4854,22));
            
            vz[r++][0]=Double.parseDouble(readChar(4854,22));
            
            System.out.println("3-rd  data  point  position  vector  as latitude, longitude and altitude for airborne sensor platform, and as (X,Y,Z) coordinates for spaceborne sensor platform in a reference system such as CTS (meters))"+readChar(4876,22));
            
            x[i++][0]=Double.parseDouble(readChar(4876,22));
            
            System.out.println("3-rd  data  point  position  vector  as latitude, longitude and altitude for airborne sensor platform, and as (X,Y,Z) coordinates for spaceborne sensor platform in a reference system such as CTS (meters)"+readChar(4898,22));
            
            y[j++][0]=Double.parseDouble(readChar(4898,22));
            
            System.out.println("3-rd  data  point  position  vector  as latitude, longitude and altitude for airborne sensor platform, and as (X,Y,Z) coordinates for spaceborne sensor platform in a reference system such as CTS (meters)"+readChar(4920,22));
            
            z[k++][0]=Double.parseDouble(readChar(4920,22));
            
            System.out.println("3-rd data point velocity vector in airborne coordinates (meters/second & degrees/second)for airborne sensor platform or (X ,Y ,Z ) in a reference system such as CTS for spaceborne sensor platforms "+readChar(4942,22));
            
            vx[p++][0]=Double.parseDouble(readChar(4942,22));
            
            System.out.println("3-rd data point velocity vector in airborne coordinates (meters/second & degrees/second)for airborne sensor platform or (X ,Y ,Z ) in a reference system such as CTS for spaceborne sensor platforms "+readChar(4964,22));
            
            vy[q++][0]=Double.parseDouble(readChar(4964,22));
            
            System.out.println("3-rd data point velocity vector in airborne coordinates (meters/second & degrees/second)for airborne sensor platform or (X ,Y ,Z ) in a reference system such as CTS for spaceborne sensor platforms "+readChar(4986,22));
            
            vz[r++][0]=Double.parseDouble(readChar(4986,22));
            
            System.out.println("4-th  data  point  position  vector  as latitude, longitude and altitude for airborne sensor platform, and as (X,Y,Z) coordinates for spaceborne sensor platform in a reference system such as CTS (meters))"+readChar(5008,22));
            
            x[i++][0]=Double.parseDouble(readChar(5008,22));
            
            System.out.println("4-th  data  point  position  vector  as latitude, longitude and altitude for airborne sensor platform, and as (X,Y,Z) coordinates for spaceborne sensor platform in a reference system such as CTS (meters)"+readChar(5030,22));
            
            y[j++][0]=Double.parseDouble(readChar(5030,22));
            
            System.out.println("4-th  data  point  position  vector  as latitude, longitude and altitude for airborne sensor platform, and as (X,Y,Z) coordinates for spaceborne sensor platform in a reference system such as CTS (meters)"+readChar(5052,22));
            
            z[k++][0]=Double.parseDouble(readChar(5052,22));
            
            System.out.println("4-th data point velocity vector in airborne coordinates (meters/second & degrees/second)for airborne sensor platform or (X ,Y ,Z ) in a reference system such as CTS for spaceborne sensor platforms "+readChar(5074,22));
            
            vx[p++][0]=Double.parseDouble(readChar(5074,22));
            
            System.out.println("4-th data point velocity vector in airborne coordinates (meters/second & degrees/second)for airborne sensor platform or (X ,Y ,Z ) in a reference system such as CTS for spaceborne sensor platforms "+readChar(5096,22));
            
            vy[q++][0]=Double.parseDouble(readChar(5096,22));
            
            System.out.println("4-th data point velocity vector in airborne coordinates (meters/second & degrees/second)for airborne sensor platform or (X ,Y ,Z ) in a reference system such as CTS for spaceborne sensor platforms "+readChar(5118,22));
            
            vz[r++][0]=Double.parseDouble(readChar(5118,22));
            
            System.out.println("5-th  data  point  position  vector  as latitude, longitude and altitude for airborne sensor platform, and as (X,Y,Z) coordinates for spaceborne sensor platform in a reference system such as CTS (meters))"+readChar(5140,22));
            
            x[i++][0]=Double.parseDouble(readChar(5140,22));
            
            System.out.println("5-th  data  point  position  vector  as latitude, longitude and altitude for airborne sensor platform, and as (X,Y,Z) coordinates for spaceborne sensor platform in a reference system such as CTS (meters)"+readChar(5162,22));
            
            y[j++][0]=Double.parseDouble(readChar(5162,22));
            
            System.out.println("5-th  data  point  position  vector  as latitude, longitude and altitude for airborne sensor platform, and as (X,Y,Z) coordinates for spaceborne sensor platform in a reference system such as CTS (meters)"+readChar(5184,22));
            
            z[k++][0]=Double.parseDouble(readChar(5184,22));
            
            System.out.println("5-th data point velocity vector in airborne coordinates (meters/second & degrees/second)for airborne sensor platform or (X ,Y ,Z ) in a reference system such as CTS for spaceborne sensor platforms "+readChar(5206,22));
            
            vx[p++][0]=Double.parseDouble(readChar(5206,22));
            
            System.out.println("5-th data point velocity vector in airborne coordinates (meters/second & degrees/second)for airborne sensor platform or (X ,Y ,Z ) in a reference system such as CTS for spaceborne sensor platforms "+readChar(5228,22));
            
            vy[q++][0]=Double.parseDouble(readChar(5228,22));
            
            System.out.println("5-th data point velocity vector in airborne coordinates (meters/second & degrees/second)for airborne sensor platform or (X ,Y ,Z ) in a reference system such as CTS for spaceborne sensor platforms "+readChar(5250,22));
            
            vz[r++][0]=Double.parseDouble(readChar(5250,22));
            
            
            br.close();
            
            System.out.println("hr="+hr+" "+min+" "+sec);
        
            for(i=0;i<5;i++)
			{
				A[i][0]=1;
			}
            
            for(i=0;i<5;i++)
            {
            A[i][1]=t;
                            t=t+interval;
            }
            slave_norm_temp=A[0][1];
            
            for(i=0;i<5;i++)
            {
            	A[i][1]=(A[i][1]-slave_norm_temp);
            	A[i][2]=A[i][1]*A[i][1];
                A[i][3]=A[i][1]*A[i][1]*A[i][1];
            }
            
            constructTrans(A,AT);
            
            matMul(4,5,4,AT,A,ATA);
            
            decompose(L,ATA);
            
            constructLT(L,LT);

            double b[][]=new double [4][1];
            double m1[][]=new double [4][1];
            matMul(4,5,1,AT,y,b);
            
            
            m1[0][0]=b[0][0]/L[0][0];
            m1[1][0]=(b[1][0]-m1[0][0]*L[1][0])/L[1][1];
            m1[2][0]=(b[2][0]-m1[0][0]*L[2][0]-m1[1][0]*L[2][1])/L[2][2];
            m1[3][0]=(b[3][0]-m1[0][0]*L[3][0]-m1[1][0]*L[3][1]-m1[2][0]*L[3][2])/L[3][3];
            
           
            
            b[3][0]=m1[3][0]/LT[3][3];
            b[2][0]=(m1[2][0]-LT[2][3]*b[3][0])/LT[2][2];
            b[1][0]=(m1[1][0]-LT[1][2]*b[2][0]-LT[1][3]*b[3][0])/LT[1][1];
            b[0][0]=(m1[0][0]-LT[0][1]*b[1][0]-LT[0][2]*b[2][0]-LT[0][3]*b[3][0])/LT[0][0];
            
            
          
            
            
            //a0,a1,a2,a3
            double a[][]=new double [4][1];
            matMul(4,5,1,AT,x,a);
            
            
            m1[0][0]=a[0][0]/L[0][0];
            m1[1][0]=(a[1][0]-m1[0][0]*L[1][0])/L[1][1];
            m1[2][0]=(a[2][0]-m1[0][0]*L[2][0]-m1[1][0]*L[2][1])/L[2][2];
            m1[3][0]=(a[3][0]-m1[0][0]*L[3][0]-m1[1][0]*L[3][1]-m1[2][0]*L[3][2])/L[3][3];
            
                          
            
             
            a[3][0]=m1[3][0]/LT[3][3];
            a[2][0]=(m1[2][0]-LT[2][3]*a[3][0])/LT[2][2];
            a[1][0]=(m1[1][0]-LT[1][2]*a[2][0]-LT[1][3]*a[3][0])/LT[1][1];
            a[0][0]=(m1[0][0]-LT[0][1]*a[1][0]-LT[0][2]*a[2][0]-LT[0][3]*a[3][0])/LT[0][0];
           
          
          
          
            
            //c0,c1,c3
            double c[][]=new double [4][1];
            matMul(4,5,1,AT,z,c);
            
            
            
            
            m1[0][0]=c[0][0]/L[0][0];
            m1[1][0]=(c[1][0]-m1[0][0]*L[1][0])/L[1][1];
            m1[2][0]=(c[2][0]-m1[0][0]*L[2][0]-m1[1][0]*L[2][1])/L[2][2];
            m1[3][0]=(c[3][0]-m1[0][0]*L[3][0]-m1[1][0]*L[3][1]-m1[2][0]*L[3][2])/L[3][3];
            
           
           
            c[3][0]=m1[3][0]/LT[3][3];
            c[2][0]=(m1[2][0]-LT[2][3]*c[3][0])/LT[2][2];
            c[1][0]=(m1[1][0]-LT[1][2]*c[2][0]-LT[1][3]*c[3][0])/LT[1][1];
            c[0][0]=(m1[0][0]-LT[0][1]*c[1][0]-LT[0][2]*c[2][0]-LT[0][3]*c[3][0])/LT[0][0];

         
            
          //d0,d1,d3
            double d[][]=new double [4][1];
            matMul(4,5,1,AT,vx,d);
            
            m1[0][0]=d[0][0]/L[0][0];
            m1[1][0]=(d[1][0]-m1[0][0]*L[1][0])/L[1][1];
            m1[2][0]=(d[2][0]-m1[0][0]*L[2][0]-m1[1][0]*L[2][1])/L[2][2];
            m1[3][0]=(d[3][0]-m1[0][0]*L[3][0]-m1[1][0]*L[3][1]-m1[2][0]*L[3][2])/L[3][3];
            
           
            d[3][0]=m1[3][0]/LT[3][3];
            d[2][0]=(m1[2][0]-LT[2][3]*d[3][0])/LT[2][2];
            d[1][0]=(m1[1][0]-LT[1][2]*d[2][0]-LT[1][3]*d[3][0])/LT[1][1];
            d[0][0]=(m1[0][0]-LT[0][1]*d[1][0]-LT[0][2]*d[2][0]-LT[0][3]*d[3][0])/LT[0][0];
            
          
          //e0,e1,e3
            double e[][]=new double [4][1];
            matMul(4,5,1,AT,vy,e);
            
            m1[0][0]=e[0][0]/L[0][0];
            m1[1][0]=(e[1][0]-m1[0][0]*L[1][0])/L[1][1];
            m1[2][0]=(e[2][0]-m1[0][0]*L[2][0]-m1[1][0]*L[2][1])/L[2][2];
            m1[3][0]=(e[3][0]-m1[0][0]*L[3][0]-m1[1][0]*L[3][1]-m1[2][0]*L[3][2])/L[3][3];
            
             
            
            e[3][0]=m1[3][0]/LT[3][3];
            e[2][0]=(m1[2][0]-LT[2][3]*e[3][0])/LT[2][2];
            e[1][0]=(m1[1][0]-LT[1][2]*e[2][0]-LT[1][3]*e[3][0])/LT[1][1];
            e[0][0]=(m1[0][0]-LT[0][1]*e[1][0]-LT[0][2]*e[2][0]-LT[0][3]*e[3][0])/LT[0][0];
            
                  
           
          //f0,f1,f3
            double f[][]=new double [4][1];
            matMul(4,5,1,AT,vz,f);
            
            m1[0][0]=f[0][0]/L[0][0];
            m1[1][0]=(f[1][0]-m1[0][0]*L[1][0])/L[1][1];
            m1[2][0]=(f[2][0]-m1[0][0]*L[2][0]-m1[1][0]*L[2][1])/L[2][2];
            m1[3][0]=(f[3][0]-m1[0][0]*L[3][0]-m1[1][0]*L[3][1]-m1[2][0]*L[3][2])/L[3][3];
          
            
           
            f[3][0]=m1[3][0]/LT[3][3];
            f[2][0]=(m1[2][0]-LT[2][3]*f[3][0])/LT[2][2];
            f[1][0]=(m1[1][0]-LT[1][2]*f[2][0]-LT[1][3]*f[3][0])/LT[1][1];
            f[0][0]=(m1[0][0]-LT[0][1]*f[1][0]-LT[0][2]*f[2][0]-LT[0][3]*f[3][0])/LT[0][0];

          
        for(i=0;i<4;i++)
        {
        	slave_a[i][0]=a[i][0];
        	slave_b[i][0]=b[i][0];
        	slave_c[i][0]=c[i][0];
        	slave_d[i][0]=d[i][0];
        	slave_e[i][0]=e[i][0];
        	slave_f[i][0]=f[i][0];
            
        
        }
       }
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	static void ReadMasterOrbit() throws FileNotFoundException, IOException
	{
		
     int len;
   double t,interval;
	 double x[][]=new double[5][1];
	 double y[][]=new double[5][1];
	 double z[][]=new double[5][1];
	 double vx[][]=new double[5][1];
	 double vy[][]=new double[5][1];
	 double vz[][]=new double[5][1];
	 
	 
	double A[][]=new double[5][4],LT[][]=new double[4][4],AT[][]=new double[4][5],ATA[][]=new double[4][4],L[][]=new double[4][4];

  /* 1. Edit master leader file path here */
		
        try (DataInputStream br = new DataInputStream(new FileInputStream("/media/aniket/Study/ProjectASMA/DhanbadDataCD/SLC1/LEADER.DAT"))) {
            len = br.read(buf);
            System.out.println("Length "+len);
            
            System.out.printf("\nProcessed scene centre geodetic latitude defined as positive to the north  of the equator and negative to the south (deg)"+readChar(836,16));
            master_centre_lat=Double.parseDouble(readChar(836,16).trim());
            
            System.out.printf("\nProcessed scene centre geodetic longitude defined as positive to the east of the prime meridian and negative to the west. (deg.)"+readChar(852,16));
            master_centre_long=Double.parseDouble(readChar(852,16).trim());
            
            
            
            System.out.printf("\nScene center line number "+readChar(1044,8));
            
            master_centerline=Integer.parseInt(readChar(1044,8).trim());
            
            System.out.printf("\nScene center pixel number "+readChar(1052,8));
            
            master_centerpixel=Integer.parseInt(readChar(1052,8).trim());
            
            System.out.printf("\nProcessed scene length including zero fill "+readChar(1060,16));
            
            System.out.printf("\nProcessed scene width including zero fill "+readChar(1076,16));
            
             
            
            System.out.printf("\nSampling rate (MHz) "+readChar(1430,16));
            master_SF=Double.parseDouble(readChar(1430,16));
            
            
                          
            System.out.printf("\nNominal PRF (Hz)"+readChar(1654,16));
            master_PRF=Double.parseDouble(readChar(1654,16));
            
           
/**************************************************************************************************************/                
            
           
            System.out.println("\n\nSENSOR SPECIFIC LOCAL USE SEGMENT");
            
            System.out.print("\nZero-doppler range time of first range pixel "+readChar(2486,16));
            master_tr0=Double.parseDouble(readChar(2486,16));
            
            System.out.print("\nZero-doppler range time of center range pixel "+readChar(2502,16));
            
            System.out.print("\nZero-doppler range time of last range pixel "+readChar(2518,16));
            
            System.out.print("\nZero-doppler azimuth time of first azimuth pixel "+readChar(2534,24));
            
            String str1=readChar(2534,24);
             int hr,min;
             
             String shr="",smin="",ssec="";
           
             double sec;
           
            int ind;
           
            for(ind=0;str1.charAt(ind)!=':';ind++)
            {}           
            	
            shr=(str1.substring(ind-2,ind));
            smin=(str1.substring(ind+1, ind+3)); 
            
            for(ind=ind+1;str1.charAt(ind)!=':';ind++)
            {}
            System.out.println(ind);

            ssec=(str1.substring(ind+1,str1.length())); 	
            	
        
            
        	sec=Double.parseDouble(ssec);
            hr=Integer.parseInt(shr.trim());
            min=Integer.parseInt(smin.trim());
        	
            master_ta0=hr*60*60+min*60+sec;

            System.out.println(sec+" "+hr+" "+min+" "+master_ta0+" "+master_tr0);
                         	
            System.out.print("\nZero-doppler azimuth time of center azimuth pixel "+readChar(2558,24));
            
            System.out.print("\nZero-doppler azimuth time of last azimuth pixel "+readChar(2582,24));
            

/*******************************************************************************************/                
            System.out.println("\n\nRADIOMETRIC COMPENSATION DATA RECORD\n");
            
            
            System.out.println("Number of data points (up to 64)"+readChar(4366,4));
            
            System.out.println("Year  of data point. (YYYY)"+readChar(4370,4));
            
            System.out.println("Month of data point. ($$MM)"+readChar(4374,4));
            
            System.out.println("Day   of data point. ($$DD)"+readChar(4378,4));
            
            System.out.println("Day in the year (GMT)"+readChar(4382,4));
            
            System.out.println("Seconds of day (GMT) of data"+readChar(4386,22));
            
            t=Double.parseDouble(readChar(4386,22));
            
            System.out.println("Time interval between DATA points (sec)"+readChar(4408,22));
            
            interval=Double.parseDouble(readChar(4408,22));
            
            int i,j,k,p,q,r;
            i=j=k=p=q=r=0;
            
            System.out.println("l-st  data  point  position  vector  as latitude, longitude and altitude for airborne sensor platform, and as (X,Y,Z) coordinates for spaceborne sensor platform in a reference system such as CTS (meters))"+readChar(4612,22));
            
            x[i++][0]=Double.parseDouble(readChar(4612,22));
            
            System.out.println("l-st  data  point  position  vector  as latitude, longitude and altitude for airborne sensor platform, and as (X,Y,Z) coordinates for spaceborne sensor platform in a reference system such as CTS (meters)"+readChar(4634,22));
            
            y[j++][0]=Double.parseDouble(readChar(4634,22));
            
            System.out.println("l-st  data  point  position  vector  as latitude, longitude and altitude for airborne sensor platform, and as (X,Y,Z) coordinates for spaceborne sensor platform in a reference system such as CTS (meters)"+readChar(4656,22));
            
            z[k++][0]=Double.parseDouble(readChar(4656,22));
            
            System.out.println("lst data point velocity vector in airborne coordinates (meters/second & degrees/second)for airborne sensor platform or (X ,Y ,Z ) in a reference system such as CTS for spaceborne sensor platforms "+readChar(4678,22));
            
            vx[p++][0]=Double.parseDouble(readChar(4678,22));
            
            System.out.println("lst data point velocity vector in airborne coordinates (meters/second & degrees/second)for airborne sensor platform or (X ,Y ,Z ) in a reference system such as CTS for spaceborne sensor platforms "+readChar(4700,22));
            
            vy[q++][0]=Double.parseDouble(readChar(4700,22));
            
            System.out.println("lst data point velocity vector in airborne coordinates (meters/second & degrees/second)for airborne sensor platform or (X ,Y ,Z ) in a reference system such as CTS for spaceborne sensor platforms "+readChar(4722,22));
            
            vz[r++][0]=Double.parseDouble(readChar(4722,22));
            
            System.out.println("2-nd  data  point  position  vector  as latitude, longitude and altitude for airborne sensor platform, and as (X,Y,Z) coordinates for spaceborne sensor platform in a reference system such as CTS (meters))"+readChar(4744,22));
            
            System.out.print("i="+i);
            x[i++][0]=Double.parseDouble(readChar(4744,22));
            
            
            System.out.println("2-nd  data  point  position  vector  as latitude, longitude and altitude for airborne sensor platform, and as (X,Y,Z) coordinates for spaceborne sensor platform in a reference system such as CTS (meters)"+readChar(4766,22));
            
            y[j++][0]=Double.parseDouble(readChar(4766,22));
            
            System.out.println("2-nd  data  point  position  vector  as latitude, longitude and altitude for airborne sensor platform, and as (X,Y,Z) coordinates for spaceborne sensor platform in a reference system such as CTS (meters)"+readChar(4788,22));
            
            z[k++][0]=Double.parseDouble(readChar(4788,22));
            
            System.out.println("2-nd data point velocity vector in airborne coordinates (meters/second & degrees/second)for airborne sensor platform or (X ,Y ,Z ) in a reference system such as CTS for spaceborne sensor platforms "+readChar(4810,22));
            
            vx[p++][0]=Double.parseDouble(readChar(4810,22));
            
            System.out.println("2-nd data point velocity vector in airborne coordinates (meters/second & degrees/second)for airborne sensor platform or (X ,Y ,Z ) in a reference system such as CTS for spaceborne sensor platforms "+readChar(4832,22));
            
            vy[q++][0]=Double.parseDouble(readChar(4832,22));
            
            System.out.println("2-nd data point velocity vector in airborne coordinates (meters/second & degrees/second)for airborne sensor platform or (X ,Y ,Z ) in a reference system such as CTS for spaceborne sensor platforms "+readChar(4854,22));
            
            vz[r++][0]=Double.parseDouble(readChar(4854,22));
            
            System.out.println("3-rd  data  point  position  vector  as latitude, longitude and altitude for airborne sensor platform, and as (X,Y,Z) coordinates for spaceborne sensor platform in a reference system such as CTS (meters))"+readChar(4876,22));
            
            x[i++][0]=Double.parseDouble(readChar(4876,22));
            
            System.out.println("3-rd  data  point  position  vector  as latitude, longitude and altitude for airborne sensor platform, and as (X,Y,Z) coordinates for spaceborne sensor platform in a reference system such as CTS (meters)"+readChar(4898,22));
            
            y[j++][0]=Double.parseDouble(readChar(4898,22));
            
            System.out.println("3-rd  data  point  position  vector  as latitude, longitude and altitude for airborne sensor platform, and as (X,Y,Z) coordinates for spaceborne sensor platform in a reference system such as CTS (meters)"+readChar(4920,22));
            
            z[k++][0]=Double.parseDouble(readChar(4920,22));
            
            System.out.println("3-rd data point velocity vector in airborne coordinates (meters/second & degrees/second)for airborne sensor platform or (X ,Y ,Z ) in a reference system such as CTS for spaceborne sensor platforms "+readChar(4942,22));
            
            vx[p++][0]=Double.parseDouble(readChar(4942,22));
            
            System.out.println("3-rd data point velocity vector in airborne coordinates (meters/second & degrees/second)for airborne sensor platform or (X ,Y ,Z ) in a reference system such as CTS for spaceborne sensor platforms "+readChar(4964,22));
            
            vy[q++][0]=Double.parseDouble(readChar(4964,22));
            
            System.out.println("3-rd data point velocity vector in airborne coordinates (meters/second & degrees/second)for airborne sensor platform or (X ,Y ,Z ) in a reference system such as CTS for spaceborne sensor platforms "+readChar(4986,22));
            
            vz[r++][0]=Double.parseDouble(readChar(4986,22));
            
            System.out.println("4-th  data  point  position  vector  as latitude, longitude and altitude for airborne sensor platform, and as (X,Y,Z) coordinates for spaceborne sensor platform in a reference system such as CTS (meters))"+readChar(5008,22));
            
            x[i++][0]=Double.parseDouble(readChar(5008,22));
            
            System.out.println("4-th  data  point  position  vector  as latitude, longitude and altitude for airborne sensor platform, and as (X,Y,Z) coordinates for spaceborne sensor platform in a reference system such as CTS (meters)"+readChar(5030,22));
            
            y[j++][0]=Double.parseDouble(readChar(5030,22));
            
            System.out.println("4-th  data  point  position  vector  as latitude, longitude and altitude for airborne sensor platform, and as (X,Y,Z) coordinates for spaceborne sensor platform in a reference system such as CTS (meters)"+readChar(5052,22));
            
            z[k++][0]=Double.parseDouble(readChar(5052,22));
            
            System.out.println("4-th data point velocity vector in airborne coordinates (meters/second & degrees/second)for airborne sensor platform or (X ,Y ,Z ) in a reference system such as CTS for spaceborne sensor platforms "+readChar(5074,22));
            
            vx[p++][0]=Double.parseDouble(readChar(5074,22));
            
            System.out.println("4-th data point velocity vector in airborne coordinates (meters/second & degrees/second)for airborne sensor platform or (X ,Y ,Z ) in a reference system such as CTS for spaceborne sensor platforms "+readChar(5096,22));
            
            vy[q++][0]=Double.parseDouble(readChar(5096,22));
            
            System.out.println("4-th data point velocity vector in airborne coordinates (meters/second & degrees/second)for airborne sensor platform or (X ,Y ,Z ) in a reference system such as CTS for spaceborne sensor platforms "+readChar(5118,22));
            
            vz[r++][0]=Double.parseDouble(readChar(5118,22));
            
            System.out.println("5-th  data  point  position  vector  as latitude, longitude and altitude for airborne sensor platform, and as (X,Y,Z) coordinates for spaceborne sensor platform in a reference system such as CTS (meters))"+readChar(5140,22));
            
            x[i++][0]=Double.parseDouble(readChar(5140,22));
            
            System.out.println("5-th  data  point  position  vector  as latitude, longitude and altitude for airborne sensor platform, and as (X,Y,Z) coordinates for spaceborne sensor platform in a reference system such as CTS (meters)"+readChar(5162,22));
            
            y[j++][0]=Double.parseDouble(readChar(5162,22));
            
            System.out.println("5-th  data  point  position  vector  as latitude, longitude and altitude for airborne sensor platform, and as (X,Y,Z) coordinates for spaceborne sensor platform in a reference system such as CTS (meters)"+readChar(5184,22));
            
            z[k++][0]=Double.parseDouble(readChar(5184,22));
            
            System.out.println("5-th data point velocity vector in airborne coordinates (meters/second & degrees/second)for airborne sensor platform or (X ,Y ,Z ) in a reference system such as CTS for spaceborne sensor platforms "+readChar(5206,22));
            
            vx[p++][0]=Double.parseDouble(readChar(5206,22));
            
            System.out.println("5-th data point velocity vector in airborne coordinates (meters/second & degrees/second)for airborne sensor platform or (X ,Y ,Z ) in a reference system such as CTS for spaceborne sensor platforms "+readChar(5228,22));
            
            vy[q++][0]=Double.parseDouble(readChar(5228,22));
            
            System.out.println("5-th data point velocity vector in airborne coordinates (meters/second & degrees/second)for airborne sensor platform or (X ,Y ,Z ) in a reference system such as CTS for spaceborne sensor platforms "+readChar(5250,22));
            
            vz[r++][0]=Double.parseDouble(readChar(5250,22));
            
            
            br.close();
            
            System.out.println("hr="+hr+" "+min+" "+sec);
            
            
            
            ///////////
            for(i=0;i<5;i++)
			{
				A[i][0]=1;
			}
            
            for(i=0;i<5;i++)
            {
            A[i][1]=t;
                            t=t+interval;
            }
             master_norm_temp=A[0][1];
            
            for(i=0;i<5;i++)
            {
            	A[i][1]=(A[i][1]-master_norm_temp);
            	A[i][2]=A[i][1]*A[i][1];
                A[i][3]=A[i][1]*A[i][1]*A[i][1];
            }
            
            constructTrans(A,AT);
            
            matMul(4,5,4,AT,A,ATA);
            
            decompose(L,ATA);
            
            constructLT(L,LT);

            double m1[][]=new double [4][1];
            matMul(4,5,1,AT,y,b);
            
            
            m1[0][0]=b[0][0]/L[0][0];
            m1[1][0]=(b[1][0]-m1[0][0]*L[1][0])/L[1][1];
            m1[2][0]=(b[2][0]-m1[0][0]*L[2][0]-m1[1][0]*L[2][1])/L[2][2];
            m1[3][0]=(b[3][0]-m1[0][0]*L[3][0]-m1[1][0]*L[3][1]-m1[2][0]*L[3][2])/L[3][3];
            
           
            
            b[3][0]=m1[3][0]/LT[3][3];
            b[2][0]=(m1[2][0]-LT[2][3]*b[3][0])/LT[2][2];
            b[1][0]=(m1[1][0]-LT[1][2]*b[2][0]-LT[1][3]*b[3][0])/LT[1][1];
            b[0][0]=(m1[0][0]-LT[0][1]*b[1][0]-LT[0][2]*b[2][0]-LT[0][3]*b[3][0])/LT[0][0];
            
            
          
            
            
            //a0,a1,a2,a3
          
            matMul(4,5,1,AT,x,a);
            
            
            m1[0][0]=a[0][0]/L[0][0];
            m1[1][0]=(a[1][0]-m1[0][0]*L[1][0])/L[1][1];
            m1[2][0]=(a[2][0]-m1[0][0]*L[2][0]-m1[1][0]*L[2][1])/L[2][2];
            m1[3][0]=(a[3][0]-m1[0][0]*L[3][0]-m1[1][0]*L[3][1]-m1[2][0]*L[3][2])/L[3][3];
            
                          
            
             
            a[3][0]=m1[3][0]/LT[3][3];
            a[2][0]=(m1[2][0]-LT[2][3]*a[3][0])/LT[2][2];
            a[1][0]=(m1[1][0]-LT[1][2]*a[2][0]-LT[1][3]*a[3][0])/LT[1][1];
            a[0][0]=(m1[0][0]-LT[0][1]*a[1][0]-LT[0][2]*a[2][0]-LT[0][3]*a[3][0])/LT[0][0];
           
          
          
          
            
            //c0,c1,c3
            matMul(4,5,1,AT,z,c);
            
            
            
            
            m1[0][0]=c[0][0]/L[0][0];
            m1[1][0]=(c[1][0]-m1[0][0]*L[1][0])/L[1][1];
            m1[2][0]=(c[2][0]-m1[0][0]*L[2][0]-m1[1][0]*L[2][1])/L[2][2];
            m1[3][0]=(c[3][0]-m1[0][0]*L[3][0]-m1[1][0]*L[3][1]-m1[2][0]*L[3][2])/L[3][3];
            
           
           
            c[3][0]=m1[3][0]/LT[3][3];
            c[2][0]=(m1[2][0]-LT[2][3]*c[3][0])/LT[2][2];
            c[1][0]=(m1[1][0]-LT[1][2]*c[2][0]-LT[1][3]*c[3][0])/LT[1][1];
            c[0][0]=(m1[0][0]-LT[0][1]*c[1][0]-LT[0][2]*c[2][0]-LT[0][3]*c[3][0])/LT[0][0];

         
            
          //d0,d1,d3
            matMul(4,5,1,AT,vx,d);
            
            m1[0][0]=d[0][0]/L[0][0];
            m1[1][0]=(d[1][0]-m1[0][0]*L[1][0])/L[1][1];
            m1[2][0]=(d[2][0]-m1[0][0]*L[2][0]-m1[1][0]*L[2][1])/L[2][2];
            m1[3][0]=(d[3][0]-m1[0][0]*L[3][0]-m1[1][0]*L[3][1]-m1[2][0]*L[3][2])/L[3][3];
            
           
            d[3][0]=m1[3][0]/LT[3][3];
            d[2][0]=(m1[2][0]-LT[2][3]*d[3][0])/LT[2][2];
            d[1][0]=(m1[1][0]-LT[1][2]*d[2][0]-LT[1][3]*d[3][0])/LT[1][1];
            d[0][0]=(m1[0][0]-LT[0][1]*d[1][0]-LT[0][2]*d[2][0]-LT[0][3]*d[3][0])/LT[0][0];
            
          
          //e0,e1,e3
            matMul(4,5,1,AT,vy,e);
            
            m1[0][0]=e[0][0]/L[0][0];
            m1[1][0]=(e[1][0]-m1[0][0]*L[1][0])/L[1][1];
            m1[2][0]=(e[2][0]-m1[0][0]*L[2][0]-m1[1][0]*L[2][1])/L[2][2];
            m1[3][0]=(e[3][0]-m1[0][0]*L[3][0]-m1[1][0]*L[3][1]-m1[2][0]*L[3][2])/L[3][3];
            
             
            
            e[3][0]=m1[3][0]/LT[3][3];
            e[2][0]=(m1[2][0]-LT[2][3]*e[3][0])/LT[2][2];
            e[1][0]=(m1[1][0]-LT[1][2]*e[2][0]-LT[1][3]*e[3][0])/LT[1][1];
            e[0][0]=(m1[0][0]-LT[0][1]*e[1][0]-LT[0][2]*e[2][0]-LT[0][3]*e[3][0])/LT[0][0];
            
                  
           
          //f0,f1,f3
            matMul(4,5,1,AT,vz,f);
            
            m1[0][0]=f[0][0]/L[0][0];
            m1[1][0]=(f[1][0]-m1[0][0]*L[1][0])/L[1][1];
            m1[2][0]=(f[2][0]-m1[0][0]*L[2][0]-m1[1][0]*L[2][1])/L[2][2];
            m1[3][0]=(f[3][0]-m1[0][0]*L[3][0]-m1[1][0]*L[3][1]-m1[2][0]*L[3][2])/L[3][3];
          
            
           
            f[3][0]=m1[3][0]/LT[3][3];
            f[2][0]=(m1[2][0]-LT[2][3]*f[3][0])/LT[2][2];
            f[1][0]=(m1[1][0]-LT[1][2]*f[2][0]-LT[1][3]*f[3][0])/LT[1][1];
            f[0][0]=(m1[0][0]-LT[0][1]*f[1][0]-LT[0][2]*f[2][0]-LT[0][3]*f[3][0])/LT[0][0];

          
        
            
            
            

        }
		
		
	}
    /*Functio to construct LT */
	private static void constructLT(double L[][],double LT[][])
	{
		for(int i=0;i<4;i++)
			for(int j=0;j<4;j++)
				LT[j][i]=L[i][j];	
	}
	
	/*Functio for decompose*/
	private static void decompose(double L[][],double ATA[][])
	{
		for(int i=0;i<4;i++)
		{
			for(int j=0;j<4;j++)
			{
			if(j>i)
				L[i][j]=0;
				
			}
		}
		//k=1
		L[0][0]=Math.sqrt(ATA[0][0]);
		
		//k=2
		L[1][0]=ATA[0][1]/L[0][0];
		L[1][1]=Math.sqrt(ATA[1][1]-L[1][0]*L[1][0]);
		
		//k=3
		double tempMat[][]=new double[2][2];
		tempMat[0][0]=L[0][0];
		tempMat[1][0]=-L[1][0];
		tempMat[1][1]=L[1][1];
		
		double det=tempMat[1][1]*tempMat[0][0]-tempMat[0][1]*tempMat[1][0];
		
		double tmp=tempMat[0][0];
		tempMat[0][0]=tempMat[1][1];
		tempMat[1][1]=tmp;
		
		for(int i=0;i<2;i++)
		{
			for(int j=0;j<2;j++)
			{
				tempMat[i][j]=tempMat[i][j]/det;	
			}
		}
		
		double mat21[][]=new double [2][1],mat22[][]=new double [2][1],mat3[][]=new double[1][2],mat0[][]=new double [1][1];
		
		mat21[0][0]=ATA[0][2];
		mat21[1][0]=ATA[1][2];
		
		matMul(2,2,1,tempMat,mat21,mat22);

		mat3[0][0]=mat22[0][0];
		mat3[0][1]=mat22[1][0];
		L[2][0]=mat22[0][0];
		L[2][1]=mat22[1][0];
		matMul(1,2,1,mat3,mat22,mat0);
		System.out.println(""+mat0[0][0]);
		L[2][2]=Math.sqrt( ATA[2][2]-mat0[0][0]);
		
		//k=4
		double a[][]=new double[3][1],determinant;
		double toInverse[][]=new double[3][3],inverse[][]=new double[3][3];
		a[0][0]=ATA[0][3];
		a[1][0]=ATA[1][3];
		a[2][0]=ATA[2][3];
		
		for(int i=0;i<3;i++)
			for(int j=0;j<3;j++)
				toInverse[i][j]=L[i][j];
		
		determinant=(toInverse[0][0]*(toInverse[1][1]*toInverse[2][2]-toInverse[1][2]*toInverse[2][1]))-(toInverse[0][1]*(toInverse[1][0]*toInverse[2][2]-toInverse[1][2]*toInverse[2][0]))+(toInverse[0][2]*(toInverse[1][0]*toInverse[2][1]-toInverse[1][1]*toInverse[2][0]));
		inverse[0][0]=(toInverse[1][1]*toInverse[2][2]-toInverse[1][2]*toInverse[2][1])/determinant;
		inverse[0][1]=(-(toInverse[0][1]*toInverse[2][2]-toInverse[0][2]*toInverse[2][1]))/determinant;
		inverse[0][2]=(toInverse[0][1]*toInverse[1][2]-toInverse[0][2]*toInverse[1][1])/determinant;
		inverse[1][0]=(-(toInverse[1][0]*toInverse[2][2]-toInverse[1][2]*toInverse[0][2]))/determinant;
		inverse[1][1]=(toInverse[0][0]*toInverse[2][2]-toInverse[0][2]*toInverse[2][0])/determinant;
		inverse[1][2]=(-(toInverse[0][0]*toInverse[1][2]-toInverse[0][2]*toInverse[1][0]))/determinant;
		inverse[2][0]=(toInverse[1][0]*toInverse[2][1]-toInverse[1][1]*toInverse[2][0])/determinant;
		inverse[2][1]=(-(toInverse[0][0]*toInverse[2][1]-toInverse[0][1]*toInverse[2][0]))/determinant;
		inverse[2][2]=(toInverse[0][0]*toInverse[1][1]-toInverse[0][1]*toInverse[1][0])/determinant;
		
		toInverse=new double[3][1];
		double[][] toInversetr=new double[1][3];
		
		matMul(3,3,1,inverse,a,toInverse);
		L[3][0]=toInversetr[0][0]=toInverse[0][0];
		L[3][1]=toInversetr[0][1]=toInverse[1][0];
		L[3][2]=toInversetr[0][2]=toInverse[2][0];
		
		matMul(1,3,1,toInversetr,toInverse,mat0);
		L[3][3]=Math.sqrt(ATA[3][3]-mat0[0][0]);
		System.out.println("\nDecomposition is ");
		for(int i=0;i<4;i++)
		{
			for(int j=0;j<4;j++)
			{
				System.out.print(L[i][j]+"\t");
			}
			System.out.println();
		}
	}
	
	
	/*Multiplication function*/
	private static void matMul(int m,int n,int l,double AT[][],double A[][],double ATA[][]) {
		double sum=0.00;
		for ( int c = 0 ; c < m ; c++ )
         {
            for ( int d = 0 ; d < l ; d++ )
            {   
               for ( int k = 0 ; k < n ; k++ )
               {
                  sum = sum + AT[c][k]*A[k][d];
               }
 
               ATA[c][d] = sum;
               sum = 0.0;
            }
         }
	}
/*Function to read float*/
	public static float readFloat(int off) {  
	    ByteBuffer bu = ByteBuffer.wrap(buf);  
	    return bu.getFloat(off);
	} 
	/*Function to read char*/	
	static String readChar(int off,int size)
	{
                String str="";
		for(int i=0;i<size;i++)
		str=str+(char)buf[off++];
		
                    return str;
	}
	/*Function to transpose matrix A*/
	static void constructTrans(double A[][],double AT[][])
	{
		for(int i=0;i<5;i++)
			for(int j=0;j<4;j++)
				AT[j][i]=A[i][j];
	}
	/*Function to read binary*/
	static Integer readBin(int off,int size)
	{
	
		int k=0;
		int tmp;
	    for(int j=0;j<size;j++)
		{
		
			tmp=buf[off+j];
			  tmp&=0x0ff;
			  tmp<<=(size-1-j)*8;
				k=k|tmp;
		}
		return k;
	}
	
	

}
