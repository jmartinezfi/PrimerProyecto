package com.jmartinez.bean;

import java.util.ArrayList;
import java.util.List;

public class ClaseBean {

	private String paqueteria;
	private String nombre;
	private boolean oBean;
	//nombre del atributo para los filtros
	private String filtro = "Nombre"; 
	
	private String abreviatura = "";
	
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
	public boolean isoBean() {
		return oBean;
	}
	public void setoBean(boolean oBean) {
		this.oBean = oBean;
	}
	public String getFiltro() {
		//return filtro;
		if(filtro==null) {
			return "";
		}else {
			String temp = filtro.substring(0, 1).toUpperCase()+filtro.substring(1);
			return temp;
		}
	}
	public void setFiltro(String filtro) {
		this.filtro = filtro;
	}
	public String getAbreviatura() {
		if(abreviatura.isEmpty()) {
			//return nombre.substring(0, 4);
		}
		return abreviatura;
	}
	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}
	
	
}
