package org.springframework.sample.batch;

import java.util.Arrays;
import java.util.Objects;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;



@SpringBootApplication
@EnableBatchProcessing
@EnableScheduling
public class SpringBatchApplication {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(SpringBatchApplication.class);
	private static final String EXPORT_VETS = "exportVets";
	
	public static void main(String[] args) {
		SpringApplication.run(SpringBatchApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext context) {
		return args -> {
			int exitCode = 0;
			LOGGER.info("Starting the batch");
			Arrays.asList(args).forEach(LOGGER::debug);
			CommandLineParser parser = new BasicParser();
			Options options = new Options();
			options.addOption(EXPORT_VETS, false, "Use given file for job");

	 
			CommandLine line = null;
			
			JobLauncher jobLauncher = context.getBean("jobLauncher", JobLauncher.class);

			try {
				line = parser.parse(options, args);

			} catch (Exception ex) {
				LOGGER.error("Error in batch parameters", ex);
				exitCode = 1;
			}

			if (line != null) {
				JobExecution jobExecution = null;
				if (line.hasOption(EXPORT_VETS)) {
					LOGGER.info("Start the vetsLauncher 4");
					
					jobExecution = jobLauncher.run((Job) context.getBean("databaseCursorJob", Job.class),
							new JobParametersBuilder().toJobParameters());
				} else {
					LOGGER.warn("No known option");
					exitCode = 1;
				}

				while ((jobExecution != null && jobExecution.isRunning())) {
					LOGGER.info("jobExecution is running");
					Thread.sleep(1000 * 70);
				}
				exitCode = (Objects.requireNonNull(jobExecution).getExitStatus().compareTo(ExitStatus.COMPLETED));
			} else {
				LOGGER.warn("No known option");
				exitCode = 1;
			}
			Thread.sleep(1000 * 70);
			System.exit(exitCode);
			

		};
	}
}
