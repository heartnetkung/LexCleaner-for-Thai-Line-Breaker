package hnk.lib.tlb.preprocessor;

import hnk.lib.tlb.engine.ImmutableContainmentSearcher;
import hnk.lib.tlb.engine.ImmutableInverseTrie;
import hnk.lib.tlb.util.FileUtil;
import hnk.lib.tlb.util.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

	/**
	 * Read words from inFile, delete words existing in deletionListFile, and
	 * print it to outFile
	 * 
	 * @param inFile
	 *            word list, one per line
	 * @param outFile
	 *            word list, one per line
	 * @param deletionListFile
	 *            word list, one per line
	 * @throws IOException
	 *             file missing blah blah
	 * @throws NullPointerException
	 *             if any argument is null
	 */
	public static void delete(String inFile, String outFile,
			String deletionListFile) throws IOException, NullPointerException {
		if (inFile == null || outFile == null || deletionListFile == null)
			throw new NullPointerException();
		List<String> all = Utils.longStringToList(FileUtil.readPlainText(
				new File(inFile), FileUtil.ms874));

		Set<String> delete = new HashSet<>(Utils.longStringToList(FileUtil
				.readPlainText(new File(deletionListFile), FileUtil.ms874)));

		StringBuilder out = new StringBuilder();

		int deleteCount = 0;
		for (String s : all) {
			if (!delete.contains(s))
				out.append(s).append('\n');
			else
				deleteCount++;
		}
		if (out.length() > 0)
			out.deleteCharAt(out.length() - 1);

		System.out.println(delete);
		System.out.println(deleteCount + " words deleted");

		FileUtil.writePlainText(new File(outFile), out.toString(),
				FileUtil.ms874);
	}

	/**
	 * Read words from inFile, make sure they're unique, and print it to outFile
	 * 
	 * @param inFile
	 *            word list, one per line
	 * @param outFile
	 *            word list, one per line
	 * @throws IOException
	 *             file missing blah blah
	 * @throws NullPointerException
	 *             if any argument is null
	 */
	public static void unique(String inFile, String outFile)
			throws IOException, NullPointerException {
		if (inFile == null || outFile == null)
			throw new NullPointerException();
		List<String> allList = Utils.longStringToList(FileUtil.readPlainText(
				new File(inFile), FileUtil.ms874)), deleting = new ArrayList<String>();
		Set<String> allSet = new HashSet<>();

		StringBuilder out = new StringBuilder();

		for (String s : allList) {
			if (allSet.add(s))
				out.append(s).append('\n');
			else
				deleting.add(s);
		}
		if (out.length() > 0)
			out.deleteCharAt(out.length() - 1);

		System.out.println(deleting);
		System.out.println(deleting.size() + " words deleted");

		FileUtil.writePlainText(new File(outFile), out.toString(),
				FileUtil.ms874);
	}

	public static void createTrieCache(String dictFile) throws Exception {
		ImmutableInverseTrie ans = ImmutableInverseTrie.fromWordList(FileUtil
				.readPlainText(new File(dictFile), FileUtil.ms874));
		FileUtil.writePlainText(new File("trie_cache.txt"), ans.serialize(),
				FileUtil.ms874);
	}

	public static void createSearcherCache(String dictFile) throws Exception {
		ImmutableContainmentSearcher ans = ImmutableContainmentSearcher
				.build(FileUtil.readPlainText(new File(dictFile),
						FileUtil.ms874));
		FileUtil.writePlainText(new File("whole_word_cache.txt"),
				ans.serialize(), FileUtil.ms874);

	}

	/**
	 * run this in pc only
	 */
	public static void main(String[] args) throws Exception {
		// choose operations to be done

		// delete("lexitron.txt", "lexout.txt", "delete4.txt");
		// unique("lexitron.txt", "lexout.txt");
		createTrieCache("lexitron.txt");
		createSearcherCache("lexitron.txt");
	}

}
