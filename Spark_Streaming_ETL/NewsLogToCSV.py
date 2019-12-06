import ast
import time
import os
import pymongo
import pandas as pd
from pyspark.context import SparkContext
from pyspark.streaming import StreamingContext



class NewsLogToCSV(object):
    """
    Process news logs into a csv files.


    Extract new id number from logset,
    count the keywords of all news and the news user checked,
    then merge them into one dataframe.

    The result will be saved as a csv file per a user.
    """

    con = pymongo.MongoClient('localhost',27777)['news']['news_analyze']

    chkedwords = {}
    top_newsword_df = pd.DataFrame()
    chkedwords_df = pd.DataFrame()

    def process_newslog(self, logset):
        """Get a list of logs from each logsets and process them into a csv file."""
        loglist = logset.collect()
        if loglist:
            user_loglist = self.newsno_extract(loglist)
            self.get_top_newswords()
            self.each_tocsv(user_loglist)

    def newsno_extract(self, loglist):
        """Extract id numbers of the news that users checked."""
        user_loglist = []
        for logmsg in loglist:
            if logmsg:
                log = ast.literal_eval(logmsg)
                userid_newsnum = (log['id'], log['news_number'])
                if userid_newsnum[0] != 'nonuser':
                    user_loglist.append(userid_newsnum)
        if user_loglist:
            return user_loglist
   
    def get_top_newswords(self):
        """Get the top 500 keywords of all news."""
        self.top_newsword_df = pd.read_csv('./top_newswords.csv', encoding='euc-kr', index_col=0) \
                                .transpose().rename_axis('word')
    
    def each_tocsv(self, user_loglist):
        """Update or create a csv file of the processed news logs"""
        for user_log in user_loglist:
            self.count_chkedwords(user_log)
            user_id = user_log[0]
            #newslog.csv 없으면 생성
            if not os.path.exists('./newslog.csv'):
                self.create_newslog_csv(user_id)
            else:
                self.update_newslog_csv(user_id)

    def count_chkedwords(self, user_log):
        """Count keywords of the checked news."""
        news_content = self.con.find(
            {'news_number': int(user_log[1])}, 
            {'_id': 0, 'data': 1, 'news_number': 1})
        for i in news_content[0]['data']:
            if str(i) in self.chkedwords:
                self.chkedwords[i][user_log[0]] += news_content[0]['data'].get(i)
            else:
                self.chkedwords[i] = {user_log[0]: news_content[0]['data'].get(i)} 

    def create_newslog_csv(self, user_id):
        """Create a new newslog csv file."""
        self.user_chkedwords_df = pd.DataFrame(self.chkedwords) \
                                    .transpose().rename_axis('word')
        newslog_df = pd.concat(
            [self.top_newsword_df, self.user_chkedwords_df], 
            axis=1, 
            join_axes=[self.top_newsword_df.index], 
            join='inner'
            )
        newslog_df[user_id] = newslog_df[user_id].fillna(0).astype(int)
        newslog_df.transpose().to_csv('./newslog.csv', encoding='euc-kr')

    def update_newslog_csv(self, user_id):
        """Update an existing newslog csv file."""
        oldlog = pd.read_csv('./newslog.csv', encoding='euc_kr', index_col=0).transpose()
        oldlog['cnt'] = oldlog['cnt'].astype(int)
        if user_id in oldlog.columns:
            oldlog[user_id] += self.chkedwords_df[user_id]
            oldlog[user_id] = oldlog[user_id].fillna(0).astype(int)
            oldlog.transpose().to_csv('./newslog.csv', encoding='euc_kr', mode='w')
        else:
            newlog = pd.concat([oldlog,self.chkedwords_df],axis=1, join_axes=[oldlog.index], join='inner')
            newlog[user_id] = newlog[user_id].fillna(0).astype(int)
            newlog.transpose().to_csv('./newslog.csv', encoding='euc-kr')
    

if __name__ == '__main__':
    sc = SparkContext()
    ssc = StreamingContext(sc, 10)
    
    date = time.strftime("%y%m%d")
    tstream = ssc.textFileStream('hdfs://192.168.56.102:9000/cplogs/news/'+date) 

    logprocess = NewsLogToCSV()
    tstream.foreachRDD(logprocess.process_newslog)

    ssc.start()
    ssc.awaitTermination()
    
    