package es.udc.citytrash.controller.util.dtos.sensor;

import java.util.Calendar;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class BuscarValoresDto {

	//http://blog.devatlant.com/blog/2018/02/25/how-to-fix-datetime-local-input-in-chrome/*/
		
	@DateTimeFormat(pattern = "dd-MM-dd-yyyy'T'HH:mm:ss")
	private Date FechaInicio = Calendar.getInstance().getTime();

	@DateTimeFormat(pattern = "dd-MM-yyyy'T'HH:mm:ss")
	private Date fechaFin = Calendar.getInstance().getTime();
	
}
