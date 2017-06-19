package io.knotx.examples;

import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.serviceproxy.ProxyHelper;

public class SimpleAdapterProxy extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(SimpleAdapterProxy.class);

  private MessageConsumer<JsonObject> consumer;

  @Override
  public void init(Vertx vertx, Context context) {
    super.init(vertx, context);
  }

  @Override
  public void start() throws Exception {
    LOGGER.info("Starting <{}>", this.getClass().getSimpleName());
    // TODO init consumer
  }

  @Override
  public void stop() throws Exception {
    ProxyHelper.unregisterService(consumer);
  }

}
