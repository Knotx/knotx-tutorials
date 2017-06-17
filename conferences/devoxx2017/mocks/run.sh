#!/bin/bash
java -Dlogback.configurationFile=knotx.logback.xml -cp "app/*" io.knotx.launcher.LogbackLauncher -conf knotx-mocks.json
