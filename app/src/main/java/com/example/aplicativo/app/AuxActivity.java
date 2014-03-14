package com.example.aplicativo.app;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Space;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class AuxActivity extends Activity {

	private DBHelper dbHelper;
	private SQLiteDatabase db;

	ListView list;
    ArrayAdapter<String> listAdapter;
    ArrayList<String> dbEntries;

	//Variáveis usadas para captura de fotos
	String mCurentPhotoPath;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aux);
		
		//Banco de Dados
		dbHelper = new DBHelper(this, "TESTE", 10); //A versão 1 tinha "description"
		db = dbHelper.getWritableDatabase();
		dbEntries = new ArrayList<String>();

		if(getGreatesId() == 0)
			insertRoot();

		loadDB();

		//ListView
        list = (ListView)findViewById(R.id.listView);

        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dbEntries);
        list.setAdapter(listAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
	            String[] data = select(new String[]{DBHelper.FIELD_NOME, DBHelper.FIELD_ID, DBHelper.FIELD_INDEX}, index);

	            DialogManager.dialogOk(data[0], getString(R.string.item_id) + ": " + data[1] + "\n" + getString(R.string.item_index) + ": " + data[2], AuxActivity.this);
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int index, long l) {
	            final String[] itemName = select(new String[]{DBHelper.FIELD_NOME}, index);

	            DialogManager.dialogOptions(AuxActivity.this, itemName[0],
			            new String[]{getString(R.string.list_item_edit), getString(R.string.list_remove_item)},
			            new OnClickListener(){
				            @Override
				            public void onClick(DialogInterface dialogInterface, int item){
					            switch(item){
						            case Constants.ITEM_EDIT:

							            LinearLayout editLayout = new LinearLayout(AuxActivity.this);
							            editLayout.setOrientation(LinearLayout.VERTICAL);

							            Space vSpace1 = new Space(AuxActivity.this);
							            vSpace1.setMinimumHeight(5);
							            Space vSpace2 = new Space(AuxActivity.this);
							            vSpace2.setMinimumHeight(8);
							            final EditText nameText = new EditText(AuxActivity.this);
							            nameText.setSingleLine(true);
							            nameText.setHint(R.string.item_name);
							            nameText.setText(itemName[0]);
							            nameText.setSelection(0, itemName[0].length());
							            nameText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

							            editLayout.addView(vSpace1);
							            editLayout.addView(nameText);
							            editLayout.addView(vSpace2);
							            DialogManager.dialogComplex(AuxActivity.this, getString(R.string.list_item_edit), editLayout, new OnClickListener(){
								            @Override
								            public void onClick(DialogInterface dialogInterface, int i){
									            update(nameText.getText().toString(), index);
									            listAdapter.notifyDataSetChanged();
									            reloadItem(index);
								            }
							            }, null, getString(R.string.confirm), getString(R.string.cancel));

							            break;

						            case Constants.ITEM_REMOVE:
							            DialogManager.dialogComplex(AuxActivity.this, getString(R.string.list_remove_item), getString(R.string.sure_remove_item), new OnClickListener(){
								            @Override
								            public void onClick(DialogInterface dialogInterface, int i){
									            dbEntries.remove(index);
									            delete(index);
									            updateIndexes(index, dbEntries.size());
									            listAdapter.notifyDataSetChanged();
									            reloadDB(index);
								            }
							            }, null, null, null);

							            break;

						            default:
							            return;
					            }
				            }
			            });

                return true;
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu){

		// Inflate the menu; this adds items to the action bar if it
		// is present.
		getMenuInflater().inflate(R.menu.action_bar_refresh, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){

		switch(item.getItemId()){
			case R.id.action_refresh:

				this.recreate();
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		
		switch(requestCode){
		
		case Constants.REQUEST_SELECT_PHONE_NUMBER:{
			if(resultCode == RESULT_OK){
				// Detecta o URI e cria a QUERY para buscar informações do contato
				Uri contactUri = data.getData();

				String[] numberProjection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
				Cursor numberCursor = getContentResolver().query(contactUri, numberProjection, null, null, null);

				// Se o CURSOR retornado for válido, ele pega o número de telefone
				if(numberCursor != null && numberCursor.moveToFirst()){
					int numberIndex = numberCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
					String number = numberCursor.getString(numberIndex);
					DialogManager.dialogOk(getString(R.string.contact_phone), number, this);
				}
			}
			break;
		}
		
		case Constants.REQUEST_IMAGE_CAPTURE:{
			if(resultCode == RESULT_OK){
				DialogManager.showToast(this, mCurentPhotoPath, true);
			}
			break;
		}
		
		default:
			break;
		}
	}

	private void takePicture(){
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if(intent.resolveActivity(getPackageManager()) != null){
			
			File photoFile = null;
			try{
				photoFile = createImageFile();
			}catch(IOException e){
				DialogManager.dialogOk(getString(R.string.error), e.getMessage(), this);
			}
			
			if(photoFile != null){
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
			}
			startActivityForResult(intent, Constants.REQUEST_IMAGE_CAPTURE);
		}
	}
	
	public void pickContactNumber(View v){
		// Inicia uma Intent para o usuário escolher um contato
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
		if(intent.resolveActivity(getPackageManager()) != null){
			startActivityForResult(intent, Constants.REQUEST_SELECT_PHONE_NUMBER);
		}
	}

	private void galleryAddPic(){
		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File photo = new File(mCurentPhotoPath);
		Uri contentUri = Uri.fromFile(photo);
		mediaScanIntent.setData(contentUri);
		this.sendBroadcast(mediaScanIntent);
	}
	
	private File createImageFile() throws IOException{
		String timeStamp = new SimpleDateFormat("yyyyMMDD_HHmmss").format(new Date());
		String imageFileName = timeStamp + "_";
		
		File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(
				imageFileName,  //Prefixo
				".jpg", 		//Sufixo
				storageDir	    //Caminho
		);
		
		mCurentPhotoPath = image.getAbsolutePath();
		galleryAddPic();
		return image;
	}
	
	public void callOpenCamera(View v){
		takePicture();
	}

    public void callAddItemToListView(View v){
	    LinearLayout insertLayout = new LinearLayout(this);
	    insertLayout.setOrientation(LinearLayout.VERTICAL);

	    final EditText textName = new EditText(this);
	    textName.setSingleLine(true);
	    textName.setHint(R.string.item_name);
	    textName.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

	    insertLayout.addView(textName);

        DialogManager.dialogComplex(this, getString(R.string.list_add_item), insertLayout, new OnClickListener(){
	        @Override
	        public void onClick(DialogInterface dialogInterface, int i){
		        insert(textName.getText().toString());
		        listAdapter.notifyDataSetChanged();
		        dbEntries.add(textName.getText().toString());
	        }
        }, null, getString(R.string.add), getString(R.string.cancel));
    }

	private int getGreatesId(){
		Cursor cursor = db.query(false, DBHelper.TABLE, new String[] {"MAX(_id)"}, null, null, null, null, null, null, null);

		cursor.moveToNext();
		int id = cursor.getInt(0);

		return id;
	}

	private String selectNameById(int id){
		Cursor cursor = db.query(DBHelper.TABLE, new String[] {DBHelper.FIELD_NOME},
				DBHelper.FIELD_ID + " = ?", new String[] {String.valueOf(id)}, null, null, null);

		String result = null;// = new String[2];

		while(cursor.moveToNext()){
			result = cursor.getString(0);
		}

		cursor.close();

		return result;
	}

	private String selectNameByIndex(int index){
		Cursor cursor = db.query(DBHelper.TABLE, new String[] {DBHelper.FIELD_NOME},
				DBHelper.FIELD_INDEX + " = ?", new String[] {String.valueOf(index)}, null, null, null);

		String result = null;

		while(cursor.moveToNext()){
			result = cursor.getString(0);
		}

		cursor.close();

		return result;
	}

	private int selectIdByIndex(int index){
		Cursor cursor = db.query(DBHelper.TABLE, new String[] {DBHelper.FIELD_ID},
				DBHelper.FIELD_INDEX + " = ?", new String[] {String.valueOf(index)}, null, null, null);

		int result = 0;

		while(cursor.moveToNext()){
			result = cursor.getInt(0);
		}

		cursor.close();

		return result;
	}

	private String[] select(String[] attrs, int index){
		Cursor cursor = db.query(DBHelper.TABLE, attrs, DBHelper.FIELD_INDEX + " = ?", new String[]{String.valueOf(index + 1)},
				null, null, null);

		String[] result = new String[attrs.length];

		cursor.moveToNext();

		for(int i = 0; i < attrs.length; i++){
			result[i] = cursor.getString(i);
		}

		cursor.close();

		return result;
	}

	private String[] selectBatch(int startIndex){
		Cursor cursor = db.query(DBHelper.TABLE, new String[]{DBHelper.FIELD_NOME}, DBHelper.FIELD_INDEX + " >= ?",
				new String[]{String.valueOf(startIndex)}, null, null, null);

		ArrayList<String> result = new ArrayList<String>();

		while(cursor.moveToNext()){
			result.add(cursor.getString(0));
		}

		String[] names = new String[result.size()];

		for(int i = 0; i < names.length; i++){
			names[i] = result.get(i);
		}

		return names;
	}

	private void update(String name, int index){
		ContentValues values = new ContentValues();

		values.put(DBHelper.FIELD_NOME, name);

		db.update(DBHelper.TABLE, values, DBHelper.FIELD_INDEX + " = ?", new String[]{String.valueOf(index + 1)});
	}

	private void updateIndexes(int startIndex, int endIndex){
		startIndex++;
		endIndex++;

		ContentValues values = null;
		for(int i = (startIndex); i <= (endIndex); i++){
			values = new ContentValues();

			values.put(DBHelper.FIELD_INDEX, i);

			db.update(DBHelper.TABLE, values, DBHelper.FIELD_INDEX + " = ?", new String[]{String.valueOf(i + 1)});
		}
	}

	private void delete(int index){
		db.delete(DBHelper.TABLE, DBHelper.FIELD_INDEX + " = ?",
				new String[]{String.valueOf(index + 1)});
	}

	private void insert(String name){
		ContentValues values = new ContentValues();

		values.put(DBHelper.FIELD_INDEX, dbEntries.size() + 1);
		values.put(DBHelper.FIELD_NOME, name);

		db.insert(DBHelper.TABLE, null, values);
	}

	private void loadDB(){
		if(dbEntries.size() != 0)
			dbEntries.clear();

		//String[] names = selectBatch(1);

		for(String name : selectBatch(1)){
			dbEntries.add(name);
		}
	}

	private void reloadItem(int index){
		dbEntries.set(index, selectNameByIndex(index + 1));
	}

	private void insertRoot(){
		ContentValues values = new ContentValues();

		values.put(DBHelper.FIELD_ID, 1);
		values.put(DBHelper.FIELD_NOME, "root");
		values.put(DBHelper.FIELD_INDEX, 0);

		db.insert(DBHelper.TABLE, null, values);
	}

	private void reloadDB(int startIndex){
		startIndex++;

		String[] names = selectBatch(startIndex - 1);

		for(int i = startIndex; i < dbEntries.size(); i++){
			dbEntries.set(i, names[i]);
		}
	}
}
