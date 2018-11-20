package com.google.api.services.samples.adexchangebuyer.bulk_uploader;

//import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class bulk_upload extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            //System.out.println("Report1Servlet");
            PrintWriter out = response.getWriter();

            String  user_list = request.getParameter("userList");

            String status = Upload.upload(user_list);

            out.print(status);

            out.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
