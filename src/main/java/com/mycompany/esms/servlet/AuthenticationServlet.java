package com.mycompany.esms.servlet;

import com.mycompany.esms.dao.MemberDAO;
import com.mycompany.esms.dao.MemberDAO.AuthenticationResult;
import com.mycompany.esms.model.Member;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/auth/*")
public class AuthenticationServlet extends HttpServlet {
    private MemberDAO memberDAO;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public void init() {
        memberDAO = new MemberDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String action = request.getPathInfo();
        if (action == null) {
            response.sendRedirect(request.getContextPath() + "/auth/showLogin");
            return;
        }

        try {
            switch (action) {
                case "/login":
                    login(request, response);
                    break;
                case "/register":
                    register(request, response);
                    break;
                case "/logout":
                    logout(request, response);
                    break;
                case "/showLogin":
                    showLoginForm(request, response);
                    break;
                case "/showRegister":
                    showRegisterForm(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/auth/showLogin");
                    break;
            }
        } catch (SQLException ex) {
            System.err.println("SQL Exception occurred: " + ex.getMessage());
            request.setAttribute("errorMessage", "Database error occurred: " + ex.getMessage());
            showErrorPage(request, response);
        } catch (Exception ex) {
            System.err.println("Unexpected Exception: " + ex.getMessage());
            request.setAttribute("errorMessage", "An unexpected error occurred: " + ex.getMessage());
            showErrorPage(request, response);
        }
    }

    private void showLoginForm(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/member/login.jsp");
        dispatcher.forward(request, response);
    }

    private void showRegisterForm(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/member/register.jsp");
        dispatcher.forward(request, response);
    }

    private void login(HttpServletRequest request, HttpServletResponse response)
    throws SQLException, IOException, ServletException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Username and password are required");
            showLoginForm(request, response);
            return;
        }

        AuthenticationResult result = memberDAO.authenticateWithLock(username, password);

        switch (result.getStatus()) {
            case SUCCESS: {
                Member member = result.getMember();
                HttpSession session = request.getSession();
                session.setAttribute("currentUser", member);
                session.setAttribute("username", member.getUsername());
                session.setAttribute("userId", member.getId());

                String role = member.getRole();
                if (role != null) {
                    if ("customer".equalsIgnoreCase(role)) {
                        response.sendRedirect(request.getContextPath() + "/customer/customerHomeView.jsp");
                    } else if ("employee".equalsIgnoreCase(role)) {
                        // Lấy position từ tblEmployee để xác định quyền truy cập
                        String position = memberDAO.getEmployeePosition(member.getId());
                        session.setAttribute("employeePosition", position);
                        
                        if (position != null && "manager".equalsIgnoreCase(position)) {
                            response.sendRedirect(request.getContextPath() + "/manager/managerHomeView.jsp");
                        } else {
                            request.setAttribute("errorMessage", "Tính năng này chưa phát triển. Vị trí của bạn (" + position + ") hiện chưa được hỗ trợ.");
                            showErrorPage(request, response);
                        }
                    } else {
                        request.setAttribute("errorMessage", "Tính năng này chưa phát triển. Vai trò của bạn (" + role + ") hiện chưa được hỗ trợ.");
                        showErrorPage(request, response);
                    }
                } else {
                    response.sendRedirect(request.getContextPath() + "/customer/customerHomeView.jsp");
                }
                break;
            }
            case ACCOUNT_LOCKED: {
                request.setAttribute("lockedUsername", username);
                request.getRequestDispatcher("/member/accountLocked.jsp").forward(request, response);
                break;
            }
            case INVALID_CREDENTIALS: {
                int remaining = result.getRemainingAttempts();
                final int maxAttempts = 5;
                StringBuilder message = new StringBuilder("Tên đăng nhập hoặc mật khẩu không đúng.");
                if (remaining >= 0 && remaining < maxAttempts) {
                    message.append(" Bạn còn ").append(remaining).append(" lần thử trước khi tài khoản bị khóa.");
                }
                request.setAttribute("errorMessage", message.toString());
                request.setAttribute("username", username);
                showLoginForm(request, response);
                break;
            }
            default:
                break;
        }
    }

    private void register(HttpServletRequest request, HttpServletResponse response)
    throws SQLException, IOException, ServletException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String birthDateStr = request.getParameter("birthDate");

        // Validation
        if (name == null || email == null || phoneNumber == null || username == null || 
            password == null || confirmPassword == null ||
            name.trim().isEmpty() || email.trim().isEmpty() || username.trim().isEmpty() || 
            password.trim().isEmpty()) {
            request.setAttribute("errorMessage", "All fields are required");
            request.setAttribute("name", name);
            request.setAttribute("email", email);
            request.setAttribute("phoneNumber", phoneNumber);
            request.setAttribute("username", username);
            request.setAttribute("birthDate", birthDateStr);
            showRegisterForm(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Passwords do not match");
            request.setAttribute("name", name);
            request.setAttribute("email", email);
            request.setAttribute("phoneNumber", phoneNumber);
            request.setAttribute("username", username);
            request.setAttribute("birthDate", birthDateStr);
            showRegisterForm(request, response);
            return;
        }

        // Check if username already exists
        if (memberDAO.isUsernameExists(username)) {
            request.setAttribute("errorMessage", "Username already exists");
            request.setAttribute("name", name);
            request.setAttribute("email", email);
            request.setAttribute("phoneNumber", phoneNumber);
            request.setAttribute("birthDate", birthDateStr);
            showRegisterForm(request, response);
            return;
        }

        // Check if email already exists
        if (memberDAO.isEmailExists(email)) {
            request.setAttribute("errorMessage", "Email already exists");
            request.setAttribute("name", name);
            request.setAttribute("phoneNumber", phoneNumber);
            request.setAttribute("username", username);
            request.setAttribute("birthDate", birthDateStr);
            showRegisterForm(request, response);
            return;
        }

        // Parse birth date
        Date birthDate = null;
        if (birthDateStr != null && !birthDateStr.trim().isEmpty()) {
            try {
                birthDate = dateFormat.parse(birthDateStr);
            } catch (ParseException e) {
                System.err.println("Error parsing birth date: " + e.getMessage());
            }
        }

        // Map to ESMS Member (uses LocalDate)
        Member newUser = new Member();
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setPhoneNumber(phoneNumber);
        newUser.setUsername(username);
        newUser.setPassword(password);
        if (birthDate != null) {
            newUser.setBirthdate(birthDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate());
        }
        newUser.setRole("customer");
        memberDAO.insertMember(newUser);

        // Registration successful
        request.setAttribute("successMessage", "Registration successful! Please login.");
        showLoginForm(request, response);
    }

    private void logout(HttpServletRequest request, HttpServletResponse response)
    throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        // Redirect to root after logout
        response.sendRedirect(request.getContextPath() + "/");
    }

    private void showErrorPage(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/member/error.jsp");
        dispatcher.forward(request, response);
    }
} 
