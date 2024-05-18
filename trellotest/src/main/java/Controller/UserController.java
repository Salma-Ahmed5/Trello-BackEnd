package Controller;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import Model.User;
import Service.UserService;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("user")
public class UserController {
		
		
		@EJB
		UserService userservice;
		
		
		@POST
		@Path("/register")
		public String Register(User User)
		{
			return userservice.register(User);
		}
		

		@POST
		@Path("/login")
		public String login(User User) {
		try {
			return userservice.login(User.getEmail(),User.getPassword());	
		}
		catch(Exception e) {
			return e.getMessage();
		}
		}
		
		@PUT
		@Path("updateProfile")
		public String UpdateProfile(User updatedUser)
		{
			return userservice.updateprofile(updatedUser);
		}
		
		@GET
		@Path("viewloggeduser")
		public User ViewLoggedUser()
		{
			return userservice.viewloggeduser();
		}
}
