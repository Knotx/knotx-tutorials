/*
    Copyright (C) 2016 Cognifide Limited

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

package io.knotx.tutorial.adapter.example;

import io.knotx.proxy.AdapterProxy;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.jdbc.JDBCClient;
import io.vertx.serviceproxy.ServiceBinder;


public class ExampleServiceAdapter extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(ExampleServiceAdapter.class);

  private MessageConsumer<JsonObject> consumer;
  private ExampleServiceAdapterConfiguration configuration;
  private ServiceBinder serviceBinder;

  @Override
  public void init(Vertx vertx, Context context) {
    super.init(vertx, context);
    LOGGER.debug("Initializing <{}>", this.getClass().getSimpleName());
    // using config() method from AbstractVerticle we simply pass our JSON file configuration to Java model
    configuration = new ExampleServiceAdapterConfiguration(config());
  }

  @Override
  public void start() throws Exception {
    LOGGER.info("Starting <{}>", this.getClass().getSimpleName());

    //create JDBC Clinet here and pass it to AdapterProxy - notice using clientOptions property here
    final JDBCClient client = JDBCClient.createShared(vertx, configuration.getClientOptions());

    //register the service proxy on the event bus, notice using `getVertx()` here to obtain non-rx version of vertx
    serviceBinder = new ServiceBinder(getVertx());
    consumer = serviceBinder
        .setAddress(configuration.getAddress())
        .register(AdapterProxy.class, new ExampleServiceAdapterProxy(client));
  }

  @Override
  public void stop() throws Exception {
    // unregister adapter when no longer needed
    serviceBinder.unregister(consumer);
    LOGGER.debug("Stopped <{}>", this.getClass().getSimpleName());
  }
}
