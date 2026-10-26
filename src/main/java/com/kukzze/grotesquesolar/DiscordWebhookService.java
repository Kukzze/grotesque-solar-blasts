package com.kukzze.grotesquesolar;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Slf4j
@Singleton
public class DiscordWebhookService
{
	private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	private static final int EMBED_COLOR = 16753920; // Radiant solar gold

	private final OkHttpClient okHttpClient;
	private final Gson gson;
	private final GrotesqueSolarConfig config;

	@Inject
	public DiscordWebhookService(OkHttpClient okHttpClient, Gson gson, GrotesqueSolarConfig config)
	{
		this.okHttpClient = okHttpClient;
		this.gson = gson;
		this.config = config;
	}

	public void sendKillNotification(String playerName, String killCount, String duration, boolean isPersonalBest)
	{
		String webhookUrl = config.discordWebhook().trim();
		if (webhookUrl.isEmpty() || !isValidWebhookUrl(webhookUrl))
		{
			return;
		}

		JsonObject payload = new JsonObject();
		payload.addProperty("username", "Grotesque Solar Blasts");
		payload.addProperty("avatar_url", "https://oldschool.runescape.wiki/images/Smol_Heredit.png");

		JsonArray embeds = new JsonArray();
		JsonObject embed = new JsonObject();

		String title = isPersonalBest
			? "Grotesque Guardians Defeated - New Personal Best! \u2600\uFE0F"
			: "Grotesque Guardians Defeated! \u2600\uFE0F";
		embed.addProperty("title", title);

		String description = String.format("**%s** crushed Dusk and Dawn with **%s** active.",
			playerName, config.beamStyle().getName());
		embed.addProperty("description", description);
		embed.addProperty("color", EMBED_COLOR);

		JsonArray fields = new JsonArray();

		JsonObject kcField = new JsonObject();
		kcField.addProperty("name", "Kill Count");
		kcField.addProperty("value", killCount);
		kcField.addProperty("inline", true);
		fields.add(kcField);

		if (duration != null && !duration.isEmpty())
		{
			JsonObject timeField = new JsonObject();
			timeField.addProperty("name", isPersonalBest ? "New PB Time" : "Kill Duration");
			timeField.addProperty("value", duration);
			timeField.addProperty("inline", true);
			fields.add(timeField);
		}

		JsonObject styleField = new JsonObject();
		styleField.addProperty("name", "Solar Style");
		styleField.addProperty("value", config.beamStyle().getName());
		styleField.addProperty("inline", true);
		fields.add(styleField);

		embed.add("fields", fields);

		JsonObject footer = new JsonObject();
		footer.addProperty("text", "Grotesque Solar Blasts \u2022 RuneLite");
		embed.add("footer", footer);

		embeds.add(embed);
		payload.add("embeds", embeds);

		RequestBody body = RequestBody.create(JSON, gson.toJson(payload));
		Request request = new Request.Builder()
			.url(webhookUrl)
			.post(body)
			.build();

		okHttpClient.newCall(request).enqueue(new Callback()
		{
			@Override
			public void onFailure(Call call, IOException e)
			{
				log.warn("Failed to dispatch Discord webhook notification: {}", e.getMessage());
			}

			@Override
			public void onResponse(Call call, Response response)
			{
				response.close();
				if (!response.isSuccessful())
				{
					log.warn("Discord webhook returned status code: {}", response.code());
				}
				else
				{
					log.debug("Successfully posted Grotesque Guardians kill to Discord");
				}
			}
		});
	}

	public boolean isValidWebhookUrl(String url)
	{
		HttpUrl parsed = HttpUrl.parse(url);
		if (parsed == null)
		{
			return false;
		}

		String host = parsed.host();
		boolean validHost = host.equalsIgnoreCase("discord.com")
			|| host.equalsIgnoreCase("discordapp.com");

		return validHost && parsed.encodedPath().startsWith("/api/webhooks/");
	}
}
