package model;

public class PaiGoodsBean extends GoodsBean{
        public static final String STATE_NO_START = "1";
        public static final String STATE_END = "2";
        public static final String STATE_STARTING = "3";
        public static final String TYPE_DAO = "3";
        public static final String TYPE_WU = "4";
        public static final String TYPE_YOU = "5";
        public String commodity_type;
        public String state;
        public String timeStr;
        public String timeend;
    }