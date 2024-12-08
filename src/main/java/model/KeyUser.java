package model;

import java.sql.Date;

public class KeyUser {
    int user_id;
    String key;
    Date create_at;
    Date expired_at;
    String status;

    public KeyUser(int user_id, String key, Date create_at, Date expired_at, String status) {
        this.user_id = user_id;
        this.key = key;
        this.create_at = create_at;
        this.expired_at = expired_at;
        this.status = status;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Date getCreate_at() {
        return create_at;
    }

    public void setCreate_at(Date create_at) {
        this.create_at = create_at;
    }

    public Date getExpired_at() {
        return expired_at;
    }

    public void setExpired_at(Date expired_at) {
        this.expired_at = expired_at;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
