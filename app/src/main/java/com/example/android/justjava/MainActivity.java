/**
 * Add your package below. Package name can be found in the project's AndroidManifest.xml file.
 * This is the package name our example uses:
 * <p>
 * package com.example.android.justjava;
 */
package com.example.android.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {
    //Global variables
    int quantity = 2;
    int priceOfCoffee = 5;
    int priceOfWhippedCream = 1;
    int priceOfChocolate = 2;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        boolean hasWhippedCream = ((CheckBox) findViewById(R.id.whippedcream_checkbox)).isChecked();
        boolean hasChocolate = ((CheckBox) findViewById(R.id.chocolate_checkbox)).isChecked();
        String customerName = ((EditText) findViewById(R.id.customerName)).getText().toString();
        String orderMessage = calculateOrder(calculatePrice(hasWhippedCream, hasChocolate), hasWhippedCream, hasChocolate, customerName);

        //Enviamos el order summary por correo
        String subject = "Just Java app order from " + ((EditText) findViewById(R.id.customerName)).getText().toString();
        String [] addresses = {"tonivalleraull@gmail.com"};
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, orderMessage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * This method calculates the price of the order.
     * @param addWhippedCream to check if the user wants whipped cream
     * @param addChocolate to check if the user wants chocolate
     * @return total price
     */
    public int calculatePrice(boolean addWhippedCream, boolean addChocolate) {
        int finalPriceOfCoffe = priceOfCoffee;

        //If the user wants cream we add the price of the whipped cream topping
        if(addWhippedCream){
            finalPriceOfCoffe += priceOfWhippedCream;
        }
        //If the user wants chocolate we add the price of the chocolate topping
        if(addChocolate){
            finalPriceOfCoffe += priceOfChocolate;
        }

        return finalPriceOfCoffe * quantity;
    }

    /**
     * Create summary of the order.
     *
     * @param addWhippedCream is whether or not the user wants whipped cream topping
     * @param addChocolate is whether or not the user wants whipped cream topping
     * @param customerName is the name of the customer for the Order
     * @param price of the order
     * @return text summary
     */
    public String calculateOrder(int price, boolean addWhippedCream, boolean addChocolate, String customerName) {
        String message = "Name: " + customerName + "\n";
        message += "Add whiped cream? " + addWhippedCream + "\n";
        message += "Add chocolate? " + addChocolate + "\n";
        message += "Quantity: " + quantity + "\n";
        message += "Total: " + price + "€\n";
        message += "Thank you!";
        return message;
    }

    /**
     * This method is called when the increment button is clicked.
     */
    public void increment(View view) {

        //We don't allow to order more than one hundred coffees
        if (quantity < 100) {
            quantity++;
            display(quantity);
            return;
        }
        //We show a toast message informing the user that it's not possible to order more than one hundred coffees
        else{
            Toast.makeText(this, "You can't order more than one hundred coffees", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    /**
     * This method is called when the decrement button is clicked.
     */
    public void decrement(View view) {
        //We don't allow to order less than one coffee
        if (quantity > 1) {
            quantity--;
            display(quantity);
            return;
        }
        //We show a toast message informing the user that it's not possible to order less than 1 coffee
        else{
            Toast.makeText(this, "You can't order less than one coffees", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}