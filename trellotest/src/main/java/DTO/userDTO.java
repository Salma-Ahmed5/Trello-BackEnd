package DTO;
import java.util.List;

import Model.Board;
public class userDTO {

	    private int id;
	    private String name;
	    private String role;
	    private List<boardDTO> leaderboards;
	    private List<boardDTO> CollaboratedBoards;

		public List<boardDTO> getCollaboratedBoards() {
			return CollaboratedBoards;
		}
		public void setCollaboratedBoards(List<boardDTO> collaboratedBoards) {
			CollaboratedBoards = collaboratedBoards;
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
		public String getRole() {
			return role;
		}
		public void setRole(String role) {
			this.role = role;
		}
		public List<boardDTO> getLeaderboards() {
			return leaderboards;
		}
		public void setLeaderboards(List<boardDTO> leaderboards) {
			this.leaderboards = leaderboards;
		}
}
