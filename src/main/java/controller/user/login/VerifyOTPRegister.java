package controller.user.login;

import database.KeyUserDAO;
import database.UserDAO;
import model.ErrorBean;
import model.KeyUser;
import model.User;
import util.Email;
import util.RSA;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(name = "VerifyOTPRegister", value = "/VerifyOTPRegister")
public class VerifyOTPRegister extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html; charset=UTF-8");
            HttpSession session = request.getSession();

            // Retrieve email from session (assuming email was saved earlier in session)
            String emailFromSession = (String) session.getAttribute("emailre");

            String enteredOtp = request.getParameter("otpregister");
            int generatedOtp = (Integer) session.getAttribute("otpregister");

            // Get user data from the hidden inputs
            String username = request.getParameter("usernamere");
            String name = request.getParameter("namere");
            String email = request.getParameter("emailre");
            String password = request.getParameter("passwordre");
            System.out.println(email);

            String url = "/WEB-INF/book/register.jsp"; // Default URL in case of error

            // Verify the OTP
            if (Integer.parseInt(enteredOtp) == generatedOtp) {
                UserDAO userDAO = new UserDAO();
                User customer = userDAO.selectByEmail2(email);
                RSA rsa = new RSA();
                String publicKey = rsa.getPublicKey();
                String privateKey = rsa.getPrivateKey();

                // Save the RSA keys in KeyUser table
                KeyUserDAO keyUserDAO = new KeyUserDAO();
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDate = now.format(formatter);
                Timestamp reportTimestamp = Timestamp.valueOf(formattedDate);
                Timestamp maxTimestamp = Timestamp.valueOf("2038-01-19 03:14:07");

                KeyUser keyUser = new KeyUser(customer, publicKey, reportTimestamp, maxTimestamp, "ON");
                keyUserDAO.insert(keyUser);

                // Send email with private key
                String emailSubject = "Thông báo đăng ký tài khoản thành công!";
                String emailBody = "Chúc mừng bạn đã trở thành khách hàng thân thiết của chúng tôi! \n\n" +
                        "Khoá bảo mật của bạn được đính kèm dưới đây. \nVui lòng giữ kín và không chia sẻ với người khác.";
                Email.sendEmailWithAttachment(email, emailSubject, emailBody, "private_key.txt", privateKey);

                // Redirect to login page
                url = "/WEB-INF/book/login.jsp";
            } else {
                // OTP is incorrect, return to OTP input page with error message
                request.setAttribute("error", "Mã OTP không đúng!");
                url = "/WEB-INF/book/enterOTPRegister.jsp";
            }

            // Forward the response to the appropriate page
            RequestDispatcher dispatcher = request.getRequestDispatcher(url);
            dispatcher.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("/WEB-INF/book/error.jsp"); // In case of error
        }
    }
}
