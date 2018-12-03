# Part-timer

<table>
    <thead>
        <tr>
            <th colspan="3">Contributers</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td><b>Name</b></td>
            <td><b>Banner ID</b></td>
   	<td><b>Email ID</b></td>
        </tr>
  <tr>  
        <td>Abraham Vineel Katikala</td>
        <td> B00793815</td>
        <td>ab760856@dal.ca</td>
    </tr>
        <tr>  
        <td>Albertus Brink</td>
        <td>B00584349</td>
        <td>al663745@dal.ca</td>
    </tr>
      <tr>  
        <td>Dinesh Kuncham</td>
        <td>B00801162</td>
        <td>dinesh.kuncham@dal.ca</td>
    </tr>
      <tr>  
        <td>Mohan Pathania</td>
        <td>B00813435</td>
        <td>mohanp@dal.ca</td>
    </tr>
      <tr>  
        <td>Vamsi Krishna Reddy Dwarsala</td>
        <td>B00800531</td>
        <td>vm420113@dal.ca</td>
    </tr>
    </tbody>
</table>

GitHub Repo Link -  [Click here](https://github.com/albrink92/part-timer)

# Project Summary

We have developed an application that allows international students who work part-time to track their work hours reliably. The application’s main purpose is to log user’s entry and exit times at a specific geological location and to track how much time the user spent at that location. For international students, there is a limit on the number of work-hours per week. So our app makes sure that users are aware of how many hours they have worked for so that they do not cross that time limit. The app assists users in planning their time according to their university and work schedules. It also calculates the user's expected pay based on the number of hours they spend at work, which will be useful for managing their personal budget. The user can also manually add/edit the logged work-hours. 


## Libraries

**Google Maps geofencing API:** geofencing defines perimeters and gives notifications when a user crosses that perimeter. It is for us the track user’s entry/exit at a set location.
Source [here](https://developers.google.com/location-context/geofencing/)

**Room Persistence Library** 
*The Room persistence library provides an abstraction layer over SQLite to allow for more robust database access while harnessing the full power of SQLite. The library helps you create a cache of your app's data on a device that's running your app. This cache, which serves as your app's single source of truth, allows users to view a consistent copy of key information within your app, regardless of whether users have an internet connection.*
Source [here](https://developer.android.com/topic/libraries/architecture/room)

## Installation Notes
This app has to be installed on the phone as it will not work in the emulator. The reason being that the Geofencing API used in the app does not support emulators. On the phone, it is installed like any other Android package.

## Code Examples
You will encounter roadblocks and problems while developing your project. Share 2-3 'problems' that your team solved while developing your project. Write a few sentences that describe your solution and provide a code snippet/block that shows your solution. Example:

**Problem 1: Running database queries on main thread caused a little delay in app**

We used AsyncTask to leverage multi-threading option of Android and run the database queries on a secondary thread.


```
// The method we implemented that solved our problem
AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        logData.setTracking(false);
                        logData.setPlaceID(selectedLocation);
                        appDatabase.logDataDaoModel().insert(logData);
                    }
                })
```
**Problem 2: Tracking user location using inbuilt GPS drained battery**

Using android LocationServices resulted in low power efficiency and poor GPS accuracy so we had to find another solution. The answer was the Geofencing - a feature also offered in Android Studio which more accurately user tracked location, preserved more battery power, and worked even when the app was not running. 
```
// The method we implemented that solved our problem
@NonNull
    private GeofencingRequest geofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(mContext, GeofenceTransitionsIntentServices.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling addgeoFences()
        mGeofencePendingIntent = PendingIntent.getService(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return mGeofencePendingIntent;
    }
  ```


## Feature Section


### Seamless work hour tracking
Part-Timer seamlessly tracks users part-time work hours using their device’s built-in GPS system, wifi connection, and accelerometers to enable a function called Geofencing. Geofencing allows a users device to log their potential work hours even when Part-Timer is not running, ensuring that they never forget to track their hours. 


### Overture Prevention
Users will be kept up to date about their hours worked during the current week at all times with information on the home and stats page of Part-Timer to ensure that they do not exceed their strict 20 work-hour a week limit.


### Pay calculation
Users will be able to see roughly how much they have earned during the current week based on their hours worked and hourly rate. 


### Visualized Data
An overview of hours worked in the past weeks and months will be displayed in the statistics page of Part-Timer to allow users to better understand their work patterns so that they can plan for the future and understand the past.


### Control over data
As data will be stored locally on user’s devices, they will have complete control over it. Users will be able to edit and delete existing entries as well create entirely new entries manually if they wish. This allows users to rest easy knowing that their location and work-hour information is completely within their hands. 


## Final Project Status

As it stands, Part-Timer is functional though the UI needs further refinement. We achieved the majority of our functionality goals - our application successfully tracks the users location and logs time spent at designated work locations. Statistics about these tracked hours are displayed within the application, and the primary display alerting users to their remaining work hours this week has also been implemented. 

If we were to continue work, the first step would be to settle on a unifying design, as it would be the biggest hindrance to our success if we were to publish Part-Timer now. A sleek feel and unifying aesthetic are critical to success in today’s app market. 

Further development would see us begin to integrate calendars, add suggested bus routes, support multiple workplaces, and generate additional statistics. 


#### Minimum Functionality
- Log check-in time at a location (Completed)
- Log check-out time of a location (Completed)
- Calculate hours ‘worked’ at a location (Completed)

#### Expected Functionality
- Track hours worked (Completed)
- Users informed/notified about tracked work-hours (Completed)
- Show number of hours worked this day/week/month (Completed)
- Add, edit, and delete data (Completed)
- Show estimated pay for this week (Completed)

#### Bonus Functionality
- Edit check-in time and check-out time (Implemented)
- Option to change time format (Implemented) 
- Show suggested bus routes (Not Implemented)
- Show additional tips (Not Implemented)
- Calculate and show additional statistics (Not Implemented)
- Update estimated time of arrival on the fly (Not Implemented)
- Alert user about upcoming check-in time (Not Implemented)
- Adding additional work locations (Not Implemented)


## Sources
- Design guides [Prototype and ClickStream tool](https://proto.io/)
- Research material - [Android Developer Guides](https://developer.android.com/guide/)
- Android libraries: [Room Persistence Library](https://developer.android.com/topic/libraries/architecture/room), Multi-threading [AsyncTask](https://developer.android.com/reference/android/os/AsyncTask)

