package com.rabit.consumer.config;

import com.rabit.consumer.ItemReader.AmqpItemReader;
import com.rabit.consumer.data.ProductDTO;
import com.rabit.consumer.model.Product;
import com.rabit.consumer.repo.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class BatchCondig {
    private final PlatformTransactionManager platformTransactionManager;
    private final JobRepository jobRepository;
    private final AmqpTemplate amqpTemplate;
    private final ProductRepo productRepo;

    @Value("${rabitmq.queue.test}")
    private String testQueue;

    @Bean
    public FlatFileItemWriter<Product> fileWriter() {
        FlatFileItemWriter<Product> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource("outputs/outputFile.txt"));

        DelimitedLineAggregator<Product> aggregator = new DelimitedLineAggregator<>();
        BeanWrapperFieldExtractor<Product> extractor = new BeanWrapperFieldExtractor<>();
        extractor.setNames(new String[]{
                 "name", "price", "category"
        });
        aggregator.setFieldExtractor(extractor);
        writer.setLineAggregator(aggregator);
        return writer;
    }

    @Bean
    public ItemWriter<Product> jpaWriter(){
        return new ItemWriter<Product>() {
            @Override
            public void write(Chunk<? extends Product> chunk) throws Exception {
                productRepo.saveAll(chunk.getItems());
            }
        };
    }

    public ItemReader<ProductDTO> amqpItemReader() {
        return () -> {
            var data =(ProductDTO) amqpTemplate.receiveAndConvert(testQueue);
            return data;
        };
    }

    @Bean
    public Job testJob() {
        return new JobBuilder("testJob", jobRepository)
                .start(testStep1())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step testStep1() {
        return new StepBuilder("testStep", jobRepository)
                .<ProductDTO, Product>chunk(10, platformTransactionManager)
                .reader(amqpItemReader())
                .allowStartIfComplete(true)
                .processor(new ItemProcessor<ProductDTO, Product>() {
                    @Override
                    public Product process(ProductDTO item) throws Exception {
                        return new Product()
                                .setName(item.getName())
                                .setCategory(item.getCategory())
                                .setPrice(item.getPrice())
                                .setDescription(item.getDescription());
                    }
                })
//                .writer(fileWriter())
                .writer(jpaWriter())
                .readerIsTransactionalQueue()
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor("spring_batch");
    }
}
