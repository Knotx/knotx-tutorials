package io.knotx.example.gateway.impl;

import io.knotx.dataobjects.ClientResponse;
import io.knotx.dataobjects.KnotContext;
import io.knotx.knot.AbstractKnotProxy;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.util.Calendar;
import java.util.NoSuchElementException;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import rx.Single;

public class RequestProcessorKnotProxyImpl extends AbstractKnotProxy {

  private static final int SIMULATIONS_NO = 240;

  @Override
  protected Single<KnotContext> processRequest(KnotContext knotContext) {
    return Single.just(createSuccessResponse(knotContext));
  }

  @Override
  protected boolean shouldProcess(Set<String> knots) {
    return true;
  }

  @Override
  protected KnotContext processError(KnotContext knotContext, Throwable error) {
    HttpResponseStatus statusCode;
    if (error instanceof NoSuchElementException) {
      statusCode = HttpResponseStatus.NOT_FOUND;
    } else {
      statusCode = HttpResponseStatus.INTERNAL_SERVER_ERROR;
    }
    knotContext.getClientResponse().setStatusCode(statusCode.code());
    return knotContext;
  }

  private KnotContext createSuccessResponse(KnotContext knotContext) {

    ClientResponse clientResponse = new ClientResponse();

    io.vertx.rxjava.core.MultiMap headers = clientResponse.getHeaders();
    final String response = generateResponse(knotContext);
    headers.add(HttpHeaders.CONTENT_LENGTH.toString().toLowerCase(),
        Integer.toString(response.length()))
        .add("Content-Type", "application/json");

    clientResponse.setBody(Buffer.buffer(response)).setHeaders(headers);
    clientResponse.setStatusCode(HttpResponseStatus.OK.code());

    return new KnotContext()
        .setClientRequest(knotContext.getClientRequest())
        .setClientResponse(clientResponse);
  }

  private String generateResponse(KnotContext knotContext) {

    final MarketSimulation marketSimulation = new MarketSimulation(0.05d, 50.d);
    final Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.SECOND, -SIMULATIONS_NO);

    JsonArray simulation = new JsonArray();
    for (int i = 0; i < SIMULATIONS_NO; i++) {
      final long timeInMillis = nextDate(calendar);
      final JsonArray dayRates = new JsonArray();
      dayRates.add(timeInMillis)
          .add(marketSimulation.simulate());
      simulation.add(dayRates);
    }

    JsonObject response = new JsonObject();
    response.put("name", extractName(knotContext));
    response.put("rates", simulation);
    return response.toString();
  }

  private String extractName(KnotContext knotContext) {
    final String requestPath = knotContext.getClientRequest().getPath();
    return StringUtils.substringAfterLast(requestPath, "/");
  }

  private long nextDate(Calendar c) {
    c.add(Calendar.SECOND, 1);
    return c.getTimeInMillis();
  }
}
