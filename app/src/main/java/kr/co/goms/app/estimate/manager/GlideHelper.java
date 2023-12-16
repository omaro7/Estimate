package kr.co.goms.app.estimate.manager;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.io.IOException;
import java.util.concurrent.Callable;

import kr.co.goms.app.estimate.MyApplication;

public class GlideHelper {

    public static GlideHelper instance;
    public GlideHelper() {
    }

    public static GlideHelper I(){
        if(instance == null){
            instance = new GlideHelper();
        }
        return instance;
    }
    /**
     *
     * @param activity
     * @param imageUri
     * @param imageView
     */
    public void setImageView(Activity activity, Uri imageUri, ImageView imageView) {
        Glide.with(activity)
                .load(imageUri)
                .into(imageView);
    }
    /**
     *
     * @param activity
     * @param imageUri
     * @param imageView
     * @param width     100
     * @param height    100
     */
    public void setImageView(Activity activity, Uri imageUri, ImageView imageView, int width, int height) {
        Glide.with(activity)
                .load(imageUri)
                .override(width,height)
                .into(imageView);
    }


}