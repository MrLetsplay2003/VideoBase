package me.mrletsplay.videobase.util;

import java.time.Instant;
import java.time.temporal.TemporalUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Cache<T> {

	private Map<String, CacheEntry> entries;
	private long amount;
	private TemporalUnit unit;

	public Cache(long amount, TemporalUnit unit) {
		this.entries = new HashMap<>();
		this.amount = amount;
		this.unit = unit;
	}

	public synchronized String add(T element) {
		String id = UUID.randomUUID().toString().replace("-", "");
		Instant expiresAt = Instant.now().plus(amount, unit);
		CacheEntry entry = new CacheEntry(element, expiresAt);
		entries.put(id, entry);
		return id;
	}

	public synchronized T get(String id) {
		return entries.get(id).element;
	}

	public synchronized void purge() {
		Instant now = Instant.now();
		entries.entrySet().removeIf(en -> now.isAfter(en.getValue().expiresAt));
	}

	private class CacheEntry {

		T element;
		Instant expiresAt;

		public CacheEntry(T element, Instant expiresAt) {
			this.element = element;
			this.expiresAt = expiresAt;
		}

	}

}
