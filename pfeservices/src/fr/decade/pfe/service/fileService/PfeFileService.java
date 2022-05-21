/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
package fr.decade.pfe.service.fileService;

import java.io.File;


/**
 *
 */
public interface PfeFileService
{
	public File createErrorFile(final String error, final String eurorCsvFilePath);

	public boolean mergeErrorFiles(final File unresolvedDataFile, final File rejectFile);

}
