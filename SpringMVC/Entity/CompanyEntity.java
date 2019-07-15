package com.comp;

public class CompanyEntity {
   private String crp_nm; // 기업명 1
   private String crp_nm_i; // 기업명(주) 1
   private String ceo_nm; // ceo 이름 1
   private String adr; // 기업 주소 1
   private String hm_url; // 기업 홈페이지 주소 1
   private String phn_no; // 기업 전화번호 1
   private String fax_no; // 기업 팩스 번호 1
   private String err_code; // 에러코드
   private String err_msg; // 에러 메시지
   private String crp_nm_e; //
   private String stock_cd;
   private String crp_cls;
   private String crp_no; // 기업번호 pk
   private String bsn_no;
   private String ind_cd;
   private String est_dt;
   private String acc_mt;
   private String RegLogImgNm;
   private String rcp_no;
   


   public CompanyEntity(String crp_nm, String crp_nm_i, String ceo_nm, String adr, String hm_url, String phn_no,
         String fax_no, String err_code, String err_msg, String crp_nm_e, String stock_cd, String crp_cls,
         String crp_no, String bsn_no, String ind_cd, String est_dt, String acc_mt, String regLogImgNm,
         String rcp_no) {
      super();
      this.crp_nm = crp_nm;
      this.crp_nm_i = crp_nm_i;
      this.ceo_nm = ceo_nm;
      this.adr = adr;
      this.hm_url = hm_url;
      this.phn_no = phn_no;
      this.fax_no = fax_no;
      this.err_code = err_code;
      this.err_msg = err_msg;
      this.crp_nm_e = crp_nm_e;
      this.stock_cd = stock_cd;
      this.crp_cls = crp_cls;
      this.crp_no = crp_no;
      this.bsn_no = bsn_no;
      this.ind_cd = ind_cd;
      this.est_dt = est_dt;
      this.acc_mt = acc_mt;
      this.RegLogImgNm = regLogImgNm;
      this.rcp_no = rcp_no;
   }

   public String getRcp_no() {
      return rcp_no;
   }

   public void setRcp_no(String rcp_no) {
      this.rcp_no = rcp_no;
   }

   public CompanyEntity() {
      super();
      // TODO Auto-generated constructor stub
   }

   public String getCrp_nm() {
      return crp_nm;
   }

   public void setCrp_nm(String crp_nm) {
      this.crp_nm = crp_nm;
   }

   public String getCrp_nm_i() {
      return crp_nm_i;
   }

   public void setCrp_nm_i(String crp_nm_i) {
      this.crp_nm_i = crp_nm_i;
   }

   public String getCeo_nm() {
      return ceo_nm;
   }

   public void setCeo_nm(String ceo_nm) {
      this.ceo_nm = ceo_nm;
   }

   public String getAdr() {
      return adr;
   }

   public void setAdr(String adr) {
      this.adr = adr;
   }

   public String getHm_url() {
      return hm_url;
   }

   public void setHm_url(String hm_url) {
      this.hm_url = hm_url;
   }

   public String getPhn_no() {
      return phn_no;
   }

   public void setPhn_no(String phn_no) {
      this.phn_no = phn_no;
   }

   public String getFax_no() {
      return fax_no;
   }

   public void setFax_no(String fax_no) {
      this.fax_no = fax_no;
   }

   public String getErr_code() {
      return err_code;
   }

   public void setErr_code(String err_code) {
      this.err_code = err_code;
   }

   public String getErr_msg() {
      return err_msg;
   }

   public void setErr_msg(String err_msg) {
      this.err_msg = err_msg;
   }

   public String getCrp_nm_e() {
      return crp_nm_e;
   }

   public void setCrp_nm_e(String crp_nm_e) {
      this.crp_nm_e = crp_nm_e;
   }

   public String getStock_cd() {
      return stock_cd;
   }

   public void setStock_cd(String stock_cd) {
      this.stock_cd = stock_cd;
   }

   public String getCrp_cls() {
      return crp_cls;
   }

   public void setCrp_cls(String crp_cls) {
      this.crp_cls = crp_cls;
   }

   public String getCrp_no() {
      return crp_no;
   }

   public void setCrp_no(String crp_no) {
      this.crp_no = crp_no;
   }

   public String getBsn_no() {
      return bsn_no;
   }

   public void setBsn_no(String bsn_no) {
      this.bsn_no = bsn_no;
   }

   public String getInd_cd() {
      return ind_cd;
   }

   public void setInd_cd(String ind_cd) {
      this.ind_cd = ind_cd;
   }

   public String getEst_dt() {
      return est_dt;
   }

   public void setEst_dt(String est_dt) {
      this.est_dt = est_dt;
   }

   public String getAcc_mt() {
      return acc_mt;
   }

   public void setAcc_mt(String acc_mt) {
      this.acc_mt = acc_mt;
   }

   
   public String getRegLogImgNm() {
      return RegLogImgNm;
   }

   public void setRegLogImgNm(String regLogImgNm) {
      RegLogImgNm = regLogImgNm;
   }

   @Override
   public String toString() {
      return "CompanyEntity [crp_nm=" + crp_nm + ", crp_nm_i=" + crp_nm_i + ", ceo_nm=" + ceo_nm + ", adr=" + adr
            + ", hm_url=" + hm_url + ", phn_no=" + phn_no + ", fax_no=" + fax_no + ", err_code=" + err_code
            + ", err_msg=" + err_msg + ", crp_nm_e=" + crp_nm_e + ", stock_cd=" + stock_cd + ", crp_cls=" + crp_cls
            + ", crp_no=" + crp_no + ", bsn_no=" + bsn_no + ", ind_cd=" + ind_cd + ", est_dt=" + est_dt
            + ", acc_mt=" + acc_mt + ", RegLogImgNm=" + RegLogImgNm + ", rcp_no=" + rcp_no + "]";
   }

   


}

