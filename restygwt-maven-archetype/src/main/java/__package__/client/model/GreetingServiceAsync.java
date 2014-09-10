package __package__.client.model;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import __package__.shared.entites.SimpleMessage;


@Path("/api/v1/greet")
public interface GreetingServiceAsync extends RestService {

	@POST
	@Path("/")
	void greet(SimpleMessage textToServer, MethodCallback<SimpleMessage> methodCallback);

}