/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
package fr.decade.pfe.service.customer;

import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.core.model.user.CustomerModel;


/**
 *
 */
public interface PfeCustomerAccountService extends CustomerAccountService
{

	void updateProfile(CustomerModel customerModel, String titleCode, String name, String login, String postalCode)
			throws DuplicateUidException;

}
