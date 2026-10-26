package com.kukzze.grotesquesolar;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DiscordWebhookServiceTest
{
	@Mock
	private OkHttpClient okHttpClient;

	@Mock
	private GrotesqueSolarConfig config;

	private DiscordWebhookService service;

	@BeforeEach
	void setUp()
	{
		service = new DiscordWebhookService(okHttpClient, new Gson(), config);
	}

	@Test
	void testValidWebhookUrls()
	{
		assertTrue(service.isValidWebhookUrl("https://discord.com/api/webhooks/123456789/token_abc_123"));
		assertTrue(service.isValidWebhookUrl("https://discordapp.com/api/webhooks/987654321/token_xyz_456"));
	}

	@Test
	void testInvalidWebhookUrls()
	{
		assertFalse(service.isValidWebhookUrl(""));
		assertFalse(service.isValidWebhookUrl("https://google.com/search?q=test"));
		assertFalse(service.isValidWebhookUrl("https://discord.com/channels/@me/123"));
		assertFalse(service.isValidWebhookUrl("not-a-valid-url"));
	}
}
