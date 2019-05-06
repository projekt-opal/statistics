package org.dice_research.opal.statistics.licences;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;

/**
 * SPARQL query helper.
 *
 * @author Adrian Wilke
 */
public abstract class Queries {

	/**
	 * Gets query from file.
	 */
	public static String getQuery(String id) throws IOException {
		File file = new File("src/main/resources/queries/" + id + ".txt");
		return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
	}

}