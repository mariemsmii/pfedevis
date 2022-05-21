/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package fr.decade.pfe.fulfilmentprocess.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import fr.decade.pfe.fulfilmentprocess.constants.PfeFulfilmentProcessConstants;

public class PfeFulfilmentProcessManager extends GeneratedPfeFulfilmentProcessManager
{
	public static final PfeFulfilmentProcessManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (PfeFulfilmentProcessManager) em.getExtension(PfeFulfilmentProcessConstants.EXTENSIONNAME);
	}
	
}
