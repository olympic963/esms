package com.mycompany.esms.servlet;

import com.mycompany.esms.dao.SupplierStatisticsDAO;
import com.mycompany.esms.model.SupplierStatistic;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet(name = "SupplierStatisticsServlet", urlPatterns = {"/manager/supplierStatistics"})
public class SupplierStatisticsServlet extends HttpServlet {
    private SupplierStatisticsDAO dao;

    public void init() { dao = new SupplierStatisticsDAO(); }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doSupplierStatistics(request, response);
    }

    // protected void doPost(HttpServletRequest request, HttpServletResponse response)
    //         throws ServletException, IOException {
    //     doSupplierStatistics(request, response);
    // }

    protected void doSupplierStatistics(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String start = request.getParameter("startDate");
        String end = request.getParameter("endDate");

        LocalDate startDate = null;
        LocalDate endDate = null;
        try {
            startDate = (start != null && !start.isEmpty()) ? LocalDate.parse(start) : LocalDate.now().minusMonths(1);
            endDate = (end != null && !end.isEmpty()) ? LocalDate.parse(end) : LocalDate.now();
        } catch (Exception ignored) {
            startDate = LocalDate.now().minusMonths(1);
            endDate = LocalDate.now();
        }

        List<SupplierStatistic> stats = dao.getSupplierStatistics(startDate, endDate);
        request.setAttribute("stats", stats);
        request.setAttribute("startDate", startDate.toString());
        request.setAttribute("endDate", endDate.toString());
        request.getRequestDispatcher("/manager/supplierStatisticsView.jsp").forward(request, response);
    }
}

