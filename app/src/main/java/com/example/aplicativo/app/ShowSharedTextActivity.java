package com.example.aplicativo.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ShowSharedTextActivity extends Activity{

	String mSharedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_shared_text);

	    final TextView text = (TextView) findViewById(R.id.shared_text);

	    //Texto compartilhado
	    Intent intent = getIntent();
	    String action = intent.getAction();
	    String type = intent.getType();

	    if(action.equals(Intent.ACTION_SEND) && type != null){
		    if(type.equals("text/plain")){
			    mSharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
			    if(mSharedText != null){
				    if(LinkManager.verifyHasLink(mSharedText)){
					    text.setText(Html.fromHtml(LinkManager.parseLinks(mSharedText)));
					    text.setMovementMethod(LinkMovementMethod.getInstance());
				    }else{
					    text.setText(Html.fromHtml(mSharedText));
				    }
			    }
		    }
	    }
    }

	public void killActivity(View v){
		this.finish();
	}

	public void saveToFile(View v){
		try{
			String timeStamp = new SimpleDateFormat("yyyyMMDD_HHmmss").format(new Date());
			FileManager.createTextFile(getString(R.string.shared_text) + "_" + timeStamp, mSharedText, this);
		}catch(Exception e){
			DialogManager.showToast(this, e.getMessage(), false);
		}

		killActivity(null);
	}
}
