
public class LinkStrand implements IDnaStrand {

	private class Node {
		String info;
		Node next;
		public Node(String s) {
			info = s;
			next = null;
		}
	}
	private Node myFirst,myLast;
	private long mySize;
	private int myAppends;
	private int count;
	private int dex;
	private Node list;
	
	public LinkStrand(){
		this("");
	}
	public LinkStrand(String s){
		initialize(s);
	}
	public String toString(){
		StringBuilder answer = new StringBuilder();
		Node curr = myFirst;
		answer.append(curr.info);
		while(curr.next!=null){
			
			curr=curr.next;
			answer.append(curr.info);
		}
		return answer.toString();
	}

	@Override
	public IDnaStrand cutAndSplice(String enzyme, String splicee) {
		int pos = 0;
		int start = 0;
		String search = myFirst.info;
		boolean first = true;
		IDnaStrand ret = null;

		// code identical to StringStrand, both String and StringBuilder
		// support .substring and .indexOf
		if (myFirst.next != null) throw new UnsupportedOperationException();
		while ((pos = search.indexOf(enzyme, pos)) >= 0) {
			if (first) {
				ret = getInstance(search.substring(start, pos));
				first = false;
			} 
			else {
				ret.append(search.substring(start, pos));

			}
			start = pos + enzyme.length();
			ret.append(splicee);
			pos++;
		}

		if (start < search.length()) {
			// NOTE: This is an important special case! If the enzyme
			// is never found, return an empty String.
			if (ret == null) {
				ret = getInstance("");
			} else {
				ret.append(search.substring(start));
			}
		}
		return ret;
	}
	@Override
	public long size() {
		// TODO Auto-generated method stub
		return mySize;
	}

	@Override
	public void initialize(String source) {
		// TODO Auto-generated method stub
		Node newNode = new Node(source);
		myFirst = newNode;
		myLast = newNode;	
		mySize = source.length();
		myAppends = 0;
		count =0;
		dex =0;
		list = myFirst;
		
	}

	@Override
	public IDnaStrand getInstance(String source) {
		// TODO Auto-generated method stub
		return new LinkStrand(source);
	}

	@Override
	public IDnaStrand append(String dna) {
		
		Node newNode = new Node(dna);
		myLast.next = newNode;
		myLast = myLast.next;
		myAppends +=1;
		mySize = mySize + dna.length();
		return this;
	}
	

	@Override

	public IDnaStrand reverse() {
		LinkStrand t = new LinkStrand();
		Node first = myFirst;
		StringBuilder copy = new StringBuilder(first.info);
		copy=copy.reverse();
		String reverse = copy.toString();
		Node answer = new Node(reverse);
		t.initialize(answer.info);
		
	
		
		Node list = first;
		while(list.next!= null){
			list=list.next;
			StringBuilder copyIn = new StringBuilder(list.info);//copyInsideLoop
			copyIn=copyIn.reverse();
			Node temp = new Node(copyIn.toString());//creates new node of reversed string that points to reversed linklist
			t.mySize+=copyIn.toString().length();
			t.myLast= list;
			temp.next = t.myFirst;
			t.myFirst = temp;
			
			
		}
		
		return t ;
	}

	@Override
	public String getStats() {
		return String.format("# appends = %d", myAppends);
	}

	@Override
	public char charAt(int index) {
		if(index>mySize-1) throw new IndexOutOfBoundsException();
		if(count>index){
			count =0; 
			list =myFirst; 
			dex =0;
		}
		while(count!= index){
			dex++;
			count++;
			if(dex>= list.info.length()){
				//
				list=list.next;
				dex=0;
			}
			
		}
		
		return list.info.charAt(dex);
		
	}
	/*public static String testReverse(){
		String for1 = "atcg";
		String for2 = "cata";
		String for3 = "tagg";
		String rev1 = "gcta";
		String rev2 = "atac";
		String rev3 = "ggat";
		LinkStrand t = new LinkStrand();
		t.initialize(for1);
		t.append(for2);
		t.append(for3);
		LinkStrand x = new LinkStrand();
		x.initialize(rev3);
		x.append(rev2);
		x.append(rev1);
		if(t.reverse().toString().equals(x.toString())){
			return "TRUE";
			
	}
		else{return x.reverse().toString()+"           " + t.toString();}
		
		
	}
	public static void main(String[] args){
		System.out.println(testReverse());
	}*/
}
