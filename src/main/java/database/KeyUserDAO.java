package database;

import model.KeyUser;

import java.sql.SQLException;
import java.util.ArrayList;

public class KeyUserDAO implements DAOInterface<KeyUser>{
    @Override
    public ArrayList<KeyUser> selectAll() {
        return null;
    }

    @Override
    public KeyUser selectById(int id) {
        return null;
    }

    @Override
    public int insert(KeyUser keyUser) throws SQLException {
        return 0;
    }

    @Override
    public int insertAll(ArrayList<KeyUser> list) throws SQLException {
        return 0;
    }

    @Override
    public int delete(KeyUser keyUser) {
        return 0;
    }

    @Override
    public int deleteAll(ArrayList<KeyUser> list) {
        return 0;
    }

    @Override
    public int update(KeyUser keyUser) {
        return 0;
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
}
