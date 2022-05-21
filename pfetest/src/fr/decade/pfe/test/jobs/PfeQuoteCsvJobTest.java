/**
 *
 */
package fr.decade.pfe.test.jobs;

import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.order.QuoteModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.opencsv.CSVWriter;

import fr.decade.pfe.facade.jobs.PfeQuoteCsvJob;
import fr.decade.pfe.service.fileService.PfeFileService;
import fr.decade.pfe.service.job.PfeImportJob;
import fr.decade.pfe.service.sftp.csvFile.PfeTransferFileService;


/**
 * @author mariem
 *
 */
@IntegrationTest
public class PfeQuoteCsvJobTest extends ServicelayerTransactionalTest
{

	private static final Logger LOG = Logger.getLogger(PfeQuoteCsvJobTest.class);


	@Resource
	private PfeTransferFileService pfeTransferFileService;
	@Resource
	private PfeImportJob pfeImportJob;

	@Resource
	private PfeFileService pfeFileService;

	@Resource
	private PfeQuoteCsvJob pfeQuoteCsvJob;

	@Resource
	private FlexibleSearchService flexibleSearchService;

	private static final String FIND_QUOTE_BY_CODE_QUERY = "SELECT {quote:" + QuoteModel.PK + "} FROM {" + QuoteModel._TYPECODE
			+ " as quote} WHERE {quote:" + QuoteModel.CODE + "} = ?code";

