import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class finale {
/*
static double tm=61861.74312846786,mx=1632014.8753856479,my=5865104.498961136,mz=3769398.9553901535;
static double ts=61861.08273967403,sx=1631886.552651787,sy=5865114.484739908,sz=3769414.315334531;
static double dist,Am=17693.94367741083,As=17844.481721873723,Nm=1869.1874423857294,Ns=1861.81869038828;	
*/
	static double tm,mx,my,mz;
	static double ts,sx,sy,sz;
	static double dist,Am,As,Nm,Ns;
	static float Fh,Fv;
public static void main(String[] args) throws IOException{
    
	
	/* Temporary files holding certain parameters of master and slave output*/
	String path1=new String("/home/aniket/xyz1");
	
    FileInputStream fos1 = new FileInputStream(path1);
    DataInputStream dos1 = new DataInputStream(fos1);
    
    String path2=new String("/home/aniket/xyz2");
	
    FileInputStream fos2 = new FileInputStream(path2);
    DataInputStream dos2 = new DataInputStream(fos2);
    
    
    
    sx=dos2.readDouble();
    sy=dos2.readDouble();
    sz=dos2.readDouble();
    ts=dos2.readDouble();
    As=dos2.readDouble();
    Ns=dos2.readDouble();
    System.out.println(" "+sx+" "+sy+" "+sz+" "+ts+" "+" "+As+" "+Ns);
    mx=dos1.readDouble();
    my=dos1.readDouble();
    mz=dos1.readDouble();
    tm=dos1.readDouble();
    Am=dos1.readDouble();
    Nm=dos1.readDouble();
    System.out.println(" "+mx+" "+my+" "+mz+" "+tm+" "+" "+Am+" "+Nm);
   dos1.close();
   dos2.close(); 
		
    dist =Math.sqrt((mx-sx)*(mx-sx)+(my-sy)*(my-sy)+(mz-sz)*(mz-sz));
		
    double px=423127.45733329916,py=5838627.012801608,pz=2532141.780721806; 
	double Bpar=/*(299792458)*(master_trc-slave_trc);*/Math.sqrt((mx-px)*(mx-px)+(my-py)*(my-py)+(mz-pz)*(mz-pz))-Math.sqrt((sx-px)*(sx-px)+(sy-py)*(sy-py)+(sz-pz)*(sz-pz));

    System.out.println("Baseline length="+dist +"metre"+" parallel baseline="+Bpar);
		Fv=(float)(Am-As);
		Fh=(float)(Nm-Ns);
		System.out.println("Horizontal offset ="+Fh);
		System.out.println("Vertical offset ="+Fv);	
		
		
   }
}
