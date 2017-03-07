# Custom Service Adatper with Knot.x

In this tutorial we create a page: [http://localhost:8092/html/books.html](http://localhost:8092/html/books.html) books from database.
Database access is not available via WebAPI thus we have 2 options:

1. integrate with data source implementing WebAPI layer
2. write simple [Knot.x Service Adapter](https://github.com/Cognifide/knotx/wiki/ServiceAdapter)

Before running this example you will need to setup HSQL database.
You may read how to do this in [this tutorial](http://o7planning.org/en/10287/installing-and-configuring-hsqldb-database).
To create database use script provided in `db` folder of this tutorial root.

To run this tutorial you will need java 8 and maven installed.

1. Download [knotx-standalone-1.0.0.fat.jar](https://oss.sonatype.org/content/groups/public/io/knotx/knotx-standalone/1.0.0/knotx-standalone-1.0.0.fat.jar)
to `app` folder in this tutorial root.
2. Build custom service adapter using `mvn clean install` command.
3. Copy `custom-service-adapter-1.0.0-fat.jar` to `app` folder.
4. Use `run.sh` to start Knot.x instance with.