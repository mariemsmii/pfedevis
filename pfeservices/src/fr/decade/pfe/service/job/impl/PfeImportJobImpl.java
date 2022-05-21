/**
 *
 */
package fr.decade.pfe.service.job.impl;

import de.hybris.platform.cronjob.constants.CronJobConstants;
import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.impex.jalo.ImpExManager;
import de.hybris.platform.impex.jalo.ImpExMedia;
import de.hybris.platform.impex.jalo.cronjob.ImpExImportCronJob;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.util.Config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import fr.decade.pfe.service.job.PfeImportJob;


/**
 * @author mariem
 *
 */
public class PfeImportJobImpl implements PfeImportJob
{
	private static final Logger LOG = Logger.getLogger(PfeImportJobImpl.class);
	private static final String ERROR_MODE = "failed";
	private static final char NEWLINE = '\n';
	private static final String HEADER_SEPARATOR = "|";


	@Override
	public String getImpexParameters(final String csvFileDataPath, final String errorFilePath) throws IOException
	{
		final StringBuilder impexParameters = new StringBuilder();
		impexParameters.append("$csvfilePath=");
		impexParameters.append(csvFileDataPath);
		impexParameters.append(NEWLINE);
		impexParameters.append("$errorCsvFilePath=");
		impexParameters.append(errorFilePath);
		impexParameters.append(NEWLINE);
		return impexParameters.toString();
	}

	@Override
	public ImpExImportCronJob getImportCronJob(final String importProcess, final String cronjobId, final InputStream inputStream)
	{
		final String codeImpex = Config.getString("pfe.impex.code", "utf-8");
		final int nbMaxThreads = Config.getInt("pfe.impex.maxthread.job." + importProcess, Integer.parseInt("4"));
		final boolean isLegacyMode = Config.getBoolean("pfe.impex.islegacymode.job." + importProcess, true);
		final String impexJobQuoteCode = "Quote_" + cronjobId;
		ImpExMedia impexJobQuote = null;
		try
		{
			impexJobQuote = ImpExManager.getInstance().createImpExMedia(impexJobQuoteCode, codeImpex);
			impexJobQuote.setData(inputStream, "impex_" + cronjobId, ImpExConstants.File.MIME_TYPE_IMPEX);
		}
		catch (final UnsupportedEncodingException exception)
		{
			LOG.error("UnsupportedEncodingException: error while creating impex quote");
		}

		final ImpExImportCronJob impexCronJob = ImpExManager.getInstance().createDefaultImpExImportCronJob();
		impexCronJob.setEnableCodeExecution(true);
		if (impexJobQuote != null)
		{
			impexCronJob.setJobMedia(impexJobQuote);
		}
		impexCronJob.setMaxThreads(nbMaxThreads);
		impexCronJob.setLegacyMode(isLegacyMode);
		impexCronJob.setErrorMode(getErrorMode(importProcess));
		try
		{
			impexCronJob.getJob().perform(impexCronJob, true);
		}
		catch (final Exception ex)
		{
			impexCronJob.setResult(EnumerationManager.getInstance().getEnumerationValue(CronJobConstants.TC.CRONJOBRESULT,
					CronJobConstants.Enumerations.CronJobResult.FAILURE));
		}
		return impexCronJob;
	}


	/**
	 * @param importProcess
	 * @return
	 */
	@Override
	public EnumerationValue getErrorMode(final String importProcess)
	{
		EnumerationValue errorMode = EnumerationManager.getInstance().getEnumerationValue(CronJobConstants.TC.ERRORMODE,
				CronJobConstants.Enumerations.ErrorMode.FAIL);

		final String mode = Config.getParameter("pfe.impex.errormode.job." + importProcess);
		if (StringUtils.isNotEmpty(mode) && !StringUtils.equalsIgnoreCase(mode, ERROR_MODE))
		{
			errorMode = EnumerationManager.getInstance().getEnumerationValue(CronJobConstants.TC.ERRORMODE,
					CronJobConstants.Enumerations.ErrorMode.IGNORE);
		}
		return errorMode;
	}






}
