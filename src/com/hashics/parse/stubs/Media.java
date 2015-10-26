package com.hashics.parse.stubs;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

/*
 * An extension of ParseObject that makes
 * it more convenient to access information
 * about a given Media 
 * 
 * 	albumID 	: String
 * 	createdBy 	: User
 *  mediaContent: File
 */
@ParseClassName("Media")
public class Media extends ParseObject {
	public Media() {
		// Default constructor
	}

	public String getAlbumID() {
		return getString("albumID");
	}

	public void setAlbumID(String albumID) {
		put("albumID", albumID);
	}

	public ParseUser getCreatedBy() {
		return getParseUser("createdBy");
	}

	public void setCreatedBy(ParseUser user) {
		put("createdBy", user);
	}

	public ParseFile getMediaContent() {
		return getParseFile("mediaContent");
	}

	public void setMediaContent(ParseFile mediaContent) {
		put("mediaContent", mediaContent);
	}
}
