import pandas as pd
import numpy as np

class h():
    def r(self):
        rdf = pd.read_csv('./complog.csv', encoding='euc_kr',index_col=0)
        rdf=rdf.groupby(['id','crp_no'],as_index=False).agg(np.sum).sort_values(['cnt'], ascending=False).set_index('id').drop(columns=['cnt'])
        rdf['crp_no'] = rdf['crp_no'].astype(str)
        rdf.to_csv('id_com.csv', encoding='euc_kr')
        print(rdf)
        '''
                 crp_no
        id                 
        nang  1301110006246
        sj    1101111707178

        '''
        self.res(rdf)
        
    def res(self,df):
        newsdf = pd.read_csv('./newslog.csv', encoding='euc_kr',index_col=0)
        res = pd.concat([newsdf,df],axis=1, join_axes=[newsdf.index],join='inner').fillna('comp')
        print('\n\n\n\n\n\n\nres\n\n',res)
        res.to_csv('./tolearn.csv',encoding='euc_kr')
        
if __name__ == '__main__':
    h= h()
    h.r()