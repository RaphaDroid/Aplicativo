package com.example.aplicativo.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.widget.Toast;


public class DialogManager {
	
	public static void showToast(Context context, String msg, boolean longDuration){
		Toast.makeText( context, msg,
						(longDuration) ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT)
						.show();
	}
	
	public static void dialogOk(String title, Object view, Context context){

		final AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        if(title != null){
		    dialog.setTitle(title);
        }

		if(view instanceof String)
			dialog.setMessage((String)view);
		else
			dialog.setView((View)view);

		dialog.show();
	}
	
	public static void dialogComplex(Context context, String title, Object view,
	                                 OnClickListener yesListener, OnClickListener noListener,
	                                 String yesText, String noText){

		AlertDialog.Builder dialog = new AlertDialog.Builder(context);

		if(title != null){
			dialog.setTitle(title);
		}

		if(yesListener != null){
			dialog.setPositiveButton(yesText != null ? yesText : context.getString(R.string.yes), yesListener);
		}

		if(noListener != null){
			dialog.setNegativeButton(noText != null ? noText : context.getString(R.string.no), noListener);
		}

        if(view instanceof String)
            dialog.setMessage((String)view);
        else
            dialog.setView((View)view);

		dialog.show();
	}
	
	public static void dialogOptions(Context context, String title, String[] options,
	                                 OnClickListener itemListener){

		AlertDialog.Builder dialog = new AlertDialog.Builder(context);

		if(title != null){
			dialog.setTitle(title);
		}

		dialog.setItems(options, itemListener);

		dialog.show();
	}
}
