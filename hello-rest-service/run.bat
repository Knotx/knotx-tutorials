#!/bin/bash
java -Dvertx.disableDnsResolver=true -Dlogback.configurationFile=logback.xml -cp "app/*" io.knotx.launcher.LogbackLauncher -conf knotx-config.json
