package com.jmartinez.proyecto;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.jmartinez.bean.AtributosBean;
import com.jmartinez.bean.ClaseBean;
import com.jmartinez.bean.ProyectoBean;

public class CrearClaseJavav2 {
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
		ProyectoBean proy = new ProyectoBean();
		proy.setNombreProyecto("ProyectoVentas");
		proy.setUbicacionProyecto("../");
		proy.setRutajava("/src/main/java/");
		ClaseBean archivobean = new ClaseBean();
		archivobean.setNombre("archivoBean");
		archivobean.setPaqueteria("com.martinez.proyectoVentas.bean");
		AtributosBean atributo1 = new AtributosBean();
		atributo1.setClase("java.lang.String");
		atributo1.setNombre("nombre");
		atributo1.setAutomatico("\"\"");
		AtributosBean atributo2 = new AtributosBean();
		atributo2.setClase("java.lang.String");
		atributo2.setNombre("apellido");
		List<AtributosBean> atributos = new ArrayList<>();
		atributos.add(atributo1);
		atributos.add(atributo2);
		archivobean.setAtributos(atributos );
		List<ClaseBean> clases = new ArrayList<>();
		clases.add(archivobean);
		ClaseBean archivobean2 = new ClaseBean();
		archivobean2.setNombre("archivoBean2");
		archivobean2.setPaqueteria("com.martinez.proyectoVentas.bean");
		archivobean2.setAtributos(atributos );
		clases.add(archivobean2);
		proy.setClases(clases );
		Gson gson = new Gson();
		String json = gson.toJson(proy);
		//response = gson.fromJson(valor, BuscaRazonSocialResponse.class);
		System.out.println("json: "+json);
		/*** {"nombreProyecto":"ProyectoVentas","ubicacionProyecto":"../","rutamvn":"/src/main/java/",
		 * "clases":[{"paqueteria":"com.martinez.proyectoVentas.bean","nombre":"archivoBean",
		 * "atributos":[{"clase":"java.lang.String","nombre":"nombre","isset":true,"isget":true,"automatico":"\"\""},
		 * {"clase":"java.lang.String","nombre":"apellido","isset":true,"isget":true}]},
		 * {"paqueteria":"com.martinez.proyectoVentas.bean","nombre":"archivoBean2",
		 * "atributos":[{"clase":"java.lang.String","nombre":"nombre","isset":true,"isget":true,"automatico":"\"\""},
		 * {"clase":"java.lang.String","nombre":"apellido","isset":true,"isget":true}]}]}**/
		System.out.println("cantidad de archivos a crear"+proy.getClases().size());
		for (ClaseBean clase : proy.getClases()) {
			String prefixfile = proy.getUbicacionProyecto() + proy.getNombreProyecto() + proy.getRutajava() + clase.getPaqueteria().replace(".", "/") + "/";
			System.out.println("ruta : " + prefixfile);
			// Falta crear la carperta y sub directorios correctamente
			prefixfile = "../";
			String nombreClase =clase.getNombre();
			File archivo = new File(prefixfile + nombreClase+".java");
			String informacion = "";
			BufferedWriter bw;
			bw = new BufferedWriter(new FileWriter(archivo));
			informacion += "package "+clase.getPaqueteria();
			informacion += ";\n";
			informacion += "public class "+nombreClase+" { " + "\n";
			
			for (AtributosBean atributo : clase.getAtributos()) {
				
				informacion += "\t"+"private "+atributo.getClase()+" "+atributo.getNombre()+"" ;
				
				if(!atributo.getAutomatico().isEmpty()) {
					informacion += " = "+atributo.getAutomatico();
				}
				informacion +="; \n";
				//if(atributo.isIsget()) {
					informacion +="\t"+"public "+atributo.getClase()+" get"+atributo.getNombreClase()+" { "+"\n";
					informacion += "\t"+"\t"+"return "+atributo.getNombre()+ ";"+"\n";
					informacion += "\t"+"}"+"\n";
				//}
				//if(atributo.isIsset()) {
					informacion +="\t"+"public "+atributo.getClase()+" set"+atributo.getNombreClase()+"("+atributo.getClase()+" "+atributo.getNombre()+") { "+"\n";
					informacion += "\t"+"\t"+"this."+atributo.getNombre()+" = "+atributo.getNombre()+";"+"\n";
					informacion += "\t"+"}"+"\n";
				//}
			}
			informacion += "\n";
			informacion += "}";
			bw.write(informacion);
			bw.close();
			System.out.println(""+clase.getNombre() + ": creada correctamente");
		}
		
	}
}
