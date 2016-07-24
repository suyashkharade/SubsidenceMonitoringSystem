package CoregDoris;

import java.io.IOException;
import java.util.StringTokenizer;

import jdoris.SpectralUtils;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.jblas.ComplexDoubleMatrix;
import org.jblas.DoubleMatrix;

public class Map extends Mapper<LongWritable,Text,LongWritable,Text>{
	double avgm,avgs;
	long keys[]=new long[driver.linesperwin*driver.pixelsperwin];
	int linescnt,pixelscnt,cnt;
	Long pixelno[]=new Long[driver.linesperwin*driver.pixelsperwin],lineno[]=new Long[driver.linesperwin*driver.pixelsperwin];
	double real1,img1,real2,img2;
	DoubleMatrix magm=new DoubleMatrix(driver.linesperwin,driver.pixelsperwin),mags=new DoubleMatrix(driver.linesperwin,driver.pixelsperwin);
	ComplexDoubleMatrix magm2=new ComplexDoubleMatrix(2*driver.linesperwin, 2*driver.pixelsperwin);
	ComplexDoubleMatrix mags2=new ComplexDoubleMatrix(2*driver.linesperwin, 2*driver.pixelsperwin);
	ComplexDoubleMatrix D=new ComplexDoubleMatrix(2*driver.linesperwin, 2*driver.pixelsperwin);
	ComplexDoubleMatrix one=new ComplexDoubleMatrix(2*driver.linesperwin, 2*driver.pixelsperwin);
	@Override
	protected void setup(Context context)
			throws IOException, InterruptedException {
		super.setup(context);
			avgm=avgs=0;
			linescnt=pixelscnt=0;
			cnt=0;
	}
	
	public void map(LongWritable key, Text value,
			Context context) throws IOException, InterruptedException {
			String line=value.toString();
			StringTokenizer st=new StringTokenizer(line);
			
			
			
			keys[cnt]=Long.parseLong(st.nextToken());
			lineno[cnt]=Long.parseLong(st.nextToken());
			pixelno[cnt++]=Long.parseLong(st.nextToken());
			real1=Double.parseDouble(st.nextToken());
			img1=Double.parseDouble(st.nextToken());
			real2=Double.parseDouble(st.nextToken());
			img2=Double.parseDouble(st.nextToken());
			
			
			magm.put(linescnt, pixelscnt, Math.sqrt(real1*real1+img1*img1));
			mags.put(linescnt, pixelscnt, Math.sqrt(real2*real2+img2*img2));
			
			pixelscnt++;
			if(pixelscnt==driver.pixelsperwin)
			{
				linescnt++;
				if(linescnt==driver.linesperwin)
				{
					double maxcorr = -999;
					long max_corr_line=0;
					long max_corr_pixel=0 ;
		    		long half_L,half_P;
					avgm=magm.mean();
					avgs=mags.mean();
					magm=magm.sub(avgm);
					mags=mags.sub(avgs);

					for(int i=0;i<driver.linesperwin;i++)
					{
						for(int j=0;j<driver.pixelsperwin;j++)
						{
							D.putReal(driver.linesperwin+i,driver.pixelsperwin+j,Math.pow(magm.get(driver.linesperwin-i-1,driver.pixelsperwin-1-j),2));
							D.putImag(driver.linesperwin+i,driver.pixelsperwin+j,mags.get(i,j)*mags.get(i,j));
							one.putReal((driver.linesperwin/2)+i,(driver.pixelsperwin/2)+j, 1.0);
							magm2.putReal(i,j,magm.get(i, j));
							mags2.putReal((driver.linesperwin/2)+i,(driver.pixelsperwin/2)+j,mags.get(i, j));
						}
					}
					SpectralUtils.fft2D_inplace(D);
					SpectralUtils.fft2D_inplace(one);
					SpectralUtils.fft2D_inplace(magm2);
					SpectralUtils.fft2D_inplace(mags2);
					magm2=magm2.conj();
				
					one=one.conj();
					mags2.muli(magm2);
					D.muli(one);
					SpectralUtils.invfft2D_inplace(mags2);
					SpectralUtils.invfft2D_inplace(D);
					for(int i=0;i<driver.linesperwin;i++)
					{
						for(int j=0;j<driver.pixelsperwin;j++)
						{
							Double tmp=mags2.get(i,j).real()/(Math.sqrt(D.get(i,j).real()*D.get(i,j).imag()));
							if(maxcorr<tmp)
							{
								maxcorr=tmp;
								max_corr_line=i;
								max_corr_pixel=j;
							}
							context.write(new LongWritable(keys[i*driver.pixelsperwin+j]), new Text(lineno[i*driver.pixelsperwin+j].toString()+"\t"+pixelno[i*driver.pixelsperwin+j]+"\t"+tmp.toString()));
						}
					}
					
					half_L=(driver.linesperwin)/2;
					half_P=(driver.pixelsperwin/2);
					
					System.out.println("Max corelation="+maxcorr+"Line="+max_corr_line+"Pixel="+max_corr_pixel);
					System.out.println("Max corelation="+maxcorr+"Line="+max_corr_line+"Pixel="+max_corr_pixel);
					context.write(new LongWritable(max_corr_line-half_L), new Text(""+(max_corr_pixel-half_P)+"\t"+maxcorr));
					linescnt=0;
					cnt=0;
				
					/*long start=keys[0];
					for(int i=0;i<driver.linesperwin*2;i++)
					{
						for(int j=0;j<driver.pixelsperwin*2;j++)
						{
					//		D.putReal(driver.linesperwin+i,driver.pixelsperwin+j,magm.get(driver.linesperwin-i-1,driver.pixelsperwin-1-j)*magm.get(driver.linesperwin-i-1,driver.pixelsperwin-1-j));
						//	D.putImag(driver.linesperwin+i,driver.pixelsperwin+j,mags.get(i,j)*mags.get(i,j));
							//one.putReal(driver.linesperwin/2+i,driver.pixelsperwin/2+ j, 1);
						//	mags2.putReal(i,j,magm.get(i, j));
							///mags2.putReal(driver.linesperwin/2+i,driver.pixelsperwin/2+j,mags.get(i, j));
						
							//context.write(new LongWritable(keys[i*driver.pixelsperwin+j]), new Text(magm.get(i, j)+"\t"+mags.get(i, j)+"\t"));
							
							context.write(new LongWritable(start), new Text(magm2.get(i, j)+"\t"+mags2.get(i, j)+"\t"));
							start++;
						}
					}
					*/
				}
				pixelscnt=0;
				
			}

	}
}