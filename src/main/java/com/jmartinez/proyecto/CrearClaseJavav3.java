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
					creanDAOImpl(proy, clase);
				}
			} else {
				System.out.println("Proyecto null. Terminando");
			}
		} else {
			System.out.println("Archivo No encontrado");
		}
	}

	private static void creanDAO(ProyectoBean proy, ClaseBean clase) throws IOException {
		String prefixfile = proy.getUbicacionProyecto() + proy.getNombreProyecto() + proy.getRutamvn()
				+ clase.getPaqueteria().replace(".", "/") + "/";
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
		informacion += "\tpublic java.util.ArrayList<"+nombreBean+"> select(java.lang.String filtro);\n";
		informacion += "\n";
		informacion += "}";
		bw.write(informacion);
		bw.close();
		System.out.println("" + clase.getNombre() + ": creada correctamente");
	}

	private static void creanDAOImpl(ProyectoBean proy, ClaseBean clase) throws IOException {
		String prefixfile = proy.getUbicacionProyecto() + proy.getNombreProyecto() + proy.getRutamvn()
				+ clase.getPaqueteria().replace(".", "/") + "/";
		String nombreClase = clase.getNombre();
		File archivo = new File(prefixfile + "dao/" + nombreClase + "DaoImpl.java");
		String informacion = "";
		BufferedWriter bw;
		String nombreBean = ""+clase.getPaqueteria()+".bean."+nombreClase+"Bean";
		bw = new BufferedWriter(new FileWriter(archivo));
		informacion += "package " + clase.getPaqueteria() + ".dao";
		informacion += ";\n";
		informacion += "public class " + nombreClase + "DaoImpl extends ConexionDB implements "+nombreClase+"Dao { " + "\n";
		informacion += "\tpublic void insert ("+nombreBean+" dato){\n";
		informacion += insertDao(clase);
		informacion += "\t}\n";
		informacion += "\tpublic void insertSelective ("+nombreBean+" dato){\n";
		informacion += insertDao(clase);
		informacion += "\t}\n";
		informacion += "\tpublic void update ("+nombreBean+" dato){\n";
		informacion += updateDao(clase);
		informacion += "\t}\n";
		informacion += "\tpublic void updateSelective ("+nombreBean+" dato){\n";
		informacion += updateDao(clase);
		informacion += "\t}\n";
		informacion += "\tpublic java.util.ArrayList<"+nombreBean+"> select(){return null;}\n";
		informacion += "\tpublic java.util.ArrayList<"+nombreBean+"> select(java.lang.String filtro){"
				+ "return null;}\n";
		informacion += "\n";
		informacion += "}";
		bw.write(informacion);
		bw.close();
		System.out.println("" + clase.getNombre() + ": creada correctamente");
	}
	
	private static String insertDao(ClaseBean clase) {
		String funcionalidad = "\t\t\tString sql = \"insert into "+clase.getNombre()+"(<<nombres>>) values(<<indices>>)\" ; \n";
		funcionalidad += "\t\t\tpstm = conn.prepareStatement(sql);\n"; 
		int i = 1;
		String nombres = "";
		String indices = "";
		for (AtributosBean element : clase.getAtributos()) {
			nombres +=element.getNombre()+",";
			indices +="?,";
			funcionalidad += "\t\t\tpstm.set"+element.getClaseDB()+"("+i+", dato.get"+element.getNombreClase()+"());\n";
			i++;
			element.getNombre();
		}
		nombres = nombres.substring(0, nombres.length()-1);
		indices = indices.substring(0, indices.length()-1);
		funcionalidad = funcionalidad.replace("<<nombres>>", nombres);
		funcionalidad = funcionalidad.replace("<<indices>>", indices);
		return funcionalDao(funcionalidad);
	}
	
	private static String updateDao(ClaseBean clase) {
		String funcionalidad = "\t\t\tString sql = \"update "+clase.getNombre()+" set \" ;\n";
		
		int i = 1;
		String nombres = "";
		String indices = "";
		for (AtributosBean element : clase.getAtributos()) {
			nombres +=element.getNombre()+",";
			indices +="?,";
			funcionalidad += "\t\t\tpstm.set"+element.getClaseDB()+"("+i+", dato.get"+element.getNombreClase()+"());\n";
			i++;
			element.getNombre();
		}
		funcionalidad += "\t\t\tpstm = conn.prepareStatement(sql);\n";
		
		
		nombres = nombres.substring(0, nombres.length()-1);
		indices = indices.substring(0, indices.length()-1);
		funcionalidad = funcionalidad.replace("<<nombres>>", nombres);
		funcionalidad = funcionalidad.replace("<<indices>>", indices);
		return funcionalDao(funcionalidad);
	}
	
	private static String funcionalDao(String datos) {
		datos = "       java.sql.Connection conn = null;\r\n" + 
				"		java.sql.PreparedStatement pstm  = null;\r\n" + 
				"		try {\r\n" + 
				"			conn = getConexion();\r\n" + datos +
				"			pstm.execute();\r\n" +
				"		} catch (java.sql.SQLException e) {\r\n" + 
				"			e.printStackTrace();\r\n" + 
				"		} catch (Exception e) {\r\n" + 
				"			e.printStackTrace();\r\n" + 
				"		} finally {\r\n" + 
				"			try {\r\n" + 
				"				if (pstm != null)\r\n" + 
				"					pstm.close();\r\n" + 
				"			} catch (java.sql.SQLException e) {\r\n" + 
				"				e.printStackTrace();\r\n" + 
				"			}\r\n" + 
				"			try {\r\n" + 
				"				if (conn != null)\r\n" + 
				"					conn.close();\r\n" + 
				"			} catch (java.sql.SQLException e) {\r\n" + 
				"				e.printStackTrace();\r\n" + 
				"			}\r\n" + 
				"		}\n";
		return datos;
	}
	
	private static void crearBean(ProyectoBean proy, ClaseBean clase) throws IOException {
		String prefixfile = proy.getUbicacionProyecto() + proy.getNombreProyecto() + proy.getRutamvn()
				+ clase.getPaqueteria().replace(".", "/") + "/";
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
