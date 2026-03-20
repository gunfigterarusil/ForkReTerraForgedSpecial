package rayman.rtfs.concurrent.cache;

public interface SafeCloseable extends AutoCloseable {
    void close();
}
