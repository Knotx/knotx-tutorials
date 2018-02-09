#!/bin/bash
java -Dlogback.configurationFile=knotx-standalone.logback.xml -cp "app/*" io.knotx.launcher.LogbackLauncher -conf knotx-standalone.json
