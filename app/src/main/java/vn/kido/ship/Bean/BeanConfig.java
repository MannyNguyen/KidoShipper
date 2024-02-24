package vn.kido.ship.Bean;

import com.google.gson.annotations.SerializedName;

public class BeanConfig extends BeanBase {
    @SerializedName("version")
    private String version;
    @SerializedName("restful_ip")
    private String restful_ip;
    @SerializedName("restful_ip_dev")
    private String restful_ip_dev;
    @SerializedName("restful_ip_test")
    private String restful_ip_test;
    @SerializedName("socket_chat_ip")
    private String socket_chat_ip;
    @SerializedName("update_status")
    private String update_status;
    @SerializedName("maintenance_status")
    private String maintenance_status;
    @SerializedName("android_url")
    private String android_url;

    public BeanConfig() {
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRestful_ip() {
        return restful_ip;
    }

    public void setRestful_ip(String restful_ip) {
        this.restful_ip = restful_ip;
    }

    public String getRestful_ip_dev() {
        return restful_ip_dev;
    }

    public void setRestful_ip_dev(String restful_ip_dev) {
        this.restful_ip_dev = restful_ip_dev;
    }

    public String getRestful_ip_test() {
        return restful_ip_test;
    }

    public void setRestful_ip_test(String restful_ip_test) {
        this.restful_ip_test = restful_ip_test;
    }

    public String getSocket_chat_ip() {
        return socket_chat_ip;
    }

    public void setSocket_chat_ip(String socket_chat_ip) {
        this.socket_chat_ip = socket_chat_ip;
    }

    public String getUpdate_status() {
        return update_status;
    }

    public void setUpdate_status(String update_status) {
        this.update_status = update_status;
    }

    public String getMaintenance_status() {
        return maintenance_status;
    }

    public void setMaintenance_status(String maintenance_status) {
        this.maintenance_status = maintenance_status;
    }

    public String getAndroid_url() {
        return android_url;
    }

    public void setAndroid_url(String android_url) {
        this.android_url = android_url;
    }
}
