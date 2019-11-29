# Project COMPATH

COMPATH is a team project for a web platform that provides job seekers with more information of Korean companies. 

In this repository, you can find 3 features as follows.

1. Spring server pages
2. Log collecting with Log4j and Flume
3. Log processing with Spark Streaming

## Spring server pages

You can find two server pages, here.

Also, you can check logging codes with @Around annotation.

### Mypage
In this page, you would have this lists of news, companies, recruit posts, other users' Sbooks.

Sbooks means posts written by users which contains various information such as recruitments, company features.   
We named this user-created posts 'Scrap Book' as which you will find 'sbook' in code.  

### Sbook
here, you will find CRUD features of sbook, and sharing freature as well.

### MyBatis configurations
For accessing Oracle Database, we used MyBatis as ORM.

### Log Advice
Please refer to aop directory.



## Log Collection with Log4j and Flume

### RollingFileAppender

At first, I wanted to create log files locally.  
From CPLogger.java you can check codes to generate log messages.  
log4j2.xml includes path for log files and log pattern.   

### FlumeAppender

Then, we decided to use logs for company recommendation system.   
Every time user requests a company's detail page or a news detail page,   
a log message will be generated including the id number of the company or the news will be sent to flume   
through Avro source and stored on hdfs without being saved at local storage.   



## Flume configuration

As I tried log collecting in two ways, Flume configuration file has to be switched as well. 

### execAgent

This conf file is for Rolling File Appender.   
Flume keeps monitoring on the designated log file, and when finding new log messages, it pulls 

### avroAgent

For Flume Appender. This takes log messages through Avro source. 



## Spark Streaming ETL

Here, for Spark streaming we used textFileStream.  
Each RDD contains newly generated logs.  

### Log Extraction
company numbers, news numbers
### Lot Transformation
**Company numbers** are saved in a Pandas Dataframe as a csv file, with columns of id, crp_no  
**News numbers** are saved in a Pandas Dataframe as a csv file, with columns of id, news word count
