package com.jmartinez.bean;

import java.util.ArrayList;
import java.util.List;

public class ClaseBean {

	private String paqueteria;
	private String nombre;
	private List<AtributosBean> atributos = new ArrayList<>();
	public String getPaqueteria() {
		return paqueteria;
	}
	public void setPaqueteria(String paqueteria) {
		this.paqueteria = paqueteria;
	}
	public List<AtributosBean> getAtributos() {
		return atributos;
	}
	public void setAtributos(List<AtributosBean> atributos) {
		this.atributos = atributos;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
}
