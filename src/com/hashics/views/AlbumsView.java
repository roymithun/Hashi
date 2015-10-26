package com.hashics.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.hashics.adapters.parse.AlbumsAdapter;
import com.hashics.R;

public class AlbumsView extends BaseActivity {

	private static final String TAG = AlbumsView.class.getSimpleName();
	private ImageButton mImgBtnMenu;
	private ListView mListViewAlbums;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_album);

		findViewById(R.id.btn_menu).setOnClickListener(mListener);
		findViewById(R.id.btn_add_album).setOnClickListener(mListener);
		mListViewAlbums = (ListView) findViewById(R.id.list_view_albums);
		mListViewAlbums.setAdapter(new AlbumsAdapter(this));
	}

	private OnClickListener mListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.btn_menu) {
				Toast.makeText(AlbumsView.this, "Click on menu!!!", Toast.LENGTH_SHORT).show();
			} else if (v.getId() == R.id.btn_add_album) {
				// Toast.makeText(AlbumsView.this, "Click on add album!!!",
				// Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(AlbumsView.this, CreateAlbum.class);
				startActivity(intent);
			}

		}

	};

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

//	class AlbumsAdapter extends BaseAdapter {
//
//		LayoutInflater mInflater;
//		Context mContext;
//
//		AlbumsAdapter(Context context) {
//			mContext = context;
//			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		}
//
//		@Override
//		public int getCount() {
//			// TODO Auto-generated method stub
//			return 10;
//		}
//
//		@Override
//		public Object getItem(int position) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public long getItemId(int position) {
//			// TODO Auto-generated method stub
//			return 0;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			ViewHolder viewHolder;
//			if (convertView == null) {
//				convertView = mInflater.inflate(R.layout.list_item_album, null);
//				viewHolder = new ViewHolder();
//				// GridView gridview = (GridView)
//				// convertView.findViewById(R.id.grid_view_albums);
//				// gridview.setAdapter(new ImageAdapter(mContext));
//				// viewHolder.gridview = gridview;
//				convertView.setTag(viewHolder);
//			} else {
//				viewHolder = (ViewHolder) convertView.getTag();
//			}
//			// viewHolder.txtView.setText("List Item: " + position);
//			return convertView;
//		}
//
//	}

	class ViewHolder {
		// GridView gridview;
	}

	// references to our images
	private Integer[] mThumbIds = { R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher };
	
}
