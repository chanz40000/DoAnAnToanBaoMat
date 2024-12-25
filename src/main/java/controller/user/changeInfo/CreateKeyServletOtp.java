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


        String mess="";


        // Lấy giá trị thời gian từ form


        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = now.format(formatter);
        Timestamp noww = Timestamp.valueOf(formattedDate);

        String timeString = request.getParameter("time");
        System.out.println("time String: "+ timeString);
        Timestamp timestamp;
        if (timeString != null && !timeString.isEmpty()) {
            // Chuyển đổi sang kiểu dữ liệu Timestamp
            timestamp= Timestamp.valueOf(timeString.replace("T", " ") + ":00");

        } else {
            timestamp=noww;
        }

        //kiểm tra xem thoi gian hop le khong
        Timestamp create_At = keyUser.getCreate_at();

        boolean check_time =isTimestampInRange(timestamp, create_At, noww);
        if(!check_time){
            mess+="Thời gian lộ key không hợp lệ. ";
        }

        //kiem tra otp
        HttpSession session = request.getSession();
        int value = Integer.parseInt(request.getParameter("otpKey"));
        int otp = (int) session.getAttribute("otpKey");
        boolean check_Otp;

        if(value==otp){
            check_Otp = true;
        }else{
            mess+="OTP không hợp lệ . ";
            check_Otp = false;
        }

        if (check_Otp && check_time) {
            this.updatekeyy(keyUser, timestamp);
            request.setAttribute("message", "Khóa mới đã được tạo thành công!");
            request.setAttribute("type", "success");
        } else {
                request.setAttribute("message", mess);
                ErrorBean eb = new ErrorBean();
                eb.setError((String) request.getAttribute("message"));
                request.setAttribute("errorBean", eb);

                String url = request.getContextPath() + "/WEB-INF/book/enterOtpKey.jsp";
                RequestDispatcher dispatcher = request.getRequestDispatcher(url);
                dispatcher.forward(request, response);
                return;

        }

//         Forward response back to the JSP
                request.getRequestDispatcher("/WEB-INF/book/profile.jsp").forward(request, response);

    }

    public boolean isTimestampInRange(Timestamp target, Timestamp start, Timestamp end) {
        if (target == null || start == null || end == null) {
            throw new IllegalArgumentException("Các giá trị timestamp không được null.");
        }
        return !target.before(start) && !target.after(end);
    }
    public boolean updatekeyy(KeyUser keyUser, Timestamp reportTimestamp) {
        KeyUserDAO keyUserDAO = new KeyUserDAO();
        //DOI NGAY het han
        keyUser.setExpired_at(reportTimestamp);
        //doi status
        keyUser.setStatus("OFF");
        //CAP NHAT VAO DATABASE
        keyUserDAO.update(keyUser);


        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = now.format(formatter);
        Timestamp noww = Timestamp.valueOf(formattedDate);
        //Them moi mot Key khac cho user
        RSA rsa = new RSA();
        try {
            keyUserDAO.insert(new KeyUser(keyUser.getUser_id(), rsa.getPublicKey(), noww,  Timestamp.valueOf("2038-01-19 03:14:07"), "ON"));
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