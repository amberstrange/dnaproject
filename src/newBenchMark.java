import java.io.File;
import java.util.*;

/**
 * Code for benchmarking the time taken to simulate cutting
 * and splicing strands of DNA. These benchmark methods are
 * intended to be used in reasoning about tradeoffs in using 
 * a linked list to represent a strand of DNA and to compare
 * this representation with a simple String representation.
 * @author Owen Astrachan
 * @date 2/11/2009
 * 
 * Update: System.nanoTime, Repeated and threaded tests.
 * @contributor Brian Lavallee
 * @date 10 March 2016
 */

import javax.swing.JFileChooser;

public class newBenchMark {
	
	/*
	 * Change these to change the tests to
	 * use a different type
	 */
	private static final String strandType = "StringStrand";
	//private static final String strandType = "LinkStrand";
	//private static final String strandType = "StringBuilderStrand";
	private static String[] EnzymeList = createDNA();
	
	private static final String ENZYME = "ccgtacgatcagg";
	private static final int TRIALS = 2;
	
	private static String mySource;
	public static String [] createDNA(){
		String [] answer = new String[51];
		for(int k=0; k<=1000;k+=20){
			StringBuilder sb = new StringBuilder();
		
		for(int n =1; n<=k; n++){
			sb.append(ENZYME);
			sb.append("g");
		}
			for(int i=0; i<50000-(ENZYME.length()*k); i++){
			sb.append("g");
			
		
		
		}
		answer[(k/20)]= sb.toString();
		}
		return answer;
	}

	/**
	 * Return a string representing the DNA read from the scanner, ignoring any
	 * characters can't be part of DNA and converting all characters to lower
	 * case.
	 * 
	 * @param s is the Scanner read from
	 * @return a string representing the DNA read, characters in the returned
	 *         string are restricted to 'c', 'g', 't', 'a'
	 */

	public static String strandSpliceBenchmark(String enzyme, String splicee, String className)
			throws Exception {

		String dna = mySource;
		IDnaStrand strand;
		try {
			strand = (IDnaStrand) Class.forName(className).newInstance();
			strand.initialize(dna);
			long length = strand.size();
			IDnaStrand recomb = strand.cutAndSplice(enzyme, splicee);
			long length2 = strand.size();
			if (length != length2) {
				System.err.printf("trouble splicing %d strand to %d\n", length, length2);
			}
			long recLength = recomb.size();
			double time = 0;
			for (int i = 0; i < TRIALS; i++) {
				Thread thread = new Thread(() -> {
					strand.cutAndSplice(enzyme, splicee);
				});
				double start = System.nanoTime();
				thread.run();
				thread.join();
				time += (System.nanoTime() - start) / TRIALS / 1e9;
			}
			String ret = String.format("%s:\t%,15d\t%,15d\t%1.3f\t%s", className, splicee.length(), recLength, time,
					recomb.getStats());
			System.out.println();
			return ret;
		} catch (ClassNotFoundException e) {
			return "could not create class " + className;
		}
	}

	public static void main(String[] args)
			throws Exception {

		mySource = EnzymeList[0];

		System.out.printf("dna length = %,d\n", mySource.length());
		System.out.println("cutting at enzyme " + ENZYME);
		System.out.println("-----");
		System.out.printf("Class\t%23s\t%12s\ttime\n", "splicee", "recomb");
		System.out.println("-----");
		for (int i = 0; i<EnzymeList.length; i++) {
			
			mySource = EnzymeList[i];
			String splicee = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
			String results = strandSpliceBenchmark(ENZYME, splicee, strandType);
			System.out.println(results);
			
			}
			
		/*for (int j = 8; j <= 16; j++) {
			mySource = EnzymeList[5];
			StringBuilder b = new StringBuilder("");
			int spSize = (int) Math.pow(2, j);
			for (int k = 0; k < spSize; k++) {
				b.append("c");
			}
			String splicee = b.toString();
			String results = strandSpliceBenchmark(ENZYME, splicee, strandType);
			System.out.println(results);
		}*/
		System.exit(0);
	}

}