package cn.sanlicun.pay.model;

/**
 * Created by 小饭 on 2018/7/6.
 */

public class QrRetModel extends  BaseModel {

    private  String money;
    private  String mark;
    private  String type;
    private  String payUrl;

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPayUrl() {
        return payUrl;
    }

    public void setPayUrl(String payUrl) {
        this.payUrl = payUrl;
    }
}
