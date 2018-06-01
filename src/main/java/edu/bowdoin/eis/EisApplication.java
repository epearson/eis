package edu.bowdoin.eis;

import edu.bowdoin.eis.core.EISProperty;
import edu.bowdoin.eis.core.EISRoute;
import edu.bowdoin.eis.data.EISPropertyRepository;
import edu.bowdoin.eis.data.EISRouteFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.model.RoutesDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import io.hawt.web.AuthenticationFilter;

import java.io.ByteArrayInputStream;
import java.util.List;

@SpringBootApplication
public class EisApplication {

	private static final Logger logger = LoggerFactory.getLogger(EisApplication.class);

	@Autowired
	EISRouteFactory eisRouteFactory;

	@Autowired
	EISPropertyRepository eisPropertyRepo;

	@Autowired
	CamelContext camelContext;

	public static void main(String[] args) {
		System.setProperty(AuthenticationFilter.HAWTIO_AUTHENTICATION_ENABLED, "false");
		SpringApplication.run(EisApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() throws Exception {
		Iterable<EISProperty> eisProperties = eisPropertyRepo.findAll();

		for(EISProperty eisProperty : eisProperties) {
			System.setProperty(eisProperty.getKey(), eisProperty.getSecretValue());
		}

		List<EISRoute> eisRoutes = eisRouteFactory.getAllActiveEISRoutes();

		for (EISRoute eisRoute : eisRoutes) {
			logger.debug("Loading route: " + eisRoute.getRouteName());
			RoutesDefinition routes = camelContext.loadRoutesDefinition(new ByteArrayInputStream(eisRoute.getRouteDefinition().getBytes()));

			camelContext.addRouteDefinitions(routes.getRoutes());
		}
	}
}
