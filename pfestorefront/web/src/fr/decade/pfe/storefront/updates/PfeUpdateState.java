/**
 *
 */
package fr.decade.pfe.storefront.updates;

import de.hybris.platform.core.enums.QuoteState;


/**
 * @author mariem
 *
 */
public class PfeUpdateState
{
	private QuoteState state;

	/**
	 * @return the state
	 */
	public QuoteState getState()
	{
		return state;
	}

	/**
	 * @param state
	 *           the state to set
	 */
	public void setState(final QuoteState state)
	{
		this.state = state;
	}


}
