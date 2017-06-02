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
		//System.out.println("prev: "+prev);
		//System.out.println("i: "+i);
		
		//handling the last character
		arr[pos]=arr[prev];
		pos++;
		if(k>1){
			arr[pos]= (char)(k+48);
			pos++;
		}
		
		//removing unwanted characters at the end
		StringBuilder sb = new StringBuilder();
		for(int j=0;j<pos;j++){
			sb.append(arr[j]);
		}
	return sb.toString();

	}
