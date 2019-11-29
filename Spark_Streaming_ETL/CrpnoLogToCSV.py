import ast
from pyspark.context import SparkContext
from pyspark.streaming import StreamingContext
import pandas as pd

class CrpnoLogToCSV:
    
    userChkedCrpno = {} # 로그인한 사람이 본 기업의 번호 리스트

    def extractIdCrpno(self, logset):
        loglist = logset.collect()
        #all_list = [] # 해당 로그메세지에서 사람들이 본 기업의 번호 리스트
        for n in range(len(loglist)):
            if loglist[n] !='':
                msg = ast.literal_eval(loglist[n])
                userid = msg['id']
                crpno = msg['crp_no']
            if userid != 'nonuser':
                self.userChkedCrpno[userid] = crpno
                self.save()
    def save(self):
        dftosave = pd.DataFrame.from_dict(self.userChkedCrpno)
        dftosave.to_csv('./complog.csv', encoding='euc_kr')

    
                    
sc = SparkContext('spark://localhost:7077', 'crp_no print')
ssc = StreamingContext(sc, 10)
tstream = ssc.textFileStream('hdfs://192.168.56.102:9000/cplogs/comp/190627')
crpnoLogToCSV = CrpnoLogToCSV()
tstream.foreachRDD(crpnoLogToCSV.extractIdCrpno)

ssc.start()
ssc.awaitTermination()
