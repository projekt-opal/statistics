package org.dice_research.opal.statistics.licences;

import java.util.function.Consumer;

import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFuseki;
import org.apache.jena.rdfconnection.RDFConnectionRemote;
import org.apache.jena.rdfconnection.SparqlQueryConnection;

/**
 * Query execution.
 * 
 * @author Adrian Wilke
 */
public class QueryExecution {

	protected final static int SELECT = 0;
	protected final static int DESCRIBE = 1;

	/**
	 * Connects to SPARQL endpoint.
	 * 
	 * @see https://jena.apache.org/documentation/rdfconnection/#configuring-a-remote-rdfconnection
	 */
	protected static RDFConnection connect(String sparqlEndpoint, boolean isFuseki) {
		if (isFuseki) {
			return RDFConnectionFuseki.create().destination(sparqlEndpoint).build();
		} else {
			return RDFConnectionRemote.create().destination(sparqlEndpoint).build();
		}
	}

	/**
	 * Prints result set.
	 * 
	 * Consumer<ResultSet>
	 */
	protected static void print(ResultSet resultSet) {
		ResultSetFormatter.out(resultSet);
	}

	/**
	 * Executes query on endpoint.
	 */
	protected static void execute(String sparqlEndpoint, boolean isFusekiEndpoint, Consumer<ResultSet> consumer,
			int queryType, String queryString) {
		try (SparqlQueryConnection connection = connect(sparqlEndpoint, isFusekiEndpoint)) {
			if (queryType == SELECT) {
				connection.queryResultSet(queryString, consumer);
			} else if (queryType == DESCRIBE) {
				connection.queryDescribe(queryString).write(System.out, "TTL", null);
			}
			connection.close();
		}
	}

}