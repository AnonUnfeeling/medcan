package ua.softgroup.medreview.web.controller.mvc;

import org.springframework.data.domain.Page;

/**
 * Created by jdroidcoder on 30.12.2016.
 */
public class JSONModel {
    private int countPage;
    private Page objectList;

    public JSONModel(int countPage, Page objectList) {
        this.countPage = countPage;
        this.objectList = objectList;
    }

    public int getCountPage() {
        return countPage;
    }

    public void setCountPage(int countPage) {
        this.countPage = countPage;
    }

    public Page getObjectList() {
        return objectList;
    }

    public void setObjectList(Page objectList) {
        this.objectList = objectList;
    }
}
