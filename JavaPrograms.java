import java.util.Arrays;

public class Solution {
	
	// String Compression
	// input: aaabccd
	// output: a3bc2d
	
	static String compress(String s){
		char[] arr = s.toCharArray();
		int i=1,pos=0,k=1;
		char prev = arr[0];
		
		while(i<arr.length){
			if(arr[i]==prev){
				prev = arr[i];
				i++;
				k++;
			}else{
				arr[pos]=prev;
				pos++;
				if(k>1){
					arr[pos] = (char)(k+48);
					pos++;
				}
				k=1;
				prev = arr[i];
				i++;
			}		
		}
    
		//handling the last character
		int prev_pos = i-1;
		if(arr[prev_pos]==arr[prev_pos-1]){
			arr[pos]=arr[prev_pos];
			pos++;k++;
			if(k>1){
				arr[pos]= (char)(k+48);
				pos++;
			}
		}else{
			arr[pos]=arr[prev_pos];
			pos++;
		}
		
		//removing unwanted characters at the end
		StringBuilder sb = new StringBuilder();
        for(int j=0;j<pos;j++){
        	sb.append(arr[j]);
        }
		return sb.toString();
		
	}
	/* -------------------------------------------------------------------------------------------------------------- */
	public static void main(String args[]){
		String result = compress("3232fsddfsd");
		System.out.println(result);
		
	}

}
