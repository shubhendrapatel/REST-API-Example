package com.app;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import com.bean.User;

@ApplicationPath("/")
@Path("/")
public class Main extends Application implements ContainerResponseFilter {

	Connection connection;

	public Main() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3307/userDb", "root", "root");
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<>();
		classes.add(this.getClass());
		return classes;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> getAllUsersData(@QueryParam("name") String name) {
		List<User> users = new ArrayList<>();

		try {
			String query = "Select * from users;";

			Statement stm = connection.createStatement();

			ResultSet result = stm.executeQuery(query);

			while (result.next()) {
				User usr = new User();
				usr.setFirstName(result.getString(1));
				usr.setLastName(result.getString(2));
				users.add(usr);
			}

		} catch (SQLException e) {
			System.out.println(e.toString());
		}

		return users;
	}

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String storeUserData(User user) {
		try {
			String query = "Insert Into users values(?,?)";

			PreparedStatement preparedStmt = connection.prepareStatement(query);
			preparedStmt.setString(1, user.getFirstName());
			preparedStmt.setString(2, user.getLastName());

			if (preparedStmt.executeUpdate() > 0) {
				return "User Inserted";
			}
		} catch (SQLException e) {
			System.out.println(e.toString());
			return e.toString();
		}

		return "Error While inserting";
	}

	@Override
	public void filter(ContainerRequestContext requestContext,
			ContainerResponseContext responseContext) throws IOException {

		MultivaluedMap<String, Object> headers = responseContext.getHeaders();

		headers.add("Access-Control-Allow-Origin", "*");
		// headers.add("Access-Control-Allow-Origin",
		// "http://podcastpedia.org"); //allows CORS requests only coming from
		// podcastpedia.org
		headers.add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
		headers.add("Access-Control-Allow-Headers",
				"X-Requested-With, Content-Type, X-Codingpedia");
	}

}