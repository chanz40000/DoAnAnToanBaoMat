package controller.user.checkout;

import com.fasterxml.jackson.databind.ObjectMapper;
import database.*;
import model.*;
import util.ConfigUtil;
import util.Email;
import util.Hash;

import org.json.JSONArray;

import org.json.JSONObject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@WebServlet(name = "CheckoutController", value = "/CheckoutController")
public class CheckoutController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Do nothing for GET requests
    }

    @Override


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // Nhận thông tin từ client
        String fullNameTinh = request.getParameter("tinh");
        String fullNameQuan = request.getParameter("quan");
        String fullNamePhuong = request.getParameter("phuong");
        String address = request.getParameter("address");
        String fullAddress = fullNameTinh + ", " + fullNameQuan + ", " + fullNamePhuong + ", " + address;
        String note = request.getParameter("note");
        String phone = request.getParameter("phone");
        String name = request.getParameter("nameConsignee");
        String paymentIdString = request.getParameter("payment");
        int paymentId = Integer.parseInt(paymentIdString);

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("userC");

        if (user == null) {
            String url = request.getContextPath() + "/WEB-INF/book/logintwo.jsp";
            RequestDispatcher dispatcher = request.getRequestDispatcher(url);
            dispatcher.forward(request, response);
            return;
        }

        Cart cart = (Cart) session.getAttribute("cart");
        int totalQuantity = cart.getCart_items().stream().mapToInt(CartItem::getQuantity).sum();
        int shippingCost = "Hồ Chí Minh".equals(fullNameTinh) ? 20000 * totalQuantity : 40000 * totalQuantity;

        double cartTotal = cart.calculateTotal();
        Double discountValue = (Double) session.getAttribute("discountValue");
        Integer discountType = (Integer) session.getAttribute("discountType");

        double discount = (discountValue != null) ? discountValue : 0.0;

        // Tính toán tổng số tiền sau khi áp dụng giảm giá
        double newTotal;
        if (discountType != null && discountType == 1) {
            // Giảm giá theo tỷ lệ phần trăm
            newTotal = cartTotal - (cartTotal * discount / 100);
        } else if (discountType != null && discountType == 2) {
            // Giảm giá theo giá trị cố định
            newTotal = cartTotal - discount;
        } else {
            // Không có giảm giá
            newTotal = cartTotal;
        }

        double allTotal = newTotal + shippingCost;

        // Tạo đối tượng Order
        OrderDAO orderDAO = new OrderDAO();
        PaymentDAO paymentDAO = new PaymentDAO();
        Payment payment = paymentDAO.selectById(paymentId);
        StatusOrder statusOrder = new StatusOrder(11);

        // Sử dụng LocalDateTime và DateTimeFormatter để có timestamp với giây
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = now.format(formatter);
        Timestamp bookingTimestamp = Timestamp.valueOf(formattedDate);

        Order order = new Order(orderDAO.creatId() + 1, user, allTotal, name, phone, fullAddress, payment, bookingTimestamp, note, shippingCost, statusOrder);
        order.setNameConsignee(name);
        order.setUser(user);
        order.setPhone(phone);
        order.setAddress(fullAddress);
        order.setNote(note);
        order.setTotalPrice(allTotal);
        order.setStatus(statusOrder);

        // Lưu đơn hàng vào cơ sở dữ liệu
        session.setAttribute("orderBooking", order);
        int resultOrder = orderDAO.insert(order);

        if (resultOrder > 0) {
            OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
            ProductDAO productDAO = new ProductDAO();
            int overallResult = 1;

            for (CartItem cartItem : cart.getCart_items()) {
                Product product = cartItem.getProduct();
                Integer selectedCouponId = (Integer) session.getAttribute("selectedCouponId");
                String appliedCouponCode = (String) session.getAttribute("appliedCouponCode");
                CouponDAO couponDAO = new CouponDAO();

                if (discount != 0) {
                    if (selectedCouponId != null) {
                        Coupon coupon = couponDAO.selectById(selectedCouponId);
                        couponDAO.updateQuantiyCouponById(selectedCouponId, coupon.getMaxQuantityUseOfUser() - 1, coupon.getMaxUseOfCoupon() - 1);
                    } else if (appliedCouponCode != null) {
                        Coupon coupon = couponDAO.selectByCode(appliedCouponCode);
                        int id = coupon.getCouponId();
                        couponDAO.updateQuantiyCouponById(id, coupon.getMaxQuantityUseOfUser() - 1, coupon.getMaxUseOfCoupon() - 1);
                    }
                }

                int quantityOrdered = cartItem.getQuantity();
                int remainingQuantity = product.getQuantity() - quantityOrdered;
                int resultUpQuantity = productDAO.updateQuantityOrder(product.getProductId(), remainingQuantity);
                if (resultUpQuantity > 0) {
                    double price = cartItem.getPrice();
                    double totalPrice = quantityOrdered * price;
                    OrderDetail orderDetail = new OrderDetail(orderDetailDAO.creatId() + 1, order, product, quantityOrdered, totalPrice);
                    int resultOrderDetail = orderDetailDAO.insert(orderDetail);

                    List<OrderDetail> orderDetailList = (List<OrderDetail>) session.getAttribute("orderDetails");
                    if (orderDetailList == null) {
                        orderDetailList = new ArrayList<>();
                    }
                    orderDetailList.add(orderDetail);
                    session.setAttribute("orderDetails", orderDetailList);

                    if (resultOrderDetail <= 0) {
                        overallResult = resultOrderDetail;
                        break;
                    }
                } else {
                    overallResult = resultUpQuantity;
                    break;
                }
            }

            if (overallResult > 0) {
                // Generate the hash after all order and order details are saved
                CompletableFuture.runAsync(() -> {
                try {
                // Serialize dữ liệu để tạo chuỗi hash
                StringBuilder serializedData = new StringBuilder();
                Order orderInDatabase = orderDAO.selectById(order.getOrderId());
                serializedData.append("user_id:").append(user.getUserId()).append(",");
                serializedData.append("email:").append(user.getEmail()).append(",");
                serializedData.append("order_id:").append(orderInDatabase.getOrderId()).append(",");
                serializedData.append("total_price:").append(orderInDatabase.getTotalPrice()).append(",");
                serializedData.append("booking_date:").append(orderInDatabase.getBookingDate()).append(",");
                serializedData.append("shipping_fee:").append(orderInDatabase.getShippingFee()).append(",");

                for (OrderDetail cartItem : orderDetailDAO.selectByOrderId(orderInDatabase.getOrderId())) {
                    serializedData.append("product_id:").append(cartItem.getProduct().getProductId()).append(",");
                    serializedData.append("product_name:").append(cartItem.getProduct().getProduct_name()).append(",");
                    serializedData.append("price:").append(cartItem.getPrice()).append(",");
                    serializedData.append("quantity:").append(cartItem.getQuantity()).append(",");
                }
                    String hash = Hash.calculateHash(serializedData.toString().getBytes(StandardCharsets.UTF_8));

                    System.out.println("Serialized Data (Checkout): " + serializedData.toString());
                    System.out.println("Hash (Checkout): " + hash + " cua don hang: "+order.getOrderId());

                    OrderSignatureDAO orderSignatureDAO = new OrderSignatureDAO();
                    StatusOrder statusOrder1 = new StatusOrder(11);
                    OrderSignature orderSignature = new OrderSignature(order, hash, statusOrder1);
                    int addHash = orderSignatureDAO.insert(orderSignature);

                    if (addHash > 0) {
                        String emailSubject = "Mã xác thực của đơn hàng MDH" +orderInDatabase.getOrderId()+ " bạn cần ký tên";
                        String emailBody = "<!DOCTYPE html>" +
                                "<html>" +
                                "<head>" +
                                "<meta charset='UTF-8'>" +
                                "<title>Xác thực chữ ký</title>" +
                                "</head>" +
                                "<body>" +
                                "<h1>Xin chào " + user.getName() + "</h1>" +
                                "<h1>Mã đơn hàng của bạn là: " + "MDH" + order.getOrderId() + "</h1>" +
                                "<p>Chúng tôi gửi bạn mã bạn cần để ký tên xác nhận đơn hàng!</p>" +
                                "<h2>Mã của bạn là: <strong>" + hash + "</strong></h2>" +
                                "<p>Vui lòng sử dụng mã này để ký tên và gửi chữ ký cho chúng tôi xác nhận đơn hàng của bạn.</p>" +
                                "<p>Sau 24h nếu bạn không gửi chữ ký, đơn hàng sẽ tự động bị hủy.</p>" +
                                "<p>Trân trọng,</p>" +
                                "<p>Cửa hàng của chúng tôi</p>" +
                                "</body>" +
                                "</html>";

                        Email.sendEmail(order.getUser().getEmail(), emailBody, emailSubject);
                    }


                } catch (NoSuchAlgorithmException | SQLException e) {
                        throw new RuntimeException(e);
                    }
                });

                // Xóa giỏ hàng sau khi đặt hàng thành công
                cart.clearCart();
                if (paymentId == 1) {
                    request.getRequestDispatcher("/WEB-INF/book/Vnpay.jsp").forward(request, response);
                } else {
                    session.removeAttribute("appliedCouponCode");
                    session.removeAttribute("discountValue");
                    session.removeAttribute("discountType");
                    session.removeAttribute("discount");
                    session.removeAttribute("newTotal");
                    response.sendRedirect(request.getContextPath() + "/verify-order?OrderIdVerify=" + order.getOrderId());
                    return;
                }
            }
        }
        // Trả về kết quả

//        JsonObject jsonResponse = new JsonObject();

//        jsonResponse.addProperty("success", true);

//        jsonResponse.addProperty("newTotal", newTotal);

//        jsonResponse.addProperty("discount", discount);

        // Tạo ObjectMapper để chuyển đối tượng Order thành JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String orderJson = objectMapper.writeValueAsString(order);
        request.setAttribute("orderJson", orderJson);
    }
}