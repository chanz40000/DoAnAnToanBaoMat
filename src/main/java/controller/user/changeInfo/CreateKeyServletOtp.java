package controller.user.changeInfo;

import database.KeyUserDAO;
import model.ErrorBean;
import model.KeyUser;
import model.User;
import util.Email;
import util.PasswordEncryption;
import util.RSA;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet(name = "CreateKeyServletOtp", value = "/CreateKeyServletOtp")
public class CreateKeyServletOtp extends HttpServlet {
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


        HttpSession session = request.getSession();
        int value = Integer.parseInt(request.getParameter("otpKey"));
        int otp = (int) session.getAttribute("otpKey");
        boolean isCreated;
//        String password = request.getParameter("password");
//        password = PasswordEncryption.toSHA1(password);

        if(value==otp){
            isCreated = true;
        }else{
            isCreated = false;
        }

        if (isCreated) {
            this.updatekeyy(keyUser);
            request.setAttribute("message", "Khóa mới đã được tạo thành công!");
            request.setAttribute("type", "success");
        } else {
                request.setAttribute("message", "Sai mã OTP");
                ErrorBean eb = new ErrorBean();
                eb.setError((String) request.getAttribute("message"));
                request.setAttribute("errorBean", eb);

                String url = request.getContextPath() + "/WEB-INF/book/enterOtpKey.jsp";
                RequestDispatcher dispatcher = request.getRequestDispatcher(url);
                dispatcher.forward(request, response);
                return;
//            request.setAttribute("errorBean", "Tạo mới khóa thất bại. Vui lòng thử lại!");
//            request.setAttribute("message", "Mật khẩu sai. Vui lòng thử lại!");
//            request.setAttribute("type", "error");
        }

//         Forward response back to the JSP
                request.getRequestDispatcher("/WEB-INF/book/profile.jsp").forward(request, response);

    }

    public boolean updatekeyy(KeyUser keyUser) {
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
        RSA rsa = new RSA();
        try {
            keyUserDAO.insert(new KeyUser(keyUser.getUser_id(), rsa.getPublicKey(), reportTimestamp,  Timestamp.valueOf("2038-01-19 03:14:07"), "ON"));
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