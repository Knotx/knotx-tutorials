# Devoxx 2017 demo

This is demo script prepared for Devoxx 2017.

<p align="center">
  <img src="https://github.com/Cognifide/knotx/blob/master/icons/180x180.png?raw=true" alt="Knot.x Logo"/>
</p>

## Prerequisites

Before the next steps download [Knot.x standalone fat jar](https://oss.sonatype.org/content/groups/public/io/knotx/knotx-standalone/1.0.1/knotx-standalone-1.0.1.fat.jar)
 and place it in the `app` folder under both `demo-instance` and `database-instance` directories.

## Demo:
```
$ cd demo-instance
$ java -Dvertx.disableDnsResolver=true -Dlogback.configurationFile=knotx-demo.logback.xml -cp "app/*" io.knotx.launcher.LogbackLauncher -conf knotx-demo.json
```