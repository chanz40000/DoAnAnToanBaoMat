package controller.user.checkout;

import database.OrderDAO;
import model.ErrorBean;
import model.Order;
import model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;

@WebServlet(name = "VerifyOTPBank", value = "/VerifyOTPBank")
public class VerifyOTPBank extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Handle GET requests if necessary
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // Retrieve the OTP value entered by the user
        String otpInput = request.getParameter("otpnhap");

        // Retrieve OTP from the session (stored earlier when the OTP was sent)
        HttpSession session = request.getSession();
        String otpFromSession = request.getParameter("otpbank");
        // If OTP from session is null, that means session expired or OTP was not stored
        if (otpFromSession == null) {
            request.setAttribute("error", "OTP session expired. Please try again.");
            forwardToOTPPage(request, response);
            return;
        }

        // Check if the OTP from the user matches the one stored in the session
        try {
            if (otpInput != null && !otpInput.isEmpty()) {
                // Convert OTP input to an integer

                // Check if OTPs match
                if (otpInput.equals(otpFromSession)) {
                    // Get user from session
                    User user = (User) session.getAttribute("userC");

                    // Get the latest order ID for this user with payment ID 1
                    OrderDAO orderDAO = new OrderDAO();
                    int orderId = orderDAO.selectLatestOrderIdByUserAndPayment(user.getUserId());

                    // If an order ID is found, redirect to the order verification page
                    if (orderId != -1) {
                        response.sendRedirect(request.getContextPath() + "/verify-order?OrderIdVerify=" + orderId);
                    } else {
                        // If no valid order is found, display an error message
                        request.setAttribute("error", "No valid order found for the user.");
                        forwardToOTPPage(request, response);
                    }
                } else {
                    // If OTPs do not match, display error
                    request.setAttribute("error", "Incorrect OTP entered.");
                    forwardToOTPPage(request, response);
                }
            } else {
                // If OTP input is empty
                request.setAttribute("error", "OTP cannot be empty.");
                forwardToOTPPage(request, response);
            }
        } catch (NumberFormatException e) {
            // If OTP input is not a valid number
            request.setAttribute("error", "Invalid OTP format.");
            forwardToOTPPage(request, response);
        }
    }

    // Helper method to forward to OTP page with error message
    private void forwardToOTPPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = request.getContextPath() + "/WEB-INF/book/OTPBank.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(url);
        dispatcher.forward(request, response);
    }
}
