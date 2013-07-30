package com.danilov.AndroidLikeThreadModelLibrary;

public class Utils {
	
		public static void log(String tag, String str) {
			System.out.println(tag + ": " + str);
		}
		
		public static void log(String str) {
			System.out.println(str);
		}
		
		public static void log(String tag, String str, Exception e) {
			System.out.println(tag + ": " + str + "\n["
					+ e.getStackTrace().toString() + "]");
		}
}
