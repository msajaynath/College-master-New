package com.example.hp.college;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Adapter.MySimpleArrayAdapter;
import Entity.College;
import Entity.Courses;
import Entity.ListItem;

public class ResultActivity extends AppCompatActivity {

    List<ListItem> listItems = new ArrayList<ListItem>();
    final String networkLocationProvider = LocationManager.NETWORK_PROVIDER;
    final String gpsLocationProvider = LocationManager.GPS_PROVIDER;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ListView listview = (ListView) findViewById(R.id.searchlistview);
        Intent intent = getIntent();
        String city = intent.getStringExtra("city");
        String course = intent.getStringExtra("course");

        if ((city.length()>0 ||city.length()==0)&& course.length()==0) {
            Log.d("city", city);

            final List<College> notes = College.findWithQuery(College.class, "Select * from College where city like '%" + city + "%' ");
            for (College c : notes) {
                List<Courses> courseList = Courses.findWithQuery(Courses.class, "Select * from Courses where college = ?", c.getId() + "");
                String clist = "";
                for (Courses cc : courseList) {
                    clist = clist + ", " + cc.name;

                }
                clist = clist.replaceFirst(",", "");
                listItems.add(new ListItem(c.name, c.address, clist,c.getId(),c.rank));
                // ListItem l=new ListItem();
            }

            MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this, listItems);
            listview.setAdapter(adapter);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent in = new Intent(ResultActivity.this, DetailActivity.class);
                    in.putExtra("logid", notes.get(position).getId());
                    in.putExtra("courses", listItems.get(position).course);
                    startActivity(in);

                }
            });


        }


        else if (city.length()>0 && course.length()>0) {
            Log.d("city", city);

            final List<College> notes = College.findWithQuery(College.class, "Select * from College where city like '%" + city + "%' ");
            for (College c : notes) {
                List<Courses> courseList = Courses.findWithQuery(Courses.class, "Select * from Courses where college = ?", c.getId() + "");
                String clist = "";
                for (Courses cc : courseList) {
                    clist = clist + ", " + cc.name;

                }
                clist = clist.replaceFirst(",", "");
                if(clist.contains(course))

                    listItems.add(new ListItem(c.name, c.address, clist,c.getId(),c.rank));
                // ListItem l=new ListItem();
            }

            MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this, listItems);
            listview.setAdapter(adapter);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent in = new Intent(ResultActivity.this, DetailActivity.class);
                    in.putExtra("logid",  listItems.get(position).id);
                    in.putExtra("courses", listItems.get(position).course);
                    startActivity(in);

                }
            });


        }


        else   if (city.length()==0 && course.length()>=0) {

            final List<Courses> notes = Courses.findWithQuery(Courses.class, "Select * from Courses where name like '%" + course + "%'");
            for (Courses c : notes) {
                College col = College.findById(College.class, c.college.getId());

                listItems.add(new ListItem(col.name, col.address, course,col.getId(),col.rank));
                // ListItem l=new ListItem();
            }

            Collections.sort(listItems, new Comparator<ListItem>() {
                @Override
                public int compare(ListItem z1, ListItem z2) {
                    if (z1.rank > z2.rank)
                        return 1;
                    if (z1.rank < z2.rank)
                        return -1;
                    return 0;
                }
            });

            MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this, listItems);
            listview.setAdapter(adapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent in = new Intent(ResultActivity.this, DetailActivity.class);
                    in.putExtra("logid", listItems.get(position).id);
                    in.putExtra("courses", listItems.get(position).course);
                    startActivity(in);


                }
            });

        }







    }

    public Location getLastKnownLoaction(boolean enabledProvidersOnly, Context context) {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Location utilLocation = null;
        List<String> providers = manager.getProviders(enabledProvidersOnly);
        for (String provider : providers) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    Toast.makeText(getApplicationContext(), "Permission not granted to access device location", Toast.LENGTH_SHORT).show();            }
            }
            utilLocation = manager.getLastKnownLocation(provider);
            if(utilLocation != null) return utilLocation;
        }
        return null;
    }

}
