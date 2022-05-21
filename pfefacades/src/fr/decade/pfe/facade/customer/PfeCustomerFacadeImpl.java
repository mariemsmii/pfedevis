/**
 *
 */
package fr.decade.pfe.facade.customer;

import de.hybris.platform.commercefacades.customer.impl.DefaultCustomerFacade;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.core.model.user.CustomerModel;

import javax.annotation.Resource;

import fr.decade.pfe.service.customer.PfeCustomerAccountService;


/**
 * @author mariem
 *
 */
public class PfeCustomerFacadeImpl extends DefaultCustomerFacade
{
	@Resource(name = "customerAccountService")
	private PfeCustomerAccountService customerAccountService;



	@Override
	public void updateProfile(final CustomerData customerData) throws DuplicateUidException
	{

		validateDataBeforeUpdate(customerData);
		String postalCode = null;

		final String name = getCustomerNameStrategy().getName(customerData.getFirstName(), customerData.getLastName());
		final CustomerModel customer = getCurrentSessionCustomer();
		customer.setOriginalUid(customerData.getDisplayUid());
		final AddressData billingAddress = customerData.getDefaultBillingAddress();
		if (billingAddress != null)
		{
			postalCode = billingAddress.getPostalCode();
		}
		customerAccountService.updateProfile(customer, customerData.getTitleCode(), name, customerData.getUid(), postalCode);

	}
}
