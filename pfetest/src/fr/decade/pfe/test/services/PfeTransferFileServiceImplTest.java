/**
 *
 */
package fr.decade.pfe.test.services;


import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;

import java.io.File;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import fr.decade.pfe.service.sftp.csvFile.PfeTransferFileService;


/**
 * @author mariem
 *
 */
@IntegrationTest
public class PfeTransferFileServiceImplTest extends ServicelayerTransactionalTest
{

	private static final Logger LOG = Logger.getLogger(PfeTransferFileServiceImplTest.class);

	@Resource
	private PfeTransferFileService pfeTransferFileService;


	final String csvFileName = "localCsvQuoteUpload.csv";
	final String username = "erptohost";
	final String password = "erptohost";
	final String remoteHost = "ftpserver";
	final int remotePort = 22;
	final int sessionTimeout = 10000;
	final int channelTimeout = 50000;
	final String localPathdownload = "/home/hybris/bin/platform/../../temp/hybris/quotes";
	final String csvRemoteDir = "/home/erptohost/usersQuotes/quotes";
	final String remoteDirFileUpload = "/home/erptohost/usersQuotes/quotes";
	final String csvLocalFileUpload = "/home/hybris/bin/platform/../../temp/hybris/quotes/remoteCsvQuoteUpload.csv";
	final String csvFileDownloaded = "/home/hybris/bin/platform/../../temp/hybris/quotes/localCsvQuoteUpload.csv";


	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws JSchException
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
	 * {@link fr.decade.pfe.service.sftp.fileService.impl.PfeTransferCsvFileServiceImpl#sftpConnection(java.lang.String, java.lang.String, java.lang.String, int, int, int)}.
	 */
	@Test
	public final void testSftpConnection() throws Exception
	{
		final Channel channel = pfeTransferFileService.sftpConnection(username, password, remoteHost, remotePort, sessionTimeout,
				channelTimeout);
		channel.connect();
		Assert.assertTrue(channel.isConnected());
	}

	/**
	 * Test method for
	 * {@link fr.decade.pfe.service.sftp.fileService.impl.PfeTransferCsvFileServiceImpl#download(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, int, int)}.
	 */
	@Test
	public final void testDownload() throws JSchException, SftpException
	{
		pfeTransferFileService.download(csvRemoteDir, localPathdownload, csvFileName, username, password, remoteHost, remotePort,
				sessionTimeout, channelTimeout);
		final File downloaded = new File(csvFileDownloaded);
		Assert.assertTrue(downloaded.exists());
	}

	/**
	 * Test method for
	 * {@link fr.decade.pfe.service.sftp.fileService.impl.PfeTransferCsvFileServiceImpl#upload(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, int, int)}.
	 */
	@Test
	public final void testUpload() throws JSchException, SftpException
	{
		pfeTransferFileService.upload(csvLocalFileUpload, remoteDirFileUpload, username, password, remoteHost, remotePort,
				sessionTimeout, channelTimeout);
		final Channel channel = pfeTransferFileService.sftpConnection(username, password, remoteHost, remotePort, sessionTimeout,
				channelTimeout);
		channel.connect();
		final ChannelSftp channelSftp = (ChannelSftp) channel;
		try
		{
			channelSftp.cd("usersQuotes/quotes");
		}
		catch (final SftpException sftpException)
		{
			LOG.error("Failed to change the directory in sftp." + sftpException.getMessage());
		}
		final String path = channelSftp.ls("remoteCsvQuoteUpload.csv").toString();
		if (!path.contains("remoteCsvQuoteUpload.csv"))
		{
			LOG.info("File doesn't exist.");

		}
		else
		{
			LOG.info("File already exist.");
			Assert.assertTrue(path.contains("remoteCsvQuoteUpload.csv"));
		}
	}

	/**
	 * @return the pfeTransferFileService
	 */
	public PfeTransferFileService getPfeTransferFileService()
	{
		return pfeTransferFileService;
	}

	/**
	 * @param pfeTransferFileService
	 *           the pfeTransferFileService to set
	 */
	public void setPfeTransferFileService(final PfeTransferFileService pfeTransferFileService)
	{
		this.pfeTransferFileService = pfeTransferFileService;
	}

}
