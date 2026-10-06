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

public class GetGigsServlet extends HttpServlet {

    private String escapeJson(String text) {

        if (text == null) {
            return "";
        }

        return text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\r", "\\r")
                .replace("\n", "\\n")
                .replace("\t", "\\t")
                .replace("\b", "\\b")
                .replace("\f", "\\f");
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");

        PrintWriter out = response.getWriter();

        String sql =
                "SELECT gig_id, title, description, budget, deadline " +
                "FROM Gigs ORDER BY gig_id DESC";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            out.print("[");

            boolean first = true;

            while (rs.next()) {

                if (!first) {
                    out.print(",");
                }

                first = false;

                int id = rs.getInt("gig_id");

                String title =
                        escapeJson(rs.getString("title"));

                String description =
                        escapeJson(rs.getString("description"));

                double budget =
                        rs.getDouble("budget");

                String deadline =
                        escapeJson(rs.getString("deadline"));

                out.print("{");

                out.print("\"id\":" + id + ",");

                out.print("\"title\":\"" + title + "\",");

                out.print("\"description\":\"" + description + "\",");

                out.print("\"budget\":" + budget + ",");

                out.print("\"deadline\":\"" + deadline + "\"");

                out.print("}");
            }

            out.print("]");

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
