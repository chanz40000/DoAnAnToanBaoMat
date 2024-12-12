package controller.user;

import database.KeyUserDAO;
import database.OrderDAO;
import database.OrderDetailDAO;
import database.OrderSignatureDAO;
import model.*;
import util.Hash;
import util.RSA;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet(name = "VerifySignature", value = "/VerifySignature")
public class VerifySignature extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        String signature = request.getParameter("signature");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("userC");
        Order order = (Order) session.getAttribute("order");

        if (user == null || order == null || signature == null) {
            response.getWriter().write("{\"status\": \"error\", \"message\": \"Thông tin không đầy đủ!\"}");
            return;
        }

        try {
            KeyUserDAO keyUserDAO = new KeyUserDAO();
            KeyUser userKey = keyUserDAO.selectById(user.getUserId());
            String publicKey = userKey.getKey();

            OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
            List<OrderDetail> orderDetails = orderDetailDAO.selectByOrderId(order.getOrderId());
            OrderSignatureDAO orderSignatureDAO = new OrderSignatureDAO();
            OrderSignature orderSignature = orderSignatureDAO.selectByOrderId(order.getOrderId());

            RSA rsa = new RSA();
            boolean isValid = rsa.verifySignature(orderSignature.getHash(), signature, publicKey);

            if (!isValid) {
                response.getWriter().write("{\"status\": \"error\", \"message\": \"Chữ ký không hợp lệ!\"}");
                return;
            }

            // Cập nhật trạng thái đơn hàng
            StatusOrder statusOrder = new StatusOrder(12);
            OrderDAO orderDAO = new OrderDAO();
            orderDAO.updateStatusOrder(order.getOrderId(), statusOrder);
            orderSignatureDAO.updateSignatureAndStatusByOrderId(order.getOrderId(), signature, statusOrder);

            // Trả về URL chuyển hướng
            response.getWriter().write("{\"status\": \"success\", \"redirectUrl\": \"" + request.getContextPath() + "/OrderDetail?OrderId=" + order.getOrderId() + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"status\": \"error\", \"message\": \"Đã xảy ra lỗi!\"}");
        }
    }



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
