package com.hashics;

import android.app.Application;

import com.hashics.parse.stubs.Album;
import com.hashics.parse.stubs.Media;
import com.parse.Parse;
import com.parse.ParseObject;

public class BaseApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		/*
		 * Register ParseObject subclasses before Parse.intitialize
		 */
		ParseObject.registerSubclass(Album.class);
		ParseObject.registerSubclass(Media.class);
		
		Parse.initialize(this, "p9KXgagK5kRJ2cMwOCuXETpyLN8RQClxGy8sYqSf", "Lit21GQbeIOh2UbT0Ahry1ftGH6ObhvwmllcaFUX");
	}

	
}