	final CronJobModel cronJob = new CronJobModel();
	final String createdSuccessQuotesPath = "/home/hybris/bin/platform/../../temp/hybris/quotes/testJobCsvQuotefileSuccess.csv";
	final String failedQuotesPath = "/home/hybris/bin/platform/../../temp/hybris/quotes/testJobCsvQuotefileFailed.csv";
	final String csvFileName = "testJobCsvQuotefile.csv";
	final String username = "erptohost";
	final String password = "erptohost";
	final String remoteHost = "ftpserver";
	final int remotePort = 22;
	final int sessionTimeout = 10000;
	final int channelTimeout = 50000;
	final String localPathdownload = "/home/hybris/bin/platform/../../temp/hybris/quotes";
	final String remoteSuccessQuoteFileUpload = "/home/erptohost/usersQuotes/quotes/testJobCsvQuotefileSuccess.csv";
	final String remoteFailedQuoteFileUpload = "/home/erptohost/usersQuotes/quotes/testJobCsvQuotefileFailed.csv";
	final String successPath = "home/erptohost/usersQuotes/quotes/successQuotes/Success_testJobCsvQuotefileSuccess.csv";
	final String failedPath = "/home/erptohost/usersQuotes/quotes/rejectQuotes/Failed_testJobCsvQuotefileSuccess.csv";
	final String LocalfailedPath = "/home/hybris/bin/platform/../../temp/hybris/quotes/testJobCsvQuotefileFailed_rejected.txt";



	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
	}

	/**
	 * Test method for
	 * {@link fr.decade.pfe.facade.jobs.PfeQuoteCsvJob#perform(de.hybris.platform.cronjob.model.CronJobModel)}.
	 *
	 * @throws JSchException
	 * @throws SftpException
	 * @throws IOException
	 */

	@Test
	public final void testCreateSuccessQuotes() throws JSchException, SftpException, IOException
	{
		final File createdSuccessQuotes = new File(createdSuccessQuotesPath);

		try
		{
			final FileWriter outputSuccessfile = new FileWriter(createdSuccessQuotesPath);

			final CSVWriter writerSuccessfile = new CSVWriter(outputSuccessfile, ';', CSVWriter.NO_QUOTE_CHARACTER,
					CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);

			final List<String[]> successQuotes = new ArrayList<String[]>();
			successQuotes.add(new String[]
			{ "UserId", "QuoteId", "version", "currency", "QuoteName", "Description", "CreationDate", "state", "totalPrice" });
			successQuotes.add(new String[]
			{ "william.hunter@rustic-hw.com", "12325870", "1", "USD", "Quote 10000000", "", "18.04.2022 09:42:50",
					"SELLERAPPROVER_APPROVED", "200000" });
			successQuotes.add(new String[]
			{ "william.hunter@rustic-hw.com", "74125890", "1", "USD", "Quote 11111111", "", "18.04.2022 09:42:50",
					"SELLERAPPROVER_APPROVED", "22222222" });
			writerSuccessfile.writeAll(successQuotes);

			writerSuccessfile.close();
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
		pfeTransferFileService.upload(createdSuccessQuotesPath, remoteSuccessQuoteFileUpload, username, password, remoteHost,
				remotePort, sessionTimeout, channelTimeout);
		createdSuccessQuotes.delete();
		final String code = "pfe:quote,quoteEntries";
		cronJob.setCode(code);
		pfeQuoteCsvJob.perform(cronJob);
		final PerformResult result = pfeQuoteCsvJob.perform(cronJob);
		Assert.assertEquals(CronJobResult.SUCCESS, result.getResult());
		Assert.assertEquals(CronJobStatus.FINISHED, result.getStatus());
		Assert.assertEquals(getQuoteForCode("12325870").getCode(), "12325870");
		Assert.assertEquals(getQuoteForCode("74125890").getCode(), "74125890");
	}

	@Test
	public final void testFailedQuotes() throws JSchException, SftpException, IOException
	{
		final File FailedQuotes = new File(failedQuotesPath);
		try
		{
			final FileWriter outputFailedfile = new FileWriter(failedQuotesPath);

			final CSVWriter writerFailedfile = new CSVWriter(outputFailedfile, ';', CSVWriter.NO_QUOTE_CHARACTER,
					CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);

			final List<String[]> failedQuotes = new ArrayList<String[]>();
			failedQuotes.add(new String[]
			{ "UserId", "QuoteId", "version", "currency", "QuoteName", "Description", "CreationDate", "state", "totalPrice" });
			failedQuotes.add(new String[]
			{ "william.hunter@rustic-hw.com", "00000000", "", "USD", "Quote 00000000", "", "18.04.2022 09:42:50",
					"SELLERAPPROVER_APPROVED", "200000" });
			failedQuotes.add(new String[]
			{ "william.hunter@rustic-hw.com", "111111", "1", "USD", "Quote 11111111", "", "18.04.2022 09:42:50", "SELLERAPPROVER",
					"200000" });
			writerFailedfile.writeAll(failedQuotes);

			writerFailedfile.close();
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
		pfeTransferFileService.upload(failedQuotesPath, remoteFailedQuoteFileUpload, username, password, remoteHost, remotePort,
				sessionTimeout, channelTimeout);
		FailedQuotes.delete();
		final String code = "pfe:quote,quoteEntries";
		cronJob.setCode(code);
		pfeQuoteCsvJob.perform(cronJob);
		final PerformResult result = pfeQuoteCsvJob.perform(cronJob);
		Assert.assertEquals(CronJobResult.ERROR, result.getResult());
		Assert.assertEquals(CronJobStatus.FINISHED, result.getStatus());
		final File localfailed = new File(LocalfailedPath);
		localfailed.getAbsoluteFile();
		assertTrue(localfailed.exists());

	}

	@Test
	public final void testPerformCronJobModel() throws JSchException, SftpException
	{
		try
		{

			final ChannelSftp channelSftp = pfeTransferFileService.sftpConnection(username, password, remoteHost, remotePort,
					sessionTimeout, channelTimeout);
			channelSftp.connect();
			try
			{
				channelSftp.cd("usersQuotes/quotes/successQuotes");
			}
			catch (final SftpException sftpException)
			{
				LOG.error("Failed to change the directory in sftp." + sftpException.getMessage());
			}
			final String path = channelSftp.ls("Success_testJobCsvQuotefileSuccess.csv").toString();
			if (!path.contains("Success_testJobCsvQuotefileSuccess.csv"))
			{
				LOG.info("File doesn't exist.");

			}
			else
			{
				LOG.info("File already exist.");
				Assert.assertTrue(path.contains("Success_testJobCsvQuotefileSuccess.csv"));
			}
			try
			{
				channelSftp.cd("usersQuotes/quotes/rejectQuotes");
			}
			catch (final SftpException sftpException)
			{
				LOG.error("Failed to change the directory in sftp." + sftpException.getMessage());
			}
			final String rejectpath = channelSftp.ls("Success_testJobCsvQuotefileSuccess.csv").toString();
			if (!rejectpath.contains("Failed_testJobCsvQuotefileFailed.csv"))
			{
				LOG.info("File doesn't exist.");

			}
			else
			{
				LOG.info("File already exist.");
				Assert.assertTrue(path.contains("Failed_testJobCsvQuotefileFailed.csv"));
			}


		}
		catch (final Exception e)
		{
			LOG.error("Failed to connect to SFTP server." + e.getMessage());
			e.printStackTrace();
		}
	}

	public QuoteModel getQuoteForCode(final String code)
	{
		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(FIND_QUOTE_BY_CODE_QUERY);
		searchQuery.addQueryParameter("code", code);
		searchQuery.setCount(1);
		return flexibleSearchService.searchUnique(searchQuery);
	}

}
