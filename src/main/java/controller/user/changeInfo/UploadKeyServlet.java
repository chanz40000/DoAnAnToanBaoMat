package controller.user.changeInfo;

import database.KeyUserDAO;
import model.KeyUser;
import model.User;
import util.Email;
import util.PasswordEncryption;
import util.RSA;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet(name = "UploadKeyServlet", value = "/UploadKeyServlet")
public class UploadKeyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //lay ra user hien tai
        User user =(User) request.getSession().getAttribute("userC");
        System.out.println("user: "+ user);
        //lay ra key user hien tai
        KeyUserDAO keyUserDAO = new KeyUserDAO();
        KeyUser keyUser = keyUserDAO.selectByUserIdStatus(user.getUserId(), "ON");
        System.out.println("keyUser: "+ keyUser.toString());




        String password = request.getParameter("password");
        password = PasswordEncryption.toSHA1(password);
        //kiem tra xem key co khop nhau khong

        boolean isCreated = password.equals(user.getPassword());

        if (isCreated) {
            String publicKey = request.getParameter("keyContent");
            this.updatekeyy(keyUser, publicKey);
            request.setAttribute("message", "Khóa mới đã thêm thành công!");
            request.setAttribute("type", "success");
        } else {
            request.setAttribute("errorBean", "Pass bị sai. Vui lòng thử lại!");
            request.setAttribute("message", "Pass bị sai. Vui lòng thử lại!");
            request.setAttribute("type", "error");
        }

        // Forward response back to the JSP
        request.getRequestDispatcher("/WEB-INF/book/profile.jsp").forward(request, response);
    }

    public boolean updatekeyy(KeyUser keyUser, String publicKey) {
        KeyUserDAO keyUserDAO = new KeyUserDAO();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = now.format(formatter);
        Timestamp reportTimestamp = Timestamp.valueOf(formattedDate);
        //DOI NGAY het han
        keyUser.setExpired_at(reportTimestamp);
        //doi status
        keyUser.setStatus("OFF");
        //CAP NHAT VAO DATABASE
        keyUserDAO.update(keyUser);

        //Them moi mot Key khac cho user
        try {
            keyUserDAO.insert(new KeyUser(keyUser.getUser_id(), publicKey, reportTimestamp,  Timestamp.valueOf("2038-01-19 03:14:07"), "ON"));
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}