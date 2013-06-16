package com.lm.weather.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CityCodeDB {
	public static final String TABLE_PROVINCE = "province";
	public static final String TABLE_CITY = "city";
	public static final String TABLE_AREA = "area";
	public static final String TABLE_CITY_CODE = "city_code";

	private Context context;

	CityCodeDB(Context context) {
		this.context = context;
	}

	SQLiteDatabase getDatabase(String dbname) {
		AssetsDatabaseManager.initManager(context);
		AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
		SQLiteDatabase db = mg.getDatabase(dbname);
		return db;
	}

	// 查询province表,返回省份信息cursor
	Cursor getAllProvince(SQLiteDatabase db) {
		if (db != null) {
			Cursor cur = db
					.query(TABLE_PROVINCE, new String[] { "id", "name" }, null,
							null, null, null, null);
			return cur;
		} else {
			return null;
		}
	}

	// 查询city表,返回指定省份的所有城市信息cursor
	Cursor getCity(SQLiteDatabase db, String provinceid) {
		if (db != null) {
			Cursor cur = db.query(TABLE_CITY, new String[] { "id", "p_id",
					"name" }, "p_id = ?", new String[] { provinceid }, null,
					null, null);
			return cur;
		} else {
			return null;
		}
	}

	// 查询area表,返回指定城市的所有地区信息cursor
	Cursor getArea(SQLiteDatabase db, String cityid) {
		if (db != null) {
			Cursor cur = db.query(TABLE_AREA, new String[] { "id", "c_id",
					"name" }, "c_id = ?", new String[] { cityid }, null, null,
					null);
			return cur;
		} else {
			return null;
		}
	}

	// 查询city_code表,通过areaid获取城市码code
	String getCityCode(SQLiteDatabase db, String areaid) {
		if (db != null) {
			Cursor cur = db.query(TABLE_CITY_CODE, new String[] { "id", "code",
					"name" }, "id = ?", new String[] { areaid }, null, null,
					null);
			String citycode = null;
			if (cur.moveToFirst()) {
				citycode = cur.getString(cur.getColumnIndex("code"));
			}
			return citycode;
		} else {
			return null;
		}
	}

	// 查询city_code表,通过areaname获取城市码code
	String getCityCodeByName(SQLiteDatabase db, String areaname) {
		if (db != null) {
			Cursor cur = db.query(TABLE_CITY_CODE, new String[] { "id", "code",
					"name" }, "name = ?", new String[] { areaname }, null,
					null, null);
			String citycode = null;
			if (cur.moveToFirst()) {
				citycode = cur.getString(cur.getColumnIndex("code"));
			}
			return citycode;
		} else {
			return null;
		}
	}
}
