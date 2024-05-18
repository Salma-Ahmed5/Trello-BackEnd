package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import DTO.cardDTO;
@Entity
public class BoardList implements Serializable  {
	
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id ;
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "BoardId")
	 @JsonIgnore
	private Board board=new Board();
	
	@OneToMany(mappedBy="boardList") //Each list has many cards
	 
	List<Card> cards = new ArrayList<>();


	public Board getBoard() {
		return board;
	}

	public BoardList()
	{
		
	}
	
	  public BoardList(String name,ArrayList<Card> cards){
		    this.name=name;
	        this.cards = new ArrayList<>();
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
	
	
	  public List<Card> getCards() {
	        return cards;
	    }
	  
	    public void setCards(List<Card> cards) {
	        this.cards = cards;
	    }
	    public void setBoard(Board board) {
	        this.board = board;
	    }
	    

}
