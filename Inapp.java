package inapp.devi.com.inappdemo.main;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import inapp.devi.com.inappdemo.R;
import inapp.devi.com.inappdemo.util.IabHelper;
import inapp.devi.com.inappdemo.util.IabResult;
import inapp.devi.com.inappdemo.util.Inventory;
import inapp.devi.com.inappdemo.util.Purchase;

public class Inapp extends AppCompatActivity {

    IabHelper iabHelper;
    Context context;
    private static final String TAG =
            "devi.com.inappdemo.main";
    String ITEM_SKU = "";
    String base64EncodedPublicKey = "";
    
    // ITEM_SKU is Item ID of item to be purchased ; which is set when a new inapp item is saved in Google Play Store
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result,
                                          Purchase purchase) {
            if (result.isFailure()) {
                // Handle error
                return;
            } else if (purchase.getSku().equals(ITEM_SKU)) {
                // Do your code on purachase finished
                consumePurchasedItem();
                Log.v("ITEM_DETAILS", "" + purchase.getOriginalJson());
            }
        }
    };


    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase,
                                      IabResult result) {

            if (result.isSuccess()) {
                //purchase success,
                //onActivity called
            } 
            else {
                // handle error
            }
        }
    };

    IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {

            if (result.isFailure()) {
                // Handle failure
            } 
            else {
                onConsumeFinished(inventory);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_inapp);

        setUpInapp(this, base64EncodedPublicKey);
    }

    public void setUpInapp(Context context, String base64EncodedPublicKey) {
        /* base64EncodedPublicKey is applications public key ;
         which developer gets from Services&APIs page of Google Play Developer Console.
         */
        iabHelper = new IabHelper(context, base64EncodedPublicKey);

        iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    Log.d(TAG, "Inapp Billing setup failed:" + result);
                } else {
                    Log.d(TAG, "Inapp Billing is set up successfully");
                }
            }
        });
    }


    public void doPurchase(Context context) {
        /* initiating the purchase process.
         response is received in onActivityResult.*/

        iabHelper.launchPurchaseFlow(context, ITEM_SKU, 10001,
                mPurchaseFinishedListener, "mypurchasetoken");
        
    }

    public void consumePurchasedItem() {
        /* On consuming purchased item
         Depending on the type of item (one-time purchase , subscription , purchase again once consumed )
         re-activate the purchase process if needed.
          */
        iabHelper.queryInventoryAsync(mReceivedInventoryListener);

    }

    public void onConsumeFinished(Inventory inventory) {
        //Do code which is to be performed after consuming the purchased item.
        iabHelper.consumeAsync(inventory.getPurchase(ITEM_SKU),
                mConsumeFinishedListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (iabHelper != null) iabHelper.dispose();
        iabHelper = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (!iabHelper.handleActivityResult(requestCode,

                resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);


        }
        //after payment successfull
        Log.v("activity", "result" + requestCode + " " + resultCode);

    }

}

