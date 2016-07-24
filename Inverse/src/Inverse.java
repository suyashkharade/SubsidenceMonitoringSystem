
public class Inverse {



	public static void main(String args[])
	{
		Double a,b,c,d,e,f,g,h,i;
		Double [][]arr=new Double[3][3];
		Double x0,y0,z0,x1,y1,z1;
		Double determinant;
		Double doppler,range,elipsoid;
		Double [][]inverse=new Double[3][3];
		Double []fx0=new Double[3];
		
                Double major,minor,ecc,phi,lambada,N,Nh,height;
                Double perx,pery,perz;
             
                perx=pery=perz=0.0;
		
             //Initialize
                height=0.0;
                major=6378144.00;
                minor=6356759.0;
          
                phi=Math.PI*32.347/180;
                lambada=Math.PI*77.584/180;
                
                
                
                
                ecc=(1-(Math.pow(minor,2)/Math.pow(major,2)));
                N=major/(Math.pow((1-(Math.pow(ecc,2)*Math.pow(Math.sin(phi),2))), 0.5));
                Nh=N+height;
           
                x0=Nh*Math.cos(phi)*Math.cos(lambada);
                y0=Nh*Math.cos(phi)*Math.sin(lambada);
                z0=(Nh-(Math.pow(ecc,2)*N))*Math.sin(phi);
                System.out.println(x0+"   "+y0+"   "+z0+"   ");
                
           /*  
                x0=1.0;
                y0=1.0;
                z0=0.0;
            */    
                 Double x,y,z,vx,vy,vz;
                      
                      		
                 
                 x=1630203.8132320952;
                 y=	5876824.092895963;
                 z=3751941.769568175;
                 vx=651.4722097813876;
                         vy=-4185.320512966313;
                         vz=6253.430311965925;
                 /*
                 x=1630203.8132320945;
y=5876824.092895962;
z=3751941.769568176;
vx=651.4722097813874;
        vy=-4185.320512966311;
        vz=6253.430311965929;
        */
		/*
        x0=1158550.0;
		y0=5262381.6;
		z0=6355298.5;
		*/

        x1=y1=z1=0.0;
                

	
        
        Double temp;
        int l=0;
        do
        {
             System.out.println("Iteration "+l++);
      
             /*
             doppler=(9*Math.pow(x0, 2)+36*Math.pow(y0, 2)+4*Math.pow(z0, 2)-36);
             range=(Math.pow(x0, 2)-2*Math.pow(y0, 2)-20*z0);
             elipsoid=(Math.pow(x0, 2)-Math.pow(y0, 2)+Math.pow(z0, 2));

        */
             doppler=(Math.pow(x0, 2)+Math.pow(y0, 2)+Math.pow(z0, 2)-(2*(x)*x0)-(2*y*y0)-(2*(z)*z0)+(Math.pow(x,2))+Math.pow(y,2)+Math.pow(z,2)-Math.pow(850047.795,2));    /* Math((3*Math(10,8 )*0.005602506333333333)/2,2));*/
		range=(vx*(x-x0)+vy*(y-y0)+vz*(z-z0));
		elipsoid=((Math.pow(x0, 2)/(Math.pow(major,2)))+(Math.pow(y0, 2)/(Math.pow(major,2)))+(Math.pow(z0, 2)/(Math.pow(minor,2))))-1.00;             
             
		 System.out.println(x0+"   "+y0+"   "+z0+"   ");
   
		
		 System.out.println("Doppler"+doppler+"   "+"Range"+range+"     "+"elipsoid"+elipsoid);
		
		 fx0[0]=doppler;
		
		fx0[1]=range;
		fx0[2]=elipsoid;
		
                
                
                   //For Jacobian and inverse
		
		  	   a=(2*x0-2*x);
               b=2*y0-2*y;
               c=2*z0-2*z;
               d=-vx;
               e=-vy;
               f=-vz;
               g=2*x0/(4.068072088*Math.pow(10, 13));
               h=2*y0/(4.068072088*Math.pow(10, 13));
               i=2*z0/(4.040838498*Math.pow(10, 13)); 
          //   System.out.println("a,b,c "+a+" "+b+" "+c+" "+d+" "+e+" "+f+" "+g+" "+h+" "+i);
                             
		
		 

		 
/*         
	     a=(18*x0);
         b=72*y0;
         c=8*z0;
         d=2*x0;
         e=-4*y0;
         f=-20.0;
         g=2*x0;
         h=-2*y0;
         i=2*z0; 
 
              
  */            
              

		   determinant=(double)((a*(e*i-f*h))-(b*(d*i-f*g))+(c*(d*h-e*g)));
			

			System.out.println("det "+determinant);
				
		      
			inverse[0][0]=(1/determinant)*((e*i-f*h));
		        inverse[0][1]=(1/determinant)*(-(b*i-c*h));
		        inverse[0][2]=(1/determinant)*((b*f-c*e));
		        inverse[1][0]=(1/determinant)*(-(d*i-f*g));
		        inverse[1][1]=(1/determinant)*((a*i-c*g));
		        inverse[1][2]=(1/determinant)*(-(a*f-c*d));
		        inverse[2][0]=(1/determinant)*((d*h-e*g));
		        inverse[2][1]=(1/determinant)*(-(a*h-b*g));
		        inverse[2][2]=(1/determinant)*((a*e-b*d));
/*
		        System.out.println("--Inverse--");
		        for(int m=0;m<3;m++)
				{
					for(int n=0;n<3;n++)
					{
						System.out.print(inverse[m][n]+"   ");
						
					}
					System.out.println();
					
				}
			
*/
                
        for(int m=0;m<3;m++)
		{
        	temp=0.0;
			for(int n=0;n<3;n++)
			{
			   temp+=inverse[m][n]*fx0[n];
			   
			}
			
      
			if(m==0)
             x1=x0-temp;
            
			else if(m==1)
			 y1=y0-temp;

			else if(m==2)
			z1=z0-temp;
 
			System.out.println();
			
		}
        
            
        System.out.println("x1="+x1);
    	System.out.println("y1="+y1);
    	System.out.println("z1="+z1);
    	
    	System.out.println("Percentage Error");
    	
    	System.out.println("For x1="+(perx=Math.abs(((x1-x0)/x1)*100)));
    	System.out.println("For y1="+(pery=Math.abs(((y1-y0)/y1)*100)));
    	System.out.println("For z1="+(perz=Math.abs(((z1-z0)/z1)*100)));
        
        x0=x1;
        y0=y1;
        
       //if(perz>1)
          z0=z1;
        
          
          
  }while(l<20);//(perx>1||perx<0)||(perx>1||perx<0));
        
        double ax=1632014.8753856479,ay=5865104.498961136,az=3769398.9553901535,avx=644.8395473257651,avy=-4203.277916227708,avz=6241.981459425897;
double ax0=1158557.7357517134,ay0= 5262415.375879012,az0=   3412466.463955462;
        System.out.println("dop"+(avx*(ax-ax0)+avy*(ay-ay0)+avz*(az-az0)));
      
        //center-4.607690056480962 0.005602506333333333
      //xyzvx 						T line61861.74312846786
      //center16.046951380500104 0.005602506333333333
        //1632014.8753856479	5865104.498961136	3769398.9553901535		-4203.277916227708	6241.981459425897
        }


}
