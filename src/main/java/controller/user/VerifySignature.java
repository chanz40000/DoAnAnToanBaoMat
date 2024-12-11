package controller.user;

import database.KeyUserDAO;
import database.OrderDetailDAO;
import model.*;
import util.Hash;
import util.RSA;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@WebServlet(name = "VerifySignature", value = "/VerifySignature")
public class VerifySignature extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        
        String signature = request.getParameter("signature");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("userC");
        Order order = (Order) session.getAttribute("order");
        OrderDetailDAO orderDetailDAO = new OrderDetailDAO();


        if (user == null) {
            response.getWriter().write("Thông tin user không đầy đủ!");
            return;
        }
        if (order == null ) {
            response.getWriter().write("Thông tin order không đầy đủ!");
            return;
        }
        if (signature == null) {
            response.getWriter().write("Thông tin signature không đầy đủ!");
            return;
        }
        try {
        KeyUserDAO keyUserDAO = new KeyUserDAO();
        KeyUser userKey = keyUserDAO.selectById(user.getUserId());
        String publicKey = userKey.getKey();
            // Lấy danh sách chi tiết đơn hàng từ DAO
            List<OrderDetail> orderDetails = orderDetailDAO.selectByOrderId(order.getOrderId());
            if (orderDetails == null || orderDetails.isEmpty()) {
                response.getWriter().write("Không có chi tiết đơn hàng cho đơn hàng này.");
                return;
            }
            // Tạo danh sách chỉ chứa productId và quantity
            List<OrderVerify> orderDetailSummaries = new ArrayList<>();
            for (OrderDetail detail : orderDetails) {
                orderDetailSummaries.add(new OrderVerify(detail.getProduct().getProductId(), detail.getQuantity()));
            }

            // Sắp xếp danh sách OrderDetailSummary theo productId
            orderDetailSummaries.sort((p1, p2) -> Integer.compare(p1.getProductId(), p2.getProductId()));


            // Serialize danh sách đã sắp xếp
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
            objectStream.writeObject(orderDetailSummaries); // Serialize danh sách chỉ chứa productId và quantity
            objectStream.flush();
            byte[] serializedData = byteStream.toByteArray();

            String hash = Hash.calculateHash(serializedData);

            System.out.println("sign:"+signature);
            System.out.println("hash:"+hash);
            System.out.println("publicKey:"+publicKey);
            for (OrderVerify detail : orderDetailSummaries) {
                System.out.println("Product ID: " + detail.getProductId() + ", Quantity: " + detail.getQuantity());
            }
// Sau serialize
            System.out.println("Dữ liệu serialize (byte array):");
            System.out.println(java.util.Arrays.toString(serializedData));

            RSA rsa = new RSA();
        boolean verify = rsa.verifySignature(hash, signature, publicKey);
            if (verify == true) {
                response.getWriter().write("Chữ ký hợp lệ! Đơn hàng đã được xác nhận.");
            } else {
                response.getWriter().write("Chữ ký không hợp lệ!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("Lỗi khi xác minh chữ ký: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

}
