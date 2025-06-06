package fi.fabianadrian.playerlist.locale;

import fi.fabianadrian.playerlist.PlayerList;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.minimessage.translation.MiniMessageTranslationStore;
import net.kyori.adventure.translation.GlobalTranslator;
import org.slf4j.Logger;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public final class TranslationManager {
	public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;
	public static final List<Locale> BUNDLED_LOCALES = List.of(DEFAULT_LOCALE, Locale.of("fi", "FI"));
	private final Logger logger;
	private final MiniMessageTranslationStore store;

	public TranslationManager(PlayerList playerList) {
		this.logger = playerList.getSLF4JLogger();

		this.store = MiniMessageTranslationStore.create(Key.key("playerlist", "main"));
		this.store.defaultLocale(DEFAULT_LOCALE);

		loadFromResourceBundle();
		GlobalTranslator.translator().addSource(this.store);
	}

	private void loadFromResourceBundle() {
		try {
			BUNDLED_LOCALES.forEach(locale -> {
				ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
				this.store.registerAll(locale, bundle, false);
			});
		} catch (IllegalArgumentException e) {
			this.logger.warn("Error loading default locale file", e);
		}
	}
}
