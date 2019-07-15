import pymongo

import pandas as pd
from pyspark.context import SparkContext
from pyspark.streaming import StreamingContext
import ast, time, os

class hi():
        
    con = pymongo.MongoClient("localhost",27777)["news"]["news_analyze"]
    dd = {}

    def wordmerge(self,userno):
        dic = self.con.find({"news_number" : int(userno[1])},{"_id" : 0,"data" : 1,"news_number" : 1})
        for i in dic[0]["data"]:
            if str(i) in self.dd:
                self.dd[i][userno[0]] += dic[0]["data"].get(i)
            else:
                self.dd[i] = { userno[0] :dic[0]["data"].get(i)} 
        
    def hey(self ,user_list):
        for i in user_list:
            self.wordmerge(i)
            id= i[0]
            
            #전체 뉴스의 word count가 담긴 csv 읽어오기
            lt = pd.read_csv('abc.csv', encoding='euc-kr', index_col=0).transpose().rename_axis('word')
            
            #유저가 새로 확인한 뉴스의 word count가 담긴 dataframe 생성
            rt = pd.DataFrame(self.dd).transpose().rename_axis('word')
            
            #newslog.csv 없으면 생성
            if not os.path.exists('./newslog.csv'):
                #전체 word 기준으로 새로운 id 칼럼 붙이고, na값 int 0으로 채우기
                df = pd.concat([lt, rt], axis=1, join_axes=[lt.index], join='inner')
                df[id] = df[id].fillna(0).astype(int)
                df.transpose().to_csv('./newslog.csv', encoding='euc-kr')
            else:
                #newslog.csv 존재하면 파일을 열어서 해당 id 칼럼이 있으면 update, 아니면 id 칼럼 추가 
                ot = pd.read_csv('./newslog.csv', encoding='euc_kr', index_col=0).transpose()
                ot['cnt'] = ot['cnt'].astype(int)
                if id in ot.columns:
                    ot[id] += rt[id]
                    ot[id] = ot[id].fillna(0).astype(int)
                    ot.transpose().to_csv('./newslog.csv', encoding='euc_kr', mode='w')
                else:
                    nt = pd.concat([ot,rt],axis=1, join_axes=[ot.index], join='inner')
                    nt[id] = nt[id].fillna(0).astype(int)
                    nt.transpose().to_csv('./newslog.csv', encoding='euc-kr')

            
def f(x):
    h = hi()
    st = time.time()
    loglist = x.collect()
    print(loglist)
    if loglist :
        user_list = [] #회원이 본 뉴스번호 리스트
        for n in range(len(loglist)):
            if loglist[n] :
                log = ast.literal_eval(loglist[n])
                print('log',log)
                id_no = (log['id'],log['news_number'])
                print('no',id_no)
                if id_no[0] != 'nonuser':
                    user_list.append(id_no)

        if len(user_list) > 0:
            h.hey(user_list)
    print(time.time() - st)
            
if __name__ == '__main__':
    sc = SparkContext()
    ssc = StreamingContext(sc, 10)
    #오늘 날짜의 news log가 발생할 때 처리
    date = time.strftime("%y%m%d")
    tstream = ssc.textFileStream('hdfs://192.168.56.102:9000/cplogs/news/'+date) 
    tstream.foreachRDD(f)
    ssc.start()
    ssc.awaitTermination()
    
    