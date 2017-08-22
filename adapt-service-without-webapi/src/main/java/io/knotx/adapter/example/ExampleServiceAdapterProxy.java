package io.knotx.adapter.example;

import io.knotx.adapter.AbstractAdapterProxy;
import io.knotx.dataobjects.AdapterRequest;
import io.knotx.dataobjects.AdapterResponse;
import io.knotx.dataobjects.ClientResponse;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.sql.ResultSet;
import io.vertx.rxjava.ext.jdbc.JDBCClient;
import rx.Single;

public class ExampleServiceAdapterProxy extends AbstractAdapterProxy {

	//we will need JDBC Client here to perform DB queries
	private final JDBCClient client;

	public ExampleServiceAdapterProxy(JDBCClient client) {
		this.client = client;
	}

	@Override
	protected Single<AdapterResponse> processRequest(AdapterRequest adapterRequest) {
		final String query = adapterRequest.getParams().getString("query");
		return client.rxGetConnection()
				.flatMap(
						sqlConnection -> sqlConnection.rxQuery(query)
				)
				.map(this::toAdapterResponse);
	}

	private AdapterResponse toAdapterResponse(ResultSet rs) {
		final AdapterResponse adapterResponse = new AdapterResponse();
		final ClientResponse clientResponse = new ClientResponse();
		clientResponse.setBody(Buffer.buffer(new JsonArray(rs.getRows()).encode()));
		adapterResponse.setResponse(clientResponse);
		return adapterResponse;
	}

}