package yagoo.algostruct.sort.bubble;

import java.util.Arrays;

public class BubbleMain {
	
	public static void reverse(String[] arr) {
	    //
		int k = 0;
		if (arr.length < 1) return;
		while (k < (arr.length) / 2) {
			String temp = arr[k]; System.out.println(k + " : " + arr[k]);
			arr[k] = arr[arr.length - 1 - k];
			arr[arr.length - 1 - k] = temp;
			k++;
		}
	}
	
	public static void sort(String[] arr) {
		for (int i = 0; i < arr.length; i++) {
			for (int k = i; k < arr.length; k++) {
				if (arr[i].compareTo(arr[k]) > 0) {
					String temp = arr[k];
					arr[k] = arr[i];
					arr[i] = temp;
				}
			}
		}
	}

	public static void main(String[] args) {
		String[] arr = new String[] {"B",  "D", "A", "C"};
		System.out.println(Arrays.toString(arr));
		BubbleMain.sort(arr);
		System.out.println(Arrays.toString(arr));
		reverse(arr);
		System.out.println(Arrays.toString(arr));
	}

}
