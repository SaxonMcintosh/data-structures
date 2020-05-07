/**
 * Here are my functions and their original names, for clarity.
 *
 * @author Saxon McIntosh
 * @version April 1, 2020
 */

import java.util.*;

public class Sorting {
    public static HashMap<String, Integer> map = new HashMap<>();

    public static void main(String args[]) {
	map.put("iCompare", 0);
	map.put("iExchange", 0);
	map.put("qCompare", 0);
	map.put("qExchange", 0);
	map.put("mCompare", 0);
	map.put("mExchange", 0);

	Random rand = new Random();
	int arr[] = new int[1000];
	for (int i = 0; i < 1000; i++) {
	    arr[i] = rand.nextInt(10);
	}
	
	int arr2[] = arr.clone();
	int arr3[] = arr.clone();

	System.out.println("Original:");
	printer(arr);

	insertionSort(arr);
	System.out.println("iSorted:");
	printer(arr);

        quickSort(arr2, 0, arr2.length - 1);
	System.out.println("qSorted:");
	printer(arr2);
	
	mergeSort(arr3);
	System.out.println("mSorted:");
	printer(arr3);

	System.out.println(map);
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
    
    /**
     * insertionSort() works similarly to the way the a cardplayer would
     * sort their hand (I supposed minus exchanging every pair on the
     * way down.
     *
     * @param arr the array to be sorted
     *
     * @return the sorted array
     */
    public static int[] insertionSort(int arr[]) {
	int index;
	int next;

	for (int i = 1; i < arr.length; i++) {
	    index = i;
	    next = arr[index];

	    while (index > 0 && arr[index - 1] > next) {
		arr[index] = arr[index -1];
		index--;

		map.replace("iExchange", map.get("iExchange") + 1);
		map.replace("iCompare", map.get("iCompare") + 1);
	    }

	    arr[index] = next;
	    map.replace("iExchange", map.get("iExchange") + 1);
	    /* I had actually forgotten to add this comparison in to the file
	    * that I sent to Spencer, which would account for the comparison
	    * that ultimately breaks the while loop. */
	    map.replace("iCompare", map.get("iCompare") + 1);
	} 
	
	return arr;
    }

    /**
     * quickSort() is a divide and conquer method that utilizes a pivot
     * value to split the array recursively into a series of smaller
     * arrays, all to be sorted.
     *
     * @param arr the array to be sorted
     * @param lo the lowest element in the subarray to sort
     * @param hi the highest element in the subarray to sort
     *
     * @return the sorted array
     */
    public static int[] quickSort(int arr[], int lo, int hi) {
	int temp;
	int pivot = arr[hi];

	int i = lo;
	int j = hi - 1;

	while (j > i) {
	    while (i < j && arr[i] <= pivot) {
		i++;

		map.replace("qCompare", map.get("qCompare") + 1);
	    }
	    while (i < j && arr[j] > pivot) {
		j--;

		map.replace("qCompare", map.get("qCompare") + 1);
	    }
	    
	    temp = arr[i];
	    arr[i] = arr[j];
	    arr[j] = temp;

	    map.replace("qExchange", map.get("qExchange") + 1);
	}

	// leftHi helps by handling the cases where all of the elements
	// in the left array are less than or equal to the pivot.
	int leftHi = i - 1;
	
	if (arr[i] > pivot) {
	    temp = arr[i];
	    arr[i] = arr[hi];
	    arr[hi] = temp;

	    map.replace("qExchange", map.get("qExchange") + 1);
	} else {
	    /* Since there are an indeterminate number of values to the
	     * right of 'i' when arr[i] == pivot, the hi value of the
	     * left subarry is now one less than the old hi value */ 
	    leftHi = hi - 1;
	}
	map.replace("qCompare", map.get("qCompare") + 1);

	if (i - lo > 1) {
	    quickSort(arr, lo, leftHi);
	}

	if (hi - i > 1) {
	    quickSort(arr, i + 1, hi);
	}
	
	return arr;
    }

    /**
     * mergeSort is another divide and conquer algorithm that works by
     * splitting the array in half recursively, sorting each pair, then
     * merging each sorted array with the next.
     *
     * @param arr the array to be sorted
     *
     * @return the sorted array
     */
    public static int[] mergeSort(int arr[]) {
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
	    
	    mergeSort(left);
	    mergeSort(right);
	    
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

		map.replace("mExchange", map.get("mExchange") + 1);
		map.replace("mCompare", map.get("mCompare") + 1);
	    }
	    while (l < left.length) {
		arr[i] = left[l];
		l++;
		i++;

		map.replace("mExchange", map.get("mExchange") + 1);
	    }
	    while (r < right.length) {
		arr[i] = right[r];
		r++;
		i++;

		map.replace("mExchange", map.get("mExchange") + 1);
	    }
	}
	
	return arr;
    }
}
