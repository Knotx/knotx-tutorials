package io.knotx.tutorials;

import io.knotx.proxy.AdapterProxy;
import io.knotx.tutorials.impl.BooksDbAdapterProxyImpl;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.rxjava.ext.jdbc.JDBCClient;
import io.vertx.serviceproxy.ProxyHelper;

public class BooksDbAdapter extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(BooksDbAdapter.class);

  private MessageConsumer<JsonObject> consumer;
  private BooksDbAdapterConfiguration configuration;

  @Override
  public void init(Vertx vertx, Context context) {
    super.init(vertx, context);
    configuration = new BooksDbAdapterConfiguration(config());
  }

  @Override
  public void start() throws Exception {
    LOGGER.info("Starting <{}>", this.getClass().getSimpleName());

    final io.vertx.rxjava.core.Vertx rxVertx = new io.vertx.rxjava.core.Vertx(this.vertx);
    final JDBCClient client = JDBCClient.createShared(rxVertx, configuration.getClientOptions());

    //register the service proxy on event bus
    consumer = ProxyHelper
        .registerService(AdapterProxy.class, this.vertx,
            new BooksDbAdapterProxyImpl(client),
            configuration.getAddress());
  }

  @Override
  public void stop() throws Exception {
    ProxyHelper.unregisterService(consumer);
  }

}
