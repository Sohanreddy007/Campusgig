package com.campusgig.servlet;

import com.campusgig.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class PostGigServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String budget = request.getParameter("budget");
        String deadline = request.getParameter("deadline");

        if (title == null || title.trim().isEmpty()
                || description == null || description.trim().isEmpty()
                || budget == null || budget.trim().isEmpty()
                || deadline == null || deadline.trim().isEmpty()) {

            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().println("<h2>Missing required information.</h2>");
            return;
        }

        String sql =
                "INSERT INTO Gigs " +
                "(poster_id, title, description, budget, deadline) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setInt(1, 1);
            ps.setString(2, title);
            ps.setString(3, description);
            ps.setDouble(4, Double.parseDouble(budget));
            ps.setString(5, deadline);

            int rowsInserted = ps.executeUpdate();

            if (rowsInserted > 0) {
                response.sendRedirect("Gigs.html");
            } else {
                response.setContentType("text/html;charset=UTF-8");
                response.getWriter().println("<h2>Gig was not inserted.</h2>");
            }

        } catch (Exception e) {
            e.printStackTrace();

            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().println(
                    "<h2>Error while posting gig</h2>" +
                    "<p>" + e.getMessage() + "</p>"
            );
        }
    }
}
