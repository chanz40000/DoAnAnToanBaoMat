package controller.user;

import database.OrderDAO;
import database.OrderDetailDAO;
import database.OrderSignatureDAO;
import model.Order;
import model.OrderDetail;
import model.OrderSignature;
import model.User;
import util.Email;
import util.Hash;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

@WebServlet(name = "SendEmailHash", value = "/SendEmailHash")
public class SendEmailHash extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("userC");
        Order order = (Order) session.getAttribute("order");
        OrderDAO orderDAO = new OrderDAO();
        OrderDetailDAO orderDetailDAO = new OrderDetailDAO();

        // Nếu người dùng chưa đăng nhập, chuyển đến trang đăng nhập
        if (user == null) {
            String url = request.getContextPath() + "/WEB-INF/book/logintwo.jsp";
            RequestDispatcher dispatcher = request.getRequestDispatcher(url);
            dispatcher.forward(request, response);
            return;
        }

        //            // Serialize dữ liệu để tạo chuỗi hash
//            StringBuilder serializedData = new StringBuilder();
//            Order orderInDatabase = orderDAO.selectById(order.getOrderId());
//            serializedData.append("user_id:").append(user.getUserId()).append(",");
//            serializedData.append("email:").append(user.getEmail()).append(",");
//            serializedData.append("order_id:").append(orderInDatabase.getOrderId()).append(",");
//            serializedData.append("total_price:").append(orderInDatabase.getTotalPrice()).append(",");
//            serializedData.append("booking_date:").append(orderInDatabase.getBookingDate()).append(",");
//            serializedData.append("shipping_fee:").append(orderInDatabase.getShippingFee()).append(",");
//
//            for (OrderDetail cartItem : orderDetailDAO.selectByOrderId(orderInDatabase.getOrderId())) {
//                serializedData.append("product_id:").append(cartItem.getProduct().getProductId()).append(",");
//                serializedData.append("product_name:").append(cartItem.getProduct().getProduct_name()).append(",");
//                serializedData.append("price:").append(cartItem.getPrice()).append(",");
//                serializedData.append("quantity:").append(cartItem.getQuantity()).append(",");
//            }
        OrderSignatureDAO orderSignatureDAO = new OrderSignatureDAO();
        OrderSignature orderSignature = orderSignatureDAO.selectByOrderId(order.getOrderId());
        String hash = orderSignature.getHashOrder();

        // Trả lời ngay lập tức rằng hash đã được gửi thành công
        response.getWriter().write("Hash sent successfully.");

        // Tạo một luồng mới để gửi email trong nền
        new Thread(() -> {
            try {
                // Gửi email trong nền
                Email.sendEmailHashOrderToUser(user.getName(), hash, order);
            } catch (Exception e) {
                e.printStackTrace(); // Ghi log nếu gửi email gặp lỗi
            }
        }).start();

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Không cần xử lý gì trong phương thức POST
    }
}
