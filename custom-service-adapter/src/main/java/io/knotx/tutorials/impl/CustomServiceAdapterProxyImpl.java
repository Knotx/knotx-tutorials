package io.knotx.tutorials.impl;

import com.google.common.collect.ImmutableList;
import io.knotx.adapter.AbstractAdapterProxy;
import io.knotx.dataobjects.AdapterRequest;
import io.knotx.dataobjects.AdapterResponse;
import io.knotx.dataobjects.ClientResponse;
import io.knotx.tutorials.CustomServiceAdapter;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.rxjava.core.Vertx;
import java.util.HashMap;
import java.util.Map;
import rx.Observable;

public class CustomServiceAdapterProxyImpl extends AbstractAdapterProxy {

  private static final Logger LOGGER = LoggerFactory.getLogger(CustomServiceAdapter.class);

  private final Vertx vertx;
  private JDBCClient client;

  public CustomServiceAdapterProxyImpl(Vertx vertx, JDBCClient client) {
    this.vertx = vertx;
    this.client = client;
  }

  @Override
  protected Observable<AdapterResponse> processRequest(AdapterRequest adapterRequest) {
    client.getConnection(conn -> {
      if (conn.failed()) {
        System.err.println(conn.cause().getMessage());
        return;
      }

      final SQLConnection connection = conn.result();
      connection.query("select * from books", rs -> {
        for (JsonArray line : rs.result().getResults()) {
          LOGGER.info(line.encode());
        }

        // and close the connection
        connection.close(done -> {
          if (done.failed()) {
            throw new RuntimeException(done.cause());
          }
        });
      });
    });

    final Map<String, Object> map = new HashMap<>();
    map.put("title", "Java");
    map.put("books", ImmutableList.of("Book1", "Book2", "XYZ321"));

    final ClientResponse clientResponse = new ClientResponse();
    clientResponse
        .setBody(Buffer.buffer("{\"title\": \"Java Books\",\"books\": [\"A1\",\"A2\",\"C3\"]}"));
    final AdapterResponse adapterResponse = new AdapterResponse();
    adapterResponse.setResponse(clientResponse);
    return Observable.just(adapterResponse);
  }

}
