package database;

import model.*;
import util.Hash;
import util.RSA;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

public class OrderDAO extends AbsDAO<Order>{
    private ArrayList<Order> data = new ArrayList<>();

    public OrderDAO(HttpServletRequest request) {
        super(request);
    }

    public OrderDAO() {

    }

    public int creatId() {
        data = selectAll();
        return data.size();
    }
    @Override
    public ArrayList<Order> selectAll() {
        ArrayList<Order> orders = new ArrayList<>();
        try {
            // tao mot connection
            Connection con = JDBCUtil.getConnection();

            // tao cau lenh sql
            String sql = "SELECT * FROM orders";

            PreparedStatement st = con.prepareStatement(sql);

            // thuc thi

            ResultSet rs = st.executeQuery();

            while (rs.next()) {

                int idImport = rs.getInt("order_id");
                int idUser = rs.getInt("user_id");
                double totalPrice = rs.getDouble("total_price");
                String userConsignee = rs.getString("nameConsignee");
                String phone = rs.getString("phone");
                String address = rs.getString("address");
                int idPayment = rs.getInt("payment_id");
                Timestamp time = rs.getTimestamp("booking_date");
                String note = rs.getString("note");
                double shippingFee = rs.getDouble("shipping_fee");
                int status = rs.getInt("status_id");
                int statusSignature = rs.getInt("signature_status_id");

                // Áp dụng phương thức kiểm tra và cập nhật trạng thái chữ ký
                statusSignature = checkAndUpdateSignatureStatus(con, idImport, statusSignature);
                statusSignature = checkChangeDatabase(con, idUser, idImport, statusSignature);
                if (status == 3 && statusSignature == 2) {
                    updateOrderStatus(con, idImport, 13);
                }
                updateOrdersBasedOnKeyStatus();
                User u = new UserDAO().selectById(idUser);
                Payment pay = new PaymentDAO().selectById(idPayment);
                StatusOrder statusOrder = new StatusOrderDAO().selectById(status);
                StatusSignature statusSignature1 = new StatusSignatureDAO().selectById(statusSignature);
                Order order = new Order(idImport,u,totalPrice,userConsignee,phone,address,pay,time,note,shippingFee,statusOrder, statusSignature1);


                orders.add(order);

            }

            JDBCUtil.closeConnection(con);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return orders;

    }
    public List<Order> selectOrderByStatus(int status){
        List<Order> orders = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM orders WHERE status_id = ?";
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, status);
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                int idImport = rs.getInt("order_id");
                int idUser = rs.getInt("user_id");
                double totalPrice = rs.getDouble("total_price");
                String userConsignee = rs.getString("nameConsignee");
                String phone = rs.getString("phone");
                String address = rs.getString("address");
                int idPayment = rs.getInt("payment_id");
                Timestamp time = rs.getTimestamp("booking_date");
                String note = rs.getString("note");
                double shippingFee = rs.getDouble("shipping_fee");
                int statusId = rs.getInt("status_id");
                int statusSignature = rs.getInt("signature_status_id");

                // Áp dụng phương thức kiểm tra và cập nhật trạng thái chữ ký
                statusSignature = checkAndUpdateSignatureStatus(con, idImport, statusSignature);
                statusSignature = checkChangeDatabase(con, idUser, idImport, statusSignature);
                if (status == 3 && statusSignature == 2) {
                    updateOrderStatus(con, idImport, 13);
                }

                updateOrdersBasedOnKeyStatus();
                User u = new UserDAO().selectById(idUser);
                Payment pay = new PaymentDAO().selectById(idPayment);
                StatusOrder statusOrder = new StatusOrderDAO().selectById(status);
                StatusSignature statusSignature1 = new StatusSignatureDAO().selectById(statusSignature);
                Order order = new Order(idImport,u,totalPrice,userConsignee,phone,address,pay,time,note,shippingFee,statusOrder, statusSignature1);
                orders.add(order);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return orders;
    }

    /**
     * Cập nhật trạng thái các đơn hàng đã quá 24 giờ và chưa xác nhận chữ ký (status_id = 9) thành "hủy đơn" (status_id = 6)
     */
    public int cancelExpiredOrders() {
        int rowsUpdated = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "UPDATE orders " +
                    "SET status_id = 6 " +
                    "WHERE status_id = 11 " +
                    "AND TIMESTAMPDIFF(HOUR, booking_date, NOW()) >= 24";
//                    "AND TIMESTAMPDIFF(MINUTE, booking_date, NOW()) >= 1";
            PreparedStatement st = con.prepareStatement(sql);
            rowsUpdated = st.executeUpdate();
            JDBCUtil.closeConnection(con);


            System.out.println("Da cap nhat " + rowsUpdated + " don hang sang trang thai 'huy don'. do het han xac minh");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsUpdated;
    }

