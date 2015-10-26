package com.hashics.views;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.hashics.R;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.hashics.parse.stubs.Album;
import com.hashics.parse.stubs.Media;
import com.hashics.views.PublishAlbum.ImageAdapter;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class PublishAlbum extends BaseActivity {
	private static final String TAG = AlbumsView.class.getSimpleName();

	// this is the action code we use in our intent,
	// this way we know we're looking at the response from our own action
	private static final int SELECT_PICTURE = 1;
	// private String selectedImagePath;
	ImageView mImgViewNoPics;
	GridView mGridViewAlbum;
	private ImageAdapter mImageAdapter;
	private ArrayList<Uri> mListImageUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish_album);

		mListImageUri = new ArrayList<Uri>();

		findViewById(R.id.btn_add_pictures).setOnClickListener(mListener);
		mImgViewNoPics = (ImageView) findViewById(R.id.img_view_invite_frnds_add_picture);
		mGridViewAlbum = (GridView) findViewById(R.id.grid_view_album);

		mImageAdapter = new ImageAdapter(this);
		mGridViewAlbum.setAdapter(mImageAdapter);

		// gridview.setOnItemClickListener(new OnItemClickListener() {
		// public void onItemClick(AdapterView<?> parent, View v, int position,
		// long id) {
		// Toast.makeText(PublishAlbum.this, "" + position,
		// Toast.LENGTH_SHORT).show();
		// }
		// });
	}

	public class ImageAdapter extends BaseAdapter {
		private Context mContext;

		public ImageAdapter(Context c) {
			mContext = c;
		}

		public int getCount() {
			return mListImageUri.size();
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}

		// create a new ImageView for each item referenced by the Adapter
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView;
			if (convertView == null) { // if it's not recycled, initialize some
										// attributes
				imageView = new ImageView(mContext);
				imageView.setLayoutParams(new GridView.LayoutParams(getResources().getDimensionPixelSize(R.dimen.img_album_size), getResources()
						.getDimensionPixelSize(R.dimen.img_album_size)));
				imageView.setScaleType(ImageView.ScaleType.FIT_XY);
				imageView.setPadding(8, 8, 8, 8);
			} else {
				imageView = (ImageView) convertView;
			}

			imageView.setImageURI(mListImageUri.get(position));
			return imageView;
		}
	}

	private OnClickListener mListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.btn_add_pictures) {
				// let user select a file
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
			}

		}

	};

	int count = 1;

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == SELECT_PICTURE) {
				final Uri selectedImageUri = data.getData();
				if (mListImageUri.contains(selectedImageUri)) {
					Toast.makeText(this, "Image already exists", Toast.LENGTH_SHORT).show();
					return;
				}
				// selectedImagePath = getPath(selectedImageUri);

				/*
				 * Upload a ParseFile for obtained image
				 */
				Log.i(TAG, "Uri type->" + getContentResolver().getType(selectedImageUri));
				try {
					final Media media = new Media();
					media.setAlbumID("134456F");
					media.setCreatedBy(ParseUser.getCurrentUser());

					InputStream iStream = getContentResolver().openInputStream(selectedImageUri);
					byte[] inputData = getBytes(iStream);

					final ParseFile photo = new ParseFile("photo_" + count, inputData, getContentResolver().getType(selectedImageUri));
					photo.saveInBackground(new SaveCallback() {

						@Override
						public void done(ParseException e) {
							if (e == null) {
								media.setMediaContent(photo);
								// Save the album and return
								media.saveInBackground(new SaveCallback() {

									@Override
									public void done(ParseException e) {
										if (e == null) {
											count++;
											// setVisibility of views
											Log.i(TAG, "NoPics Visisble? " + mImgViewNoPics.getVisibility());
											Log.i(TAG, "Grid Visisble? " + mGridViewAlbum.getVisibility());
											mImgViewNoPics.setVisibility(View.GONE);
											mGridViewAlbum.setVisibility(View.VISIBLE);
											mListImageUri.add(selectedImageUri);
											mImageAdapter.notifyDataSetChanged();

											ParseQuery<Album> query = ParseQuery.getQuery("Album");
											query.whereEqualTo("title", "134456F");
											query.getFirstInBackground(new GetCallback<Album>() {

												@Override
												public void done(Album album, ParseException e) {
													if (e == null) {
														Log.i(TAG, "gibow album name: " + album.getTitle());
														ParseRelation relation = album.getMediaList();
														relation.add(media);
														album.saveInBackground(new SaveCallback() {

															@Override
															public void done(ParseException e) {
																if (e == null)
																	Log.i(TAG, "gibow album created");
																else
																	Log.i(TAG, "gibow album not created");
															}
														});
													}
												}
											});
										} else {
											Toast.makeText(PublishAlbum.this, "Error saving media: " + e.getMessage(), Toast.LENGTH_SHORT).show();
										}
									}

								});
							} else {
								Toast.makeText(PublishAlbum.this, "Error Saving image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
							}
						}
					});

				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public byte[] getBytes(InputStream inputStream) throws IOException {
		ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
		int bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];

		int len = 0;
		while ((len = inputStream.read(buffer)) != -1) {
			byteBuffer.write(buffer, 0, len);
		}
		return byteBuffer.toByteArray();
	}

	/**
	 * helper to retrieve the path of an image URI
	 */
	public String getPath(Uri uri) {
		// just some safety built in
		if (uri == null) {
			// TODO perform some logging or show user feedback
			return null;
		}
		// try to retrieve the image from the media store first
		// this will only work for images selected from gallery
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		if (cursor != null) {
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		}
		// this is our fallback here
		return uri.getPath();
	}

	public void onBack(View v) {
		finish();
	}
}
