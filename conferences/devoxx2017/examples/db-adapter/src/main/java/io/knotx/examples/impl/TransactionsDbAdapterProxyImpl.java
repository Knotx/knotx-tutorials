package io.knotx.examples.impl;

import io.knotx.adapter.AbstractAdapterProxy;
import io.knotx.dataobjects.AdapterRequest;
import io.knotx.dataobjects.AdapterResponse;
import io.knotx.dataobjects.ClientResponse;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.sql.ResultSet;
import io.vertx.rxjava.ext.jdbc.JDBCClient;
import rx.Single;

public class TransactionsDbAdapterProxyImpl extends AbstractAdapterProxy {

  private static final Logger LOGGER = LoggerFactory.getLogger(TransactionsDbAdapterProxyImpl.class);
  private final JDBCClient client;

  public TransactionsDbAdapterProxyImpl(JDBCClient client) {
    this.client = client;
  }

  @Override
  protected Single<AdapterResponse> processRequest(AdapterRequest adapterRequest) {
    final String query = adapterRequest.getParams().getString("query");
    LOGGER.debug("Processing request with query: `{}`", query);
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
