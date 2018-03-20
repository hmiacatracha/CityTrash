package es.udc.citytrash.controller.util.anotaciones;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

/**
 * 
 * 
 * @AuthenticationPrincipal customizado
 * @author hmia for more information reading this link
 *         https://docs.spring.io/spring-security/site/docs/current/reference/html/mvc.html
 */
@Target({ PARAMETER, TYPE })
@Retention(RUNTIME)
@Documented
@AuthenticationPrincipal
public @interface UsuarioActual {

}
