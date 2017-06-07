# Knot.x Prototyping Module

## Prerequisites

Before the next steps download [Knot.x mocks fat jar](https://oss.sonatype.org/content/groups/public/io/knotx/knotx-mocks/1.0.1/knotx-mocks-1.0.1.fat.jar)
 and place it in the `app` folder under parent `mocks` directory.

## Run
Knot.x Prototyping module is separate JVM application allowing to mock REST services responses.

It is written on the top of Vert.x so it is simple jar file to run.
```
$ java -Dvertx.disableDnsResolver=true -Dlogback.configurationFile=knotx.logback.xml -cp "app/*" io.knotx.launcher.LogbackLauncher -conf knotx-mocks.json
```
It listens on the port 3000 by default.

You can validate a module status: [http://localhost:3000/healthcheck.json](http://localhost:3000/healthcheck.json).