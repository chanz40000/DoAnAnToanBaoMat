package database;

import model.ChangePrice;
import model.KeyUser;
import model.Product;
import model.User;
import util.Email;
import util.RSA;

import java.sql.*;
import java.util.ArrayList;

public class KeyUserDAO implements DAOInterface<KeyUser>{
    @Override
    public ArrayList<KeyUser> selectAll() {
        ArrayList<KeyUser> keyUserArrayList = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM key_user";
            PreparedStatement st = con.prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int user_id = rs.getInt("user_id");
                String key = rs.getString("public_key");
                Timestamp create_at = rs.getTimestamp("create_at");
                Timestamp expired_at = rs.getTimestamp("expired_at");
                String status = rs.getString("status");

                UserDAO userDAO = new UserDAO();
                User user = userDAO.selectById(user_id);

                KeyUser keyUser = new KeyUser(id, user, key, create_at, expired_at, status); // Thêm id vào constructor
                keyUserArrayList.add(keyUser);
            }

            JDBCUtil.closeConnection(con);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return keyUserArrayList;
    }


    public ArrayList<KeyUser> selectByUser(int user_id) {
        ArrayList<KeyUser> keyUserArrayList = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM key_user WHERE user_id = ?";
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, user_id);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String key = rs.getString("public_key");
                Timestamp create_at = rs.getTimestamp("create_at");
                Timestamp expired_at = rs.getTimestamp("expired_at");
                String status = rs.getString("status");

                UserDAO userDAO = new UserDAO();
                User user = userDAO.selectById(user_id);

                KeyUser keyUser = new KeyUser(id, user, key, create_at, expired_at, status); // Thêm id vào constructor
                keyUserArrayList.add(keyUser);
            }

            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return keyUserArrayList;
    }




    @Override
    public KeyUser selectById(int id) {
        KeyUser keyUser = null;

        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM key_user WHERE id = ?";
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                int user_id = rs.getInt("user_id");
                String key = rs.getString("public_key");
                Timestamp create_at = rs.getTimestamp("create_at");
                Timestamp expired_at = rs.getTimestamp("expired_at");
                String status = rs.getString("status");

                UserDAO userDAO = new UserDAO();
                User user = userDAO.selectById(user_id);

                keyUser = new KeyUser(id, user, key, create_at, expired_at, status); // Thêm id vào constructor
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return keyUser;
    }



    public KeyUser selectByUserIdStatus(int id, String status) {
        KeyUser keyUser = null;

        try {
            Connection con = JDBCUtil.getConnection();

            String sql = "SELECT * FROM key_user WHERE user_id = ? AND status = ?";

            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, id);
            st.setString(2, status);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                int idKey = rs.getInt("id");
                int user_id = rs.getInt("user_id");
                String key = rs.getString("public_key");
                Timestamp create_at = rs.getTimestamp("create_at");
                Timestamp expired_at = rs.getTimestamp("expired_at");
                 status = rs.getString("status");


                UserDAO userDAO = new UserDAO();
                User user = userDAO.selectById(user_id);

                keyUser = new KeyUser(idKey, user, key, create_at, expired_at, status);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return keyUser;
    }

    @Override
    public int insert(KeyUser keyUser) throws SQLException {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "INSERT INTO key_user (user_id, public_key, create_at, expired_at, status) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, keyUser.getUser_id().getUserId());
            ps.setString(2, keyUser.getKey());
            ps.setTimestamp(3, keyUser.getCreate_at());
            ps.setTimestamp(4, keyUser.getExpired_at());
            ps.setString(5, keyUser.getStatus());
            result = ps.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public int insertAll(ArrayList<KeyUser> list) throws SQLException {
        int result = 0;
        for (KeyUser cp : list) {
            result += this.insert(cp);
        }
        return result;
    }

    @Override
    public int delete(KeyUser keyUser) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "DELETE FROM key_user WHERE user_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, keyUser.getUser_id().getUserId());
            result = ps.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public int deleteAll(ArrayList<KeyUser> list) {
        int result = 0;
        for (KeyUser cp : list) {
            result += this.delete(cp);
        }
        return result;
    }

    @Override
    public int update(KeyUser keyUser) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "UPDATE key_user SET public_key=?, create_at=?, expired_at=?, status=? WHERE user_id=? AND status='ON'";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, keyUser.getKey());
            ps.setTimestamp(2, keyUser.getCreate_at());
            ps.setTimestamp(3, keyUser.getExpired_at());
            ps.setString(4, keyUser.getStatus());
            ps.setInt(5, keyUser.getUser_id().getUserId());
            result = ps.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
    
//    use book;
//    CREATE TABLE key_user (
//            id INT AUTO_INCREMENT PRIMARY KEY,
//            user_id INT NOT NULL,
//            public_key TEXT NOT NULL,
//            create_at DATETIME,
//            expired_at DATETIME,
//            status CHAR(3) DEFAULT 'on',
//    CONSTRAINT fk_user_id FOREIGN KEY (user_id)
//    REFERENCES users(user_id)
//    ON DELETE CASCADE
//    ON UPDATE CASCADE
//);
    //phuong thuc cho nguoi dung bao loi key, cap nhat key moi


    public static void main(String[] args) {
        KeyUserDAO keyUserDAO = new KeyUserDAO();
//        System.out.println(keyUserDAO.selectByUser(3).get(0).getCreate_at());
//
        keyUserDAO.selectById(10);
        UserDAO userDAO = new UserDAO();
        User user = userDAO.selectById(2);
        KeyUser userKey = keyUserDAO.selectByUserIdStatus(user.getUserId(), "ON");
        System.out.println(userKey.toString());

    }
}
