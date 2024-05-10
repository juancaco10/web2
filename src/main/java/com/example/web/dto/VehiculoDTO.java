package com.example.web.dto;

public class VehiculoDTO {
	private int idVehiculo;
	private String matricula;
	private String color;
	private String modelo;
	private String ano;
	private String marca;
	private int idCategoria;
	private String imagen;

	public VehiculoDTO() {
	}

	public VehiculoDTO(int idVehiculo, String matricula,String ano, String color, String modelo, String marca, int idCategoria,
			String imagen) {
		this.idVehiculo = idVehiculo;
		this.matricula = matricula;
		this.color = color;
		this.ano = ano;
		this.modelo = modelo;
		this.marca = marca;
		this.idCategoria = idCategoria;
		this.imagen = imagen;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public int getIdVehiculo() {
		return idVehiculo;
	}

	public void setIdVehiculo(int idVehiculo) {
		this.idVehiculo = idVehiculo;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public int getIdCategoria() {
		return idCategoria;
	}

	public void setIdCategoria(int idCategoria) {
		this.idCategoria = idCategoria;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
}
