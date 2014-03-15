package com.example.aplicativo.app;


public class Constants {
	
	public Constants(){}

	//DB Items operations constants
	public static final int ITEM_EDIT = 0;
	public static final int ITEM_REMOVE = 1;

	//Reboot type constants
	public static final String REBOOT_SYSTEM = "reboot now";
	public static final String REBOOT_RECOVERY = "reboot recovery";
	public static final String REBOOT_BOOTLOADER = "reboot bootloader";
	public static final String HOT_REBOOT = "busybox killall system_server";
	public static final String REBOOT_DOWNLOAD = "reboot download";
	
	//Media type constants
	public static final int REQUEST_SELECT_PHONE_NUMBER = 1;
	public static final int REQUEST_IMAGE_CAPTURE = 2;
	
	//Links
	public static String GOOGLE = "https://www.google.com.br";
}
