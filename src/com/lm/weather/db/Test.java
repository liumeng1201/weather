package com.lm.weather.db;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.lm.weather.MainActivity;
import com.lm.weather.R;

public class Test extends Activity {
	Spinner SPprovince, SPcity, SParea;
	Button btnOK, btnSearch;
	EditText etSearch;
	TextView tv;
	List<String> provinceid, provincename;
	List<String> cityid, cityname;
	List<String> areaid, areaname;
	String province;
	String city;
	String area;
	String citycode;
	String citycode_name;
	CityCodeDB citycodedb = null;
	SQLiteDatabase db = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);

		init();
		initProvinceSpinner(db);

		btnOK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Test.this, MainActivity.class);
				intent.putExtra("citycode", citycode);
				intent.putExtra("citycode_name", citycode_name);
				startActivity(intent);
			}
		});

		btnSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String cityname = etSearch.getText().toString();
				citycode_name = cityname;
				citycode = searchCityCodeByName(db, cityname);
				if (citycode != null) {
					Intent intent = new Intent(Test.this, MainActivity.class);
					intent.putExtra("citycode", citycode);
					intent.putExtra("citycode_name", citycode_name);
					startActivity(intent);
				} else {
					tv.setText("您查找的城市或地区不存在!");
				}
			}
		});
	}

	// 执行初始化操作
	void init() {
		SPprovince = (Spinner) findViewById(R.id.province);
		SPcity = (Spinner) findViewById(R.id.city);
		SParea = (Spinner) findViewById(R.id.area);
		btnOK = (Button) findViewById(R.id.btnOK);
		btnSearch = (Button) findViewById(R.id.btnSearch);
		etSearch = (EditText) findViewById(R.id.search);
		tv = (TextView) findViewById(R.id.citycode);
		provinceid = new ArrayList<String>();
		provincename = new ArrayList<String>();
		cityid = new ArrayList<String>();
		cityname = new ArrayList<String>();
		areaid = new ArrayList<String>();
		areaname = new ArrayList<String>();

		citycodedb = new CityCodeDB(Test.this);
		db = citycodedb.getDatabase("data.db");
	}

	// 初始化省/直辖市Spinner
	void initProvinceSpinner(SQLiteDatabase database) {
		Cursor provincecursor = citycodedb.getAllProvince(database);

		if (provincecursor != null) {
			provinceid.clear();
			provincename.clear();
			if (provincecursor.moveToFirst()) {
				do {
					String province_id = provincecursor
							.getString(provincecursor.getColumnIndex("id"));
					String province_name = provincecursor
							.getString(provincecursor.getColumnIndex("name"));
					provinceid.add(province_id);
					provincename.add(province_name);
				} while (provincecursor.moveToNext());
			}
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(Test.this,
				android.R.layout.simple_list_item_1, android.R.id.text1,
				provincename);
		SPprovince.setAdapter(adapter);

		OnItemSelectedListener listener = new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View v,
					int position, long id) {
				// TODO Auto-generated method stub
				initCitySpinner(db, provinceid.get(position).toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		};

		SPprovince.setOnItemSelectedListener(listener);
	}

	// 初始化市Spinner
	void initCitySpinner(SQLiteDatabase database, String provinceid) {
		Cursor citycursor = citycodedb.getCity(database, provinceid);
		if (citycursor != null) {
			cityid.clear();
			cityname.clear();
			if (citycursor.moveToFirst()) {
				do {
					String city_id = citycursor.getString(citycursor
							.getColumnIndex("id"));
					String city_name = citycursor.getString(citycursor
							.getColumnIndex("name"));
					String province = citycursor.getString(citycursor
							.getColumnIndex("p_id"));
					cityid.add(city_id);
					cityname.add(city_name);
				} while (citycursor.moveToNext());
			}
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(Test.this,
				android.R.layout.simple_list_item_1, android.R.id.text1,
				cityname);
		SPcity.setAdapter(adapter);

		OnItemSelectedListener listener = new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View v,
					int position, long id) {
				// TODO Auto-generated method stub
				initAreaSpinner(db, cityid.get(position).toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		};
		SPcity.setOnItemSelectedListener(listener);
	}

	// 初始化地区Spinner,同时获取城市码
	void initAreaSpinner(SQLiteDatabase database, String cityid) {
		Cursor areacursor = citycodedb.getArea(db, cityid);
		if (areacursor != null) {
			areaid.clear();
			areaname.clear();
			if (areacursor.moveToFirst()) {
				do {
					String area_id = areacursor.getString(areacursor
							.getColumnIndex("id"));
					String area_name = areacursor.getString(areacursor
							.getColumnIndex("name"));
					String city = areacursor.getString(areacursor
							.getColumnIndex("c_id"));
					areaid.add(area_id);
					areaname.add(area_name);
				} while (areacursor.moveToNext());
			}
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(Test.this,
				android.R.layout.simple_list_item_1, android.R.id.text1,
				areaname);
		SParea.setAdapter(adapter);

		OnItemSelectedListener listener = new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View v,
					int position, long id) {
				// TODO Auto-generated method stub
				citycode_name = areaname.get(position).toString();
				citycode = citycodedb.getCityCode(db, areaid.get(position)
						.toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		};
		SParea.setOnItemSelectedListener(listener);
	}

	// 通过城市名查找城市码
	String searchCityCodeByName(SQLiteDatabase database, String cityname) {
		String citycode = null;
		citycode = citycodedb.getCityCodeByName(database, cityname);
		return citycode;
	}
}
