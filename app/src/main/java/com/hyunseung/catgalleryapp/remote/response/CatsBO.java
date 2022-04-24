package com.hyunseung.catgalleryapp.remote.response;


import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.util.ArrayList;

@Root(name = "response")
public class CatsBO {
    @Path("data/images")
    @ElementList(type = CatBO.class, inline = true)
    private ArrayList<CatBO> cats;

    public ArrayList<CatBO> getCats() {
        return cats;
    }

    public void setCats(ArrayList<CatBO> cats) {
        this.cats = cats;
    }
}