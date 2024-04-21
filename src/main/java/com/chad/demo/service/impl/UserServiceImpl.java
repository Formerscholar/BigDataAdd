package com.chad.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chad.demo.beans.User;
import com.chad.demo.mapper.UserMapper;
import com.chad.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.concurrent.*;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(8, 8, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
    @Autowired
    private TransactionTemplate transactionTemplate;

    @Override
    public void addAll() {
        long startTime = System.currentTimeMillis(); // 获取开始时间
        int total = 1000000;
        int size = 1000;
        int batch = total / size;
        log.info("总条数：{}，批次：{}，每批：{}条", total, batch, size);
        CountDownLatch latch = new CountDownLatch(batch);
        for (int i = 0; i < batch; i++) {
            int finalI = i;
            threadPoolExecutor.execute(() -> {
                CopyOnWriteArrayList<User> arrayList = new CopyOnWriteArrayList<>();
                for (int y = 0; y < size; y++) {
                    User user = new User();
                    user.setAge(finalI);
                    user.setSex(0);
                    user.setName(String.valueOf(finalI + y));
                    user.setMark(new BigDecimal(y));
                    arrayList.add(user);
                }
                transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                    @Override
                    protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                        try {
                            // 在这里进行需要在事务中执行的操作
                            boolean b = saveBatch(arrayList);
                            log.info("批量插入数据结果：{}", b);
                            // 如果操作成功，手动提交事务
                            transactionStatus.flush();
                        } catch (Exception e) {
                            // 如果操作失败，手动回滚事务
                            e.printStackTrace();
                            log.error("批量插入数据错误：{}", e.getMessage());
                            transactionStatus.setRollbackOnly();
                        } finally {
                            latch.countDown();
                        }
                    }

                });
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        long endTime = System.currentTimeMillis(); // 获取结束时间
        long elapsedTime = endTime - startTime; // 计算执行时间
        System.out.println("数据插入执行时间： " + elapsedTime + "毫秒");
    }
}
