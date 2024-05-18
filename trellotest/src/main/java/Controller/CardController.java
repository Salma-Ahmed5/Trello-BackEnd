package Controller;

import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import Model.Board;
import Model.BoardList;
import Model.Card;
import Service.UserService;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("card")
public class CardController {

	@EJB
	UserService userservice;

	@POST
	@Path("createCard")
	public Response createCardint (@QueryParam("cardname") String cardname, @QueryParam("boardListname") String boardListname,
			@QueryParam("boardname") String boardname) {
		return userservice.createCard(cardname, boardListname, boardname);
	}

/*	@PUT
	@Path("movecard")
	public String moveCard(String card, String newBoardList, String newBoard) {
		return userservice.moveCard(card, newBoardList, newBoard);
	}*/
	@PUT
    @Path("movecard")
    public String moveCard(@QueryParam("cardid") int cardid, @QueryParam("boardListname") String boardListname,
            @QueryParam("boardname") String boardname) {
        return userservice.moveCard(cardid, boardListname, boardname);
    }

	@GET
	@Path("getCards")
	public List<Card> getAllCards() {

		return userservice.ViewAllCards();
	}

	@POST
	@Path("assignCard")
	public String assignCard(String cardName, String assigneeName, String boardName, String boardListName) {
		return userservice.assignCard(cardName, assigneeName, boardName, boardListName);

	}

	@POST
	@Path("addcomment")
	public String addcomment(@QueryParam("cardid")int cardid, @QueryParam("comment")String comment) {
		return userservice.addComment(cardid, comment);
	}
	
	@POST
	@Path("addDescription")
	public String addDescription(@QueryParam("cardid")int cardid, @QueryParam("Description")String dis) {
		return userservice.addDescription(cardid, dis);
	}

	

}
