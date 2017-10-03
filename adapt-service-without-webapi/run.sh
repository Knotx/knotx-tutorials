#!/bin/bash
java -Dlogback.configurationFile=knotx-standalone-1.1.1.logback.xml -cp "app/*" io.knotx.launcher.LogbackLauncher -conf knotx-standalone-1.1.1.json
