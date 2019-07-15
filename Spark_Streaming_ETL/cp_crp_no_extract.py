# A StreamingContext object can be created from a SparkContext object.
from pyspark.context import SparkContext
from pyspark.streaming import StreamingContext
import ast
#id가 비회원이 아닐경우 user로도 보낸다 

def f(x):
    loglist = x.collect()
    all_list = [] #전체가 본 기업의 번호 리스트
    user_list = [] #회원이 본 기업의 번호 리스트
    for n in range(len(loglist)):
            if list[n] !='':
                log = ast.literal_eval(list[n])
                crp_no = log['crp_no']
                if log['id'] != 'nonuser':
                    user_list.append(crp_no)
                else:
                    all_list.append(crp_no)
                    
sc = SparkContext('spark://localhost:7077', 'crp_no print')
ssc = StreamingContext(sc, 10)
tstream = ssc.textFileStream('hdfs://192.168.56.102:9000/cplogs/comp/190627')
tstream.foreachRDD(f)

ssc.start()
ssc.awaitTermination()
