package com.parrot.freeflight.activities;

import android.content.Context;
import android.hardware.SensorManager;
import android.view.Display;
import android.view.WindowManager;

import com.google.vrtoolkit.cardboard.sensors.Clock;
import com.google.vrtoolkit.cardboard.sensors.DeviceSensorLooper;
import com.google.vrtoolkit.cardboard.sensors.HeadTracker;
import com.google.vrtoolkit.cardboard.sensors.SensorEventProvider;
import com.google.vrtoolkit.cardboard.sensors.SystemClock;

/**
 * Created by Rollin Tschirgi on 4/3/16.
 */
public class DroneHeadTracker extends HeadTracker{
    


    public DroneHeadTracker(SensorEventProvider sensorEventProvider, Clock clock, Display display) {
        super(sensorEventProvider, clock, display);
    }


    public static DroneHeadTracker createTrackerFromContext(Context context){
        SensorManager sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        Display display = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        return new DroneHeadTracker(new DeviceSensorLooper(sensorManager), new SystemClock(), display);

    }


    public float[] getEulers(float[] droneVector) {

        float[] headView = new float[16];
        this.getLastHeadView(headView, 0);

        float pitch = (float) Math.asin((double) headView[6]);
        float yaw;
        float roll;
        if (Math.sqrt((double) (1.0F - headView[6] * headView[6])) >= 0.009999999776482582D) {
            yaw = (float) Math.atan2((double) (-headView[2]), (double) headView[10]);
            roll = (float) Math.atan2((double) (-headView[4]), (double) headView[5]);
        } else {

            yaw = 0.0F;
            roll = (float) Math.atan2((double) headView[1], (double) headView[0]);
        }

        /* From ArDrone Documentation:

        Sets the pitch parameter. Range from -1.0 (pitch forward to euler_angle_max radians) to +1.0 (pitch back to euler_angle_max radians). 0.0 means level.
        Defaults at initialisation to 0.0. The new pitch parameter will be sent during the next update() if the drone is flying.*/
        droneVector[0] = -pitch;

        /* From ArDrone Documentation:

        Sets the yaw (rate of change of yaw). Range from -1.0 (rotate left at control_yaw radians/sec) to +1.0 (rotate right at control_yaw radians/sec).
        0.0 means no rotation. Defaults at initialisation to 0.0. The new yaw parameter will be sent during the next update() if the drone is flying.*/
        droneVector[1] = -yaw;

        /* From ArDrone Documentation:

        Sets the roll parameter. Range from -1.0 (roll left to euler_angle_max radians) to +1.0 (roll right to euler_angle_max radians).
        0.0 means level. Defaults at initialisation to 0.0. The new roll parameter will be sent during the next update() if the drone is flying.*/
        droneVector[2] = -roll;

        /* From ArDrone Documentation:

        Sets the gaz (rate of change of altitude). Range from -1.0 (descend at control_vz_max mm/sec) to +1.0 (ascend at control_vz_max mm/sec).
        0.0 means level. Defaults at initialisation to 0.0. The new gaz parameter will be sent during the next update() if the drone is flying.*/
        // TODO: 4/5/16
        droneVector[3]=droneVector[0];
        return droneVector;
    }
}





