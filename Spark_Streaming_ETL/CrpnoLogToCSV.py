import ast
from pyspark.context import SparkContext
from pyspark.streaming import StreamingContext
import pandas as pd

class CrpnoLogToCSV(object):
    """
    Extracts id numbers of companies of which each users checked the details.
    The result is saved as a csv file.
    """

    chked_crps = {}

    def process_crplogs(self, logset):
        """Get a list of logs from each logsets and process them into a csv file."""
        loglist = logset.collect()
        if loglist:
            self.crpno_extract(loglist)
            self.each_tocsv()

    def crpno_extract(self, loglist):
        """Extract id numbers of companies that users checked."""
        for n in range(len(loglist)):
            if loglist[n] != '':
                log = ast.literal_eval(loglist[n])
                user_id = log['id']
                crpno = log['crp_no']
            if user_id != 'nonuser':
                self.chked_crps[user_id] = crpno

    def each_tocsv(self):
        """Save logs as a csv file."""
        complogdf = pd.DataFrame.from_dict(self.chked_crps)
        complogdf.to_csv('./complog.csv', encoding='euc_kr')

    
if __name__ == '__main__':      
    sc = SparkContext('spark://localhost:7077', 'crp_no print')
    ssc = StreamingContext(sc, 10)
    tstream = ssc.textFileStream('hdfs://192.168.56.102:9000/cplogs/comp/190627')
    
    logprocess = CrpnoLogToCSV()
    tstream.foreachRDD(logprocess.process_crplogs)
    
    ssc.start()
    ssc.awaitTermination()
