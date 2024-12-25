package database;

import model.*;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import util.Hash;
import util.RSA;

import java.nio.charset.StandardCharsets;
import java.util.List;


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
                String signature = rs.getString("signature");
                Timestamp createdAt = rs.getTimestamp("created_at");
                Timestamp updatedAt = rs.getTimestamp("updated_at");
                boolean isVerify = rs.getBoolean("is_signature_verified");
                String hash = rs.getString("hash_order");
                int idkeyuser = rs.getInt("key_user_id");
                // Lấy thông tin Order và StatusOrder từ các bảng tương ứng
                Order order = new OrderDAO().selectById(orderId);
                KeyUser keyUser = new KeyUserDAO().selectById(idkeyuser);
                OrderSignature orderSignature = new OrderSignature(id, order, signature, hash, isVerify, keyUser, createdAt, updatedAt);
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
                String signature = rs.getString("signature");
                Timestamp createdAt = rs.getTimestamp("created_at");
                Timestamp updatedAt = rs.getTimestamp("updated_at");
                boolean isVerify = rs.getBoolean("is_signature_verified");
                String hash = rs.getString("hash_order");
                int idkeyuser = rs.getInt("key_user_id");

                KeyUser keyUser = new KeyUserDAO().selectById(idkeyuser);
                // Lấy thông tin Order và StatusOrder từ các bảng tương ứng
                Order order = new OrderDAO().selectById(orderId);

                orderSignature = new OrderSignature(id, order, signature, hash, isVerify, keyUser, createdAt, updatedAt);
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
                String signature = rs.getString("signature");

                Timestamp createdAt = rs.getTimestamp("created_at");
                Timestamp updatedAt = rs.getTimestamp("updated_at");
                boolean isVerify = rs.getBoolean("is_signature_verified");
                String hash = rs.getString("hash_order");
                int idkeyuser = rs.getInt("key_user_id");

                KeyUser keyUser = new KeyUserDAO().selectById(idkeyuser);
                // Lấy thông tin Order và StatusOrder từ các bảng tương ứng
                Order order = new OrderDAO().selectById(orderId);

                orderSignature = new OrderSignature(id, order, signature, hash, isVerify, keyUser, createdAt, updatedAt);
            }
            // Đóng kết nối
            JDBCUtil.closeConnection(con);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return orderSignature;
    }

    public String getSignatureOrderByOrderId(int orderId) {
        String signature = null;

        try {
            // Create a connection to the database
            Connection con = JDBCUtil.getConnection();

            // Create the SQL query to get the signature based on the order_id
            String sql = "SELECT signature FROM order_signatures WHERE order_id = ?";

            // Prepare the statement
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, orderId);

            // Execute the query
            ResultSet rs = st.executeQuery();

            // If a record is found, retrieve the signature
            if (rs.next()) {
                signature = rs.getString("signature");
            }

            // Close the connection
            JDBCUtil.closeConnection(con);

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching signature for orderId: " + orderId, e);
        }

        return signature;
    }
    @Override
    public int insert(OrderSignature orderSignature) throws SQLException {
        int result = 0;
        try {
            // Tạo kết nối đến cơ sở dữ liệu
            Connection con = JDBCUtil.getConnection();

            // Tạo câu lệnh SQL
            String sql = "INSERT INTO order_signatures (order_id, signature, is_signature_verified, hash_order, key_user_id) VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, orderSignature.getOrderId().getOrderId());
            ps.setString(2, orderSignature.getSignature());
            ps.setBoolean(3, orderSignature.isSignatureVerified());
            ps.setString(4, orderSignature.getHashOrder());

            // Kiểm tra nếu keyUser là null
            if (orderSignature.getKeyUser() != null) {
                ps.setInt(5, orderSignature.getKeyUser().getId());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }

            // Thực thi câu lệnh
            result = ps.executeUpdate();

            // Đóng PreparedStatement
            ps.close();

            // Đóng kết nối
            JDBCUtil.closeConnection(con);

        } catch (SQLException e) {
            throw new RuntimeException("Error while inserting order signature", e);
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
            String sql = "UPDATE order_signatures SET order_id = ?, signature = ?, created_at = ?, updated_at = ?, is_signature_verified=? WHERE id = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, orderSignature.getOrderId().getOrderId());
            ps.setString(2, orderSignature.getSignature());
            ps.setTimestamp(3, orderSignature.getCreatedAt());
            ps.setTimestamp(4, orderSignature.getUpdatedAt());
            ps.setBoolean(5, orderSignature.isSignatureVerified());
            ps.setInt(6, orderSignature.getId());
            result = ps.executeUpdate();

            // Đóng kết nối
            JDBCUtil.closeConnection(con);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
    public int updateSignatureAndStatusByOrderId(int orderId, String signature) {
        int result = 0;
        try {
            // Tạo kết nối đến cơ sở dữ liệu
            Connection con = JDBCUtil.getConnection();

            // Tạo câu lệnh SQL để cập nhật signature và is_signature_verified
            String sql = "UPDATE order_signatures SET signature = ? WHERE order_id = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, signature);      // Gán giá trị signature mới
            ps.setInt(2, orderId);           // Gán giá trị orderId

            result = ps.executeUpdate();     // Thực thi câu lệnh cập nhật

            // Đóng kết nối
            JDBCUtil.closeConnection(con);

        } catch (SQLException e) {
            throw new RuntimeException(e); // Ném ngoại lệ nếu có lỗi xảy ra
        }
        return result; // Trả về số dòng bị ảnh hưởng
    }

    public int updateByOrderId(int orderId, String signature) {
        int result = 0;
        try {
            // Tạo kết nối đến cơ sở dữ liệu
            Connection con = JDBCUtil.getConnection();

            // Tạo câu lệnh SQL để cập nhật signature, keyUser, và is_signature_verified
            String sql = "UPDATE order_signatures " +
                    "SET signature = ?,  is_signature_verified = ? " +
                    "WHERE order_id = ?";

            // Chuẩn bị câu lệnh
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, signature);          // Gán giá trị signature mới
//            ps.setInt(2, keyUserId);             // Gán giá trị keyUserId
            ps.setBoolean(2, true);              // Gán giá trị true cho is_signature_verified
            ps.setInt(3, orderId);               // Gán giá trị orderId

            // Thực thi câu lệnh cập nhật
            result = ps.executeUpdate();

            // Đóng PreparedStatement và kết nối
            ps.close();
            JDBCUtil.closeConnection(con);

        } catch (SQLException e) {
            throw new RuntimeException("Error updating signature, keyUser, and verification status for orderId: " + orderId, e);
        }
        return result; // Trả về số dòng bị ảnh hưởng
    }
    public int updateByOrderIdKey(int orderId, String signature, int keyUserId) {
        int result = 0;
        try {
            // Tạo kết nối đến cơ sở dữ liệu
            Connection con = JDBCUtil.getConnection();

            // Tạo câu lệnh SQL để cập nhật signature, keyUser, và is_signature_verified
            String sql = "UPDATE order_signatures " +
                    "SET signature = ?, key_user_id = ?, is_signature_verified = ? " +
                    "WHERE order_id = ?";

            // Chuẩn bị câu lệnh
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, signature);          // Gán giá trị signature mới
            ps.setInt(2, keyUserId);             // Gán giá trị keyUserId
            ps.setBoolean(3, true);              // Gán giá trị true cho is_signature_verified
            ps.setInt(4, orderId);               // Gán giá trị orderId

            // Thực thi câu lệnh cập nhật
            result = ps.executeUpdate();

            // Đóng PreparedStatement và kết nối
            ps.close();
            JDBCUtil.closeConnection(con);

        } catch (SQLException e) {
            throw new RuntimeException("Error updating signature, keyUser, and verification status for orderId: " + orderId, e);
        }
        return result; // Trả về số dòng bị ảnh hưởng
    }

    public int updateVerifySignatureByOrderId(int orderId, boolean signature) {
        int result = 0;
        try {
            // Tạo kết nối đến cơ sở dữ liệu
            Connection con = JDBCUtil.getConnection();

            // Tạo câu lệnh SQL để cập nhật signature và is_signature_verified
            String sql = "UPDATE order_signatures SET is_signature_verified = ? WHERE order_id = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setBoolean(1, signature);          // Gán giá trị true cho is_signature_verified
            ps.setInt(2, orderId);           // Gán giá trị orderId

            result = ps.executeUpdate();     // Thực thi câu lệnh cập nhật

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
                        hash = rs.getString("hash_order");  // Lấy giá trị hash
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching hash for orderId: " + orderId, e);
        }

        return hash;
    }
    public int insertOrderIdAndHash(int orderId, String hash) {
        int result = 0;
        try {
            // Tạo kết nối đến cơ sở dữ liệu
            Connection con = JDBCUtil.getConnection();

            // Tạo câu lệnh SQL chỉ để chèn order_id và hash
            String sql = "INSERT INTO order_signatures (order_id, hash_order) VALUES (?, ?)";

            // Chuẩn bị câu lệnh
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, orderId);  // Gán giá trị order_id
            ps.setString(2, hash); // Gán giá trị hash

            // Thực thi câu lệnh SQL
            result = ps.executeUpdate();

            // Đóng PreparedStatement và kết nối
            ps.close();
            JDBCUtil.closeConnection(con);

        } catch (SQLException e) {
            throw new RuntimeException("Error inserting order_id and hash", e);
        }
        return result; // Trả về số dòng bị ảnh hưởng
    }

    public static void main(String[] args) throws SQLException, NoSuchAlgorithmException {
        OrderSignatureDAO orderSignatureDAO = new OrderSignatureDAO();

    }

}


