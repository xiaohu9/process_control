package common;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.function.Consumer;

public class ProcessExecutor <T> {
    private T threadPoolExecutor;
    private TreeMap<Integer, List<ProcessDto>> processMap = new TreeMap<>();
    private ProcessConsumer rootConsumer;

    public void addThen(Integer priority, ProcessConsumer processConsumer, String processName, boolean interruptError) {
        List<ProcessDto> subList = processMap.computeIfAbsent(priority, k -> new ArrayList<>());
        subList.add(new ProcessDto(processName, processConsumer, interruptError));
    }

    public void init() {}

    @Data
    private class ProcessDto {

        // 进程名
        private String processName;
        // 函数方法
        private ProcessConsumer consumer;
        // 是否在出错时终端线程
        private boolean interruptError = true;
        // 超时控制
        private int timeout = 1;

        public ProcessDto() {}

        public ProcessDto(String processName, ProcessConsumer consumer, boolean interruptError) {
            this.processName = processName;
            this.consumer = consumer;
            this.interruptError = interruptError;
        }
    }

    @Data
    private class ProcessConsumer implements Consumer<T> {

        private List<ProcessDto> processDtoList;

        @Override
        public void accept(T t) {

        }
    }
}
