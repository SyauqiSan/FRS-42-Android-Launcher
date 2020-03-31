package com.example.launchertrial01;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class HomeActivity extends Activity {
    TextView txtTime,txtDate;
    Calendar c;
    SimpleDateFormat simpleDateFormat;
    SimpleDateFormat simpleTimeFormat;


    // For grid view
    PackageManager homePackageManager;
    public static List<AppInfo> homeApps;
    GridView homeGridView;
    public static ArrayAdapter<AppInfo> homeAdapter;

    // Put it in static variable at on create to hold the value, so setStatusBar() function can be static.
    public static TextView statusBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Init static values
        statusBar = findViewById(R.id.status_bar);

        initHomeBtn();

        homeApps = null;
        homeAdapter = null;
        loadHomeApps();
        loadHomeListView();
        addHomeGridListeners();
    }

    // This is default application in home screen
    private ArrayList<String> getHomeAppsPackageList(){
        ArrayList<String> list = new ArrayList<String>();
        list.add("com.android.contacts");
        list.add("com.android.settings");
        list.add("com.android.deskclock");
        list.add("com.google.android.apps.messaging");
        list.add("com.google.android.apps.photos");
        list.add("com.google.android.music");
        return list;
    }

    private void initHomeBtn(){
        // image
        Button homeBtn1 = (Button) findViewById(R.id.homeBtn1);
        Button homeBtn2 = (Button) findViewById(R.id.homeBtn2);
        Button homeBtn4 = (Button) findViewById(R.id.homeBtn4);
        Button homeBtn5 = (Button) findViewById(R.id.homeBtn5);

        try {
            Drawable icon1 = this.getPackageManager().getApplicationIcon("com.android.calculator2");
            Drawable icon2 = this.getPackageManager().getApplicationIcon("com.android.contacts");
            Drawable icon4 = this.getPackageManager().getApplicationIcon("com.android.settings");
            Drawable icon5 = this.getPackageManager().getApplicationIcon("com.android.chrome");
            homeBtn1.setBackground(icon1);
            homeBtn2.setBackground(icon2);
            homeBtn4.setBackground(icon4);
            homeBtn5.setBackground(icon5);
        }catch(Exception e){

        }
    }
    public void runHomeBtn1(View v) {
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.android.calculator2");
        startActivity(launchIntent);
    }
    public void runHomeBtn2(View v) {
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.android.contacts");
        startActivity(launchIntent);
    }
    public void runHomeBtn3(View v) {
        Intent i = new Intent(HomeActivity.this, GetApps.class);
        startActivity(i);
    }
    public void runHomeBtn4(View v) {
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.android.settings");
        startActivity(launchIntent);
    }
    public void runHomeBtn5(View v) {
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.android.chrome");
        startActivity(launchIntent);
    }

    public static void setStatusBar(String text){
        statusBar.setText(text);
    }


    private void loadHomeApps() {
        try {

            homePackageManager = getPackageManager();
            if (homeApps == null) {
                homeApps = new ArrayList<AppInfo>();

                // get All List
//                prepareAllApps();

                // Get home apps only
                prepareHomeApps();

            }
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage().toString() + " loadApps", Toast.LENGTH_LONG).show();
            Log.e("Error loadApps", ex.getMessage().toString() + " loadApps");
        }
    }

    private void prepareAllApps(){
        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
//                i.addCategory(Intent.CATEGORY_DEFAULT);
//                i.addCategory(Intent.CATEGORY_APP_CALENDAR);
//                i.addCategory(Intent.CATEGORY_APP_BROWSER);
//                i.addCategory(Intent.CATEGORY_APP_CONTACTS);
//                i.addCategory(Intent.CATEGORY_ALTERNATIVE);

        List<ResolveInfo> availableApps = homePackageManager.queryIntentActivities(i, 0);
        for (ResolveInfo ri : availableApps) {
            AppInfo appinfo = new AppInfo();
            appinfo.label = ri.loadLabel(homePackageManager);
            appinfo.name = ri.activityInfo.packageName;
            appinfo.icon = ri.activityInfo.loadIcon(homePackageManager);
            homeApps.add(appinfo);
        }
    }

    private void prepareHomeApps(){
        ArrayList<String> packageNames = getHomeAppsPackageList();

        for (String packageName : packageNames){
            prepareApps(packageName);
        }
    }

    private void prepareApps(String packageName){
        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        i.setPackage(packageName);

        List<ResolveInfo> availableApps = homePackageManager.queryIntentActivities(i, 0);
        for (ResolveInfo ri : availableApps) {
            AppInfo appinfo = new AppInfo();
            appinfo.label = ri.loadLabel(homePackageManager);
            appinfo.name = ri.activityInfo.packageName;
            appinfo.icon = ri.activityInfo.loadIcon(homePackageManager);
            homeApps.add(appinfo);
        }
    }

//    public void queryIntentActivities_ServiceMatch() throws Exception {
//        Intent i = new Intent("SomeStrangeAction");
//        ResolveInfo info = new ResolveInfo();
//        info.nonLocalizedLabel = TEST_PACKAGE_LABEL;
//        info.serviceInfo = new ServiceInfo();
//        info.serviceInfo.name = "name";
//        info.serviceInfo.packageName = TEST_PACKAGE_NAME;
//        shadowPackageManager.addResolveInfoForIntent(i, info);
//        List<ResolveInfo> activities = packageManager.queryIntentActivities(i, 0);
//        assertThat(activities).isNotNull();
//        assertThat(activities).isEmpty();
//    }

    private void loadHomeListView() {
        try {
            homeGridView = (GridView) findViewById(R.id.grid_homeApps);
            if (homeAdapter == null) {
                homeAdapter = new ArrayAdapter<AppInfo>(this, R.layout.grd_items, homeApps) {

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {

                        ViewHolderItem viewHolder = null;

                        if (convertView == null) {
                            convertView = getLayoutInflater().inflate(
                                    R.layout.grd_items, parent, false
                            );
                            viewHolder = new ViewHolderItem();
                            viewHolder.icon = (ImageView) convertView.findViewById(R.id.img_icon);
                            viewHolder.name = (TextView) convertView.findViewById(R.id.txt_name);
                            viewHolder.label = (TextView) convertView.findViewById(R.id.txt_label);

                            convertView.setTag(viewHolder);
                        } else {
                            viewHolder = (ViewHolderItem) convertView.getTag();
                        }

                        AppInfo appInfo = homeApps.get(position);

                        if (appInfo != null) {
                            viewHolder.icon.setImageDrawable(appInfo.icon);
                            viewHolder.label.setText(appInfo.label);
                            viewHolder.name.setText(appInfo.name);
                        }
                        return convertView;
                    }

                    final class ViewHolderItem {
                        ImageView icon;
                        TextView label;
                        TextView name;
                    }
                };
            }
            homeGridView.setAdapter(homeAdapter);
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage().toString() + " loadListView", Toast.LENGTH_LONG).show();
            Log.e("Error loadListView", ex.getMessage().toString() + " loadListView");
        }
    }

    public void addHomeGridListeners() {
        try {
            homeGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = homePackageManager.getLaunchIntentForPackage(homeApps.get(i).name.toString());
                    HomeActivity.this.startActivity(intent);
                }
            });
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage().toString() + " Grid", Toast.LENGTH_LONG).show();
            Log.e("Error Grid", ex.getMessage().toString() + " Grid");
        }

    }
}
