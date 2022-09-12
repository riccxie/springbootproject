package com.example.springbootproject;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class)
public class ApplicationTests {

    @Test
    void contextLoads() {
        List<String> list = new ArrayList<>();
        list.add("3");
        list.add("43");
        System.out.println(list.toArray());

        String[] array = new String[]{"123", "23"};
        System.out.println(Arrays.asList(array));
    }



    @Test
    public void test() {
        ScheduledExecutorService scheduledThreadPool =  Executors.newScheduledThreadPool(3);
        scheduledThreadPool.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("等3秒");
            }
        }, 3, TimeUnit.SECONDS);

        scheduledThreadPool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("延迟一秒后每三秒执行一次");
            }
        }, 1, 3, TimeUnit.SECONDS);

        scheduledThreadPool.scheduleAtFixedRate(() -> {
            System.out.println("===============");
        }, 1, 3, TimeUnit.SECONDS);
    }

    public static void main(String[] args) {
        ScheduledExecutorService scheduledThreadPool =  Executors.newScheduledThreadPool(3);
        scheduledThreadPool.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("等3秒");
            }
        }, 3, TimeUnit.SECONDS);

        scheduledThreadPool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("延迟一秒后每三秒执行一次");
            }
        }, 1, 3, TimeUnit.SECONDS);

        scheduledThreadPool.scheduleAtFixedRate(() -> {
            System.out.println("===============");
        }, 1, 3, TimeUnit.SECONDS);
    }

    @Test
    public void lockTest() {
        //选择公平锁、非公平锁
        ReentrantLock lock = new ReentrantLock(true);
        lock.lock();
        try {
            try {
                boolean tryLock = lock.tryLock(100, TimeUnit.MILLISECONDS);
                if (tryLock) {
                    //业务
                    System.out.println("hello");
                }
            } catch (InterruptedException e) {
                log.error("e:", e);
            } finally {
                lock.unlock();
            }
        } finally {
            lock.unlock();
        }
    }



    //synchronized
    //方法
    @Test
    public synchronized void hello() {
        //代码块
        synchronized (this) {

        }
        //对象
        synchronized (new KeyValue()) {

        }
        //可重入
        for (int i = 0; i < 100; i++) {
            synchronized (this) {

            }
        }
    }

    @Test
    public void testt() {
        System.out.println("helo");
    }
    
    
    
    @Test
    public void testT() {
        System.out.println("testT start");
        Thread thread = new MyThread();
        thread.setName("守护线程");
        thread.setDaemon(true);
        thread.start();
        System.out.println("testT end");

    }

    class MyThread extends  Thread {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName());
            System.out.println(Thread.currentThread().getId());
            System.out.println("start");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error("e:", e);
            }
            System.out.println("end");
        }

    }


}

class KeyValue {
    private String key;
    private String value;
}
