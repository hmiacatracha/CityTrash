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

@JsonIgnoreProperties(ignoreUnknown = true)
public class ComponentLastObserv {

	private List<SensorLastObservations> sensorLastObservations;

	private String lastUpdateTimeMessage;

	private String componentName;

	private String componentDesc;

	private String componentId;

	private String componentType;

	public List<SensorLastObservations> getSensorLastObservations() {
		return sensorLastObservations;
	}

	public void setSensorLastObservations(List<SensorLastObservations> sensorLastObservations) {
		this.sensorLastObservations = sensorLastObservations;
	}

	public String getLastUpdateTimeMessage() {
		return lastUpdateTimeMessage;
	}

	public void setLastUpdateTimeMessage(String lastUpdateTimeMessage) {
		this.lastUpdateTimeMessage = lastUpdateTimeMessage;
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public String getComponentDesc() {
		return componentDesc;
	}

	public void setComponentDesc(String componentDesc) {
		this.componentDesc = componentDesc;
	}

	public String getComponentId() {
		return componentId;
	}

	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}

	public String getComponentType() {
		return componentType;
	}

	public void setComponentType(String componentType) {
		this.componentType = componentType;
	}

	@Override
	public String toString() {
		return "ComponentLastObserv {lastUpdateTimeMessage=" + lastUpdateTimeMessage + ", componentName="
				+ componentName + ", componentDesc=" + componentDesc + ", componentId=" + componentId
				+ ", componentType=" + componentType + "sensorLastObservations=" + sensorLastObservations + "}";
	}
}
