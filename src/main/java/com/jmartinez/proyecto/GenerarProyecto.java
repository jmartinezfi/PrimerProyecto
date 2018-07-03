package com.jmartinez.proyecto;

import java.io.File;
import java.io.IOException;

import com.google.gson.Gson;
import com.jmartinez.bean.ClaseBean;
import com.jmartinez.bean.ProyectoBean;

public class GenerarProyecto {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//String ubicacion = "../PrimerProyecto/src/main/resources/Proy01Test.json";
		String ubicacion = "../PrimerProyecto/src/test/resources/test.json";
		File archivojson = new File(ubicacion);
		if (archivojson.exists()) {
			System.out.println("Archivo encontrado");
			String datos = CrearClaseJavav3.readFile(ubicacion);
			Gson gson = new Gson();
			ProyectoBean proy = gson.fromJson(datos, ProyectoBean.class);
			if (proy != null) {
				// Creacion de proyecto
				System.out.println("Nombre:" + proy.getNombreProyecto());
				System.out.println("cantidad de archivos a crear: " + proy.getClases().size());
				//Crear paqueteria
				for (ClaseBean clase : proy.getClases()) {
					System.out.println("<a href=\""+clase.getNombre()+"Form.html\">"+clase.getNombre()+"</a>");
					CrearClaseJavav3.crearBean(proy, clase);
					//System.out.println(clase.getNombre() + ":"+clase.isoBean());
					if(!clase.isoBean()) {
						CrearClaseJavav3.creanSQL(proy, clase);
						CrearClaseJavav3.creanDAO(proy, clase);
						CrearClaseJavav3.creanDAOImpl(proy, clase);
						CrearClaseJavav3.crearServlet(proy, clase);
						CrearClaseJavav3.crearHtml(proy,clase);
					}
				}
				
			} else {
				System.out.println("Proyecto null. Terminando");
			}
		} else {
			System.out.println("Archivo No encontrado");
		}
	}

}
