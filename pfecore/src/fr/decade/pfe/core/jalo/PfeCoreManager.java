/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package fr.decade.pfe.core.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import fr.decade.pfe.core.constants.PfeCoreConstants;
import fr.decade.pfe.core.setup.CoreSystemSetup;


/**
 * Do not use, please use {@link CoreSystemSetup} instead.
 * 
 */
public class PfeCoreManager extends GeneratedPfeCoreManager
{
	public static final PfeCoreManager getInstance()
	{
		final ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (PfeCoreManager) em.getExtension(PfeCoreConstants.EXTENSIONNAME);
	}
}
