package com.mycompany.esms.servlet;

import com.mycompany.esms.dao.ItemDAO;
import com.mycompany.esms.model.Item;
import java.io.IOException;
import java.net.URLEncoder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ArrayList;

@WebServlet(name = "ItemServlet", urlPatterns = {"/ItemServlet"})
public class ItemServlet extends HttpServlet {
    private ItemDAO itemDAO;

    public void init() {
        itemDAO = new ItemDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("detail".equalsIgnoreCase(action)) {
            viewItemDetails(request, response);
            return;
        }
        searchItems(request, response);
    }
    private void searchItems(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        String pageParam = request.getParameter("page");
        int page = 1;
        int pageSize = 10;
        if (pageParam != null) {
            try { page = Math.max(1, Integer.parseInt(pageParam)); } catch (NumberFormatException ignored) {}
        }

        boolean searched = keyword != null && !keyword.trim().isEmpty();
        if (!searched) {
            request.setAttribute("searched", false);
            request.getRequestDispatcher("/customer/searchItemsView.jsp").forward(request, response);
            return;
        }

        // Enforce page parameter in URL: redirect to page=1 when missing
        if (pageParam == null) {
            String encodedKeyword = URLEncoder.encode(keyword.trim(), StandardCharsets.UTF_8);
            String redirectUrl = request.getContextPath() + "/ItemServlet?action=search&keyword=" + encodedKeyword + "&page=1";
            response.sendRedirect(redirectUrl);
            return;
        }

        List<Item> all = itemDAO.getKeywordItems(keyword.trim());

        int total = all.size();
        int totalPages = Math.max(1, (int) Math.ceil(total / (double) pageSize));
        if (page > totalPages) page = totalPages;
        int offset = (page - 1) * pageSize;
        int toIndex = Math.min(offset + pageSize, total);
        List<Item> items = all.subList(offset, toIndex);

        request.setAttribute("searched", true);
        request.setAttribute("keyword", keyword.trim());
		request.setAttribute("listItem", items);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("offset", offset);

        // Build pagination model similar to Angular example
        int windowSize = 2; // pages around current
        int start = Math.max(1, page - windowSize);
        int end = Math.min(totalPages, page + windowSize);
        List<Integer> pageRange = new ArrayList<>();
        for (int p = start; p <= end; p++) {
            pageRange.add(p);
        }
        boolean ellipsisFirst = start > 2; // there is a gap after page 1
        boolean ellipsisEnd = end < totalPages - 1; // there is a gap before last page

        request.setAttribute("pageRange", pageRange);
        request.setAttribute("ellipsisFirst", ellipsisFirst);
        request.setAttribute("ellipsisEnd", ellipsisEnd);

        request.getRequestDispatcher("/customer/searchItemsView.jsp").forward(request, response);
    }

    private void viewItemDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam == null) {
            response.sendRedirect(request.getContextPath() + "/customer/searchItemsView.jsp");
            return;
        }
        int id;
        try { id = Integer.parseInt(idParam); } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/customer/searchItemsView.jsp");
            return;
        }
        Item item = itemDAO.getItemInfo(id);
        request.setAttribute("item", item);
        request.getRequestDispatcher("/customer/itemDetailsView.jsp").forward(request, response);
    }
}
