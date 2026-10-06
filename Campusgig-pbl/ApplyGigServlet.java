
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

public class ApplyGigServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String gigId = request.getParameter("gig_id");

        if (gigId == null || gigId.trim().isEmpty()) {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().println(
                    "<h2>Error: Gig ID is missing.</h2>"
            );
            return;
        }

        HttpSession session = request.getSession(false);

        if (session == null ||
                session.getAttribute("studentId") == null) {

            response.sendRedirect("login.html");
            return;
        }

        int applicantId =
                (Integer) session.getAttribute("studentId");

        String pitchText =
                "I am interested in this gig and would like to apply.";

        String sql =
                "INSERT INTO Applications " +
                "(gig_id, applicant_id, pitch_text, portfolio_path) " +
                "VALUES (?, ?, ?, ?)";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setInt(1, Integer.parseInt(gigId));
            ps.setInt(2, applicantId);
            ps.setString(3, pitchText);
            ps.setString(4, null);

            int rowsInserted = ps.executeUpdate();

            response.setContentType("text/html;charset=UTF-8");

            if (rowsInserted > 0) {

                response.getWriter().println(
                        "<html>" +
                        "<head>" +
                        "<title>Application Submitted</title>" +
                        "</head>" +
                        "<body style='font-family:Arial;text-align:center;padding:80px'>" +
                        "<h1>Application Submitted Successfully!</h1>" +
                        "<p>Your application has been saved in the database.</p>" +
                        "<br>" +
                        "<a href='Gigs.html'>Back to Gigs</a>" +
                        "</body>" +
                        "</html>"
                );

            } else {

                response.getWriter().println(
                        "<h2>Application could not be submitted.</h2>"
                );
            }

        } catch (NumberFormatException e) {

            response.setStatus(
                    HttpServletResponse.SC_BAD_REQUEST
            );

            response.getWriter().println(
                    "<h2>Invalid Gig ID.</h2>"
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.setStatus(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );

            response.getWriter().println(
                    "<h2>Error while submitting application</h2>" +
                    "<p>" + e.getMessage() + "</p>"
            );
        }
    }
}

