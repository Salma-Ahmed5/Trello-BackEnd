package Service;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import Model.Board;
import Model.BoardList;
import Model.Card;
import Model.User;
import messaging.Client;

import DTO.userDTO;

import DTO.boardDTO;
import DTO.cardDTO;

@Stateless
public class UserService {

	@PersistenceContext(unitName = "hello")
	private EntityManager entityManager;

	@Inject
	private Client client;

	private User currentloggeduser;

	public String login(String email, String password) {
		TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
		query.setParameter("email", email);

		try {
			User User = query.getSingleResult();

			if (User.getPassword().equals(password)) {

				currentloggeduser = User;// correct email and pass ->user login successful
				return "Login successful";
			} else {

				return "Incorrect password. Please try again.";
			}
		} catch (NoResultException e) {

			return "This account doesn't exist. You need to register first.";
		}
	}

	public String register(User newUser) {
		TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
		query.setParameter("email", newUser.getEmail());

		try {
			User existingUser = query.getSingleResult();
			return "Already registered";
		} catch (NoResultException e) {
			entityManager.persist(newUser);
			return "Registered";
		}
	}

	public String updateprofile(User updatedUser) {
		try {
			currentloggeduser = entityManager.find(User.class, currentloggeduser.getId());

			if (currentloggeduser == null) {
				return "You should login first";
			}
			// Check if the user is logged in
			if (login(currentloggeduser.getEmail(), currentloggeduser.getPassword()).equals("Login successful")) {

				currentloggeduser.setName(updatedUser.getName());
				currentloggeduser.setEmail(updatedUser.getEmail());
				currentloggeduser.setPassword(updatedUser.getPassword());

				entityManager.merge(currentloggeduser);

				return "Profile updated successfully";
			} else {
				return "You should Login first ";
			}
		} catch (NoResultException e) {
			return "Failed to update profile";
		}
	}

	public User viewloggeduser() {
		User currentLoggedUser = entityManager.find(User.class, currentloggeduser.getId());
		return currentLoggedUser;
	}

	// ***************************************************************
	// Board Service
	public String createBoard(String newBoardName) {
		// Ensure the current user is logged in
		if (currentloggeduser == null) {
			return "Login failed";
		}

		try {
			// Set user role to TeamLeader
			currentloggeduser.setRole("TeamLeader");

			// Create a new Board entity
			Board newBoard = new Board();
			newBoard.setName(newBoardName);

			// Set the owner of the board to the current logged-in user
			newBoard.setOwner(currentloggeduser);

			currentloggeduser.setLeaderboards(newBoard);

			// Persist the new Board entity
			entityManager.persist(newBoard);

			// Simulate message sending
			client.sendMessage(currentloggeduser.getName() + " Message -> Board Created");

			// Return success message
			return "Board created successfully";
		} catch (Exception e) {
			// Handle any exceptions that might occur during persistence
			e.printStackTrace(); // Log the exception for debugging purposes
			return "Failed to create board";
		}
	}
		
