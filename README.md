# Project COMPATH

COMPATH was a team project for a web platform that provides job seekers with more information of Korean companies. 

My role was to make Spring MVC pattern for parts of webservices,
 and to collect and process logs to make datasets for ML for company recommendation system.

In this repository, you can find 3 features as follows.

1. Spring MVC pattern
2. Log Collection 
3. Log ETL with Spark Streaming

## Spring DI, AOP

There are two controller classes, for mypage and Sbook controllers. Those are programmed to experience DI feature of Spring. Also, you can check logging codes with @Around annotation.

1. Mypage
    In this page, you can see lists of news, companies, recruit posts, other users' Sbooks.
    (Sbooks means posts written by users which includes various information such as recruitments, company features, etc. 
    We called this user-created posts 'Scrap Book' which you'll find as 'sbook' in code.

2. Sbook
here, you will find CRUD features of sbook, and sharing freature as well.

For accessing Oracle Database, we used MyBatis.

1. Configuration for Oracle access
2. Util class to declare static SqlSessionFactory variable
3. XML mapper files

3. Log Advice
Please check aop directory.

## Log Collection

Log4J was base tool to collect 
There are two appenders, because I switched the way to collect logs.


### 1. RollingFileAppender

At first, I wanted to generate log file at local path. 
From CPLogger.java you can check codes to generate log messages.
log4j2.xml includes path for log files and log pattern. 

### 2. FlumeAppender

Then, we decided to use logs for company recommendation system. Every time user access certain page (such as company detail page), log message is sent to flume through Avro source and directly stored on hdfs without being saved at local storage. 



## Flume configuration

As I tried log collection in two ways, Flume configuration file has to be switched as well. 

### 1. execAgent

This conf file is for Rolling File Appender. Flume keeps monitoring on the designated log file, and when finding new log messages, it pulls 

### 2. avroAgent

For Flume Appender. This takes log messages through Avro source. 



## Spark Streaming ETL

Here, for Spark streaming we used textFileStream.
Each RDD contains newly generated logs.

1. Log Extraction
company numbers, news numbers
2. Lot Transformation
**Company numbers** are saved in a Pandas Dataframe as a csv file, with columns of id, crp_no
**News numbers** are saved in a Pandas Dataframe as a csv file, with columns of id, news word count
