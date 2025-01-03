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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        ErrorBean eb = new ErrorBean();
        if (user == null || order == null || signature == null) {
            String errorMessage = "Thông tin không đầy đủ!";
            request.setAttribute("Error", errorMessage);
            eb.setError(errorMessage);
            request.setAttribute("errorBean", eb);

            // Forward lại trang verify-order.jsp cùng thông báo lỗi
            String url = "/WEB-INF/book/verify-order.jsp";
            RequestDispatcher dispatcher = request.getRequestDispatcher(url);
            dispatcher.forward(request, response);
            return;
        }
        // Kiểm tra trạng thái đơn hàng
        if (order.getStatus().getStatusId() != 11) {
            String errorMessage = "Trạng thái đơn hàng không hợp lệ để xác thực!";
            request.setAttribute("Error", errorMessage);
            eb.setError(errorMessage);
            request.setAttribute("errorBean", eb);

            // Forward lại trang verify-order.jsp cùng thông báo lỗi
            String url = "/WEB-INF/book/verify-order.jsp";
            RequestDispatcher dispatcher = request.getRequestDispatcher(url);
            dispatcher.forward(request, response);
            return;
        }

        try {

            // Kiểm tra trạng thái chữ ký của đơn hàng
            if (order.getStatusSignature().getStatusSignatureId() == 2) {
                String errorMessage = "Đơn hàng của bạn đã bị thay đổi! Không thể xác nhận đơn hàng.";
                request.setAttribute("Error", errorMessage);
                eb.setError(errorMessage);
                request.setAttribute("errorBean", eb);

                // Forward lại trang verify-order.jsp cùng thông báo lỗi
                String url = "/WEB-INF/book/verify-order.jsp";
                RequestDispatcher dispatcher = request.getRequestDispatcher(url);
                dispatcher.forward(request, response);
                return;
            }

            KeyUserDAO keyUserDAO = new KeyUserDAO();
            KeyUser userKey = keyUserDAO.selectByUserIdStatus(user.getUserId(), "ON");
            String publicKey = userKey.getKey().trim();

            OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
            List<OrderDetail> orderDetails = orderDetailDAO.selectByOrderId(order.getOrderId());
            OrderSignatureDAO orderSignatureDAO = new OrderSignatureDAO();
//            OrderSignature orderSignature = orderSignatureDAO.selectByOrderId(order.getOrderId());
//
            // Serialize dữ liệu để tạo chuỗi hash
            StringBuilder serializedData = new StringBuilder();
            serializedData.append("user_id:").append(user.getUserId()).append(",");
            serializedData.append("email:").append(user.getEmail()).append(",");
            serializedData.append("order_id:").append(order.getOrderId()).append(",");
            serializedData.append("total_price:").append(order.getTotalPrice()).append(",");
            serializedData.append("booking_date:").append(order.getBookingDate()).append(",");
            serializedData.append("shipping_fee:").append(order.getShippingFee()).append(",");

            for (OrderDetail cartItem : orderDetailDAO.selectByOrderId(order.getOrderId())) {
                serializedData.append("product_id:").append(cartItem.getProduct().getProductId()).append(",");
                serializedData.append("product_name:").append(cartItem.getProduct().getProduct_name()).append(",");
                serializedData.append("price:").append(cartItem.getPrice()).append(",");
                serializedData.append("quantity:").append(cartItem.getQuantity()).append(",");
            }

            // Tính hash từ dữ liệu serialize

            String hash = Hash.calculateHash(serializedData.toString().getBytes(StandardCharsets.UTF_8));
            System.out.println("Serialized Data (Verify): " + serializedData.toString());
            System.out.println("Hash (Verify): " + hash+ " cua don hang: "+order.getOrderId());
            System.out.println("Publickey: " + publicKey);
            System.out.println("Chu ky: "+signature);

            RSA rsa = new RSA();
//            boolean isValid = rsa.validateSignature(orderSignature.getHash(), signature, publicKey);
            boolean isValid = RSA.validateSignature(hash, publicKey, signature.trim());
            if (!isValid) {
                String errorMessage = "Chữ ký không hợp lệ!";
                request.setAttribute("Error", errorMessage); // Gán lỗi vào request attribute
                eb.setError(errorMessage);
                request.setAttribute("errorBean", eb);

                // Forward lại trang verify-order.jsp cùng thông báo lỗi
                String url = "/WEB-INF/book/verify-order.jsp";
                RequestDispatcher dispatcher = request.getRequestDispatcher(url);
                dispatcher.forward(request, response);
                return;
            }

            // Cập nhật trạng thái đơn hàng
            OrderDAO orderDAO = new OrderDAO();
            StatusSignature statusSignature = new StatusSignature(3);
            boolean isVerify = true;
            // Sử dụng LocalDateTime và DateTimeFormatter để có timestamp với giây
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDate = now.format(formatter);
            Timestamp signTimestamp = Timestamp.valueOf(formattedDate);
            OrderSignature orderSignature = orderSignatureDAO.selectByOrderId(order.getOrderId());
            int addHash = orderSignatureDAO.updateByOrderIdKey(order.getOrderId(), signature, userKey.getId());
            if (addHash > 0) {
                if (order.getPayment().getPaymentId() == 1) {
                    StatusOrder statusOrder = new StatusOrder(9);
                    orderDAO.updateStatusOrder(order.getOrderId(), statusOrder);
                    orderDAO.updateStatusSignatureOrder(order.getOrderId(), statusSignature);

                   String url = request.getContextPath() + "/OrderDetail?OrderId=" + order.getOrderId();

                    RequestDispatcher dispatcher = request.getRequestDispatcher(url);
                    dispatcher.forward(request, response);

                    return;
                } else {

                    StatusOrder statusOrder = new StatusOrder(1);
                    orderDAO.updateStatusOrder(order.getOrderId(), statusOrder);
                    orderDAO.updateStatusSignatureOrder(order.getOrderId(), statusSignature);
                   String redirectUrl = request.getContextPath() + "/OrderDetail?OrderId=" + order.getOrderId();
                    response.sendRedirect(redirectUrl);
                }
            }
            eb.setError((String) request.getAttribute("Error"));
            request.setAttribute("errorBean", eb);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
