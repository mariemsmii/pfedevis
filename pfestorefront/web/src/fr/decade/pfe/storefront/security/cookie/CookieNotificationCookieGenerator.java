/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package fr.decade.pfe.storefront.security.cookie;


/**
 * CookieGenerator for CookieNotification overlay.
 *
 */
public class CookieNotificationCookieGenerator extends EnhancedCookieGenerator
{
	private static final String COOKIE_NOTIFICATION = "cookie-notification";

	@Override
	public String getCookieName()
	{
		return COOKIE_NOTIFICATION;
	}
}
