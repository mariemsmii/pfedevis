/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
package fr.decade.pfe.service.quote.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.commerceservices.enums.QuoteAction;
import de.hybris.platform.commerceservices.order.CommerceQuoteService;
import de.hybris.platform.commerceservices.order.impl.DefaultCommerceQuoteService;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.QuoteState;
import de.hybris.platform.core.model.order.QuoteModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.store.BaseStoreModel;

import java.util.HashSet;
import java.util.Set;


/**
 *
 */
public class PfeDefaultCommerceQuoteService extends DefaultCommerceQuoteService implements CommerceQuoteService
{
	@Override
	public QuoteModel getQuoteByCodeAndCustomerAndStore(final CustomerModel customerModel, final UserModel quoteUserModel,
			final BaseStoreModel store, final String quoteCode)
	{

		validateParameterNotNullStandardMessage("customerModel", customerModel);
		validateParameterNotNullStandardMessage("quoteUserModel", quoteUserModel);
		validateParameterNotNullStandardMessage("quoteCode", quoteCode);
		validateParameterNotNullStandardMessage("store", store);
		final Set<QuoteState> quoteStates = new HashSet<QuoteState>();
		quoteStates.add(QuoteState.BUYER_APPROVED);
		quoteStates.add(QuoteState.CREATED);
		quoteStates.add(QuoteState.BUYER_REJECTED);
		quoteStates.add(QuoteState.SUBMITTED);
		quoteStates.add(QuoteState.SELLERAPPROVER_APPROVED);
		quoteStates.add(QuoteState.SELLERAPPROVER_REJECTED);
		quoteStates.add(QuoteState.BUYER_SUBMITTED);
		quoteStates.add(QuoteState.BUYER_DRAFT);
		quoteStates.add(QuoteState.BUYER_ORDERED);


		return getCommerceQuoteDao().findUniqueQuoteByCodeAndCustomerAndStore(customerModel, store, quoteCode, quoteStates);
	}

	@Override
	public SearchPageData<QuoteModel> getQuoteList(final CustomerModel customerModel, final UserModel quoteUserModel,
			final BaseStoreModel store, final PageableData pageableData)
	{
		validateParameterNotNullStandardMessage("customerModel", customerModel);
		validateParameterNotNullStandardMessage("quoteUserModel", quoteUserModel);
		validateParameterNotNullStandardMessage("store", store);
		validateParameterNotNullStandardMessage("pageableData", pageableData);
		final Set<QuoteState> quoteStates = new HashSet<QuoteState>();
		quoteStates.add(QuoteState.BUYER_APPROVED);
		quoteStates.add(QuoteState.CREATED);
		quoteStates.add(QuoteState.BUYER_REJECTED);
		quoteStates.add(QuoteState.SUBMITTED);
		quoteStates.add(QuoteState.SELLERAPPROVER_APPROVED);
		quoteStates.add(QuoteState.SELLERAPPROVER_REJECTED);
		quoteStates.add(QuoteState.BUYER_SUBMITTED);
		quoteStates.add(QuoteState.BUYER_DRAFT);
		quoteStates.add(QuoteState.BUYER_ORDERED);
		return getCommerceQuoteDao().findQuotesByCustomerAndStore(customerModel, store, pageableData, quoteStates);
	}

	public QuoteModel updateStateToBuyerApproved(final QuoteModel quoteModel, final UserModel userModel)
	{
		validateParameterNotNullStandardMessage("quoteModel", quoteModel);
		validateParameterNotNullStandardMessage("userModel", userModel);

		final QuoteModel updatedQuoteModel = getQuoteUpdateStateStrategy().updateQuoteState(QuoteAction.APPROVE, quoteModel,
				userModel);
		validateQuoteTotal(updatedQuoteModel);
		getQuoteMetadataValidationStrategy().validate(QuoteAction.SUBMIT, updatedQuoteModel, userModel);

		if (quoteModel.getState().equals(QuoteState.SELLERAPPROVER_APPROVED))
		{
			quoteModel.setState(QuoteState.BUYER_ORDERED);
		}
		getModelService().save(updatedQuoteModel);
		getModelService().refresh(updatedQuoteModel);
		return updatedQuoteModel;

	}

	public QuoteModel updateStateToBuyerReject(final QuoteModel quoteModel, final UserModel userModel)
	{
		validateParameterNotNullStandardMessage("quoteModel", quoteModel);
		validateParameterNotNullStandardMessage("userModel", userModel);

		final Set<QuoteState> quoteStates = new HashSet<QuoteState>();
		quoteStates.add(QuoteState.BUYER_APPROVED);
		quoteStates.add(QuoteState.CREATED);
		quoteStates.add(QuoteState.SUBMITTED);
		quoteStates.add(QuoteState.SELLERAPPROVER_APPROVED);
		quoteStates.add(QuoteState.BUYER_SUBMITTED);
		quoteStates.add(QuoteState.BUYER_DRAFT);
		quoteStates.add(QuoteState.BUYER_ORDERED);

		final QuoteModel updatedQuoteModel = getQuoteUpdateStateStrategy().updateQuoteState(QuoteAction.APPROVE, quoteModel,
				userModel);
		validateQuoteTotal(updatedQuoteModel);
		getQuoteMetadataValidationStrategy().validate(QuoteAction.SUBMIT, updatedQuoteModel, userModel);
		if (quoteStates.contains(quoteModel.getState()))
		{
			quoteModel.setState(QuoteState.BUYER_REJECTED);
		}
		getModelService().save(updatedQuoteModel);
		getModelService().refresh(updatedQuoteModel);
		return updatedQuoteModel;

	}

}
