package controller.user.changeInfo;

import database.KeyUserDAO;
import model.KeyUser;
import model.User;
import util.Email;
import util.RSA;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

@WebServlet(name = "CreateKeyServlet", value = "/CreateKeyServlet")
public class CreateKeyServlet extends HttpServlet {
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
        //lay ra public key
        String publicKey = keyUser.getKey();


        //lay ra private key
        String privateKey = request.getParameter("keyContent");
        System.out.println("privateKey: "+ privateKey);
        System.out.println("publickey: "+publicKey);
        //kiem tra xem key co khop nhau khong
        RSA rsa = new RSA();
        boolean isCreated = rsa.validateRSAKeys(publicKey, privateKey);

        if (isCreated) {
            this.updatekeyy(keyUser);
            request.setAttribute("message", "Khóa đã được tạo thành công!");
        } else {
            request.setAttribute("errorBean", "Tạo khóa thất bại. Vui lòng thử lại!");
            request.setAttribute("message", "Tạo khóa thất bại. Vui lòng thử lại!");
        }

        // Forward response back to the JSP
        request.getRequestDispatcher("/WEB-INF/book/profile.jsp").forward(request, response);
    }

    public boolean updatekeyy(KeyUser keyUser){
        KeyUserDAO keyUserDAO = new KeyUserDAO();
        Date date = new Date(System.currentTimeMillis());
        //DOI NGAY het han
        keyUser.setExpired_at(date);
        //doi status
        keyUser.setStatus("OFF");
        //CAP NHAT VAO DATABASE
        keyUserDAO.update(keyUser);

        //Them moi mot Key khac cho user
        RSA rsa = new RSA();
        try {
            keyUserDAO.insert(new KeyUser(keyUser.getUser_id(), rsa.getPublicKey(), date, new Date(2999, 12, 30), "ON"));
            //gui mail
            String emailSubject = "Thong bao cap nhat khoa bao mat!";
            String emailBody = "Hellooo,\n\n" +
                    "Khoa bao mat moi cua ban la: \n" + rsa.getPrivateKey() + "\n\n" +
                    "Vui long gi khoa va khong chia se voi nguoi khac.\n\n" +
                    "Tran trong,\nCua hang sach cutee.";

            Email.sendEmail(keyUser.getUser_id().getEmail(), emailBody, emailSubject);
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}