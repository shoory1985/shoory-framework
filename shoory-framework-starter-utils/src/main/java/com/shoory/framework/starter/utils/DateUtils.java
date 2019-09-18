package com.shoory.framework.starter.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
public class DateUtils {
	@Bean
	@Primary
	public DateUtils getDateUtils() {
		return new DateUtils();
	}

	private final HashMap<String, SimpleDateFormat> map = new HashMap<String, SimpleDateFormat>();

	public Date parse(String str, String pattern) {
		if (str == null || str.equals("") || pattern == null || pattern.equals("")) {
			return null;
		}
		try {
			return getSimpleDateFormat(pattern).parse(str);
		} catch (ParseException e) {
			return null;
		}
	}

	public SimpleDateFormat getSimpleDateFormat(String pattern) {
		SimpleDateFormat sdf = map.get(pattern);
		if (sdf == null) {
			map.put(pattern, sdf = new SimpleDateFormat(pattern));
		}
		return sdf;
	}

	public Date parseDateTime(String str) {
		return parse(str, "yyyy-MM-dd HH:mm:ss");
	}

	public Date parseDate(String str) {
		return parse(str, "yyyy-MM-dd");
	}

	public String format(Date date, String pattern) {
		if (date == null || date.equals("") || pattern == null || pattern.equals("")) {
			return null;
		}
		try {
			return getSimpleDateFormat(pattern).format(date);
		} catch (Exception e) {
			return null;
		}
	}

	public String formatDateTime(Date date) {
		return format(date, "yyyy-MM-dd HH:mm:ss");
	}

	public String formatDate(Date date) {
		return format(date, "yyyy-MM-dd");
	}

	public String formatDateTimeSSS(Date date) {
		return format(date, "yyyyMMddHHmmssSSS");
	}
	/**
	 * 获取日期列表
	 * @param start
	 * @param end
	 * @return [2018-08-28, 2018-08-29, 2018-08-30]
	 * @return 
	 */
	public List<String> getDays(Date start, Date end) {
		// 返回的日期集合
		List<String> days = new ArrayList<String>();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar tempStart = Calendar.getInstance();
		tempStart.setTime(start);
		Calendar tempEnd = Calendar.getInstance();
		tempEnd.setTime(end);
		tempEnd.add(Calendar.DATE, +1);
		// 日期加1(包含结束)
		while (tempStart.before(tempEnd)) {
			days.add(dateFormat.format(tempStart.getTime()));
			tempStart.add(Calendar.DAY_OF_YEAR, 1);
		}
		return days;

	}
	
	/**
	 * 获取月份的1号零点
	 * @param adjustMonth ... -1（上月）、0（本月）、1（下月） ...
	 * @return
	 */
	public Date getMonthBegin(int adjustMonth) {
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd");
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
		Calendar cal = Calendar.getInstance();// 获取当前日期
		cal.add(Calendar.MONTH, adjustMonth);
		cal.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		
		String  nextFirstday = sdf.format(cal.getTime());  
		return this.parseDate(nextFirstday);
	}

	/**
	 * 获取月份的1号零点
	 * @param adjustMonth ... -1（上月）、0（本月）、1（下月） ...
	 * @return
	 */
	public Date getDiffDate(int n) {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
		
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd");
		Date date = new Date();
		String today = sdf.format(date.getTime());
		Date  todayDay = this.parseDate(today);
		
		Long todayTime = todayDay.getTime();//今天零点
		Long dayTime = todayTime + (n * 24 * 60 * 60 * 1000);//days日零点
		return new Date(dayTime);
	}
	
	
	public Date getMonthBegin(int year, int month, int adjustYear, int adjustMonth) {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
		Calendar cal = Calendar.getInstance();// 获取当前日期
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.YEAR, adjustYear);
		cal.add(Calendar.MONTH, adjustMonth);
		return cal.getTime();
	}

	public int getAgeByTime(String dateOfBirth, long endTime) {
		if (dateOfBirth == null || dateOfBirth.length() < 10) {
			return 0;
		}
		Date date = new Date(endTime);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 得到当前的年份
		String cYear = sdf.format(date).substring(0, 4);
		String cMouth = sdf.format(date).substring(5, 7);
		String cDay = sdf.format(date).substring(8, 10);
		// 得到生日年份
		String birth_Year = dateOfBirth.substring(0, 4);
		String birth_Mouth = dateOfBirth.substring(5, 7);
		String birth_Day = dateOfBirth.substring(8, 10);
		int age = Integer.parseInt(cYear) - Integer.parseInt(birth_Year);
		if ((Integer.parseInt(cMouth) - Integer.parseInt(birth_Mouth)) < 0) {
			age = age - 1;
		} else if ((Integer.parseInt(cMouth) - Integer.parseInt(birth_Mouth)) == 0) {
			if ((Integer.parseInt(cDay) - Integer.parseInt(birth_Day)) > 0) {
				age = age - 1;
			} else {
				age = Integer.parseInt(cYear) - Integer.parseInt(birth_Year);
			}
		} else if ((Integer.parseInt(cMouth) - Integer.parseInt(birth_Mouth)) > 0) {
			age = Integer.parseInt(cYear) - Integer.parseInt(birth_Year);
		}

		return age;
	}
	
	public int getAge(String dateOfBirth) {
		return this.getAgeByTime(dateOfBirth, System.currentTimeMillis());
	}
	
	/**
	 * 获取days天之前的日期
	 * @param days  天数
	 * @return
	 */
	public String getDateBefore(int days) {
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd");
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
		Calendar cal = Calendar.getInstance();// 获取当前日期
		long time = cal.getTimeInMillis()- days * 24 * 60 * 60 * 1000; //得到days天前的时间戳
		cal.setTimeInMillis(time);
		String  date = sdf.format(cal.getTime());  
		return date;
	}
	/**
	 * 根据日期（带时区）
	 * @return
	 */
	public String formatWithTimeZone(String pattern, long time, int timezone) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		TimeZone.setDefault(TimeZone.getTimeZone("GMT" + (timezone >= 0 ? "+" + timezone : timezone)));
		Calendar cal = Calendar.getInstance();// 获取当前日期
		cal.setTimeInMillis(time);
		String  date = sdf.format(cal.getTime());  
		return date;
	}
}