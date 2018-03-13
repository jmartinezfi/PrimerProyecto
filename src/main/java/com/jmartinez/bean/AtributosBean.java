package com.jmartinez.bean;

public class AtributosBean {
	
	//Tipo de Clase en Java
	private String clase;
	//Nombre de atributo
	private String nombre;
	
	private boolean isset = true;
	private boolean isget = true;
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
	
	
}
