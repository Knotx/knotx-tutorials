#!/bin/bash
java -Dlogback.configurationFile=logback.xml -cp "app/*" io.knotx.launcher.LogbackLauncher -conf knotx-db-adapter-extension.json -cluster
