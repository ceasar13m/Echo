//package com.company.servlets;
//
//import com.company.MySQLRepositoryImplementation;
//import com.company.Repository;
//import com.company.model.Good;
//import com.company.util.HttpStatus;
//import com.google.gson.Gson;
//
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.BufferedReader;
//import java.io.PrintWriter;
//
//@WebServlet(name = "GoodsServlet", urlPatterns = "/good")
//public class GoodsServlet extends HttpServlet {
//
//    Repository repository;
//
//    public GoodsServlet() {
//        repository = new MySQLRepositoryImplementation();
//    }
//
//    /**
//     * Get запрос - это получение чего то
//     * @param request
//     * @param response
//     */
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
//        try {
//
//            String myJson = repository.getAllGoods();
//
//            response.setStatus(HttpStatus.OK);
//            response.setContentType("application/json");
//            PrintWriter out = response.getWriter();
//            out.println(myJson);
//
//
//
//        } catch (Exception e) {
//            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
//            e.printStackTrace();
//        }
//
//    }
//
//
//    /**
//     * PUT - это добавить
//     * @param request
//     * @param response
//     */
//    @Override
//    protected void doPut(HttpServletRequest request, HttpServletResponse response) {
//        try {
//            BufferedReader reader = request.getReader();
//            Gson gson = new Gson();
//
//            Good newGood = gson.fromJson(reader, Good.class);
//            repository.addGood(newGood);
//
//            response.setStatus(HttpStatus.OK);
//            // ничего можем не возвращать (текст в смысле)
//        } catch (Exception e) {
//            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
//            e.printStackTrace();
//        }
//    }
//
//
//    /**
//     * Изменить товар, POST запрос
//     * @param request
//     * @param response
//     */
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
//
//    }
//
//
//    /**
//     * Удалить товар
//     * @param request
//     * @param response
//     */
//    @Override
//    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
//
//    }
//
//}
