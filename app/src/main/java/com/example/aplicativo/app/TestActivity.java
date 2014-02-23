package com.example.aplicativo.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class TestActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState){

		super.onCreate(savedInstanceState);
		
		// Métodos de construção
		
		setContentView(R.layout.activity_test);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu){

		// Inflate the menu; this adds items to the action bar if it
		// is present.
		getMenuInflater().inflate(R.menu.preferences, menu);
		return true;
	}

}
