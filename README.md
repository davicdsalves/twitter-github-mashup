Create a simple command line (ascii text) API mashup of GitHub and Twitter APIs. 

Search for "Reactive" projects on GitHub, then for each project search for tweets that mention it.

You should output a summary of each project with a short list of recent tweets, in JSON
format.

Building project:
```
mvn clean package
```
Running project, replace APP-ID and APP-SECRET with credentials: 
```
java -DTWITTER-APP-ID={APP-ID} -DTWITTER-APP-SECRET={APP-SECRET} -jar target/challenge-1.0.0-SNAPSHOT.jar
``` 
You can also build the project using docker:
```
mvn clean package docker:build -DdockerImageTags=latest 
```
And running the image(you can already run it since it's on dockerhub):
```
docker run -e TWITTER-APP-ID={APP-ID} -e TWITTER-APP-SECRET={APP-SECRET} davicdsalves/twitter-github-mashup:latest
```

You can also use additional env variables like:

KEYWORD: keyword to find projects on github (default: reactive).

MAX-PROJECTS: max number of projects to search on twitter (default: 10).

MAX-TWEETS: max number of tweets to search (default: 15).

RANDOM-REPOS: randomize repositories from github response (default: true).