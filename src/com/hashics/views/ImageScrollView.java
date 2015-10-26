package com.hashics.views;

import java.util.List;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hashics.R;
import com.hashics.constants.Constants;
import com.hashics.customviews.CustomHorizontalScrollView;
import com.hashics.parse.stubs.Album;
import com.hashics.parse.stubs.Media;
import com.hashics.util.DisplayUtils;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

public class ImageScrollView extends BaseActivity {
	private static final String TAG = ImageScrollView.class.getSimpleName();

	int mNumOfPages = 0;
	List<Media> mMediaList;
	CustomHorizontalScrollView horizontalScrollView;
	ParseImageView mParseImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_slideview);

		// get display width
		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		int height = dm.heightPixels;

		String albumTitle = getIntent().getStringExtra(Constants.INTENT_EXTRA_ALBUM_NAME);
		getImages(albumTitle, width, height);
	}

	void getImages(String albumTitle, final int width, final int height) {
		ParseQuery<Album> query = ParseQuery.getQuery("Album");
		query.whereEqualTo("title", albumTitle);
		query.getFirstInBackground(new GetCallback<Album>() {

			@Override
			public void done(Album album, ParseException e) {
				if (e == null) {
					ParseRelation<Media> relation = album.getMediaList();
					relation.getQuery().findInBackground(new FindCallback<Media>() {

						@Override
						public void done(List<Media> mediaList, ParseException e) {
							mNumOfPages = mediaList.size();
							mMediaList = mediaList;
							horizontalScrollView = new CustomHorizontalScrollView(ImageScrollView.this, mNumOfPages, width);

							LinearLayout imgHorizontalSlide = (LinearLayout) findViewById(R.id.layout_img_horz_slide);
							imgHorizontalSlide.addView(horizontalScrollView);

							LinearLayout container = new LinearLayout(ImageScrollView.this);
							container.setLayoutParams(new LayoutParams(width, height));

							for (int iLoop = 0; iLoop < mNumOfPages; iLoop++) {
								mParseImageView = new ParseImageView(ImageScrollView.this);
								mParseImageView.setAdjustViewBounds(true);
								mParseImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
								mParseImageView.setLayoutParams(new LayoutParams(width, height
										- DisplayUtils.getActionBarHeight(ImageScrollView.this)
										- DisplayUtils.getStatusBarHeight(ImageScrollView.this)));
								ParseFile photoFile = mMediaList.get(iLoop).getParseFile("mediaContent");
								if (photoFile != null) {
									mParseImageView.setParseFile(photoFile);
									mParseImageView.loadInBackground(new GetDataCallback() {
										@Override
										public void done(byte[] data, ParseException e) {
										}
									});
								}
								container.addView(mParseImageView);
							}

							// adding container to horizontal scroll
							horizontalScrollView.addView(container);
						}

					});
				} else {
					Toast.makeText(ImageScrollView.this, "Error fetching album: " + e.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
}
