package Model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id ;
	@NotNull(message="name cannot be a null value , please enter name")
    private String name;
	
	@NotNull(message="email cannot be a null value , please enter email")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",message = "Invalid email format")
    private String email;
	
	@NotNull(message="password cannot be a null value , please enter password")
    @Size(min = 8, message = "Password must be at least 8 characters ")
    private String password;

    private String role="user"; //default value = user
    
    
    @OneToMany(mappedBy="Owner",cascade = CascadeType.REMOVE,fetch=FetchType.EAGER)
    @JsonIgnore
 	List<Board> leaderboards=new ArrayList<>();
    
   @ManyToMany(cascade = CascadeType.MERGE ,fetch=FetchType.EAGER)
    @JoinTable(
    		name="UserXBoard",
    		joinColumns=@JoinColumn(name="Userid"),
    		inverseJoinColumns=@JoinColumn(name="Boardid") )
   
 	List<Board> CollaboratedBoards=new ArrayList<>();

   @ManyToMany(mappedBy="users")
   List<Card> cards=new ArrayList<>();
   
   


public List<Board> getLeaderboards() {
		return leaderboards;
	}

	public void setLeaderboards(Board leaderboards) {
		this.leaderboards.add(leaderboards);
	}
	
	public void deleteLeaderboards(Board leaderboards) {
		this.leaderboards.remove(leaderboards);
	}

	public List<Board> getBoards() {
		return CollaboratedBoards;
	}

	public List<Board> getCollaboratedBoards() {
		return CollaboratedBoards;
	}

	public void setCollaboratedBoards(List<Board> collaboratedBoards) {
		CollaboratedBoards = collaboratedBoards;
	}
	
	public void addCollaboratedBoards(Board collaboratedBoards) {
		CollaboratedBoards.add(collaboratedBoards);
    }

	public void removeaddCollaboratedBoards(Board collaboratedBoards) {
		CollaboratedBoards.remove(collaboratedBoards);
    }

	public void setLeaderboards(List<Board> leaderboards) {
		this.leaderboards = leaderboards;
	}

	public void setBoards(List<Board> boards) {
		CollaboratedBoards = boards;
	}

public User() {
        
    }

public User(String name) {
    this.name=name;
}
    
    public User(String name, String email,String password,String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
    
    public void setRole(String role)
    {
    	this.role = role ;
    }
    
    public String getRole(){
    	return role;
        }
    

    public int getId() {
    	return id;
    }
    
    public void setId(int id)
    {
    	this.id=id;
    }
}
