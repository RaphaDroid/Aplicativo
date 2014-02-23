package com.example.aplicativo.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.content.Context;
import android.os.Environment;

public class FileManager{

	public static final File DIRECTORY_SDCARD_ROOT = Environment.getExternalStorageDirectory();

	public static File createTextFile(String name, String content, Context context){

		if(!name.contains(".txt"))
			name += ".txt";

		File filePath = Environment.getExternalStoragePublicDirectory("Teste/" + context.getString(R.string.files));
		File textFile = new File(filePath, name);

		if(!filePath.mkdirs())
			filePath.mkdirs();

		FileOutputStream fOut = null;
		OutputStreamWriter writer = null;

		try{
			fOut = new FileOutputStream(textFile, true);
			writer = new OutputStreamWriter(fOut);

			writer.write(content);
			writer.close();

			DialogManager.showToast(context, context.getString(R.string.file_successfully_created), false);
		}catch (Exception e){
			DialogManager.dialogOk(context.getString(R.string.error), e.getMessage(), context);
		}

		return textFile;
	}

	public static String getFileContent(File file, Context context){

		StringBuilder fileText = new StringBuilder();
		BufferedReader br = null;

		try{
			br = new BufferedReader(new FileReader(file));
			String line = null;

			while((line = br.readLine()) != null){
				fileText.append(line + "\n");
			}
			br.close();
		}catch(IOException e){
			DialogManager.dialogOk(context.getString(R.string.error), e.getMessage(), context);
		}

		return new String(fileText);
	}

    public static boolean removeTextFromFile(File file, String tag, Context context){

        if(file == null || tag.equals("_TAG_"))
            return false;

        String content = getFileContent(file, context);

        while(content.contains(tag))
            content = content.replace(tag, "");

        FileOutputStream fOut = null;
        OutputStreamWriter writer = null;

        try{
            fOut = new FileOutputStream(file);
            writer = new OutputStreamWriter(fOut);

            writer.write(content);
            writer.close();

            return true;
        }catch(IOException e){
            DialogManager.dialogOk(context.getString(R.string.error), e.getMessage(), context);
        }

        return false;
    }
}