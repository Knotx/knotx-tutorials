package io.knotx.tutorials;

import io.knotx.proxy.AdapterProxy;
import io.knotx.tutorials.impl.CustomServiceAdapterProxyImpl;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.serviceproxy.ProxyHelper;

public class CustomServiceAdapter extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(CustomServiceAdapter.class);

  private MessageConsumer<JsonObject> consumer;
  private CustomAdapterConfiguration configuration;

  @Override
  public void init(Vertx vertx, Context context) {
    super.init(vertx, context);
    configuration = new CustomAdapterConfiguration(config());
  }

  @Override
  public void start() throws Exception {
    LOGGER.info("Starting <{}>", this.getClass().getSimpleName());

    final JDBCClient client = JDBCClient.createShared(vertx, configuration.getClientOptions());

    //register the service proxy on event bus
    consumer = ProxyHelper
        .registerService(AdapterProxy.class, vertx,
            new CustomServiceAdapterProxyImpl(new io.vertx.rxjava.core.Vertx(vertx), client),
            configuration.getAddress());
  }

  @Override
  public void stop() throws Exception {
    ProxyHelper.unregisterService(consumer);
  }

}
