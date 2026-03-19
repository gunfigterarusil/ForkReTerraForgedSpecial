package raccoonman.reterraforged.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class ThreadPools {
	private static final AtomicReference<ExecutorService> WORLD_GEN_REF = new AtomicReference<>(null);

	/**
	 * Lazily-created world-gen pool.  The first call creates a pool sized to
	 * {@link #availableProcessors()}.  Call {@link #resize(int)} after reading
	 * PerformanceConfig to apply a custom thread count.
	 */
	public static ExecutorService worldGen() {
		ExecutorService existing = WORLD_GEN_REF.get();
		if (existing != null) return existing;
		ExecutorService created = Executors.newFixedThreadPool(availableProcessors(), r -> {
			Thread t = new Thread(r, "RTF-WorldGen");
			t.setDaemon(true);
			return t;
		});
		if (WORLD_GEN_REF.compareAndSet(null, created)) {
			return created;
		}
		// Another thread raced us – shut down the duplicate and return the winner
		created.shutdownNow();
		return WORLD_GEN_REF.get();
	}

	/** Backward-compat constant used by MixinUtil / TileGenerator. */
	public static final ExecutorService WORLD_GEN = new DelegatingExecutorService();

	/**
	 * Re-create the pool with a new thread count.
	 * Safe to call once during server start-up before world generation begins.
	 */
	public static void resize(int threadCount) {
		int clamped = Math.max(1, threadCount);
		ExecutorService old = WORLD_GEN_REF.getAndSet(
			Executors.newFixedThreadPool(clamped, r -> {
				Thread t = new Thread(r, "RTF-WorldGen");
				t.setDaemon(true);
				return t;
			})
		);
		if (old != null) old.shutdownNow();
	}

	public static int availableProcessors() {
		return Math.max(2, Runtime.getRuntime().availableProcessors());
	}

	/** Thin wrapper that always forwards to the current pool in WORLD_GEN_REF. */
	private static final class DelegatingExecutorService extends java.util.concurrent.AbstractExecutorService {
		@Override public void execute(Runnable command) { worldGen().execute(command); }
		@Override public void shutdown()               { worldGen().shutdown(); }
		@Override public java.util.List<Runnable> shutdownNow() { return worldGen().shutdownNow(); }
		@Override public boolean isShutdown()          { return worldGen().isShutdown(); }
		@Override public boolean isTerminated()        { return worldGen().isTerminated(); }
		@Override public boolean awaitTermination(long timeout, java.util.concurrent.TimeUnit unit)
			throws InterruptedException                { return worldGen().awaitTermination(timeout, unit); }
	}
}
