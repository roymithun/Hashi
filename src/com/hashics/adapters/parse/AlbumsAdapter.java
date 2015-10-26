package com.hashics.adapters.parse;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.hashics.R;
import com.hashics.constants.Constants;
import com.hashics.parse.stubs.Album;
import com.hashics.parse.stubs.Media;
import com.hashics.views.ImageScrollView;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseRelation;

/*
 * The AlbumsAdapter is an extension of ParseQueryAdapter
 */
public class AlbumsAdapter extends ParseQueryAdapter<Album> {
	private static final String TAG = AlbumsAdapter.class.getSimpleName();

	private Context mContext;

	public AlbumsAdapter(Context context) {
		super(context, new ParseQueryAdapter.QueryFactory<Album>() {
			public ParseQuery<Album> create() {
				// Here we can configure a ParseQuery to display albums
				ParseQuery query = new ParseQuery("Album");
				// query.whereContainedIn("rating", Arrays.asList("5", "4"));
				// query.orderByDescending("rating");
				return query;
			}
		});
		mContext = context;
	}

	@Override
	public View getItemView(final Album album, View v, ViewGroup parent) {

		if (v == null) {
			v = View.inflate(getContext(), R.layout.list_item_album, null);
		}

		super.getItemView(album, v, parent);

		final ArrayList<ParseImageView> listImgView = new ArrayList<ParseImageView>();
		String imgViewID = "";
		for (int j = 1; j <= 12; j++) {
			imgViewID = String.format("id/parse_img_view_%d", j);
			int resId = getResourceId(mContext, imgViewID, null, mContext.getPackageName());
			listImgView.add((ParseImageView) v.findViewById(resId));
		}

		ParseRelation<Media> relation = album.getMediaList();
		relation.getQuery().findInBackground(new FindCallback<Media>() {
			@Override
			public void done(List<Media> results, ParseException e) {
				if (e != null) {
					// There was an error
				} else {
					arrangeGridRows(results, listImgView);

					int x = 0;
					for (Media media : results) {
						final ParseImageView gridImg = listImgView.get(x++);
						
						// set onclick listener for images
						gridImg.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								Intent intent=new Intent(mContext, ImageScrollView.class);
								intent.putExtra(Constants.INTENT_EXTRA_ALBUM_NAME, album.getTitle());
								mContext.startActivity(intent);
							}
						});
						ParseFile photoFile = media.getParseFile("mediaContent");
						if (photoFile != null) {
							gridImg.setParseFile(photoFile);
							gridImg.loadInBackground(new GetDataCallback() {
								@Override
								public void done(byte[] data, ParseException e) {
								}
							});
						}
					}
					// results have all the Posts the current user liked.

				}
			}
		});

		// TextView titleTextView = (TextView) v.findViewById(R.id.text1);
		// titleTextView.setText(album.getTitle());
		// TextView ratingTextView = (TextView)
		// v.findViewById(R.id.favorite_meal_rating);
		// ratingTextView.setText(album.getRating());
		return v;
	}

	protected void arrangeGridRows(List<Media> results, ArrayList<ParseImageView> listImgView) {
		int row = 0;
		if (results.size() > 0 && results.size() <= 4) {
			row = 1;
		} else if (results.size() > 4 && results.size() <= 8) {
			row = 2;
		} else if (results.size() > 8) {
			row = 3;
		}

		switch (row) {
		case 1:
			for (int i = 0; i < 4; i++) {
				((ParseImageView) listImgView.get(i)).setVisibility(View.VISIBLE);
			}
			for (int i = 4; i < 12; i++) {
				((ParseImageView) listImgView.get(i)).setVisibility(View.GONE);
			}
			break;
		case 2:
			for (int i = 0; i < 8; i++) {
				((ParseImageView) listImgView.get(i)).setVisibility(View.VISIBLE);
			}
			for (int i = 8; i < 12; i++) {
				((ParseImageView) listImgView.get(i)).setVisibility(View.GONE);
			}
			break;
		case 3:
			for (int i = 0; i < 12; i++) {
				((ParseImageView) listImgView.get(i)).setVisibility(View.VISIBLE);
			}
			break;
		default:
			for (int i = 0; i < 12; i++) {
				((ParseImageView) listImgView.get(i)).setVisibility(View.GONE);
			}
			break;
		}
	}

	public int getResourceId(Context context, String pResourceUri, String pResourcename, String pPackageName) {
		try {
			return context.getResources().getIdentifier(pResourceUri, pResourcename, pPackageName);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
}
