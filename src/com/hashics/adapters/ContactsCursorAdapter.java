package com.hashics.adapters;

import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.provider.ContactsContract.Contacts;
import android.provider.MediaStore;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hashics.R;
import com.hashics.customviews.RoundImage;

public class ContactsCursorAdapter extends CursorAdapter {

	LayoutInflater mLayoutInflater;

	public ContactsCursorAdapter(Context context, Cursor cursor, int flags) {
		super(context, cursor, flags);
		mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView txtViewTitle = (TextView) view.findViewById(R.id.txt_view_contact_name);
		ImageView imgViewPhoto = (ImageView) view.findViewById(R.id.img_view_contact_photo);
		String contactName = cursor.getString(cursor.getColumnIndex(Contacts.DISPLAY_NAME_PRIMARY));
		txtViewTitle.setText(contactName);
		String id = cursor.getString(cursor.getColumnIndex(Contacts._ID));
		// photo.setImageURI(getPhotoUriFromID(context, id));
		Bitmap bitmap = null;
		try {
			bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), getPhotoUriFromID(context, id));
			RoundImage roundedImage = new RoundImage(bitmap);
			imgViewPhoto.setImageDrawable(roundedImage);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// photo.setImageBitmap(getRoundedCornerBitmap(bitmap, 10));
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return mLayoutInflater.inflate(R.layout.list_item_invite_friends, parent, false);
	}

	private Uri getPhotoUriFromID(Context context, String id) {
		Uri contactUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, Long.parseLong(id));
		Uri photoUri = Uri.withAppendedPath(contactUri, Contacts.Photo.CONTENT_DIRECTORY);
		try {
			Cursor cur = context.getContentResolver().query(photoUri, new String[] { Contacts.Photo.PHOTO }, null, null, null);

			if (cur != null) {
				if (!cur.moveToFirst()) {
					return null; // no photo
				}
			} else {
				return null; // error in cursor process
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return photoUri;
	}

	public Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}
}
