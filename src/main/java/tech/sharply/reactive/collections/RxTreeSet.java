package tech.sharply.reactive.collections;

import io.reactivex.rxjava3.processors.FlowableProcessor;
import io.reactivex.rxjava3.processors.PublishProcessor;
import lombok.Getter;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A TreeSet extension that on every put/remove operation emits the complete new state.
 * Warning: Changes to value object property level should be emitted manually; RxTreeSet cannot detect those changes.
 * @param <V>
 */
@Getter
public class RxTreeSet<V> extends TreeSet<V> implements IRxCollection {

	private final FlowableProcessor<TreeSet<V>> publisher;

	public RxTreeSet() {
		super();
		this.publisher = PublishProcessor.<TreeSet<V>>create().toSerialized();
	}

	public RxTreeSet(Collection<? extends V> collection) {
		super(collection);
		this.publisher = PublishProcessor.<TreeSet<V>>create().toSerialized();
	}

	public RxTreeSet(Comparator<? super V> comparator) {
		super(comparator);
		this.publisher = PublishProcessor.<TreeSet<V>>create().toSerialized();
	}

	@Override
	public boolean add(V value) {
		final var val = super.add(value);
		this.publish();
		return val;
	}

	@Override
	public boolean addAll(Collection<? extends V> collection) {
		final var val = super.addAll(collection);
		this.publish();
		return val;
	}

	@Override
	public boolean remove(Object value) {
		final var val = super.remove(value);
		this.publish();
		return val;
	}

	@Override
	public void clear() {
		super.clear();
		this.publish();
	}

	@Override
	public void publish() {
		publisher.onNext(this);
	}

	@Override
	public String toString() {
		AtomicReference<String> str = new AtomicReference<>("");
		this.forEach(val -> str.set(str.get() + val + "\n"));

		return str.get();
	}
}
