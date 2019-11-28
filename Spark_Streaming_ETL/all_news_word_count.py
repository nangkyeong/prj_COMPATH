import pymongo
import pandas as pd
import time

# 전체 뉴스의  상위 500개 단어를 abc.csv에 담는다 
class hi():

    # 뉴스 데이터를 가져오기 위해 pymongo 연결
    con = pymongo.MongoClient("localhost", 27777)["news"]["news_analyze"]
    dd = {}
  
    def wordmerge(self, news_number):
        dic = self.con.find({ "news_number" : int(news_number) }, { "_id" : 0, "data" : 1, "news_number" : 1 })
        for i in dic[0]["data"]:
            if str(i) in self.dd:
                self.dd[i]["cnt"] += dic[0]["data"].get(i)
            else:
                self.dd[i] = { "cnt" : dic[0]["data"].get(i) } 
        
    def hey(self ,all_li):
        for i in all_li:
            self.wordmerge(i)

        # dataframe화하여 csv로 저장
        top500 = pd.DataFrame(self.dd).transpose().reset_index().rename(columns={'index':'word'}).sort_values(by='cnt', ascending=False)['word'].head(500)
        csv_file = pd.DataFrame(top500).reset_index().rename(columns={'index':'cnt'}).set_index('word').transpose()
        csv_file.to_csv('abc.csv', encoding='euc-kr')
               
if __name__ == '__main__':
    st = time.time()
    all_li = list(range(2557))
    h = hi()
    h.hey(all_li)
    print(time.time() - st) 
    
    
    