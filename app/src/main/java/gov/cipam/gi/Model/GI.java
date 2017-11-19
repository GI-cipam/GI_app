package gov.cipam.gi.Model;

/**
 * Created by Deepak on 11/18/2017.
 */

public class GI {

    private String name,dpurl,detail,category,state;

    public GI(){

    }

    public void  GI(String name, String dpurl, String detail, String category, String state) {
        this.name = name;
        this.dpurl = dpurl;
        this.detail = detail;
        this.category = category;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDpurl() {
        return dpurl;
    }

    public void setDpurl(String dpurl) {
        this.dpurl = dpurl;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
