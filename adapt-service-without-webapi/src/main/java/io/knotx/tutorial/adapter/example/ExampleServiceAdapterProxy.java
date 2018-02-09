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

import io.knotx.adapter.AbstractAdapterProxy;
import io.knotx.dataobjects.AdapterRequest;
import io.knotx.dataobjects.AdapterResponse;
import io.knotx.dataobjects.ClientResponse;
import io.reactivex.Single;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.sql.ResultSet;
import io.vertx.reactivex.ext.jdbc.JDBCClient;

public class ExampleServiceAdapterProxy extends AbstractAdapterProxy {

  //we will need JDBC Client here to perform DB queries
  private final JDBCClient client;

  public ExampleServiceAdapterProxy(JDBCClient client) {
    this.client = client;
  }

  @Override
  protected Single<AdapterResponse> processRequest(AdapterRequest adapterRequest) {
    final String query = adapterRequest.getParams().getString("query");
    return client.rxGetConnection()
        .flatMap(
            sqlConnection -> sqlConnection.rxQuery(query)
        )
        .map(this::toAdapterResponse);
  }

  private AdapterResponse toAdapterResponse(ResultSet rs) {
    final AdapterResponse adapterResponse = new AdapterResponse();
    final ClientResponse clientResponse = new ClientResponse();
    clientResponse.setBody(Buffer.buffer(new JsonArray(rs.getRows()).encode()));
    adapterResponse.setResponse(clientResponse);
    return adapterResponse;
  }
}