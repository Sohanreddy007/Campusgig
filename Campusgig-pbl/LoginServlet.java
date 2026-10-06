package com.campusgig.servlet;

import com.campusgig.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.security.MessageDigest;

public class LoginServlet extends HttpServlet {

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

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || email.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {

            response.setContentType("text/html;charset=UTF-8");

            response.getWriter().println(
                    "<h2>Please enter email and password.</h2>" +
                    "<a href='login.html'>Go Back</a>"
            );

            return;
        }

        try {

            String hashedPassword = hashPassword(password);

            String sql =
                    "SELECT student_id, name, email " +
                    "FROM Students " +
                    "WHERE email = ? AND password = ?";

            try (
                    Connection con = DBConnection.getConnection();
                    PreparedStatement ps = con.prepareStatement(sql)
            ) {

                ps.setString(1, email.trim());
                ps.setString(2, hashedPassword);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {

                        int studentId = rs.getInt("student_id");
                        String name = rs.getString("name");
                        String userEmail = rs.getString("email");

                        HttpSession session = request.getSession();

                        session.setAttribute("studentId", studentId);
                        session.setAttribute("studentName", name);
                        session.setAttribute("studentEmail", userEmail);

                        response.sendRedirect("dashboard.html");

                    } else {

                        response.setContentType("text/html;charset=UTF-8");

                        response.getWriter().println(
                                "<html>" +
                                "<head><title>Login Failed</title></head>" +
                                "<body style='font-family:Arial;text-align:center;padding:80px'>" +
                                "<h1>Invalid Login</h1>" +
                                "<p>Incorrect email or password.</p>" +
                                "<br>" +
                                "<a href='login.html'>Try Again</a>" +
                                "</body>" +
                                "</html>"
                        );
                    }
                }
            }

        } catch (Exception e) {

            e.printStackTrace();

            response.setContentType("text/html;charset=UTF-8");

            response.getWriter().println(
                    "<h2>Login Error</h2>" +
                    "<p>" + e.getMessage() + "</p>"
            );
        }
    }
}