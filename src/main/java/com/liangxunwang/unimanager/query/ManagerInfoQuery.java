package com.liangxunwang.unimanager.query;

/**
 *
 */
public class ManagerInfoQuery {
    private String id;//商家店铺ID
    private int index;
    private int size;
    private String is_card;
    private String keyWords;
    private String phone;
    private String company_name;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getIs_card() {
        return is_card;
    }

    public void setIs_card(String is_card) {
        this.is_card = is_card;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }
}
