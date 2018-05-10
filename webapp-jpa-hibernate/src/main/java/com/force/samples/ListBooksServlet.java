package com.force.samples;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.*;
import java.util.Iterator;
import java.util.LinkedList;
import com.force.samples.entity.Author;

import com.force.samples.entity.Book;
import com.force.samples.util.PersistenceUtil;

public class ListBooksServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		EntityManager em = PersistenceUtil.getEntityManager();
		Query query = em.createQuery("select b from Book b");
		List<Book> books = query.getResultList();
		System.out.println("Books = " + books);


		// connect to the database
		Connection conn = null;
		LinkedList listOfAuthors = new LinkedList();

		conn = connectToDatabaseOrDie();

		// get the data
		populateListOfAuthors(conn, listOfAuthors);

		// print the results
		printAuthors(listOfAuthors);

		req.setAttribute("books", books);
		
		RequestDispatcher rd = getServletContext().getRequestDispatcher("/listResults.jsp");
		rd.forward(req, resp);
	}

	private Connection connectToDatabaseOrDie() {
		Connection conn = null;
		try {
			Class.forName("org.postgresql.Driver");
			String url = "jdbc:postgresql://db/app";
			conn = DriverManager.getConnection(url, "app", "app");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(2);
		}
		return conn;
	}


	private void printAuthors(LinkedList listOfAuthors) {
		Iterator it = listOfAuthors.iterator();
		while (it.hasNext()) {
			Author user = (Author) it.next();
			System.out.println("id: " + user.getId()+ ", firstName: " + user.getFirstName());
		}
	}

	private void populateListOfAuthors(Connection conn, LinkedList listOfAuthors) {
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT id, firstName, lastName FROM author");
			while (rs.next()) {
				Author user = new Author();
				user.setId(rs.getLong("id"));
				user.setFirstName(rs.getString("firstName"));
				user.setLastName(rs.getString("lastName"));
				listOfAuthors.add(user);
			}
			rs.close();
			st.close();
		} catch (SQLException se) {
			System.err.println("Threw a SQLException creating the list of blogs.");
			System.err.println(se.getMessage());
		}
	}
}
