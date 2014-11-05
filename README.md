Java-Web-applications-Course-Advanced-Web-technologies-and-Services
============


![Alt text](https://github.com/krunogr/Java-Web-applications-Course-Advanced-Web-technologies-and-Services/blob/master/system_schema.JPG "Schema of system")

###The system consists the following applications:
<u>kgrcevic_aplikacija_1</u>

  - Web application runs in background mode thread that takes the current 
meteorological data from the WeatherBug Web service for selected group of USA 
cities. It is necessary to store a minimum of 5 meteorological data (temperature, 
pressure, humidity, wind, etc) in the database.
  - Management with background thread is performed by primitive servers which has a 
role socket server on a particular port.
  - Current meteorological data downloaded via operation 
GetLiveWeatherByUSZipCode with WeatherBugWebServices web service 
http://api.wxbug.net/weatherservice.asmx.
  - Second task of the Web Application has been providing web services for collected 
meteorological data as operation: current meteorological data for selected zip code, 
the ranking list (first n) zip codes which is the most collected meteorological data, the 
last n meteorological data for the selected zip code, meteorological data for the zip 
code in a time interval (from Date, Date to), etc.
  - The third task is the visible part of the web applications and user part should contain 
an overview of the collected meteorological data, an overview of the requirements 
for the server that manages the thread and review the log of user requirements via 
JSP and displaytag-and with the filtering of data (filtering elements: the chosen zip code, time interval (from-to, in the format ddMM.yyyy hh.mm.ss) in which collected 
data, etc.), status and paging (choice of 5, 10, 20, 50, 100, all).
  - Web server: Tomcat, User Interface: JSP, Database: MySQL.


![Alt text](https://github.com/krunogr/Java-Web-applications-Course-Advanced-Web-technologies-and-Services/blob/master/visible_part.JPG "Visible part of application")


kgrcevic_aplikacija_2
  - Enterprise applications which has EJB and Web modules. The application runs in background mode thread that checks the mailbox (server address, username and password are defined in the configuration file) inbox.
  - From the received e-mail messages, messages which has subject "NWTiS" and MIME 
type "text / plain" call NWTiS messages. NWTiS processed messages to be transferred 
to a folder / directory according to the settings for NWTiS messages. Other non-NWTiS messages to be transferred to folder for non-NWTiS messages. At the end of 
each iteration processing of email messages should be sent to a JMS message (queue 
name 1) with data about the time of start and completion of the work, the number of 
read messages, the number NWTiS messages, etc
  - The second task is user part or web module, which should be realized through JSF 
(Facelets) with bilingual (Croatian and English language). Language is selected on the 
home page of application.
  - Web server: Glassfish, User Interface: JSF (facelets), Database: JavaDB

kgrcevic_aplikacija_3
  -  Enterprise applications which has EJB and Web modules. The application takes two 
types of JMS messages (like MessageDriven Bean) for the state of processing email messages and zip code for that should start to take meteorological data on the 
review of data meteorological forecasts.
  - Web application module uses JSF (Facelets) to view the saved JMS message. The user 
can delete selected messages or all messages.
  - Web server: Glassfish, User Interface: JSF (facelets).

Communication between applications:
  - via a web service, email messaging, JMS messaging and language-based socket server.
