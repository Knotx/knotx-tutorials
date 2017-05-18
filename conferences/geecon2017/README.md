<img src="https://2017.geecon.org/assets/images/logo.png" alt="GeeCon"/>

# GeeCON 2017 demo

<p align="center">
  <img src="https://github.com/Cognifide/knotx/blob/master/icons/180x180.png?raw=true" alt="Knot.x Logo"/>
</p>

## Knot.x as a tool - simple REST integration

This demo extends [Hello REST service](http://knotx.io/blog/hello-rest-service/) tutorial with cluster mode aspects.

### Mocked data version

<p align="center">
  <img src="https://raw.githubusercontent.com/Knotx/knotx-tutorials/feature/geecon-demo/conferences/geecon2017/img/demo-knot-1.png" alt="Mocked"/>
</p>

Before the next steps download [Knot.x standalone fat jar](https://oss.sonatype.org/content/groups/public/io/knotx/knotx-standalone/1.0.1/knotx-standalone-1.0.1.fat.jar)
 and place it in the `app` folder under both `instance-1` and `instance-2` directories.

Next download [Knot.x mocks module](https://oss.sonatype.org/content/groups/public/io/knotx/knotx-mocks/1.0.1/knotx-mocks-1.0.1.fat.jar)
and place it in the `mocks/app` folder.

##### 1. Run mocks:
```
$ cd mocks
$ java -Dlogback.configurationFile=knotx-mocks-1.0.1.logback.xml -cp "app/*" io.knotx.launcher.LogbackLauncher -conf knotx-mocks-1.0.1.json
```

##### 2. Enter [http://localhost:3000/mock/service/books.json](http://localhost:3000/mock/service/books.json).

##### 3. Run `instance-1` with mock-config:
```
$ cd instance-1
$ java -Dvertx.disableDnsResolver=true -Dlogback.configurationFile=knotx-standalone-1.0.1.logback.xml -cp "app/*" io.knotx.launcher.LogbackLauncher -conf knotx-mocked-1.0.1.json
```

You should see the console log that lists used Knot.x Core Modules:
```
                Deployed 6497dd7d-d8a1-4ab4-995f-2cf42f897c0c [knotx:io.knotx.ServiceKnot]
                Deployed c8e6f66a-5887-4796-ad6b-3de9d02ca810 [knotx:io.knotx.ActionKnot]
                Deployed a926938c-e6a0-4186-a8f2-d047dc9db132 [knotx:io.knotx.FilesystemRepositoryConnector]
                Deployed f74675f8-550b-4d4d-bd5c-eea1b8e17357 [knotx:io.knotx.FragmentSplitter]
                Deployed 03a7f904-f699-437a-9965-b8f751b32858 [knotx:io.knotx.FragmentAssembler]
                Deployed 185e6dee-a347-4d11-ab8e-3a876df2655e [knotx:io.knotx.HttpRepositoryConnector]
                Deployed 4e43d285-0557-41ea-bacb-db288fc78366 [knotx:io.knotx.HttpServiceAdapter]
                Deployed 048fae38-7633-4c02-b4fb-a9f045352143 [knotx:io.knotx.KnotxServer]
                Deployed a4589518-3b0e-405a-a024-7c8556727288 [knotx:io.knotx.HandlebarsKnot]
```


##### 4. Enter [Books Page](http://localhost:8092/service/books.html)
See console log:
```
2017-05-17 19:56:22 [vert.x-eventloop-thread-0] DEBUG i.k.k.service.impl.FragmentProcessor - Fetching data from service knotx.adapter.service.http {"path":"/books/v1/volumes?q=java"}
```

##### 5. Open [Books Page CMS Template](https://github.com/Knotx/knotx-tutorials/blob/feature/geecon-demo/conferences/geecon2017/instance-1/library/service/books.html#L25)
Notice the configuration that points to service `"bookslist"`:
```
<script data-knotx-knots="services,handlebars"
        data-knotx-service="bookslist"
        type="text/knotx-snippet">
```

##### 6. Shut down the `instance-1` (by `^C`).

---

### GoogleAPI data version

<p align="center">
  <img src="https://raw.githubusercontent.com/Knotx/knotx-tutorials/feature/geecon-demo/conferences/geecon2017/img/demo-knot-2.png" alt="Google API"/>
</p>

##### 1. Open [Google Books API](https://www.googleapis.com/books/v1/volumes?q=java)

##### 2. Run `instance-1` in *cluster* mode:
```
$ cd instance-1
$ java -Dvertx.disableDnsResolver=true -Dlogback.configurationFile=knotx-standalone-1.0.1.logback.xml -cp "app/*" io.knotx.launcher.LogbackLauncher -conf knotx-standalone-1.0.1.json
```

##### 3. Open [Books Page](http://localhost:8092/service/books.html?q=java)
See the console log and try also with other queries:
    - [Vert.x](http://localhost:8092/service/books.html?q=vertx)
    - [Reactive](http://localhost:8092/service/books.html?q=reactive)

##### 4. Shut down the `instance-1` (by `^C`).

### Cluster mode - scale the most used part of the system - `Search`

<p align="center">
  <img src="https://raw.githubusercontent.com/Knotx/knotx-tutorials/feature/geecon-demo/conferences/geecon2017/img/demo-knot-3.png" alt="Cluster"/>
</p>

##### 1. Start the instances `instance-1` and `instance-2` of Knot.x application:
```
$ cd instance-1
$ java -Dvertx.disableDnsResolver=true -Dlogback.configurationFile=knotx-standalone-1.0.1.logback.xml -cp "app/*" io.knotx.launcher.LogbackLauncher -conf knotx-standalone-1.0.1.json -cluster
```

```
$ cd instance-2
$ java -Dvertx.disableDnsResolver=true -Dlogback.configurationFile=knotx-standalone-1.0.1.logback.xml -cp "app/*" io.knotx.launcher.LogbackLauncher -conf knotx-standalone-1.0.1.json -cluster
```

See the console log and see that clustering works:
 ```
                 Deployed e25c5446-0a19-4b76-87c3-cc1cec07e869 [knotx:io.knotx.ServiceKnot]
                 Deployed e900595f-2522-4279-82c8-a49b304c1895 [knotx:io.knotx.HttpServiceAdapter]
 ...
 Members [2] {
         Member [192.168.56.1]:5701
         Member [192.168.56.1]:5702 this
 }
 ```

##### 2. Open [Books Page](http://localhost:8092/service/books.html?q=java) and look how **Load Balancing** works:
See the console log and try also with other queries:
    - [Vert.x](http://localhost:8092/service/books.html?q=vertx)
    - [Reactive](http://localhost:8092/service/books.html?q=reactive)


## Knot.x as an integration layer

### DB Integration

<p align="center">
  <img src="https://raw.githubusercontent.com/Knotx/knotx-tutorials/feature/geecon-demo/conferences/geecon2017/img/demo-knot-4.png" alt="DB Integration"/>
</p>

Before the next steps checkout and build [Adapt Service Without Web API](https://github.com/Knotx/knotx-tutorials/tree/master/adapt-service-without-webapi)
using Maven. Place the `custom-service-adapter-1.0.1-fat.jar` it in the `instance-2/app` folder.
Then prepare the DB according to [Tutorial](http://o7planning.org/en/10287/installing-and-configuring-hsqldb-database).

##### 1. Run `instance-3` with `BooksDbAdapter` in cluster mode (assuming `instance-1` and `instance-2` are still running):

```
$ cd instance-3
$ java -Dvertx.disableDnsResolver=true -Dlogback.configurationFile=knotx-standalone-1.0.1.logback.xml -cp "app/*" io.knotx.launcher.LogbackLauncher -conf knotx-standalone-1.0.1.json -cluster
```

##### 2. Open [DB Books Page](http://localhost:8092/db/books.html).

##### 3. Add a record to Books DB (e.g. new author or a book).

##### 4. Refresh [DB Books Page](http://localhost:8092/db/books.html).

##### 5. See how [`BooksDbAdapter`]() and [`BooksDbAdapterProxyImpl`]() works.

##### 6. See the `knotx-standalone-1.0.1.json` config file in the `instance-3` <- connection is defined there.
