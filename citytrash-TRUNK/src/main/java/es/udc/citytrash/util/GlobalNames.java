package es.udc.citytrash.util;

public class GlobalNames {
	public static final String SPRING_CONFIG_FILE = "classpath:/spring-config.xml";

	public static final String SPRING_SECURITY_FILE = "classpath:/spring-security.xml";

	private GlobalNames() {
	}

	public static final String TBL_TRABAJ = "TBL_TRABAJADORES";
	public static final String TBL_TIPO_BASURA = "TBL_TIPOS_BASURA";
	public static final String TBL_CAMION = "TBL_CAMIONES";
	public static final String TBL_MODELO_CAMION = "TBL_MODELOS_CAMION";
	public static final String TBL_MODELO_CAMION_TIPO_DE_BASURA = "TBL_MC_TB";

	public static final String TBL_CONTENEDOR = "TBL_CONTENEDORES";
	public static final String TBL_MODELO_CONTENEDOR = "TBL_MODELOS_CONTENEDOR";

	public static final String CAMPO_DISCRIMINADOR_BD = "TRABAJADOR_TYPE";
	public static final String CAMPO_ROL_BD = "ROL";

	public static final String DISCRIMINADOR_X_DEFECTO = "NONE";
	public static final String DISCRIMINADOR_ADMIN = "ADMIN";
	public static final String DISCRIMINADOR_RECOLECTOR = "RECOLEC";
	public static final String DICRIMINADOR_CONDUCTOR = "CONDUCT";

	public static final String ROL_TRABAJADOR = "ROLE_USER";
	public static final String ROL_ADMINISTRADOR = "ROLE_ADMIN";
	public static final String ROL_RECOLECTOR = "ROLE_USER";
	public static final String ROL_CONDUCTOR = "ROLE_USER";
	public static final String ROL_REINICIAR_PASSWORD = "ROLE_CHANGE_PASSWORD";

}
