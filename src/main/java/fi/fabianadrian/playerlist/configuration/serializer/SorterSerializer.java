package fi.fabianadrian.playerlist.configuration.serializer;

import fi.fabianadrian.playerlist.PlayerList;
import fi.fabianadrian.playerlist.list.sorting.*;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.Arrays;

public final class SorterSerializer implements TypeSerializer<Sorter> {
	private static final String TYPE = "type";
	private static final String ORDER = "order";
	private static final String CRITERIA = "criteria";
	private static final String PLACEHOLDER = "placeholder";
	private static final String CASE_SENSITIVE = "case-sensitive";
	private final PlayerList plugin;

	public SorterSerializer(PlayerList plugin) {
		this.plugin = plugin;
	}

	@Override
	public Sorter deserialize(Type type, ConfigurationNode node) throws SerializationException {
		final ConfigurationNode typeNode = nonVirtualNode(node, TYPE);
		final SorterType sorterType = typeNode.get(SorterType.class);
		final ConfigurationNode orderNode = nonVirtualNode(node, ORDER);
		final SortingOrder order = orderNode.get(SortingOrder.class);

		switch (sorterType) {
			case LUCKPERMS -> {
				final ConfigurationNode criteriaNode = nonVirtualNode(node, CRITERIA);
				final LuckPermsSorter.Criteria criteria = criteriaNode.get(LuckPermsSorter.Criteria.class);

				return new LuckPermsSorter(order, criteria);
			}
			case PLACEHOLDER -> {
				final ConfigurationNode placeholderNode = nonVirtualNode(node, PLACEHOLDER);
				final String placeholder = placeholderNode.getString();
				final Boolean caseSensitive = node.node(CASE_SENSITIVE).getBoolean(true);

				return new PlaceholderSorter(this.plugin, order, placeholder, caseSensitive);
			}
			default -> throw new IllegalStateException("Unknown sorter type");
		}
	}

	@Override
	public void serialize(Type type, @Nullable Sorter sorter, ConfigurationNode node) throws SerializationException {
		if (sorter == null) {
			node.raw(null);
			return;
		}

		node.node(TYPE).set(sorter.type());
		node.node(ORDER).set(sorter.order());

		if (sorter instanceof LuckPermsSorter luckPermsSorter) {
			node.node(CRITERIA).set(luckPermsSorter.criteria());
		} else if (sorter instanceof PlaceholderSorter placeholderSorter) {
			node.node(PLACEHOLDER).set(placeholderSorter.placeholder());
			node.node(CASE_SENSITIVE).set(placeholderSorter.caseSensitive());
		}
	}

	private ConfigurationNode nonVirtualNode(final ConfigurationNode source, final Object... path) throws SerializationException {
		if (!source.hasChild(path)) {
			throw new SerializationException("Required field " + Arrays.toString(path) + " was not present in node");
		}
		return source.node(path);
	}
}
