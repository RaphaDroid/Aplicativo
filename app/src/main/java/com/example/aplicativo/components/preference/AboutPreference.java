package com.example.aplicativo.components.preference;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.aplicativo.app.R;

import java.util.Locale;


public class AboutPreference extends DialogPreference
	implements DialogInterface.OnClickListener, DialogInterface.OnDismissListener{

	private AlertDialog.Builder mBuilder;
	private Dialog mDialog;

	private String mTitle;
	private TextView mPreferenceTitle;

	public AboutPreference(Context context, AttributeSet attrs){
		super(context, attrs);

		mTitle = context.getString(R.string.about);
	}

	@Override
	protected View onCreateView(ViewGroup parent) {

		View layout = View.inflate(getContext(), R.layout.about_preference, null);

		mPreferenceTitle = (TextView) layout.findViewById(R.id.about_title);
		mPreferenceTitle.setText(mTitle);

		return layout;
	}

	@Override
	protected void showDialog(Bundle state){

		Context context = getContext();

		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);

		TextView text = new TextView(context);
		text.setPadding(30, 10, 30, 20);
		text.setText(Html.fromHtml(Locale.getDefault().getLanguage().equals("pt") ?
			"Autor: <b>Raphael Schwinden da Silveira</b>, também conhecido como <b>RaphaDroid.</b><br/>Esta aplicação vem sendo desenvolvida com o único propósito de aprender e ensinar, sem fins lucrativos e com o código-fonte disponível em sua totalidade.<br/><br/><br/><b>Tópico no BrasilDroid:<br/></b><a href=\"http://brasildroid.com.br/android-basico/23307-basico-pouco-que-hello-world.html\">http://brasildroid.com.br/android-basico/23307-basico-pouco-que-hello-world.html</a><br/><br/><b>Código-fonte (obrigado GitHub):<br/></b><a href=\"https://github.com/RaphaDroid/Aplicativo\">https://github.com/RaphaDroid/Aplicativo</a>" :
			"Author: <b>Raphael Schwinden da Silveira</b>, a.k.a. <b>RaphaDroid</b>.</b><br/>This application is being developed with the teatching and learning purposes only, does not have commercial ends and got its entire source code available.<br/><br/><br/><b>BrasilDroid thread: </b><a href=\"http://brasildroid.com.br/android-basico/23307-basico-pouco-que-hello-world.html\">http://brasildroid.com.br/android-basico/23307-basico-pouco-que-hello-world.html</a><br/><b>Source code (thanks to Github): </b><a href=\"https://github.com/RaphaDroid/Aplicativo\">https://github.com/RaphaDroid/Aplicativo</a>"));
		text.setMovementMethod(LinkMovementMethod.getInstance());

		layout.addView(text);


		mBuilder = new AlertDialog.Builder(context)
				.setTitle(mTitle)
				.setPositiveButton(context.getString(R.string.close), this);

		mBuilder.setView(layout);

		onPrepareDialogBuilder(mBuilder);

		//getPreferenceManager().registerOnActivityDestroyListener(this);

		// Create the dialog
		final Dialog dialog = mDialog = mBuilder.create();
		if (state != null) {
			dialog.onRestoreInstanceState(state);
		}
		dialog.setOnDismissListener(this);
		dialog.show();
	}
}
