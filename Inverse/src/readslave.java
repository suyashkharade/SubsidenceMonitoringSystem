
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class readslave {



	static int centerline,centerpixel;
	static int i=0,j=0,k=0,p=0,q=0,r=0;
	static double ta0,tac,trc;
	static double PRF;
	static double SF;
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
		public static void main(String str[]) throws IOException
		{
			int len;
			
            try (DataInputStream br = new DataInputStream(new FileInputStream("/media/ubuntu/Work/ers/ers1/LEADER_ers2.DAT"))) {
                len=br.read(buf);
                System.out.println("Length "+len);
                
                System.out.println("\n1.FILE DESCRIPTOR RECORD (FIXED SEGMENT)\n");
                
                System.out.print("Record Sequence no "+readBin(0,4));
                
                System.out.print("\n1-st record sub-type code "+readBin(4,1));
                
                System.out.print("\nRecord type code "+readBin(5,1));
                
                System.out.print("\n2-st record sub-type code "+readBin(6,1));
                
                System.out.print("\n3-st record sub-type code "+readBin(7,1));
                
                System.out.print("\nLength of this record "+readBin(8,4));
                
                System.out.print("\nASCII/EBCDIC flag "+readChar(12,2));
                
                System.out.print("\nFormat control document ID for this data file format "+readChar(16,12));
                
                System.out.print("\nFormat control document revision level "+readChar(28,2));
                
                System.out.print("\nFile design descriptor revision letter "+readChar(30,2));
                
                System.out.print("\nGenerating software release and revision level "+readChar(32,12));
                
                System.out.print("\nFile number "+readChar(44,4));
                
                System.out.print("\nFile name "+readChar(48,16));
                
                System.out.print("\nRecord sequence and location type flag "+readChar(64,4));
                
                System.out.print("\nsequence number location "+readChar(68,8));
                
                System.out.print("\nsequence number field length  "+readChar(76,4));
                
                System.out.print("\n Record code n location type flag "+readChar(80,4));
                
                System.out.print("\nRecord code location "+readChar(84,8));
                
                System.out.print("\nRecord code field length "+readChar(92,4));
                
                System.out.print("\nRecord length n location type flag "+readChar(96,4));
                
                System.out.print("\nRecord length location "+readChar(100,8));
                
                System.out.print("\nRecord length field length  "+readChar(108,4));
                //System.out.print("\nNumber of data points (up to 64) "+readChar(2747,4));
                
                
                System.out.println("\n\nFILE DESCRIPTOR RECORD VARIABLE SEGMENT");

                System.out.print("\nNumber of data set summary records "+readChar(180,6));
                
                System.out.print("\nData set summary record length "+readChar(186,6));
                
                System.out.print("\nNumber of map projection data records "+readChar(192,6));
                
                System.out.print("\nMap projection record length "+readChar(198,6));
                
                System.out.print("\nMNumber of platform pos. data records "+readChar(204,6));
                
                System.out.print("\nPlatform position record length "+readChar(210,6));
                
                System.out.print("\nNumber of attitude data records "+readChar(216,6));
                
                System.out.print("\nAttitude data record length "+readChar(222,6));
                
                System.out.print("\nNumber of radiometric data records "+readChar(228,6));
                
                System.out.print("\nRadiometric record length "+readChar(234,6));
                
                System.out.print("\nNumber of rad. compensation records "+readChar(240,6));
                
                System.out.print("\nRadiometric compensation rec. length "+readChar(246,6));
                
                System.out.print("\nNumber of data quality summary records "+readChar(252,6));
                
                System.out.print("\nData quality summary record length "+readChar(258,6));
                
                System.out.print("\nNumber of data histograms records "+readChar(264,6));
                
                System.out.print("\nData histogram record length "+readChar(270,6));
                
                System.out.print("\nNumber of range spectra records "+readChar(276,6));
                
                System.out.print("\nRange spectra record length "+readChar(282,6));
                
                System.out.print("\nNumber of DEM descriptor records "+readChar(288,6));
                
                System.out.print("\nDEM descriptor record length "+readChar(294,6));
                
                System.out.print("\nNumber of Radar par. update records "+readChar(300,6));
                
                System.out.print("\nRadar par. update record length "+readChar(306,6));
                
                System.out.print("\nNumber of Annotation data records "+readChar(312,6));
                
                System.out.print("\nAnnotation data record length "+readChar(318,6));
                            
                System.out.print("\nNumber of Det.processing records "+readChar(324,6));
                
                System.out.print("\nDet.processing record length "+readChar(230,6));
                
                System.out.print("\nNumber of Calibration records "+readChar(236,6));
                
                System.out.print("\nCalibration record length "+readChar(342,6));
                
                System.out.print("\nNumber of GCP records "+readChar(348,6));
                
                System.out.print("\nGCP record length "+readChar(354,6));
                
                System.out.print("\nNumber of Facility data records "+readChar(348,6));
                
                System.out.print("\nFacility data record length "+readChar(354,6));
                
                System.out.println("\n\nDATASET SUMMARY RECORDS\n");
                
                System.out.print("Record Sequence no "+readBin(720,4));
                
                System.out.print("\n1-st record sub-type code "+readBin(724,1));
                
                System.out.print("\nRecord type code "+readBin(725,1));
                
                System.out.print("\n2-st record sub-type code "+readBin(726,1));
                
                System.out.print("\n3-st record sub-type code "+readBin(727,1));
                
                System.out.print("\nLength of this record "+readBin(728,4));
                
                System.out.print("\nData set Summary Record sequence number (starts at 1) "+readChar(732,4));
                
                System.out.print("\nSAR channel indicator "+readChar(736,4));
                
                System.out.println("\n\nSCENE PARAMETERS\n");
                
                System.out.print("\nScene reference number (if one exits) "+readChar(756, 32));
                
                System.out.print("\nInput scene centre time "+readChar(788, 32));
                
                System.out.printf("\nProcessed scene centre geodetic latitude defined as positive to the north  of the equator and negative to the south (deg)"+readChar(836,16));
                
                System.out.printf("\nProcessed scene centre geodetic longitude defined as positive to the east of the prime meridian and negative to the west. (deg.)"+readChar(852,16));
                
                System.out.print("\nProcessed scene centre true heading as calculated relative to North (deg)"+readChar(868,16));
                
                System.out.print("\nEllipsoid designator "+readChar(884,16));
                
                System.out.print("\nEllipsoid semimajor axis (km)   - (R) "+readChar(900,16));
                
                System.out.print("\nEllipsoid semiminor axis (km)   - (R) "+readChar(916,16));
                
                System.out.print("\nEarth\"s mass times Gravitational constant - (M*G) "+readChar(932,16));
                
                System.out.print("\nEllipsoid J2 parameter "+readChar(964,16));
                
                System.out.print("\nEllipsoid J3 parameter "+readChar(980,16));
                
                System.out.print("\nEllipsoid J4 parameter "+readChar(996,16));
                
                System.out.printf("\nScene center line number "+readChar(1044,8));
                
                centerline=Integer.parseInt(readChar(1044,8).trim());
                
                System.out.printf("\nScene center pixel number "+readChar(1052,8));
                
                centerpixel=Integer.parseInt(readChar(1052,8).trim());
                
                System.out.printf("\nProcessed scene length including zero fill "+readChar(1060,16));
                
                System.out.printf("\nProcessed scene width including zero fill "+readChar(1076,16));
                
                System.out.printf("\nNumber of SAR channels "+readChar(1108,4));
                
                System.out.printf("\nSensor platform mission identification "+readChar(1116,16));
                
                System.out.printf("\nSensor id "+readChar(1132,32));
                
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
                
                System.out.printf("\nRange pulse (chirp) amplitude coefficient constant term (offset from DC) (Hz)) "+readChar(1254,16));
                
                System.out.printf("\nRange pulse (chirp) amplitude coefficient (linear term (Hz/sec)) "+readChar(1270,16));
                
                System.out.printf("\nRange pulse amplitude coefficient(quadratic term) "+readChar(1286,16));
                
                System.out.printf("\nRange pulse amplitude coefficient (cubic term) "+readChar(1292,16));
                
                System.out.printf("\nRange pulse amplitude coefficient (quartic term) "+readChar(1318,16));
                
                System.out.printf("\nRange pulse phase coefficient (offset in radiants) "+readChar(1334,16));
                          
                System.out.printf("\nRange pulse phase coefficient (linear term in rads./sec)"+readChar(1350,16));
                
                System.out.printf("\nRange pulse phase coefficient (quadratic term in rads.sec) "+readChar(1366,16));
                
                System.out.printf("\nRange pulse phase coefficient (cubic term) "+readChar(1382,16));
                
                System.out.printf("\nRange pulse phase coefficient (quartic term) "+readChar(1398,16));
                
                System.out.printf("\nDown linked data chirp extraction index "+readChar(1414,8));
                
                System.out.printf("\nSampling rate (MHz) "+readChar(1430,16));
                SF=Double.parseDouble(readChar(1430,16));
                
                
                System.out.printf("\nRange gate at early edge (in time) at the start of the image (?sec) "+readChar(1446,16));
                
                System.out.printf("\nRange pulse length (?sec) "+readChar(1462,16));
                
                System.out.printf("\nRange compressed flag "+readChar(1482,4));
                
                System.out.printf("\nQuantization in bits per channel 5I 5Q/6I 6Q for OGRC/OBRC "+readChar(1518,8));
                
                System.out.printf("\nQuantizer descriptor (eg: UNIFORM$I,Q$) "+readChar(1526,12));
                
                System.out.printf("\nDC Bias for I-component "+readChar(1538,16));
                
                System.out.printf("\nDC Bias for Q-component "+readChar(1554,16));

                System.out.printf("\nGain imbalance for I & Q "+readChar(1570,16));
                
                System.out.printf("\nAntenna mechanical boresight relative to platform vertical axis at the start of the image positive to the right negative to the left.(deg) "+readChar(1634,16));
                
                System.out.printf("\nNominal PRF (Hz)"+readChar(1654,16));
                PRF=Double.parseDouble(readChar(1654,16));
                
               
/**************************************************************************************************************/                
                int m=719;
                
                System.out.println("\n\nSENSOR SPECIFIC PARAMETERS\n");

                System.out.printf("\nSatellite encoded binary time code "+readChar(m+983,16));
                
                System.out.printf("\nSatellite clock time <YYYYMMDDhhmmssttt$$$...$> "+readChar(m+999,32));
                
                System.out.printf("\nSatellite clock increment (nano-secs)) "+readChar(m+1031,8));
                
                System.out.printf("\nspare "+readChar(m+1039,8));
                
                System.out.printf("\nProcessing facility identifier "+readChar(m+1047,16));
                
                System.out.printf("\nProcessing system identifier "+readChar(m+1063,8));
                
                System.out.printf("\nProcessing version identifier "+readChar(m+1071,8));
                
                System.out.printf("\nProduct type specifier "+readChar(m+1111,32));
                
                System.out.printf("\nProcessing algorithm identifier "+readChar(m+1143,32));
                
                System.out.printf("\nNominal number of looks processed in Azimuth "+readChar(m+1175,16));
                
                System.out.printf("\nNominal effective number of looks processed in Range "+readChar(m+1191,16));
                
                System.out.printf("\nBandwidth per look in Azimuth ( Hz ) "+readChar(m+1207,16));
                
                System.out.printf("\nBandwidth per look in Range ( MHz ) "+readChar(m+1223,16));
                
                System.out.printf("\nTotal processor bandwidth in Azimuth "+readChar(m+1239,16));
                
                System.out.printf("\nTotal processor bandwidth in Range (MHz) "+readChar(m+1255,16));
                
                System.out.printf("\nWeighing function designator in Azimuth "+readChar(m+1271,32));
                
                System.out.printf("\nWeighing function designator in Range "+readChar(m+1303,32));
                
                System.out.printf("\nData input source (eg:HDDT  identifier) "+readChar(m+1335,16));
                
                System.out.printf("\nNominal resolution equal to 3dB points in ground range (meter) "+readChar(m+1351,16));
                
                System.out.printf("\nNominal resolution in Azimuth (meter) "+readChar(m+1367,16));
                
                System.out.printf("\nAlong track Doppler frequency constant term at early edge of image (Hz) "+readChar(m+1415,16));
                
                System.out.printf("\nAlong track Doppler frequency linear term at early edge of the image (Hz/sec) "+readChar(m+1431,16));
                
                System.out.printf("\nAlong track Doppler frequency quadratic term at early edge of the image (Hz/sec/sec) "+readChar(m+1447,16));
                
                System.out.printf("\nspare "+readChar(m+1463,16));
                
                System.out.printf("\nCross track Doppler frequency constant term at early edge of the image (Hz) "+readChar(m+1479,16));
                
                System.out.printf("\nCross track Doppler frequency linear term at early edge of the image (Hz/sec) "+readChar(m+1495,16));
                
                System.out.printf("\nCross track Doppler frequency quadratic term at early edge of the image (Hz/pixeI/sec) "+readChar(m+1511,16));
                
                System.out.printf("\nTime direction indicator along pixel direction(ie: INCREASE -ing  or DECREASE -ing ) "+readChar(m+1527,8));
                
                System.out.printf("\nTime direction indicator along line direction(ie: INCREASE -ing or DECREASE -ing ) "+readChar(m+1535,8));
                
                System.out.printf("\nAlong track Doppler frequency rate constant term at early edge of the image (Hz/sec) "+readChar(m+1543,16));
                
                System.out.printf("\nAlong track Doppler frequency rate linear term at early edge of the image (Hz/sec/sec) "+readChar(m+1559,16));
                
                System.out.printf("\nAlong track Doppler frequency rate quadratic term at early edge of the image (Hz/sec/sec/sec) "+readChar(m+1575,16));
                
                System.out.printf("\nspare "+readChar(m+1591,16));
                
                System.out.printf("\nCross track Doppler frequency rate constant term at near edge of the image (Hz/sec) "+readChar(m+1607,16));
                
                System.out.printf("\nCross track Doppler frequency rate linear term relative to near edge of the image (Hz/sec/sec) "+readChar(m+1623,16));
                
                System.out.printf("\nCross track Doppler frequency rate quadratic term relative to near edge of the image (Hz/sec/sec/sec) "+readChar(m+1639,16));
                
                System.out.printf("\nspare "+readChar(m+1655,16));
                
                System.out.printf("\nLine content indicator "+readChar(m+1671,8));
                
                System.out.printf("\nClutter lock applied flag "+readChar(m+1679,4));
                
                System.out.printf("\nAutofocussing applied flag "+readChar(m+1683,4));
                
                System.out.printf("\nLine spacing (meters) "+readChar(m+1687,16));
                
                System.out.printf("\nPixel spacing ( in range )(meters) "+readChar(m+1703,16));
                
                System.out.printf("\nProcessor range compression designator "+readChar(m+1719,16));
                
                System.out.printf("\nspare "+readChar(m+1735,16));
                
                System.out.printf("\nspare "+readChar(m+1751,16));
                
            /*    System.out.printf("\n "+readChar(m+,16));
                
                System.out.printf("\n "+readChar(m+,16));
                
                System.out.printf("\n "+readChar(m+,16));
                
                System.out.printf("\n "+readChar(m+,16));
              */  
                
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
                System.out.println("Sequence Number "+readBin(2606,4));
                System.out.println("Sequence Number "+readBin(m+1,4));
                
                System.out.println("1-st record sub-type code "+readBin(2610,1));
                System.out.println("1-st record sub-type code "+readBin(m+5,1));
                
                System.out.println("Record type code "+readBin(2611,1));
                
                System.out.println("2-nd record sub-type code "+readBin(2612,1));
                
                System.out.println("3-rd record sub-type code "+readBin(2613,1));
                
                System.out.println("Length of this record  "+readBin(2614,4));
                System.out.println("Length of this record  "+readBin(m+9,4));
                
                System.out.println("\nRad. compensation record Sequence number "+readBin(m+13,4));
                
                System.out.println("\nSAR channel indicator "+readBin(m+17,4));
                
                System.out.println("\nNumber of radiometric compensation data sets in the record "+readBin(m+21,8));
                
                System.out.println("\nCompensation data set size (bytes) "+readBin(m+29,8));
                
                System.out.println("\nCompensation data type designator (eg: RANGE  AZIMUTH  PIXEL , LINE "+readBin(m+37,8));
                
                System.out.println("\nCompensation  data   descriptor   (eg: elevation  antenna  pattern ,   range attenuation , resolution cell size , azimuth attenuation , etc.) "+readBin(m+45,32));
                
                System.out.println("\nNumber of compensation records "+readBin(m+77,4));
                
                System.out.println("\nSequence number in the full compensation table of the table contained in this record "+readBin(m+81,4));
                
                System.out.println("\nTotal number of compensation pairs in the full compensation table "+readBin(m+85,8));
                
                System.out.println("\nData pixel number corresponding to first correction value in compensation table "+readBin(m+93,8));
                
                System.out.println("\nData pixel number corresponding to last correction value in compensation table "+readBin(m+101,8));
                
                System.out.println("\nCompensation pixel group size (pixels) "+readBin(m+109,8));
                
                System.out.println("\nMin. table Offset value (dB) "+readBin(m+117,16));
                
                System.out.println("\nMin. table Gain value (dB) "+readBin(m+133,16));
                
                System.out.println("\nMax. table Offset value (dB) "+readBin(m+149,16));
                
                System.out.println("\nMax. table Gain value (dB) "+readBin(m+165,16));
                              
 
                
                
                
                
                ///Sequence number FOUR
                System.out.println("\n\nPLATFORM POSITION DATA RECORD\n");
                
                System.out.println("Sequence Number (FOUR)"+readBin(4226,4));
                
                System.out.println("1-st record sub-type code "+readBin(4230,1));
                
                System.out.println("Record type code "+readBin(4231,1));
                
                System.out.println("2-nd record sub-type code "+readBin(4232,1));
                
                System.out.println("3-rd record sub-type code "+readBin(4233,1));
                
                System.out.println("Length of this record "+readBin(4234,4));
                
                System.out.println("Number of data points (up to 64)"+readChar(4366,4));
                
                System.out.println("Year  of data point. (YYYY)"+readChar(4370,4));
                
                System.out.println("Month of data point. ($$MM)"+readChar(4374,4));
                
                System.out.println("Day   of data point. ($$DD)"+readChar(4378,4));
                
                System.out.println("Day in the year (GMT)"+readChar(4382,4));
                
                System.out.println("Seconds of day (GMT) of data"+readChar(4386,22));
                
                t=Double.parseDouble(readChar(4386,22));
                
                System.out.println("Time interval between DATA points (sec)"+readChar(4408,22));
                
                interval=Double.parseDouble(readChar(4408,22));
                
                System.out.println("Reference coordinate system "+readChar(4430,64));
                
                System.out.println("Greenwich mean hour angle (degrees) "+readChar(4494,22));
                
                System.out.println("Along track position error (meters) "+readChar(4516,16));
                
                System.out.println("Along track position error (meters) "+readChar(4532,16));
                
                System.out.println("Across track position error (meters) "+readChar(4548,16));
                
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
                
                System.out.println("\nFACILITY RELATED DATA RECORD GENERAL TYPE\n");
                
                System.out.println("Sequence Number (FIVE)"+readBin(5272,4));
                
                System.out.println("1-st record sub-type code "+readBin(5276,1));
                
                System.out.println("Record type code "+readBin(5277,1));
                
                System.out.println("2-nd record sub-type code "+readBin(5278,1));
                
                System.out.println("3-rd record sub-type code "+readBin(5279,1));
                
                System.out.println("Length of this record "+readBin(5280,4));
                
                System.out.println("incidence angle at first range pixel(at mid-azimuth,deg) "+readChar(5854,16));
                
                System.out.println("incidence angle at center range pixel(at mid-azimuth,deg) "+readChar(5870,16));
                
                System.out.println("incidence angle at last range pixel(at mid-azimuth,deg) "+readChar(5886,16));
                
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
                //b0,b1,b2,b3
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
                
                
                System.out.println("Trial e is ");
                for(int i=0;i<4;i++)
                	System.out.println(e[i][0]);
               
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
                
                tac=ta0+(centerline-1)/PRF;
                
                //Scaled tac
                tac=(tac-temp);
               // tac=tac-5876225.33;
                
                
                trc=tr0*Math.pow(10,-3)+(centerpixel-1)/(2*SF*1000000);
                
                
              
                double x1=a[0][0]+a[1][0]*tac+a[2][0]*tac*tac+a[3][0]*tac*tac*tac;
                
                double y1=b[0][0]+b[1][0]*tac+b[2][0]*tac*tac+b[3][0]*tac*tac*tac;
                double z1=c[0][0]+c[1][0]*tac+c[2][0]*tac*tac+c[3][0]*tac*tac*tac;
                double vx1=d[0][0]+d[1][0]*tac+d[2][0]*tac*tac+d[3][0]*tac*tac*tac;
                double vy1=e[0][0]+e[1][0]*tac+e[2][0]*tac*tac+e[3][0]*tac*tac*tac;
                double vz1=f[0][0]+f[1][0]*tac+f[2][0]*tac*tac+f[3][0]*tac*tac*tac;
                
                System.out.println("Test x1"+x1+" "+y1+" "+z1+" "+vx1+" "+vy1+" "+vz1+" ");
                
                double per,t1,t=tac,ft,ftd,i0,i1,i2,i3,i4,i5,i6,x0=1158557.7357517134,y0=5262415.375879012,z0=3412466.463955462;//x0= 1158248.2786581956,y0=5267536.275425667,z0=3393322.775855191;//x0=1183018.977471917,y0=5264862.852820739,z0=3388952.9258690397;
                
                       
                System.out.println("Trial a is ");
                for(int i=0;i<4;i++)
                	System.out.println(a[i][0]);
           
                
                System.out.println("Trial b is ");
                for(int i=0;i<4;i++)
                	System.out.println(b[i][0]);
                
                
                System.out.println("Trial c is ");
                for(int i=0;i<4;i++)
                	System.out.println(c[i][0]);
                
                
                System.out.println("Trial d is ");
                for(int i=0;i<4;i++)
                	System.out.println(d[i][0]);
                
                
                System.out.println("Trial e is ");
                for(int i=0;i<4;i++)
                	System.out.println(e[i][0]);
                
                System.out.println("Trial f is ");
                for(int i=0;i<4;i++)
                	System.out.println(f[i][0]);
  /*           
                i5=3*(a[3][0]*a[3][0]+b[3][0]*b[3][0]+c[3][0]*c[3][0]);
                i4=5*(a[2][0]*a[3][0]+b[2][0]*b[3][0]+c[2][0]*c[3][0]);
                i3=4*(a[1][0]*a[3][0]+b[1][0]*b[3][0]+c[1][0]*c[3][0])+2*(a[2][0]*a[2][0]+b[2][0]*b[2][0]+c[2][0]*c[2][0]);
                i2=3*(a[1][0]*a[2][0]+a[0][0]*a[3][0]+b[1][0]*b[2][0]+b[0][0]*b[3][0]+c[1][0]*c[2][0]+c[0][0]*c[3][0])-3*(a[3][0]*x0+b[3][0]*y0+c[3][0]*z0);
                i1=a[1][0]*a[1][0]+2*a[1][0]*a[2][0]-2*(a[2][0]*x0+b[2][0]*y0+c[2][0]*z0);
                i0=a[0][0]*a[1][0]-(a[1][0]*x0+b[1][0]*y0+c[1][0]*z0);
*/
                i6=a[3][0]*d[3][0]+b[3][0]*e[3][0]+c[3][0]*f[3][0];
                
                i5=a[3][0]*d[2][0]+a[2][0]*d[3][0]+b[3][0]*e[2][0]+b[2][0]*e[3][0]+c[3][0]*f[2][0]+c[2][0]*f[3][0];

                i4=a[3][0]*d[1][0]+a[2][0]*d[2][0]+a[1][0]*d[3][0]+b[3][0]*e[1][0]+b[2][0]*e[2][0]+b[1][0]*e[3][0]+c[3][0]*f[1][0]+c[2][0]*f[2][0]+c[1][0]*f[3][0];

                i3=a[3][0]*d[0][0]+a[2][0]*d[1][0]+a[1][0]*d[2][0]+a[0][0]*d[3][0]-d[3][0]*x0+b[3][0]*e[0][0]+b[2][0]*e[1][0]+b[1][0]*e[2][0]+b[0][0]*e[3][0]-e[3][0]*y0+c[3][0]*f[0][0]+c[2][0]*f[1][0]+c[1][0]*f[2][0]+c[0][0]*f[3][0]-f[3][0]*z0;
                
                i2=a[2][0]*d[0][0]+a[1][0]*d[1][0]+a[0][0]*d[2][0]+b[2][0]*e[0][0]+b[1][0]*e[1][0]+b[0][0]*e[2][0]+c[2][0]*f[0][0]+c[1][0]*f[1][0]+c[0][0]*f[2][0]-d[2][0]*x0-e[2][0]*y0-f[2][0]*z0;
                
                i1=a[1][0]*d[0][0]+a[0][0]*d[1][0]+b[1][0]*e[0][0]+b[0][0]*e[1][0]+c[1][0]*f[0][0]+c[0][0]*f[1][0]-d[1][0]*x0-e[1][0]*y0-f[1][0]*z0;
                
                i0=a[0][0]*d[0][0]+b[0][0]*e[0][0]+c[0][0]*f[0][0]-d[0][0]*x0-e[0][0]*y0-f[0][0]*z0;
                
                
/*                i0=a[0][0]*a[0][0]+b[0][0]*b[0][0]+c[0][0]*c[0][0]-2*a[0][0]*x0-2*b[0][0]*y0-2*c[0][0]*z0+x0*x0+y0*y0+z0*z0-Math.pow(850047.795,2);
                
                i1=2*(a[0][0]*a[1][0]+b[0][0]*b[1][0]+c[0][0]*c[1][0]-a[1][0]*x0-b[1][0]*y0-c[1][0]*z0);
                
                i2=a[1][0]*a[1][0]+2*a[0][0]*a[2][0]+b[1][0]*b[1][0]+2*b[0][0]*b[2][0]+c[1][0]*c[1][0]+2*c[0][0]*c[2][0]-2*a[2][0]*x0-2*b[2][0]*y0-2*c[2][0]*z0;//-3*Math.pow(10,8)*3*Math.pow(10,8)/4;
                
                i3=2*(a[0][0]*a[3][0]+a[1][0]*a[2][0]+b[0][0]*b[3][0]+b[1][0]*b[2][0]+c[0][0]*c[3][0]+c[1][0]*c[2][0]-a[3][0]*x0-b[3][0]*y0-c[3][0]*z0);
                
                i4=a[2][0]*a[2][0]+2*a[1][0]*a[3][0]+b[2][0]*b[2][0]+2*b[1][0]*b[3][0]+c[2][0]*c[2][0]+2*c[1][0]*c[3][0];
                
                i5=2*(a[2][0]*a[3][0]+b[2][0]*b[3][0]+c[2][0]*c[3][0]);
                
                i6=a[3][0]*a[3][0]+b[3][0]*b[3][0]+c[3][0]*c[3][0];
                */
               
                
                System.out.println("t is "+t);
                for(int i=0;i<=20;i++)
                {
                	
//                      ft=i5*Math.pow(t, 5)+i4*Math.pow(t, 4)+i3*Math.pow(t, 3)+i2*Math.pow(t, 2)+i1*t+i0;
                    
  //                  ftd=5*i5*Math.pow(t, 4)+4*i4*Math.pow(t, 3)+3*i3*Math.pow(t, 2)+2*i2*t+i1;
                	ft=i6*Math.pow(t, 6)+i5*Math.pow(t, 5)+i4*Math.pow(t, 4)+i3*Math.pow(t, 3)+i2*Math.pow(t, 2)+i1*t+i0;
                    
                    ftd=6*i6*Math.pow(t, 5)+5*i5*Math.pow(t, 4)+4*i4*Math.pow(t, 3)+3*i3*Math.pow(t, 2)+2*i2*t+i1;
                   //ft=Math.pow(t, 3)-20;
                	//ftd=3*t*t;
                	t1=t-(ft/ftd);
                	
                	per=((t1-t)/t)*100;
                	
                	t=t1;
                	
                	System.out.println("t : "+t+"Percentage error in t "+per+"   ft "+ft);
                }
               
                tac=t;

                 x1=a[0][0]+a[1][0]*tac+a[2][0]*tac*tac+a[3][0]*tac*tac*tac;
                
                 y1=b[0][0]+b[1][0]*tac+b[2][0]*tac*tac+b[3][0]*tac*tac*tac;
                 z1=c[0][0]+c[1][0]*tac+c[2][0]*tac*tac+c[3][0]*tac*tac*tac;
                 /*vx1=d[0][0]+d[1][0]*tac+d[2][0]*tac*tac+d[3][0]*tac*tac*tac;
                 vy1=e[0][0]+e[1][0]*tac+e[2][0]*tac*tac+e[3][0]*tac*tac*tac;
                 vz1=f[0][0]+f[1][0]*tac+f[2][0]*tac*tac+f[3][0]*tac*tac*tac;
                */
                 vx1=a[1][0]+2*a[2][0]*tac+3*a[3][0]*tac*tac;
                 vy1=b[1][0]+2*b[2][0]*tac+3*b[3][0]*tac*tac;
                 vz1=c[1][0]+2*c[2][0]*tac+3*c[3][0]*tac*tac;
                            	System.out.println("t : "+t+" tac "+tac);
                
                System.out.println("xyzvx "+x1+"\t"+y1+"\t"+z1+"\t"+vx1+"\t"+vy1+"\t"+vz1+"\t");
                
                		
    //            double mx=1630145.4188392828,my=5877199.154403538,mz=3751381.3209614186;
      double mx=1630203,my=5876824.092895963,mz=3751941.769568175;
                System.out.println("Baseline calculation------");
            	System.out.println("Baseline calculation------"+Math.sqrt((mx-x1)*(mx-x1)+(my-y1)*(my-y1)+(mz-z1)*(mz-z1))); 
                	System.out.println("center"+tac+" "+trc);
                //Displaying Vx,Vy,Vz matrices
                System.out.println("\n\tX\tY\tZ");
                for(i=0;i<5;i++)
                {
                	System.out.println(vx[i][0]+"\t"+vy[i][0]+"\t"+vz[i][0]+"\t");
                }
                
                //Displaying A
                System.out.println("\nMatrix A ");
                for(i=0;i<5;i++)
                {
                	for(j=0;j<4;j++)
                	{
                		System.out.print(A[i][j]+"\t");
                	}
                	System.out.println();
                }
                
              
              //Displaying A transpose
                System.out.println("\nMatrix A' ");
                for(i=0;i<4;i++)
                {
                	for(j=0;j<5;j++)
                	{
                		System.out.print(AT[i][j]+"\t");
                	}
                	System.out.println();
                }
                System.out.println("a matrix ");
                for(int i=0;i<4;i++)
                {
                System.out.println(a[i][0]);	
                }
                System.out.println("b matrix ");
                for(int i=0;i<4;i++)
                {
                System.out.println(b[i][0]);	
                }
                System.out.println("c matrix ");
                for(int i=0;i<4;i++)
                {
                System.out.println(c[i][0]);	
                }
                
              //Displaying A'A
                System.out.println("\nMatrix A'A ");
                for(i=0;i<4;i++)
                {
                	for(j=0;j<4;j++)
                	{
                		System.out.print(ATA[i][j]+"\t");
                	}
                	System.out.println();
                }
                
                System.out.println("\n\n"+ATA[0][1]+"\n"+AT[0][0]+" "+A[0][1]+" "+AT[0][1]+" "+A[1][1]+" "+AT[0][2]+" "+A[2][1]+" "+AT[0][3]+" "+A[3][1]+" "+AT[0][4]+" "+A[4][1]);
                       }
		}
		private static void constructLT()
		{
			for(int i=0;i<4;i++)
				for(int j=0;j<4;j++)
					LT[j][i]=L[i][j];	
		}
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

		public static float readFloat(int off) {  
		    ByteBuffer bu = ByteBuffer.wrap(buf);  
		    return bu.getFloat(off);
		} 
		
		static String readChar(int off,int size)
		{
                    String str="";
			for(int i=0;i<size;i++)
			str=str+(char)buf[off++];
			
                        return str;
		}
		static void constructTrans()
		{
			for(int i=0;i<5;i++)
				for(int j=0;j<4;j++)
					AT[j][i]=A[i][j];
		}
		static Integer readBin(int off,int size)
		{
			
			/*for(int i=0;i<size;i++)
			{
				System.out.print(buf[off++]);
			}
			return "";*/
			/*
			String str2="";
			
			for(int i=0;i<size;i++)
				 str2=str2+(((buf[off+i]))&0xff);
			
			return str2;
			*/
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


