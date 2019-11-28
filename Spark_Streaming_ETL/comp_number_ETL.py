import pandas as pd
import numpy as np

class h():
    def r(self):
        complogdf = pd.read_csv('./complog.csv', encoding='euc_kr', index_col = 0)
        complogdf = complogdf.groupby(['id', 'crp_no'], as_index=False).agg(np.sum).sort_values(['cnt'], ascending=False).set_index('id').drop(columns=['cnt'])
        complogdf['crp_no'] = complogdf['crp_no'].astype(str)
        complogdf.to_csv('id_com.csv', encoding='euc_kr')
        self.res(complogdf)
        
    def res(self, complogdf):
        newslogdf = pd.read_csv('./newslog.csv', encoding='euc_kr', index_col = 0)
        tolearn = pd.concat([newslogdf, complogdf], axis=1, join_axes=[newslogdf.index], join='inner').fillna('comp')
        tolearn.to_csv('./tolearn.csv', encoding='euc_kr')
        
if __name__ == '__main__':
    h= h()
    h.r()