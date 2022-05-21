/**
 *
 */
package fr.decade.pfe.service.job;

import de.hybris.platform.impex.jalo.cronjob.ImpExImportCronJob;
import de.hybris.platform.jalo.enumeration.EnumerationValue;

import java.io.IOException;
import java.io.InputStream;


/**
 * @author mariem
 *
 */
public interface PfeImportJob
{
	public EnumerationValue getErrorMode(final String importProcess);

	public String getImpexParameters(final String dataFilePath, final String errorFilePath) throws IOException;

	public ImpExImportCronJob getImportCronJob(final String importProcess, final String cronjobId, final InputStream inputStream);

}
