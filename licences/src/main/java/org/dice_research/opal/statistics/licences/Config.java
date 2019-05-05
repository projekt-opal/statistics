package org.dice_research.opal.statistics.licences;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * Configuration.
 * 
 * To configure the project, copy {@link Config#PROPERTIES_FILE} to
 * {@link Config#PROPERTIES_FILE_PRIVATE}.
 *
 * @author Adrian Wilke
 */
public abstract class Config {

	// Config keys
	
	public static final String SPARQL_ENDPOINT_EUROPEANDATAPORTAL = "sparql.endpoint.europeandataportal";
	public static final String SPARQL_ENDPOINT_GOVDATA = "sparql.endpoint.govdata";
	public static final String SPARQL_ENDPOINT_MCLOUD = "sparql.endpoint.mcloud";

	// Internal
	
	private final static String PROPERTIES_FILE = "src/main/resources/config.properties";
	private final static String PROPERTIES_FILE_PRIVATE = "src/main/resources/private.properties";
	private final static Properties properties = new Properties();

	public static String get(String key) {
		try {
			if (new File(PROPERTIES_FILE_PRIVATE).canRead()) {
				properties.load(new FileInputStream(PROPERTIES_FILE_PRIVATE));
			} else {
				properties.load(new FileInputStream(PROPERTIES_FILE));
			}
		} catch (Throwable t) {
			System.err.println(t + " " + Config.class.getName());
		}
		return properties.getProperty(key);
	}
}