	 public userDTO viewUserBoards() {
		User currentLoggedUser = entityManager.find(User.class, currentloggeduser.getId());

		userDTO TheUser = new userDTO();
		TheUser.setId(currentLoggedUser.getId());
		TheUser.setName(currentLoggedUser.getName());
		TheUser.setRole(currentLoggedUser.getRole());

		List<boardDTO> boardDTOs = new ArrayList<>();
		List<Board> leaderboards = currentLoggedUser.getLeaderboards();
		for (Board board : leaderboards) {
			boardDTO boardDto = new boardDTO();
			boardDto.setId(board.getId());
			boardDto.setName(board.getName());
			List<BoardList> lists = fetchListsForBoard(board);
			for (BoardList list : lists)
			{
				List<Card> cards = fetchCardsForList(list); 
				list.setCards(cards); 
				for (Card card : cards) {
					card.getUsers().size(); 
				}
			}

			boardDto.setLists(lists);
			boardDTOs.add(boardDto);
		}
		TheUser.setLeaderboards(boardDTOs);
		ArrayList<boardDTO> boards2 = new ArrayList<>();
		List<Board> collaboratedBoards = currentLoggedUser.getCollaboratedBoards();
		for (Board board : collaboratedBoards) {
			boardDTO boardDto = new boardDTO();
			boardDto.setId(board.getId());
			boardDto.setName(board.getName());

			List<BoardList> lists = fetchListsForBoard(board); 
			for (BoardList list : lists) {
				List<Card> cards = fetchCardsForList(list); // Custom method to fetch cards
				list.setCards(cards); // Set cards to avoid lazy loading issues

				// Initialize users for each card
				for (Card card : cards) {
					card.getUsers().size(); // Force initialization of users collection
				}
			}

			boardDto.setLists(lists);
			boards2.add(boardDto);
		}

		TheUser.setCollaboratedBoards(boards2);

		return TheUser;
	}

	private List<BoardList> fetchListsForBoard(Board board) {
		return entityManager.createQuery("SELECT bl FROM BoardList bl WHERE bl.board = :board", BoardList.class)
				.setParameter("board", board).getResultList();
	}

	private List<Card> fetchCardsForList(BoardList list) {
		return entityManager.createQuery("SELECT c FROM Card c WHERE c.boardList = :list", Card.class)
				.setParameter("list", list).getResultList();
	}



	public String inviteCollaborator(int boardId, int collaboratorId) {
		try {

			Board board = entityManager.find(Board.class, boardId);
			if (board == null) {
				return "Board not found with ID: " + boardId;
			}

			User collaborator = entityManager.find(User.class, collaboratorId);
			if (collaborator == null) {
				return "User not found with ID: " + collaboratorId;
			}

			collaborator.addCollaboratedBoards(board);
			entityManager.merge(collaborator);

			board.addCollaborator(collaborator);
			entityManager.merge(board);
			User currentLoggedUser = entityManager.find(User.class, currentloggeduser.getId());

			client.sendMessage("Message -> " + currentloggeduser.getName() + " invite " + collaborator.getName()
					+ " to " + board.getName());
			return "Invited " + collaborator.getName() + " to " + board.getName();

		} catch (Exception e) {
			e.printStackTrace();
			return "Failed to invite collaborator: " + e.getMessage();
		}
	}

