/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
package fr.decade.pfe.service.order;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.QuoteModel;
import de.hybris.platform.core.model.user.UserModel;


/**
 *
 */
public interface PfeCreateOrderFromQuoteService
{
	OrderModel createOrderFromQuote(QuoteModel quoteReference, UserModel user);

}
