package goldstein;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import org.jblas.ComplexDouble;
import org.jblas.ComplexDoubleMatrix;
//libgfortran;
public class PhaseFilterMain {

	//static double kernelArray[] = new double[5];
	static float goldstAlpha = (float) 0.2;
	static int width = 4894;
	static int height = 5158;
	static PhaseFilter goldstein;
	public static void main(String args[]) throws IOException {
		init();
		System.out.println("Copying done...!");

		System.out.println("Copying done...!");
		goldstein.filter();
		System.out.println("Copying done...!");

		//DataOutputStream out = new DataOutputStream(new FileOutputStream(
			//	"/home/aniket/Phase_filtered_Goldstein"));
	
		FileWriter f1 = new FileWriter ("/home/aniket/filtered_Goldstein_32_12_07.txt", true); 
		BufferedWriter b1 = new BufferedWriter (f1);
		
		System.out.println("Filtering done...!");
	for (int i = 0; i < height; i++)
		{	
		for (int j = 0; j < width; j++)
			{	
				
		
	//		System.out.println("Absolute "+Math.sqrt(Math.pow(goldstein.getData().get(0, i).real(),2)+Math.pow(goldstein.getData().get(0, i).imag(),2)));
		//	out.writeChars(""+goldstein.getData().get(0, i));
			//out.writeChars(""+i+"\t"+j+"\t"+goldstein.getData().get(i, j).real()+"\t"+goldstein.getData().get(i, j).imag()+"\n");
			 //out.writeUTF(i+"\t"+j+"\t"+goldstein.getData().get(i, j).real()+"\t"+goldstein.getData().get(i, j).imag()+"\n");
			 //out.writeInt(i);
			 //out.writeInt(j);
			 //out.writeDouble(goldstein.getData().get(i, j).real());
			 //out.writeDouble(goldstein.getData().get(i, j).imag());
			// out.writeFloat((float) goldstein.getData().get(i, j).real());
			// out.writeFloat((float) goldstein.getData().get(i, j).imag());
		
			
			b1.write(i+"\t"+j+"\t"+goldstein.getData().get(i, j).real()+"\t"+goldstein.getData().get(i, j).imag()+"\n");
			}
			System.out.println("i"+i);
		}
		//out.close();

	 System.out.println("df"+goldstein.getData().get(21, 2138).real()+"ffg"+goldstein.getData().get(21, 2139).real());
	}

	public static void init() throws IOException {

	//	double arrdata[][]=new double[height][width];
		double kernelArray[]={1,1,1,1,1};
		ComplexDoubleMatrix data;
		data = new ComplexDoubleMatrix(height,width);
		DataInputStream in = new DataInputStream(new FileInputStream(
				"/home/aniket/Downloads/multilookRefphase"));
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				String line = br.readLine();
				StringTokenizer st = new StringTokenizer(line);
				st.nextToken();
                st.nextToken();
				data.putReal(i,j,Double.parseDouble(st.nextToken()));
				data.putImag(i, j, Double.parseDouble(st.nextToken()));
				//arrdata[i][j]=Double.parseDouble(st.nextToken());
			}
		}
		//data = new ComplexDoubleMatrix(arrdata);
				
		br.close();
       
	   goldstein = new PhaseFilter("goldstein", data, 32, 12,
				kernelArray, goldstAlpha);
		
	}
}