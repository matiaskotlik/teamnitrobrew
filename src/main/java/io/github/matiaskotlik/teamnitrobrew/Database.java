package io.github.matiaskotlik.teamnitrobrew;

import java.util.List;
import java.util.function.Predicate;

public abstract class Database<T> {
	public abstract void load();
	public abstract void save();
	public abstract List<T> getAll();
	public T get(Predicate<T> predicate, T def) {
		return getAll().stream().filter(predicate).findFirst().orElse(def);
	}
	public T get(Predicate<T> predicate) {
		return get(predicate, null);
	}
}
