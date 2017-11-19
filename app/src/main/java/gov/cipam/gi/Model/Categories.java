package gov.cipam.gi.Model;

/**
 * Created by Deepak on 11/18/2017.
 */

public class Categories {
    private String name,dpurl;

    public Categories(){

    }

    public Categories(String name, String dpurl) {
        this.name = name;
        this.dpurl = dpurl;
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
}
