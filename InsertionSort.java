
public class InsertionSort {
	void insertionSort(int a[]){
		int n = a.length;
		int temp, hole=0;
		
		for(int i=1;i<n;i++){
			temp = a[i];
			hole = i;
			
			while(hole>0 && a[hole-1]>temp){
				a[hole]=a[hole-1];
				hole--;
			}
			a[hole]=temp;
		}
					
			
	
	}
	
	public static void main(String args[]){
		InsertionSort insort= new InsertionSort();
		int a[] = {9,51,7,31,2,1,0,4,23,5,6};

		System.out.println("Before Sorting: ");
		for(int i: a){
			System.out.print(i+" ");
		}

		insort.insertionSort(a);

        System.out.println("\nAfter Sorting: ");
		for(int i: a){
			System.out.print(i+" ");
		}
	}

}
