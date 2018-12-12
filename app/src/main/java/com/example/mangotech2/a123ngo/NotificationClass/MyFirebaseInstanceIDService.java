package com.example.mangotech2.a123ngo.NotificationClass;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.widget.Toast;

import com.example.mangotech2.a123ngo.DriverDocuments;
import com.example.mangotech2.a123ngo.Parsing_JSON.UpdateFCMToken;
import com.example.mangotech2.a123ngo.Signin;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * Created by Sameer on 11/28/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
   public static String token;
    String refreshedToken;
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        refreshedToken   = FirebaseInstanceId.getInstance().getToken();

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(refreshedToken);
    }
    @Override
    public void    handleIntent(Intent intent) {
        try{
        super.handleIntent(intent);
        String token=refreshedToken;
            SharedPreferences prefs =getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("FCM_Token", token);
            editor.commit();

         /*   SharedPreferences prefs =getDefaultSharedPreferences(getApplicationContext());
            String access_tokens = prefs.getString("access_token","no token");
            String user_id = prefs.getString("user_id","no user_id");
            Signin.user_id=user_id;
            Signin.access_token=access_tokens;
        if(!token.equals(null)&& !Signin.access_token.equals(null) && !Signin.access_token.equals("no token")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
                new UpdateFCMToken().execute(refreshedToken);
            }
        }*/
        }catch (Exception e){
         //   Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
    }

}
