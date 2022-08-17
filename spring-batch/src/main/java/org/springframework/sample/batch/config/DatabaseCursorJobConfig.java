package org.springframework.sample.batch.config;

import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.sample.batch.SpringBatchApplication;
import org.springframework.sample.batch.dto.VetsDTO;

import java.io.IOException;

import javax.sql.DataSource;

/**
 * This configuration class configures the Spring Batch job that
 * is used to demonstrate that our item reader reads the correct
 * information from the database by using a database cursor.
 */
@Configuration
public class DatabaseCursorJobConfig {

    private static final String PROPERTY_CSV_EXPORT_FILE_HEADER = "batch.job.export.file.header";
    private static final String PROPERTY_CSV_EXPORT_FILE_PATH = "batch.job.export.file.path";
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(DatabaseCursorJobConfig.class);
    
    private static final String QUERY_FIND_VETS =
            "SELECT " +
                    "first_name, " +
                    "last_name " +
            "FROM VETS " +
            "ORDER BY id ASC";

    @Bean
    public ItemReader<VetsDTO> databaseCursorItemReader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<VetsDTO>()
                .name("cursorItemReader")
                .dataSource(dataSource)
                .sql(QUERY_FIND_VETS)
                .rowMapper(new BeanPropertyRowMapper<>(VetsDTO.class))
                .build();
    }

    
    @Bean
    public ItemWriter<VetsDTO> csvWriter(Environment environment) {
        String exportFilePath = environment.getRequiredProperty(PROPERTY_CSV_EXPORT_FILE_PATH);
        Resource exportFileResource = new FileSystemResource(exportFilePath);

        String exportFileHeader = environment.getRequiredProperty(PROPERTY_CSV_EXPORT_FILE_HEADER);
        StringHeaderWriter headerWriter = new StringHeaderWriter(exportFileHeader);

        LineAggregator<VetsDTO> lineAggregator = createVetsLineAggregator();
        
        try {
			Thread.sleep(1000 * 70);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}

		LOGGER.info(String.format("ItemWriter => Path %s file : %s",exportFilePath,exportFileResource.getFilename()));
		
        return new FlatFileItemWriterBuilder<VetsDTO>()
                .name("csvWriter")
                .headerCallback(headerWriter)
                .lineAggregator(lineAggregator)
                .resource(exportFileResource)
                .build();
    }
    
    private LineAggregator<VetsDTO> createVetsLineAggregator() {
        DelimitedLineAggregator<VetsDTO> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(";");

        FieldExtractor<VetsDTO> fieldExtractor = createVetsFieldExtractor();
        lineAggregator.setFieldExtractor(fieldExtractor);

        return lineAggregator;
    }

    private FieldExtractor<VetsDTO> createVetsFieldExtractor() {
        BeanWrapperFieldExtractor<VetsDTO> extractor = new BeanWrapperFieldExtractor<>();
        extractor.setNames(new String[] {"firstName", "lastName"});
        return extractor;
    }

    /**
     * Creates a bean that represents the only step of our batch job.
     * @param reader
     * @param writer
     * @param stepBuilderFactory
     * @return
     */
    @Bean
    public Step databaseCursorStep(@Qualifier("databaseCursorItemReader") ItemReader<VetsDTO> reader,
                                   @Qualifier("csvWriter") ItemWriter<VetsDTO> writer,
                                   StepBuilderFactory stepBuilderFactory) {
    	
    	LOGGER.info("Run step databaseCursorStep");
    	
        return stepBuilderFactory.get("databaseCursorStep")
        		.allowStartIfComplete(true)
                .<VetsDTO, VetsDTO>chunk(1)
                .reader(reader)
                .writer(writer)
                .build();
    }

    /**
     * Creates a bean that represents our example batch job.
     * @param exampleJobStep
     * @param jobBuilderFactory
     * @return
     */
    @Bean
    public Job databaseCursorJob(@Qualifier("databaseCursorStep") Step exampleJobStep,
                                 JobBuilderFactory jobBuilderFactory) {
    	
    	LOGGER.info("Run Job databaseCursorJob");
    	
        return jobBuilderFactory.get("databaseCursorJob")
                .incrementer(new RunIdIncrementer())
                .flow(exampleJobStep)
                .end()
                .build();
    }
}
