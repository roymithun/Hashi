package com.hashics.views;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract.Contacts;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.hashics.R;
import com.hashics.adapters.ContactsCursorAdapter;

public class InviteFriendsView extends BaseActivity {

	private static final String TAG = InviteFriendsView.class.getSimpleName();
	private ListView mListViewContacts;
	private Cursor mCursor;
	private ContactsCursorAdapter mCustomAdapter;

	// Your database schema
	String[] mProjection = { Contacts._ID, Contacts.DISPLAY_NAME_PRIMARY, Contacts.PHOTO_THUMBNAIL_URI };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invite_friends);

		// Configure Navigation Bar
		findViewById(R.id.img_btn_nav_bar_right).setVisibility(View.GONE);
		findViewById(R.id.btn_nav_bar_right).setVisibility(View.VISIBLE);
		findViewById(R.id.navigation_bar).setBackgroundColor(getResources().getColor(R.color.custom_grey_2));
		((TextView) findViewById(R.id.txt_view_title)).setText("Pick Friends");

		// /////////////////////////
		mListViewContacts = (ListView) findViewById(R.id.list_view_contacts);

		// Here we query database
		mCursor = getContentResolver().query(Contacts.CONTENT_URI, mProjection, null, null, null);

		new Handler().post(new Runnable() {

			@Override
			public void run() {
				mCustomAdapter = new ContactsCursorAdapter(InviteFriendsView.this, mCursor, 0);

				mListViewContacts.setAdapter(mCustomAdapter);
			}

		});
	}
}
