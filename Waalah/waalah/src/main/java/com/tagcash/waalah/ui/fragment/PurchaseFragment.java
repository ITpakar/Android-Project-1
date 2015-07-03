package com.tagcash.waalah.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tagcash.waalah.R;
import com.tagcash.waalah.app.Constants;
import com.tagcash.waalah.app.WAApplication;
import com.tagcash.waalah.model.WAPurchase;
import com.tagcash.waalah.ui.activity.MainActivity;
import com.tagcash.waalah.util.IabHelper;
import com.tagcash.waalah.util.IabResult;
import com.tagcash.waalah.util.Inventory;
import com.tagcash.waalah.util.Purchase;
import com.tagcash.waalah.util.WAFontProvider;

public class PurchaseFragment extends Fragment implements OnClickListener {

	private final static String TAG = "PurchaseFragment";
	
	private MainActivity mainActivity;
	
    static final String SKU_COIN_1 = "coin1";
    static final String SKU_COIN_2 = "coin2";
    static final String SKU_COIN_3 = "coin3";
    static final String SKU_COIN_4 = "coin4";
    static final String SKU_COIN_5 = "coin5";
	
	
	public ListView lst_purchase = null;
	
	IabHelper mHelper;


	private ArrayList<WAPurchase> list_data = new ArrayList<WAPurchase>();
	
	public PurchaseFragment(MainActivity activity) {
		super();
		
		mainActivity = activity;
		
		// compute your public key and store it in base64EncodedPublicKey
		mHelper = new IabHelper(mainActivity, Constants.BILLING_PUBLIC_KEY);

        // enable debug logging (for a production application, you should set this to false).
        mHelper.enableDebugLogging(true);

        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    Log.d(TAG, "Problem setting up In-app Billing: " + result);
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                Log.d(TAG, "Setup successful. Querying inventory.");
                
                List<String> additionalSkuList = new ArrayList<String>();
                additionalSkuList.add(SKU_COIN_1);
//                additionalSkuList.add(SKU_BANANA);
                mHelper.queryInventoryAsync(true, additionalSkuList, mGotInventoryListener);

//                mHelper.queryInventoryAsync(mGotInventoryListener);
            }
        });
	}
	
    /** Verifies the developer payload of a purchase. */
    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();

        /*
         * TODO: verify that the developer payload of the purchase is correct. It will be
         * the same one that you sent when initiating the purchase.
         *
         * WARNING: Locally generating a random string when starting a purchase and
         * verifying it here might seem like a good approach, but this will fail in the
         * case where the user purchases an item on one device and then uses your app on
         * a different device, because on the other device you will not have access to the
         * random string you originally generated.
         *
         * So a good developer payload has these characteristics:
         *
         * 1. If two different users purchase an item, the payload is different between them,
         *    so that one user's purchase can't be replayed to another user.
         *
         * 2. The payload must be such that you can verify it even when the app wasn't the
         *    one who initiated the purchase flow (so that items purchased by the user on
         *    one device work on other devices owned by the user).
         *
         * Using your own server to store and verify developer payloads across app
         * installations is recommended.
         */

        return true;
    }
    
    // Listener that's called when we finish querying the items and subscriptions we own
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // Is it a failure?
            if (result.isFailure()) {
                Log.d(TAG, "Failed to query inventory: " + result);
                return;
            }

            Log.d(TAG, "Query inventory was successful.");

            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */


//            String coin1_price = inventory.getSkuDetails(SKU_COIN_1).getPrice();
//            String coin1_title = inventory.getSkuDetails(SKU_COIN_1).getTitle();
//            WAPurchase purchase = new WAPurchase();
//            purchase.price = coin1_price;
//            purchase.title = coin1_title;
//            
//            list_data.add(purchase);
            lst_purchase.invalidate();      
            
            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
        }
    };
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.fragment_purchase, null);

		final ImageView img_menu = (ImageView) v.findViewById(R.id.img_menu);
		img_menu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mainActivity.showMenu();
			}
		});
				
		lst_purchase = (ListView) v.findViewById(R.id.lst_purchase);

		return v;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStart() {
		super.onStart();

		showListTest();
	}
	
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		if (mHelper != null) mHelper.dispose();
		mHelper = null;
	}

	private void showListTest() {
		list_data.clear();
		
		
		LasyAdapter adapter = new LasyAdapter(this.getActivity());
		lst_purchase.setAdapter(adapter);
	}
	
	private  class LasyAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public LasyAdapter(Context context) {
			if (context == null)
				context = WAApplication.getContext();
			mInflater = LayoutInflater.from(context);
		}

		public int getCount() {
			int nSize = list_data.size();
			return nSize;
		}

		public Object getItem(int position) {
			return list_data.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		class ViewHolder {
			TextView txt_price, txt_title;
		}	

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.row_purchase_item, null);
				holder = new ViewHolder();

				holder.txt_price = (TextView) convertView.findViewById(R.id.txt_price);
				holder.txt_title = (TextView) convertView.findViewById(R.id.txt_title);
				
				// set font
				holder.txt_price.setTypeface(WAFontProvider.getFont(WAFontProvider.GOTHAM_BOLD, PurchaseFragment.this.getActivity()));
				holder.txt_title.setTypeface(WAFontProvider.getFont(WAFontProvider.HELVETICA_NEUE, PurchaseFragment.this.getActivity()));

				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			WAPurchase purchase = (WAPurchase) getItem(position);
			
			holder.txt_price.setText(purchase.price);
			holder.txt_title.setText(purchase.title);
			
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO
				}
			});

			return convertView;
		}
	}
}

