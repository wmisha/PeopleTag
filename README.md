# Personshelf App for Java on App Engine Standard Tutorial 
## Auth

Contains the code for using Cloud Datastore and Cloud SQL v2.

This is a modified version of a [Bookshelf tutorial](https://cloud.google.com/java/getting-started/tutorial-app). It
allows creation of person objects with images, and allows users to login to google account,
and shows persons that each user entered (or all)

To run:

1. Change the “personshelf.bucket” setting in the pom.xml file so that your app will know where images are. Replace “dwpersonshelf.appspot.com” with what you are going to name your app. So if you are going to name your app zzpersonshelf, replace “dwpersonshelf” with “zzpersonshelf” in the line below: 

<personshelf.bucket>dwpersonshelf.appspot.com</personshelf.bucket> <!-- eg project-id.appspot.com -->

2. Deploy the app using the following command (instead of mvn appengine:deploy)

  mvn appengine:update -Dappengine.appId=[YOUR-PROJECT-ID]    
  -Dappengine.version=[YOUR-VERSION]

example
mvn appengine:update -Dappengine.appId=dwpersonshelf -Dappengine.version=1

Test by going to yourapp.appspot.com

Deploy before running locally because this will setup a bucket in which your images will be stored, even
for localhost.


3. Run locally. Because this app stores images, you’ll need to do things a bit differently. At a command-line terminal:


Authorize yourself:


gcloud auth application-default login


Instead of mvn appengine:run, do:

mvn appengine:devserver-Dpersonshelf.bucket=your app URL  e..g,

		mvn appengine:deverserver -Dpersonshelf.bucket=zzpersonshelf.appspot.com

Test by going to localhost:8080

