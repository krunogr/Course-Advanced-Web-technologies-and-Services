Course-Advanced-Web-technologies-and-Services
============


![Alt text](https://github.com/krunogr/Java-Web-applications-Course-Advanced-Web-technologies-and-Services/blob/master/system_schema.JPG "Schema of system")

The system has following applications:
kgrcevic_aplikacija_1

  - Web application runs in background mode and takes the current 
meteorological data from the WeatherBug Web service for selected group of USA 
cities. It is necessary to store a minimum of 5 meteorological data (temperature, 
pressure, humidity, wind, etc) to the database.
  - Current meteorological data are being downloaded via operation 
GetLiveWeatherByUSZipCode with WeatherBugWebServices web service 
http://api.wxbug.net/weatherservice.asmx.
  - Application is providing web services for collected 
meteorological data like current meteorological data for selected zip codes, meteorological data for the zip code in a time interval (from Date, Date to) and similar.
  - The last part of application is user interface where all collected meteorological data have been showed.
  - Web server: Tomcat, User Interface: JSP, Database: MySQL.

![Alt text](https://github.com/krunogr/Java-Web-applications-Course-Advanced-Web-technologies-and-Services/blob/master/visible_part.JPG "Visible part of application")

######kgrcevic_aplikacija_2
  - Web server: Glassfish, User Interface: JSF (facelets), Database: JavaDB

######kgrcevic_aplikacija_3
  - Web server: Glassfish, User Interface: JSF (facelets).

######Communication between applications:
  - REST, email messaging, JMS messaging and language-based socket server.
