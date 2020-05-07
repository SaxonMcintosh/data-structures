import java.util.*;
import java.io.*;
/**
 *
 * @author snott
 */
public class CSIT {
    
    static final int max = 1000;
    static final int increment = 50;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try(PrintWriter writer=new PrintWriter(new File("output.csv"))){
            StringBuilder sb = new StringBuilder();
            sb.append("N");
            sb.append(",");
            sb.append("Sort 1 Comparisons");
            sb.append(",");
            sb.append("Sort 1 Exchanges");
            sb.append(",");
            sb.append("Sort 1 Time(ns)");
            sb.append(",");
            sb.append("Sort 2 Comparisons");
            sb.append(",");
            sb.append("Sort 2 Exchanges");
            sb.append(",");
            sb.append("Sort 2 Time(ns)");
            sb.append(",");
            sb.append("Sort 3 Comparisons");
            sb.append(",");
            sb.append("Sort 3 Exchanges");
            sb.append(",");
            sb.append("Sort 3 Time(ns)");
            sb.append("\n");
		
            writer.write(sb.toString());
            long timeStart;
            long timeEnd;
            int n = 0;
            
            while(n<max){
                Sort1 sort1=new Sort1();
                Sort2 sort2=new Sort2();
                Sort3 sort3=new Sort3();
                
                int array[] = ArrayGen(n);
			int array1[]= Arrays.copyOf(array, array.length);
			int array2[]= Arrays.copyOf(array, array.length);
			int array3[]= Arrays.copyOf(array, array.length);
			
			timeStart = System.nanoTime();
			sort1.sort(array1);
			timeEnd = System.nanoTime();
			long s1Time = (timeEnd-timeStart);
			int s1comp = sort1.total.getComp();
			int s1ex = sort1.total.getExch();
			
			
			timeStart = System.nanoTime();
			sort2.sort(array2);
			timeEnd = System.nanoTime();
			long s2Time = (timeEnd-timeStart);
			int s2comp = sort2.total.getComp();
			int s2ex = sort2.total.getExch();
			
			
			timeStart = System.nanoTime();
			sort3.sort(array3);
			timeEnd = System.nanoTime();
			long s3Time = (timeEnd-timeStart);
			int s3comp = sort3.total.getComp();
			int s3ex = sort3.total.getExch();
			
			
			sb = new StringBuilder();
			sb.append(n);
			sb.append(",");
			sb.append(s1comp);
			sb.append(",");
			sb.append(s1ex);
			sb.append(",");
			sb.append(s1Time/1000);
			sb.append(",");
			sb.append(s2comp);
			sb.append(",");
			sb.append(s2ex);
			sb.append(",");
			sb.append(s2Time/1000);
			sb.append(",");
			sb.append(s3comp);
			sb.append(",");
			sb.append(s3ex);
			sb.append(",");
			sb.append(s3Time/1000);
			sb.append("\n");
			
			writer.write(sb.toString());
			System.out.println("N = " + n);
			
			n += increment;
		}
            }
        catch(FileNotFoundException e) {
            }
        }
    
    
    public static int[] ArrayGen(int size){
		int array[] = new int[size];
		for(int i = 0; i < size; i++){
			array[i] = (int)(Math.random()*1000000)+1;
		}
		return array;
	}
}
