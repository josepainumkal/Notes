
public class QuickSort {
	
	void swap(int arr[], int a, int b ){
		int temp = arr[a];
		arr[a] = arr[b];
		arr[b] = temp;
	}
	
	int partition(int arr[], int start, int end){
		int l = start;
		int h = end-1;		
		if(start<end){ // put this, if not arr[-1], error
			int pivot = arr[end];
			
			while(l<=h){ // I didn't put '=', if = is not given, then l will be equal to h at the time of loop exit, then swap with pivot becomes errorneous. l should  cross h, so give =
				if(arr[l]<pivot){
					l++;
				}
				else if(arr[h]>pivot){
	                h--;				
				}
				else{
					swap(arr,l,h);
				}
			}
			swap(arr,l,end);	
		}
		return l;
	}
	
	void quickSort(int arr[], int start, int end){
		if(start<end){ // I didn't put this - if not put this condition, infinite loop is the result. the 2nd quicksort never get executed
			int q = partition(arr, start, end);
			quickSort(arr, start,q-1);
			quickSort(arr,q+1,end);
		}
	}
	
	public static void main(String args[]){
		int arr[] = {4,12,10,8,5,2,11,7,3};
		QuickSort qsort = new QuickSort();
		qsort.quickSort(arr,0,arr.length-1);
		
		for(int i=0;i<arr.length;i++){
			System.out.print(arr[i]+" ");
		}
		
	}

}
