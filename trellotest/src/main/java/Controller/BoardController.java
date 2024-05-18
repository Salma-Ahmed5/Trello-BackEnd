package Controller;

import java.util.List;

import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.websocket.server.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import Model.Board;
import Model.User;
import Service.UserService;
import DTO.userDTO;
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("board")
public class BoardController {

	@EJB
	UserService userservice;
	
	
	@POST
	@Path("/createBoard")
	 public String CreateBoard(String board)
	{
		return userservice.createBoard(board);
	}
	
	@DELETE
	@Path("deleteboard") 
	public String DeleteBoard(String boardName) 
	{
	    return userservice.deleteBoard(boardName);
	}

   
	@GET
	@Path("getuserBoard")
	 public userDTO getUserBoards() {
	        return userservice.viewUserBoards();
	    }
	/*@GET
	@Path("getuserBoard")
	 public Response getUserBoards() {
	        return userservice.getAllBoards();
	    }*/
	
	
	
	@POST
	@Path("/invite")
	public String inviteCollaborator(@QueryParam("boardid")int boardid,@QueryParam("collaboratorid") int collaboratorid)
	{
		return userservice.inviteCollaborator(boardid, collaboratorid);
	}
	
	
	
}
