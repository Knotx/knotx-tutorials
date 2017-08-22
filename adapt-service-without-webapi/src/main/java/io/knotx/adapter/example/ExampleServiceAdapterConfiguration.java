package io.knotx.adapter.example;

import io.vertx.core.json.JsonObject;

public class ExampleServiceAdapterConfiguration {

  private String address;

  private JsonObject clientOptions;

  public ExampleServiceAdapterConfiguration(JsonObject config) {
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