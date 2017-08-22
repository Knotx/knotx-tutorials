package io.knotx.adapter.example;

import io.knotx.proxy.AdapterProxy;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.ext.jdbc.JDBCClient;
import io.vertx.serviceproxy.ProxyHelper;

public class ExampleServiceAdapter extends AbstractVerticle {

  private MessageConsumer<JsonObject> consumer;
  private ExampleServiceAdapterConfiguration configuration;

  @Override
  public void init(Vertx vertx, Context context) {
    super.init(vertx, context);
    // using config() method from AbstractVerticle we simply pass our JSON file configuration to Java model
    configuration = new ExampleServiceAdapterConfiguration(config());
  }

  @Override
  public void start() throws Exception {
    //create JDBC Clinet here and pass it to AdapterProxy - notice using clientOptions property here
    final JDBCClient client = JDBCClient.createShared(vertx, configuration.getClientOptions());

    //register the service proxy on the event bus, notice using `getVertx()` here to obtain non-rx version of vertx
    consumer = ProxyHelper
            .registerService(AdapterProxy.class, getVertx(),
                    new ExampleServiceAdapterProxy(client),
                    configuration.getAddress());
  }

  @Override
  public void stop() throws Exception {
    // unregister adapter when no longer needed
    ProxyHelper.unregisterService(consumer);
  }

}