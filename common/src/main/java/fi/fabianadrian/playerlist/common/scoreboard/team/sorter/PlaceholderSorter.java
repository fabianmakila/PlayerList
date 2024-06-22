package fi.fabianadrian.playerlist.common.scoreboard.team.sorter;

import io.github.miniplaceholders.api.MiniPlaceholders;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public final class PlaceholderSorter extends Sorter {
	private final MiniMessage miniMessage = MiniMessage.miniMessage();
	private final PlainTextComponentSerializer plainTextSerializer = PlainTextComponentSerializer.plainText();
	private final String placeholder;

	public PlaceholderSorter(SorterOrder order, String placeholder) {
		super(order);
		this.placeholder = placeholder;
	}

	@Override
	public String output(Audience player) {
		Component parsed = this.miniMessage.deserialize(this.placeholder, MiniPlaceholders.getAudienceGlobalPlaceholders(player));
		String parsedPlainText = this.plainTextSerializer.serialize(parsed);

		if (parsedPlainText.matches("\\d*")) {
			int number = Integer.parseInt(parsedPlainText);

			switch (this.order) {
				case ASCENDING -> {
					return String.format("%03x", this.baseInteger + number);
				}
				case DESCENDING -> {
					return String.format("%03x", this.baseInteger - number);
				}
				default -> throw new IllegalStateException("Unknown order");
			}
		}

		String filtered = parsedPlainText.toLowerCase().replaceAll("[^a-z\\d]", "");
		switch (this.order) {
			case ASCENDING -> {
				return filtered;
			}
			case DESCENDING -> {
				StringBuilder builder = new StringBuilder();

				for (char character : filtered.toCharArray()) {
					char reversed = (char) (219 - character);
					builder.append(reversed);
				}
				return builder.toString();
			}
			default -> throw new IllegalStateException("Unknown order");
		}
	}
}
