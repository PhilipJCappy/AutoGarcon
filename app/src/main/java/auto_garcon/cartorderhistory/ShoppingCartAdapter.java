package auto_garcon.cartorderhistory;

import android.app.Dialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.auto_garcon.R;

import java.util.ArrayList;

import auto_garcon.menustuff.MenuItem;
import auto_garcon.singleton.SharedPreference;
import auto_garcon.singleton.ShoppingCartSingleton;
/**
 *The class represents the format for how are our shopping list will act and work
 * the class also let the user allow change a quantity of each menu, and remove each menu.
 */
public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ShoppingCartViewHolder>{
    //data fields
    private ArrayList<MenuItem> menuItemArrayList;
    private Context context;
    private SharedPreference preference;
    private ShoppingCartSingleton cart;
    //A constructor to listen the user actions (add, decrement, and remove)
    public boolean isPlaced;
    private boolean isChanged;
    private Dialog menuItemEditPopup;
    /**
     * This constructor initializes our variables passed in from the shopping cart page
     * @param context
     * @param items
     */
    public ShoppingCartAdapter(Context context, ArrayList<MenuItem> items) {
        this.context = context;
        this.menuItemArrayList = items;
        this.preference = new SharedPreference(context);
        this.isPlaced = false;
    }
    @NonNull
    @Override
    public ShoppingCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);//this allows the list expand dynamically
        View view = inflater.inflate(R.layout.shopping_cart_tile,parent,false);//make the list visible

        return new ShoppingCartViewHolder(view);//set visibility on the ShoppingCart
    }

    @Override
    public void onBindViewHolder(@NonNull final ShoppingCartAdapter.ShoppingCartViewHolder holder, final int position) {
        cart = preference.getShoppingCart();
        //Creating a view of a menu item specified by each position.
        //Set the cost and the quantity to the view.
        holder.itemImage.setImageBitmap(BitmapFactory.decodeByteArray(menuItemArrayList.get(position).getItemImage(), 0, menuItemArrayList.get(position).getItemImage().length));

        holder.name.setText(menuItemArrayList.get(position).getNameOfItem());
        menuItemArrayList.get(position).setCost();
        holder.price.setText(String.format("$%.02f", menuItemArrayList.get(position).getCost()));
        holder.quantity.setText("Qty(" + menuItemArrayList.get(position).getQuantity() + ")");
        //If the user pushes the add button on the item view, then the action is taken.
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( isPlaced ){
                    setIsChanged(true);
                    Toast.makeText(context, "Changed menu",Toast.LENGTH_LONG).show();
                }
                //Getting a item that the user pushed the add button
                //Incrementing the quantity and recalculating the total cost of the item.
                menuItemArrayList.get(position).incrementQuantity();
                menuItemArrayList.get(position).setCost();

                //Set its view again to show the updated quantity.
                holder.quantity.setText("Qty(" + menuItemArrayList.get(position).getQuantity() + ")");
                holder.price.setText(String.format("$%.02f", menuItemArrayList.get(position).getCost()));
                cart.cartContainsItem(menuItemArrayList.get(position)).incrementQuantity();
                preference.setShoppingCart(cart);
            }
        });
        //If the user pushes the remove button on the item view, then the action is taken.
        holder.remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if( isPlaced ){
                            setIsChanged(true);
                            Toast.makeText(context, "Changed menu",Toast.LENGTH_LONG).show();
                        }
                        //Getting a item that the user pushed the add button
                        //Decrementing the quantity and recalculating the total cost of the item.
                        if(menuItemArrayList.get(position).getQuantity() > 1){
                            menuItemArrayList.get(position).decrementQuantity();
                            menuItemArrayList.get(position).setCost();
                            //Set its view again to show the updated quantity.
                            holder.quantity.setText("Qty(" + menuItemArrayList.get(position).getQuantity() + ")");
                            holder.price.setText(String.format("$%.02f", menuItemArrayList.get(position).getCost()));
                            cart.cartContainsItem(menuItemArrayList.get(position)).decrementQuantity();
                            preference.setShoppingCart(cart);
                        }
                        else {
                            menuItemArrayList.remove(position);
                            cart.setItems(menuItemArrayList);
                            preference.setShoppingCart(cart);
                            notifyDataSetChanged();
                        }
            }
        });
        //If the user pushes the removeItem button on the item view, then the action is taken.
        holder.removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Deleting the item from a list
                if( isPlaced ){
                    setIsChanged(true);
                    Toast.makeText(context, "Changed menu",Toast.LENGTH_LONG).show();
                }

                menuItemArrayList.remove(position);
                cart.setItems(menuItemArrayList);
                preference.setShoppingCart(cart);
                notifyDataSetChanged();
            }
        });

        holder.editItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuItemEditPopup = new Dialog(context);
                menuItemEditPopup.setContentView(R.layout.menu_item_edit_popup);
                menuItemEditPopup.show();

                menuItemEditPopup.findViewById(R.id.menu_item_edit_close).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        menuItemEditPopup.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuItemArrayList.size();
    }//A number of items on the list
    //Creating holder to keep menu items as a list dynamically.
    public class ShoppingCartViewHolder extends RecyclerView.ViewHolder{

        TextView name;//A name of food
        TextView price;//A price of food
        TextView quantity;//A quantity of food
        ImageButton add;//Set actionListener to add button
        ImageButton remove;//Set actionListener to remove (decrement) button
        TextView removeItem;//Set actionListener to remove button
        TextView editItem;
        ImageView itemImage;

        //set the above variables to each tag on the xml file.
        public ShoppingCartViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_name);
            quantity = itemView.findViewById(R.id.item_quantity);
            add = itemView.findViewById(R.id.addButton);
            remove = itemView.findViewById(R.id.removeButton);
            price = itemView.findViewById(R.id.price);
            removeItem = itemView.findViewById(R.id.removeItem);
            editItem = itemView.findViewById(R.id.editItem);
            itemImage = itemView.findViewById(R.id.item_image_shopping_cart_tile);
        }
    }

    public void setIsPlaced(boolean isPlaced){
        this.isPlaced = isPlaced;
    }
    public void setIsChanged(boolean isChanged){
        this.isChanged = isChanged;
    }
    public boolean isMenuChangedAfterPlaced(){
        return isPlaced && isChanged;
    }
}