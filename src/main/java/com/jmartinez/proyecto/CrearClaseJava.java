package com.jmartinez.proyecto;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CrearClaseJava {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		// definiar ubicacion del proyecto
		// crear paqueteria base
		// Leer un bean con los valores a crear.
		// Crear un bean
		// Crear acceso a base de datos
		// crear la capa de servicios
		// Crear el front.
		System.out.println("Automatización de un proyecto Java");
		System.out.println("Carperta de creación. Nombre de proyecto");
		String ubicacionProyecto = "../";
		String nombreProyecto = "ProyectoVentas";
		String rutamvn = "/src/main/java/";
		String paqueteria = "com.martinez.proyectoVentas.bean";
		String prefixfile = ubicacionProyecto + nombreProyecto + rutamvn + paqueteria.replace(".", "/") + "/";
		System.out.println("ruta : " + prefixfile);
		// Falta crear la carperta y sub directorios correctamente

		prefixfile = "../";
		String nombreClase = "ArchivoBean";
		File archivo = new File(prefixfile + nombreClase+".java");
		String informacion = "";
		BufferedWriter bw;
		/**if (archivo.exists()) {
			bw = new BufferedWriter(new FileWriter(archivo));
			informacion = "El fichero de texto ya estaba creado. 1";
		} else {
			bw = new BufferedWriter(new FileWriter(archivo));
			informacion = "Acabo de crear el fichero de texto.";
		}*/
		bw = new BufferedWriter(new FileWriter(archivo));
		informacion += "package "+paqueteria;
		informacion += ";\n";
		informacion += "public class "+nombreClase+" {";
		informacion += "\n";
		informacion += "\n";
		informacion += "}";
		bw.write(informacion);
		bw.close();

	}
}