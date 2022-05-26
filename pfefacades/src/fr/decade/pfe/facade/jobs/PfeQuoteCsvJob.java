/**
 *
 */
package fr.decade.pfe.facade.jobs;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.impex.jalo.ImpExMedia;
import de.hybris.platform.impex.jalo.cronjob.ImpExImportCronJob;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.util.Config;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import fr.decade.pfe.service.fileService.PfeFileService;
import fr.decade.pfe.service.job.PfeImportJob;
import fr.decade.pfe.service.sftp.csvFile.PfeTransferFileService;


/**
 * @author mariem
 *
 */
public class PfeQuoteCsvJob extends AbstractJobPerformable<CronJobModel>
{
	private static final Logger LOG = Logger.getLogger(PfeQuoteCsvJob.class);
	private static final String FIRSTSEPARATOR = ":";
	private static final String SECONDSEPARATOR = ",";

	private PfeTransferFileService pfeTransferFileService;

	private PfeImportJob pfeImportJob;

	private PfeFileService pfeFileService;

	@Override
	public PerformResult perform(final CronJobModel cronJob)
	{
		final List<File> rejectedFiles = new ArrayList<File>();
		CronJobResult jobResult = CronJobResult.SUCCESS;
		CronJobStatus jobStatus = CronJobStatus.FINISHED;
		CronJobResult impexCronJobResult;
		CronJobStatus impexCronJobStatus;
		String importProcess;
		String cronJobId;
		String csvFileDataPath;
		String rejectedDataFileName;
		String errorFilePath;
		File unresolvedDataFile;
		List<String> downloadFiles;
		String localPath;
		String csvFileName;
		String successFileReturn;
		String failedFileReturn;
		String impexfolder;
		String impexFileName;
		String csvRemotDir;
		String errorRemotDir;
		String extension;
		String username;
		String password;
		String remoteHost;
		String csvRejectDir;
		String csvSuccessDir;
		String code;
		int remotePort;
		int sessionTimeout;
		int channelTimeout;
		int i = 0;
		InputStream inputStream;
		ImpExImportCronJob impexCronJob;
		File rejectFile;
		ImpExMedia unresolvedData;
		boolean result;
		code = cronJob.getCode();
		if (StringUtils.isBlank(code))
		{
			LOG.error("The code of this job is Empty");
			jobResult = CronJobResult.ERROR;
			jobStatus = CronJobStatus.ABORTED;
		}
		else
		{
			final String[] codeElements = StringUtils.split(code, FIRSTSEPARATOR);
			if (codeElements == null)
			{
				LOG.error("The code of this job has not a processes");
				jobResult = CronJobResult.ERROR;
				jobStatus = CronJobStatus.ABORTED;
			}
			if (codeElements.length != 2)
			{
				LOG.error("The code of this job entered incorrectly");
				jobResult = CronJobResult.ERROR;
				jobStatus = CronJobStatus.ABORTED;
			}
			else
			{
				if (codeElements.length > 1)
				{
					final String[] processes = StringUtils.split(codeElements[1], SECONDSEPARATOR);
					if (processes == null)
					{
						LOG.error("The code of this job entered incorrectly");
						jobResult = CronJobResult.ERROR;
						jobStatus = CronJobStatus.ABORTED;
					}
					for (i = 0; i < processes.length; i++)
					{
						importProcess = processes[i];
						cronJobId = importProcess;
						localPath = Config.getString("pfe.hybris.temp.dir.csv." + importProcess, StringUtils.EMPTY);
						csvFileName = Config.getString("pfe.file.name." + importProcess, StringUtils.EMPTY);
						successFileReturn = Config.getString("pfe.impex.success.return.file." + importProcess, "Success");
						failedFileReturn = Config.getString("pfe.impex.failed.return.file." + importProcess, "Failed");
						impexfolder = Config.getString("pfe.impex.folder.name." + importProcess, StringUtils.EMPTY);
						impexFileName = Config.getString("pfe.impex.file.name." + importProcess, StringUtils.EMPTY);
						csvRemotDir = Config.getString("pfe.hybris.sftp.dir.csv." + importProcess, StringUtils.EMPTY);
						errorRemotDir = Config.getString("pfe.hybris.sftp.error.dir.csv." + importProcess, StringUtils.EMPTY);
						extension = Config.getString("pfe.extension.file.csv." + importProcess, StringUtils.EMPTY);
						username = Config.getString("pfe.username." + importProcess, StringUtils.EMPTY);
						password = Config.getString("pfe.password." + importProcess, StringUtils.EMPTY);
						remoteHost = Config.getString("pfe.remote.host." + importProcess, StringUtils.EMPTY);
						remotePort = Config.getInt("pfe.remote.port." + importProcess, 22);
						sessionTimeout = Config.getInt("pfe.session.timeout." + importProcess, 10000);
						channelTimeout = Config.getInt("pfe.channel.timeout." + importProcess, 50000);
						csvRejectDir = Config.getString("pfe.hybris.sftp.reject.dir." + importProcess, StringUtils.EMPTY);
						csvSuccessDir = Config.getString("pfe.hybris.sftp.success.dir.csv." + importProcess, StringUtils.EMPTY);



						if (StringUtils.isBlank(csvRemotDir) || StringUtils.isBlank(localPath))
						{
							LOG.error("No such file in local directory or relote directory " + importProcess);
						}
						else
						{
							LOG.info("Start importing CSV quotes from SFTP to " + importProcess);

							downloadFiles = pfeTransferFileService.download(csvRemotDir, localPath, csvFileName, username, password,
									remoteHost, remotePort, sessionTimeout, channelTimeout);
							if (downloadFiles == null || downloadFiles.isEmpty())
							{
								LOG.error("There is no file in SFTP server to download " + importProcess);
							}
							else
							{
								inputStream = getClass().getResourceAsStream(impexFileName);
								if (inputStream != null)
								{
									for (final String filename : downloadFiles)
									{
										csvFileDataPath = localPath + File.separator + filename;
										try
										{

											rejectedDataFileName = FilenameUtils.removeExtension(filename) + "_rejected" + extension;
											errorFilePath = localPath + File.separator + rejectedDataFileName;
											inputStream = new SequenceInputStream(
													new ByteArrayInputStream(
															pfeImportJob.getImpexParameters(csvFileDataPath, errorFilePath).getBytes()),
													inputStream);
											impexCronJob = pfeImportJob.getImportCronJob(importProcess, cronJobId, inputStream);
											if (impexCronJob != null)
											{
												rejectFile = new File(errorFilePath);
												unresolvedData = impexCronJob.getUnresolvedDataStore();
												if (unresolvedData != null)
												{
													unresolvedDataFile = unresolvedData.getFile();
													pfeFileService.mergeErrorFiles(unresolvedDataFile, rejectFile);
												}
												if (rejectFile.exists())
												{
													result = pfeTransferFileService.upload(errorFilePath,
															csvRejectDir + File.separator + failedFileReturn + "_" + filename, username,
															password, remoteHost, remotePort, sessionTimeout, channelTimeout);
													if (result == false)
													{
														LOG.error("there was a problem while uploading the file  " + filename
																+ " for the process" + importProcess);
														jobResult = CronJobResult.ERROR;
													}

													LOG.error("there was a problem while processing the file  " + filename + " for the process"
															+ importProcess);
													jobResult = CronJobResult.ERROR;
												}
												else
												{
													impexCronJobResult = CronJobResult.valueOf(impexCronJob.getResult().getCode());
													impexCronJobStatus = CronJobStatus.valueOf(impexCronJob.getStatus().getCode());
													if ((impexCronJobResult == CronJobResult.SUCCESS)
															&& (impexCronJobStatus == CronJobStatus.FINISHED))
													{
														result = pfeTransferFileService.upload(csvFileDataPath,
																csvSuccessDir + File.separator + successFileReturn + "_" + filename, username,
																password, remoteHost, remotePort, sessionTimeout, channelTimeout);
														if (result == false)
														{
															LOG.error("there was a problem while uploading the file  " + filename
																	+ " for the process" + importProcess);
															jobResult = CronJobResult.ERROR;
															jobStatus = CronJobStatus.ABORTED;
														}
														LOG.info("the CSV Quote file " + filename
																+ " has been successfully treated with the process " + importProcess);
													}
													else
													{
														LOG.error("a problem when processing the impex with the process " + importProcess);
														return new PerformResult(CronJobResult.FAILURE, CronJobStatus.ABORTED);

													}
												}
											}

										}
										catch (final Exception e)
										{
											LOG.error(e.getMessage() + "with the process " + importProcess);
											return new PerformResult(CronJobResult.FAILURE, CronJobStatus.ABORTED);
										}
									}
								}
								else
								{
									LOG.error("No such file in " + impexfolder + "with the process " + importProcess);
									return new PerformResult(CronJobResult.FAILURE, CronJobStatus.ABORTED);
								}
							}
						}
					}
				}
			}

		}
		return new PerformResult(jobResult, jobStatus);

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






	/**
	 * @return the pfeImportJob
	 */
	public PfeImportJob getpfeImportJob()
	{
		return pfeImportJob;
	}



	/**
	 * @param pfeImportJob
	 *           the pfeImportJob to set
	 */

	public void setpfeImportJob(final PfeImportJob pfeImportJob)
	{
		this.pfeImportJob = pfeImportJob;
	}



	/**
	 * @return the pfeFileService
	 */
	public PfeFileService getPfeFileService()
	{
		return pfeFileService;
	}



	/**
	 * @param pfeFileService
	 *           the pfeFileService to set
	 */

	public void setPfeFileService(final PfeFileService pfeFileService)
	{
		this.pfeFileService = pfeFileService;
	}


}

