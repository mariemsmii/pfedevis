/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
package fr.decade.pfe.service.order.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.QuoteModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.List;

import fr.decade.pfe.service.order.PfeCreateOrderFromQuoteService;


/**
 *
 */
public class PfeCreateOrderFromQuoteServiceImpl implements PfeCreateOrderFromQuoteService
{
	private ModelService modelService;
	private TimeService timeService;
	private BaseSiteService baseSiteService;
	private BaseStoreService baseStoreService;
	private CommonI18NService commonI18NService;


	@Override
	public OrderModel createOrderFromQuote(final QuoteModel quoteModel, final UserModel user)
	{
		validateParameterNotNullStandardMessage("quoteModel", quoteModel);
		validateParameterNotNullStandardMessage("userModel", user);
		final OrderModel orderModel = modelService.create(OrderModel.class);
		orderModel.setUser(quoteModel.getUser());
		orderModel.setCode(quoteModel.getCode());
		orderModel.setCurrency(quoteModel.getCurrency());
		orderModel.setDate(timeService.getCurrentTime());
		orderModel.setQuoteReference(quoteModel);
		orderModel.setStatus(OrderStatus.CREATED);
		orderModel.setEntries(getEntriesFromQuotesToOrder(quoteModel, orderModel));
		orderModel.setSite(getBaseSiteService().getCurrentBaseSite());
		orderModel.setStore(getBaseStoreService().getCurrentBaseStore());
		orderModel.setLanguage(getCommonI18NService().getCurrentLanguage());
		modelService.save(orderModel);
		return orderModel;
	}

	private List<AbstractOrderEntryModel> getEntriesFromQuotesToOrder(final QuoteModel quoteModel, final OrderModel orderModel)
	{
		final List<AbstractOrderEntryModel> entries = quoteModel.getEntries();
		for (int i = 0; i < entries.size(); i++)
		{
			orderModel.setEntries(entries);
		}
		return null;
	}


	/**
	 * @return the baseSiteService
	 */
	public BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	/**
	 * @param baseSiteService
	 *           the baseSiteService to set
	 */
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	/**
	 * @return the baseStoreService
	 */
	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	/**
	 * @param baseStoreService
	 *           the baseStoreService to set
	 */
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	/**
	 * @return the commonI18NService
	 */
	public CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	/**
	 * @param commonI18NService
	 *           the commonI18NService to set
	 */
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * @return the timeService
	 */
	public TimeService getTimeService()
	{
		return timeService;
	}

	/**
	 * @param timeService
	 *           the timeService to set
	 */
	public void setTimeService(final TimeService timeService)
	{
		this.timeService = timeService;
	}

}
