# The-Rolla-Mission-App
Projects for CS 4096 (Spring 2020)

To run the app, open its `build.gradle` file in android studio. For the app to be functional, it must connect to the companion Django web app. 

## Configure Android app to work with Django app
1. Ensure that the Django app is running locally (follow the instructions at https://github.com/Thomas-McKanna/TheRollaMissionDjango).
2. Take note of your IP address (https://www.howtogeek.com/233952/how-to-find-your-routers-ip-address-on-any-computer-smartphone-or-tablet/).
3. Change app/java/com.project.therollamissionapp/di/AppModule so that the API_BASE_URL uses your IP address.
4. In the root of the project, make a file called secret.properties and populate it like this:
```
API_USER="<YOU DECIDE THE USERNAME>"
API_PASS="<YOU DECIDE THE PASSWORD>"
```
5. In the Django app, go to the admin interface and add a user with the credentials you chose above.
