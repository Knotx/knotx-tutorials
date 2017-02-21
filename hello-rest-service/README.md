# Hello rest service with Knot.x

In this tutorial we create a page: [http://localhost:8092/content/books.html](http://localhost:8092/content/books.html) that lists Java related books.
We use [Knot.x](http://knotx.io) in order to dynamically inject list of books  returned by 
[Googleapis books service](https://www.googleapis.com/books/v1/volumes?q=java).

To run this tutorial download [knotx-standalone-1.0.0.fat.jar](https://oss.sonatype.org/content/groups/public/io/knotx/knotx-standalone/1.0.0/knotx-standalone-1.0.0.fat.jar) to tutorial root folder.
Use `run.sh` to start Knot.x instance.