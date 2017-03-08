package io.knotx.tutorials.impl;

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
import rx.Observable;

public class BooksDbAdapterProxyImpl extends AbstractAdapterProxy {

  private static final Logger LOGGER = LoggerFactory.getLogger(BooksDbAdapterProxyImpl.class);
  private final JDBCClient client;

  public BooksDbAdapterProxyImpl(JDBCClient client) {
    this.client = client;
  }

  @Override
  protected Observable<AdapterResponse> processRequest(AdapterRequest adapterRequest) {
    final String query = adapterRequest.getParams().getString("query");
    LOGGER.info("Processing request with query: `{}`", query);
    return client.getConnectionObservable()
        .flatMap(
            sqlConnection -> sqlConnection.queryObservable(query)
        )
        .map(this::toAdapterResponse);
  }

  private AdapterResponse toAdapterResponse(ResultSet rs) {
    final AdapterResponse adapterResponse = new AdapterResponse();
    final ClientResponse clientResponse = new ClientResponse();
    clientResponse.setBody(Buffer.buffer(new JsonArray(rs.getRows()).encode()));
    adapterResponse.setResponse(clientResponse);
    adapterResponse.setSignal("next");
    return adapterResponse;
  }

}
