package com.jmartinez.bean;

import java.util.ArrayList;
import java.util.List;

public class ProyectoBean {
	private String nombreProyecto;
	private List<ClaseBean> clases;
	public String getNombreProyecto() {
		return nombreProyecto;
	}
	public void setNombreProyecto(String nombreProyecto) {
		this.nombreProyecto = nombreProyecto;
	}
	public List<ClaseBean> getClases() {
		if(clases == null) {
			return new ArrayList<ClaseBean>();
		}
		return clases;
	}
	public void setClases(List<ClaseBean> clases) {
		this.clases = clases;
	}
}
