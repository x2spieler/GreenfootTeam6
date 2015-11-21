package world.mapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class CircleGraph<T> implements Iterable<T> {
	private final List<T> nodes = new ArrayList<>();
	private int position, lastNodeIndex;

	public CircleGraph() {
		super();
		this.position = 0;
		this.lastNodeIndex = 0;
	}

	public CircleGraph(List<T> nodes) {
		super();
		this.nodes.addAll(nodes);
		this.position = 0;
		this.lastNodeIndex = nodes.size() - 1;
	}

	@SafeVarargs
	public CircleGraph(T... nodes) {
		this(Arrays.asList(nodes));
	}

	public void add(T node) {
		if (node == null)
			throw new IllegalArgumentException("node must not be null.");
		nodes.add(node);
		lastNodeIndex++;
	}

	public void replaceCurrent(T node) {
		if (node != null)
			nodes.remove(position);
		add(node);
	}

	public T step() {
		if (position < lastNodeIndex) {
			position++;
		} else {
			position = 0;
		}
		return nodes.get(position);
	}

	public T stepBack() {
		if (position > 0) {
			position--;
		} else {
			position = lastNodeIndex;
		}
		return nodes.get(position);
	}

	public T peakNext() {
		int index;
		if (position < lastNodeIndex) {
			index = position + 1;
		} else {
			index = 0;
		}
		return nodes.get(index);
	}

	public T peakLast() {
		int index;
		if (position > 0) {
			index = position - 1;
		} else {
			index = lastNodeIndex;
		}
		return nodes.get(index);
	}

	public boolean contains(T node) {
		return nodes.contains(node);
	}

	@Override
	public Iterator<T> iterator() {
		return nodes.iterator();
	}

	public boolean isAtStart() {
		return position == 0;
	}

	public T get() {
		return nodes.get(position);
	}

	public List<T> getNodes() {
		return nodes;
	}
}
