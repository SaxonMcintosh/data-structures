/**
 * The obscured code for use by another student.
 *
 * @author Saxon McIntosh
 * @version April 1, 2020
 */
import java.util.*;
import java.io.*;

public class CrimeSort {
    public static HashMap<String, Integer> map = new HashMap<>();

    public static void main(String args[])
	throws FileNotFoundException {
	map.put("larcenyCompare", 0);
	map.put("larcenyExchange", 0);
	map.put("arsonCompare", 0);
	map.put("arsonExchange", 0);
	map.put("theftCompare", 0);
	map.put("theftExchange", 0);

        /* So the sorting methods you have to work with are:
	 * larcenySort()
	 * arsonSort()
	 * theftSort()
	 *
	 * All three accept an array of integers as an argument, and
	 * produce a sorted array. If you should want to print the
	 * the arrays, you can use the void printer() function which
	 * also accpets just an array as an argument. */

	PrintWriter writer = new PrintWriter(new File("stats.csv"));
	writer.write("N,lComparisons,lExchanges,lTime(ns),aComparisons,aExchanges,aTime(ns),tComparisons,tExchanges,tTime(ns)\n");
	long start;
	long end;
	long time;
	int n = 50;
	StringBuilder sb = new StringBuilder();

	while (n <= 1000) {
	    int arr[] = arrayBuilder(n);
	    int arr2[] = arr.clone();
	    int arr3[] = arr.clone();
	    sb.append(n);
	    sb.append(",");
	    
	    start = System.nanoTime();
	    larcenySort(arr);
	    end = System.nanoTime();
	    time = end - start;
	    sb.append(map.get("larcenyCompare"));
	    sb.append(",");
	    sb.append(map.get("larcenyExchange"));
	    sb.append(",");
	    sb.append(time / 1000);
	    sb.append(",");

	    start = System.nanoTime();
	    arsonSort(arr2);
	    end = System.nanoTime();
	    time = end - start;
	    sb.append(map.get("arsonCompare"));
	    sb.append(",");
	    sb.append(map.get("arsonExchange"));
	    sb.append(",");
	    sb.append(time / 1000);
	    sb.append(",");

	    start = System.nanoTime();
	    theftSort(arr3);
	    end = System.nanoTime();
	    time = end - start;
	    sb.append(map.get("theftCompare"));
	    sb.append(",");
	    sb.append(map.get("theftExchange"));
	    sb.append(",");
	    sb.append(time / 1000);
	    sb.append("\n");

	    writer.write(sb.toString());
	    sb.setLength(0);
	    map.replace("larcenyCompare", 0);
	    map.replace("larcenyExchange", 0);
	    map.replace("arsonCompare", 0);
	    map.replace("arsonExchange", 0);
	    map.replace("theftCompare", 0);
	    map.replace("theftExchange", 0);
	    
	    System.out.println("n = " + n);

	    n += 50;
	}
	writer.close();
    }
    
    /**
     * A means of randomly building integer arrays.
     *
     * @param size the number of elements in the array
     *
     * @return the randomized array
     */
    public static int[] arrayBuilder(int size) {
	int arr[] = new int[size];
	for (int i = 0; i < size; i++) {
	    arr[i] = (int)(Math.random() * 1000000) + 1;
	}
	return arr;
    }
    
    /**
     * A helper method to print each array.
     *
     * @param arr the array to be printed
     */
    public static void printer(int arr[]) {
	for (int i = 0; i < arr.length; i++) {
	    System.out.print(arr[i] + " ");
	}
	System.out.println();
    }
    
    public static int[] theftSort(int arr[]) {
	int index;
	int next;

	for (int i = 1; i < arr.length; i++) {
	    index = i;
	    next = arr[index];

	    while (index > 0 && arr[index - 1] > next) {
		arr[index] = arr[index -1];
		index--;

		map.replace("theftExchange",
			    map.get("theftExchange") + 1);
		map.replace("theftCompare",
			    map.get("theftCompare") + 1);
	    }

	    arr[index] = next;
	    
	    map.replace("theftExchange", map.get("theftExchange") + 1);
	    // I augmented this after I sent it.
	    map.replace("theftCompare", map.get("theftCompare") + 1);
	} 
	
	return arr;
    }

    public static int[] arsonSort(int arr[]) {
	return arsonSort(arr, 0, arr.length - 1);
    }

    private static int[] arsonSort(int arr[], int lo, int hi) {
	int temp;
	int pivot = arr[hi];

	int i = lo;
	int j = hi - 1;

	while (j > i) {
	    while (i < j && arr[i] <= pivot) {
		i++;

		map.replace("arsonCompare", map.get("arsonCompare") + 1);
	    }
	    while (i < j && arr[j] > pivot) {
		j--;

		map.replace("arsonCompare", map.get("arsonCompare") + 1);
	    }
	    
	    temp = arr[i];
	    arr[i] = arr[j];
	    arr[j] = temp;

	    map.replace("arsonExchange", map.get("arsonExchange") + 1);
	}
	int leftHi = i - 1;
	
	if (arr[i] > pivot) {
	    temp = arr[i];
	    arr[i] = arr[hi];
	    arr[hi] = temp;

	    map.replace("arsonExchange", map.get("arsonExchange") + 1);
	} else {
	    leftHi = hi - 1;
	}
	map.replace("arsonCompare", map.get("arsonCompare") + 1);

	if (i - lo > 1) {
	    arsonSort(arr, lo, leftHi);
	}

	if (hi - i > 1) {
	    arsonSort(arr, i + 1, hi);
	}
	
	return arr;
    }

    public static int[] larcenySort(int arr[]) {
	if (arr.length > 1) {
	    int mid = arr.length / 2;
	    int left[] = new int[mid];
	    int right[] = new int[arr.length - mid];

	    for (int i = 0; i < mid; i++) {
		left[i] = arr[i];
	    }
	    for (int i = mid; i < arr.length; i++) {
		right[i - mid] = arr[i];
	    }
	    
	    larcenySort(left);
	    larcenySort(right);
	    
	    int i = 0;
	    int l = 0;
	    int r = 0;
	    while (l < left.length && r < right.length) {
		if (left[l] <= right[r]) {
		    arr[i] = left[l];
		    l++;
		} else {
		    arr[i] = right[r];
		    r++;
		}
		i++;

		map.replace("larcenyExchange",
			    map.get("larcenyExchange") + 1);
		map.replace("larcenyCompare",
			    map.get("larcenyCompare") + 1);
	    }
	    while (l < left.length) {
		arr[i] = left[l];
		l++;
		i++;

		map.replace("larcenyExchange",
			    map.get("larcenyExchange") + 1);
	    }
	    while (r < right.length) {
		arr[i] = right[r];
		r++;
		i++;

		map.replace("larcenyExchange",
			    map.get("larcenyExchange") + 1);
	    }
	}
	
	return arr;
    }
}
