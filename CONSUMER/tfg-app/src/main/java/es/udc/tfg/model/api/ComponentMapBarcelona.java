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
package es.udc.tfg.model.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * Component api map
 * @author hmia
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ComponentMapBarcelona {
	
	private List<Component> component;
	
	public ComponentMapBarcelona() {
		
	}

	/**
	 * Barcelona component map
	 * @param component
	 */
	public ComponentMapBarcelona(List<Component> component) {		
		this.component = component;
	}

	public List<Component> getComponents() {
		return component;
	}

	public void setComponents(List<Component> component) {
		this.component = component;
	}

	@Override
	public String toString() {
		return " {components=" + component + "}";
	}
}
