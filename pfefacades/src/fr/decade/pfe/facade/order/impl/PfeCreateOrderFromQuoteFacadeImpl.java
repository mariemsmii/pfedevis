/**
 *
 */
package fr.decade.pfe.facade.order.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.core.enums.QuoteState;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.QuoteModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;

import javax.annotation.Resource;

import fr.decade.pfe.facade.order.PfeCreateOrderFromQuoteFacade;
import fr.decade.pfe.facade.quote.PfeDefaultQuoteFacade;
import fr.decade.pfe.service.order.PfeCreateOrderFromQuoteService;


/**
 * @author mariem
 *
 */
public class PfeCreateOrderFromQuoteFacadeImpl implements PfeCreateOrderFromQuoteFacade
{
	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "pfeCreateOrderFromQuoteService")
	private PfeCreateOrderFromQuoteService pfeCreateOrderFromQuoteService;

	@Resource(name = "pfeDefaultQuoteFacade")
	private PfeDefaultQuoteFacade pfeDefaultQuoteFacade;

	@Override
	public void createOrderFromQuote(final String quoteCode)
	{
		validateParameterNotNullStandardMessage("quoteCode", quoteCode);

		final QuoteModel quoteModel = pfeDefaultQuoteFacade.getQuoteModelForCode(quoteCode);
		final UserModel userModel = userService.getCurrentUser();
		final OrderModel orderModel;
		if (quoteModel.getState().equals(QuoteState.BUYER_ORDERED))
		{
			orderModel = pfeCreateOrderFromQuoteService.createOrderFromQuote(quoteModel, userModel);
			pfeCreateOrderFromQuoteService.EntriesFromQuotesToOrder(quoteModel, orderModel);
		}


	}

}
