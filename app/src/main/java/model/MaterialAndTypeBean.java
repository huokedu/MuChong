package model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/1.
 */
public class MaterialAndTypeBean {
    public String id;
    public String name;
    public ArrayList<SmallClass> list;
    public class SmallClass{
        public String id;
        public String materialsub_name;
    }
}
