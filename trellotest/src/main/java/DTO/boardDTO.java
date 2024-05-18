package DTO;

import java.util.ArrayList;
import java.util.List;

import Model.BoardList;
import Model.User;

public class boardDTO {
	private int id;
    private String name;
    
    List<BoardList> lists = new ArrayList<>();
    
    List<User> collaborators=new ArrayList<>();
    
	public List<User> getCollaborators() {
		return collaborators;
	}
	public void setCollaborators(List<User> collaborators) {
		this.collaborators = collaborators;
	}
	public List<BoardList> getLists() {
		return lists;
	}
   
	
	public void setLists(List<BoardList> lists) {
		this.lists = lists;
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
}
