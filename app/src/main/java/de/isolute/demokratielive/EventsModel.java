package de.isolute.demokratielive;

import org.json.JSONObject;

/**
 * Created by aprang on 04.05.16.
 */
public class EventsModel {
    private static EventsModel instance;

    public static JSONObject stations;
    public static MainActivity mainActivity;

    public static void initInstance()
    {
        if (instance == null)
        {
            // Create the instance
            instance = new EventsModel();
        }
    }

    public static EventsModel getInstance()
    {
        if (instance == null)
        {
            // Create the instance
            instance = new EventsModel();
            instance.loadData();
        }

        return instance;
    }

    private EventsModel()
    {
        // Constructor hidden because this is a singleton
    }

    public Integer numberOfStations()
    {
/*        if (stations != null) {
            return 2;//stations.length();
        } else {
            return 0;
        }*/
        return 2;
    }

    public void loadData() {
        new RetrieveJsonTask().execute("http://demokratielive.org/API/Apps/events.php");
    }

}
