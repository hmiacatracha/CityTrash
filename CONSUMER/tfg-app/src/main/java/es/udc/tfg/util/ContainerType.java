/*-
 * ========================LICENSE_START=================================
 * TFG BYTEPIT-APP
 * %%
 * Copyright (C) 2016 Heidy Mabel Izaguirre Alvarez
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * =========================LICENSE_END==================================
 */
package es.udc.tfg.util;

/**
 * Container type with a small description
 * 
 * @author hmia
 *
 */
public enum ContainerType {

	CONT_INORG("Contenedor Inorgánico"), CONT_ORGAN("Contenedor Orgánico"), CONT_PAPER("Contenedor Papel"), CONT_PLAST(
			"Contenedor Plástico"), CONT_GLASS("Contenedor Vidrio"), NONE("ninguno");

	private final String descripcion;

	ContainerType(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return descripcion;
	}
}
