package io.knotx.tutorials.impl;

import io.knotx.adapter.AbstractAdapterProxy;
import io.knotx.dataobjects.AdapterRequest;
import io.knotx.dataobjects.AdapterResponse;
import io.knotx.dataobjects.ClientResponse;
import io.knotx.tutorials.CustomServiceAdapter;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.rxjava.ext.jdbc.JDBCClient;
import rx.Observable;

public class CustomServiceAdapterProxyImpl extends AbstractAdapterProxy {

  private static final Logger LOGGER = LoggerFactory.getLogger(CustomServiceAdapter.class);
  private final JDBCClient client;

  public CustomServiceAdapterProxyImpl(JDBCClient client) {
    this.client = client;
  }

  @Override
  protected Observable<AdapterResponse> processRequest(AdapterRequest adapterRequest) {
    return client.getConnectionObservable()
        .flatMap(
            sqlConnection -> sqlConnection
                .queryObservable("select * from books")
                .doOnNext(rs -> {
                      for (JsonArray line : rs.getResults()) {
                        LOGGER.info("LINE: {}", line.encode());
                      }
                    }
                )
        )
        .map(rs -> new JsonArray(rs.getResults()))
        .map(body -> {
          final ClientResponse clientResponse = new ClientResponse();
          clientResponse.setBody(Buffer.buffer(body.encode()));
          return clientResponse;
        })
        .doOnNext(response -> LOGGER.info("client response: {}", response))
        .map(clientResponse -> {
          final AdapterResponse adapterResponse = new AdapterResponse();
          adapterResponse.setResponse(clientResponse);
          adapterResponse.setSignal("next");
          return adapterResponse;
        })
        .doOnNext(response -> LOGGER.info("adapter response: {}", response));
  }

}
