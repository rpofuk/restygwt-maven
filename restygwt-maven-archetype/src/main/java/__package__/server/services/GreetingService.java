package __package__.server.services;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import __package__.shared.entites.SimpleMessage;


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
