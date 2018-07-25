package com.jmartinez.proyecto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.jmartinez.bean.AtributosBean;
import com.jmartinez.bean.ClaseBean;
import com.jmartinez.bean.ProyectoBean;

public class ConexionDB {

	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/mys.ventas"; // jdbc:mysql://localhost/

	// Database credentials
	static final String USER = "root";
	static final String PASS = "sistemas";

	public static void main(String[] args) {
		Connection conn = null;
		Statement stmt = null;
		try {
			// STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");
			// STEP 3: Open a connection
			// System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			ProyectoBean proy = new ProyectoBean();
			proy.setNombreProyecto("webexample01");
			proy.setPaqueteria("com.martinez.natura");
			proy.setUbicacionProyecto("I:\\proyectos\\java\\gitProyects/");
			ArrayList<String> tablas = new ArrayList<>();
			// STEP 4: Execute a query
			String sql0 = "show tables";
			stmt = conn.prepareStatement(sql0);
			ResultSet rs = stmt.executeQuery(sql0);
			while (rs.next()) {
				String tabla = rs.getString(1);
				if(tabla.contains("cosmeticos")) {
					tablas.add(rs.getString(1));	
				}else if(tabla.contains("sistema")) {
					tablas.add(rs.getString(1));	
				}
				
			}
			rs.close();
			stmt.close();
			for (String tabla : tablas) {
				ClaseBean clase = new ClaseBean();
				clase.setNombre(tabla);
				clase.setAbreviatura(null);
				String sql = "DESCRIBE " + tabla;
				stmt = conn.prepareStatement(sql);
				rs = stmt.executeQuery(sql);
				// System.out.println("Field \t Type "+tabla);
				String nfiltro = "";
				boolean existenfiltro = false;
				while (rs.next()) {
					AtributosBean att = new AtributosBean();
					String claseT = rs.getString("Type");
					String ncampo = rs.getString("Field");
					
					att.setNombre(ncampo);
					if ("PRI".equals(rs.getString("Key"))) {
						nfiltro = ncampo;
						att.setIspk(true);
					} else {
						att.setIspk(false);
					}
					if(ncampo.equalsIgnoreCase("nombre")){
						existenfiltro = true;
						nfiltro = ncampo;
					}
					if(!existenfiltro&&ncampo.equalsIgnoreCase("descripcion")) {
						existenfiltro = true;
						nfiltro = ncampo;
					}
					if (rs.getString("Null").equals("NO")) {
						att.setRequired("required");
					}else {
						att.setRequired(null);
					}
					att.setMaxlength(null);
					att.setTabla(null);
					att.setJoin(null);
					att.setAttdb(null);
					if (claseT.contains("varchar")) {
						att.setClase("String");
						String maxlength = claseT.replaceAll("varchar", "");
						maxlength = maxlength.substring(1, maxlength.length() - 1);
						att.setMaxlength(maxlength);
					} else if (claseT.contains("int")) {
						att.setClase("int");
					} else if (claseT.contains("double")) {
						att.setClase("Double"); // float
					} else if (claseT.contains("float")) {
						att.setClase("float"); //
					} else {
						att.setClase(claseT);
					}
					clase.getAtributos().add(att);
				}
				clase.setFiltro(nfiltro);
				rs.close();
				stmt.close();
				proy.getClases().add(clase);
			}

			Gson gson = new Gson();
			String json = gson.toJson(proy);
			System.out.println(json);
			// System.out.println("Database created successfully...");
			String sql = "DESCRIBE ventas_venta_det";
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery(sql);
			// System.out.println("Field \t Type "+tabla);
			while (rs.next()) {
				//System.out.print("" + rs.getString("Field") + "-\t" + rs.getString("Type"));
				//System.out.print("" + rs.getString("Null") + "-\t" + rs.getString("Key"));
				//System.out.println("" + rs.getString("Default") + "-\t" + rs.getString("Extra"));
			}
			rs.close();
			stmt.close();
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			} // nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			} // end finally try
		} // end try
			// System.out.println("Goodbye!");
	}// end main
}
