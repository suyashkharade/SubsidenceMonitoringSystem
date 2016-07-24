import java.util.ArrayList;


public class toarra {

	public static void main(String args[])
	{
		ArrayList <String>names=new ArrayList<String>();
		names.add("aniket");
		String[] list;
		list= names.toArray(new String[names.size()]);
		System.out.print("aaf"+list[0]);	
	
	}
}
