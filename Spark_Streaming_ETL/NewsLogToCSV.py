import pymongo

import pandas as pd
from pyspark.context import SparkContext
from pyspark.streaming import StreamingContext
import ast, time, os

class NewsLogToCSV():
        
    con = pymongo.MongoClient('localhost',27777)['news']['news_analyze']
    exposedWordCount = {}
    topNewsWords = pd.DataFrame()
    userChkedWords = pd.DataFrame()

    def processNewsLog(self, logset):
        loglist = logset.collect()
        if loglist:
            user_list = self.newsNoExtract(loglist)
            self.readTopNewsWords()
            self.eachToCSV(user_list)

    def newsNoExtract(self, loglist):
        user_list = [] # 회원이 본 뉴스번호 리스트
        for i in range(len(loglist)):
            if loglist[i] :
                log = ast.literal_eval(loglist[i])
                userid_newsnum = (log['id'], log['news_number'])
                if userid_newsnum[0] != 'nonuser':
                    user_list.append(userid_newsnum)
        if user_list:
            return user_list
   
    def readTopNewsWords(self):
        self.topNewsWords = pd.read_csv('./topNewsWords.csv', encoding='euc-kr', index_col=0) \
                              .transpose() \
                              .rename_axis('word')
    
    def eachToCSV(self, user_list):
        for user in user_list:
            self.countExposedWord(user)
            userid = user[0]
            #newslog.csv 없으면 생성
            if not os.path.exists('./newslog.csv'):
                self.createNewslogCSV(userid)
            else:
                self.updateNewsLogCSV(userid)

    def countExposedWord(self, userno):
        newsContent = self.con.find({ "news_number" : int(userno[1]) }, { "_id" : 0, "data" : 1, "news_number" : 1 })
        for i in newsContent[0]["data"]:
            if str(i) in self.exposedWordCount:
                self.exposedWordCount[i][userno[0]] += newsContent[0]["data"].get(i)
            else:
                self.exposedWordCount[i] = { userno[0] : newsContent[0]["data"].get(i) } 

    def createNewslogCSV(self, userid):
        self.userChkedWords = pd.DataFrame(self.exposedWordCount).transpose().rename_axis('word')
        df = pd.concat([self.topNewsWords, self.userChkedWords], axis=1, join_axes=[self.topNewsWords.index], join='inner')
        df[userid] = df[userid].fillna(0).astype(int)
        df.transpose().to_csv('./newslog.csv', encoding='euc-kr')

    def updateNewsLogCSV(self, userid):
        # newslog.csv 존재하면 파일을 열어서 해당 id 칼럼이 있으면 update, 아니면 id 칼럼 추가 
        oldlog = pd.read_csv('./newslog.csv', encoding='euc_kr', index_col=0).transpose()
        oldlog['cnt'] = oldlog['cnt'].astype(int)
        if userid in oldlog.columns:
            oldlog[userid] += self.userChkedWords[userid]
            oldlog[userid] = oldlog[userid].fillna(0).astype(int)
            oldlog.transpose().to_csv('./newslog.csv', encoding='euc_kr', mode='w')
        else:
            newlog = pd.concat([oldlog,self.userChkedWords],axis=1, join_axes=[oldlog.index], join='inner')
            newlog[userid] = newlog[userid].fillna(0).astype(int)
            newlog.transpose().to_csv('./newslog.csv', encoding='euc-kr')
    
if __name__ == '__main__':
    sc = SparkContext()
    ssc = StreamingContext(sc, 10)
    #로그를 오늘 날짜의 폴더에 적재
    date = time.strftime("%y%m%d")
    tstream = ssc.textFileStream('hdfs://192.168.56.102:9000/cplogs/news/'+date) 
    newsLogToCSV = NewsLogToCSV()
    tstream.foreachRDD(newsLogToCSV.newsNoExtract)
    ssc.start()
    ssc.awaitTermination()
    
    