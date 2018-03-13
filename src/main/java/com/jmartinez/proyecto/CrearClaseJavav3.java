package com.jmartinez.proyecto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.jmartinez.bean.AtributosBean;
import com.jmartinez.bean.ClaseBean;
import com.jmartinez.bean.ProyectoBean;

public class CrearClaseJavav3 {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		// Convertir el json Proy01Test a String
		String ubicacion = "../PrimerProyecto/src/main/resources/Proy01Test.json";
		File archivojson = new File(ubicacion);
		if (archivojson.exists()) {
			System.out.println("Archivo encontrado");
			String datos = readFile(ubicacion);
			Gson gson = new Gson();
			ProyectoBean proy = gson.fromJson(datos, ProyectoBean.class);
			if (proy != null) {
				// Creacion de proyecto
				System.out.println("Nombre:" + proy.getNombreProyecto());
				System.out.println("cantidad de archivos a crear" + proy.getClases().size());
				for (ClaseBean clase : proy.getClases()) {
					crearBean(proy, clase);
					creanDAO(proy, clase);
				}
			} else {
				System.out.println("Proyecto null. Terminando");
			}
		} else {
			System.out.println("Archivo No encontrado");
		}
	}

	private static void creanDAO(ProyectoBean proy, ClaseBean clase) throws IOException {
		// TODO Auto-generated method stub
		String prefixfile = proy.getUbicacionProyecto() + proy.getNombreProyecto() + proy.getRutamvn()
				+ clase.getPaqueteria().replace(".", "/") + "/";
		System.out.println("ruta : " + prefixfile);
		String nombreClase = clase.getNombre();
		File archivo = new File(prefixfile + "dao/" + nombreClase + "Dao.java");
		String informacion = "";
		BufferedWriter bw;
		String nombreBean = ""+clase.getPaqueteria()+".bean."+nombreClase+"Bean";
		bw = new BufferedWriter(new FileWriter(archivo));
		informacion += "package " + clase.getPaqueteria() + ".dao";
		informacion += ";\n";
		informacion += "public interface " + nombreClase + "Dao { " + "\n";
		informacion += "\tpublic void insert ("+nombreBean+" dato);\n";
		informacion += "\tpublic void insertSelective ("+nombreBean+" dato);\n";
		informacion += "\tpublic void update ("+nombreBean+" dato);\n";
		informacion += "\tpublic void updateSelective ("+nombreBean+" dato);\n";
		informacion += "\tpublic java.util.ArrayList<"+nombreBean+"> select();\n";
		informacion += "\n";
		informacion += "}";
		bw.write(informacion);
		bw.close();
		System.out.println("" + clase.getNombre() + ": creada correctamente");
	}

	private static void crearBean(ProyectoBean proy, ClaseBean clase) throws IOException {
		String prefixfile = proy.getUbicacionProyecto() + proy.getNombreProyecto() + proy.getRutamvn()
				+ clase.getPaqueteria().replace(".", "/") + "/";
		System.out.println("ruta : " + prefixfile);
		String nombreClase = clase.getNombre();
		File archivo = new File(prefixfile + "bean/" + nombreClase + "Bean.java");
		String informacion = "";
		BufferedWriter bw;
		bw = new BufferedWriter(new FileWriter(archivo));
		informacion += "package " + clase.getPaqueteria() + ".bean";
		informacion += ";\n";
		informacion += "public class " + nombreClase + "Bean { " + "\n";
		for (AtributosBean atributo : clase.getAtributos()) {
			informacion += "\t" + "private " + atributo.getClase() + " " + atributo.getNombre() + "";
			if (!atributo.getAutomatico().isEmpty()) {
				informacion += " = " + atributo.getAutomatico();
			}
			informacion += ";\n";
			if (atributo.isIsget()) {
				informacion += "\t" + "public " + atributo.getClase() + " get" + atributo.getNombreClase() + "() { "
						+ "\n";
				informacion += "\t" + "\t" + "return " + atributo.getNombre() + ";" + "\n";
				informacion += "\t" + "}" + "\n";
			}
			if (atributo.isIsset()) {
				informacion += "\t" + "public void set" + atributo.getNombreClase() + "(" + atributo.getClase() + " "
						+ atributo.getNombre() + ") { " + "\n";
				informacion += "\t" + "\t" + "this." + atributo.getNombre() + " = " + atributo.getNombre() + ";" + "\n";
				informacion += "\t" + "}" + "\n";
			}
		}
		informacion += "\n";
		informacion += "}";
		bw.write(informacion);
		bw.close();
		System.out.println("" + clase.getNombre() + ": creada correctamente");
	}

	private static String readFile(String file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader((file)));
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		String ls = System.getProperty("line.separator");

		try {
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append(ls);
			}

			return stringBuilder.toString();
		} finally {
			reader.close();
		}
	}

}
