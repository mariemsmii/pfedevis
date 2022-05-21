/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
package fr.decade.pfe.service.customer.impl;


import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.customer.impl.DefaultCustomerAccountService;
import de.hybris.platform.commerceservices.event.UpdatedProfileEvent;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import fr.decade.pfe.service.customer.PfeCustomerAccountService;


/**
 *
 */
public class PfeCustomerAccountServiceImpl extends DefaultCustomerAccountService implements PfeCustomerAccountService
{
	private static final Logger LOG = Logger.getLogger(DefaultCustomerAccountService.class);

	@Override
	public void updateProfile(final CustomerModel customerModel, final String titleCode, final String name, final String login,
			final String postalCode) throws DuplicateUidException
	{
		validateParameterNotNullStandardMessage("customerModel", customerModel);

		customerModel.setUid(login);
		customerModel.setName(name);
		if (StringUtils.isNotBlank(titleCode))
		{
			customerModel.setTitle(getUserService().getTitleForCode(titleCode));
		}
		else
		{
			customerModel.setTitle(null);
		}
		if (StringUtils.isNotBlank(postalCode))
		{
			try
			{
				AddressModel paymentAddress = customerModel.getDefaultPaymentAddress();
				if (paymentAddress == null)
				{
					paymentAddress = getModelService().create(AddressModel.class);
					paymentAddress.setOwner(customerModel);
				}
				paymentAddress.setPostalcode(postalCode);
				customerModel.setDefaultPaymentAddress(paymentAddress);
				getModelService().save(paymentAddress);
			}
			catch (final Exception e)
			{
				LOG.error("problem in saving customer with postal Code" + postalCode);
			}

			internalSaveCustomer(customerModel);
			getEventService().publishEvent(initializeEvent(new UpdatedProfileEvent(), customerModel));
		}

	}

}
