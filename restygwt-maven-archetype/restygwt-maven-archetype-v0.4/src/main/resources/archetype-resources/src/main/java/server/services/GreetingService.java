#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.server.services;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import ${package}.shared.entites.SimpleMessage;


@Path("/api/v1/greet")
@Produces("application/json")
public class GreetingService {

	@POST
	@Path("/")
	public SimpleMessage greet(SimpleMessage textToServer) {
		System.out.println(textToServer.getMessage());
		textToServer.setMessage("Hi " + textToServer.getMessage());
		return textToServer;
	}

}
