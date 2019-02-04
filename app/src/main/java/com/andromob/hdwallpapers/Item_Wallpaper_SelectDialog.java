package com.andromob.hdwallpapers;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

import com.andromob.hdwallpapers.R;
/**
 * Created by andromob on 26/08/2018.
 */

@SuppressLint("ValidFragment")
public class Item_Wallpaper_SelectDialog extends DialogFragment {

    Context context;
    Bitmap resource;
    View view;


    public Item_Wallpaper_SelectDialog(Context context, Bitmap resource, View view) {
        this.context = context;
        this.resource = resource;
        this.view = view;
    }




    private void setOurWall(int which, final int sbMessage) {

          try {


              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                  WallpaperManager.getInstance(context)
                          .setBitmap(resource, null, true, which);
                  Snackbar.make(view, sbMessage, Snackbar.LENGTH_SHORT).show();
              }else{

                    // alert dialog when api < 24
                  new AlertDialog.Builder(getActivity()).setTitle("Confirm Delete?")
                          .setMessage("Your android version is less than Nougat 7.0 , you will set the wallpaper as home screen and lock screen ? ")
                          .setPositiveButton("YES",
                                  new DialogInterface.OnClickListener() {
                                      public void onClick(DialogInterface dialog, int which) {

                                          // Perform Action & Dismiss dialog
                                          try {
                                              WallpaperManager.getInstance(context)
                                                      .setBitmap(resource);
                                              Snackbar.make(view, "Home and lockscreen wallpaper set!", Snackbar.LENGTH_SHORT).show();
                                          } catch (IOException e) {
                                              e.printStackTrace();
                                          }
                                      }
                                  })
                          .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialog, int which) {
                                  // Do nothing
                                  dialog.dismiss();
                              }
                          })
                          .create()
                          .show();
              }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }




    private void setOurWall(int sbMessage) {

        try {
            WallpaperManager.getInstance(context)
                    .setBitmap(resource);
            Snackbar.make(view, sbMessage, Snackbar.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder setWall = new AlertDialog.Builder(context);
        setWall.setTitle(R.string.set_wallpaper)
                .setItems(R.array.set_wallpaper_options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        switch (i) {
                            case 0: {
                                new Handler(Looper.getMainLooper()).post(new Runnable() {

                                    @Override
                                    public void run() {
                                        setOurWall(WallpaperManager.FLAG_SYSTEM, R.string.home_set);

                                    }
                                });
                                break;
                            }
                            case 1: {
                                new Handler(Looper.getMainLooper()).post(new Runnable() {

                                    @Override
                                    public void run() {
                                        setOurWall(WallpaperManager.FLAG_LOCK, R.string.lock_screen_set);
                                    }
                                });
                                break;
                            }
                            case 2: {
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        setOurWall(R.string.both_set);
                                    }
                                });
                                break;
                            }
                        }

                    }
                });

        return setWall.create();
    }
}
