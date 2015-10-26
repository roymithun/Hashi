package com.hashics.parse.stubs;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

/*
 * An extension of ParseObject that makes
 * it more convenient to access information
 * about a given Album 
 * 
 * 	title 		: String
 * 	createdBy 	: User
 *  mode		: String
 *  mediaList	: Pointer
 */
@ParseClassName("Album")
public class Album extends ParseObject {
	public Album() {
		// Default constructor
	}

	public String getTitle() {
		return getString("title");
	}

	public void setTitle(String title) {
		put("title", title);
	}

	public ParseUser getCreatedBy() {
		return getParseUser("createdBy");
	}

	public void setCreatedBy(ParseUser user) {
		put("createdBy", user);
	}

	public String getMode() {
		return getString("mode");
	}

	public void setMode(String mode) {
		put("mode", mode);
	}
	
	public ParseRelation getMediaList() {
		return getRelation("mediaList");
	}

	public void setMediaList(ParseRelation relation) {
		put("mediaList", relation);
	}
}
