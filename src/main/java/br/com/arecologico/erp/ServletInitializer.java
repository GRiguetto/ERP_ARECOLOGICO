package br.com.arecologico.erp;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * ============================================================================
 * CONCEITO SPRING BOOT: SpringBootServletInitializer
 * ============================================================================
 * Esta classe é necessária quando o projeto Spring Boot é empacotado como um
 * arquivo WAR (Web Application Archive) para ser implantado em um servidor de
 * aplicação ou container de servlets tradicional externo (ex: Apache Tomcat,
 * Jetty, WildFly).
 * 
 * Funcionamento:
 * 1. O método configure() vincula a aplicação Spring Boot ao ciclo de vida do Servlet Container.
 * 2. Permite que a classe ErpApplication seja o ponto de partida tanto executando
 *    como Standalone JAR (via método main()) quanto hospedada como WAR em servidor externo.
 * ============================================================================
 */
public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ErpApplication.class);
	}

}
