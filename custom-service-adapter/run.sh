#!/bin/bash
java -Dvertx.disableDnsResolver=true -Dlogback.configurationFile=knotx-standalone-1.0.0.logback.xml -cp "app/*" io.knotx.launcher.LogbackLauncher -conf knotx-standalone-1.0.0.json
