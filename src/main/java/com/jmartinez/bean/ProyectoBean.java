package com.jmartinez.bean;

import java.util.ArrayList;
import java.util.List;

public class ProyectoBean {
	private String nombreProyecto;
	private String ubicacionProyecto;
	private String rutajava ;
	private String rutaweb;
	private String rutafile;
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
	
	public String getPaqueteria() {
		return paqueteria;
	}
	public void setPaqueteria(String paqueteria) {
		this.paqueteria = paqueteria;
	}
	public String getRutajava() {
		return rutajava;
	}
	public void setRutajava(String rutajava) {
		this.rutajava = rutajava;
	}

	public String getRutafile() {
		return rutafile;
	}
	public void setRutafile(String rutafile) {
		this.rutafile = rutafile;
	}
	public String getRutaweb() {
		return rutaweb;
	}
	public void setRutaweb(String rutaweb) {
		this.rutaweb = rutaweb;
	}
}
