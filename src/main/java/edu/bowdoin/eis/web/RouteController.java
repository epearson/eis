/**
 * 
 */
package edu.bowdoin.eis.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import edu.bowdoin.eis.core.EISRoute;
import edu.bowdoin.eis.data.EISRouteFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.RoutesDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.xml.sax.SAXException;

/**
 * @author epearson
 *
 */
@Controller
public class RouteController {
	
	private static final Logger LOG = LoggerFactory.getLogger(RouteController.class);
	
	@Autowired
	CamelContext camelContext;

	@Autowired
	EISRouteFactory routeFactory;
	
	@PostMapping("/load")
	public String load(@RequestParam(value="xml", required=true) String xml,
					   @RequestParam(value="routeName") String routeName) throws Exception {
		
		LOG.debug(xml);
		
		validateXml(xml, "http://localhost:8080/camel-spring-2.20.1.xsd");

		EISRoute route = new EISRoute(routeName, xml);

		EISRoute oldRoute = routeFactory.getActiveEISRouteByName(routeName);

		RoutesDefinition oldRoutes = null;

		if (oldRoute != null) {
			oldRoutes = camelContext.loadRoutesDefinition(new ByteArrayInputStream(oldRoute.getRouteDefinition().getBytes()));

			for (RouteDefinition theOldRoute : oldRoutes.getRoutes()) {
				camelContext.removeRoute(theOldRoute.getId());
			}
		}

		route = routeFactory.saveEISRoute(route);

		RoutesDefinition routes = camelContext.loadRoutesDefinition(new ByteArrayInputStream(xml.getBytes()));

		camelContext.addRouteDefinitions(routes.getRoutes());
		
		return "redirect:index.html";
	}
	
	private void validateXml(String xml, String xsdUrl) throws SAXException, IOException {
		URL schemaFile = new URL(xsdUrl);
		Source xmlFile = new StreamSource(new ByteArrayInputStream(xml.getBytes()));
		SchemaFactory schemaFactory = SchemaFactory
		    .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		
		  Schema schema = schemaFactory.newSchema(schemaFile);
		  Validator validator = schema.newValidator();
		  validator.validate(xmlFile);
		  LOG.debug(xmlFile.toString() + " is valid");
	}
	
}
