package org.dice_research.opal.statistics.licences;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.io.FileUtils;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

/**
 * OPAL licence statistics.
 * 
 * Configuration and execution in {@link #main(String[])} method.
 *
 * @author Adrian Wilke
 */
public class LicenseExtraction extends QueryExecution {

	public final static File FILE_CATALOGS = new File("catalogs.txt");
	public final static File FILE_DATASETS = new File("datasets.txt");
	public final static File FILE_LICENCES = new File("licences.txt");
	public final static File FILE_META = new File("meta.txt");

	public final static String PREFIX = "http://projekt-opal.de/dataset/";
	public final static int PREFIX_LENGTH = PREFIX.length();

	private final static long TIME = System.currentTimeMillis();
	private final static int LIMIT = 10 * 1000;

	private static List<String> licenses = new LinkedList<String>();
	private static List<String> catalogs = new LinkedList<String>();

	private static int resultCounter = 0;
	private static boolean run = true;

	private static BufferedWriter writer;

	static {
		try {
			writer = new BufferedWriter(new FileWriter(FILE_DATASETS, true));
		} catch (IOException e) {
			System.err.println(e);
			System.exit(1);
		}
	}

	/**
	 * Main entry point.
	 */
	public static void main(String[] args) throws Exception {

		// Configuration
		String sparqlEndpoint = Config.get(Config.SPARQL_ENDPOINT_OPAL);
		boolean isFusekiEndpoint = true;
		Consumer<ResultSet> consumer = LicenseExtraction::writeToFile;
		int queryType = SELECT;
		String queryString = Queries.getQuery("extract-licenses");
		int offset = 0;

		// Execution
		while (run) {
			execute(sparqlEndpoint, isFusekiEndpoint, consumer, queryType,
					queryString.replace("LIMIT 10", "LIMIT " + LIMIT).replace("OFFSET 0", "OFFSET " + offset));
			offset += LIMIT;
		}
		writer.close();

		// Lists
		FileUtils.writeLines(FILE_LICENCES, licenses);
		FileUtils.writeLines(FILE_CATALOGS, catalogs);

		// Meta
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("Date: ");
		stringBuffer.append(TIME);
		stringBuffer.append(System.lineSeparator());
		stringBuffer.append("Dataset prefix: ");
		stringBuffer.append(PREFIX);
		stringBuffer.append(System.lineSeparator());
		stringBuffer.append("Processed results: ");
		stringBuffer.append(resultCounter);
		stringBuffer.append(System.lineSeparator());
		stringBuffer.append("Runtime: ");
		stringBuffer.append((System.currentTimeMillis() - TIME) / 1000 + " seconds");
		FileUtils.write(FILE_META, stringBuffer, StandardCharsets.UTF_8);

		// User info
		System.out.println(FILE_META.getAbsolutePath());
	}

	/**
	 * Writes result set into file;
	 * 
	 * Consumer<ResultSet>
	 */
	private static void writeToFile(ResultSet resultSet) {
		try {
			String string;

			if (!resultSet.hasNext()) {
				run = false;
			}

			while (resultSet.hasNext()) {
				QuerySolution querySolution = resultSet.next();

				string = querySolution.get("dataset").toString();
				writer.append(string.substring(PREFIX_LENGTH));
				writer.append(System.lineSeparator());

				string = querySolution.get("license").toString();
				if (!licenses.contains(string)) {
					licenses.add(string);
				}
				writer.append("" + licenses.indexOf(string));
				writer.append(System.lineSeparator());

				string = querySolution.get("catalog").toString();
				if (!catalogs.contains(string)) {
					catalogs.add(string);
				}
				writer.append("" + catalogs.indexOf(string));
				writer.append(System.lineSeparator());

				resultCounter++;
			}

			writer.flush();

			System.out.println(resultCounter);
			System.out.println((System.currentTimeMillis() - TIME) / 1000 + " seconds");

		} catch (Exception e) {
			System.err.println(e);
			System.exit(1);
		}
	}

}