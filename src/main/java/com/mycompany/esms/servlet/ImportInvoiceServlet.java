package com.mycompany.esms.servlet;

import com.mycompany.esms.dao.ImportInvoiceDAO;
import com.mycompany.esms.model.ImportInvoice;
import com.mycompany.esms.model.ImportDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet(name = "ImportInvoiceServlet", urlPatterns = {"/manager/importedSupplier", "/manager/importDetails"})
public class ImportInvoiceServlet extends HttpServlet {
    private ImportInvoiceDAO dao;

    public void init() { dao = new ImportInvoiceDAO(); }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/manager/importDetails".equals(path)) {
            showImportDetails(request, response);
        } else {
            showImportedSupplier(request, response);
        }
    }

    protected void showImportedSupplier(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String supplierIdStr = request.getParameter("supplierId");
        String start = request.getParameter("startDate");
        String end = request.getParameter("endDate");

        int supplierId = 0;
        try { supplierId = Integer.parseInt(supplierIdStr); } catch (Exception ignored) {}
        LocalDate startDate = (start != null && !start.isEmpty()) ? LocalDate.parse(start) : LocalDate.now().minusMonths(1);
        LocalDate endDate = (end != null && !end.isEmpty()) ? LocalDate.parse(end) : LocalDate.now();

        List<ImportInvoice> invoices = dao.getInvoiceBySupplier(supplierId, startDate, endDate);
        // Lấy supplierName từ invoice đầu tiên (nếu có) vì đã được đóng gói trong ImportInvoice
        String supplierName = null;
        if (!invoices.isEmpty() && invoices.get(0).getSupplier() != null) {
            supplierName = invoices.get(0).getSupplier().getName();
        }

        request.setAttribute("supplierName", supplierName);
        request.setAttribute("startDate", startDate.toString());
        request.setAttribute("endDate", endDate.toString());
        request.setAttribute("invoices", invoices);
        request.getRequestDispatcher("/manager/importedSupplierView.jsp").forward(request, response);
    }

    protected void showImportDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String importInvoiceIdStr = request.getParameter("importInvoiceId");
        
        if (importInvoiceIdStr == null || importInvoiceIdStr.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Import Invoice ID is required");
            return;
        }
        
        int importInvoiceId = 0;
        try {
            importInvoiceId = Integer.parseInt(importInvoiceIdStr);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Import Invoice ID");
            return;
        }
        
        // Lấy danh sách chi tiết các mặt hàng (đã đóng gói đầy đủ thông tin Invoice)
        List<ImportDetails> details = dao.getImportInvoiceDetails(importInvoiceId);
        
        if (details.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Import Invoice not found");
            return;
        }
        
        // Lấy invoice từ detail đầu tiên để hiển thị thông tin chung
        ImportInvoice invoice = details.get(0).getImportInvoice();
        
        request.setAttribute("invoice", invoice);
        request.setAttribute("details", details);
        request.getRequestDispatcher("/manager/importDetailsView.jsp").forward(request, response);
    }
}

