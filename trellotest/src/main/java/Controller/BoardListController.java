package Controller;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import Model.BoardList;
import Service.UserService;
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("list")
public class BoardListController {

	@EJB
	UserService userservice;
	
	@POST
	@Path("createlist/{boardName}/{newListName}")
	public String createList(@PathParam("boardName") String boardName, @PathParam("newListName") String newListName)
	{
		return userservice.createList(boardName, newListName);
	}

	@DELETE
	@Path("deletelist/{boardName}/{listName}")
	public String deleteList(@PathParam("boardName")  String boardName, @PathParam("listName") String listName)
	{
		return userservice.deleteList(boardName, listName);
	}
	
}
