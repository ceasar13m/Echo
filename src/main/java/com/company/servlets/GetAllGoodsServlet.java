package com.company.servlets;

import com.company.InMemoryRepositoryImplementation;
import com.company.MySQLRepositoryImplementation;
import com.company.Repository;
import com.company.util.HttpStatus;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class GetAllGoodsServlet extends HttpServlet {

    Repository repository = new MySQLRepositoryImplementation();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String myJson = repository.getAllGoods();
//            response.setStatus(HttpStatus.OK);
//            response.setContentType("application/json");
//            PrintWriter out = response.getWriter();
//            out.println(myJson);

            PrintWriter writer = response.getWriter();
            writer.println(myJson);

        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            e.printStackTrace();
            PrintWriter writer = response.getWriter();
            writer.println(e.fillInStackTrace());
        }
    }
}
