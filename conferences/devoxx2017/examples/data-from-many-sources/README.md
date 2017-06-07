# Example: data from many sources

## Configuration

Before the next steps download [Knot.x standalone fat jar](https://oss.sonatype.org/content/groups/public/io/knotx/knotx-standalone/1.0.1/knotx-standalone-1.0.1.fat.jar)
 and place it in the `app` folder under parent `data-from-many-sources` directory.

More details about deployment can be found [here](https://github.com/Cognifide/knotx/wiki/KnotxDeployment)

## Run

Knot.x is delivered as simple fat jar file. The command below starts Knot.x with all configured modules.

```
$ java -Dlogback.configurationFile=knotx.logback.xml -cp "app/*" io.knotx.launcher.LogbackLauncher -conf knotx-data-from-many-sources.json -cluster
```

It listens on the port 8092 by default.