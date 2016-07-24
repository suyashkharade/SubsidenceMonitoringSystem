package readimg1;

import java.io.FileInputStream;
import java.io.IOException;

public class ReadMeta1 {
	
	 public static String str1,str2;
	 public static byte[] temp=new byte[19612*26002];;
				
	 static	int index=0;
	public  String A(int index,int length)
	{
		str2="";
		for(int i=0;i<length;i++)
		 str2=str2+(char)temp[index+i];
		return str2;	
	}	

	public  String B(int index,int length)
	{
		str2="";
		
		for(int i=0;i<length;i++)
			 str2=str2+(((temp[index+i]))&0x0ff);
		
		return str2;	
	}
	public  String I(int index,int length)
	{
		 long temper=0;
		 int j=0;
           for(int i=length-1;i>=0;i--)
           {
        	   temper|=((long)temp[index+j]&0xff)<<8*i;
        	   j++;
            }
		str2=""+temper;
				return str2;	
	}
	public static void main(String args[])
	throws IOException
	{
		
		System.out.println("This program displays Vol directory file");
		FileInputStream fin;
	
		
		ReadMeta1 img1 = new ReadMeta1();
			fin=new FileInputStream("/media/aniket/Study/Project ASMA/Dhanbad Data CD/SLC1/IMGDATA.DAT");
			fin.read(temp);
			
			
			/////////////////
		
					
			       
				    
				    

				    str1="Record sequence number      :";
				    str2=img1.I(0,4);
				    System.out.println(str1+str2);//print_bin(temp));
			       
				    
				    
				    str1="1st Record subtype code";
				       str2=img1.B(4,1);
				       System.out.println(str1+str2);
				   	   	    
				     	 str1="Record subtype code";
				         str2=img1.B(5,1);
				        	  System.out.println(str1+str2);
				        	  
				        	 
				        	  str1="2nd Record subtype code";
					            str2=img1.B(6,1);
					            
					            System.out.println(str1+str2);
					          	
					          	
					            
					            str1="3rd Record subtype code";
					            str2=img1.B(7,1);
					            System.out.println(str1+str2);
					 
					          	/**/
					          	 str1="Length of this Record     :";
					     	    str2=img1.I(8,4);
					     	    System.out.println(str1+str2);
					     	    
					     	   str1="ASCII/EBCDIC Flag  :";
					    	    str2=img1.A(12,2);
					    	    System.out.println(str1+str2);
					    	    
					    	    str1="Format Control Document ID  :";
					    	    str2=img1.A(16,12);
					    	    System.out.println(str1+str2);
					    	    
					    	    
					    	    str1="Format Control Document Revision letter  :";
					    	    str2=img1.A(28,2);//
					    	    System.out.println(str1+str2);
					    	    
					    	    str1="File design Descriptor Revision letter  :";
					    	    str2=img1.A(30,2);//
					    	    System.out.println(str1+str2);
					    	    
					    	    str1="Generating Software release and revision level  :";
					    	    str2=img1.A(32,12);//
					    	    System.out.println(str1+str2);
					    	    
					    	    str1="File number  :";
					    	    str2=img1.A(44,4);//
					    	    System.out.println(str1+str2);
				 	        	 
					    	    str1="File name:";
					    	    str2=img1.A(48,16);//
					    	    System.out.println(str1+str2);
					    	    
					    	    str1="Record length and location type flag:";
					 	         str2=img1.A(64,4);
					 	        	  System.out.println(str1+str2); 
				 	        	
					 	        	  str1="Sequence Number:";
				 	 	         str2=img1.A(68,8);
				 	 	        	  System.out.println(str1+str2);
				 	        	  
				 	 	        	  str1="Sequence number field:";
				 		 	         str2=img1.A(76,4);
				 		 	        	  System.out.println(str1+str2);
				 		 	        	
				 		 	        	 str1="Record code and location type flag:";
				 			 	         str2=img1.A(80,4);
				 			 	        	  System.out.println(str1+str2);
				 		 	        	  
				 			 	        	 str1="Record code location:";
				 				 	         str2=img1.A(84,8);
				 				 	        	  System.out.println(str1+str2);
				 				 	        	  
				 				 	        	 str1="Record code field length:";
				 					 	         str2=img1.A(92,4);
				 					 	        	  System.out.println(str1+str2);
				 					 	        	  
				 					 	        	 str1="Record length and location type flag:";
				 						 	         str2=img1.A(96,4);
				 						 	        	  System.out.println(str1+str2); 
				 						 	        	 
				 						 	        	  str1="Record length location:";
				 							 	         str2=img1.A(100,8);
				 							 	        	  System.out.println(str1+str2);
				 						 	        	  
				 							 	        	 str1="Record length field length:";
				 								 	         str2=img1.A(108,4);
				 								 	        	  System.out.println(str1+str2);
				 						 	        	  
				 			
				    	   
				    	   
				    	    str1="Number of Data Records  :";
				    	    str2=img1.A(180,6);//
				    	    System.out.println(str1+str2);

				    	    str1="Record length  :";
				    	    str2=img1.A(186,6);//
				    	    System.out.println(str1+str2);

				    	    str1="Number of bits per sample  :";
				    	    str2=img1.A(216,4);//
				    	    System.out.println(str1+str2);
			  
				    	    str1="Number of samples per data group  :";
				    	    str2=img1.A(220,4);//
				    	    System.out.println(str1+str2);
				    	 
				    	    str1="Number  bytes per data group  :";
				    	    str2=img1.A(224,4);//
				    	    System.out.println(str1+str2);
				    	    
				    	    str1="   Justification and order of samples within data group  :";
				    	    str2=img1.A(228,4);//
				    	    System.out.println(str1+str2);
				    	    
				    	    str1="Number of SAR channels in this file   :";
				    	    str2=img1.A(232,4);//
				    	    System.out.println(str1+str2);
				    	    
				    	    str1="Number of lines per data set   (minimum) :";
				    	    str2=img1.A(236,8);//
				    	    System.out.println(str1+str2);
				    	    
				    	    str1=" Number of left border pixels per line    :";
				    	    str2=img1.A(244,4);//
				    	    System.out.println(str1+str2);
				    	    
				    	    str1="Total number of data groups per line per SAR channel  :";
				    	    str2=img1.A(248,8);//
				    	    System.out.println(str1+str2);
				    	    
				    	    str1=" Number of right border pixels per line   :";
				    	    str2=img1.A(256,4);//
				    	    System.out.println(str1+str2);
				    	    
				    	    str1=" Number of top border lines   :";
				    	    str2=img1.A(260,4);//
				    	    System.out.println(str1+str2);
				    	    
				    	    str1="Number of bottom border lines  :";
				    	    str2=img1.A(264,4);//
				    	    System.out.println(str1+str2);
				    	    
				    	    
				    	    str1="Interleaving indicatores  :";
				    	    str2=img1.A(268,4);//
				    	    System.out.println(str1+str2);
				    	    
				    	    str1="Number of physical records per line   :";
				    	    str2=img1.A(272,2);//
				    	    System.out.println(str1+str2);
				    	    
				    	    str1=" Number of physical records per multi-channel line     :";
				    	    str2=img1.A(274,2);//
				    	    System.out.println(str1+str2);
				    	    
				    	    str1=" Number of bytes of prefix data per record  :";
				    	    str2=img1.A(276,4);//
				    	    System.out.println(str1+str2);
				    	    
				    	    str1=" Number of bytes of SAR data (or pixel data)  :";
				    	    str2=img1.A(280,8);//
				    	    System.out.println(str1+str2);
				    	    str1=" Number of bytes of suffix data per record  :";
				    	    str2=img1.A(288,4);//
				    	    System.out.println(str1+str2);
				    	    	    
				    	    str1="Reserved  :";
				    	    str2=img1.A(292,48);//
				    	    System.out.println(str1+str2);
				    	    
				    	    str1="blanks  :";
				    	    str2=img1.A(340,28);//
				    	    System.out.println(str1+str2);
				    	    
				    	    str1="reserved  :";
				    	    str2=img1.A(368,32);//
				    	    System.out.println(str1+str2);
				    	    
				    	    str1="SAR Data format type identifier  :";
				    	    str2=img1.A(400,28);//
				    	    System.out.println(str1+str2);

				    	    str1="SAR Data format type code  :";
				    	    str2=img1.A(428,4);//
				    	    System.out.println(str1+str2);
				    	    
				    	    str1="Number of left fill bits within pixel :";
				    	    str2=img1.A(432,4);//
				    	    System.out.println(str1+str2);
				    	    
				    	    str1="Number of right fill bits within pixel :";
				    	    str2=img1.A(436,4);//
				    	    System.out.println(str1+str2);
				    	    
				    	    str1="Maximum data range of pixel :";
				    	    str2=img1.A(440,8);//
				    	    System.out.println(str1+str2);
				    	    

				    	    
				    	    ////******DATA RECORDS****///
				    	   
				    	    
				    	    index=19612;
				    	  
				    	    
				    	  
					          	int i=1;
					          	int j=0;
					          	short k=0;
					          	int len=0,convtemp=0;
	                 
					       while(i<=2)
					       {
					    	  index=19612*i;
					    	
					    	  str1="----******-----   DATA RECORD "+i+++"  ------******----\n";

					    	str1=str1+" Record sequence number  :";
				            for(int d=3;d>=0;d--)
				            	convtemp|=((int)temp[index++]&0xff)<<8*d;
			   
				    	    System.out.println(str1+convtemp);
				    	    
				    	 
				    	    
				    	    str1="1st Record subtype code";
				 	       str2=img1.B(index,1);
				 	       System.out.println(str1+str2);
				 	   	   	
				 	     	index++;
				 	     
				 	     	str1="Record subtype code";
				 	         str2=img1.B(index,1);
				 	        System.out.println(str1+str2);
		 		          	
				 	        	 
				 	        		index++;
				 	        		str1="2nd Record subtype code";
				 		            str2=img1.B(index,1);
				 		            
				 		            System.out.println(str1+str2);
				 		          	
				 		       	index++;
				 		            
				 		            str1="3rd Record subtype code";
				 		            str2=img1.B(index,1);
				 		            System.out.println(str1+str2);
				 		          	
				 		          	index++;
				 		          	str1="  Length of this record     (nominal)";

				 		            len=0;
						            for(int d=3;d>=0;d--)
						            {
						            	len|=((int)temp[index++]&0xff)<<8*d;
		 				            }
						            System.out.println(str1+len);
							           
				 		          	
				 		          	System.out.println("index="+index);
			                          
				 		          	str1="";
				 		  
			                          
			                     for(j=0;j<2;j++)
				 		         { 		          		  
				 		          	   str1=str1+"\tSample "+(j+1)+":";
				 		          	   
				 		          	   k=0;
				 		          	   
				 		          	   //System.out.println(Integer.toBinaryString(temp[index])+"   "+Integer.toBinaryString(temp[index+1])+"character format="+((byte)temp[index]) +"   "+((byte)temp[index+1])+"  "+(int)temp[index+2]+"  "+(int)temp[index+3]);
				 		          	   

				 		          	   /*------ Logic to read pixel -------*/
				 		          	   
				 		          //1. 2 byte real part in big endian format  
				 		           	   
				 		          	   k=(short) ((short)temp[index]&0xff);
				 		          	   k<<=8; 
				 		         	   k|=((short)temp[index+1]&0xff);
					 		           
				 		         	   System.out.println("Real k= "+k);
	
				 		          //2. 2 byte imaginary part in big endian format  
						 		          
				 		         	   k= (short) temp[index+2];
					 		           k&=0x0ff;
					 		           k<<=8; 
					 		           k|=((short)temp[index+3]&0xff);	

						 		       System.out.println("Imag k= "+k);
						 		        	
						 		          		index+=4;
					 		          		

					 		          	 
					 		          	System.out.println("index="+index);       		
				 		          	  }//for
				 		          
				 		          	 }
fin.close();
				 		          	}

			

	}

			
			
		
		


