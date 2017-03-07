package io.knotx.tutorials.impl;

import com.google.common.collect.ImmutableList;
import io.knotx.adapter.AbstractAdapterProxy;
import io.knotx.dataobjects.AdapterRequest;
import io.knotx.dataobjects.AdapterResponse;
import io.knotx.dataobjects.ClientResponse;
import io.knotx.tutorials.CustomAdapterConfiguration;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.Vertx;
import java.util.HashMap;
import java.util.Map;
import rx.Observable;

public class CustomServiceAdapterProxyImpl extends AbstractAdapterProxy {

  private final Vertx vertx;
  private final CustomAdapterConfiguration configuration;

  public CustomServiceAdapterProxyImpl(Vertx vertx, CustomAdapterConfiguration configuration) {

    this.vertx = vertx;
    this.configuration = configuration;
  }

  @Override
  protected Observable<AdapterResponse> processRequest(AdapterRequest adapterRequest) {
    final Map<String, Object> map = new HashMap<>();
    map.put("title", "Java");
    map.put("books", ImmutableList.of("Book1", "Book2", "XYZ321"));

    final ClientResponse clientResponse = new ClientResponse();
    clientResponse.setBody(Buffer.buffer("{\"title\": \"Java Books\",\"books\": [\"A1\",\"A2\",\"C3\"]}"));
    final AdapterResponse adapterResponse = new AdapterResponse();
    adapterResponse.setResponse(clientResponse);

    return Observable.just(adapterResponse);
  }

}
