# Next High Speed Metal Tube

When is the Next High Speed Metal Tube going nearby? Go to www.nexthighspeedmetaltube.com and find out! Works on desktops, mobiles, and tablets.

Sometimes, a High Speed Metal Tube (HSMT) is also—archaically—called a bus. Please disregard this.

# For developers

Build status for branches `master` and `develop`, respectively:
[![Build Status](https://travis-ci.org/2yxwx/nexthighspeedmetaltube.svg?branch=master)](https://travis-ci.org/2yxwx/nexthighspeedmetaltube)
[![Build Status](https://travis-ci.org/2yxwx/nexthighspeedmetaltube.svg?branch=develop)](https://travis-ci.org/2yxwx/nexthighspeedmetaltube)

To build the code yourself, clone the repository and run `./gradlew build` to test and build, and/or `./gradlew jettyRun` to run the web application locally (on http://localhost:8080). Check the javadocs for usage information. Note that some tests depend on data from a 3rd party web service, 

# For reviewers

This section of the readme is for reviewers of the code. I chose the **departure times** project, which at this point in time states:

> Create a service that gives real-time departure time for public transportation (use freely available public API). The app should geolocalize the user.

I've created a service at www.nexthighspeedmetaltube.com that does exactly this (it works in Denmark, as the data source is Danish). The implementation is **full-stack**, although the focus has been on the backend. In the following, I will detail my design goals with the project, as well as describe functionality, code design, testing, and various technical choices.

## Design goals

The overall user interface design goal is that users should be able to get departure information for the next nearby bus _fast_ and with minimal interaction, i.e. favoring simplicity of the product. From a user perspective, the product has very few features:

- The user is geolocalized and this is shown on a map. If geolocalization is wrong or not possible, the user pin can be moved manually.
- Nearby bus stops are also shown on the map, and when clicked upon show the next five departures from that stop.

That's it. The map _is_ the interface, with no option to search for buses or departures—other services exist for that already. Being a web app, it works on desktops, smartphones, tablets, etc. Superfluous map controls have been removed, the mouse (or fingers, for touch interaction) are simply used to move and zoom the map, if that is at all necessary.

### Backend design goals

Again, the overall design goal has been simplicity. By creating a simple system that gets the job done, it is easy to test, understand, and therefore maintain. Components are loosely coupled and flexible, and immutable and thread-safe for ease of scalability.

## Process

A professor teaching a software architecture course at my university taught me to _maximize work not done_, i.e. to not spend time on features and abstractions that will never be used. This gets things done. I've used test-driven development (TDD) to force myself to consider how the code will be used before writing it, as well as for creating tested code from the get-go. I've used well-established software patterns where it makes sense, and observed best practices from books such as Effective Java. Git (and git flow) for version control as well as the issue tracker on Github have been used throughout, and Travis CI (www.travic-ci.org) has been used as a build server.

## Technical choices

> Reasoning behind your technical choices (and level of experience with those).

### General

No real frameworks, lots of useful little libraries.

### Backend

### Frontend

## Issues, challenges, and future work

> Trade-offs you might have made, anything you left out, or what you might do differently if you were to spend additional time on the project.

- Caching of stops with data structure that allows fast spatial search (e.g. k-d tree)
- Favorite stops in area
- Testing of servlets directly instead of through jetty
- Analytics for usage, both frontend and backend
- Performance testing
- Checking of parameters in servlets is suboptimal, but a whole framework just for that seemed wrong
