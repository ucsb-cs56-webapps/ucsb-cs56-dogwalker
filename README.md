# ucsb-cs56-dogwalker
Dog walker/playdate matcher

make a .env with ur mlab stuff then do . .env before compiling
USE JAVA8

Live here :  https://dogwalker.herokuapp.com/


Future Ideas
 
 show multiple posts for the same username and password
 pick which post to delete instead of deleting all 
 upload images and display them
 disassociate a users profile from posts
 

# Getting it to run: setting up env.sh

To run, the usual steps of `mvn compile exec:java` then visiting `http://localhost:4567` are a good start.

But, you'll get this error message:

```
Error: Must define env variable MONGODB_USER
Error: Must define env variable MONGODB_PASS
Error: Must define env variable MONGODB_NAME
Error: Must define env variable MONGODB_HOST
Error: Must define env variable MONGODB_PORT
Phillips-Mac-mini:sparkjava-rest-mlab-frontend pconrad$ 
```

To fix this, you need to take the following steps.  Note that the steps involving [mlab.com](https://mlab.com/) are pretty self-explanatory if you go to their website, so I'm not including much detail.  You'll figure it out.
1. Create a free account at [mlab.com](https://mlab.com/)
2. Create a deployment on the free tier
3. Create a database.  Call it whatever you like; perhaps `mlab-blog-demo` for example.
4. In that database, create two collections, initially empty
   * a collection called `posts`
   * a collection called `counters`
5. In the `counters` collection, create a document with exactly this content:
   ```json
   {
    "_id": "postId",
    "seq": 0
   }
   ```
6. Keep your [mlab.com](https://mlab.com/) window open; you'll need it.  But now turn back to the command line where you cloned this repo.  You'll see a file called `env.sh.EXAMPLE`.  Copy it to `env.sh`
   ```
   cp env.sh.EXAMPLE env.sh
   ```
7. Edit the env.sh file.  The values in it are just example values.  You'll need to change them as indicated in the next steps.  For each step, you'll get some piece of information from the [mlab.com](https://mlab.com/) window, so arrange your windows side by side where you can see them both.

   Go to the [mlab.com](https://mlab.com/) window and navigate to the page for your database.  If you called it `mlab-blog-demo`, for
   example, that page will have the URL `https://mlab.com/databases/mlab-blog-demo` and it will information like this at the top (this is just an example)
   
   ```
   To connect using the mongo shell:
     mongo ds143932.mlab.com:43932/cs56-m18-demo -u <dbuser> -p <dbpassword>
   To connect using a driver via the standard MongoDB URI (what's this?):
     mongodb://<dbuser>:<dbpassword>@ds143932.mlab.com:43932/cs56-m18-demo
   ```
   You should also see tabs for Collections, Users, Stats, Backups and Tools.

8. Now, open up env.sh for editing.  The first two lines say:
   ```
   export MONGODB_USER=testuser
   export MONGODB_PASS=abcd1234
   ```
   DO NOT CHANGE THESE TO THE USERNAME AND PASSWORD YOU USED TO LOGIN TO [mlab.com](https://mlab.com/)!!! These are a different
   user and password, that you are going to create right now in the [mlab.com](https://mlab.com/) window.
   
   In your file, create a username (Literally using `testuser` is fine).  For password, make up a good long random password, such as
   `8sfvlSFE13RGDG2`.  The longer and more random the better, because you are never going to have to remember or type in this password;
   You are going to enter it once in this file; then copy and paste it into MLab when you create the user/password, and then never have
   to type it again.  Please DON'T literally use `abcd1234` or `8sfvlSFE13RGDG2`.
   
   Type it in the `env.sh` file first.  Then click the "Users" tab, and look over to the right side of the screen for the "Add database user" button.  Click it, and enter the username and password that you just created (e.g. `testuser` and `8sfvlSFE13RGDG2`.  You'll want to copy/paste the password since you have to type it twice.)
   
   Now, we'll move on to the other values in the `env.sh` file.
   
9. For these values, you are going to find these on the Mlab screen for your database:
   
  * For `MONGODB_NAME` change it from `cs56-m18-demo` to whatever the name of your database is (e.g. `mlab-blog-demo`)
  * For `MONGODB_HOST` and `MONGO_PORT` find the thing that says:
      ``` 
      To connect using the mongo shell:
        mongo ds144023.mlab.com:47245/cs56-m18-demo -u <dbuser> -p <dbpassword>
      ```
      In this example, `MONGODB_HOST` should be `ds144023.mlab.com` and `MONGODB_PORT` should be `47245`.
      
10. Once you've made these edits, you need to type the following so that these environment variables take effect:
   ```
   . env.sh
   ```
   This sets up the environment variables that the Java code will read from.
   
   While the previous steps 1-9 are "one time only" steps, this final step must be done each time you log in to a term Unix terminal
   session; the environment variable are defined as part of the current process.
   
Once you've done these steps, you should be able to run and not see the error message about defining environment variables.

```
Error: Must define env variable MONGODB_USER
etc..
```

So try doing `mvn compile exec:java` again, and visiting `http://localhost:4567`
      
# More detail

This code shows a way to use:
* Lombok (<https://projectlombok.org>) to reduce the Java boilerplate you need for pure data classes.
   * Basically, you put `@lombok.Data` on your class, and then you don't need to write constructors, getters,
      setters, `toString`, `equals`, `hashCode`, etc.   Lombok does it for you.
   * Note that this feature is coming in later versions of Java (though use of Java beyond Java 8 in the real
      world is still limited, as of Summer 2018.   Java 8 is the "long-term-support" version; Java 11 is only
      just about to become that later in 2018.)
* Jackson and Gson to convert data to/from JSON automatically
* Building a RESTful API (one that "speaks in JSON") using SparkJava


# Modifications from the original [reducing-java-boilerplate](http://sparkjava.com/tutorials/reducing-java-boilerplate) tutorial:

* The original is all in one `.java` source file.  I broke it up.
   * All one source file is convenient for a  quick demo example.
   * It is NOT intended as an example of good practice.
* The original isn't set up to be conveniently deployable on Heroku
   * I added the port number settings, a `Procfile`, and the Maven code in the `pom.xml` to enable `mvn heroku:deploy`
* The original uses `Map` instead of `Map<Integer,Post>`, for example, which triggers deprecation warnings, and in one case, a compiler fatal error.
   * I modified the code to remove these issues.
   
# Testing the RESTful API:

Since this app is a RESTFUL api that "speaks JSON", you'll need to use special techniques to test it.

The original tutorial shows testing it with a Chrome extension called Postman, but that Chrome extension
appears to be deprecated, and the replacements for it are heavyweight, and require giving access to your
Google account, etc. to unknown parties.  I'd suggest using a different approach.

Here's a tutorial that shows how to test it with plain old curl at the CLI (e.g. the command line on CSIL):

* [Test a REST API with curl](https://www.baeldung.com/curl-rest)

And here's an example that shows how to do the tests originally shown in the tutorial with Postman, but
using curl.

Before we start: let's acknowledge a possible confusion between:
* `POST`, all caps, which is an HTTP method type (`GET` vs. `POST` vs. `PUT`, vs. `DELETE` etc.)
* post, which is an example of a single message on a blog (i.e. a blog post).

Those words are both spelled p-o-s-t, but they are entirely separately concepts.  I'll use `POST` when I mean
the http method, and "post" when I just mean one of the messages on the blog, or the "object" that represents
one of those messages.

Now let's get started. Start the application running on `localhost:4567` in one terminal window using `mvn compile exec:java`.  In a second terminal window, use `curl http://localhost:4567/posts`.   This does a simple `GET` http request to the server, and what is returned in the JSON representation of all the posts currently stored on the server.  


```
$ curl http://localhost:4567/posts
[ ]$ 
```

At the moment, that's an empty list, which in JSON is represneted as `[ ]`.

So if we want some posts in the list, we'll need to add some.

If you `cd` into the directory `testdata`, you'll see that I've created some files that represent
blog posts formatted in JSON.  For example, the contents of the file `post1.json` is this:

```json
{
    "title" : "A post about Spark",
    "content" : "Spark is quite cool!",
    "categories" : ["java","web apps"]
}
```

The curl command can be used with the `-d` option (which stands for data) to do a `POST` request to add this post to the blog.  Here's what that looks like.    This sends a `POST` request to the url, with the payload (content) being the contents of the file `post1.json`:

```
$ curl -d @post1.json http://localhost:4567/posts
1$
```

A few notes about that:

* The response from the server was just the integer `1`; you can see that response before the `$` which is the CLI prompt.  The server responded with the `id` of the object we created.

If we repeat this command a few times, we get `2`, `3`, `4` etc.:

```
$ curl -d @post1.json  http://localhost:4567/posts
3$ curl -d @post1.json  http://localhost:4567/posts
4$
```

If we then simply use `curl http://localhost:4567/posts` again, we get a list of all of these posts formatted in JSON:

```
$ curl http://localhost:4567/posts
[ {
  "id" : 1,
  "title" : "A post about Spark",
  "categories" : [ "java", "web apps" ],
  "content" : "Spark is quite cool!"
}, {
  "id" : 2,
  "title" : "A post about Spark",
  "categories" : [ "java", "web apps" ],
  "content" : "Spark is quite cool!"
}, {
  "id" : 3,
  "title" : "A post about Spark",
  "categories" : [ "java", "web apps" ],
  "content" : "Spark is quite cool!"
}, {
  "id" : 4,
  "title" : "A post about Spark",
  "categories" : [ "java", "web apps" ],
  "content" : "Spark is quite cool!"
} ] $
```

If we stop and restart the webapp, we will see that since this list is just in memory, and not in a database, it does not persist (i.e. stick around).   If we want that, we need to store it in a database with each operation.

So after stopping and restarting the server, once again, we have an empty list:

```
$ curl http://localhost:4567/posts
[ ]$ 
```

To test methods other than `GET` and `POST`, use the `-X` flag.  For example, to
test `DELETE` method, you can use:

```
$ curl -X DELETE http://localhost:4567/posts/13
```


# How to compile and run

| To do this | Do this |
| -----------|-----------|
| run the program | Type `mvn exec:java`.  Visit the web page it indicates in the message |
| check that edits to the pom.xml file are valid | Type `mvn validate` |
| clean up so you can recompile everything  | Type `mvn clean` |
| edit the source code for the app | edit files in `src/main/java`.<br>Under that the directories for the package are `edu/ucsb/cs56/pconrad`  |
| edit the source code for the app | edit files in `src/test/java`.<br>Under that the directories for the package are `edu/ucsb/cs56/pconrad`  |
| compile    | Type `mvn compile` |
| run junit tests | Type `mvn test` |
| build the website, including javadoc | Type `mvn site-deploy` then look in either `target/site/apidocs/index.html`  |
| copy the website to `/docs` for publishing via github-pages | Type `mvn site-deploy` then look for javadoc in `docs/apidocs/index.html` |	
| make a jar file | Type `mvn package` and look in `target/*.jar` |

| run the main in the jar file | Type `java -jar target/sparkjava-demo-01-1.0-jar-with-dependencies.jar ` |
| change which main gets run by the jar | Edit the `<mainClass>` element in `pom.xml` |
| deploy to heroku | change the `<appname>` element and the name of the jar file in both pom.xml and Procfile, then use `heroku login`, then `mvn heroku:deploy` |
