package fi.fabianadrian.playerlist.config.liaison;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import space.arim.dazzleconf.LoadResult;
import space.arim.dazzleconf.engine.DeserializeInput;
import space.arim.dazzleconf.engine.SerializeDeserialize;
import space.arim.dazzleconf.engine.SerializeOutput;
import space.arim.dazzleconf.engine.TypeLiaison;
import space.arim.dazzleconf.reflect.TypeToken;

import java.util.regex.Pattern;

public final class PatternLiaison implements TypeLiaison {
	@Override
	public @Nullable <V> Agent<V> makeAgent(@NonNull TypeToken<V> typeToken, @NonNull Handshake handshake) {
		return Agent.matchOnToken(typeToken, Pattern.class, PatternAgent::new);
	}

	private static class PatternAgent implements Agent<Pattern> {
		@Override
		public @NonNull SerializeDeserialize<Pattern> makeSerializer() {
			return new SerializeDeserialize<>() {
				@Override
				public @NonNull LoadResult<Pattern> deserialize(@NonNull DeserializeInput deser) {
					LoadResult<String> result = deser.requireString();
					if (result.isFailure()) {
						return LoadResult.failure(result.getErrorContexts());
					}
					String value = result.getOrThrow();
					return LoadResult.of(Pattern.compile(value));
				}

				@Override
				public void serialize(@NotNull Pattern value, @NonNull SerializeOutput ser) {
					ser.outString(value.pattern());
				}
			};
		}
	}
}
