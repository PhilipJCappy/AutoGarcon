package auto_garcon.cartorderhistory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.auto_garcon.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.ArrayList;

import auto_garcon.accountstuff.*;
import auto_garcon.homestuff.*;
import auto_garcon.accountstuff.Settings;
import auto_garcon.initialpages.Login;
import auto_garcon.initialpages.QRcode;
import auto_garcon.singleton.SharedPreference;
import auto_garcon.singleton.ShoppingCartSingleton;
import auto_garcon.singleton.VolleySingleton;

/**
 * This class pulls data from the database relating to the user's past orders
 * The class is tied to the order history xml and uses nav bars to navigate other xml's
 * This class main function is to allow the user to view previous orders  and also click on the previous orders if they want to order again
 */
public class OrderHistory extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;// for the adapter class to be used as a non final
    private SharedPreference pref;// used to reference user information
    private ArrayList<String> order;// used to capture user order number
    private ArrayList<ShoppingCartSingleton> carts;// used to handle items returned from the recent order history
    private ArrayList<String> date;// used to capture time for all orders
    private ArrayList<Double> prices;
    private ArrayList<String> restaurantName;
    private ArrayList<byte[]> logos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        order = new ArrayList<>();
        date = new ArrayList<>();
        carts = new ArrayList<>();
        prices = new ArrayList<>();
        restaurantName = new ArrayList<>();
        logos = new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        pref = new SharedPreference(this);
        recyclerView = findViewById(R.id.order_list);

        final StringRequest getRequest = new StringRequest(Request.Method.GET, "http://50.19.176.137:8000/customer/history/" + pref.getUser().getUsername(), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if (response.equals("No order history for this customer")) {
                    recyclerView.setVisibility(View.GONE);
                } else {
                    JsonParser parser = new JsonParser();
                    JsonObject json = (JsonObject) parser.parse(response);

                    findViewById(R.id.no_order_history).setVisibility(View.GONE);
                    /*--------------------------------------------------------------*/
                    //parsing through json from get request to add them to menu
                    int tracker = 0;

                    for(int i = 0;i<json.size();i++){
                        if(i!=0){//first item check
                            if(order.get(tracker-1).equals(json.getAsJsonObject(""+i).get("order_num").getAsString())){//if there is an order that has the same id

                                auto_garcon.menustuff.MenuItem item = new auto_garcon.menustuff.MenuItem();// get the item for that order
                                item.setNameOfItem(json.getAsJsonObject("" + i).get("item_name").getAsString());//set the item name
                                item.setQuantity(json.getAsJsonObject("" + i).get("quantity").getAsInt());//set the new item quantity
                                item.setPrice(json.getAsJsonObject(""+i).get("price").getAsDouble());
                                // Log.d("asdff", "" + item.getCost());

                                carts.get(tracker-1).addToCart(item);
                            }
                            else{
                                auto_garcon.menustuff.MenuItem item = new auto_garcon.menustuff.MenuItem();//create the new item
                                item.setNameOfItem(json.getAsJsonObject("" + i).get("item_name").getAsString());//set the item name
                                item.setQuantity(json.getAsJsonObject("" + i).get("quantity").getAsInt());//set the new item quantity
                                item.setPrice(json.getAsJsonObject(""+i).get("price").getAsDouble());
                                order.add(json.getAsJsonObject("" + i).get("order_num").getAsString());//get the new order number and add it to the item arraylsit
                                carts.add(new ShoppingCartSingleton(json.getAsJsonObject("" + i).get("restaurant_id").getAsInt()));//get the new restaurant id and create a new shopping cart
                                carts.get(tracker).addToCart(item);//ad the new item to the cart
                                date.add(json.getAsJsonObject("" + i).get("order_date").getAsString());//add the date
                                restaurantName.add(json.getAsJsonObject(""+i).get("restaurant_name").getAsString());
                                byte[] temp = new byte[json.getAsJsonObject(""+i).getAsJsonObject("logo").getAsJsonArray("data").size()];

                                for(int j = 0; j < temp.length; j++) {
                                    temp[j] = (byte) (((int) json.getAsJsonObject(""+i).getAsJsonObject("logo").getAsJsonArray("data").get(j).getAsInt()) & 0xFF);
                                }
                                logos.add(temp);
                                tracker=tracker+1;
                            }
                        }
                        else {
                            auto_garcon.menustuff.MenuItem item = new auto_garcon.menustuff.MenuItem();
                            item.setNameOfItem(json.getAsJsonObject("" + i).get("item_name").getAsString());
                            item.setQuantity(json.getAsJsonObject("" + i).get("quantity").getAsInt());
                            item.setPrice(json.getAsJsonObject(""+i).get("price").getAsDouble());
                            order.add(json.getAsJsonObject("" + i).get("order_num").getAsString());
                            carts.add(new ShoppingCartSingleton(json.getAsJsonObject("" + i).get("restaurant_id").getAsInt()));
                            carts.get(i).addToCart(item);
                            date.add(json.getAsJsonObject("" + i).get("order_date").getAsString());
                            restaurantName.add(json.getAsJsonObject(""+i).get("restaurant_name").getAsString());
                            byte[] temp = new byte[json.getAsJsonObject(""+i).getAsJsonObject("logo").getAsJsonArray("data").size()];

                            for(int j = 0; j < temp.length; j++) {
                                temp[j] = (byte) (((int) json.getAsJsonObject(""+i).getAsJsonObject("logo").getAsJsonArray("data").get(j).getAsInt()) & 0xFF);
                            }
                            Log.d("123", String.valueOf(json.getAsJsonObject(""+i).getAsJsonObject("logo").getAsJsonArray("data").get(61).getAsInt()));

                            logos.add(temp);

                            tracker=tracker+1;
                        }
                    }


                    OrderHistoryAdapter adapter = new OrderHistoryAdapter(OrderHistory.this,pref,order,carts,date,restaurantName,logos);//values that will be needed to input data into our xml objects that is handled in our adapter class
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(OrderHistory.this));

                    //creating side nav drawer
                    DrawerLayout drawerLayout = findViewById(R.id.order_history_main);// associating xml objects with the java Object equivalent
                    Toolbar toolbar = findViewById(R.id.xml_toolbar);// associating xml objects with the java Object equivalent
                    NavigationView navigationView = findViewById(R.id.navigationView);// associating xml objects with the java Object equivalent
                    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(OrderHistory.this, drawerLayout, toolbar, R.string.drawerOpen, R.string.drawerClose);

                    TextView usernameSideNavBar = navigationView.getHeaderView(0).findViewById(R.id.side_nav_bar_name);
                    usernameSideNavBar.setText(pref.getUser().getUsername());

                    drawerLayout.addDrawerListener(toggle);
                    toggle.syncState();
                    navigationView.setNavigationItemSelectedListener(OrderHistory.this);

                    BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);// associating xml objects with the java Object equivalent

                    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
                            new BottomNavigationView.OnNavigationItemSelectedListener() {
                                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                                    switch (item.getItemId()) {
                                        case R.id.action_scan:
                                            startActivity(new Intent(OrderHistory.this, QRcode.class));
                                            return true;
                                        case R.id.action_home:
                                            startActivity(new Intent(OrderHistory.this, Home.class));
                                            return true;
                                        case R.id.action_cart:
                                            startActivity(new Intent(OrderHistory.this, ShoppingCart.class));
                                            return true;
                                    }
                                    return false;
                                }
                            };

                    bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
                }
            }
        },
                new Response.ErrorListener()

                {
                    @Override
                    public void onErrorResponse (VolleyError error){
                        Toast.makeText(OrderHistory.this, "An Error has Occured", Toast.LENGTH_LONG).show();
                    }
                });

        VolleySingleton.getInstance(OrderHistory.this).addToRequestQueue(getRequest);// sending the request to the database
    }

    //onClick for side nav bar
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem nav_item){
        switch(nav_item.getItemId()){
            case R.id.account:
                startActivity(new Intent(OrderHistory.this, Account.class));
                break;
            case R.id.order_history:
                startActivity(new Intent(OrderHistory.this, OrderHistory.class));
                break;
            case R.id.current_orders:
                startActivity(new Intent(OrderHistory.this, CurrentOrders.class));
                break;
            case R.id.settings:
                startActivity(new Intent(OrderHistory.this, Settings.class));
                break;
            case R.id.log_out:
                pref.changeLogStatus(false);
                pref.logOut();

                startActivity(new Intent(OrderHistory.this, Login.class));
                break;
        }
        return false;
    }
}
