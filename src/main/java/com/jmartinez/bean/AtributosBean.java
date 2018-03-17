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
	public String getClaseDB() {
		return claseDB;
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
	
	
}
