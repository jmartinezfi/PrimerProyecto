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
				System.out.println("cantidad de archivos a crear: " + proy.getClases().size());
				//Crear paqueteria
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
		informacion += "\tpublic "+nombreBean+" selectOne("+nombreBean+" dato);\n";
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
		informacion += "\tpublic "+nombreBean+" selectOne("+nombreBean+" dato){\n"; //return null;}
		informacion += selectOne(clase,nombreBean);
		informacion += "\t}\n";
		informacion += "\tpublic java.util.ArrayList<"+nombreBean+"> select(java.lang.String filtro){\n";
		informacion += selectDao(clase,nombreBean);
		informacion += "\t}\n";
		informacion += "}";
		bw.write(informacion);
		bw.close();
		System.out.println("" + clase.getNombre() + ": creada correctamente");
	}
	private static String selectOne(ClaseBean clase, String nombreBean) {
		String filtro = "\t\t\tString sql = \"Select * from "+clase.getNombre()+" \" ;\n";
		String funcionalidad = "";
		funcionalidad += "\t\t\trs = pstm.executeQuery();\n";
		funcionalidad += "\t\t\twhile(rs.next()){\n";
		for (AtributosBean element : clase.getAtributos()) {
			if(element.isIspk()) {
				filtro += "\t\t\tsql += \" where "+element.getNombreClase()+" =  ? \";\n";
				filtro += "\t\t\tpstm = conn.prepareStatement(sql);\n"; 
				filtro += "\t\t\tpstm.set"+element.getClaseDB()+"(1, dato.get"+element.getNombreClase()+"());\n";
			}
			funcionalidad +="\t\t\t\tlistado.set"+element.getNombreClase()+"(rs.get"+element.getClaseDB()+"(\""+element.getNombreClase()+"\"));\n";
		}
		funcionalidad += "\t\t\t}\n";
		String inicial = "       "+nombreBean+" listado = new "+nombreBean+"();\n" ;
		return funcionalSDao(inicial,filtro + funcionalidad,nombreBean);
	}
	private static String selectDao(ClaseBean clase, String nombreBean) {
		String funcionalidad = "";
		funcionalidad  += "\t\t\tString sql = \"Select * from "+clase.getNombre()+" \";\n";
		funcionalidad += "\t\t\tif(filtro!=null&&!filtro.isEmpty())\n"; 
		funcionalidad += "\t\t\t\tsql += filtro;\n"; 
		funcionalidad += "\t\t\tpstm = conn.prepareStatement(sql);\n"; 
		funcionalidad += "\t\t\trs = pstm.executeQuery();\n";
		funcionalidad += "\t\t\twhile(rs.next()){\n";
		funcionalidad += "\t\t\t\t"+nombreBean+ " idato = new "+nombreBean+"();\n";
		for (AtributosBean element : clase.getAtributos()) {
			funcionalidad +="\t\t\t\tidato.set"+element.getNombreClase()+"(rs.get"+element.getClaseDB()+"(\""+element.getNombreClase()+"\"));\n";
		}
		funcionalidad += "\t\t\t\tlistado.add(idato);\n";
		funcionalidad += "\t\t\t}\n";
		String inicial = "       java.util.ArrayList<"+nombreBean+"> listado = new java.util.ArrayList<"+nombreBean+">();\n" ;
		return funcionalSDao(inicial,funcionalidad,nombreBean);
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
		
		String nombres = "";
		String post = "";
		String primary = "";
		String primarypost = "";
		for (AtributosBean element : clase.getAtributos()) {
			if(element.isIspk()) {
				primary = "\t\t\tsql += \" where  "+element.getNombre()+" = ? \" ; \n";
				primarypost += "\t\t\tpstm.set"+element.getClaseDB()+"(i++, dato.get"+element.getNombreClase()+"());\n";
			}else {
				nombres += "\t\t\tif(null!= dato.get"+element.getNombreClase()+"())\n";
				nombres += "\t\t\t\tsql += \" "+element.getNombre()+" = ? ,\";\n";
				post += "\t\t\tif(null!= dato.get"+element.getNombreClase()+"())\n";
				post += "\t\t\t\tpstm.set"+element.getClaseDB()+"(i++, dato.get"+element.getNombreClase()+"());\n";
			}
		}
		funcionalidad += nombres;
		funcionalidad += "\t\t\tsql = sql.substring(0, sql.length()-1);\n";
		funcionalidad +=primary;
		funcionalidad += "\t\t\tpstm = conn.prepareStatement(sql);\n";
		funcionalidad += "\t\t\tint i = 1;\n";
		funcionalidad +=post;
		funcionalidad +=primarypost;
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
	
	private static String funcionalSDao(String variable, String datos, String nombreBean) {
		
		datos = variable+ 
				"       java.sql.Connection conn = null;\r\n" + 
				"		java.sql.PreparedStatement pstm  = null;\r\n" +
				"		java.sql.ResultSet rs = null;\r\n" + 
				"		try {\r\n" + 
				"			conn = getConexion();\r\n" + datos +
				"		} catch (java.sql.SQLException e) {\r\n" + 
				"			e.printStackTrace();\r\n" + 
				"		} catch (Exception e) {\r\n" + 
				"			e.printStackTrace();\r\n" + 
				"		} finally {\r\n" + 
				"			try {\r\n" + 
				"				if (rs != null)\r\n" + 
				"					rs.close();\r\n" + 
				"			} catch (java.sql.SQLException e) {\r\n" + 
				"				e.printStackTrace();\r\n" + 
				"			}\r\n" +
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
				"		}\n"+
				"		return listado;\n" ;
		return datos;
	}
	
	
	private static void crearBean(ProyectoBean proy, ClaseBean clase) throws IOException {
		String prefixfile = proy.getUbicacionProyecto() + proy.getNombreProyecto() + proy.getRutamvn()
				+ clase.getPaqueteria().replace(".", "/") + "/";
		//File directorio = new File(prefixfile);
		//directorio.mkdirs();
		System.out.println("prefixfile:"+prefixfile);
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
