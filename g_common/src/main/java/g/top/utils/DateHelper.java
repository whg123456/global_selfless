package g.top.utils;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 时间处理工具类
 */
@Slf4j
public class DateHelper {

    public final static String DATEFORMAT_FULL = "yyyy-MM-dd HH:mm:ss.SSS";
    public final static String DATEFORMAT_STANDARD = "yyyy-MM-dd HH:mm:ss";
    public final static String DATEFORMAT_ONLY_DATE = "yyyy-MM-dd";
    public final static String DATEFORMAT_ONLY_DATE_SHORT = "yyyyMMdd";
    public final static String DATEFORMAT_NEW = "yyyy/MM/dd HH:mm:ss";
    public final static String DATEFORMAT_FULL_PURE_NUMBER = "yyyyMMddHHmmssSSS";
    public final static String DATEFORMAT_FULL_PURE_NUMBER_SHORT = "yyyyMMddHHmmss";
    public final static String FULL_TIME_PATTERN = "HH:mm:ss.SSS";
    public final static String FULL_TIME_PATTERN_SHORT = "HH:mm:ss";
    public final static String FULL_TIME_PATTERN_SHORT_T = "HHmmss";
    public final static String DATEFORMONTH_PATTERN_SHORT = "MM-dd HH:mm";
    /**
     * "2019-01-30T13:16:06.950+0000"
     * "2019-01-30T13:16:06.950"
     * "2019-01-30T13:16:06"
     */
    public final static String DATEFORMAT_SPECIAL_T = "yyyy-MM-dd'T'HH:mm:ss";

    public static void main(String[] args) {
        long l = getTodayRemainingTimeInSecond();
        System.out.println(l);
        long h = getRemainSecondsOneDay(new Date());
        System.out.println(h);

    }

