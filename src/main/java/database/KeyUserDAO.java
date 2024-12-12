package database;

import model.ChangePrice;
import model.KeyUser;
import model.Product;
import model.User;

import java.sql.*;
import java.util.ArrayList;

public class KeyUserDAO implements DAOInterface<KeyUser>{
    @Override
    public ArrayList<KeyUser> selectAll() {
        ArrayList<KeyUser> keyUserArrayList = new ArrayList<>();
        try {
            // tao mot connection
            Connection con = JDBCUtil.getConnection();

            // tao cau lenh sql
            String sql = "SELECT * FROM key_user";

            PreparedStatement st = con.prepareStatement(sql);

            // thuc thi
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                int user_id = rs.getInt("user_id");
                String key = rs.getString("public_key");
                Date create_at = rs.getDate("create_at");
                Date expired_at = rs.getDate("expired_at");
                String status = rs.getString("status");


                UserDAO userDAO = new UserDAO();
                User user = userDAO.selectById(user_id);

                KeyUser keyUser = new KeyUser(user, key, create_at, expired_at, status);
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
            // tao mot connection
            Connection con = JDBCUtil.getConnection();

            // tao cau lenh sql
            String sql = "SELECT * FROM key_user WHERE  user_id=?";

            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, user_id);
            // thuc thi
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                 user_id = rs.getInt("user_id");
                String key = rs.getString("public_key");
                Date create_at = rs.getDate("create_at");
                Date expired_at = rs.getDate("expired_at");
                String status = rs.getString("status");


                UserDAO userDAO = new UserDAO();
                User user = userDAO.selectById(user_id);

                KeyUser keyUser = new KeyUser(user, key, create_at, expired_at, status);
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

            String sql = "SELECT * FROM key_user WHERE user_id = ?";

            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                int user_id = rs.getInt("user_id");
                String key = rs.getString("public_key");
                Date create_at = rs.getDate("create_at");
                Date expired_at = rs.getDate("expired_at");
                String status = rs.getString("status");


                UserDAO userDAO = new UserDAO();
                User user = userDAO.selectById(user_id);

                 keyUser = new KeyUser(user, key, create_at, expired_at, status);
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
            ps.setDate(3, keyUser.getCreate_at());
            ps.setDate(4, keyUser.getExpired_at());
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
            String sql = "UPDATE key_user SET public_key=?, create_at=?, expired_at=?, date_update=? WHERE user_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, keyUser.getKey());
            ps.setDate(2, keyUser.getCreate_at());
            ps.setDate(3, keyUser.getExpired_at());
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

    public static void main(String[] args) {
        KeyUserDAO keyUserDAO = new KeyUserDAO();
        System.out.println(keyUserDAO.selectByUser(3).get(0).getCreate_at());
    }
}
