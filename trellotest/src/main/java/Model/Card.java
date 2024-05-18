package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Card implements Serializable  {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id ;
	private String name;
	private String description;
	private String comment;
	

	@ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(
        name = "CardXUser", // Name of the join table
        joinColumns = @JoinColumn(name = "card_id"), // Column name for Card entity
        inverseJoinColumns = @JoinColumn(name = "user_id") // Column name for User entity
    )
    private List<User> users = new ArrayList<>();

   private Board board;
   
	@ManyToOne
	@JoinColumn(name="boardList_Id")
	 @JsonIgnore
   private BoardList boardList;
	
	
	public Card() {
     
    }
	
	  public Card(String name, String description,String comment) {
	        this.name = name;
	        this.description=description;
	        this.comment=comment;
	    }

	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setBoardList(BoardList boardList) {
		  this.boardList = boardList;
		
	}
	   public void setBoard(Board board) {
	        this.board = board;
	    }

	public BoardList getBoardList() {
		return boardList;
	}

	public Board getBoard() {
		return board;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	
}
