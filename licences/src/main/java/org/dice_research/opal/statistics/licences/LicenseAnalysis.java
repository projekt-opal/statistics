package org.dice_research.opal.statistics.licences;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.jena.ext.com.google.common.io.Files;

public class LicenseAnalysis {

	private final static int TYPE_DATASET = 0;
	private final static int TYPE_LICENSE = 1;
	private final static int TYPE_CATALOG = 2;

	private static int type = 0;
	private static String dataset;
	private static int license;
	private static int catalog;

	/**
	 * <catalog, <licence, counter>
	 */
	private static Map<Integer, Map<Integer, Integer>> resultsMap = new HashMap<Integer, Map<Integer, Integer>>();

	public static void main(String[] args) throws Exception {
		new LicenseAnalysis().analyse();
	}

	private void analyse() throws IOException {
		List<String> licenses = FileUtils.readLines(LicenseExtraction.FILE_LICENCES, StandardCharsets.UTF_8);
		List<String> catalogs = FileUtils.readLines(LicenseExtraction.FILE_CATALOGS, StandardCharsets.UTF_8);
		Files.asCharSource(LicenseExtraction.FILE_DATASETS, StandardCharsets.UTF_8)
				.forEachLine(LicenseAnalysis::readLine);

		int results = 0;
		for (Entry<Integer, Map<Integer, Integer>> resultEntry : resultsMap.entrySet()) {
			String catalogTitle = catalogs.get(resultEntry.getKey());
			for (Entry<Integer, Integer> catalogEntry : resultEntry.getValue().entrySet()) {
				String licenseTitle = licenses.get(catalogEntry.getKey());
				Integer number = catalogEntry.getValue();
				System.out.println(catalogTitle + " " + licenseTitle + " " + number);
				results += number;
			}
		}

		System.out.println(results);
	}

	private static void readLine(String result) {
		if (type == TYPE_DATASET) {
			dataset = result;
		} else if (type == TYPE_LICENSE) {
			license = Integer.valueOf(result);
		} else if (type == TYPE_CATALOG) {
			catalog = Integer.valueOf(result);
			putData();
		}
		type = (type + 1) % 3;
	}

	private static void putData() {
		if (!resultsMap.containsKey(catalog)) {
			resultsMap.put(catalog, new HashMap<Integer, Integer>());
		}

		Map<Integer, Integer> catalogMap = resultsMap.get(catalog);
		if (catalogMap.containsKey(license)) {
			catalogMap.put(license, catalogMap.get(license) + 1);
		} else {
			catalogMap.put(license, 1);
		}
	}

}