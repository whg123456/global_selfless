package g.top.data.cache;

/**
 * Created by zhangjie1231 on 2018/1/3.
 */
public interface CachedConfigurer {

     enum ExpireTime {

        NONE(0, "无固定期限"),
        ONE_SEC(1, "1秒钟"),
        FIVE_SEC(5, "5秒钟"),
        TEN_SEC(10, "10秒钟"),
        HALF_A_MIN(30, "30秒钟"),
        ONE_MIN(60, "1分钟"),
        FIVE_MIN(5 * 60, "5分钟"),
        TEN_MIN(10 * 60, "10分钟"),
        TWENTY_MIN(20 * 60, "20分钟"),
        HALF_AN_HOUR(30 * 60, "30分钟"),
        ONE_HOUR(60 * 60, "1小时"),
        ONE_DAY(24 * 60 * 60, "1天"),
         TWO_DAY(2 * 24 * 60 * 60, "2天"),
        ONE_WEEK(7 * 24 * 60 * 60, "1周"),
        TOW_WEEK(14 * 24 * 60 * 60, "2周"),
        ONE_MON(30 * 24 * 60 * 60, "1个月"),
        ONE_YEAR(365 * 24 * 60 * 60, "1年");

        private final int ttl;

        private final String desc;

        ExpireTime(int ttl, String desc) {
            this.ttl = ttl;
            this.desc = desc;
        }

        public int getTTL() {
            if (this == NONE) {
                return -1;
            }
            return ttl;
        }

        public String getDesc() {
            return desc;
        }

        public static ExpireTime match(int time) {

            if (NONE.getTTL() == time) {
                return NONE;
            } else if (ONE_SEC.getTTL() == time) {
                return ONE_SEC;
            } else if (FIVE_SEC.getTTL() == time) {
                return FIVE_SEC;
            } else if (TEN_SEC.getTTL() == time) {
                return TEN_SEC;
            } else if (HALF_A_MIN.getTTL() == time) {
                return HALF_A_MIN;
            } else if (ONE_MIN.getTTL() == time) {
                return ONE_MIN;
            } else if (FIVE_MIN.getTTL() == time) {
                return FIVE_MIN;
            } else if (TEN_MIN.getTTL() == time) {
                return TEN_MIN;
            } else if (TWENTY_MIN.getTTL() == time) {
                return TWENTY_MIN;
            } else if (HALF_AN_HOUR.getTTL() == time) {
                return HALF_AN_HOUR;
            } else if (ONE_HOUR.getTTL() == time) {
                return ONE_HOUR;
            } else if (ONE_DAY.getTTL() == time) {
                return ONE_DAY;
            } else if (ONE_MON.getTTL() == time) {
                return ONE_MON;
            } else if (ONE_YEAR.getTTL() == time) {
                return ONE_YEAR;
            }
            return HALF_AN_HOUR;
        }

        public static class $ {
            public final static String NONE = "NONE";
            public final static String ONE_SEC = "ONE_SEC";
            public final static String FIVE_SEC = "FIVE_SEC";
            public final static String TEN_SEC = "TEN_SEC";
            public final static String HALF_A_MIN = "HALF_A_MIN";
            public final static String ONE_MIN = "ONE_MIN";
            public final static String FIVE_MIN = "FIVE_MIN";
            public final static String TEN_MIN = "TEN_MIN";
            public final static String TWENTY_MIN = "TWENTY_MIN";
            public final static String HALF_AN_HOUR = "HALF_AN_HOUR";
            public final static String ONE_HOUR = "ONE_HOUR";
            public final static String ONE_DAY = "ONE_DAY";
            public final static String ONE_MON = "ONE_MON";
            public final static String ONE_YEAR = "ONE_YEAR";
        }
    }
}
