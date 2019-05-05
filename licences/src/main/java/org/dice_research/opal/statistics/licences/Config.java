package org.dice_research.opal.statistics.licences;

import java.io.FileInputStream;
import java.util.Properties;

public abstract class Config {

	public static final String SPARQL_ENDPOINT_EUROPEANDATAPORTAL = "sparql.endpoint.europeandataportal";
	public static final String SPARQL_ENDPOINT_GOVDATA = "sparql.endpoint.govdata";
	public static final String SPARQL_ENDPOINT_MCLOUD = "sparql.endpoint.mcloud";

	private final static String PROPERTIES_FILE = "src/main/resources/config.properties";
	private final static Properties properties = new Properties();

	public static String get(String key) {
		try {
			properties.load(new FileInputStream(PROPERTIES_FILE));
		} catch (Throwable t) {
			System.err.println(t + " " + Config.class.getName());
		}
		return properties.getProperty(key);
	}
}