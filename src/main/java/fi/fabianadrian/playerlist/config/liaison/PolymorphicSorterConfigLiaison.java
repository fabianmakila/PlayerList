package fi.fabianadrian.playerlist.config.liaison;

import fi.fabianadrian.playerlist.config.sorter.SorterConfig;
import fi.fabianadrian.playerlist.config.sorter.SorterType;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import space.arim.dazzleconf.DeveloperMistakeException;
import space.arim.dazzleconf.LoadResult;
import space.arim.dazzleconf.backend.DataEntry;
import space.arim.dazzleconf.backend.DataTree;
import space.arim.dazzleconf.engine.*;
import space.arim.dazzleconf.engine.liaison.SubSection;
import space.arim.dazzleconf.reflect.ReifiedAnnotations;
import space.arim.dazzleconf.reflect.ReifiedType;
import space.arim.dazzleconf.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.util.EnumMap;

public final class PolymorphicSorterConfigLiaison implements TypeLiaison {
	private <A extends SorterConfig> SerializeDeserialize<A> getTypeSerializer(Handshake handshake, Class<A> actionClass) {
		// Insert a @SubSection onto the SorterConfig type token, to affirm that it's a configuration interface
		// This forms a generic argument like List<@SubSection PlaceholderSorterConfig>
		ReifiedAnnotations annotations = ReifiedAnnotations.empty().setOne(
				SubSection.class, new SubSection() {
					@Override
					public Class<? extends Annotation> annotationType() {
						return SubSection.class;
					}
				}
		);
		TypeToken<A> token = new TypeToken<>(ReifiedType.of(
				actionClass, annotations
		));
		return handshake.getOtherSerializer(token);
	}

	@Override
	public @Nullable <V> Agent<V> makeAgent(@NonNull TypeToken<V> typeToken, @NonNull Handshake handshake) {
		return Agent.matchOnToken(typeToken, SorterConfig.class, () -> {
			EnumMap<SorterType, SerializeDeserialize<? extends SorterConfig>> typeSerializers = new EnumMap<>(SorterType.class);
			for (SorterType type : SorterType.values()) {
				typeSerializers.put(type, getTypeSerializer(handshake, type.sorterClass()));
			}
			return new AgentImpl(
					handshake.getOtherSerializer(new TypeToken<>() {
					}), typeSerializers
			);
		});
	}

	static final class AgentImpl implements Agent<SorterConfig>, SerializeDeserialize<SorterConfig> {
		private final SerializeDeserialize<SorterType> sorterTypeSerializer;
		private final EnumMap<SorterType, SerializeDeserialize<? extends SorterConfig>> typeSerializers;

		AgentImpl(SerializeDeserialize<SorterType> actionTypeSerializer, EnumMap<SorterType, SerializeDeserialize<? extends SorterConfig>> typeSerializers) {
			this.sorterTypeSerializer = actionTypeSerializer;
			this.typeSerializers = typeSerializers;
		}

		@Override
		public @NonNull SerializeDeserialize<SorterConfig> makeSerializer() {
			return this;
		}

		@Override
		public @NonNull LoadResult<SorterConfig> deserialize(@NonNull DeserializeInput deser) {
			LoadResult<DataTree> dataTreeResult = deser.requireDataTree();
			if (dataTreeResult.isFailure()) {
				return LoadResult.failure(dataTreeResult.getErrorContexts());
			}
			DataTree dataTree = dataTreeResult.getOrThrow();
			DataEntry typeEntry = dataTree.get("type");
			if (typeEntry == null) {
				return deser.throwError("No 'type' specified. SorterConfig must have a type");
			}
			LoadResult<SorterType> typeResult = sorterTypeSerializer.deserialize(
					deser.newInputAt("type", typeEntry)
			);
			if (typeResult.isFailure()) {
				return LoadResult.failure(typeResult.getErrorContexts());
			}
			// Note: We use the same 'deser', this works because the input is shared between 'type' and other fields
			SorterType actionType = typeResult.getOrThrow();
			SerializeDeserialize<? extends SorterConfig> serializer = typeSerializers.get(actionType);
			return serializer.deserialize(deser).map(a -> a);
		}

		@Override
		public void serialize(@NonNull SorterConfig value, @NonNull SerializeOutput ser) {
			SorterType sorterType = SorterType.fromInstance(value);

			SerializeDeserialize<?> rawSerializer = typeSerializers.get(sorterType);
			@SuppressWarnings("unchecked") // no way to get around this
			SerializeDeserialize<SorterConfig> serializer = (SerializeDeserialize<SorterConfig>) rawSerializer;
			serializer.serialize(value, ser);

			Object output = ser.getAndClearLastOutput();
			if (!(output instanceof DataTree)) {
				throw new DeveloperMistakeException("SorterConfig subclass didn't serialize as a tree");
			}
			DataTree.Mut outputTree = ((DataTree) output).intoMut();

			sorterTypeSerializer.serialize(sorterType, ser);
			output = ser.getAndClearLastOutput();
			if (output == null || output == NoOutput.INSTANCE) {
				throw new DeveloperMistakeException("Sorter type should be a scalar");
			}
			DataEntry outputType = new DataEntry(output);
			outputTree.put("type", outputType);
			ser.outDataTree(outputTree);
		}
	}
}
