package com.hashics.views;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.hashics.R;
import com.hashics.parse.stubs.Album;
import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class CreateAlbum extends BaseActivity {

	private RadioGroup mRadioAlbumMode;
	private AlbumMode mAlbumMode;
	private EditText mEditTxtAlbumName;

	public static enum AlbumMode {
		PUBLIC, PRIVATE;

		public static int getAlbumMode(AlbumMode mode) {
			switch (mode) {
			case PUBLIC:
				return R.string.strv_public;
			case PRIVATE:
				return R.string.strv_private;
			default:
				return R.string.strv_private;
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_album);

		mEditTxtAlbumName = (EditText) findViewById(R.id.edit_txt_album_name);

		/*
		 * Album mode configuration
		 */
		mAlbumMode = AlbumMode.PRIVATE;
		mRadioAlbumMode = (RadioGroup) findViewById(R.id.radio_grp_album_mode);
		mRadioAlbumMode.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case 0:
					mAlbumMode = AlbumMode.PUBLIC;
					break;
				case 1:
					mAlbumMode = AlbumMode.PRIVATE;
					break;
				default:
					mAlbumMode = AlbumMode.PRIVATE;
					break;
				}
			}
		});
	}

	public void onBack(View v) {
		finish();
	}

	public void onPublish(View v) {
		/*
		 * Check if album with same name already exists
		 */
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Album");
		query.whereEqualTo("title", mEditTxtAlbumName.getText().toString());
		query.countInBackground(new CountCallback() {

			@Override
			public void done(int count, ParseException e) {
				if (e == null) {
					if (count > 0) {
						Toast.makeText(CreateAlbum.this, "Album already exists", Toast.LENGTH_SHORT).show();
						return;
					}
					else {
						PublishAlbum();
					}
				} else {
					Toast.makeText(CreateAlbum.this, "Error Querying: " + e.getMessage(), Toast.LENGTH_SHORT).show();
					return;
				}
			}
		});
	}

	void PublishAlbum() {
		final Intent intent = new Intent(CreateAlbum.this, PublishAlbum.class);

		// Create a new album
		final Album album = new Album();
		album.setMode(getString(AlbumMode.getAlbumMode(mAlbumMode)));
		album.setCreatedBy(ParseUser.getCurrentUser());
		album.setTitle(mEditTxtAlbumName.getText().toString());

		// Save the album and return
		album.saveInBackground(new SaveCallback() {

			@Override
			public void done(ParseException e) {
				if (e == null) {
					intent.putExtra("Album", album.getTitle());
					finish();
					startActivity(intent);
				} else {
					Toast.makeText(CreateAlbum.this, "Error saving: " + e.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}

		});
	}
}
