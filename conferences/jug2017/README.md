<p align="center">
  <img src="https://github.com/Knotx/knotx-tutorials/blob/master/conferences/jug2017/img/banner.png?raw=true" alt="Knot.x JUG"/>
</p>

## Prerequisites

Before the next steps
- download [Knot.x standalone fat jar](https://oss.sonatype.org/content/groups/public/io/knotx/knotx-standalone/1.1.2/knotx-standalone-1.1.2.fat.jar)
 and place it in the `app` folder under `examples/online-markets`,
- download [Knot.x mocks fat jar](https://oss.sonatype.org/content/groups/public/io/knotx/knotx-mocks/1.1.2/knotx-mocks-1.1.2.fat.jar)
   and place it in the `app` folder under `mocks`,
- build `examples/market-api` (`mvn clean install`) and place `market-api-1.1.2.jar` in the `app` folder under `examples/online-markets`.


## Demo

### Integrating web-based services

<p align="center">
  <img src="https://github.com/Knotx/knotx-tutorials/blob/master/conferences/jug2017/img/demo-one-host.png?raw=true" alt="Knot.x JUG"/>
</p>

In `mocks` run:
```
$ java -Dlogback.configurationFile=knotx.logback.xml -cp "app/*" io.knotx.launcher.LogbackLauncher -conf knotx-mocks.json
```
to start Knot.x mocks module (it will provide some sample endpoints with JSON responses).


In `examples/online-markets` run:
```
$ java -Dlogback.configurationFile=logback.xml -cp "app/*" io.knotx.launcher.LogbackLauncher -conf knotx-standalone.json -cluster
```
to start Knot.x instance with `market-api` module.

Open [Example Online Market Dashboard Page](http://localhost:8092/example/pages/main.html).

#### Request processing

On the same page you can see `Commodities` panel with natural resources and their current market value.
This data comes from external REST `commodities` endpoint which is provided by Knot.x mocks in this example:
- [http://localhost:3000/mock/service/commodities.json](http://localhost:3000/mock/service/commodities.json)

`Commodities` panel is result of processing a Knot.x snippet and ingesting the data from the service.
This snippet looks like this:

```html
<script data-knotx-knots="services,handlebars"
      data-knotx-service="commodities"
      type="text/knotx-snippet">
          {{#each _result.commodities}}
            <a href="#" class="list-group-item">
                <i class="fa fa-money fa-fw"></i> {{name}}
                <span class="pull-right text-muted small"><em>{{units}} $</em></span>
                <span class="text-muted small {{delta}}"><em>{{change}}</em></span>
            </a>
          {{/each}}


</script>
```

In this snippet we tell Knot.x that service `commodities` will provide the data for the component.
Knot.x uses Handlebars Templating Engine to ingest the dynamic data.
As you can see in the snippet, there is no URL defined, only the name of a service `commodities`.
This approach allows the snippets to be free of the data source dependency and simply focus on shaping the data into components.
Its actual address is defined in the configuration:

```json
{
  "name": "commodities",
  "address": "knotx.adapter.service.http",
  "params": {
    "path": "/service/mock/commodities.json"
  }
}
```

### Database Integration

<p align="center">
  <img src="https://github.com/Knotx/knotx-tutorials/blob/master/conferences/jug2017/img/demo-two-hosts.png?raw=true" alt="Knot.x JUG"/>
</p>

#### Prerequisites
Build `examples/db-adapter` (`mvn clean install`) and place `db-adapter-1.0-fat.jar` in the `app` folder under `examples/db-adapter`.
Setup the DB according to [Tutorial](http://o7planning.org/en/10287/installing-and-configuring-hsqldb-database) tutorial.
You will find example data in `examples/db-adapter/db`.


#### Running cluster with DB Adapter
Stop `examples/online-markets` if it is still running. We have to update the configuration so that Knot.x know about new service available.

In `examples/db-adapter` run:
```
$ java -Dlogback.configurationFile=logback.xml -cp "app/*" io.knotx.launcher.LogbackLauncher -conf knotx-db-adapter-extension.json -cluster
```
It will start the DB Adapter in a cluster mode.


Now, modify `examples/online-markets/knotx-standalone.json` and add following configuration to `services` defined in `knotx:io.knotx.ServiceKnot`:

```json
{
  "name": "transactions",
  "address": "knotx.adapter.service.transastionsdb",
  "params": {
    "query": "SELECT * FROM transactions"
  }
}
```

Now, run again `examples/online-markets` run:
```
$ java -Dlogback.configurationFile=logback.xml -cp "app/*" io.knotx.launcher.LogbackLauncher -conf knotx-standalone.json -cluster
```
to start Knot.x instance with updated configuration.

You will see that it will find a cluster with Knot.x by message like:
```
com.hazelcast.cluster.ClusterService
INFO: [192.168.123.1]:5702 [dev] [3.6.3]

Members [2] {
        Member [192.168.123.1]:5702 this
        Member [192.168.123.1]:5701
}

```

This means that Vert.x works now in cluster - first instance is Knot.x wid `market-api`, second is `db-adapter`. They see each other thanks to Vert.x Event Bus.
Now open again [Example Online Market Dashboard Page](http://localhost:8092/example/pages/main.html).
You should see a page with no changes to previous version. We still need to embed a dynamic snippet:

```html
<script data-knotx-knots="services,handlebars"
      data-knotx-service="transactions"
      type="text/knotx-snippet">
             {{#each _result}}
                <a href="#" class="list-group-item">
                    <i class="fa fa-history fa-fw"></i> {{this.COMMODITY}}
                    <span class="pull-right text-muted"><em>total: {{this.TRANSACTION_VALUE}} $</em></span>
                    <span class="text-muted small"><em>unit price: {{this.UNIT_PRICE}}$</em></span>
                    <span class="text-muted small"><em> x {{this.UNITS_BOUGHT}}</em></span>
                    <span class="text-muted small"><em> at {{this.TRANSACTION_DATE}}</em></span>
                </a>
              {{/each}}
</script>
```

Place it instead of `<p>TODO</p>` just about line `351`.

Refresh the page - you should see transactions from the Database.