package rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/angularAPI")
public class AngularAPI 
{

	@GET
	@Path("/reloadImagem")
	public Response printMessage() 
	{
		return Response.status(200).entity("").build();
	}

}
