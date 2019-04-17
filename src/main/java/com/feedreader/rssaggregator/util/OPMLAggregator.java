package com.feedreader.rssaggregator.util;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;

public class OPMLAggregator {

	public ConcurrentSkipListSet<String> aggregateOPML(List<String> opmlList) throws IOException{
		
		ConcurrentSkipListSet<String> feedsSet = new ConcurrentSkipListSet<>();
		List<Thread> threads = new ArrayList<>();

		opmlList.forEach(opml -> {
			OPMLParser parser = new OPMLParser(opml,feedsSet);
			Thread thread = new Thread(parser);
			threads.add(thread);
			thread.start();
		});

		threads.forEach(thread -> {
			try {
				thread.join(25000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    });
		FileWriter writer = new FileWriter(new File("full3.txt"));
		 BufferedWriter bw = new BufferedWriter(writer);
		 int i=0;
		 for(String s : feedsSet) {
			 bw.write(s);
			 bw.newLine();
			 bw.flush();
		 }
				
		return feedsSet;

  }

}
