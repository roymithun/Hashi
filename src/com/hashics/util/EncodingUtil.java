package com.hashics.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Base64;
import android.util.Log;

import com.hashics.BaseApplication;

public class EncodingUtil {
	public static void showHashKey(Context context) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo("com.hashics", PackageManager.GET_SIGNATURES); // Your

			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.i("KeyHash:", "KeyHash:" + Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		} catch (NameNotFoundException e) {
			Log.i(BaseApplication.class.getName(), e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			Log.i(BaseApplication.class.getName(), e.getMessage());
		} catch (Exception e) {
			Log.i(BaseApplication.class.getName(), e.getMessage());
		}
	}
}
