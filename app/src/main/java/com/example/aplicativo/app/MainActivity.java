package com.example.aplicativo.app;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends Activity {

	//private ShareActionProvider mShareActionProvider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.activity_main);

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

		//Link
		boolean useLink = prefs.getBoolean(SettingsActivity.CHK_USE_LINK, false);

		String link = LinkManager.fixLink(prefs.getString(SettingsActivity.LINK, "www.google.com.br"));
		TextView textLink = (TextView)findViewById(R.id.text_link);

		if(useLink){
			textLink.setText(Html.fromHtml("<a href=\"" + link + "\">" + LinkManager.extractSiteName(link) + "</a>"));
			textLink.setMovementMethod(LinkMovementMethod.getInstance());
		}else
			textLink.setText(LinkManager.extractSiteName(link));

        //Fonte
		int fontType = Integer.parseInt(prefs.getString(SettingsActivity.LIST_APP_FONT, "1"));

		switch(fontType){
		case SettingsActivity.CHARLEE_DOODLES:
		    break;

        case SettingsActivity.ROBOTO_REGULAR:
            break;

        case SettingsActivity.ROBOTO_ITALIC:
            break;

        case SettingsActivity.ROBOTO_BLACK:
            break;

        case SettingsActivity.ROBOTO_BLACK_ITALIC:
            break;

        case SettingsActivity.ROBOTO_BOLD:
            break;

        case SettingsActivity.ROBOTO_BOLD_ITALIC:
            break;

        case SettingsActivity.ROBOTO_CONDENSED:
            break;

        case SettingsActivity.ROBOTO_CONDENSED_ITALIC:
            break;

        case SettingsActivity.ROBOTO_BOLD_CONDENSED:
            break;

        case SettingsActivity.ROBOTO_BOLD_CONDENSED_ITALIC:
            break;

        case SettingsActivity.ROBOTO_LIGHT:
            break;

        case SettingsActivity.ROBOTO_LIGHT_ITALIC:
            break;

        case SettingsActivity.ROBOTO_MEDIUM:
            break;

        case SettingsActivity.ROBOTO_MEDIUM_ITALIC:
            break;

        case SettingsActivity.ROBOTO_THIN:
            break;

        case SettingsActivity.ROBOTO_THIN_ITALIC:
            break;

        default:
            break;
		}

		Intent intent = getIntent();
		String action = intent.getAction();
		String type = intent.getType();

		if(action.equals(Intent.ACTION_SEND) && type != null){
			if(type.equals("text/plain")){
				String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
				if(sharedText != null){
					DialogManager.dialogOk(getString(R.string.shared_text), sharedText, this);
				}
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.preferences, menu);

		//		MenuItem item = menu.findItem(R.id.menu_item_share);
		//		mShareActionProvider = (ShareActionProvider)item.getActionProvider();

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){

		switch(item.getItemId()){
			case R.id.menu_item_share:

				callShare();
				return true;

			case R.id.action_refresh:

				this.recreate();
				return true;

			case R.id.action_settings:
				startActivity(new Intent(this, SettingsActivity.class));

			default:
				return super.onOptionsItemSelected(item);
		}
	}

	//private void setShareIntent(Intent shareIntent){
	//		
	//	if(mShareActionProvider != null){
	//		mShareActionProvider.setShareIntent(shareIntent);
	//	}
	//}

	private void callShare(){
		LinearLayout shareLayout = new LinearLayout(this);
		shareLayout.setOrientation(LinearLayout.VERTICAL);

		Space vSpace1 = new Space(this);
		vSpace1.setMinimumHeight(5);
		Space vSpace2 = new Space(this);
		vSpace2.setMinimumHeight(8);
		final EditText shareText = new EditText(this);
		shareText.setHint(R.string.share_text);

		shareLayout.addView(vSpace1);
		shareLayout.addView(shareText);
		shareLayout.addView(vSpace2);

		DialogManager.dialogComplex(this, getString(R.string.share_text), shareLayout, new OnClickListener(){
			@Override
			public void onClick(DialogInterface dialogInterface, int i){
				shareText(shareText.getText().toString());
			}
		}, null, getString(R.string.share), getString(R.string.cancel));
	}

	private void shareText(String text){

		Intent shareIntent = new Intent();

		shareIntent
				.setAction(Intent.ACTION_SEND)
				.putExtra(Intent.EXTRA_TEXT, text)
				.setType("text/plain");

		startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.share)));
	}

	public void openAuxActivity(View v){

		startActivity(new Intent(this, AuxActivity.class));
	}

	public void createNotification(View v){

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		boolean useLed = prefs.getBoolean(SettingsActivity.CHK_USE_LED, false);
		int ledColor = Integer.parseInt(prefs.getString(SettingsActivity.LIST_LED_COLOR, "-1"));
		boolean vibrate = prefs.getBoolean(SettingsActivity.CHK_VIBRATE, false);
		int vibrationDuration = prefs.getInt(SettingsActivity.VIBRATION_DURATION, 1000);

		Notification.Builder ntf =
				new Notification.Builder(this)
		.setContentTitle(getString(R.string.notif_title)) 		// Título da notificação
		.setContentText(getString(R.string.notif_text)) 	    // Texto
		.setSmallIcon(R.drawable.notification_icon_bugdroid)    // Ícone da status bar
		.setAutoCancel(true) 							        // Fecha a notificação quando o usuário clica nela
		.setContentInfo(getString(R.string.notification)) 		// Informações sobre a notificação
		.setTicker(getString(R.string.notif_test)); 		    // Texto em forma de "preview" que aparece na status bar no momento da notificação
		
		if(useLed){
			try{
				ntf.setLights(ledColor, 2000, 1000);	        // Define a cor do LED com 2 segundos aceso e 1 apagado
			}catch(Exception e){}
		}

		if(vibrate)
			this.vibrate(vibrationDuration);

		this.playNotificationRingtone();

		NotificationManager ntfMngr = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

		Intent resultIntent = new Intent(this, TestActivity.class);
		PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		ntf.setContentIntent(resultPendingIntent);	// Define a fun��o de abrir outra Activity (TestActivity.class)

		ntfMngr.notify(001, ntf.build());
	}

	public void search(View v){

		final EditText input = new EditText(this);

		input.setSingleLine(true);
		input.setHint(R.string.google_btn_text);

        DialogManager.dialogComplex(this, getString(R.string.google_btn_text), input, new OnClickListener(){
	        @Override
	        public void onClick(DialogInterface dialogInterface, int i){
		        googleSearch(input.getText().toString());
	        }
        }, null, getString(R.string.search), getString(R.string.cancel));
	}

	private void googleSearch(String search){

		if(search.isEmpty())	// Verifica a exist�ncia de conte�do na pesquisa
			// Abre a página inicial do Google
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com.br")));
		else
			// Abre o navegador com o link da pesquisa
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com.br/#q=" + search.replace(" ", "+"))));
	}

	public void openDoodles(View v){

		// Abre os Doodles do Google
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/doodles/finder/2013/All%20doodles")));
	}

	private void playNotificationRingtone(){

		Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		Ringtone ring = RingtoneManager.getRingtone(getApplicationContext(), sound);
		ring.play();	// Ativa o toque de notifica��o
	}

	private void vibrate(int duration){

		Vibrator vb = (Vibrator)getSystemService(VIBRATOR_SERVICE);
		vb.vibrate(duration);	// Ativa a vibra��o de <duration> milisegundos
	}

	public void openFileManipulation(View v){

		startActivity(new Intent(this, FileActivity.class));
	}

	public void promptReboot(View v){

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		final boolean useDownloadMode = prefs.getBoolean(SettingsActivity.CHK_REBOOT_DOWNLOAD, false);

		final String[] rebootOptions = {getString(R.string.item_reboot_system),
				getString(R.string.item_reboot_recovery),
				(useDownloadMode) ? getString(R.string.item_reboot_download) : getString(R.string.item_reboot_bootloader),
				getString(R.string.item_hot_reboot)};

		DialogManager.dialogOptions(this, getString(R.string.reboot_phone), rebootOptions,
				new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int item){

						//Descobre qual item foi selecionado e reinicia da maneira apropriada
						switch (item) {
							case 0:
								DialogManager.dialogComplex(MainActivity.this, getString(R.string.item_reboot_system), getString(R.string.prompt_reboot_phone), new OnClickListener(){

									@Override
									public void onClick(DialogInterface dialog, int which){

										rebootPhone(Constants.REBOOT_SYSTEM);

									}
								}, null, null, null);
								break;

							case 1:
								DialogManager.dialogComplex(MainActivity.this, getString(R.string.item_reboot_recovery), getString(R.string.prompt_reboot_phone), new OnClickListener(){

									@Override
									public void onClick(DialogInterface dialog, int which){

										rebootPhone(Constants.REBOOT_RECOVERY);

									}
								}, null, null, null);
								break;

							case 2:
								DialogManager.dialogComplex(MainActivity.this, getString(R.string.item_reboot_bootloader), getString(R.string.prompt_reboot_phone), new OnClickListener(){

									@Override
									public void onClick(DialogInterface dialog, int which){

										rebootPhone((useDownloadMode) ? Constants.REBOOT_DOWNLOAD : Constants.REBOOT_BOOTLOADER);

									}
								}, null, null, null);
								break;

							case 3:
								DialogManager.dialogComplex(MainActivity.this, getString(R.string.item_hot_reboot), getString(R.string.prompt_reboot_phone), new OnClickListener(){

									@Override
									public void onClick(DialogInterface dialog, int which){

										rebootPhone(Constants.HOT_REBOOT);

									}
								}, null, null, null);
								break;

							default:
								break;
						}

					}
				});

	}

	private void rebootPhone(String rebootType){

		try {
			Runtime.getRuntime().exec(new String[]{	"su",
													"-c",
													rebootType});
		} catch (IOException e) {
			DialogManager.dialogOk(getString(R.string.error), e.getMessage(), this);
		}
	}
}