    public static Date getLastDayOfMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        String dayTime = serialize(cal.getTime(), DATEFORMAT_ONLY_DATE) + " 00:00:00";
        return deserialize(dayTime, DATEFORMAT_STANDARD);
    }


    public static Date deserialize(String source, String... dateFormat) {
        Date date = null;
        if (dateFormat != null && dateFormat.length > 0) {
            for (String df : dateFormat) {
                try {
                    date = new SimpleDateFormat(df).parse(source);
                    break;
                } catch (Exception e) {
                    log.debug(e.getMessage());
                }
            }

        } else {
            // sample: /Date(1528805593000)/
            Matcher matcher = Pattern.compile("\\/Date\\((-?\\d+)\\)\\/").matcher(source);
            if (matcher.matches()) {
                date = new Date(Long.valueOf(matcher.group(1)));
            } else {
                try {
                    date = parse(source);
                } catch (ParseException e) {
                    log.debug(e.getMessage());
                }
            }


        }
        Preconditions.checkNotNull(date, String.format("date %s can't be resolved", source));
        return date;

    }

    /**
     * 使用dateFormat格式化Date对象
     *
     * @param date
     * @param dateFormat
     * @return
     */
    public static String serialize(Date date, String dateFormat) {
        DateFormat df = new SimpleDateFormat(dateFormat);
        String str = df.format(date);
        return str;
    }

    /**
     * 使用dateFormat格式化LocalDateTime对象
     *
     * @param date
     * @param dateFormat
     * @return
     */
    public static String serialize(LocalDateTime date, String dateFormat) {
        DateFormat df = new SimpleDateFormat(dateFormat);
        String str = df.format(localDateTimeToDate(date));
        return str;
    }

    /**
     * 使用dateFormat格式化LocalDate对象
     *
     * @param date
     * @param dateFormat
     * @return
     */
    public static String serialize(LocalDate date, String dateFormat) {
        DateFormat df = new SimpleDateFormat(dateFormat);
        String str = df.format(localDateToDate(date));
        return str;
    }

    /**
     * 使用yyyy-MM-dd HH:mm:ss.SSS格式化date对象
     *
     * @param date
     * @return
     */
    public static String serialize(Date date) {
        DateFormat df = new SimpleDateFormat(DateHelper.DATEFORMAT_FULL);
        String str = df.format(date);
        return str;
    }

    /**
     * 返回代表当前时间格式化的字符串（格式：yyyy-MM-dd HH:mm:ss.SSS）
     *
     * @return
     */
    public static String getNowString() {
        return serialize(getNow(), DATEFORMAT_FULL);
    }

    /**
     * 返回代表当前时间格式化的字符串
     *
     * @return
     */
    public static String getNowString(String dateFormat) {
        return serialize(getNow(), dateFormat);
    }

    /**
     * 返回一个代表当前时间的Date对象
     *
     * @return
     */
    public static Date getNow() {
        return Calendar.getInstance().getTime();
    }

    /**
     * 返回代表当前时间的毫秒数
     *
     * @return
     */
    public static long getNowInInMillis() {
        return Calendar.getInstance().getTimeInMillis();
    }

    /**
     * 获取当前日期加上指定时间长度以后的Date
     *
     * @param timeUnit 时间单位
     * @param account  添加到calendarTimeField的时间或日期数量
     * @return
     */
    public static Date getDate(TimeUnit timeUnit, int account) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(timeUnit.getValue(), account);
        return calendar.getTime();
    }

    /**
     * 加减指定单位的时间
     *
     * @param date
     * @param timeUnit
     * @param account
     * @return
     */
    public static Date add(Date date, TimeUnit timeUnit, int account) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(timeUnit.getValue(), account);
        return calendar.getTime();
    }

    /**
     * 获取指定的timestamp
     *
     * @param timeUnit 时间单位
     * @param account  添加到calendarTimeField的时间或日期数量
     * @return
     */
    public static long getTimeInMillis(TimeUnit timeUnit, int account) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(timeUnit.getValue(), account);
        return calendar.getTimeInMillis();
    }

    /**
     * 函数功能描述:UTC时间转本地时间格式
     *
     * @param datetime 日期字符串
     * @return 本地日期
     */
    private static Date parse(String datetime) throws ParseException {
        boolean isUTC = false;
        String utcTimePattern = "yyyy-MM-dd";
        String subTime = datetime.substring(10);//UTC时间格式以 yyyy-MM-dd 开头,将utc时间的前10位截取掉,之后是含有多时区时间格式信息的数据

        //处理当后缀为:+8:00时,转换为:+08:00 或 -8:00转换为-08:00
        if (subTime.indexOf("+") != -1) {
            subTime = changeUtcSuffix(subTime, "+");
        } else if (subTime.indexOf("-") != -1) {
            subTime = changeUtcSuffix(subTime, "-");
        }
        datetime = datetime.substring(0, 10) + subTime;

        //依据传入函数的utc时间,得到对应的utc时间格式
        //步骤一:处理 T
        if (datetime.indexOf("T") != -1) {
            utcTimePattern += "'T'";
        }

        //步骤二:处理毫秒SSS
        if (StringUtils.hasText(subTime)) {
            if (datetime.indexOf(".") != -1) {
                utcTimePattern = utcTimePattern + "HH:mm:ss.SSS";
            } else if (subTime.indexOf("+") == -1 || subTime.indexOf("-") == -1 || subTime.indexOf("Z") == -1) {
                List<String> list = Arrays.asList("HH:mm:ss".split("[:]"));
                utcTimePattern = utcTimePattern + String.join(":", list.subList(0, subTime.split("[:]").length));
            } else {
                utcTimePattern = utcTimePattern + "HH:mm:ss";
            }
        }

        //步骤三:处理时区问题
        if (subTime.indexOf("+") != -1 || subTime.indexOf("-") != -1) {
            utcTimePattern += "XXX";
            isUTC = true;
        } else if (subTime.indexOf("Z") != -1) {
            utcTimePattern += "'Z'";
            isUTC = true;
        }


        SimpleDateFormat utcFormater = new SimpleDateFormat(utcTimePattern);
        if (isUTC)
            utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = utcFormater.parse(datetime);
        return date;

    }

    /**
     * 函数功能描述:修改时间格式后缀
     * 函数使用场景:处理当后缀为:+8:00时,转换为:+08:00 或 -8:00转换为-08:00
     *
     * @param subTime
     * @param sign
     * @return
     */
    private static String changeUtcSuffix(String subTime, String sign) {
        String timeSuffix = null;
        String[] splitTimeArrayOne = subTime.split("[" + sign + "]");
        String[] splitTimeArrayTwo = splitTimeArrayOne[1].split(":");
        if (splitTimeArrayTwo[0].length() < 2) {
            timeSuffix = sign + "0" + splitTimeArrayTwo[0] + ":" + splitTimeArrayTwo[1];
            subTime = splitTimeArrayOne[0] + timeSuffix;
            return subTime;
        }
        return subTime;
    }

    public static long pastDays(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / (24 * 60 * 60 * 1000);
    }

    public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

    public enum TimeUnit {
        YEAR(Calendar.YEAR),
        MONTH(Calendar.MONTH),
        DAY(Calendar.DAY_OF_YEAR),
        HOUR(Calendar.HOUR_OF_DAY),
        MINUTE(Calendar.MINUTE),
        SECOND(Calendar.SECOND),
        MILLISECOND(Calendar.MILLISECOND);


        private int v;

        TimeUnit(int value) {
            this.v = value;
        }

        int getValue() {
            return this.v;
        }
    }

    public static Date localDateToDate(LocalDate localDate) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDate.atStartOfDay(zoneId);

        Date date = Date.from(zdt.toInstant());
        return date;
    }

    public static LocalDate dateToLocalDate(Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate;
    }

    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);

        Date date = Date.from(zdt.toInstant());
        return date;
    }

    public static LocalDateTime dateToLocalDateTime(Date date) {
        LocalDateTime LocalDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return LocalDateTime;
    }

    public static String getRemainingTime(Date future) {
        long seconds = (future.getTime() - System.currentTimeMillis()) / 1000;

        int secondsPerDay = 86400;
        int secondsPerHour = 3600;
        int secondsPerMinute = 60;

        if (seconds < 0) {
            return "-";
        }

        StringBuilder stringBuilder = new StringBuilder();
        if (seconds > secondsPerDay) {
            stringBuilder.append(seconds / secondsPerDay).append("天");
            seconds -= seconds / secondsPerDay * secondsPerDay;
        }


        if (seconds > secondsPerHour) {
            stringBuilder.append(seconds / secondsPerHour).append("时");
            seconds -= seconds / secondsPerHour * secondsPerHour;
        } else if (stringBuilder.length() > 0) {
            stringBuilder.append(0).append("时");
        }

        if (seconds > secondsPerMinute) {
            stringBuilder.append(seconds / secondsPerMinute).append("分");
            seconds -= seconds / secondsPerMinute * secondsPerMinute;
        } else if (stringBuilder.length() > 0) {
            stringBuilder.append(0).append("分");
        }

        stringBuilder.append(seconds).append("秒");
        return stringBuilder.toString();

    }

    public static Long getTimeDifference(Date beginDate, Date endDate, TimeUnit timeUnit){
        long beginTime = beginDate.getTime();
        long endTime = endDate.getTime();
        int unit = 1;
        if(timeUnit.getValue() == Calendar.DAY_OF_YEAR){
            unit = 1000 * 60 * 60 *24;
        }
        //其他单位待完善
        long betweenDays = (long)((endTime - beginTime) / unit + 0.5);
        return betweenDays;
    }
    /**
     * 获取当日剩余时间，单位：秒
     *
     * @return
     */
    public static long getTodayRemainingTimeInSecond() {
        Instant end = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).plusDays(1).plus(-1, ChronoUnit.SECONDS).toInstant();
        long seconds = Duration.between(Instant.now(), end).toMillis() / 1000;
        return seconds;
    }

    /**
     * 获取距离指定日期的剩余时间，单位：秒
     *
     * @return
     */
    public static Integer getRemainSecondsOneDay(Date currentDate) {
        LocalDateTime midnight = LocalDateTime.ofInstant(currentDate.toInstant(),
                ZoneId.systemDefault()).plusDays(1).withHour(0).withMinute(0)
                .withSecond(0).withNano(0);
        LocalDateTime currentDateTime = LocalDateTime.ofInstant(currentDate.toInstant(),
                ZoneId.systemDefault());
        long seconds = ChronoUnit.SECONDS.between(currentDateTime, midnight);
        return (int) seconds;
    }

    /**
     * 获取N日内剩余时间，单位：秒
     *
     * @return
     */
    public static long getDaysRemainingTimeInSecond(Long days) {
        Instant end = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).plusDays(days).plus(-1, ChronoUnit.SECONDS).toInstant();
        long seconds = Duration.between(Instant.now(), end).toMillis() / 1000;
        return seconds;
    }

    /**
     * 解析字符串为LocalTime
     * @param source
     * @return
     */
    public static LocalTime parseTime(String source) {

        String timePattern = "";

        if (source.indexOf(".") != -1) {
            timePattern = FULL_TIME_PATTERN;
        } else {
            timePattern = FULL_TIME_PATTERN.substring(0, FULL_TIME_PATTERN.indexOf("."));
            timePattern = Joiner.on(":").join(Arrays.asList(timePattern.split("[:]")).subList(0, source.split("[:]").length));
        }

        return LocalTime.parse(source, DateTimeFormatter.ofPattern(timePattern));
    }

    /**
     * 产生指定日期的随机时间点
     * @param localDate
     * @param startTime
     * @param endTime
     * @return
     */
    public static Date getRandomTime(LocalDate localDate, LocalTime startTime, LocalTime endTime) {
        int offsetSeconds = 0;
        int compared = startTime.compareTo(endTime);
        if (compared != 0) {
            Duration duration = Duration.between(startTime, endTime);
            offsetSeconds = new Random().nextInt((int) Math.abs(duration.toMillis() / 1000));
        }

        LocalTime randomTime = (compared < 0 ? startTime : endTime).plusSeconds(offsetSeconds);
        return Date.from(LocalDateTime.of(localDate, randomTime).atZone(ZoneOffset.systemDefault()).toInstant());
    }

    /**
     * 合并localDate和localTime为Date
     * @param localDate
     * @param localTime
     * @return
     */
    public static Date combine(LocalDate localDate, LocalTime localTime){
        return Date.from(LocalDateTime.of(localDate, localTime).atZone(ZoneOffset.systemDefault()).toInstant());
    }

    public static LocalTime min(LocalTime a,LocalTime b){
        return a.isBefore(b)?a:b;
    }

    public static LocalTime max(LocalTime a,LocalTime b){
        return a.isAfter(b)?a:b;
    }
    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            smdate = sdf.parse(sdf.format(smdate));
            bdate = sdf.parse(sdf.format(bdate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }
}
