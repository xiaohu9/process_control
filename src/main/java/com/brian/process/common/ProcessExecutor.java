package com.brian.process.common;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * 执行流程：导入流程执行器组件 -> addThen() -> init() -> execute()
 */
@Slf4j
@Component
public class ProcessExecutor <T> {
    private ExecutorService threadPoolExecutor;
    private TreeMap<Integer, List<ProcessDto>> processMap = new TreeMap<>();
    private Consumer<T> rootConsumer;

    public void addThen(Integer priority, Consumer<T> process, String processName, boolean interruptError) {
        List<ProcessDto> subList = processMap.computeIfAbsent(priority, k -> new ArrayList<>());
        subList.add(new ProcessDto(processName, process, interruptError));
    }

    // 流程初始化
    public void init(ExecutorService executorService) {
        this.threadPoolExecutor = executorService;
        processMap.values().forEach(param -> {
            if (rootConsumer == null) {
                rootConsumer = new ProcessConsumer(param);
            } else {
                rootConsumer.andThen(new ProcessConsumer(param));
            }
        });
    }

    // 流程运行
    public void execute(T context) {
        rootConsumer.accept(context);
    }

    @Data
    private class ProcessDto {

        // 进程名
        private String processName;
        // 函数方法
        private Consumer<T> process;
        // 是否在出错时终端线程
        private boolean interruptError = true;
        // 超时控制
        private int timeout = 1;

        public ProcessDto() {}

        public ProcessDto(String processName, Consumer<T> process, boolean interruptError) {
            this.processName = processName;
            this.process = process;
            this.interruptError = interruptError;
        }
    }

    @Data
    private class ProcessConsumer implements Consumer<T> {

        private List<ProcessDto> processDtoList;
        public ProcessConsumer(List<ProcessDto> processDtoList) {
            this.processDtoList = processDtoList;
        }

        @Override
        public void accept(T context) {
            if (processDtoList.size() == 1) {
                doSingleTask(context, processDtoList.get(0));
            } else {
                doParallelTask(context, processDtoList);
            }
        }

        // 单线程运行
        private void doSingleTask(T context, ProcessDto processDto) {
            try {
                processDto.getProcess().accept(context);
            } catch (Throwable t) {
                if (processDto.interruptError) {
                    throw t;
                }
            }
        }

        // 多线程并行
        private void doParallelTask(T context, List<ProcessDto> processDtoList) {
            List<Future<?>> futureList = new ArrayList<>();
            CountDownLatch countDownLatch = new CountDownLatch(processDtoList.size());
            int maxTimeout = 0;
            for (ProcessDto processDto: processDtoList) {
                if (processDto.timeout > maxTimeout) {
                    maxTimeout = processDto.timeout;
                }

                futureList.add(threadPoolExecutor.submit(() -> {
                    try {
                        processDto.getProcess().accept(context);
                    }  catch(Throwable t) {
                        if (processDto.interruptError) {
                            throw t;
                        }
                    } finally {
                        countDownLatch.countDown();
                    }
                }));
            }

            try {
                if (countDownLatch.await(maxTimeout, TimeUnit.SECONDS)) {
                    log.info("并行流程无法在指定时间内运行完成: 1s，优化方法");
                }

                for (int i = 0; i < futureList.size(); i++) {
                    try {
                        futureList.get(i).get();
                    } catch (Throwable t) {
                        Throwable e = t.getCause();
                        if (processDtoList.get(i).interruptError) {
                            // 中断当前线程，抛出错误
                            if (e instanceof RuntimeException) {
                                throw (RuntimeException) e;
                            } else {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            } catch (InterruptedException t) {
                // 异常中断当前线程
                // 将中断标志位设置为 true，线程会自动扫描
                Thread.currentThread().interrupt();
            }
        }
    }
}
