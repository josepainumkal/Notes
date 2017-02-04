class MSort{
	void merge_sort(int[] a){

	    int nA = a.length;
	    int mid = a.length/2;

        int left[] = new int[mid];
        int right[] = new int[nA - mid];

        if(nA<2){
           return;
        }

		for(int i=0;i<mid; i++){
            left[i] =  a[i];
		}

		for(int j=mid,i=0; j<nA; j++,i++){
		   right[i]=a[j];
		}

       merge_sort(left);
       merge_sort(right);
       merge(left,right,a);
	}


	void merge(int[] left, int[] right, int[] a){
        int nA = a.length;
        int nLeft = left.length;
        int nRight = right.length;
        int i=0,j=0,k=0;

        while(i<nLeft && j<nRight){
        	if(left[i] < right[j]){
        		a[k] = left[i];
        		k++;
        		i++;
        	}else{
        		a[k] = right[j];
        		k++;
        		j++;
        	}   
        }
        while(i<nLeft){
          	a[k] = left[i];
        	k++;
        	i++;
        }
        while(j<nRight){
          	a[k] = right[j];
        	k++;
        	j++;
        }
	}

	public static void main(String args[]){
		MSort msort= new MSort();
		int a[] = {2,4,1,6,5,3,8,7,0,3,34,5,9};

		System.out.println("Before Sorting: ");
		for(int i: a){
			System.out.print(i+" ");
		}

		msort.merge_sort(a);

        System.out.println("\nAfter Sorting: ");
		for(int i: a){
			System.out.print(i+" ");
		}
	}
}
