package com.jmartinez.test;

import java.io.File;

public class TestCreateDirectory {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String prefixfile = "I:\\proyectos\\java\\gitProyects/webexample01/src/main/java/com/martinez/proyectoVentas/beans";
		File directorio = new File(prefixfile);
		directorio.mkdirs();
		System.out.println(":"+directorio.exists());
	}

}
