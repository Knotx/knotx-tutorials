# GeeCON 2017 demo. 

Configuration comes from tutorial [Hello REST service](http://knotx.io/blog/hello-rest-service/). 

#### 1. Run instance 1 with cluster mode:
```
$ cd instance-1
$ java -Dvertx.disableDnsResolver=true -Dlogback.configurationFile=knotx-standalone-1.0.1.logback.xml -cp "app/*" io.knotx.launcher.LogbackLauncher -conf knotx-standalone-1.0.1.json -cluster
```

Console log:
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
...       
Members [1] {
        Member [192.168.56.1]:5701 this
}
```

#### 2. Open [Books Page](http://localhost:8092/html/books.html).

Console log:
```
2017-05-17 19:56:22 [vert.x-eventloop-thread-0] DEBUG i.k.k.service.impl.FragmentProcessor - Fetching data from service knotx.adapter.service.http {"path":"/books/v1/volumes?q=java"}
```


#### 3. Let's see our [external service](https://www.googleapis.com/books/v1/volumes?q=java)

JSON service call result.

#### 4. Open [books.html](https://github.com/Knotx/knotx-tutorials/blob/master/conferences/geecon2017/instance-1/library/html/books.html) with template.

HTML markup with "shaped" books.

#### 5. Start second instance of Knot.x application:
```
$ cd instance-2
$ java -Dvertx.disableDnsResolver=true -Dlogback.configurationFile=knotx-standalone-1.0.1.logback.xml -cp "app/*" io.knotx.launcher.LogbackLauncher -conf knotx-standalone-1.0.1.json -cluster
```

Console log:
```
                Deployed e25c5446-0a19-4b76-87c3-cc1cec07e869 [knotx:io.knotx.ServiceKnot]
                Deployed e900595f-2522-4279-82c8-a49b304c1895 [knotx:io.knotx.HttpServiceAdapter]
...                
Members [2] {
        Member [192.168.56.1]:5701
        Member [192.168.56.1]:5702 this
}
```

#### 6. Refresh a few times [Books Page](http://localhost:8092/html/books.html).

Console log (instance-2):
```
2017-05-17 20:30:33 [vert.x-eventloop-thread-2] DEBUG i.k.k.service.impl.FragmentProcessor - Fetching data from service knotx.adapter.service.http {"path":"/books/v1/volumes?q=java"}
```

This is internal LB.