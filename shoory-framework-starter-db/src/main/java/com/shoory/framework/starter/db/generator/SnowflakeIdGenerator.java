package com.shoory.framework.starter.db.generator;

import java.io.Serializable;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

public class SnowflakeIdGenerator implements Configurable, IdentifierGenerator {

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
		return this.produce();
	}

	@Override
	public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
		
	}


    // ==============================Fields===========================================
    /** 开始时间截 (1970-01-01) */

    /** 符号位为0 */
    //private final long UNSIGNED_BITS = 1L;
    /** Timestamp在id中占的位数 */
    private final long TIMESTAMP_BITS = 42L;
    /** 序列在id中占的位数 */
    private final long SEQUENCE_BITS = 5L;
    /** 机器id所占的位数 */
    private final long WORKER_ID_BITS = 4L;
    /** 数据标识id所占的位数 */
    private final long DATACENTER_ID_BITS = 2L;

    /** 支持的最大时间*/
    private final long MAX_TIMESTAMP = -1L ^ (-1L << TIMESTAMP_BITS);
    /** 支持的最大生成序列*/
    private final long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BITS);
    /** 支持的最大机器id */
    private final long MAX_WORKER_ID = -1L ^ (-1L << WORKER_ID_BITS);
    /** 支持的最大数据标识id */
    private final long MAX_DATACENTER_ID = -1L ^ (-1L << DATACENTER_ID_BITS);
    
    /** 时间左移位 */
    private final long TIMESTAMP_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATACENTER_ID_BITS;
    /** 序列向左移位 */
    private final long SEQUENCE_SHIFT = WORKER_ID_BITS + DATACENTER_ID_BITS;
     /** 机器ID向左移位 */
    private final long WORKER_ID_SHIFT = DATACENTER_ID_BITS;
    /** 数据标识id向左移位*/
    private final long DATACENTER_ID_SHIFT = 0;


    /** 工作机器ID(0~15) */
    @Value("${snowflake.worker-id}")
    private long workerId;

    /** 数据中心ID(0~3) */
	@Value("${snowflake.datacenter-id}")
    private long datacenterId;

    /** 毫秒内序列(0~31) */
    private long sequence = 0L;

    /** 上次生成ID的时间截 */
    private long lastTimestamp = -1L;


    // ==============================Methods==========================================
    /**
     * 获得下一个ID (该方法是线程安全的)
     * @return SnowflakeId
     */
    public synchronized long produce() {
        long timestamp = System.currentTimeMillis();

        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        //if (timestamp < lastTimestamp) {
        //    throw new RuntimeException(
        //            String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        //}

        //如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            //毫秒内序列溢出
            if (sequence == 0) {
                //阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        //时间戳改变，毫秒内序列重置
        else {
            sequence = 0L;
        }

        //上次生成ID的时间截
        lastTimestamp = timestamp;

        //移位并通过或运算拼到一起组成64位的ID
        return (timestamp << TIMESTAMP_SHIFT) // //
                | (sequence << SEQUENCE_SHIFT)//
                | (workerId << WORKER_ID_SHIFT)
                | (datacenterId << DATACENTER_ID_SHIFT) ;
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp != lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

    public long fetchTimestamp(long id) {
    	return id >> TIMESTAMP_SHIFT & MAX_TIMESTAMP;
    }
    public int fetchSequence(long id) {
    	return (int)(id >> SEQUENCE_SHIFT & MAX_SEQUENCE);
    }
    public int fetchWorkerId(long id) {
    	return (int)(id >> WORKER_ID_SHIFT & MAX_WORKER_ID);
    }
    public int fetchDatacenterId(long id) {
    	return (int)(id >> DATACENTER_ID_SHIFT & MAX_DATACENTER_ID);
    }
}
