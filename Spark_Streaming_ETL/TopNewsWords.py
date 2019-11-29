import pymongo
import pandas as pd
import time

# 전체 뉴스의 상위 500개 단어를 topNewsWords.csv에 담는다 
class TopNewsWords():

    # 뉴스 데이터를 가져오기 위해 pymongo 연결
    con = pymongo.MongoClient("localhost", 27777)["news"]["news_analyze"]
    totalCount = {}

    def countAllNews(self, newslist):
        for num in newslist:
            self.countEachNews(num)

    def countEachNews(self, news_number):
        newsContents = self.con.find({ "news_number": int(news_number) }, { "_id" : 0, "data" : 1, "news_number" : 1 })
        for i in newsContents[0]["data"]:
            if str(i) in self.totalCount:
                self.totalCount[i]["cnt"] += newsContents[0]["data"].get(i)
            else:
                self.totalCount[i] = { "cnt" : newsContents[0]["data"].get(i) } 
        
    def saveTop500(self):
        topNewsWords = pd.DataFrame(self.totalCount).transpose() \
                            .reset_index().rename(columns={'index':'word'}) \
                            .sort_values(by='cnt', ascending=False)['word'].head(500) \
                            .reset_index().rename(columns={'index':'cnt'}) \
                            .set_index('word').transpose()
        topNewsWords.to_csv('./topNewsWords.csv', encoding='euc-kr')
  
               
if __name__ == '__main__':
    st = time.time()
    topNewsWords = TopNewsWords()
    newslist = list(range(2557))
    topNewsWords.countAllNews(newslist)
    print(time.time() - st) 
    
    
    