package com.jmartinez.bean;

public class AtributosBean {
	
	//Tipo de Clase en Java
	private String clase;
	private String claseDB;
	//Nombre de atributo
	private String nombre;
	
	private boolean isset = true;
	private boolean isget = true;
	private boolean ispk = false;
	//Asignación de valor por defecto
	private String automatico;
	//Para mostrar en la grilla de listado
	private boolean verSelect;
	//Para mostrar en la grilla de Nuevo
	private boolean verNuevo;
	//Para mostrar en la grilla de actualizacion
	private boolean verActualizar;
	
	//Indicar si se muestra en el formulario
	private boolean isform = true; 
	//indica el elemento en el formulario
	private String elemento;
	//tipo de elemento
	private String type;
	//cuando es un combo
	private String servlet;
	
	//para verificar si es requerido
	private String required = "";
	//maxlength
	private String maxlength="100"; 
	//para verificar que va a base de datos
	private boolean db = true;
	//Tabla externa de utilizarse
	private String tabla = "";
	private String join = "" ;
	private String attdb = "";
	
	
	public String getMaxlength() {
		return maxlength;
	}
	public void setMaxlength(String maxlength) {
		this.maxlength = maxlength;
	}
	public String getClase() {
		return clase;
	}
	public void setClase(String clase) {
		this.clase = clase;
	}
	public String getNombre() {
		return nombre;
	}
	
	public String getNombreClase() {
		if(nombre==null) {
			return "";
		}else {
			String temp = nombre.substring(0, 1).toUpperCase()+nombre.substring(1);
			return temp;
		}
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public boolean isIsset() {
		return isset;
	}
	public void setIsset(boolean isset) {
		this.isset = isset;
	}
	public boolean isIsget() {
		return isget;
	}
	public void setIsget(boolean isget) {
		this.isget = isget;
	}
	public String getAutomatico() {
		if(automatico==null) {
			return "";
		}
		return automatico;
	}
	public void setAutomatico(String automatico) {
		this.automatico = automatico;
	}
	public boolean isVerSelect() {
		return verSelect;
	}
	public void setVerSelect(boolean verSelect) {
		this.verSelect = verSelect;
	}
	public boolean isVerNuevo() {
		return verNuevo;
	}
	public void setVerNuevo(boolean verNuevo) {
		this.verNuevo = verNuevo;
	}
	public boolean isVerActualizar() {
		return verActualizar;
	}
	public void setVerActualizar(boolean verActualizar) {
		this.verActualizar = verActualizar;
	}
	public String getClaseSQL() {
		if("int".equalsIgnoreCase(getClase())) {
			return "int(11)";
		}
		if("String".equalsIgnoreCase(getClase())) {
			return "varchar("+maxlength+")";
		}
		return claseDB;
	}
	public String getClaseDB() {
		if("int".equalsIgnoreCase(getClase())) {
			return "Int";
		}
		if("double".equalsIgnoreCase(getClase())) {
			return "Double";
		}
		if("String".equalsIgnoreCase(getClase())) {
			return "String";
		}
		return claseDB;
	}
	public String getClasePRIn() {
		if("int".equalsIgnoreCase(getClase())) {
			return "Integer.parseInt(";
		}
		if("double".equalsIgnoreCase(getClase())) {
			return "Double.parseDouble(";
		}
		return "";
	}
	public String getClasePROut() {
		if("int".equalsIgnoreCase(getClase())||"double".equalsIgnoreCase(getClase())) {
			return ")";
		}
		return "";
	}
	public void setClaseDB(String claseDB) {
		this.claseDB = claseDB;
	}
	public boolean isIspk() {
		return ispk;
	}
	public void setIspk(boolean ispk) {
		this.ispk = ispk;
	}
	public boolean isIsform() {
		return isform;
	}
	public void setIsform(boolean isform) {
		this.isform = isform;
	}
	public String getElemento() {
		return elemento;
	}
	public void setElemento(String elemento) {
		this.elemento = elemento;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getServlet() {
		return servlet;
	}
	public void setServlet(String servlet) {
		this.servlet = servlet;
	}
	public String getRequired() {
		return required;
	}
	public void setRequired(String required) {
		this.required = required;
	}
	public boolean isDb() {
		return db;
	}
	public void setDb(boolean db) {
		this.db = db;
	}
	public String getTabla() {
		return tabla;
	}
	public void setTabla(String tabla) {
		this.tabla = tabla;
	}
	public String getJoin() {
		return join;
	}
	public void setJoin(String join) {
		this.join = join;
	}
	public String getAttdb() {
		return attdb;
	}
	public void setAttdb(String attdb) {
		this.attdb = attdb;
	}
	
}
