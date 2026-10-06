
package com.campusgig.servlet;

import com.campusgig.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GetGigServlet extends HttpServlet {

    private String escapeJson(String text) {

        if (text == null) {
            return "";
        }

        return text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\r", "\\r")
                .replace("\n", "\\n")
                .replace("\t", "\\t");
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");

        PrintWriter out = response.getWriter();

        String idParameter =
                request.getParameter("id");

        if (idParameter == null) {

            response.setStatus(
                    HttpServletResponse.SC_BAD_REQUEST
            );

            out.print(
                    "{\"error\":\"Gig ID is required\"}"
            );

            return;
        }

        String sql =
                "SELECT gig_id, title, description, budget, deadline " +
                "FROM Gigs WHERE gig_id = ?";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setInt(
                    1,
                    Integer.parseInt(idParameter)
            );

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    out.print("{");

                    out.print(
                            "\"id\":" +
                            rs.getInt("gig_id") +
                            ","
                    );

                    out.print(
                            "\"title\":\"" +
                            escapeJson(
                                rs.getString("title")
                            ) +
                            "\","
                    );

                    out.print(
                            "\"description\":\"" +
                            escapeJson(
                                rs.getString("description")
                            ) +
                            "\","
                    );

                    out.print(
                            "\"budget\":" +
                            rs.getDouble("budget") +
                            ","
                    );

                    out.print(
                            "\"deadline\":\"" +
                            escapeJson(
                                rs.getString("deadline")
                            ) +
                            "\""
                    );

                    out.print("}");

                } else {

                    response.setStatus(
                            HttpServletResponse.SC_NOT_FOUND
                    );

                    out.print(
                            "{\"error\":\"Gig not found\"}"
                    );
                }
            }

        } catch (Exception e) {

            e.printStackTrace();

            response.setStatus(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );

            out.print(
                    "{\"error\":\"" +
                    escapeJson(e.getMessage()) +
                    "\"}"
            );
        }
    }
}

