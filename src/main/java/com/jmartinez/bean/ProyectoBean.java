package com.jmartinez.bean;

import java.util.ArrayList;
import java.util.List;

public class ProyectoBean {
	private String nombreProyecto;
	private String ubicacionProyecto;
	private String rutamvn ;
	private String rutamvnw;
	private String rutamvnf;
	private String paqueteria ;
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
	public String getUbicacionProyecto() {
		return ubicacionProyecto;
	}
	public void setUbicacionProyecto(String ubicacionProyecto) {
		this.ubicacionProyecto = ubicacionProyecto;
	}
	public String getRutamvn() {
		return rutamvn;
	}
	public void setRutamvn(String rutamvn) {
		this.rutamvn = rutamvn;
	}
	public String getPaqueteria() {
		return paqueteria;
	}
	public void setPaqueteria(String paqueteria) {
		this.paqueteria = paqueteria;
	}
	public String getRutamvnw() {
		return rutamvnw;
	}
	public void setRutamvnw(String rutamvnw) {
		this.rutamvnw = rutamvnw;
	}
	public String getRutamvnf() {
		return rutamvnf;
	}
	public void setRutamvnf(String rutamvnf) {
		this.rutamvnf = rutamvnf;
	}
}
