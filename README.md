# Project COMPATH

COMPATH was a team project for a web platform that provides job seekers with more information of Korean companies. 

My role was to make Spring MVC pattern for parts of webservices and to collect and process logs to make datasets for ML for company recommendation system.

In this repository, you can find 3 features as follows.

1. Spring MVC pattern
2. Log Collection 
3. Log ETL with Spark Streaming

## Spring MVC Pattern

There are two controller classes, for mypage and Sbook controllers.

1. Mypage

    In this page, you can see lists of news, companies, recruit posts, other users' Sbooks.
    (Sbooks means posts written by users which includes various information such as recruitments, company features, etc. We called this user-created posts 'Scrap Book' which you'll find as 'sbook' in code.

2. Sbook
here, you will find CRUD features of sbook, and sharing freature as well.

For accessing Oracle Database, we used MyBatis.

1. Configuration for Oracle access
2. Util class to declare static SqlSessionFactory variable
3. XML mapper files

## Log Collection

Log4J was base tool to collect 
There are two appenders, because I switched the way to collect logs.
