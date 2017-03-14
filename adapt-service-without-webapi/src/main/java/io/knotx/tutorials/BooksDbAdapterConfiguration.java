package io.knotx.tutorials;

import io.vertx.core.json.JsonObject;

public class BooksDbAdapterConfiguration {

  private String address;

  private JsonObject clientOptions;

  public BooksDbAdapterConfiguration(JsonObject config) {
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