	public String deleteBoard(String boardName) {
		try {
			User currentLoggedUser = entityManager.find(User.class, currentloggeduser.getId());
			if (currentLoggedUser == null) {
				return "Login required to delete board";
			}

			Board boardToDelete = null;
			for (Board board : currentLoggedUser.getLeaderboards()) {
				if (board.getName().equals(boardName)) {
					boardToDelete = board;
					break;
				}
			}

			if (boardToDelete != null) {
				if (boardToDelete.getLists() != null && !boardToDelete.getLists().isEmpty()) {
					for (BoardList list : boardToDelete.getLists()) {
						if (list.getCards() != null) {
							for (Card card : list.getCards()) {
								entityManager.remove(card);
							}
						}
						entityManager.remove(list);
					}
				}

				currentLoggedUser.deleteLeaderboards(boardToDelete);
				entityManager.remove(boardToDelete);
				client.sendMessage("Message -> " + currentLoggedUser.getName() + " Board deleted");
				return "Board '" + boardName + "' deleted successfully";
			} else {
				return "Board '" + boardName + "' not found for deletion";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "Failed to delete board '" + boardName + "': " + e.getMessage();
		}
	}

	// ***************************************************************
	// List Service //
	public String createList(String boardName, String newListName) {

		try { 	 currentloggeduser = entityManager.find(User.class, currentloggeduser.getId());

			for (Board board : currentloggeduser.getLeaderboards()) {
				if (board.getName().equals(boardName)) {

					BoardList newList = new BoardList();
					newList.setBoard(board);
					newList.setName(newListName);
					entityManager.persist(newList);
					entityManager.merge(board);
					client.sendMessage("Message -> " + currentloggeduser.getName() + " List created");

					return "List created successfully";
				}
			}
			return "Board not found";
		} catch (Exception e) {
			e.printStackTrace();
			return "Failed to create list";
		}
	}


	public String deleteList(String boardName, String listName) {
		try {
			// Find the currently logged-in user
			User currentLoggedUser = entityManager.find(User.class, currentloggeduser.getId());
			if (currentLoggedUser == null) {
				return "Login required to delete list";
			}

			// Find the board to delete the list from
			Board boardToDelete = null;
			for (Board board : currentLoggedUser.getLeaderboards()) {
				if (board.getName().equals(boardName)) {
					boardToDelete = board;
					break;
				}
			}

			if (boardToDelete != null) {
				// Find and delete the specified list from the board
				BoardList listToDelete = null;
				for (BoardList list : boardToDelete.getLists()) {
					if (list.getName().equals(listName)) {
						listToDelete = list;
						break;
					}
				}

				if (listToDelete != null) {
					// Delete all cards associated with the list to delete
					if (listToDelete.getCards() != null) {
						for (Card card : listToDelete.getCards()) {
							entityManager.remove(card);
						}
					}

					// Remove the list from the board
					entityManager.remove(listToDelete);

					// If the board becomes empty after removing the list, remove the board
					if (boardToDelete.getLists().isEmpty()) {
						currentLoggedUser.deleteLeaderboards(boardToDelete);
						entityManager.remove(boardToDelete);
						client.sendMessage("Message -> " + currentLoggedUser.getName() + " Board deleted");
					}

					return "List '" + listName + "' deleted successfully from board '" + boardName + "'";
				} else {
					return "List '" + listName + "' not found in board '" + boardName + "'";
				}
			} else {
				return "Board '" + boardName + "' not found for list deletion";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "Failed to delete list '" + listName + "' from board '" + boardName + "': " + e.getMessage();
		}
	}

	// ***************************************************************
	// Card Service
	public Response createCard(String cardname, String cardListName, String boardName) {
		try {
			BoardList cardList = entityManager.createQuery(
					"SELECT DISTINCT c FROM BoardList c LEFT JOIN FETCH c.cards WHERE c.name = :name AND c.board.name = :boardName",
					BoardList.class).setParameter("name", cardListName).setParameter("boardName", boardName)
					.getSingleResult();

			Card card = new Card();
			card.setName(cardname);
			card.setBoardList(cardList);

			client.sendMessage("Card created successfully: " + card);
			entityManager.persist(card);

			// Return the response with the fully initialized card
			return Response.ok(card).build();
		} catch (NoResultException e) {
			return Response.serverError().entity("Card List not found").build();
		} catch (Exception e) {
			return Response.serverError().entity(e.getMessage()).build();
		}
	}

	public List<Card> ViewAllCards() {
		TypedQuery<Card> query = entityManager.createQuery("SELECT c FROM Card c ", Card.class);
		return query.getResultList();
	}
	public String moveCard(int cardId, String newBoardListId, String newBoardId) {
        try {
            Card card = entityManager.find(Card.class, cardId);
            if (card == null) {
                return "Card not found with ID: " + cardId;
            }

            BoardList newBoardList = entityManager.find(BoardList.class, newBoardListId);
            if (newBoardList == null) {
                return "Board List not found with ID: " + newBoardListId;
            }

            Board newBoard = entityManager.find(Board.class, newBoardId);
            if (newBoard == null) {
                return "Board not found with ID: " + newBoardId;
            }

            // Remove the card from the current board list
            card.getBoardList().getCards().remove(card);

            // Add the card to the new board list
            newBoardList.getCards().add(card);
            card.setBoardList(newBoardList);

            entityManager.merge(card);

            client.sendMessage("Message -> Card moved successfully");
            return "Card moved successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to move card: " + e.getMessage();
        }
    }
	
	/*public String moveCard(int cardid,String list,String board)
	{
		try {
			Card card = entityManager.find(Card.class, cardid)
					if(card == null)
					{
						throw new Exception("Card not found");
					}
			BoardList oldList=card.getBoardList();
			BoardList newlist= entityManager.createQuery("SELECT c FROM BoardList c WHERE c.name =:name AND c.board.name =:boardName");
		}
	}*/

	/*public String moveCard(Card card, BoardList newBoardList, Board newBoard) {
		TypedQuery<Card> query = entityManager
				.createQuery("SELECT c FROM Card c WHERE c.name = :name AND c.boardList = :boardList", Card.class);
		query.setParameter("name", card.getName());
		query.setParameter("boardList", newBoardList);

		try {
			Card existingCard = query.getSingleResult();
			return "Card already exists in the new board list";
		} catch (NoResultException e) {
			if (card.getBoard().equals(newBoard)) {
				return "Card is already in the destination board";
			}

			// card.getBoardList().getCards().remove(card);

			card.setBoardList(newBoardList);
			card.setBoard(newBoard);

			newBoardList.getCards().add(card);

			entityManager.merge(card);
			return "Card moved successfully";
		}
	}*/

	public String assignCard(String cardName, String assigneeName, String boardName, String boardListName) {
		try {

			TypedQuery<Board> boardQuery = entityManager.createQuery("SELECT b FROM Board b WHERE b.name = :boardName",
					Board.class);
			boardQuery.setParameter("boardName", boardName);
			Board board = boardQuery.getSingleResult();

			TypedQuery<BoardList> listQuery = entityManager.createQuery(
					"SELECT l FROM BoardList l WHERE l.name = :listName AND l.board = :board", BoardList.class);
			listQuery.setParameter("listName", boardListName);
			listQuery.setParameter("board", board);
			BoardList list = listQuery.getSingleResult();

			TypedQuery<Card> cardQuery = entityManager
					.createQuery("SELECT c FROM Card c WHERE c.name = :cardName AND c.boardList = :list", Card.class);
			cardQuery.setParameter("cardName", cardName);
			cardQuery.setParameter("list", list);
			Card card = cardQuery.getSingleResult();

			TypedQuery<User> userQuery = entityManager.createQuery("SELECT u FROM User u WHERE u.name = :assigneeName",
					User.class);
			userQuery.setParameter("assigneeName", assigneeName);
			User assignee = userQuery.getSingleResult();

			// Assign the card to the user
			card.getUsers().add(assignee);
			entityManager.merge(card);

			return "Card assigned successfully";
		} catch (NoResultException e) {
			return "Board, board list, card, or assignee not found";
		}
	}

	public String addComment(int cardId, String comment) {

		try {

			Card card = entityManager.find(Card.class, cardId);
			if (card == null) {
				throw new Exception("Card with ID " + cardId + " does not exist");
			}
			card.setComment(comment);
            //entityManager.persist(comment);
			entityManager.merge(card);
			client.sendMessage(" Message -> New comment Added");
			return "Comment added successfully to card with ID " + cardId;
		} catch (Exception e) {
			e.printStackTrace();
			return "Failed to add comment to card with ID " + cardId + ": " + e.getMessage();
		}
	}
	public String addDescription(int cardId, String Dis) {

		try {

			Card card = entityManager.find(Card.class, cardId);
			if (card == null) {
				throw new Exception("Card with ID " + cardId + " does not exist");
			}
			card.setDescription(Dis);
			entityManager.merge(card);
			client.sendMessage(" Message -> New Description Added");
			return "Description added successfully to card with ID " + cardId;
		} catch (Exception e) {
			e.printStackTrace();
			return "Failed to add Description to card with ID " + cardId + ": " + e.getMessage();
		}
	}

	
	
}
