/**
 *
 */
package fr.decade.pfe.facade.quote;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.commercefacades.order.impl.DefaultQuoteFacade;
import de.hybris.platform.core.model.order.QuoteModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import fr.decade.pfe.service.quote.impl.PfeDefaultCommerceQuoteService;


/**
 * @author mariem
 *
 */
public class PfeDefaultQuoteFacade extends DefaultQuoteFacade
{
	private static final Logger LOG = Logger.getLogger(PfeDefaultQuoteFacade.class);

	private PfeDefaultCommerceQuoteService pfeDefaultCommerceQuoteService;

	@Resource(name = "userService")
	private UserService userService;
	@Resource(name = "baseStoreService")
	private BaseStoreService baseStoreService;


	public void updateStateToBuyerApproved(final String quoteCode)
	{
		validateParameterNotNullStandardMessage("quoteCode", quoteCode);

		final QuoteModel quoteModel = getQuoteModelForCode(quoteCode);
		final UserModel userModel = userService.getCurrentUser();
		getPfeDefaultCommerceQuoteService().updateStateToBuyerApproved(quoteModel, userModel);
	}

	public void updateStateToBuyerReject(final String quoteCode)
	{
		validateParameterNotNullStandardMessage("quoteCode", quoteCode);

		final QuoteModel quoteModel = getQuoteModelForCode(quoteCode);
		final UserModel userModel = userService.getCurrentUser();
		getPfeDefaultCommerceQuoteService().updateStateToBuyerReject(quoteModel, userModel);
	}

	@Override
	public QuoteModel getQuoteModelForCode(final String quoteCode)
	{
		final UserModel currentUser = userService.getCurrentUser();
		CustomerModel customer = new CustomerModel();
		if (!userService.isAnonymousUser(currentUser))
		{
			customer = (CustomerModel) currentUser;
		}
		final BaseStoreModel currentBaseStore = baseStoreService.getCurrentBaseStore();
		return getPfeDefaultCommerceQuoteService().getQuoteByCodeAndCustomerAndStore(customer, currentUser, currentBaseStore,
				quoteCode);
	}


	/**
	 * @return the pfeDefaultCommerceQuoteService
	 */
	public PfeDefaultCommerceQuoteService getPfeDefaultCommerceQuoteService()
	{
		return pfeDefaultCommerceQuoteService;
	}

	/**
	 * @param pfeDefaultCommerceQuoteService
	 *           the pfeDefaultCommerceQuoteService to set
	 */
	public void setPfeDefaultCommerceQuoteService(final PfeDefaultCommerceQuoteService pfeDefaultCommerceQuoteService)
	{
		this.pfeDefaultCommerceQuoteService = pfeDefaultCommerceQuoteService;
	}




}
