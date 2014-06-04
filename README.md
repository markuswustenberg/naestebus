# Next High Speed Metal Tube

When is the Next High Speed Metal Tube going nearby? Go to www.nexthighspeedmetaltube.com and find out! Works on desktops, mobiles, and tablets.

Sometimes, a High Speed Metal Tube (HSMT) is also—archaically—called a bus. Please disregard this.

Note: This service only works in Denmark. If you're physically located outside Denmark, please go to [www.nexthighspeedmetaltube.com/?position=56.172683,10.186436](http://www.nexthighspeedmetaltube.com/?position=56.172683,10.186436) instead, which positions you at the Uber office in Aarhus, Denmark.

# For developers

[![Build Status](https://travis-ci.org/2yxwx/nexthighspeedmetaltube.svg?branch=master)](https://travis-ci.org/2yxwx/nexthighspeedmetaltube)
[![Build Status](https://travis-ci.org/2yxwx/nexthighspeedmetaltube.svg?branch=develop)](https://travis-ci.org/2yxwx/nexthighspeedmetaltube)

Above: build status for branches `master` and `develop`, respectively.

To build the code yourself, clone the repository and run `./gradlew build` to test and build, and/or `./gradlew jettyRun` to run the web application locally (on [localhost:8080](http://localhost:8080)). Check the javadocs for usage information of the backend. Note that some tests depend on data from a 3rd party web service, which (however unlikely) may unexpectedly have changed its API.

# For reviewers

This section of the readme is for reviewers of the code. I chose the **departure times** project, which at this point in time states:

> Create a service that gives real-time departure time for public transportation (use freely available public API). The app should geolocalize the user.

I've created a service at www.nexthighspeedmetaltube.com that does exactly this (it works in Denmark, as the data source, _Rejseplanen_ , is Danish). The implementation is **full-stack**, although the focus has been on the backend. In the following, I will detail my design goals with the project, as well as describe functionality, code design, testing, and various technical choices.

## Design goals

The overall user interface design goal is that users should be able to get departure information for the next nearby bus _fast_ and with minimal interaction, i.e. favoring simplicity of the product. From a user perspective, the product has very few features:

- The user is geolocalized and this is shown on a map. If geolocalization is wrong or not possible, the user pin can be moved manually.
- Nearby bus stops are also shown on the map when within range, and when clicked upon show the next five departures from that stop.

That's it. The map _is_ the interface, with no option to search for buses or departures—other services exist for that already. Being a web app, it works on desktops, smartphones, tablets, etc. Superfluous map controls have been removed, the mouse (or fingers, for touch interaction) are simply used to move and zoom the map, if that is at all necessary.

### Backend design goals

Again, the overall design goal has been simplicity. By creating a simple system that gets the job done, it is easy to test, understand, and therefore maintain. Components are loosely coupled and flexible, and immutable and thread-safe for ease of scalability. Apart from in-memory caching, no state is saved, which again lends itself to horizontal scaling.

## Process

A professor teaching a software architecture course at my university taught me to _maximize work not done_, i.e. to not spend time on features and abstractions that will never be used. This gets things done. I've used test-driven development (TDD) to force myself to consider how the code will be used before writing it, as well as for creating tested code from the get-go. I've used well-established software patterns where it makes sense, and observed best practices from books such as Effective Java. Git (and git flow) for version control as well as the issue tracker on Github have been used throughout, and Travis CI (www.travic-ci.org) has been used as a build server.

## Technical choices

### Backend

For the backend, I have chosen to work with plain old Java servlets, spiced with some Google Guice for dependency injection and Google GSON for serialization to JSON. I am an experienced Java developer.

Although I was tempted to try out node.js (which I've been wanting to do for some time), I chose to go with a familiar platform, both because I knew this would lead to high quality code, but also because this is a coding challenge for a job interview, and I wanted to show my current skills. One could argue it would be better to show my skills in learning a new language and/or platform, but in the end, I opted for the current solution for the best "return of investment".

It made sense to use the Java language and JVM platform for several reasons:

- The Java language and platform is very mature and well-supported, also in the web world.
- Cloud platforms such as Google App Engine support it, and it is easy to deploy the app onto it. This makes horizontal scalability really easy.
- If wanted, newer languages such as Scala, Groovy, etc. can be run on the JVM also.
- Unit and integration testing is really easy.
- A lot of nice libraries exist for the platform.

I chose not to use a large web framework, as all I really needed was a bit of code to handle requests and responses with a bit of trivial processing, but the rest is delegated to regular Java code. If things were to grow in that department, it would perhaps make sense to include a framework like Stripes, Spring, or Struts, e.g., for handling parameter checks, serialization, and possibly more. But currently, less is more.

Several libraries are used:

- Google Guice for DI and configuration of servlets.
- Google Guava as a general-purpose library that makes life easier.
- Joda-Time for sane date and time handling.
- Google GSON for easy JSON serialization.
- SLF4J and Logback for logging.
- JUnit and Mockito for testing and mocking.

I haven't worked much with Google Guice before, but found it very easy to use. Mockito, which enables easy mocking in test code, was all new to me, and quite enjoyable to use. The rest I have a fair amount of experience with.

The code is built to be pretty much state-less, apart from some caching code. This means that the application scales trivially by adding more machines to run from, perhaps with a load balancer in front.

Apart from some model classes, two main abstractions exist, namely for the data supplier and for the serialization. Two concrete data suppliers currently exist: one that connects to Rejseplanen (_the_ Danish source of travel data) and another that is a decorator that caches requests. The serialization abstraction makes it easy to switch formats as well as unit test the serialization.

The backend has running unit tests and integration tests, which can be run with `./gradlew check`. Note that Gradle currently has a bug that doesn't shut down the integrated Jetty web service after testing, so this has to be done manually after each test run.

### Frontend

For the frontend, I have chosen to work with Javascript (naturally). I have used Javascript previously in a course and a little bit at my job as a webmaster, but not much. I chose to work only with plain Javascript and what the Google Maps API gives me (which is a lot), as well as a tiny bit of jQuery for communication to the backend and some parameter handling. This gives me a minimal and easily maintainable codebase in a ~200 line script.

I chose not to do automatic testing on the frontend, as I chose to concentrate these efforts on the backend.

## Issues, challenges, and future work

As mentioned previously, I concentrated on the backend, so automatic testing of the frontend has been left out. If I were to put more time into the project (which I might, because I plan on actually putting the service in production), some future work is:

- Better caching of data, as to minimize communication to the 3rd party. Bus stops seem trivial to cache because they don't change much, except that their IDs change quite frequently in this case, which is why I haven't done this currently. A spatial data structure such as a k-d tree in two dimensions would serve well here. Also, currently the cached data for departures is kept indefinitely, which of course is a potential memory leak. An easy fix is to let the cached data expire at some point.
- I would really like a feature to favorite stops in a particular area, so that departure times pop up automatically. This would mean that no clicks would be necessary for getting the wanted information, which I think is cool. This could perhaps also be learnt automatically based on usage.
- It would be nice to save usage data for analytics purposes.
- There are some issues with the dependency injection and using decorators, which currently is a bit of a hack (using annnotations for a particular wanted caching DataSupplier).
- Some proper performance testing would be a great idea.

## Epilogue

This has been a really fun challenge! I've built something in a few days that I actually find quite useful, so I'll make it available to the general public soon under the name _Næste Bus_ (which means _next bus_ in Danish) at www.næstebus.dk.
