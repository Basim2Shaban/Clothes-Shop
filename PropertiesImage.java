package com.basim.outfitters.image_classes;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.Toast;

import com.basim.outfitters.R;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

/**
 * Created by Basim on 16/07/2018.
 */

public class PropertiesImage {

    //
    // i use it in the custom pager adapter and it's doing dialog and properties to dialog has a button
    //
    public void AlertDialog_ButtonS(final ImageView imageView , final Context mContext){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(true)
                .setPositiveButton(mContext.getString(R.string.save), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Bitmap bitmap = ((GlideBitmapDrawable) imageView.getDrawable().getCurrent()).getBitmap();
                        SaveImage(bitmap , mContext);
                    }

                });



        final AlertDialog alert = builder.create();
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setGravity(Gravity.CENTER);
                DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
                int width = displayMetrics.widthPixels;
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setWidth(width);
            }
        });

        alert.show();

    }



    //
    // this method is doing save image
    //
    public void SaveImage(Bitmap bitmap , Context mContext) {
        boolean success = false;

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/DCIM/outfitters");
        myDir.mkdirs();
        Calendar calendarRightNow = Calendar.getInstance();
        long milli = calendarRightNow.getTimeInMillis();
        String fname = milli + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.canRead();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            success = true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (success) {
            Toast.makeText(mContext, mContext.getString(R.string.is_saved),
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(mContext,
                    mContext.getString(R.string.saveing_error), Toast.LENGTH_LONG).show();
        }


    }






}
