package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class Board implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id ;
	@NotNull
	private String name;
	
	@OneToMany(mappedBy="board",fetch=FetchType.EAGER) //each board has many lists 
	 @JsonIgnore
	List<BoardList> lists = new ArrayList<>();
	
	@ManyToMany(mappedBy="CollaboratedBoards",fetch=FetchType.EAGER)
	List<User> collaborators=new ArrayList<>();
	
    @ManyToOne(cascade = CascadeType.MERGE,fetch=FetchType.EAGER)
	@JoinColumn(name="ownerid")
    @JsonIgnore
	User Owner=new User();

	public User getOwner() {
		return Owner;
	}

	public void setOwner(User owner) {
		Owner = owner;
	}
	
	public void deletelist(BoardList list) {
		this.lists.remove(list);
	}

	public Board(){}
	
	public Board(String name)
	{
		this.name=name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	
	
	
	  public List<User> getCollaborators() {
	        return collaborators;
	    }
	  
	   public void setCollaborators(List<User> collaborators) {
	        this.collaborators = collaborators;
	        
	    }
	   public void addCollaborator(User User) {
	        collaborators.add(User);
	    }

	    public void removeCollaborator(User User) {
	        collaborators.remove(User);
	    }

		public List<BoardList> getLists() {
			return lists;
		}

		public void setLists(List<BoardList> lists) {
			this.lists = lists;
		}
	   
	   


}
