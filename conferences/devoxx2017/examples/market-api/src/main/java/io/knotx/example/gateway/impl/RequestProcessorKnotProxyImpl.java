package io.knotx.example.gateway.impl;

import io.knotx.dataobjects.ClientResponse;
import io.knotx.dataobjects.KnotContext;
import io.knotx.knot.AbstractKnotProxy;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import java.util.Calendar;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Set;
import rx.Single;

public class RequestProcessorKnotProxyImpl extends AbstractKnotProxy {

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

  private KnotContext createSuccessResponse(KnotContext inputContext) {

    ClientResponse clientResponse = new ClientResponse();

    io.vertx.rxjava.core.MultiMap headers = clientResponse.getHeaders();
    final String response = generateResponse();
    headers.add(HttpHeaders.CONTENT_LENGTH.toString().toLowerCase(),
        Integer.toString(response.length()))
        .add("Content-Type", "application/json");

    clientResponse.setBody(Buffer.buffer(response)).setHeaders(headers);
    clientResponse.setStatusCode(HttpResponseStatus.OK.code());

    return new KnotContext()
        .setClientRequest(inputContext.getClientRequest())
        .setClientResponse(clientResponse);
  }

  private String generateResponse() {
    JsonArray response = new JsonArray();

    final Date start = new Date(1223679600000L);
    final MarketSimulation marketSimulation = new MarketSimulation(0.05d, 50.d);

    for (int i = 0; i < 350; i++) {
      final long timeInMillis = nextDate(start, i);
      final JsonArray jsonArray = new JsonArray();

      jsonArray.add(timeInMillis)
          .add(marketSimulation.simulate());
      response.add(jsonArray);
    }

    return response.toString();
  }

  private long nextDate(Date start, int i) {
    Calendar c = Calendar.getInstance();
    c.setTime(start);
    c.add(Calendar.HOUR, 48 * i);
    return c.getTimeInMillis();
  }
}
