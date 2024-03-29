package com.openclassrooms.realestatemanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Philippe on 21/02/2018.
 */

public class Utils {

    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     * @param dollars
     * @return
     */
    public static int convertDollarToEuro(int dollars) {
        return (int) Math.round(dollars * 0.812);
    }

    public static int convertEuroToDollar(int euros) {
        return (int) Math.round(euros * 1.12);
    }

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     * @return
     */
    public static String getTodayDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(date);
    }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     * @param context
     * @return
     */
    public static Boolean isInternetAvailable(Context context) {

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }


    /**
     * Get latitude and longitude object from a latLng address
     * And save city name to shared preferences
     *
     * @param context
     * @param strAddress
     * @return
     */

    public static LatLng getLatLngFromAddress(Context context, String strAddress, RelativeLayout loadingPanel) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng latLng = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address.size() > 0) {
                Address location = address.get(0);

                latLng = new LatLng(location.getLatitude(), location.getLongitude());

            } else {
                return null;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return latLng;
    }

    public static String formateDateForDatabase(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }

    /**
     * Check that date is before today date
     */

    public static boolean checkBeforeToday(int soldDay, int soldMonth, int soldYear) {

        boolean isBefore = false;

        Calendar today = Calendar.getInstance();
        int todayDay = today.get(Calendar.DAY_OF_MONTH);
        int todayMonth = today.get(Calendar.MONTH);
        int todayYear = today.get(Calendar.YEAR);

        if (soldYear < todayYear) {
            isBefore = true;
        } else if (soldYear == todayYear && soldMonth < todayMonth) {
            isBefore = true;
        } else if (soldYear == todayYear && soldMonth == todayMonth && soldDay <= todayDay) {
            isBefore = true;
        }
        return isBefore;
    }

    public static String removeSpacesAndAccentLetters(String input) {
        String output;
        output = input.trim();
        output = Normalizer
                .normalize(output, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
        return output;
    }
}
