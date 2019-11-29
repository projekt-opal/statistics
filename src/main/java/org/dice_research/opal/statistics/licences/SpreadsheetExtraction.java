package org.dice_research.opal.statistics.licences;

import java.util.Iterator;
import java.util.function.Consumer;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.RDFNode;

/**
 * Executes query and prints results in format for use in spreadsheets.
 *
 * @author Adrian Wilke
 */
public class SpreadsheetExtraction extends QueryExecution {

	/**
	 * Main entry point.
	 */
	public static void main(String[] args) throws Exception {

		// Configuration
		String sparqlEndpoint = Config.get(Config.SPARQL_ENDPOINT_OPAL);
		boolean isFusekiEndpoint = true;
		Consumer<ResultSet> consumer = SpreadsheetExtraction::printForPdf;
		int queryType = SELECT;
		String queryString = Queries.getQuery("number-of-themes");

		// Execution
		System.out.println(queryString);
		System.out.println();
		System.out.println();
		execute(sparqlEndpoint, isFusekiEndpoint, consumer, queryType, queryString);
	}

	/**
	 * Prints values separated by tabular.
	 * 
	 * Consumer<ResultSet>
	 */
	private static void printForPdf(ResultSet resultSet) {
		while (resultSet.hasNext()) {
			QuerySolution querySolution = resultSet.next();
			Iterator<String> varsIterator = querySolution.varNames();
			while (varsIterator.hasNext()) {
				RDFNode rdfNode = querySolution.get(varsIterator.next());
				if (rdfNode.isLiteral()) {

					System.out.print(rdfNode.asLiteral().getLexicalForm().toString());
					System.out.print("\t");
				} else {
					System.out.print(rdfNode.toString());
					System.out.print("\t");
				}
			}
			System.out.println();
		}
	}
}
