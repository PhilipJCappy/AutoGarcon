package auto_garcon.Singleton;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.auto_garcon.R;
import com.google.gson.Gson;

import auto_garcon.Cart_OrderHistory.ShoppingCart;

public class PreferenceSingleton {
    private SharedPreferences sharedPreferences;
    private Context context;


    public PreferenceSingleton(Context context){
        this.context=context;

        sharedPreferences= context.getSharedPreferences(context.getString(R.string.pref_file),Context.MODE_PRIVATE);
    }

    public void changeLogStatus(boolean status){
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //editing prefrence file and changing login stauts to either false or true
        editor.putBoolean(context.getString(R.string.pref_login_status),status);
        //how you save???
        editor.commit();
    }
    public  boolean getLoginStatus(){

        // gets the loginstatus from prefrence file
        return sharedPreferences.getBoolean(context.getString(R.string.pref_login_status), false);
    }

    public void writeName(String name){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // we take in the name given and store it in our prefrence file
        editor.putString(context.getString(R.string.pref_user_name),name);
        //save
        editor.commit();
    }
    public String getName(){
        // returns the user logged in at the moment else returns the string user
        return  sharedPreferences.getString(context.getString(R.string.pref_user_name),"User");
    }
    public String getAuth(){

        return sharedPreferences.getString(context.getString(R.string.pref_auth_token),"Token");
    }
    public  void setAuthToken(String authToken){
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(context.getString(R.string.pref_auth_token),authToken);
        editor.commit();
    }
    public ShoppingCartSingleton getShoppingCart(){
        // this gson object will be used to convert our json string into a java object
        Gson gson = new Gson();
        //here we extract the json String from our sharedPrefrence file
        String stringJson  =sharedPreferences.getString("com.example_preference_Shopping_cart","Object");

        //here we convert the json String to our Java Object
        ShoppingCartSingleton toBeReturned = gson.fromJson(stringJson,ShoppingCartSingleton.class);
        return  toBeReturned;
    }

    public void setShoppingCart(ShoppingCartSingleton shoppingCart){

        SharedPreferences.Editor editor = sharedPreferences.edit();

        //this gson object is used to convert our java object into a json String
        Gson gson = new Gson();

        String stringJson =  gson.toJson(shoppingCart);

        // here we write the json object that represents our java object into our shared prefrence file
        editor.putString("Object",stringJson);
        //save edits
        editor.commit();

    }
}
