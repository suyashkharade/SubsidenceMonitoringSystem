package geocode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class CartesianToGeo {






	static int centerline,centerpixel;
	static int i=0,j=0,k=0,p=0,q=0,r=0;
	static double ta0,tac,trc;
	static double PRF;
	static double SF;
	static double dR;
	static double tr0;
	static double x[][]=new double[5][1];
	static double y[][]=new double[5][1];
	static double z[][]=new double[5][1];
	static double vx[][]=new double[5][1];
	static double vy[][]=new double[5][1];
	static double vz[][]=new double[5][1];
	static double t,interval;
	static double A[][]=new double[5][4],LT[][]=new double[4][4],AT[][]=new double[4][5],ATA[][]=new double[4][4],L[][]=new double[4][4];
	static byte[] buf=new byte[20000];
	static double centre_lat,centre_long;	
	    public static void main(String str[]) throws IOException
		{
			int len;
			
			
			/* 1. Edit master leader file path here */
			
            try (DataInputStream br = new DataInputStream(new FileInputStream("/media/aniket/Study/Project ASMA/Dhanbad Data CD/SLC1/LEADER.DAT"))) {
                len=br.read(buf);
                System.out.println("Length "+len);
                System.out.println("\n\nSCENE PARAMETERS\n");
                
                System.out.print("\nScene reference number (if one exits) "+readChar(756, 32));
                
                System.out.print("\nInput scene centre time "+readChar(788, 32));
                
                System.out.printf("\nProcessed scene centre geodetic latitude defined as positive to the north  of the equator and negative to the south (deg)"+readChar(836,16));
                centre_lat=Double.parseDouble(readChar(836,16).trim());
                
                System.out.printf("\nProcessed scene centre geodetic longitude defined as positive to the east of the prime meridian and negative to the west. (deg.)"+readChar(852,16));
                centre_long=Double.parseDouble(readChar(852,16).trim());
                
                System.out.print("\nProcessed scene centre true heading as calculated relative to North (deg)"+readChar(868,16));
                
                System.out.print("\nEllipsoid designator "+readChar(884,16));
                
                System.out.print("\nEllipsoid semimajor axis (km)   - (R) "+readChar(900,16));
                
                System.out.print("\nEllipsoid semiminor axis (km)   - (R) "+readChar(916,16));
                
                
                System.out.printf("\nScene center line number "+readChar(1044,8));
                
                centerline=Integer.parseInt(readChar(1044,8).trim());
                
                System.out.printf("\nScene center pixel number "+readChar(1052,8));
                
                centerpixel=Integer.parseInt(readChar(1052,8).trim());
                
                System.out.printf("\nProcessed scene length including zero fill "+readChar(1060,16));
                
                System.out.printf("\nProcessed scene width including zero fill "+readChar(1076,16));
                
                System.out.printf("\nNumber of SAR channels "+readChar(1108,4));
                
                System.out.printf("\nOrbit number "+readChar(1164,8));
                
                System.out.printf("\nSensor Platform geodetic Latitude at nadir(positive to north) corresponding to Scene Center (degrees) "+readChar(1172,8));
              
                
                System.out.print("\nSensor Platform geodetic Longitude at nadir(positive to east) corresponding to Scene Center (deg) "+readChar(1180,8));
                
                
                System.out.printf("\nSensor Platform Heading at nadir corresponding to Scene Center (deg) "+readChar(1188,8));
                
                System.out.printf("\nSensor clock angle as measured relative to snsor platform flight direction "+readChar(1196,8));
                
                System.out.printf("\nIncidence angle at scene centre as derived from sesor platform orientation "+readChar(1204,8));
                
                System.out.printf("\nRadar frequency (GHz) "+readChar(1212,8));
                
                System.out.printf("\nRadar wavelength (meters) "+readChar(1220,16));
                
                System.out.printf("\nMotion compensation indicator "+readChar(1236,2));
                
                System.out.printf("\nRange pulse code specifier "+readChar(1238,16));
                
                
                System.out.printf("\nSampling rate (MHz) "+readChar(1430,16));
                SF=Double.parseDouble(readChar(1430,16));
                
                
                              
                System.out.printf("\nNominal PRF (Hz)"+readChar(1654,16));
                PRF=Double.parseDouble(readChar(1654,16));
                
               
/**************************************************************************************************************/                
                int m=719;
                
                System.out.printf("\nLine spacing (meters) "+readChar(m+1687,16));
                
                System.out.printf("\nPixel spacing ( in range )(meters) "+readChar(m+1703,16));
                
                dR=Double.parseDouble(readChar(m+1703,16));
                
                System.out.printf("\nProcessor range compression designator "+readChar(m+1719,16));
                
                System.out.printf("\nspare "+readChar(m+1735,16));
                
                System.out.printf("\nspare "+readChar(m+1751,16));
                
               
                System.out.println("\n\nSENSOR SPECIFIC LOCAL USE SEGMENT");
                
                System.out.print("\nZero-doppler range time of first range pixel "+readChar(2486,16));
                tr0=Double.parseDouble(readChar(2486,16));
                
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
            	
                ta0=hr*60*60+min*60+sec;
 
                System.out.println(sec+" "+hr+" "+min+" "+ta0+" "+tr0);
                             	
                System.out.print("\nZero-doppler azimuth time of center azimuth pixel "+readChar(2558,24));
                
                System.out.print("\nZero-doppler azimuth time of last azimuth pixel "+readChar(2582,24));
                
 
/*******************************************************************************************/                
                System.out.println("\n\nRADIOMETRIC COMPENSATION DATA RECORD\n");
                m=2605;
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
                double temp=A[0][1];
                
                for(i=0;i<5;i++)
                {
                	A[i][1]=(A[i][1]-temp);
                	A[i][2]=A[i][1]*A[i][1];
                    A[i][3]=A[i][1]*A[i][1]*A[i][1];
                }
                
                System.out.println("Trial 1 A is ");
                
                for(int i=0;i<5;i++)
                {
                	for(int j=0;j<4;j++)
                	{              
                	System.out.print(A[i][j]+"   ");
                	}
                System.out.println();
                }
                constructTrans();
                
                System.out.println("Trial 1 AT is ");
                for(int i=0;i<4;i++)
                {
                	for(int j=0;j<5;j++)
                	{              
                	System.out.print(AT[i][j]+"   ");
                	}
                System.out.println();
                }
                matMul(4,5,4,AT,A,ATA);
                
                System.out.println("Trial 1 ATA is ");
                for(int i=0;i<4;i++)
                {
                	for(int j=0;j<4;j++)
                	{              
                	System.out.print(ATA[i][j]+"   ");
                	}
                System.out.println();
                }
                decompose();
                
                constructLT();

                double b[][]=new double [4][1];
                double m1[][]=new double [4][1];
                matMul(4,5,1,AT,y,b);
                
                System.out.println("ATy is ");
                for(int i=0;i<4;i++)
                	System.out.println(b[i][0]);
                
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
                
                              
                System.out.println("A  m me is ");
                for(int i=0;i<4;i++)
                	System.out.println(m1[i][0]);
                
                 
                a[3][0]=m1[3][0]/LT[3][3];
                a[2][0]=(m1[2][0]-LT[2][3]*a[3][0])/LT[2][2];
                a[1][0]=(m1[1][0]-LT[1][2]*a[2][0]-LT[1][3]*a[3][0])/LT[1][1];
                a[0][0]=(m1[0][0]-LT[0][1]*a[1][0]-LT[0][2]*a[2][0]-LT[0][3]*a[3][0])/LT[0][0];
               
              
                System.out.println("A is ");
                for(int i=0;i<4;i++)
                	System.out.println(a[i][0]);
              
              
                
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
                
                System.out.println("Trial d is ");
                for(int i=0;i<4;i++)
                	System.out.println(d[i][0]);
              
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
              
              //trial
                System.out.println("F  m me is ");
                for(int i=0;i<4;i++)
                	System.out.println(m1[i][0]);
                
               
                f[3][0]=m1[3][0]/LT[3][3];
                f[2][0]=(m1[2][0]-LT[2][3]*f[3][0])/LT[2][2];
                f[1][0]=(m1[1][0]-LT[1][2]*f[2][0]-LT[1][3]*f[3][0])/LT[1][1];
                f[0][0]=(m1[0][0]-LT[0][1]*f[1][0]-LT[0][2]*f[2][0]-LT[0][3]*f[3][0])/LT[0][0];

              
                System.out.println("Trial f is ");
                for(int i=0;i<4;i++)
                	System.out.println(f[i][0]);
                
             
            	Double a_temp,b_temp,c_temp,d_temp,e_temp,f_temp,g_temp,h_temp,i_temp;
        		Double [][]arr=new Double[3][3];
        		Double x0,y0,z0,x1,y1,z1;
        		Double determinant;
        		Double doppler,range,elipsoid;
        		Double [][]inverse=new Double[3][3];
        		Double []fx0=new Double[3];
        		
                        Double major,minor,ecc,phi,lambada,N,Nh,height;
                       
                     
                       
 
                        
                        //Initialize major ,minor axis according to file
                        height=0.0;
                        major=6378144.00;
                        minor=6356759.0;
                        System.out.println("lat  in file ===="+centre_lat);    

                        phi=Math.PI*centre_lat/180;
                        lambada=Math.PI*centre_long/180;
                        System.out.println("phiiiiiiiiii"+centre_long+" ===="+phi);    

         
                        
                        
                        
                        
                        ecc=(1-(Math.pow(minor,2)/Math.pow(major,2)));
                        N=major/(Math.pow((1-(Math.pow(ecc,2)*Math.pow(Math.sin(phi),2))), 0.5));
                        Nh=N+height;
                   
                        x0=Nh*Math.cos(phi)*Math.cos(lambada);
                        y0=Nh*Math.cos(phi)*Math.sin(lambada);
                        z0=(Nh-(Math.pow(ecc,2)*N))*Math.sin(phi);
                        
                        System.out.println("ground point "+x0+"   "+y0+"   "+z0+"   ");
                

                        //ground point 423127.92175588186   5838633.42124944   2532144.556908548 
                        //xyzvx 149082.7179526584	6605669.689256542	2762407.639426618	1747.4107626354905	2811.7001838423876	-6791.2364710350685	T azimuth 16.292544602407993T masterline -2.9167783731701322E7T range no.2626.110056926864 R1  850047.7950000067

                        
                        
                        
                        
                         Double x,y,z,vx,vy,vz;
                        
                                int l=centerline;
                                 tac=ta0+(l-1)/PRF;
                                int p=centerpixel; 
                                 System.out.println("tac ===="+tac);
                                 trc=tr0*Math.pow(10,-3)+(p-1)/(2*SF*1000000);
                                 System.out.println("trc ===="+trc);
                                 tac+=trc;
                                 //Scaled tac
                                 tac=(tac-temp);
                                 
                                 
                                
                               
                                  x=a[0][0]+a[1][0]*tac+a[2][0]*tac*tac+a[3][0]*tac*tac*tac;
                                 
                                  y=b[0][0]+b[1][0]*tac+b[2][0]*tac*tac+b[3][0]*tac*tac*tac;
                                  z=c[0][0]+c[1][0]*tac+c[2][0]*tac*tac+c[3][0]*tac*tac*tac;
                                
                                 vx=a[1][0]+2*a[2][0]*tac+3*a[3][0]*tac*tac;
                                 vy=b[1][0]+2*b[2][0]*tac+3*b[3][0]*tac*tac;
                                 vz=c[1][0]+2*c[2][0]*tac+3*c[3][0]*tac*tac;
                                 
                                 
                                 System.out.println("Test x  "+x+" "+y+" "+z+" "+vx+" "+vy+" "+vz+" ");
                                 
                               
                                 
                                 
                x1=y1=z1=0.0;
                        

        	
                
             
                 l=0;
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
        		
        		  	   a_temp=(2*x0-2*x);
                       b_temp=2*y0-2*y;
                       c_temp=2*z0-2*z;
                       d_temp=-vx;
                       e_temp=-vy;
                       f_temp=-vz;
                       g_temp=2*x0/(4.068072088*Math.pow(10, 13));
                       h_temp=2*y0/(4.068072088*Math.pow(10, 13));
                       i_temp=2*z0/(4.040838498*Math.pow(10, 13)); 
                  //   System.out.println("a,b,c "+a+" "+b+" "+c+" "+d+" "+e+" "+f+" "+g+" "+h+" "+i);
                                     
        		
        		 

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
                	temp=0.0;
        			for(int n=0;n<3;n++)
        			{
        			   temp+=inverse[M][n]*fx0[n];
        			   
        			}
        			
              
        			if(M==0)
                     x1=x0-temp;
                    
        			else if(M==1)
        			 y1=y0-temp;

        			else if(M==2)
        			z1=z0-temp;
         
        			
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
                
                  
                  
          }while(l<20);//(perx>1||perx<0)||(perx>1||perx<0));
                
        System.out.println("Ground point :x0="+x0+" y0="+y0+" z0="+z0);
        
        System.out.println("dop"+(vx*(x-x0)+vy*(y-y0)+vz*(z-z0)));
                
        //Ground point :x0=428058.1781461849 y0=5840939.894982393 z0=2517576.4973233296          
          //calculated lat phiiiiiiiiii ====23.24948553039226
        //t is 16.292544605832518      
                
        /*1.Edit temporary file path to store intermediate variables. */
        String path=new String("/home/aniket/gcps");
        FileOutputStream fos = new FileOutputStream(path);
        DataOutputStream dos = new DataOutputStream(fos);
        dos.writeDouble(x0);
        dos.writeDouble(y0);
        dos.writeDouble(z0);
        dos.writeInt(l);
        dos.writeInt(p);
        dos.close();
                  
                     
        		
                 phi=Math.asin(z0/Math.sqrt(major*major*(1-2*ecc*ecc+Math.pow(ecc,4))+z0*z0*ecc*ecc));
                phi=(phi*180)/Math.PI;    
                System.out.println("calculated lat phiiiiiiiiii ===="+phi);    
            
              //***************for riteish baseline retained for cross check
                double per,t1,t=tac,ft,ftd,i0,i1,i2,i3,i4,i5;
                
                
                i5=3*(a[3][0]*a[3][0]+b[3][0]*b[3][0]+c[3][0]*c[3][0]);
                i4=5*(a[2][0]*a[3][0]+b[2][0]*b[3][0]+c[2][0]*c[3][0]);
                i3=4*(a[1][0]*a[3][0]+b[1][0]*b[3][0]+c[1][0]*c[3][0])+2*(a[2][0]*a[2][0]+b[2][0]*b[2][0]+c[2][0]*c[2][0]);
                i2=3*(a[1][0]*a[2][0]+a[0][0]*a[3][0]+b[1][0]*b[2][0]+b[0][0]*b[3][0]+c[1][0]*c[2][0]+c[0][0]*c[3][0])-3*(a[3][0]*x0+b[3][0]*y0+c[3][0]*z0);
                i1=a[1][0]*a[1][0]+2*a[0][0]*a[2][0]+b[1][0]*b[1][0]+2*b[0][0]*b[2][0]+c[1][0]*c[1][0]+2*c[0][0]*c[2][0]-2*(a[2][0]*x0+b[2][0]*y0+c[2][0]*z0);
                i0=a[0][0]*a[1][0]+b[0][0]*b[1][0]+c[0][0]*c[1][0]-(a[1][0]*x0+b[1][0]*y0+c[1][0]*z0);
                            
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
               

                 x1=a[0][0]+a[1][0]*t+a[2][0]*t*t+a[3][0]*t*t*t;
                
                 y1=b[0][0]+b[1][0]*t+b[2][0]*t*t+b[3][0]*t*t*t;
                 z1=c[0][0]+c[1][0]*t+c[2][0]*t*t+c[3][0]*t*t*t;
                 double vx1 = a[1][0]+2*a[2][0]*t+3*a[3][0]*t*t;
                 double vy1 = b[1][0]+2*b[2][0]*t+3*b[3][0]*t*t;
                 double vz1 = c[1][0]+2*c[2][0]*t+3*c[3][0]*t*t;
                
                  System.out.println("t : "+t+" tac "+tac);
                          	
                            	
                            	 double Tmasterazimuth=t+temp;         	
                            	 double Tmasteraziline;
                                 Tmasteraziline=(Tmasterazimuth-ta0)*PRF+1;

                                 //range eqn solving for range offset computation
             	                double R;
             	                R=Math.sqrt((x1-x0)*(x1-x0)+(y1-y0)*(y1-y0)+(z1-z0)*(z1-z0));
             	                double Tmasterrange;
             	                Tmasterrange=((R-(tr0*Math.pow(10,-3)*3*Math.pow(10,8)/2))/dR)+1;
             	                
                                 System.out.println("xyzvx "+x1+"\t"+y1+"\t"+z1+"\t"+vx1+"\t"+vy1+"\t"+vz1+"\t"+"T azimuth "+Tmasterazimuth+"T masterline "+Tmasteraziline+"T range no."+Tmasterrange+" R1  "+R);
                
                	System.out.println("center"+tac+" "+trc);
                
              
                System.out.println("\n\n"+ATA[0][1]+"\n"+AT[0][0]+" "+A[0][1]+" "+AT[0][1]+" "+A[1][1]+" "+AT[0][2]+" "+A[2][1]+" "+AT[0][3]+" "+A[3][1]+" "+AT[0][4]+" "+A[4][1]);
              
                System.out.println("xyzvx.... "+x1+"\t"+y1+"\t"+z1+"\t   vx vy vz"+vx1+"\t"+vy1+"\t"+vz1+"\t"+"T azimuth "+Tmasterazimuth+"T masterline "+Tmasteraziline+"T range no."+Tmasterrange+" R1  "+R);
                
           //*****************riteish ends here   
                  
               
               
                
                
                
            }
            
		}//main ends here
	    
	    /*Functio to construct LT */
		private static void constructLT()
		{
			for(int i=0;i<4;i++)
				for(int j=0;j<4;j++)
					LT[j][i]=L[i][j];	
		}
		
		/*Functio for decompose*/
		private static void decompose()
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
			for(i=0;i<4;i++)
			{
				for(j=0;j<4;j++)
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
		static void constructTrans()
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
