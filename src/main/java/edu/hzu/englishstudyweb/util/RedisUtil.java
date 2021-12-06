package edu.hzu.englishstudyweb.util;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 实现jedis
 */
public class RedisUtil {
    private static String ip="localhost";
    private static final  int port=6379;
    private static final int timeout=10000;
    private static JedisPool pool=null;

    static{
        JedisPoolConfig config=new JedisPoolConfig();
        config.setMaxTotal(1024);//最大连接数
        config.setMaxIdle(200);//最大空闲实例数
        config.setMaxWaitMillis(10000);//等连接池给连接的最大时间，毫秒
        config.setTestOnBorrow(true);//borrow一个实例的时候，是否提前vaildate操作

        pool=new JedisPool(config,ip,port,timeout);

    }

    //得到redis连接
    public static Jedis getJedis(){
        if(pool!=null){
            return pool.getResource();
        }else{
            return null;
        }
    }

    //关闭redis连接
    public static void close(final Jedis redis){
        if(redis != null){
            redis.close();
        }
    }
}