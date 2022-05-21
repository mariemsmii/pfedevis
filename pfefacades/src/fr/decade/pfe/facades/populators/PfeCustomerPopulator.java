/**
 *
 */
package fr.decade.pfe.facades.populators;

import de.hybris.platform.commercefacades.user.converters.populator.CustomerPopulator;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;


/**
 * @author mariem
 *
 */
public class PfeCustomerPopulator extends CustomerPopulator
{

	private Converter<AddressModel, AddressData> addressConverter;

	@Override
	public void populate(final CustomerModel source, final CustomerData target)
	{
		super.populate(source, target);
		if (source.getDefaultPaymentAddress() != null)
		{
			final AddressData billingAddress = addressConverter.convert(source.getDefaultPaymentAddress());
			if (billingAddress != null)
			{
				target.setDefaultBillingAddress(billingAddress);
			}
		}
	}

	/**
	 * @return the addressConverter
	 */
	public Converter<AddressModel, AddressData> getAddressConverter()
	{
		return addressConverter;
	}

	/**
	 * @param addressConverter
	 *           the addressConverter to set
	 */
	public void setAddressConverter(final Converter<AddressModel, AddressData> addressConverter)
	{
		this.addressConverter = addressConverter;
	}



}
