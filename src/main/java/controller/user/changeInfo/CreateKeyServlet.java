package controller.user.changeInfo;

import database.KeyUserDAO;
import database.UserDAO;
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
import java.sql.Date;
import java.sql.SQLException;

import static util.Email.sendEmailWithAttachment;

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

//        System.out.println("privateKey: "+ privateKey);
//        System.out.println("publickey: "+publicKey);
//        //kiem tra xem key co khop nhau khong
//        RSA rsa = new RSA();
//        boolean isCreated = rsa.validateRSAKeys(publicKey, privateKey);


        boolean isCreated;
        String password = request.getParameter("password");
        password = PasswordEncryption.toSHA1(password);

        if(user.getPassword().equals(password)){
            isCreated = true;
        }else{
            isCreated = false;
        }

        if (isCreated) {
            this.updatekeyy(keyUser);
            request.setAttribute("message", "Khóa mới đã được tạo thành công!");
            request.setAttribute("type", "success");
        } else {
            request.setAttribute("errorBean", "Tạo mới khóa thất bại. Vui lòng thử lại!");
            request.setAttribute("message", "Mật khẩu sai. Vui lòng thử lại!");
            request.setAttribute("type", "error");
        }

        // Forward response back to the JSP
        request.getRequestDispatcher("/WEB-INF/book/profile.jsp").forward(request, response);
    }

    public boolean updatekeyy(KeyUser keyUser) {
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
                    "Chung toi da gui khoa bao mat duoc luu trong file private_key.txt dinh kem o duoi cho ban. \n" +  "\n\n" +
                    "Vui long gi khoa va khong chia se voi nguoi khac.\n\n" +
                    "Tran trong,\nCua hang sach cutee.";

            // Send email with the created file as an attachment
            Email.sendEmailWithAttachment(keyUser.getUser_id().getEmail(), emailSubject, emailBody, "private_key.txt", rsa.getPrivateKey());
          return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}