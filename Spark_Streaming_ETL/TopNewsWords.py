import pymongo
import pandas as pd
import time


class TopNewsWords(object):
    """
    Get the 500 most frequently used keywords of all news of the day.
    The result is saved as a csv file called 'top_newswords.csv'.
    """

    con = pymongo.MongoClient('localhost', 27777)['news']['news_analyze']
    newswords_allcnt = {}

    def count_allnews(self, newsnums):
        """Count all news keywords of the day."""
        for num in newsnums:
            self.count_eachnews(num)

    def count_eachnews(self, news_num):
        """Count keywords of each news keywords and get the sum of counts per keyword"""
        news_content = self.con.find(
            {'news_number': int(news_num)},
            {'_id': 0, 'data': 1, 'news_number': 1})
        for i in news_content[0]["data"]:
            if str(i) in self.newswords_allcnt:
                self.newswords_allcnt[i]['cnt'] += news_content[0]['data'].get(i)
            else:
                self.newswords_allcnt[i] = {'cnt': news_content[0]['data'].get(i)} 

    def top500_tocsv(self):
        """Get the top 500 keywords of all news and save it as a csv file."""
        top500 = pd.DataFrame(self.newswords_allcnt).transpose() \
                            .reset_index().rename(columns={'index': 'word'}) \
                            .sort_values(by='cnt', ascending=False)['word'].head(500) \
                            .reset_index().rename(columns={'index': 'cnt'}) \
                            .set_index('word').transpose()
        top500.to_csv('./top_newswords.csv', encoding='euc-kr')
  
               
if __name__ == '__main__':
    st = time.time()
    topword_process = TopNewsWords()
    newsnums = list(range(2557))
    topword_process.count_allnews(newsnums)
    print(time.time() - st) 
    
    
    