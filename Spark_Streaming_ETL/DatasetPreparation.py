import pandas as pd
import numpy as np


class DatasetPreparation(object):
    """
    Combine complog and newslog into one dataframe.
    The result will be used as a dataset for company recommendations.
    """

    def agg_chked_crpno(self):
        """Get counts of how many times each user checked certain companies."""
        crpno_sum = pd.read_csv('./complog.csv', encoding='euc_kr', index_col=0) \
                            .groupby(['id', 'crp_no'], as_index=False).agg(np.sum) \
                            .sort_values(['cnt'], ascending=False).set_index('id') \
                            .drop(columns=['cnt'])
        crpno_sum['crp_no'] = crpno_sum['crp_no'].astype(str)
        # crpno_sum.to_csv('crpno_sum.csv', encoding='euc_kr')
        return crpno_sum
        
    def combine_all(self, crpno_sum):
        """Combine counts of user-ckecked news keywords and companies."""
        newslog_df = pd.read_csv('./newslog.csv', encoding='euc_kr', index_col=0)
        all_combined = pd.concat(
            [newslog_df, crpno_sum], 
            axis=1, 
            join_axes=[newslog_df.index], 
            join='inner'
            ).fillna('comp')
        all_combined.to_csv('./all_combined.csv', encoding='euc_kr')
        

if __name__ == '__main__':
    prep = DatasetPreparation()
    crpno_sum = prep.agg_chked_crpno()
    prep.combine_all(crpno_sum)