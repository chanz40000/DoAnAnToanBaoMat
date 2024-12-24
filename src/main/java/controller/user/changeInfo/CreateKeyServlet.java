package controller.user.changeInfo;

import database.KeyUserDAO;
import database.UserDAO;
import model.ErrorBean;
import model.KeyUser;
import model.User;
import util.Email;
import util.PasswordEncryption;
import util.RSA;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Random;

import static util.Email.sendEmailWithAttachment;

@WebServlet(name = "CreateKeyServlet", value = "/CreateKeyServlet")
public class CreateKeyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //lay ra user hien tai
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");


        User user =(User) request.getSession().getAttribute("userC");
        String email = user.getEmail();

        //lay ra timestap hien tai
        KeyUserDAO keyUserDAO = new KeyUserDAO();
        KeyUser keyUser = keyUserDAO.selectByUserIdStatus(user.getUserId(), "ON");
        Timestamp timestamp = keyUser.getCreate_at();

        if (user != null) {
            HttpSession mySession = request.getSession();

            int otpvalue = 0;

            if (email != null || !email.equals("")) {
                // sending otp
                Random rand = new Random();
                otpvalue = rand.nextInt(1255650);

                String to = email;// change accordingly
                // Get the session object
                Email.sendEmail(email, "Mã OTP đổi key của bạn là: " + otpvalue, "Xin chào");
                System.out.println("message sent successfully");
                request.setAttribute("otpKey", otpvalue);

                mySession.setAttribute("otpKey", otpvalue);
                mySession.setAttribute("email", email);
                request.setAttribute("message", "Mã OTP đã được gửi tới email của bạn!");
                request.setAttribute("timestamp", timestamp);
                String url = request.getContextPath() + "/WEB-INF/book/enterOtpKey.jsp";
                RequestDispatcher dispatcher = request.getRequestDispatcher(url);
                dispatcher.forward(request, response);
                //request.setAttribute("status", "success");
            }
        }
        request.getRequestDispatcher("/WEB-INF/book/enterOtpKey.jsp").forward(request, response);
    }
}