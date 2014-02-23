package com.example.aplicativo.app;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class FileActivity extends Activity {
	

	@Override
	protected void onCreate(Bundle savedInstanceState){

		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_file);
	}
	
	public void createFile(View v){
		
		EditText editFileName = (EditText)findViewById(R.id.editFileName);
		EditText editFileContent = (EditText)findViewById(R.id.editFileContent);
		
		if(editFileName.getText().toString().isEmpty() || editFileContent.getText().toString().isEmpty()){
			DialogManager.showToast(this, getString(R.string.file_error_empty_fields), false);
			return;
		}
				
		File textFile = FileManager.createTextFile(editFileName.getText().toString(), editFileContent.getText().toString(), this);
		
		DialogManager.dialogOk(this.getString(R.string.success),
				this.getString(R.string.path) + ":\n\n" + textFile.getAbsolutePath(),
				this);
	}
	
	public void clearFields(View v){

		EditText editFileName = (EditText)findViewById(R.id.editFileName);
		EditText editFileContent = (EditText)findViewById(R.id.editFileContent);
		
		if(editFileName.getText().toString() != "")
			editFileName.setText("");
		
		if(editFileName.getText().toString() != "")
			editFileContent.setText("");
	}
	
	public void callChooseCreatedFile(View v){
		DialogManager.showToast(this, "TODO", false);
	}
}
