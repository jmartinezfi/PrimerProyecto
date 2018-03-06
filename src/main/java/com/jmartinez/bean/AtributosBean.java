package com.jmartinez.bean;

public class AtributosBean {
	
	private String clase;
	private String nombre;
	private boolean isset = true;
	private boolean isget = true;
	private String automatico;
	
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
