package com.nadaesimposible;

public class usuario {

String idUsuario,nombre,mail,telefono;

public usuario(String idUsuario, String nombre, String mail, String telefono) {
	super();
	this.idUsuario = idUsuario;
	this.nombre = nombre;
	this.mail = mail;
	this.telefono = telefono;
}

public String getIdUsuario() {
	return idUsuario;
}

public void setIdUsuario(String idUsuario) {
	this.idUsuario = idUsuario;
}
public String getNombre() {
	return nombre;
}
public void setNombre(String nombre) {
	this.nombre = nombre;
}
public String getMail() {
	return mail;
}
public void setMail(String mail) {
	this.mail = mail;
}
public String getTelefono() {
	return telefono;
}
public void setTelefono(String telefono) {
	this.telefono = telefono;
}


}
