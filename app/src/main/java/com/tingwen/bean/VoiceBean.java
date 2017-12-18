package com.tingwen.bean;

/**
 * Created by Administrator on 2017/9/11 0011.
 */
public class VoiceBean {


    /**
     * status : 1
     * msg : 上传音频成功!
     * results : {"filepath":"http://admin.tingwen.me/data/upload/tingyouquan/59b6084b036c5.aac","Length":null,"playtime":0,"Filesize":0,"playsize":false}
     */

    private int status;
    private String msg;
    /**
     * filepath : http://admin.tingwen.me/data/upload/tingyouquan/59b6084b036c5.aac
     * Length : null
     * playtime : 0
     * Filesize : 0
     * playsize : false
     */

    private ResultsBean results;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ResultsBean getResults() {
        return results;
    }

    public void setResults(ResultsBean results) {
        this.results = results;
    }

    public static class ResultsBean {
        private String filepath;
        private Object Length;
        private int playtime;
        private int Filesize;
        private boolean playsize;

        public String getFilepath() {
            return filepath;
        }

        public void setFilepath(String filepath) {
            this.filepath = filepath;
        }

        public Object getLength() {
            return Length;
        }

        public void setLength(Object Length) {
            this.Length = Length;
        }

        public int getPlaytime() {
            return playtime;
        }

        public void setPlaytime(int playtime) {
            this.playtime = playtime;
        }

        public int getFilesize() {
            return Filesize;
        }

        public void setFilesize(int Filesize) {
            this.Filesize = Filesize;
        }

        public boolean isPlaysize() {
            return playsize;
        }

        public void setPlaysize(boolean playsize) {
            this.playsize = playsize;
        }
    }
}
