package rayman.rtfs.concurrent.cache;

public interface ExpiringEntry {
    long getTimestamp();

    default void close() {
    }
}
