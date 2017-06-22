package io.knotx.examples;

import io.vertx.core.json.JsonObject;

public class TransactionsDbAdapterConfiguration {

  private String address;

  private JsonObject clientOptions;

  public TransactionsDbAdapterConfiguration(JsonObject config) {
    address = config.getString("address");
    clientOptions = config.getJsonObject("clientOptions", new JsonObject());
  }

  public JsonObject getClientOptions() {
    return clientOptions;
  }

  public String getAddress() {
    return address;
  }
}
