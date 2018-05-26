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
					System.out.println(clase.getNombre() + ":"+clase.isoBean());
					if(!clase.isoBean()) {
						creanSQL(proy, clase);
						creanDAO(proy, clase);
						creanDAOImpl(proy, clase);
						crearServlet(proy, clase);
						crearHtml(proy,clase);
					}
				}
				
			} else {
				System.out.println("Proyecto null. Terminando");
			}
		} else {
			System.out.println("Archivo No encontrado");
		}
	}
	private static void creanSQL(ProyectoBean proy, ClaseBean clase) throws IOException {
		String prefixfile = proy.getUbicacionProyecto() + proy.getNombreProyecto() + proy.getRutamvnf();
		File directorio = new File(prefixfile);
		directorio.mkdirs();
		String nombreClase = clase.getNombre();
		File archivo = new File(prefixfile + "" + nombreClase + ".sql");
		String informacion = "";
		BufferedWriter bw;
		bw = new BufferedWriter(new FileWriter(archivo));
		informacion += "CREATE TABLE `"+clase.getNombre()+"` (\n";
		AtributosBean ipk = null;
		for (AtributosBean element : clase.getAtributos()) {
			if(element.isIspk()) 
				ipk = element;
			informacion +="  `"+element.getNombre()+"` "+element.getClaseSQL()+" NOT NULL,\n";
		}
		informacion = informacion.substring(0, informacion.length()-2)+"\n";
		informacion += ");\n";
		if(ipk!=null) {
			informacion +="ALTER TABLE `"+clase.getNombre()+"` ADD PRIMARY KEY (`"+ipk.getNombre()+"`);";
		}
		bw.write(informacion);
		bw.close();
	}
	
	private static void creanDAO(ProyectoBean proy, ClaseBean clase) throws IOException {
		String prefixfile = proy.getUbicacionProyecto() + proy.getNombreProyecto() + proy.getRutamvn()
				+ proy.getPaqueteria().replace(".", "/") + "/";
		File directorio = new File(prefixfile+"dao/");
		directorio.mkdirs();
		String nombreClase = clase.getNombre();
		File archivo = new File(prefixfile + "dao/" + nombreClase + "Dao.java");
		String informacion = "";
		BufferedWriter bw;
		String nombreBean = ""+proy.getPaqueteria()+".bean."+nombreClase+"Bean";
		bw = new BufferedWriter(new FileWriter(archivo));
		informacion += "package " + proy.getPaqueteria() + ".dao";
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
		//System.out.println("" + clase.getNombre() + ": creada correctamente");
	}

	private static void creanDAOImpl(ProyectoBean proy, ClaseBean clase) throws IOException {
		String prefixfile = proy.getUbicacionProyecto() + proy.getNombreProyecto() + proy.getRutamvn()
				+ proy.getPaqueteria().replace(".", "/") + "/";
		File directorio = new File(prefixfile+"dao/");
		directorio.mkdirs();
		String nombreClase = clase.getNombre();
		File archivo = new File(prefixfile + "dao/" + nombreClase + "DaoImpl.java");
		String informacion = "";
		BufferedWriter bw;
		String nombreBean = ""+proy.getPaqueteria()+".bean."+nombreClase+"Bean";
		bw = new BufferedWriter(new FileWriter(archivo));
		informacion += "package " + proy.getPaqueteria() + ".dao";
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
		informacion += "\tpublic "+nombreBean+" selectOne("+nombreBean+" dato){\n";
		informacion += selectOne(clase,nombreBean);
		informacion += "\t}\n";
		informacion += "\tpublic java.util.ArrayList<"+nombreBean+"> select(java.lang.String filtro){\n";
		informacion += selectDao(clase,nombreBean);
		informacion += "\t}\n";
		informacion += "}";
		bw.write(informacion);
		bw.close();
		//System.out.println("" + clase.getNombre() + ": creada correctamente");
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
				
				nombres += "\t\t\tif(";
				nombres += element.getClase().equalsIgnoreCase("int")?"0":"null";
				nombres +="!= dato.get"+element.getNombreClase()+"()";
				if (!element.getAutomatico().isEmpty()) {
					nombres += " && !dato.get"+element.getNombreClase()+"().isEmpty()";
				}
				nombres += ")\n";
				nombres += "\t\t\t\tsql += \" "+element.getNombre()+" = ? ,\";\n";
				post += "\t\t\tif(";
				post += element.getClase().equalsIgnoreCase("int")?"0":"null";
				post +="!= dato.get"+element.getNombreClase()+"()";
				if (!element.getAutomatico().isEmpty()) {
					post += " && !dato.get"+element.getNombreClase()+"().isEmpty()";
				}
				post += ")\n";
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
				+ proy.getPaqueteria().replace(".", "/") + "/";

		File directorio = new File(prefixfile+"bean/");
		directorio.mkdirs();
		String nombreClase = clase.getNombre();
		File archivo = new File(prefixfile + "bean/" + nombreClase + "Bean.java");
		String informacion = "";
		BufferedWriter bw;
		bw = new BufferedWriter(new FileWriter(archivo));
		informacion += "package " + proy.getPaqueteria() + ".bean";
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
		//System.out.println("" + clase.getNombre() + ": creada correctamente");
	}
	
	private static void crearServlet(ProyectoBean proy, ClaseBean clase) throws IOException {
		String prefixfile = proy.getUbicacionProyecto() + proy.getNombreProyecto() + proy.getRutamvn()
				+ proy.getPaqueteria().replace(".", "/") + "/";
		String paqueteria = proy.getPaqueteria();
		File directorio = new File(prefixfile+"servlet/");
		directorio.mkdirs();
		String nombreClase = clase.getNombre();
		File archivo = new File(prefixfile + "servlet/" + nombreClase + "Servlet.java");
		String informacion = "";
		String nombreBean = paqueteria+".bean."+clase.getNombre()+"Bean";
		BufferedWriter bw;
		bw = new BufferedWriter(new FileWriter(archivo));
		informacion += "package " + proy.getPaqueteria() + ".servlet;\n";
		informacion += "import java.io.IOException;\r\n" + 
				"import java.util.ArrayList;\r\n" + 
				"\r\n" + 
				"import javax.servlet.ServletException;\r\n" + 
				"import javax.servlet.annotation.WebServlet;\r\n" + 
				"import javax.servlet.http.HttpServlet;\r\n" + 
				"import javax.servlet.http.HttpServletRequest;\r\n" + 
				"import javax.servlet.http.HttpServletResponse;\r\n" + 
				"\r\n" + 
				"import com.google.gson.Gson;\n";
		informacion +="@WebServlet(\"/"+nombreClase+"Servlet\")\n";
		informacion += "public class " + nombreClase + "Servlet extends HttpServlet { " + "\n";
		informacion += "\tprivate static final long serialVersionUID = 1L;\n";
		informacion +="\tprotected void doGet(HttpServletRequest request, HttpServletResponse response)\r\n" + 
				"			throws ServletException, IOException {\r\n" + 
				"		response.getWriter().append(\"Served at: \").append(request.getContextPath());\r\n" + 
				"	}\n";
		informacion +="\tprotected void doPost(HttpServletRequest request, HttpServletResponse response)\r\n" + 
				"			throws ServletException, IOException {\r\n" ;
		//Cuerpo
		informacion += "		Gson gson = new Gson();\r\n" + 
				"		"+paqueteria+".bean.ResultadoBean res = new "+paqueteria+".bean.ResultadoBean();\r\n" + 
				"		try {\r\n" + 
				"			String accion = request.getParameter(\"a\");";
		informacion += "\n";
		informacion += "\t\t\t"+nombreBean+" dato = new "+nombreBean+"();\n";
		//Inicio de listado
		informacion += "\t\t\tif (\"l\".equalsIgnoreCase(accion)) {\n";
		informacion +="\t\t\t\t"+paqueteria+".dao."+clase.getNombre()+"Dao dao = new "+paqueteria+".dao."+clase.getNombre()+"DaoImpl();\n";
		informacion +="				String filtro = \"\";\n";
		informacion +="				String lbBusqueda = request.getParameter(\"lbBusqueda\");\n";
		//
		informacion +="				if(null!=lbBusqueda && !lbBusqueda.isEmpty()) {\r\n" + 
				"					filtro = \" where <<nfiltro>> like ('%\"+lbBusqueda+\"%')\";\r\n" + 
				"				}\r\n" + 
				"				filtro += \" order by <<nfiltro>> asc\";\n";
		informacion = informacion.replace("<<nfiltro>>", clase.getFiltro());
		informacion += "				ArrayList<"+nombreBean+"> lista = dao.select(filtro);\n";
		informacion += "				String base = \"<tr>";
		for (AtributosBean element : clase.getAtributos()) {
			if(!element.isIsform()) {
				informacion += "<td>%s</td>";	
			}
		}
		informacion += "\"+ \"<td><button type='button' class='btn btn-info px-3' onClick='return Clickbtn_Editar(%s);' ><i class='fa fa-search-plus' aria-hidden='true'></i></button>\"\r\n" + 
				"						+ \"<button type='button' class='btn btn-danger px-3' onClick='return Clickbtn_Eliminar(%s);' ><i class='fa fa-times-circle' aria-hidden='true'></i></button>\"\r\n" + 
				"						+ \"</td></tr>\";\n";
		informacion += "				if (lista != null && !lista.isEmpty()) {\r\n" + 
				"					String html = \"\";\r\n" + 
				"					int i = 0;\r\n" + 
				"					while (i < lista.size()) {\r\n" + 
				"						"+nombreBean+" idato = lista.get(i);\r\n" + 
				"						html += String.format(base, ";
		//Recorrer el arreglo de atributos //dato.get"+element.getNombreClase()
		String id = "";
		AtributosBean elementId = null;
		for (AtributosBean element : clase.getAtributos()) {
			if(!element.isIsform()) {
				if(element.isIspk()) {
					id = "idato.get"+element.getNombreClase()+"()";
					elementId = element;
				}
				informacion += "idato.get"+element.getNombreClase()+"(),";
			}
			
		}
		informacion +=id+","+id;
		informacion += ");\r\n" + 
				"						i++;\r\n" + 
				"					}\r\n" + 
				"					res.setHtml(html);\r\n" + 
				"				}\r\n" + 
				"				res.setDescripcion(\"Conforme Listar\");\n";
		//fin de listado
		//Inicio de listado
				informacion += "\t\t\t}else if (\"s\".equalsIgnoreCase(accion)) {\n";
				informacion +="\t\t\t\t"+paqueteria+".dao."+clase.getNombre()+"Dao dao = new "+paqueteria+".dao."+clase.getNombre()+"DaoImpl();\n";
				informacion +="				String filtro = \"\";\n";
				informacion +="				filtro += \" order by <<nfiltro>> asc\";\n";
				informacion = informacion.replace("<<nfiltro>>", clase.getFiltro());
				informacion += "				ArrayList<"+nombreBean+"> lista = dao.select(filtro);\n";
				informacion += "				String base = \"<option value='%s'>%s</option>\" ;\n";
				informacion += "				if (lista != null && !lista.isEmpty()) {\r\n" + 
						"					String html = \"\";\r\n" + 
						"					int i = 0;\r\n" + 
						"					while (i < lista.size()) {\r\n" + 
						"						"+nombreBean+" idato = lista.get(i);\r\n" + 
						"						html += String.format(base, idato.get"+elementId.getNombreClase()+"(),idato.get"+clase.getFiltro()+"());\r\n" + 
						"						i++;\r\n" + 
						"					}\r\n" + 
						"					res.setHtml(html);\r\n" + 
						"				}\r\n" + 
						"				res.setDescripcion(\"Conforme Listar\");\n";
				//fin de listado
		informacion +="\t\t\t} else if (\"n\".equalsIgnoreCase(accion)) {\n";
		informacion +="\t\t\t\t"+paqueteria+".dao."+clase.getNombre()+"Dao dao = new "+paqueteria+".dao."+clase.getNombre()+"DaoImpl();\n";
		//
		for (AtributosBean element : clase.getAtributos()) {
			informacion += "\t\t\t\tdato.set"+element.getNombreClase()+"("+element.getClasePRIn()+"request.getParameter(\""+element.getNombre()+"\")"+element.getClasePROut()+");\n";
		}
		informacion +="				dao.insert(dato);\r\n" + 
				"				res.setDescripcion(\"Nuevo elemento\");\n";
		informacion +="\t\t\t} else if (\"u\".equalsIgnoreCase(accion)) {\n";
		informacion +="\t\t\t\t"+paqueteria+".dao."+clase.getNombre()+"Dao dao = new "+paqueteria+".dao."+clase.getNombre()+"DaoImpl();\n";
		//
		for (AtributosBean element : clase.getAtributos()) {
			informacion += "\t\t\t\tdato.set"+element.getNombreClase()+"("+element.getClasePRIn()+"request.getParameter(\""+element.getNombre()+"\")"+element.getClasePROut()+");\n";
		}
		informacion += "				dao.update(dato);\r\n" + 
				"				res.setDescripcion(\"Elemento actualizado\");\n";
		informacion +="\t\t\t} else if (\"d\".equalsIgnoreCase(accion)) {\n";
		informacion +="\t\t\t\t"+paqueteria+".dao."+clase.getNombre()+"Dao dao = new "+paqueteria+".dao."+clase.getNombre()+"DaoImpl();\n";
		//
		if(elementId!=null) {
			informacion += "\t\t\t\tdato.set"+elementId.getNombreClase()+"("+elementId.getClasePRIn()+"request.getParameter(\""+elementId.getNombre()+"\")"+elementId.getClasePROut()+");\n";
		}
		informacion +="				dato.setEstado(\"99\");\n";
		informacion +="				dao.updateSelective(dato);\r\n" + 
				"				res.setDescripcion(\"Eliminado correctamente\");\n";
		informacion +="\t\t\t} else if (\"o\".equalsIgnoreCase(accion)) {\n";
		informacion +="\t\t\t\t"+paqueteria+".dao."+clase.getNombre()+"Dao dao = new "+paqueteria+".dao."+clase.getNombre()+"DaoImpl();\n";
		//
		if(elementId!=null) {
			informacion += "\t\t\t\tdato.set"+elementId.getNombreClase()+"("+elementId.getClasePRIn()+"request.getParameter(\""+elementId.getNombre()+"\")"+elementId.getClasePROut()+");\n";
		}
		informacion += "				"+nombreBean+" rest = dao.selectOne(dato);\r\n" + 
				"				res.setValor(rest);\n";
		informacion +="\t\t\t} else {\r\n" + 
				"				res.setCodigo(\"2002\");\r\n" + 
				"				res.setDescripcion(\"Opción no disponible\");\r\n" + 
				"			}\r\n" + 
				"			res.setCodigo(\"2000\");\n";
		informacion += "		} catch (Exception e) {\r\n" + 
				"			res.setCodigo(\"2001\");\r\n" + 
				"			res.setDescripcion(\"Error :\" + e.getLocalizedMessage());\r\n" + 
				"			e.printStackTrace();\r\n" + 
				"		}\r\n" + 
				"		response.setContentType(\"application/json\");\r\n" + 
				"		response.getWriter().append(gson.toJson(res));";
		informacion +="	}\n";
		informacion += "}";
		bw.write(informacion);
		bw.close();
		//System.out.println("" + clase.getNombre() + ": creada correctamente");
	}
	
	
	private static void crearHtml(ProyectoBean proy, ClaseBean clase) throws IOException {
		String prefixfile = proy.getUbicacionProyecto() + proy.getNombreProyecto() + proy.getRutamvnw()
				+ "/";
		File directorio = new File(prefixfile);
		directorio.mkdirs();
		String nombreClase = clase.getNombre();
		File archivo = new File(prefixfile + "" + nombreClase + "Form.html");
		String informacion = "";
		BufferedWriter bw;
		bw = new BufferedWriter(new FileWriter(archivo));
		informacion +="<!DOCTYPE html>\r\n" + 
				"<html>\r\n" + 
				"<head>\r\n" + 
				"<meta charset=\"ISO-8859-1\">\r\n" + 
				"<title>"+clase.getNombre()+" Form</title>\r\n" + 
				"<!-- Latest compiled and minified CSS -->\r\n" + 
				"<link rel=\"stylesheet\"\r\n" + 
				"	href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css\">\r\n" + 
				"<!-- jQuery library -->\r\n" + 
				"<script\r\n" + 
				"	src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js\"></script>\r\n" + 
				"\r\n" + 
				"<link rel=\"stylesheet\"\r\n" + 
				"	href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\">\r\n" + 
				"<!-- Popper JS -->\r\n" + 
				"<script\r\n" + 
				"	src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js\"></script>\r\n" + 
				"\r\n" + 
				"<!-- Latest compiled JavaScript -->\r\n" + 
				"<script\r\n" + 
				"	src=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js\"></script>\r\n" + 
				"</head>\r\n" + 
				"<body>\r\n" + 
				"	<div class=\"container\">\r\n" + 
				"		<div class=\"navbar navbar-default\" role=\"navigation\" id=\"idCabecera\">\r\n" + 
				"			<div class=\"navbar-header\">\r\n" + 
				"				<button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\"\r\n" + 
				"					data-target=\".navbar-collapse\">\r\n" + 
				"					<span class=\"sr-only\">Toggle navigation</span> <span\r\n" + 
				"						class=\"icon-bar\"></span> <span class=\"icon-bar\"></span> <span\r\n" + 
				"						class=\"icon-bar\"></span>\r\n" + 
				"				</button>\r\n" + 
				"				<a class=\"navbar-brand\" href=\"#\">Bienvenido </a>\r\n" + 
				"			</div>\r\n" + 
				"			<div class=\"navbar-collapse collapse\">\r\n" + 
				"\r\n" + 
				"				<ul class=\"nav navbar-nav navbar-right\">\r\n" + 
				"					<li class=\"active\"><a href=\"#\">Cerrar</a></li>\r\n" + 
				"				</ul>\r\n" + 
				"			</div>\r\n" + 
				"			<!--/.nav-collapse -->\r\n" + 
				"		</div>\r\n" + 
				"		<!-- Inicio de cuerpo de la pagina style=\"display: none; position: absolute;z-index: 20;\" -->\r\n" + 
				"		<div class=\"row\">\r\n" + 
				"			<div id=\"msg\" class=\"alert\"\r\n" + 
				"				style=\"display: none; position: absolute; z-index: 20; width: 90%;\">\r\n" + 
				"				<button type=\"button\" class=\"close\" data-dismiss=\"alert\">&times;</button>\r\n" + 
				"				<strong>Success!</strong> Indicates a successful or positive action.\r\n" + 
				"			</div>\r\n" + 
				"		</div>\r\n" + 
				"		<div>\r\n" + 
				"			<!-- formulario de busqueda -->\r\n" + 
				"			<div>\r\n" + 
				"				<div id=\"busqueda\">\r\n" + 
				"					<div>\r\n" + 
				"						<form method=\"post\" id=\"formBusqueda\">\r\n" + 
				"							<div class=\"form-group row\">\r\n" + 
				"								<label for=\"lbBusqueda\" class=\"col-sm-2 col-form-label\">Busqueda:</label>\r\n" + 
				"								<div class=\"col-sm-9\">\r\n" + 
				"									<input type=\"text\" class=\"form-control\" id=\"lbBusqueda\"\r\n" + 
				"										placeholder=\"Ingrese valor de Busqueda\" value=\"\" />\r\n" + 
				"								</div>\r\n" + 
				"							</div>\r\n" + 
				"							<div class=\"form-group\">\r\n" + 
				"								<div class=\"col-sm-12 botones\">\r\n" + 
				"									<button type=\"submit\" class=\"btn\" id=\"btnBuscar\">Buscar</button>\r\n" + 
				"									<button type=\"button\" class=\"btn\" id=\"limpiarLista\"\r\n" + 
				"										onClick=\"return Clickbtn_Limpiar();\">Limpiar</button>\r\n" + 
				"									<button type=\"button\" class=\"btn\" id=\"btnNuevo\"\r\n" + 
				"										onClick=\"return Clickbtn_Nuevo();\">Nuevo</button>\r\n" + 
				"								</div>\r\n" + 
				"							</div>\r\n" + 
				"						</form>\r\n" + 
				"					</div>\r\n" + 
				"					<div>\r\n" + 
				"						<table class=\"table table-striped\">\r\n" + 
				"							<thead>\r\n" + 
				"								<tr>\r\n";
		for (AtributosBean element : clase.getAtributos()) {
			if(!element.isIsform()) {
				informacion += "									<th scope=\"col\">"+element.getNombreClase()+"</th>\r\n";	
			}
		}
		informacion += "									<th scope=\"col\">Acción</th>\r\n" + 
				"								</tr>\r\n" + 
				"							</thead>\r\n" + 
				"							<tbody id=\"listado\">\r\n" + 
				"							</tbody>\r\n" + 
				"						</table>\r\n" + 
				"						<nav aria-label=\"Page navigation example\">\r\n" + 
				"							<ul class=\"pagination justify-content-end\">\r\n" + 
				"								<li class=\"page-item disabled\"><a class=\"page-link\"\r\n" + 
				"									href=\"#\" tabindex=\"-1\">Previous</a></li>\r\n" + 
				"								<li class=\"page-item\"><a class=\"page-link\" href=\"#\">1</a></li>\r\n" + 
				"								<li class=\"page-item\"><a class=\"page-link\" href=\"#\">2</a></li>\r\n" + 
				"								<li class=\"page-item\"><a class=\"page-link\" href=\"#\">3</a></li>\r\n" + 
				"								<li class=\"page-item\"><a class=\"page-link\" href=\"#\">Next</a></li>\r\n" + 
				"							</ul>\r\n" + 
				"						</nav>\r\n" + 
				"					</div>\r\n" + 
				"				</div>\r\n" + 
				"			</div>\r\n" + 
				"			<!-- formulario de edicion -->\r\n" + 
				"			<div>\r\n" + 
				"				<div id=\"datos\">\r\n" + 
				"					<input type=\"hidden\" class=\"form-control\" id=\"accion\" value=\"\" />\r\n";
		AtributosBean pka = null;
		for (AtributosBean element : clase.getAtributos()) {
			if(element.isIspk())
				pka = element;
			informacion += 
					"					<div class=\"form-group row\">\r\n" + 
					"						<label for=\"Id"+element.getNombreClase()+"\" class=\"col-sm-2 col-form-label\">"+element.getNombreClase()+"</label>\r\n" + 
					"						<div class=\"col-sm-10\">\r\n" ;
			if("select".equalsIgnoreCase(element.getElemento())) {
				informacion +=
						"							<select class=\"form-control\" id=\"Id"+element.getNombreClase()+"\"></select>\r\n";
			}else {
				informacion +=
						"							<input type=\"text\" class=\"form-control\" id=\"Id"+element.getNombreClase()+"\"\r\n" + 
						"								placeholder=\""+element.getNombreClase()+"\" value=\"\" />\r\n";
			}
			
			informacion += 
					"						</div>\r\n" + 
					"					</div>\r\n" ;
		}
		informacion +=
				"					<div class=\"form-group row\">\r\n" + 
				"						<div class=\"col-sm-6\">\r\n" + 
				"							<input type=\"button\" class=\"btn\" id=\"btnEnviar\"\r\n" + 
				"								onClick=\"return Clickbtn_Enviar();\" value=\"Enviar\">\r\n" + 
				"						</div>\r\n" + 
				"						<div class=\"col-sm-6\">\r\n" + 
				"							<input type=\"button\" class=\"btn\" id=\"btnRegresar\"\r\n" + 
				"								onClick=\"return Clickbtn_Regresar();\" value=\"Regresar\">\r\n" + 
				"						</div>\r\n" + 
				"					</div>\r\n" + 
				"				</div>\r\n" + 
				"			</div>\r\n" + 
				"		</div>\r\n" + 
				"		<div class=\"row\">\r\n" + 
				"			<div class=\"col-md-12\" align=\"center\" style=\"border-top: 1px solid\">\r\n" + 
				"				<br> Copyright &copy; jucamafi09\r\n" + 
				"			</div>\r\n" + 
				"		</div>\r\n" + 
				"		<div class=\"modal fade\" id=\"mdEliminar\">\r\n" + 
				"			<div class=\"modal-dialog\">\r\n" + 
				"				<div class=\"modal-content\">\r\n" + 
				"					<!-- Modal Header -->\r\n" + 
				"					<div class=\"modal-header\">\r\n" + 
				"						<h4 class=\"modal-title\">Eliminar</h4>\r\n" + 
				"						<button type=\"button\" class=\"close\" data-dismiss=\"modal\">&times;</button>\r\n" + 
				"					</div>\r\n" + 
				"					<!-- Modal body -->\r\n" + 
				"					<div class=\"modal-body\" id=\"modalBody\">Eliminar el elemento\r\n" + 
				"						XYZ</div>\r\n" + 
				"					<!-- Modal footer -->\r\n" + 
				"					<div class=\"modal-footer\">\r\n" + 
				"						<button type=\"button\" class=\"btn btn-success\"\r\n" + 
				"							onClick=\"return Clickbtn_EliminarDB();\">Guardar</button>\r\n" + 
				"						<button type=\"button\" class=\"btn btn-danger\" data-dismiss=\"modal\">Cerrar</button>\r\n" + 
				"					</div>\r\n" + 
				"				</div>\r\n" + 
				"			</div>\r\n" + 
				"		</div>\r\n" + 
				"	</div>\r\n" + 
				"	<script>\r\n" + 
				"		function buscar() {\r\n" + 
				"			var lbBusqueda = $('#lbBusqueda').val();\r\n" + 
				"			console.log(\"valor de busqueda: \" + lbBusqueda);\r\n" + 
				"			try {\r\n" + 
				"				$.ajax({\r\n" + 
				"					url : \""+clase.getNombre()+"Servlet\",\r\n" + 
				"					data : \"a=l&lbBusqueda=\" + lbBusqueda,\r\n" + 
				"					type : \"POST\"\r\n" + 
				"				}).done(function(json) {\r\n" + 
				"					if (json != null) {\r\n" + 
				"						console.log(\"salida: \" + json.descripcion);\r\n" + 
				"						if (json.html == null) {\r\n" + 
				"							$('#listado').html(\"\");\r\n" + 
				"						} else {\r\n" + 
				"							$('#listado').html(json.html);\r\n" + 
				"						}\r\n" + 
				"					} else {\r\n" + 
				"						console.log(\"Error al obtener resultados\");\r\n" + 
				"					}\r\n" + 
				"				}).fail(function(xhr, status, errorThrown) {\r\n" + 
				"					alertP(\"Error! Problemas al Terminar\");\r\n" + 
				"				}).always(function(xhr, status) {\r\n" + 
				"				});\r\n" + 
				"			} catch (e) {\r\n" + 
				"				alertP(e);\r\n" + 
				"			}\r\n" + 
				"		}\r\n" + 
				"		function Clickbtn_Regresar() {\r\n" + 
				"			$('#datos').hide();\r\n" + 
				"			//busqueda\r\n" + 
				"			$('#busqueda').show();\r\n" + 
				"		}\r\n" + 
				"\r\n" + 
				"		function Clickbtn_Editar(codigo) {\r\n" + 
				"			try {\r\n" + 
				"				$.ajax({\r\n" + 
				"					url : \""+clase.getNombre()+"Servlet\",\r\n" + 
				"					data : \"a=o&"+pka.getNombre()+"=\" + codigo,\r\n" + 
				"					type : \"POST\"\r\n" + 
				"				}).done(function(json) {\r\n" + 
				"					if (json != null) {\r\n" + 
				"						console.log(\"salida: \" + json.descripcion);\r\n" + 
				"						if (json.codigo == '2000') {\r\n" + 
				"							if (json.valor != null) {\r\n"; 
				for (AtributosBean element : clase.getAtributos()) {
					informacion += "								$('#Id"+element.getNombreClase()+"').val(json.valor."+element.getNombre()+");\r\n";
				}
		informacion +=	
				"							} else {\r\n" + 
				"								alertE(json.descripcion);\r\n" + 
				"							}\r\n" + 
				"						} else {\r\n" + 
				"							alertE(json.descripcion);\r\n" + 
				"						}\r\n" + 
				"					} else {\r\n" + 
				"						console.log(\"Error al obtener resultados\");\r\n" + 
				"					}\r\n" + 
				"				}).fail(function(xhr, status, errorThrown) {\r\n" + 
				"					alertP(\"Error! Problemas al Terminar\");\r\n" + 
				"				}).always(function(xhr, status) {\r\n" + 
				"				});\r\n" + 
				"			} catch (e) {\r\n" + 
				"				alertP(e);\r\n" + 
				"			}\r\n" + 
				"			$('#datos').show();\r\n" + 
				"			//busqueda\r\n" + 
				"			$('#busqueda').hide();\r\n" + 
				"			$('#accion').val('U')\r\n" + 
				"\r\n" + 
				"		}\r\n" + 
				"		function Clickbtn_Nuevo() {\r\n" + 
				"			$('#datos').show();\r\n" + 
				"			//busqueda\r\n" + 
				"			$('#busqueda').hide();\r\n" + 
				"			$('#accion').val('N');\r\n" ;
				for (AtributosBean element : clase.getAtributos()) {
					informacion += "								$('#Id"+element.getNombreClase()+"').val('');\r\n";
				}
				informacion +=
				"		}\r\n" + 
				"		function Clickbtn_Limpiar() {\r\n" + 
				"			$('#datos').hide();\r\n" + 
				"			$('#lbBusqueda').val('');\r\n" + 
				"			$('#listado').html(\"\");\r\n" + 
				"		}\r\n" + 
				"		function Clickbtn_Eliminar(codigo) {\r\n" + 
				"			$('#Id"+pka.getNombreClase()+"').val(codigo);\r\n" + 
				"			$('#modalBody').html(\"Se eliminará el elemento \" + codigo);\r\n" + 
				"			$(\"#mdEliminar\").modal();\r\n" + 
				"		}\r\n" + 
				"\r\n" + 
				"		function Clickbtn_EliminarDB() {\r\n" + 
				"			try {\r\n" + 
				"				$.ajax({\r\n" + 
				"					url : \""+clase.getNombre()+"Servlet\",\r\n" + 
				"					data : \"a=d&"+pka.getNombre()+"=\" + $('#Id"+pka.getNombreClase()+"').val(),\r\n" + 
				"					type : \"POST\"\r\n" + 
				"				}).done(function(json) {\r\n" + 
				"					if (json != null) {\r\n" + 
				"						console.log(\"salida: \" + json.descripcion);\r\n" + 
				"						if (json.codigo == '2000') {\r\n" + 
				"							console.log(\"conforme\");\r\n" + 
				"							alertP(json.descripcion);\r\n" + 
				"						} else {\r\n" + 
				"							alertE(json.descripcion);\r\n" + 
				"						}\r\n" + 
				"					} else {\r\n" + 
				"						console.log(\"Error al obtener resultados\");\r\n" + 
				"					}\r\n" + 
				"				}).fail(function(xhr, status, errorThrown) {\r\n" + 
				"					alertE(\"Error! Problemas al Terminar\");\r\n" + 
				"				}).always(function(xhr, status) {\r\n" + 
				"				});\r\n" + 
				"			} catch (e) {\r\n" + 
				"				alertE(e);\r\n" + 
				"			}\r\n" + 
				"			$(\"#mdEliminar\").modal(\"hide\");\r\n" + 
				"		}\r\n" + 
				"		function alertP(mensaje) {\r\n" + 
				"			console.log(mensaje);\r\n" + 
				"			$(\"#msg\").addClass(\"alert-success\");\r\n" + 
				"			$(\"#msg\").html(mensaje);\r\n" + 
				"			$(\"#msg\").fadeIn();\r\n" + 
				"			$(\"#msg\").fadeOut(3000);\r\n" + 
				"		}\r\n" + 
				"		function alertE(mensaje) {\r\n" + 
				"			console.log(mensaje);\r\n" + 
				"			$(\"#msg\").addClass(\"alert-danger\");\r\n" + 
				"			$(\"#msg\").html(mensaje);\r\n" + 
				"			$(\"#msg\").fadeIn();\r\n" + 
				"			$(\"#msg\").fadeOut(3000);\r\n" + 
				"		}\r\n" + 
				"\r\n" + 
				"		function Clickbtn_Enviar() {\r\n" + 
				"			try {\r\n" + 
				"				$.ajax(\r\n" + 
				"						{\r\n" + 
				"							url : \""+clase.getNombre()+"Servlet\",\r\n" + 
				"							data : \"a=\" + $('#accion').val() \n";
		for (AtributosBean element : clase.getAtributos()) {
			informacion += "							+ \"&"+element.getNombre()+"=\"+ $('#Id"+element.getNombreClase()+"').val()\n";
		}
			informacion +=
				"							,type : \"POST\"\r\n" + 
				"						}).done(function(json) {\r\n" + 
				"					if (json != null) {\r\n" + 
				"						console.log(\"salida: \" + json.descripcion);\r\n" + 
				"						//\r\n" + 
				"						if (json.codigo == '2000') {\r\n" + 
				"							//Agregar modal automatico para confirmación y cerrarlo\r\n" + 
				"							alertP(json.descripcion);\r\n" + 
				"							buscar();\r\n" + 
				"							Clickbtn_Regresar();\r\n" + 
				"						} else {\r\n" + 
				"							alertE(json.descripcion);\r\n" + 
				"						}\r\n" + 
				"					} else {\r\n" + 
				"						console.log(\"Error al obtener resultados\");\r\n" + 
				"					}\r\n" + 
				"				}).fail(function(xhr, status, errorThrown) {\r\n" + 
				"					alertP(\"Error! Problemas al Terminar\");\r\n" + 
				"				}).always(function(xhr, status) {\r\n" + 
				"				});\r\n" + 
				"\r\n" + 
				"			} catch (e) {\r\n" + 
				"				alertP(e);\r\n" + 
				"			}\r\n" + 
				"		}\r\n" + 
				"		$(document).ready(function() {\r\n" + 
				"			$('#datos').hide();\r\n" + 
				"			$('#formBusqueda').submit(function(event) {\r\n" + 
				"				buscar();\r\n" + 
				"				event.preventDefault();\r\n" + 
				"			});\r\n" ;
			for (AtributosBean element : clase.getAtributos()) {
				if("select".equalsIgnoreCase(element.getElemento())) {
					informacion +="			try {\r\n" + 
							"				$.ajax(\r\n" + 
							"						{\r\n" + 
							"							url : \""+element.getServlet()+"Servlet\",\r\n" + 
							"							data : \"a=s\"\r\n" + 
							"							,type : \"POST\"\r\n" + 
							"						}).done(function(json) {\r\n" + 
							"					if (json != null) {\r\n" + 
							"						console.log(\"salida: \" + json.descripcion);\r\n" + 
							"						//\r\n" + 
							"						if (json.codigo == '2000') {\r\n" + 
							"							$('#Id"+element.getNombreClase()+"').html(json.html);\r\n" + 
							"						} else {\r\n" + 
							"							alertE(json.descripcion);\r\n" + 
							"						}\r\n" + 
							"					} else {\r\n" + 
							"						console.log(\"Error al obtener resultados\");\r\n" + 
							"					}\r\n" + 
							"				}).fail(function(xhr, status, errorThrown) {\r\n" + 
							"					alertP(\"Error! Problemas al Terminar\");\r\n" + 
							"				}).always(function(xhr, status) {\r\n" + 
							"				});\r\n" + 
							"			} catch (e) {\r\n" + 
							"				alertP(e);\r\n" + 
							"			}\n";
				}
			}
			informacion +=
				"		});\r\n" + 
				"	</script>\r\n" + 
				"</body>\r\n" + 
				"</html>";
		bw.write(informacion);
		bw.close();
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
