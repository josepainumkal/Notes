
public class SelectionSort {
	
	void swap(int a[], int i, int j){
		int temp = a[i];
		a[i] = a[j];
		a[j] = temp;
		
	}
	void selSort(int a[]){
		int n = a.length;
		int start = 0, end = n;
		int min, min_pos;
		
		while(start<end){
			min = a[start];
			min_pos = start;
			
			for(int i=start;i<end;i++){
				if(a[i]<min){
					min=a[i];
					min_pos=i;	
				}
			}
			swap(a,start,min_pos);
			start++;
		}		
	}
	
	public static void main(String args[]){
		SelectionSort selsort= new SelectionSort();
		int a[] = {11,10,9,8,7,6,5,4,3,2,1,3,4,5,6,7,8,99,9};

		System.out.println("Before Sorting: ");
		for(int i: a){
			System.out.print(i+" ");
		}

		selsort.selSort(a);

        System.out.println("\nAfter Sorting: ");
		for(int i: a){
			System.out.print(i+" ");
		}
	}

}
