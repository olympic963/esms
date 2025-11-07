package com.mycompany.esms.servlet;

import com.mycompany.esms.dao.MemberDAO;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code if needed
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        
        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        
        // Always prevent caching to avoid back-button showing stale pages
        httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
        httpResponse.setHeader("Pragma", "no-cache"); // HTTP 1.0
        httpResponse.setDateHeader("Expires", 0); // Proxies
        
        // Define public paths that don't require authentication
        boolean isPublicPath = requestURI.equals(contextPath) ||
                              requestURI.equals(contextPath + "/") ||
                              requestURI.equals(contextPath + "/customer/customerHomeView.jsp") ||
                              requestURI.equals(contextPath + "/customer/searchItemsView.jsp") ||
                              requestURI.equals(contextPath + "/ItemServlet") ||
                              requestURI.equals(contextPath + "/auth/showLogin") ||
                              requestURI.equals(contextPath + "/auth/login") ||
                              requestURI.equals(contextPath + "/auth/showRegister") ||
                              requestURI.equals(contextPath + "/auth/register") ||
                              requestURI.equals(contextPath + "/member/login.jsp") ||
                              requestURI.equals(contextPath + "/member/register.jsp") ||
                              requestURI.equals(contextPath + "/member/accountLocked.jsp") ||
                              requestURI.equals(contextPath + "/member/error.jsp") ||
                              requestURI.contains("/css/") ||
                              requestURI.contains("/js/") ||
                              requestURI.contains("/images/");
        
        // Check if user is authenticated
        boolean isLoggedIn = (session != null && session.getAttribute("currentUser") != null);
        
        // Load employee position if missing from session (for backward compatibility)
        if (isLoggedIn) {
            com.mycompany.esms.model.Member currentUser = (com.mycompany.esms.model.Member) session.getAttribute("currentUser");
            if (currentUser != null && "employee".equalsIgnoreCase(currentUser.getRole())) {
                String position = (String) session.getAttribute("employeePosition");
                if (position == null) {
                    // Load position from database
                    MemberDAO memberDAO = new MemberDAO();
                    position = memberDAO.getEmployeePosition(currentUser.getId());
                    if (position != null) {
                        session.setAttribute("employeePosition", position);
                    }
                }
            }
        }
        
        // If already logged in and trying to access login/register pages, redirect to appropriate dashboard based on role
        boolean isAuthPage = requestURI.equals(contextPath + "/auth/showLogin") ||
                             requestURI.equals(contextPath + "/auth/login") ||
                             requestURI.equals(contextPath + "/member/login.jsp") ||
                             requestURI.equals(contextPath + "/auth/showRegister") ||
                             requestURI.equals(contextPath + "/auth/register") ||
                             requestURI.equals(contextPath + "/member/register.jsp");
        if (isLoggedIn && isAuthPage) {
            // Redirect based on user role and position
            com.mycompany.esms.model.Member currentUser = (com.mycompany.esms.model.Member) session.getAttribute("currentUser");
            if (currentUser != null) {
                String role = currentUser.getRole();
                if ("employee".equalsIgnoreCase(role)) {
                    String position = (String) session.getAttribute("employeePosition");
                    if (position != null && "manager".equalsIgnoreCase(position)) {
                        httpResponse.sendRedirect(contextPath + "/manager/managerHomeView.jsp");
                        return;
                    }
                }
            }
            httpResponse.sendRedirect(contextPath + "/customer/customerHomeView.jsp");
            return;
        }
        
        // Manager pages require authentication
        boolean isManagerPage = requestURI.startsWith(contextPath + "/manager/");
        if (isManagerPage && !isLoggedIn) {
            httpResponse.sendRedirect(contextPath + "/auth/showLogin");
            return;
        }
        if (isManagerPage && isLoggedIn) {
            // Check if user has manager position (employee with position = "manager")
            com.mycompany.esms.model.Member currentUser = (com.mycompany.esms.model.Member) session.getAttribute("currentUser");
            if (currentUser == null) {
                httpResponse.sendRedirect(contextPath + "/customer/customerHomeView.jsp");
                return;
            }
            String role = currentUser.getRole();
            if (!"employee".equalsIgnoreCase(role)) {
                httpResponse.sendRedirect(contextPath + "/customer/customerHomeView.jsp");
                return;
            }
            String position = (String) session.getAttribute("employeePosition");
            if (position == null || !"manager".equalsIgnoreCase(position)) {
                httpResponse.sendRedirect(contextPath + "/customer/customerHomeView.jsp");
                return;
            }
        }
        
        if (isPublicPath || isLoggedIn) {
            // Allow access to public paths or authenticated users
            chain.doFilter(request, response);
        } else {
            // Redirect to login page for unauthenticated users
            httpResponse.sendRedirect(contextPath + "/auth/showLogin");
        }
    }
    
    @Override
    public void destroy() {
        // Cleanup code if needed
    }
} 