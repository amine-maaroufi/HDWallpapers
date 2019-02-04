package com.andromob.hdwallpapers;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.TextView;
import com.github.javiersantos.appupdater.AppUpdater;
import com.andromob.hdwallpapers.config.admob;

import java.util.List;

/**
 * Created by andromob on 26/08/2018.
 */

public class Fragment_Settings_app extends Fragment implements View.OnClickListener {

    LinearLayout rate,share,contact,privacy,moreapps,update;
    TextView app_name, version_name;

    public Fragment_Settings_app() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_settings_app, container, false);

        LinearLayout linearlayout = (LinearLayout)v.findViewById(R.id.unitads);
        admob.admobBannerCall(getActivity(), linearlayout);

        app_name = (TextView) v.findViewById(R.id.app_name);
        version_name = (TextView) v.findViewById(R.id.version_name);
        app_name.setText(getResources().getString(R.string.app_name));
        try {
            version_name.setText("Version "+(getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0)).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        rate = (LinearLayout) v.findViewById(R.id.about_rate);
        share = (LinearLayout) v.findViewById(R.id.about_share);
        moreapps = (LinearLayout) v.findViewById(R.id.about_moreapps);
        contact = (LinearLayout) v.findViewById(R.id.about_contact);
        privacy = (LinearLayout) v.findViewById(R.id.about_privacy);
        update = (LinearLayout) v.findViewById(R.id.update);

        update.setOnClickListener(this);
        rate.setOnClickListener(this);
        share.setOnClickListener(this);
        moreapps.setOnClickListener(this);
        contact.setOnClickListener(this);
        privacy.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.about_rate:
                try {
                    getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+getActivity().getPackageName())));
                } catch (android.content.ActivityNotFoundException anfe) {
                    getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+getActivity().getPackageName())));
                }
                break;
            case R.id.about_share:
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Hey my friend(s) check out this amazing application \n https://play.google.com/store/apps/details?id="+ getActivity().getPackageName() +" \n";
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                break;
            case R.id.update:
                AppUpdater appUpdater = new AppUpdater(getActivity())
                        .setContentOnUpdateAvailable("Check out the latest version available to get the latest features and bug fixes")
                        .setCancelable(false)
                        .setButtonDoNotShowAgain(null)
                        .setButtonUpdate("Update now")
                        .setButtonDismiss("later")
                        .showAppUpdated(true)
                        .setTitleOnUpdateNotAvailable("Update not available")
                        .setContentOnUpdateNotAvailable("No update available. Check for updates again later!");
                appUpdater.start();
                Toast.makeText(getActivity(), "Wait to check update", Toast.LENGTH_SHORT).show();
                break;
            case R.id.about_moreapps:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Classe_Settings.more_apps_link)));
                } catch (android.content.ActivityNotFoundException anfe) {
                }
                break;
            case R.id.about_contact:
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{Classe_Settings.contactMail});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, getActivity().getPackageName());
                emailIntent.setType("text/plain");
                //emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Messg content");
                final PackageManager pm = getActivity().getPackageManager();
                final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
                ResolveInfo best = null;
                for(final ResolveInfo info : matches)
                    if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                        best = info;
                if (best != null)
                    emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
                startActivity(emailIntent);
                break;
            case R.id.about_privacy:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Classe_Settings.privacy_policy_url)));
                } catch (android.content.ActivityNotFoundException anfe) {
                }
                break;
        }

    }


}
