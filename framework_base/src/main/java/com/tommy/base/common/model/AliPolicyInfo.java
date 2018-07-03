package com.tommy.base.common.model;

/**
 * Created by donghaijun on 2018/2/9.
 */
public class AliPolicyInfo {



    /**
     * AccessKeySecret : CuHebMPrZPFYrndeDqtTWV4nt81eM5XeQV4e2hMd3ZfF
     * AccessKeyId : STS.CKs4qGiyBzyFsGvSmvPSM5qwp
     * Expiration : 2017-09-06T03:40:16Z
     * SecurityToken : CAIShQJ1q6Ft5B2yfSjIoojGf8vzhKZjzbutcWHHt20jXNxh2rTckjz2IHlJenNuB+Efs/gymmxQ6/cflqJ4T55IQ1Dza8J148zwePNj7smT1fau5Jko1beHewHKeTOZsebWZ+LmNqC/Ht6md1HDkAJq3LL+bk/Mdle5MJqP+/UFB5ZtKWveVzddA8pMLQZPsdITMWCrVcygKRn3mGHdfiEK00he8TojtvXjm5XBu0eF0QGhm7Ivyt6vcsT+Xa5FJ4xiVtq55utye5fa3TRYgxowr/4r0vYap2ae443AXQUJvkTbKZnd9tx+MQl+fbMmHK1Jqvfxk/Bis/DUjZ7wzxtdzieww40H/OoagAFh0ffFJ06prtiFkFkaRvGrDM1TX134pDzSENgkR5uUDLqnmBd2NFphld0hS7meawhs99QPNA3x15TE7OCyGHNB0f/PbMHge2sgjNFcCnwUvyIvKboA/doeEkF4F+GKo6yUf03FrRZhMlS630I11/YGH8Sf8I4feU4vb4XJLsBgQA==
     * bucket : xinxun-dev
     * dir : juefu/
     * host : oss-cn-hangzhou.aliyuncs.com
     */

    public String AccessKeySecret;
    public String AccessKeyId;
    public String Expiration;
    public String SecurityToken;
    public String bucket;
    public String dir;
    public String host;

    public String getAccessKeySecret() {
        return AccessKeySecret;
    }

    public void setAccessKeySecret(String AccessKeySecret) {
        this.AccessKeySecret = AccessKeySecret;
    }

    public String getAccessKeyId() {
        return AccessKeyId;
    }

    public void setAccessKeyId(String AccessKeyId) {
        this.AccessKeyId = AccessKeyId;
    }

    public String getExpiration() {
        return Expiration;
    }

    public void setExpiration(String Expiration) {
        this.Expiration = Expiration;
    }

    public String getSecurityToken() {
        return SecurityToken;
    }

    public void setSecurityToken(String SecurityToken) {
        this.SecurityToken = SecurityToken;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
