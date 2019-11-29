import pandas as pd
import numpy as np

class DatasetPreparation():
    def combineIdCrpno(self):
        id_crpno_combined = pd.read_csv('./complog.csv', encoding='euc_kr', index_col = 0) \
                        .groupby(['id', 'crp_no'], as_index=False).agg(np.sum) \
                        .sort_values(['cnt'], ascending=False).set_index('id') \
                        .drop(columns=['cnt'])
        id_crpno_combined['crp_no'] = id_crpno_combined['crp_no'].astype(str)
        # id_crpno_combined.to_csv('id_com.csv', encoding='euc_kr')
        return id_crpno_combined
        
    def combineAll(self, id_crpno_combined):
        newslogdf = pd.read_csv('./newslog.csv', encoding='euc_kr', index_col = 0)
        allCombined = pd.concat([newslogdf, id_crpno_combined], axis=1, join_axes=[newslogdf.index], join='inner').fillna('comp')
        allCombined.to_csv('./allCombined.csv', encoding='euc_kr')
        
if __name__ == '__main__':
    prep = DatasetPreparation()
    id_crpno_combined = prep.combineIdCrpno()
    prep.combineAll(id_crpno_combined)