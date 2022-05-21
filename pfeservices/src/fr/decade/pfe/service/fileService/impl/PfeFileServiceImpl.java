/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
package fr.decade.pfe.service.fileService.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import fr.decade.pfe.service.fileService.PfeFileService;


/**
 *
 */
public class PfeFileServiceImpl implements PfeFileService
{
	private static final Logger LOG = Logger.getLogger(PfeFileServiceImpl.class);

	@Override
	public File createErrorFile(final String error, final String eurorFilePath)
	{
		File resultFile = null;
		try
		{
			final File errorFile = new File(eurorFilePath);
			final FileWriter fw = new FileWriter(errorFile.getAbsoluteFile());
			final BufferedWriter bw = new BufferedWriter(fw);
			if (!errorFile.exists())
			{
				errorFile.createNewFile();
			}
			bw.write(error);
			bw.close();
			resultFile = errorFile;
		}
		catch (final Exception e)
		{
			LOG.error("Failed to create error file ", e);
		}
		return resultFile;
	}

	@Override
	public boolean mergeErrorFiles(final File unresolvedDataFile, final File rejectFile)
	{
		boolean result = false;
		try
		{
			final FileWriter filestream = new FileWriter(rejectFile, true);
			final BufferedWriter out = new BufferedWriter(filestream);
			final FileInputStream fis = new FileInputStream(unresolvedDataFile);
			final BufferedReader in = new BufferedReader(new InputStreamReader(fis));

			String line;
			while ((line = in.readLine()) != null)
			{
				out.write(line);
				out.newLine();
			}

			in.close();
			out.close();
			result = true;
		}
		catch (final Exception e)
		{
			LOG.error("Failed to merge error files ", e);
		}
		return result;

	}

}
