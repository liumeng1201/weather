package com.lm.weather.test;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import com.lm.weather.R;

public class ActionBarTest extends Activity {
	ActionBar actionBar;
	OnNavigationListener mNavigationCallback;
	SpinnerAdapter mSpinnerAdapter;
	String[] action_list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actionbar_test);

		action_list = getResources().getStringArray(R.array.action_list);

		mNavigationCallback = new OnNavigationListener() {
			@Override
			public boolean onNavigationItemSelected(int itemPosition,
					long itemId) {
				// TODO Auto-generated method stub
				ListContnetFragment fragment = new ListContnetFragment();
				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.replace(R.id.fragment_container, fragment,
						action_list[itemPosition]);
				ft.commit();
				return true;
			}
		};

		mSpinnerAdapter = new ArrayAdapter<String>(getApplication(),
				android.R.layout.simple_list_item_1, android.R.id.text1,
				action_list);

		actionBar = getActionBar();
		actionBar.setTitle(null);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		actionBar.setListNavigationCallbacks(mSpinnerAdapter,
				mNavigationCallback);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}
}
