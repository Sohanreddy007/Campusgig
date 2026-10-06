package com.campusgig.servlet;

import com.campusgig.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLIntegrityConstraintViolationException;
import java.security.MessageDigest;

public class RegisterServlet extends HttpServlet {

    private String hashPassword(String password) throws Exception {

        MessageDigest md = MessageDigest.getInstance("SHA-256");

        byte[] hash = md.digest(password.getBytes("UTF-8"));

        StringBuilder result = new StringBuilder();

        for (byte b : hash) {
            result.append(String.format("%02x", b));
        }

        return result.toString();
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (name == null || name.trim().isEmpty()
                || email == null || email.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {

            response.setContentType("text/html;charset=UTF-8");

            response.getWriter().println(
                    "<h2>Please fill all fields.</h2>" +
                    "<a href='register.html'>Go Back</a>"
            );

            return;
        }

        try {

            String hashedPassword = hashPassword(password);

            String sql =
                    "INSERT INTO Students (name, email, password) " +
                    "VALUES (?, ?, ?)";

            try (
                    Connection con = DBConnection.getConnection();
                    PreparedStatement ps = con.prepareStatement(sql)
            ) {

                ps.setString(1, name.trim());
                ps.setString(2, email.trim());
                ps.setString(3, hashedPassword);

                int result = ps.executeUpdate();

                response.setContentType("text/html;charset=UTF-8");

                if (result > 0) {

                    response.getWriter().println(
                            "<html>" +
                            "<head><title>Registration Successful</title></head>" +
                            "<body style='font-family:Arial;text-align:center;padding:80px'>" +
                            "<h1>Registration Successful!</h1>" +
                            "<p>Welcome, " + name + "!</p>" +
                            "<p>Your CampusGig account has been created.</p>" +
                            "<br>" +
                            "<a href='login.html'>Go to Login</a>" +
                            "</body>" +
                            "</html>"
                    );
                }
            }

        } catch (SQLIntegrityConstraintViolationException e) {

            response.setContentType("text/html;charset=UTF-8");

            response.getWriter().println(
                    "<h2>Email Already Registered</h2>" +
                    "<p>Please use a different email address.</p>" +
                    "<a href='register.html'>Go Back</a>"
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.setContentType("text/html;charset=UTF-8");

            response.getWriter().println(
                    "<h2>Registration Error</h2>" +
                    "<p>" + e.getMessage() + "</p>"
            );
        }
    }
}