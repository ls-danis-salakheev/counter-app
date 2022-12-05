package com.lightspeedhq.counter.util.generator.impl;

import com.lightspeedhq.counter.util.generator.NameGenerator;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.random.RandomGenerator;

/**
 * The Snowflake IDs implementation.
 * This is a form of unique identifier that is more preferable in distributed computing.
 * <p>
 * The process creates the id (name) based on {@link #pattern}.
 * <blockquote><pre>
 * For example: 0-1668511227037-197-6, where
 * </pre></blockquote><p>
 * - 1st index is a byte sign
 * <p>
 * - 2nd index ia a timestamp obtained by a difference of an epoch {@link #startEpoch} by
 * a method {@link #getCurrentTimestamp()}
 * <p>
 * - 3rd index is a node id that calculated based on the server MAC address between {@link #createNodeId()}
 * and the max bit size {@link #maxNodeId}
 * <p>
 * - 4th index is a {@link #maxSequence} bits sequence
 */
@Component
@RequiredArgsConstructor
public class CustomNameGenerator implements NameGenerator<String> {

    @Value("${generator.nodeId.bitsCount}")
    private int nodeIdBits;

    @Value("${generator.sequence.bitsCount}")
    private int sequenceBits;

    @Value("${generator.sign.bit}")
    private byte signBit;

    @Value("${generator.string.pattern.value}")
    private String pattern;

    @Value("""
            #{T(java.time.LocalDate)
            .parse('${generator.startEpoch}')
            .atStartOfDay()
            .toEpochSecond(T(java.time.ZoneOffset).UTC)}
            """)
    private long startEpoch;

    private long nodeId;
    private long maxNodeId;
    private long maxSequence;

    private volatile long lastTimestamp = -1L;
    private volatile long sequence = 0L;

    /**
     * Thread-safe.
     * Creates snowflakes-typed id (names) uning the parrent {@link #pattern}.
     *
     * @return the id
     */
    @Override
    public synchronized String generate() {
        long currentTimestamp = getCurrentTimestamp();
        if (currentTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & maxSequence;
            if (sequence == 0) {
                currentTimestamp = nextMillis(currentTimestamp);
            }
        } else {
            sequence = 0;
        }
        lastTimestamp = currentTimestamp;
        return pattern.formatted(signBit, currentTimestamp, nodeId, sequence);
    }

    /**
     * Creates a node id
     *
     * @return the long between 0 and {@link }
     */
    private long createNodeId() {
        long nodeId;
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(localHost);
            byte[] hardwareBytes = networkInterface.getHardwareAddress();
            String[] hexadecimal = new String[hardwareBytes.length];
            for (int i = 0; i < hardwareBytes.length; i++) {
                hexadecimal[i] = String.format("%02X", hardwareBytes[i]);
            }
            String macAddress = String.join("-", hexadecimal);
            nodeId = macAddress.hashCode();
        } catch (Exception exception) {
            nodeId = RandomGenerator.getDefault().nextInt();
        }
        nodeId = nodeId & maxNodeId;
        return nodeId;
    }

    /**
     * Compares to the last and current timestamp and waits the next timestamp
     *
     * @param currentTimestamp the timestamp for comparing
     * @return the next timestamp
     */
    private long nextMillis(long currentTimestamp) {
        while (currentTimestamp == lastTimestamp) {
            currentTimestamp = getCurrentTimestamp();
        }
        return currentTimestamp;
    }

    /**
     * Calculates the current timestamp according to the epoch {@link #startEpoch}
     *
     * @return the long
     */
    private long getCurrentTimestamp() {
        return System.currentTimeMillis() - startEpoch;
    }

    /**
     * Initializes the bean properties on the init callback stage
     */
    @PostConstruct
    private void init() {
        maxNodeId = (1L << nodeIdBits) - 1;
        maxSequence = (1L << sequenceBits) - 1;
        nodeId = createNodeId();
    }
}
