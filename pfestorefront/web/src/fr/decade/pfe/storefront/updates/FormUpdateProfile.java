/**
 *
 */
package fr.decade.pfe.storefront.updates;

import de.hybris.platform.acceleratorstorefrontcommons.forms.UpdateProfileForm;


/**
 * @author mariem
 *
 */
public class FormUpdateProfile extends UpdateProfileForm
{
	private String postalCode;

	/**
	 * @return the postalCode
	 */
	public String getPostalCode()
	{
		return postalCode;
	}

	/**
	 * @param string
	 *           the postalCode to set
	 */
	public void setPostalCode(final String string)
	{
		this.postalCode = string;
	}

}
