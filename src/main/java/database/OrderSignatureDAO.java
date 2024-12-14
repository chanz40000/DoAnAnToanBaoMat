package database;

import model.Order;
import model.OrderSignature;
import model.StatusOrder;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import util.Hash;
import java.nio.charset.StandardCharsets;


public class OrderSignatureDAO implements DAOInterface<OrderSignature> {

    @Override
    public ArrayList<OrderSignature> selectAll() {
        ArrayList<OrderSignature> orderSignatures = new ArrayList<>();
        try {
            // Tạo kết nối đến cơ sở dữ liệu
            Connection con = JDBCUtil.getConnection();

            // Tạo câu lệnh SQL
            String sql = "SELECT * FROM order_signatures";

            PreparedStatement st = con.prepareStatement(sql);

            // Thực thi câu lệnh SQL
            ResultSet rs = st.executeQuery();

            // Duyệt qua kết quả và tạo đối tượng OrderSignature
            while (rs.next()) {
                int id = rs.getInt("id");
                int orderId = rs.getInt("order_id");
                String hash = rs.getString("hash");
                String signature = rs.getString("signature");
                int statusId = rs.getInt("status_id");
                Timestamp createdAt = rs.getTimestamp("created_at");
                Timestamp updatedAt = rs.getTimestamp("updated_at");

                // Lấy thông tin Order và StatusOrder từ các bảng tương ứng
                Order order = new OrderDAO().selectById(orderId);
                StatusOrder statusOrder = new StatusOrderDAO().selectById(statusId);

                OrderSignature orderSignature = new OrderSignature(id, order, hash, signature, statusOrder, createdAt, updatedAt);
                orderSignatures.add(orderSignature);
            }

            // Đóng kết nối
            JDBCUtil.closeConnection(con);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return orderSignatures;
    }

    @Override
    public OrderSignature selectById(int id) {
        OrderSignature orderSignature = null;

        try {
            // Tạo kết nối đến cơ sở dữ liệu
            Connection con = JDBCUtil.getConnection();

            // Tạo câu lệnh SQL
            String sql = "SELECT * FROM order_signatures WHERE id = ?";

            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, id);

            // Thực thi câu lệnh SQL
            ResultSet rs = st.executeQuery();

            // Nếu tìm thấy bản ghi, tạo đối tượng OrderSignature
            if (rs.next()) {
                int orderId = rs.getInt("order_id");
                String hash = rs.getString("hash");
                String signature = rs.getString("signature");
                int statusId = rs.getInt("status_id");
                Timestamp createdAt = rs.getTimestamp("created_at");
                Timestamp updatedAt = rs.getTimestamp("updated_at");

                // Lấy thông tin Order và StatusOrder từ các bảng tương ứng
                Order order = new OrderDAO().selectById(orderId);
                StatusOrder statusOrder = new StatusOrderDAO().selectById(statusId);

                orderSignature = new OrderSignature(id, order, hash, signature, statusOrder, createdAt, updatedAt);
            }
            // Đóng kết nối
            JDBCUtil.closeConnection(con);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return orderSignature;
    }
    public OrderSignature selectByOrderId(int id) {
        OrderSignature orderSignature = null;

        try {
            // Tạo kết nối đến cơ sở dữ liệu
            Connection con = JDBCUtil.getConnection();

            // Tạo câu lệnh SQL
            String sql = "SELECT * FROM order_signatures WHERE order_id = ?";

            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, id);

            // Thực thi câu lệnh SQL
            ResultSet rs = st.executeQuery();

            // Nếu tìm thấy bản ghi, tạo đối tượng OrderSignature
            if (rs.next()) {
                int orderId = rs.getInt("order_id");
                String hash = rs.getString("hash");
                String signature = rs.getString("signature");
                int statusId = rs.getInt("status_id");
                Timestamp createdAt = rs.getTimestamp("created_at");
                Timestamp updatedAt = rs.getTimestamp("updated_at");

                // Lấy thông tin Order và StatusOrder từ các bảng tương ứng
                Order order = new OrderDAO().selectById(id);
                StatusOrder statusOrder = new StatusOrderDAO().selectById(statusId);

                orderSignature = new OrderSignature(id, order, hash, signature, statusOrder, createdAt, updatedAt);
            }
            // Đóng kết nối
            JDBCUtil.closeConnection(con);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return orderSignature;
    }
    @Override
    public int insert(OrderSignature orderSignature) throws SQLException {
        int result = 0;
        try {
            // Tạo kết nối đến cơ sở dữ liệu
            Connection con = JDBCUtil.getConnection();

            // Tạo câu lệnh SQL
            String sql = "INSERT INTO order_signatures (order_id, hash, signature, status_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, orderSignature.getOrderId().getOrderId());
            ps.setString(2, orderSignature.getHash());
            ps.setString(3, orderSignature.getSignature());
            ps.setInt(4, orderSignature.getStatusId().getStatusId());
            ps.setTimestamp(5, orderSignature.getCreatedAt());
            ps.setTimestamp(6, orderSignature.getUpdatedAt());

            result = ps.executeUpdate();

            // Đóng kết nối
            JDBCUtil.closeConnection(con);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public int insertAll(ArrayList<OrderSignature> list) throws SQLException {
        int result = 0;
        for (OrderSignature orderSignature : list) {
            result += this.insert(orderSignature);
        }
        return result;
    }

    @Override
    public int delete(OrderSignature orderSignature) {
        int result = 0;
        try {
            // Tạo kết nối đến cơ sở dữ liệu
            Connection con = JDBCUtil.getConnection();

            // Tạo câu lệnh SQL
            String sql = "DELETE FROM order_signatures WHERE id = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, orderSignature.getId());

            result = ps.executeUpdate();

            // Đóng kết nối
            JDBCUtil.closeConnection(con);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public int deleteAll(ArrayList<OrderSignature> list) {
        int result = 0;
        for (OrderSignature orderSignature : list) {
            result += this.delete(orderSignature);
        }
        return result;
    }

    @Override
    public int update(OrderSignature orderSignature) {
        int result = 0;
        try {
            // Tạo kết nối đến cơ sở dữ liệu
            Connection con = JDBCUtil.getConnection();

            // Tạo câu lệnh SQL
            String sql = "UPDATE order_signatures SET order_id = ?, hash = ?, signature = ?, status_id = ?, created_at = ?, updated_at = ? WHERE id = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, orderSignature.getOrderId().getOrderId());
            ps.setString(2, orderSignature.getHash());
            ps.setString(3, orderSignature.getSignature());
            ps.setInt(4, orderSignature.getStatusId().getStatusId());
            ps.setTimestamp(5, orderSignature.getCreatedAt());
            ps.setTimestamp(6, orderSignature.getUpdatedAt());
            ps.setInt(7, orderSignature.getId());

            result = ps.executeUpdate();

            // Đóng kết nối
            JDBCUtil.closeConnection(con);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
    public int updateSignatureAndStatusByOrderId(int orderId, String signature, StatusOrder statusId) {
        int result = 0;
        try {
            // Tạo kết nối đến cơ sở dữ liệu
            Connection con = JDBCUtil.getConnection();

            // Tạo câu lệnh SQL để cập nhật signature và status
            String sql = "UPDATE order_signatures SET signature = ?, status_id = ? WHERE order_id = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, signature); // Gán giá trị signature mới
            ps.setInt(2, statusId.getStatusId());     // Gán giá trị status mới
            ps.setInt(3, orderId);      // Gán giá trị orderId

            result = ps.executeUpdate(); // Thực thi câu lệnh cập nhật

            // Đóng kết nối
            JDBCUtil.closeConnection(con);

        } catch (SQLException e) {
            throw new RuntimeException(e); // Ném ngoại lệ nếu có lỗi xảy ra
        }
        return result; // Trả về số dòng bị ảnh hưởng
    }
    public String getHashByOrderId(int orderId) {
        String hash = null;

        // Tạo kết nối đến cơ sở dữ liệu
        try (Connection con = JDBCUtil.getConnection()) {

            // Truy vấn hash từ bảng order_signatures theo order_id
            String sql = "SELECT hash FROM order_signatures WHERE order_id = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, orderId);  // Set giá trị order_id vào câu lệnh SQL

                // Thực thi câu lệnh và lấy kết quả
                try (ResultSet rs = ps.executeQuery()) {
                    // Nếu tìm thấy hash trong cơ sở dữ liệu
                    if (rs.next()) {
                        hash = rs.getString("hash");  // Lấy giá trị hash
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching hash for orderId: " + orderId, e);
        }

        return hash;
    }


// Các phương thức còn lại trong lớp OrderSignatureDAO (selectAll, selectById, insert, update, delete...)

    public static boolean validateOrderHash(int orderId) throws SQLException, NoSuchAlgorithmException {
        // Lấy dữ liệu serialized cho đơn hàng từ phương thức đã viết
        OrderSignatureDAO orderSignatureDAO = new OrderSignatureDAO();

        String serializedData = orderSignatureDAO.getSerializedDataForOrder(orderId);

        // Tính toán hash từ serializedData
        String calculatedHash = Hash.calculateHash(serializedData.getBytes(StandardCharsets.UTF_8));

        // Lấy hash đã lưu từ cơ sở dữ liệu
        String storedHash = orderSignatureDAO.getHashByOrderId(orderId);

        // So sánh hash tính toán và hash đã lưu
        return storedHash != null && storedHash.equals(calculatedHash);
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
                    // Chỉ lấy thông tin cần thiết và tạo chuỗi serialized
                    serializedData.append("user_id:").append(rs.getInt("user_id")).append(",");
                    serializedData.append("email:").append(rs.getString("email")).append(",");
                    serializedData.append("order_id:").append(rs.getInt("order_id")).append(",");
                    serializedData.append("total_price:").append(rs.getDouble("total_price")).append(",");

                    // Định dạng ngày giờ booking
                    java.sql.Timestamp timestamp = rs.getTimestamp("booking_date");
                    if (timestamp != null) {
                        // Định dạng ngày giờ đầy đủ (yyyy-MM-dd HH:mm:ss)
                        String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestamp);
                        serializedData.append("booking_date:").append(formattedDate).append(",");
                    } else {
                        serializedData.append("booking_date:null,");
                    }

                    serializedData.append("shipping_fee:").append(rs.getDouble("shipping_fee")).append(",");
                }
            }
        }

        // Không cần xóa dấu phẩy cuối cùng, vì bạn muốn dấu phẩy luôn có ở cuối.
        return serializedData.toString();
    }


    // Main method for testing the validateOrderHash method
    public static void main(String[] args) throws SQLException {
        OrderSignatureDAO orderSignatureDAO = new OrderSignatureDAO();
        int orderId = 17; // Example order ID to test with

        String serializedData = orderSignatureDAO.getSerializedDataForOrder(orderId);
        System.out.println(serializedData);


        try {
            // Validate the order hash for the specified orderId
            boolean isValid = validateOrderHash(orderId);

            if (isValid) {
                System.out.println("Đơn hàng ko bị thay đổi");
            } else {
                System.out.println("Đơn hàng bị thay đổi");
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Hashing error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }


}
