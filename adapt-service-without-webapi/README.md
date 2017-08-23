# Adapt service without Web API

In this tutorial we create a page: [http://localhost:8092/content/local/books.html](http://localhost:8092/content/local/books.html) 
displaying books and authors that are available in a relational database.

Database access is not available via WebAPI thus we have 2 options:

1. Implement a Web API layer to access the database and then integrate with it using e.g. AJAX or an [HTTP adapter](http://knotx.io/blog/hello-rest-service/).
2. Implement a _Knot.x_ [_Service Adapter_](https://github.com/Cognifide/knotx/wiki/ServiceAdapter).

For the demonstration purposes we're going to use HSQL database in this example.

Follow [this tutorial](http://o7planning.org/en/10287/installing-and-configuring-hsqldb-database)
in order to set up the database.
To create tables with data use the script provided in `db` folder of this tutorial.

To run this tutorial you will need Java 8 and Maven installed.

1. Download [`knotx-standalone-1.1.1.fat.jar`](https://oss.sonatype.org/content/groups/public/io/knotx/knotx-standalone/1.1.1/knotx-standalone-1.1.1.fat.jar)
to `app` folder in this tutorial root.
2. Build custom service adapter using `mvn clean install` command.
3. Copy `custom-service-adapter-1.1.1-fat.jar` (from `target` folder after successful maven build) 
to `app` folder.
4. Use `run.sh` to start Knot.x instance with custom adapter.