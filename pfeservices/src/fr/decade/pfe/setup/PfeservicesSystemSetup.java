/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package fr.decade.pfe.setup;

import static fr.decade.pfe.constants.PfeservicesConstants.PLATFORM_LOGO_CODE;

import de.hybris.platform.core.initialization.SystemSetup;

import java.io.InputStream;

import fr.decade.pfe.constants.PfeservicesConstants;
import fr.decade.pfe.service.PfeservicesService;


@SystemSetup(extension = PfeservicesConstants.EXTENSIONNAME)
public class PfeservicesSystemSetup
{
	private final PfeservicesService pfeservicesService;

	public PfeservicesSystemSetup(final PfeservicesService pfeservicesService)
	{
		this.pfeservicesService = pfeservicesService;
	}

	@SystemSetup(process = SystemSetup.Process.INIT, type = SystemSetup.Type.ESSENTIAL)
	public void createEssentialData()
	{
		pfeservicesService.createLogo(PLATFORM_LOGO_CODE);
	}

	private InputStream getImageStream()
	{
		return PfeservicesSystemSetup.class.getResourceAsStream("/pfeservices/sap-hybris-platform.png");
	}
}
