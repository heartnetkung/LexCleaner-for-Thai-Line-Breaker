package hnk.lib.tlb.preprocessor;
import hnk.lib.tlb.util.FileUtil;
import hnk.lib.tlb.util.ThaiUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DictFilter {
	/**
	 * run in pc only
	 */
	public static void main(String[] args) throws Exception {
		printCompoundWordWithPrefix(3);

	}

	// assume input file has only unique elements
	static void printCompoundWordWithPrefix(int prefixLength) throws Exception {
		// init stuff
		String wholeFile = FileUtil.readPlainText(new File("lexitron.txt"),
				FileUtil.ms874);
		BufferedReader br = new BufferedReader(new StringReader(wholeFile));
		String line;
		ArrayList<String> allWords = new ArrayList<>();
		VTrie dict = createDict(wholeFile);
		int prefixCount = 0;
		Set<String> compoundWord = new HashSet<>();

		// read all words
		while ((line = br.readLine()) != null)
			allWords.add(line);
		br.close();

		// start operation
		for (String s : allWords) {
			String[] ans = dict.getWords(s);
			if (s.contains("."))
				continue;// no abbreviation
			if (s.length() != prefixLength)
				continue;
			if (ans.length < 2)
				continue;// no prefix
			prefixCount++;
			for (String a : ans) {
				if (a.contains("."))
					continue;
				if (a.length() - s.length() < 2)
					continue;
				if(ThaiUtil.countNonZeroWidth(a, 0, a.length())<=6)
					continue;
				if (dict.hasPrefix(a.substring(s.length()))) {
					if (compoundWord.add(a))
						System.out.println(compoundWord.size() + "  " + a);
				}
			}
		}
		System.out.println("prefix length: " + prefixLength);
		System.out.println("prefix count: " + prefixCount);
		System.out.println("total compound words: " + compoundWord.size());
	}

	private static VTrie createDict(String longString) {
		BufferedReader br = new BufferedReader(new StringReader(longString));
		String line;
		VTrie ans = new VTrie();
		try {
			while ((line = br.readLine()) != null) {
				if (line.trim().length() > 2)
					ans.add(line.trim(), 1);
			}
			br.close();
		} catch (IOException ioe) {
			throw new RuntimeException(
					"io exception occurs even when we use string reader!? wtf!?",
					ioe);
		}
		return ans;
	}
}
