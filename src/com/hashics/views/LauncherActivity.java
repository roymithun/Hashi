package com.hashics.views;

import android.content.Intent;
import android.os.Bundle;

import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class LauncherActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Check if there is a currently logged in user
		// and it's linked to a Facebook account.
		ParseUser currentUser = ParseUser.getCurrentUser();
		if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
			launchAlbumsScreen();
		}
		else {
			launchLoginScreen();
		}
	}
	
	void launchLoginScreen() {
		Intent intent=new Intent(LauncherActivity.this, LoginActivity.class);
		finish();
		startActivity(intent);
	}

	void launchAlbumsScreen() {
		Intent intent=new Intent(LauncherActivity.this, AlbumsView.class);
		finish();
		startActivity(intent);
	}
}
