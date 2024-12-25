package model;

import java.sql.Date;
import java.sql.Timestamp;

public class KeyUser {
    int id;
    User user;
    String key;
    Timestamp create_at;
    Timestamp expired_at;
    String status;

    public KeyUser(User user_id, String key, Timestamp create_at, Timestamp expired_at, String status) {
        this.user = user_id;
        this.key = key;
        this.create_at = create_at;
        this.expired_at = expired_at;
        this.status = status;
    }
    public KeyUser(int id, User user, String key, Timestamp create_at, Timestamp expired_at, String status) {
        this.id = id;
        this.user = user;
        this.key = key;
        this.create_at = create_at;
        this.expired_at = expired_at;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser_id() {
        return user;
    }

    public void setUser_id(User user_id) {
        this.user = user_id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Timestamp getCreate_at() {
        return create_at;
    }

    public void setCreate_at(Timestamp create_at) {
        this.create_at = create_at;
    }

    public Timestamp getExpired_at() {
        return expired_at;
    }

    public void setExpired_at(Timestamp expired_at) {
        this.expired_at = expired_at;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "KeyUser{" +
                "id=" + id +
                ", user=" + user +
                ", key='" + key + '\'' +
                ", create_at=" + create_at +
                ", expired_at=" + expired_at +
                ", status='" + status + '\'' +
                '}';
    }
}