    public int cancelExpiredOrderPayment() {
        int rowsUpdated = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "UPDATE orders " +
                    "SET status_id = 6 " +
                    "WHERE status_id = 9 " +
                    "AND TIMESTAMPDIFF(HOUR, booking_date, NOW()) >= 24";
//                    "AND TIMESTAMPDIFF(MINUTE, booking_date, NOW()) >= 1";
            PreparedStatement st = con.prepareStatement(sql);
            rowsUpdated = st.executeUpdate();
            JDBCUtil.closeConnection(con);


            System.out.println("Da cap nhat " + rowsUpdated + " don hang sang trang thai 'huy don'. do het han thanh toan");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsUpdated;
    }
    public List<Order> getExpiredOrders() {
        List<Order> orders = new ArrayList<>();
        try (Connection con = JDBCUtil.getConnection();
             PreparedStatement st = con.prepareStatement(
                     "SELECT * FROM orders WHERE status_id = 6 AND TIMESTAMPDIFF(HOUR, booking_date, NOW()) >= 24"
             );
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("order_id"));
                order.getUser().setUserId(rs.getInt("user_id"));
                order.setTotalPrice(rs.getDouble("total_price"));
                order.setBookingDate(rs.getTimestamp("booking_date"));
                order.setStatus(new StatusOrder(6, "Hủy đơn"));
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public void updateStatusOrder(int orderId, StatusOrder status){
        int result = 0;
        try (Connection con = JDBCUtil.getConnection();
             PreparedStatement st = con.prepareStatement("UPDATE orders SET status_id = ? WHERE order_id = ?")) {

            st.setInt(1, status.getStatusId());
            st.setInt(2, orderId);
            result = st.executeUpdate();

            OrderDAO orderDAO = new OrderDAO();
            Order order = orderDAO.selectById(orderId);
            this.setPreValue(this.gson.toJson(order));
            order.setStatus(status);
            this.setValue("Don hang co ma: "+order.getOrderId()+" da duoc thay doi trang thai don hang thanh: " + status.getStatusName());
            int x = super.update(order);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void updateStatusSignatureOrder(int orderId, StatusSignature status){
        int result = 0;
        try (Connection con = JDBCUtil.getConnection();
             PreparedStatement st = con.prepareStatement("UPDATE orders SET signature_status_id = ? WHERE order_id = ?")) {

            st.setInt(1, status.getStatusSignatureId());
            st.setInt(2, orderId);
            result = st.executeUpdate();

            OrderDAO orderDAO = new OrderDAO();
            Order order = orderDAO.selectById(orderId);
            this.setPreValue(this.gson.toJson(order));
            order.setStatusSignature(status);
            this.setValue("Don hang co ma: "+order.getOrderId()+" da duoc thay doi trang thai xac minh thanh: " + status.getStatusSignatureName());
            int x = super.update(order);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public Order selectById(int id) {
        Order result = null;

        try {

            Connection con = JDBCUtil.getConnection();

            String sql = "SELECT * FROM orders WHERE order_id =?";

            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int idImport = rs.getInt("order_id");
                int idUser = rs.getInt("user_id");
                double totalPrice = rs.getDouble("total_price");
                String userConsignee = rs.getString("nameConsignee");
                String phone = rs.getString("phone");
                String address = rs.getString("address");
                int idPayment = rs.getInt("payment_id");
                Timestamp time = rs.getTimestamp("booking_date");
                String note = rs.getString("note");
                double shippingFee = rs.getDouble("shipping_fee");
                int status = rs.getInt("status_id");
                int statusSignature = rs.getInt("signature_status_id");

                // Áp dụng phương thức kiểm tra và cập nhật trạng thái chữ ký
                statusSignature = checkAndUpdateSignatureStatus(con, idImport, statusSignature);
                statusSignature = checkChangeDatabase(con, idUser, idImport, statusSignature);
                if (status == 3 && statusSignature == 2) {
                    updateOrderStatus(con, idImport, 13);
                }
                updateOrdersBasedOnKeyStatus();
                User u = new UserDAO().selectById(idUser);
                Payment pay = new PaymentDAO().selectById(idPayment);
                StatusOrder statusOrder = new StatusOrderDAO().selectById(status);
                StatusSignature statusSignature1 = new StatusSignatureDAO().selectById(statusSignature);
                result=  new Order(idImport,u,totalPrice,userConsignee,phone,address,pay,time,note,shippingFee,statusOrder, statusSignature1);



            }

            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;

    }
    public List<Order> selectByStatusIds(int... statusIds) {
        List<Order> orders = new ArrayList<>();
        Connection con = null;

        try {
            con = JDBCUtil.getConnection();

            // Tạo câu truy vấn SQL động với số lượng điều kiện status_id tùy biến
            StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM orders WHERE status_id IN (");
            for (int i = 0; i < statusIds.length; i++) {
                sqlBuilder.append("?");
                if (i < statusIds.length - 1) {
                    sqlBuilder.append(", ");
                }
            }
            sqlBuilder.append(")");

            PreparedStatement st = con.prepareStatement(sqlBuilder.toString());

            // Đặt giá trị cho các tham số status_id
            for (int i = 0; i < statusIds.length; i++) {
                st.setInt(i + 1, statusIds[i]);
            }

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int idImport = rs.getInt("order_id");
                int idUser = rs.getInt("user_id");
                double totalPrice = rs.getDouble("total_price");
                String userConsignee = rs.getString("nameConsignee");
                String phone = rs.getString("phone");
                String address = rs.getString("address");
                int idPayment = rs.getInt("payment_id");
                Timestamp time = rs.getTimestamp("booking_date");
                String note = rs.getString("note");
                double shippingFee = rs.getDouble("shipping_fee");
                int status = rs.getInt("status_id");
                int statusSignature = rs.getInt("signature_status_id");

                // Áp dụng phương thức kiểm tra và cập nhật trạng thái chữ ký
                statusSignature = checkAndUpdateSignatureStatus(con, idImport, statusSignature);
                statusSignature = checkChangeDatabase(con, idUser, idImport, statusSignature);
                if (status == 3 && statusSignature == 2) {
                    updateOrderStatus(con, idImport, 13);
                }
                updateOrdersBasedOnKeyStatus();
                User u = new UserDAO().selectById(idUser);
                Payment pay = new PaymentDAO().selectById(idPayment);
                StatusOrder statusOrder = new StatusOrderDAO().selectById(status);
                StatusSignature statusSignature1 = new StatusSignatureDAO().selectById(statusSignature);
                Order order = new Order(idImport,u,totalPrice,userConsignee,phone,address,pay,time,note,shippingFee,statusOrder, statusSignature1);
                orders.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeConnection(con);
        }

        return orders;
    }

    public List<Order> selectByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM orders WHERE user_id = ?";
            st = con.prepareStatement(sql);
            st.setInt(1, userId);
            rs = st.executeQuery();

            while (rs.next()) {
                int idImport = rs.getInt("order_id");
                int idUser = rs.getInt("user_id");
                double totalPrice = rs.getDouble("total_price");
                String userConsignee = rs.getString("nameConsignee");
                String phone = rs.getString("phone");
                String address = rs.getString("address");
                int idPayment = rs.getInt("payment_id");
                Timestamp time = rs.getTimestamp("booking_date");
                String note = rs.getString("note");
                double shippingFee = rs.getDouble("shipping_fee");
                int status = rs.getInt("status_id");
                int statusSignature = rs.getInt("signature_status_id");

                // Áp dụng phương thức kiểm tra và cập nhật trạng thái chữ ký
                statusSignature = checkAndUpdateSignatureStatus(con, idImport, statusSignature);
                statusSignature = checkChangeDatabase(con, userId, idImport, statusSignature);
                if (status == 3 && statusSignature ==2) {
                    updateOrderStatus(con, idImport, 13);
                }
                updateOrdersBasedOnKeyStatus();
                User u = new UserDAO().selectById(idUser);
                Payment pay = new PaymentDAO().selectById(idPayment);
                StatusOrder statusOrder = new StatusOrderDAO().selectById(status);
                StatusSignature statusSignature1 = new StatusSignatureDAO().selectById(statusSignature);
                Order order = new Order(idImport,u,totalPrice,userConsignee,phone,address,pay,time,note,shippingFee,statusOrder, statusSignature1);
                orders.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (st != null) st.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (con != null) con.close(); } catch (Exception e) { e.printStackTrace(); }
        }

        return orders;
    }

    public List<Order> selectByUserIdAndStatusId(int userId, int statusId) {
        List<Order> orders = new ArrayList<>();

        try {

            Connection con = JDBCUtil.getConnection();

            String sql = "SELECT * FROM orders WHERE user_id = ? AND status_id = ?";

            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, userId);
            st.setInt(2, statusId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int idImport = rs.getInt("order_id");
                int idUser = rs.getInt("user_id");
                double totalPrice = rs.getDouble("total_price");
                String userConsignee = rs.getString("nameConsignee");
                String phone = rs.getString("phone");
                String address = rs.getString("address");
                int idPayment = rs.getInt("payment_id");
                Timestamp time = rs.getTimestamp("booking_date");
                String note = rs.getString("note");
                double shippingFee = rs.getDouble("shipping_fee");
                int status = rs.getInt("status_id");
                int statusSignature = rs.getInt("signature_status_id");

                // Áp dụng phương thức kiểm tra và cập nhật trạng thái chữ ký
                statusSignature = checkAndUpdateSignatureStatus(con, idImport, statusSignature);
                statusSignature = checkChangeDatabase(con, userId, idImport, statusSignature);
                if (status == 3 && statusSignature == 2) {
                    updateOrderStatus(con, idImport, 13);
                }
                updateOrdersBasedOnKeyStatus();
                User u = new UserDAO().selectById(idUser);
                Payment pay = new PaymentDAO().selectById(idPayment);
                StatusOrder statusOrder = new StatusOrderDAO().selectById(status);
                StatusSignature statusSignature1 = new StatusSignatureDAO().selectById(statusSignature);
                Order order = new Order(idImport,u,totalPrice,userConsignee,phone,address,pay,time,note,shippingFee,statusOrder, statusSignature1);
                orders.add(order);
            }

            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return orders;

    }
    public List<Order> selectByUserIdAndStatusSignatureId(int userId, int statusSignatureId) {
        List<Order> orders = new ArrayList<>();

        try {

            Connection con = JDBCUtil.getConnection();

            String sql = "SELECT * FROM orders WHERE user_id = ? AND signature_status_id = ?";

            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, userId);
            st.setInt(2, statusSignatureId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int idImport = rs.getInt("order_id");
                int idUser = rs.getInt("user_id");
                double totalPrice = rs.getDouble("total_price");
                String userConsignee = rs.getString("nameConsignee");
                String phone = rs.getString("phone");
                String address = rs.getString("address");
                int idPayment = rs.getInt("payment_id");
                Timestamp time = rs.getTimestamp("booking_date");
                String note = rs.getString("note");
                double shippingFee = rs.getDouble("shipping_fee");
                int status = rs.getInt("status_id");
                int statusSignature = rs.getInt("signature_status_id");

                // Áp dụng phương thức kiểm tra và cập nhật trạng thái chữ ký
                statusSignature = checkAndUpdateSignatureStatus(con, idImport, statusSignature);
                statusSignature = checkChangeDatabase(con, userId, idImport, statusSignature);
                if (status == 3 && statusSignature == 2) {
                    updateOrderStatus(con, idImport, 13);
                }
                updateOrdersBasedOnKeyStatus();
                User u = new UserDAO().selectById(idUser);
                Payment pay = new PaymentDAO().selectById(idPayment);
                StatusOrder statusOrder = new StatusOrderDAO().selectById(status);
                StatusSignature statusSignature1 = new StatusSignatureDAO().selectById(statusSignature);
                Order order = new Order(idImport,u,totalPrice,userConsignee,phone,address,pay,time,note,shippingFee,statusOrder, statusSignature1);
                orders.add(order);
            }

            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return orders;

    }
    public List<Order> selectByUserIdAndStatusIds(int userId, int... statusIds) {
        List<Order> orders = new ArrayList<>();

        try {
            Connection con = JDBCUtil.getConnection();

            // Tạo câu truy vấn SQL động với số lượng điều kiện status_id tùy biến
            StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM orders WHERE user_id = ? AND status_id IN (");
            for (int i = 0; i < statusIds.length; i++) {
                sqlBuilder.append("?");
                if (i < statusIds.length - 1) {
                    sqlBuilder.append(", ");
                }
            }
            sqlBuilder.append(")");

            PreparedStatement st = con.prepareStatement(sqlBuilder.toString());
            st.setInt(1, userId);

            // Đặt giá trị cho các tham số status_id
            for (int i = 0; i < statusIds.length; i++) {
                st.setInt(i + 2, statusIds[i]);
            }

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int idImport = rs.getInt("order_id");
                int idUser = rs.getInt("user_id");
                double totalPrice = rs.getDouble("total_price");
                String userConsignee = rs.getString("nameConsignee");
                String phone = rs.getString("phone");
                String address = rs.getString("address");
                int idPayment = rs.getInt("payment_id");
                Timestamp time = rs.getTimestamp("booking_date");
                String note = rs.getString("note");
                double shippingFee = rs.getDouble("shipping_fee");
                int status = rs.getInt("status_id");
                int statusSignature = rs.getInt("signature_status_id");

                // Áp dụng phương thức kiểm tra và cập nhật trạng thái chữ ký
                statusSignature = checkAndUpdateSignatureStatus(con, idImport, status);
                statusSignature = checkChangeDatabase(con, userId, idImport, status);
                if (status == 3 && statusSignature == 2) {
                    updateOrderStatus(con, idImport, 13);
                }
                updateOrdersBasedOnKeyStatus();
                User u = new UserDAO().selectById(idUser);
                Payment pay = new PaymentDAO().selectById(idPayment);
                StatusOrder statusOrder = new StatusOrderDAO().selectById(status);
                StatusSignature statusSignature1 = new StatusSignatureDAO().selectById(statusSignature);
                Order order = new Order(idImport,u,totalPrice,userConsignee,phone,address,pay,time,note,shippingFee,statusOrder, statusSignature1);
                orders.add(order);
            }

            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return orders;
    }
    public void updateOrdersBasedOnKeyStatus() {
        try {
            Connection con = JDBCUtil.getConnection();

            // Lấy thời gian hết hạn của key mới nhất có trạng thái "OFF"
            String sqlOff = "SELECT expired_at FROM key_user WHERE status = 'OFF' ORDER BY expired_at DESC LIMIT 1";
            PreparedStatement stOff = con.prepareStatement(sqlOff);
            ResultSet rsOff = stOff.executeQuery();
            Timestamp expiredAt = null;
            if (rsOff.next()) {
                expiredAt = rsOff.getTimestamp("expired_at");
            }

            // Lấy thời gian tạo của key có trạng thái "ON"
            String sqlOn = "SELECT create_at FROM key_user WHERE status = 'ON' ORDER BY create_at ASC LIMIT 1";
            PreparedStatement stOn = con.prepareStatement(sqlOn);
            ResultSet rsOn = stOn.executeQuery();
            Timestamp createAt = null;
            if (rsOn.next()) {
                createAt = rsOn.getTimestamp("create_at");
            }

            // Kiểm tra nếu cả hai thời điểm được tìm thấy
            if (expiredAt != null && createAt != null) {
                // Tìm các đơn hàng có ngày đặt trong khoảng thời gian từ expiredAt đến createAt
                String sqlOrders = "SELECT order_id FROM orders WHERE booking_date BETWEEN ? AND ?";
                PreparedStatement stOrders = con.prepareStatement(sqlOrders);
                stOrders.setTimestamp(1, expiredAt);
                stOrders.setTimestamp(2, createAt);
                ResultSet rsOrders = stOrders.executeQuery();

                // Cập nhật trạng thái của các đơn hàng tìm được thành `status6`
                String updateSql = "UPDATE orders SET status_id = 6 WHERE order_id = ?";
                PreparedStatement stUpdate = con.prepareStatement(updateSql);
                while (rsOrders.next()) {
                    int orderId = rsOrders.getInt("order_id");
                    stUpdate.setInt(1, orderId);
                    stUpdate.executeUpdate();
                }


            } else {
                System.out.println("Không tìm thấy thời gian hết hạn hoặc thời gian tạo khóa phù hợp.");
            }

            // Đóng kết nối
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int checkChangeDatabase(Connection con, int userId, int orderId, int currentStatus) throws SQLException {
        // Nếu trạng thái đơn hàng là 6, bỏ qua kiểm tra
        if (currentStatus == 6) {
            return currentStatus;
        }

        try {
            // Tính toán serialized_data
            String serializedData = getSerializedDataForOrder(orderId);
            String calculatedHash = Hash.calculateHash(serializedData.getBytes(StandardCharsets.UTF_8));

            // Truy vấn để lấy chữ ký và key_user_id từ order_signatures
            String sqlSignature = """
            SELECT os.signature, os.key_user_id, o.signature_status_id, os.hash_order
            FROM order_signatures os JOIN orders o
            WHERE os.order_id = ?
        """;

            try (PreparedStatement stSignature = con.prepareStatement(sqlSignature)) {
                stSignature.setInt(1, orderId);

                try (ResultSet rsSignature = stSignature.executeQuery()) {
                    if (rsSignature.next()) {
                        String signature = rsSignature.getString("signature");
                        int keyUserId = rsSignature.getInt("key_user_id");
                        int signatureStatusId = rsSignature.getInt("signature_status_id");
                        String hashOrder = rsSignature.getString("hash_order");


                        // Nếu không có chữ ký
                        // Nếu không có chữ ký
                        if (signature == null) {
                            // So sánh hash_order với hash đã tính
                            if (calculatedHash.equals(hashOrder)) {
                                // Hash khớp -> cập nhật trạng thái = 1
                                updateOrderStatusSignature(con, orderId, 1);
                                return 1;
                            } else {
                                // Hash không khớp -> cập nhật trạng thái = 2
                                updateOrderStatusSignature(con, orderId, 2);
                                return 2;
                            }
                        }

                        // Truy vấn để lấy public_key từ bảng key_user dựa vào key_user_id
                        String sqlPublicKey = """
                        SELECT ku.public_key
                        FROM key_user ku
                        WHERE ku.id = ?
                    """;

                        try (PreparedStatement stPublicKey = con.prepareStatement(sqlPublicKey)) {
                            stPublicKey.setInt(1, keyUserId);

                            try (ResultSet rsPublicKey = stPublicKey.executeQuery()) {
                                if (rsPublicKey.next()) {
                                    String publicKey = rsPublicKey.getString("public_key");

                                    // Nếu có chữ ký, giải mã hash từ chữ ký
                                    String decryptedHash = RSA.decryptHash(publicKey, signature);

                                    // So sánh hash đã tính và hash giải mã
                                    if (calculatedHash.equals(decryptedHash)) {
                                        updateOrderStatusSignature(con, orderId, 3);
                                        return 3;
                                    } else {
                                        updateOrderStatusSignature(con, orderId, 2);
                                        return 2;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            updateOrderStatusSignature(con, orderId, 2);
            return 2;
        }

        // Trả về trạng thái hiện tại nếu không có thay đổi
        return currentStatus;
    }

    // Hàm kiểm tra hash có đúng hay không
    private boolean isHashCorrect(String serializedData, String calculatedHash) throws NoSuchAlgorithmException {
        // Tính lại hash từ dữ liệu gốc
        String recalculatedHash = Hash.calculateHash(serializedData.getBytes(StandardCharsets.UTF_8));
        System.out.println("Hash tính lại: " + recalculatedHash);

        // So sánh với hash đã tính trước đó
        return recalculatedHash.equals(calculatedHash);
    }



    // Phương thức kiểm tra và cập nhật signature_status_id
    private int checkAndUpdateSignatureStatus(Connection con, int orderId, int currentStatusSignature) throws SQLException {
        // Xử lý khi trạng thái hiện tại là 3 (đã ký)
        if (currentStatusSignature == 3) {
            // Kiểm tra chữ ký có tồn tại không và trạng thái xác minh
            String checkSql = "SELECT signature, is_signature_verified FROM order_signatures WHERE order_id = ?";
            try (PreparedStatement checkSt = con.prepareStatement(checkSql)) {
                checkSt.setInt(1, orderId);
                try (ResultSet checkRs = checkSt.executeQuery()) {
                    boolean hasSignature = false; // Biến kiểm tra sự tồn tại của chữ ký
                    while (checkRs.next()) {
                        String signature = checkRs.getString("signature");
                        hasSignature = (signature != null); // Nếu chữ ký khác null thì đã có chữ ký
                        if (!checkRs.getBoolean("is_signature_verified")) { // Nếu chữ ký chưa được xác minh
                            updateOrderStatusSignature(con, orderId, 2); // Cập nhật trạng thái về 2
                            return 2;
                        }
                    }
                    // Nếu không có chữ ký nào -> cập nhật trạng thái về 2
                    if (!hasSignature) {
                        updateOrderStatusSignature(con, orderId, 2);
                        return 2;
                    }
                }
            }
        }
        // Xử lý khi trạng thái hiện tại là 1 (chưa ký)
        else if (currentStatusSignature == 1) {
            // Kiểm tra tất cả chữ ký đã được xác minh chưa
            String checkSql = "SELECT signature, is_signature_verified FROM order_signatures WHERE order_id = ?";
            try (PreparedStatement checkSt = con.prepareStatement(checkSql)) {
                checkSt.setInt(1, orderId);
                try (ResultSet checkRs = checkSt.executeQuery()) {
                    boolean hasSignature = false; // Kiểm tra có chữ ký nào hay không
                    boolean allSignaturesVerified = true; // Giả định tất cả chữ ký đã được xác minh

                    while (checkRs.next()) {
                        String signature = checkRs.getString("signature");
                        hasSignature = (signature != null);
                        if (signature != null && !checkRs.getBoolean("is_signature_verified")) { // Nếu có chữ ký chưa xác minh
                            allSignaturesVerified = false; // Ghi nhận là có chữ ký chưa xác minh
                            break; // Thoát vòng lặp ngay khi phát hiện
                        }
                    }

                    // Nếu có chữ ký và tất cả đã được xác minh -> cập nhật trạng thái
                    if (hasSignature && allSignaturesVerified) {
                        updateOrderStatusSignature(con, orderId, 2);
                        return 2;
                    }
                }
            }
        }
        // Trả về trạng thái hiện tại nếu không có thay đổi
        return currentStatusSignature;
    }


    public String getSerializedDataForOrder(int orderId) throws SQLException {
        StringBuilder serializedData = new StringBuilder();

        // Câu lệnh SQL để lấy dữ liệu từ các bảng, chỉ lấy các trường cần thiết
        String query = "SELECT " +
                "u.user_id, " +
                "u.email, " +
                "o.order_id, " +
                "o.total_price, " +
                "o.booking_date, " +
                "o.shipping_fee " +
                "FROM orders o " +
                "JOIN users u ON o.user_id = u.user_id " +
                "WHERE o.order_id = ?";  // Sử dụng orderId từ tham số

        try (Connection con = JDBCUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, orderId);  // Gán giá trị orderId vào câu lệnh SQL

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Thêm thông tin user vào serializedData
                    serializedData.append("user_id:").append(rs.getInt("user_id")).append(",");
                    serializedData.append("email:").append(rs.getString("email")).append(",");
                    serializedData.append("order_id:").append(rs.getInt("order_id")).append(",");
                    serializedData.append("total_price:").append(rs.getDouble("total_price")).append(",");

                    // Định dạng ngày giờ booking (bao gồm phần .0 sau giây)
                    java.sql.Timestamp timestamp = rs.getTimestamp("booking_date");
                    if (timestamp != null) {
                        // Định dạng ngày giờ với .0 sau giây (yyyy-MM-dd HH:mm:ss.SSS)
                        String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(timestamp);
                        serializedData.append("booking_date:").append(formattedDate).append(",");
                    } else {
                        serializedData.append("booking_date:null,");
                    }

                    // Thêm phí vận chuyển
                    serializedData.append("shipping_fee:").append(rs.getDouble("shipping_fee")).append(",");

                    // Lấy thông tin chi tiết đơn hàng từ bảng OrderDetails
                    String detailQuery = "SELECT p.product_id, p.product_name, od.price, od.quantity " +
                            "FROM orderdetails od " +
                            "JOIN products p ON od.product_id = p.product_id " +
                            "WHERE od.order_id = ?";

                    try (PreparedStatement psDetail = con.prepareStatement(detailQuery)) {
                        psDetail.setInt(1, orderId);  // Gán giá trị orderId vào câu lệnh SQL

                        try (ResultSet rsDetail = psDetail.executeQuery()) {
                            while (rsDetail.next()) {
                                serializedData.append("product_id:").append(rsDetail.getInt("product_id")).append(",");
                                serializedData.append("product_name:").append(rsDetail.getString("product_name")).append(",");
                                serializedData.append("price:").append(rsDetail.getDouble("price")).append(",");
                                serializedData.append("quantity:").append(rsDetail.getInt("quantity")).append(",");
                            }
                        }
                    }
                }
            }
        }

        // Xóa dấu phẩy cuối cùng để chuỗi không có dấu phẩy thừa

        return serializedData.toString();
    }


    // Hàm tiện ích để cập nhật trạng thái đơn hàng
    private void updateOrderStatusSignature(Connection con, int orderId, int newStatus) throws SQLException {
        String updateSql = "UPDATE orders SET signature_status_id = ? WHERE order_id = ?";
        try (PreparedStatement updateSt = con.prepareStatement(updateSql)) {
            updateSt.setInt(1, newStatus);
            updateSt.setInt(2, orderId);
            updateSt.executeUpdate();
        }
    }

    private void updateOrderStatus(Connection con, int orderId, int newStatus) throws SQLException {
        String updateSql = "UPDATE orders SET status_id = ? WHERE order_id = ?";
        try (PreparedStatement updateSt = con.prepareStatement(updateSql)) {
            updateSt.setInt(1, newStatus);
            updateSt.setInt(2, orderId);
            updateSt.executeUpdate();
        }
    }



    public List<Order> selectByUserIdAndStatusSignatureIds(int userId, int... statusSignatureIds) {
        List<Order> orders = new ArrayList<>();

        try {
            Connection con = JDBCUtil.getConnection();

            // Tạo câu truy vấn SQL động với số lượng điều kiện status_id tùy biến
            StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM orders WHERE user_id = ? AND signature_status_id IN (");
            for (int i = 0; i < statusSignatureIds.length; i++) {
                sqlBuilder.append("?");
                if (i < statusSignatureIds.length - 1) {
                    sqlBuilder.append(", ");
                }
            }
            sqlBuilder.append(")");

            PreparedStatement st = con.prepareStatement(sqlBuilder.toString());
            st.setInt(1, userId);

            // Đặt giá trị cho các tham số status_id
            for (int i = 0; i < statusSignatureIds.length; i++) {
                st.setInt(i + 2, statusSignatureIds[i]);
            }

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int idImport = rs.getInt("order_id");
                int idUser = rs.getInt("user_id");
                double totalPrice = rs.getDouble("total_price");
                String userConsignee = rs.getString("nameConsignee");
                String phone = rs.getString("phone");
                String address = rs.getString("address");
                int idPayment = rs.getInt("payment_id");
                Timestamp time = rs.getTimestamp("booking_date");
                String note = rs.getString("note");
                double shippingFee = rs.getDouble("shipping_fee");
                int status = rs.getInt("status_id");
                int statusSignature = rs.getInt("signature_status_id");

                // Áp dụng phương thức kiểm tra và cập nhật trạng thái chữ ký
                statusSignature = checkAndUpdateSignatureStatus(con, idImport, statusSignature);


                User u = new UserDAO().selectById(idUser);
                Payment pay = new PaymentDAO().selectById(idPayment);
                StatusOrder statusOrder = new StatusOrderDAO().selectById(status);
                StatusSignature statusSignature1 = new StatusSignatureDAO().selectById(statusSignature);
                Order order = new Order(idImport,u,totalPrice,userConsignee,phone,address,pay,time,note,shippingFee,statusOrder, statusSignature1);
                orders.add(order);
            }

            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return orders;
    }
    public int selectLatestOrderIdByUserAndPayment(int userId) {
        int orderId = -1;  // Default value to indicate no order found
        try {
            // Establish a connection to the database
            Connection con = JDBCUtil.getConnection();

            // SQL query to get the most recent order's order_id for a specific user with payment_id = 1
            String sql = "SELECT order_id FROM orders WHERE user_id = ? AND payment_id = 1 ORDER BY booking_date DESC LIMIT 1";

            // Prepare the statement
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, userId);  // Set the user_id parameter

            // Execute the query
            ResultSet rs = st.executeQuery();

            // If a record is found, retrieve the order_id
            if (rs.next()) {
                orderId = rs.getInt("order_id");
            }

            // Close the connection
            JDBCUtil.closeConnection(con);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderId;
    }
    @Override
    public int insert(Order order) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();

            String sql = "INSERT INTO orders(order_id, user_id,total_price,nameConsignee,phone,address,payment_id,booking_date,note,shipping_fee,status_id, signature_status_id)"
                    + "VALUES(?, ?, ?, ?,?,?,?,?,?,?,?,?)";

            PreparedStatement rs = con.prepareStatement(sql);

            rs.setInt(1, order.getOrderId());
            rs.setInt(2, order.getUser().getUserId());
            rs.setDouble(3, order.getTotalPrice());
            rs.setString(4, order.getNameConsignee());
            rs.setString(5, order.getPhone());
            rs.setString(6, order.getAddress());
            rs.setDouble(7, order.getPayment().getPaymentId());
            rs.setTimestamp(8, order.getBookingDate());
            rs.setString(9, order.getNote());
            rs.setDouble(10, order.getShippingFee());
            rs.setInt(11,order.getStatus().getStatusId());
            rs.setInt(12,order.getStatusSignature().getStatusSignatureId());



            result = rs.executeUpdate();

            JDBCUtil.closeConnection(con);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;

    }

    @Override
    public int insertAll(ArrayList<Order> list) {
        int result = 0;
        for (Order o : list) {

            ;
            if (this.insert(o) == 1)
                result += 1;
        }
        return result;
    }

    @Override
    public int delete(Order order) {
        int result = 0;

        try {
            Connection con = JDBCUtil.getConnection();

            String sql = "DELETE from orders " + "WHERE order_id=?";

            PreparedStatement rs = con.prepareStatement(sql);
            rs.setInt(1, order.getOrderId());

            result = rs.executeUpdate();
            this.setPreValue(this.gson.toJson(order));
            int x = super.delete(order);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public int deleteAll(ArrayList<Order> list) {
        int result = 0;

        for (Order o : list) {
            result += delete(o);
        }
        return result;
    }

    @Override
    public int update(Order order) {
        int result = 0;
        Order oldRating = this.selectById(order.getOrderId());

        if (oldRating != null) {

            try {
                Connection con = JDBCUtil.getConnection();

                String sql = "UPDATE orders SET user_id=? " +
                        ", total_price=? " +
                        ", nameConsignee=? " +
                        ", phone=? " +
                        ", address=? " +
                        ", payment_id=? " +
                        ", booking_date=? " +
                        ", note=? " +
                        ", shipping_fee=? " +
                        ", status_id=? " +
                        ",signature_status_id=? " +
                        "WHERE order_id = ?";

                PreparedStatement rs = con.prepareStatement(sql);

                rs.setInt(1, order.getOrderId());
                rs.setInt(2, order.getUser().getUserId());
                rs.setDouble(3, order.getTotalPrice());
                rs.setString(4, order.getNameConsignee());
                rs.setString(5, order.getPhone());
                rs.setString(6, order.getAddress());
                rs.setDouble(7, order.getPayment().getPaymentId());
                rs.setTimestamp(8, order.getBookingDate());
                rs.setString(9, order.getNote());
                rs.setDouble(10, order.getShippingFee());
                rs.setInt(11,order.getStatus().getStatusId());
                rs.setInt(12,order.getStatusSignature().getStatusSignatureId());




                result = rs.executeUpdate();
                this.setPreValue(this.gson.toJson(oldRating));
                this.setValue(this.gson.toJson(order));
                int x = super.insert(order);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }


        return result;
    }
    //tinh doanh thu theo tuan gan nhat
    //lay ra ngay hien tai roi suy  ra khoang ngay trong tuan do
    public Map<String, Double> revenue(Date date){
        Map<String, Double> result = new HashMap<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT a.user_id AS user_id,  SUM(quantity*price) AS tongtien\n" +
                    "FROM orders a JOIN orderdetails b ON a.order_id=b.order_id\n" +
                    "WHERE a.booking_date=?\n" +
                    "GROUP BY user_id";
            PreparedStatement pre = con.prepareStatement(sql);
            pre.setDate(1, date);
            ResultSet rs = pre.executeQuery();
            while (rs.next()){
                int id = rs.getInt("user_id");
                double tongtien = rs.getDouble("tongtien");
                UserDAO userDAO = new UserDAO();
                result.put(userDAO.selectById(id).getName(), tongtien);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
    //doanh thu tu ngay n1 den ngay n2
    public Map<Date, Double> revenue(Date date1, Date date2){
        Map<Date, Double>result = new HashMap<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "WITH RECURSIVE date_series AS (\n" +
                    "    SELECT DATE(?) AS ngay\n" +
                    "    UNION ALL\n" +
                    "    SELECT ngay + INTERVAL 1 DAY\n" +
                    "    FROM date_series\n" +
                    "    WHERE ngay + INTERVAL 1 DAY <= DATE(?)\n" +
                    ")\n" +
                    "SELECT ds.ngay,\n" +
                    "       COALESCE(SUM(b.quantity * b.price), 0) AS tongtien\n" +
                    "FROM date_series ds\n" +
                    "LEFT JOIN orders a ON ds.ngay = a.booking_date\n" +
                    "LEFT JOIN orderdetails b ON a.order_id = b.order_id\n" +
                    "GROUP BY ds.ngay\n" +
                    "ORDER BY ds.ngay;";
            PreparedStatement pre = con.prepareStatement(sql);
            pre.setDate(1, date1);
            pre.setDate(2, date2);
            ResultSet rs = pre.executeQuery();
            while (rs.next()){
                Date date = rs.getDate("ngay");
                double tongtien = rs.getDouble("tongtien");
                result.put(date, tongtien);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
    //doanh thu theo thang
    public double revenue(int month, int year){
        double result =0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT \n" +
                    "    SUM(b.quantity * b.price) AS tongtien\n" +
                    "FROM \n" +
                    "    orders a\n" +
                    "JOIN \n" +
                    "    orderdetails b ON a.order_id = b.order_id\n" +
                    "WHERE \n" +
                    "    YEAR(a.booking_date) = ? AND MONTH(a.booking_date) = ?;\n";
            PreparedStatement pre = con.prepareStatement(sql);
            pre.setInt(1, year);
            pre.setInt(2, month);
            ResultSet rs = pre.executeQuery();
            while (rs.next()){
                result = rs.getDouble("tongtien");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
    //doanh thu theo nam
    public double revenue( int year){
        double result =0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT \n" +
                    "    SUM(b.quantity * b.price) AS tongtien\n" +
                    "FROM \n" +
                    "    orders a\n" +
                    "JOIN \n" +
                    "    orderdetails b ON a.order_id = b.order_id\n" +
                    "WHERE \n" +
                    "    YEAR(a.booking_date) = ? ";
            PreparedStatement pre = con.prepareStatement(sql);
            pre.setInt(1, year);
            ResultSet rs = pre.executeQuery();
            while (rs.next()){
                result = rs.getDouble("tongtien");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    //doanh thu tung thang theo nam
    public double[]revenue2(int year){
        double[]result = new double[12];
        for(int i=1; i<13; i++){
            double rev = this.revenue(i, year);
            result[i-1]=rev;
        }
        return result;
    }

    public double[] revenueForWeek(Date date) {
        double[]result = new double[7];
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // Điều chỉnh để trở về ngày Chủ nhật (hoặc Thứ Hai nếu bạn muốn tuần bắt đầu từ Thứ Hai)
        calendar.set(Calendar.DAY_OF_WEEK, calendar.MONDAY);

        try (Connection con = JDBCUtil.getConnection()) {
            for (int i = 0; i < 7; i++) {
                Date currentDate = new Date(calendar.getTimeInMillis());
                String sql = "SELECT SUM(quantity * price) AS tongtien " +
                        "FROM orders a JOIN orderdetails b ON a.order_id = b.order_id " +
                        "WHERE DATE(a.booking_date) = ?";
                try (PreparedStatement pre = con.prepareStatement(sql)) {
                    pre.setDate(1, currentDate);
                    try (ResultSet rs = pre.executeQuery()) {
                        if (rs.next()) {
                            result[i]= rs.getDouble("tongtien");
                        } else {
                            result[i]=0;
                        }
                    }
                }
                calendar.add(Calendar.DATE, 1); // Move to the next day
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }
    public static void main(String[] args) throws SQLException, NoSuchAlgorithmException {
        OrderDAO orderDAO = new OrderDAO();
        Order order = orderDAO.selectById(3);
//        orderDAO.updateStatusOrder(3,new StatusOrder(13));
//        double[]rs=orderDAO.revenueForWeek(Date.valueOf(LocalDateTime.now().toLocalDate()));
//        for (int i=0; i<5; i++){
//            System.out.println(rs[i]);
//        }
        UserDAO userDAO = new UserDAO();
        User user = userDAO.selectById(2);
                String hash = Hash.calculateHash(orderDAO.getSerializedDataForOrder(3).toString().getBytes(StandardCharsets.UTF_8));
        System.out.println("Hash trong database: "+hash);
                // lay public key giai ma chu ky
        KeyUserDAO keyUserDAO = new KeyUserDAO();
        KeyUser keyUser = keyUserDAO.selectByUserIdStatus(user.getUserId(), "ON");
        OrderSignatureDAO orderSignatureDao = new OrderSignatureDAO();
        OrderSignature orderSignature = orderSignatureDao.selectByOrderId(order.getOrderId());
        String sign = orderSignature.getSignature();
        System.out.println("Chu ky: "+sign);
        // Lấy hash đã lưu từ cơ sở dữ liệu
        String storedHash = RSA.decryptHash(keyUser.getKey(), orderSignature.getSignature());
//        String storedHash = orderSignatureDAO.getHashByOrderId(orderId);
        System.out.println(storedHash);
        if (hash.equals(storedHash)){
            System.out.println("don hang khong bi thay doi");
        }else {
            System.out.println("Bi thay doi");
        }
    }

}