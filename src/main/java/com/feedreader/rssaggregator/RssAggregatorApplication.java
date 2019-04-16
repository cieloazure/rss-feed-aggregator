package com.feedreader.rssaggregator;

import com.feedreader.rssaggregator.model.FeedAggregate;
import com.feedreader.rssaggregator.tasks.BlockingQueueFeedAggregator;
import com.feedreader.rssaggregator.tasks.FeedScanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class RssAggregatorApplication {

      private static FeedAggregate feedAggregate;

    public static void main(String[] args){
        // Start sping application
        SpringApplication.run(RssAggregatorApplication.class, args);

        FeedScanner scanner = (FeedScanner)ApplicationContextProvider.getApplicationContext().getBean("feedScanner");

        List<String> feeds = new ArrayList<>();
        try {
            File file = new ClassPathResource("feeds.txt").getFile();
            Scanner scan = new Scanner(file);

            while (scan.hasNextLine()) {
                feeds.add(scan.nextLine());
            }
            scan.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        for(int i = 0; i < feeds.size(); i++){
            scanner.addSource(feeds.get(i));
        }

        BlockingQueueFeedAggregator aggregator = (BlockingQueueFeedAggregator)ApplicationContextProvider.getApplicationContext().getBean("feedAggregator");

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

        scheduledExecutorService.scheduleAtFixedRate(scanner, 10, 300, TimeUnit.SECONDS);

        ExecutorService executorService = Executors.newFixedThreadPool(1);

        executorService.submit(aggregator);
    }

//
//  public static void main(String[] args) {
//    SpringApplication.run(RssAggregatorApplication.class, args);
//
//
//    List<String> feeds = new ArrayList<>();
//    try {
//      File file = new ClassPathResource("feeds.txt").getFile();
//      Scanner scanner = new Scanner(file);
//
//      while (scanner.hasNextLine()) {
//        feeds.add(scanner.nextLine());
//      }
//      scanner.close();
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//
//    System.out.println("Starting...");
//    long start = System.currentTimeMillis();
//    feedAggregate = aggregate(feeds, 20);
//    long finish = System.currentTimeMillis();
//    System.out.println(finish - start);
//    System.out.println("size: " + feedAggregate.getAggregatedList().size());
//
//  }


}
