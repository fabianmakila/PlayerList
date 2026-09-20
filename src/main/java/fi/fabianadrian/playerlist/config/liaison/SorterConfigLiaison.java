package fi.fabianadrian.playerlist.config.liaison;

import fi.fabianadrian.playerlist.config.sorter.SorterConfig;
import fi.fabianadrian.playerlist.config.sorter.SorterType;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import space.arim.dazzleconf.ConfigurationDefinition;
import space.arim.dazzleconf.LoadResult;
import space.arim.dazzleconf.backend.DataEntry;
import space.arim.dazzleconf.backend.DataTree;
import space.arim.dazzleconf.engine.DeserializeInput;
import space.arim.dazzleconf.engine.SerializeDeserialize;
import space.arim.dazzleconf.engine.SerializeOutput;
import space.arim.dazzleconf.engine.TypeLiaison;
import space.arim.dazzleconf.reflect.TypeToken;

import java.util.Arrays;
import java.util.Locale;

public final class SorterConfigLiaison implements TypeLiaison {
	@Override
	public <V> @Nullable Agent<V> makeAgent(@NonNull TypeToken<V> typeToken, @NonNull Handshake handshake) {
		return Agent.matchOnToken(typeToken, SorterConfig.class, () -> new SorterConfigAgent(handshake));
	}

	private record SorterConfigAgent(Handshake handshake) implements Agent<SorterConfig> {

		@Override
		public @NonNull SerializeDeserialize<SorterConfig> makeSerializer() {
			return new SerializeDeserialize<>() {

				@Override
				public @NonNull LoadResult<SorterConfig> deserialize(@NonNull DeserializeInput deser) {
					LoadResult<DataTree> treeResult = deser.requireDataTree();
					if (treeResult.isFailure()) {
						return LoadResult.failure(treeResult.getErrorContexts());
					}
					DataTree tree = treeResult.getOrThrow();

					DataEntry typeEntry = tree.get("type");
					if (typeEntry == null) {
						return deser.throwError("Sorter entry is missing a 'type' key");
					}

					DeserializeInput typeInput = deser.newInputAt("type", typeEntry);
					LoadResult<String> typeResult = typeInput.requireString();
					if (typeResult.isFailure()) {
						return LoadResult.failure(typeResult.getErrorContexts());
					}

					String rawType = typeResult.getOrThrow();
					SorterType type;
					try {
						type = SorterType.valueOf(rawType.toUpperCase(Locale.ROOT));
					} catch (IllegalArgumentException ex) {
						return deser.throwError(
								"Unknown sorter type '" + rawType + "'. Valid: " + Arrays.toString(SorterType.values()));
					}
					return deserializeAs(type.typeToken(), tree, deser);
				}

				private <C extends SorterConfig> LoadResult<SorterConfig> deserializeAs(
						TypeToken<C> token, DataTree tree, DeserializeInput deser) {
					ConfigurationDefinition<C> definition = handshake.getConfiguration(token);
					LoadResult<C> result = definition.readFrom(tree, deser);
					return result.map(c -> c);
				}

				@Override
				public void serialize(@NonNull SorterConfig value, @NonNull SerializeOutput ser) {
					serializeAs(value.type().typeToken(), value, ser);
				}

				private <C extends SorterConfig> void serializeAs(
						TypeToken<C> token, SorterConfig value, SerializeOutput ser) {
					ConfigurationDefinition<C> definition = handshake.getConfiguration(token);
					C casted = token.cast(value);
					DataTree.Mut tree = new DataTree.Mut();
					definition.writeTo(casted, tree, ser);
					ser.outDataTree(tree);
				}
			};
		}
	}
}
