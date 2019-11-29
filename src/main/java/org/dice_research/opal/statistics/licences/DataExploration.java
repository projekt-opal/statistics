package org.dice_research.opal.statistics.licences;

import java.io.IOException;
import java.util.function.Consumer;

import org.apache.jena.query.ResultSet;

/**
 * Data exploration.
 * 
 * Configuration and execution in {@link #main(String[])} method.
 *
 * @author Adrian Wilke
 */
public class DataExploration extends QueryExecution {

	@SuppressWarnings("unused")
	public static void main(String[] args) throws IOException {

		// Default configuration
		String sparqlEndpoint = Config.get(Config.SPARQL_ENDPOINT_OPAL);
		boolean isFusekiEndpoint = true;
		Consumer<ResultSet> consumer = DataExploration::print;
		int queryType = SELECT;
		String queryString = null;

		// Get graphs of endpoint
		if (false) {
			sparqlEndpoint = Config.get(Config.SPARQL_ENDPOINT_GOVDATA);
			queryString = Queries.getQuery("graphs");
		}

		// Get catalogs in graph <http://projekt-opal.de>
		if (false) {
			queryString = Queries.getQuery("catalogs");
		}

		// Get number of datasets in catalogs in graph <http://projekt-opal.de>
		if (false) {
			queryString = Queries.getQuery("datasets-in-catalogs");
		}

		// Get number of distributions in graph <http://projekt-opal.de>
		if (false) {
			queryString = Queries.getQuery("number-of-distributions");
		}

		// Get number of licenses and usages in graph <http://projekt-opal.de>
		if (false) {
			queryString = Queries.getQuery("number-of-licenses");
		}

		if (false) {
			queryString = Queries.getQuery("distributions-with-license");
		}

		if (false) {
			queryString = Queries.getQuery("licenses-by-catalog");
		}
		
		// Describe a resource in graph <http://projekt-opal.de>
		if (false) {
			String resource;
			int limit;
			resource = org.apache.jena.vocabulary.DCAT.Dataset.getURI();
			resource = org.apache.jena.vocabulary.DCAT.Distribution.getURI();
			limit = 2;

			queryType = DESCRIBE;
			queryString = Queries.getQuery("describe-dataset").replace("dcat:Dataset", "<" + resource + ">")
					.replace("LIMIT 1", "LIMIT " + limit);
		}

		// Get licence information
		if (false) {
			queryString = Queries.getQuery("extract-licenses");
		}

		// Get theme information
		if (false) {
			queryString = Queries.getQuery("number-of-themes");
		}

		if (false) {
			queryString = Queries.getQuery("themes-by-catalog");
		}

		// Execution
		if (queryString == null) {
			System.err.println("Please provide a query.");
		} else {
			System.out.println(queryString);
			System.out.println();
			System.out.println();
			execute(sparqlEndpoint, isFusekiEndpoint, consumer, queryType, queryString);
		}
	}

}