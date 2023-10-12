package io.github.ithmal.itio.codec.sgip.sequence;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 消息流水号管理器
 * @author: ken.lin
 * @since: 2023-10-12 23:09
 */
public class SequenceManager {

    private AtomicLong offset = new AtomicLong();

    public long nextValue() {
        return offset.accumulateAndGet(1, (prev, x) -> {
            if (prev == Integer.MAX_VALUE) {
                return 1;
            } else {
                return prev + x;
            }
        });
    }

    public static void main(String[] args) {
        SequenceManager manager = new SequenceManager();
        for (int i = 0; i < 10; i++) {
            System.out.println(manager.nextValue());
        }
    }
}