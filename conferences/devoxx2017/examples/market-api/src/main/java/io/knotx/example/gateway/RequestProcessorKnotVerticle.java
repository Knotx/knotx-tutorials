package io.knotx.example.gateway;

import io.knotx.gateway.configuration.KnotxGatewayKnotConfiguration;
import io.knotx.proxy.KnotProxy;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.serviceproxy.ProxyHelper;
import io.knotx.example.gateway.impl.RequestProcessorKnotProxyImpl;

public class RequestProcessorKnotVerticle extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(RequestProcessorKnotVerticle.class);

  private KnotxGatewayKnotConfiguration configuration;

  private MessageConsumer<JsonObject> consumer;

  @Override
  public void init(Vertx vertx, Context context) {
    super.init(vertx, context);
    this.configuration = new KnotxGatewayKnotConfiguration(config());
  }

  @Override
  public void start() throws Exception {
    LOGGER.info("Starting <{}>", this.getClass().getSimpleName());
    consumer = ProxyHelper
        .registerService(KnotProxy.class, vertx,
            new RequestProcessorKnotProxyImpl(),
            configuration.getAddress());

  }

  @Override
  public void stop() throws Exception {
    ProxyHelper.unregisterService(consumer);
  }
}
