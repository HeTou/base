package com.base.baselib.common.subutils.encrypt.rsa;//package com.tentcoo.base.utils.subutils.encrypt.rsa;/**
// * @Description:
// * @Author:legend.liu
// * @Date: 16:09 2017/12/12 0012
// */
//
//import java.security.interfaces.RSAPrivateKey;
//import java.security.interfaces.RSAPublicKey;
//import java.util.HashMap;
//
///**
// * 〈〉
// *
// * @author legend.liu
// * @create 2017/12/12 0012
// */
//public class RSATest {
//    public static void main(String[] args) throws Exception {
//        HashMap<String, Object> map = RSAUtils.getKeys();
//        //生成公钥和私钥
//        RSAPublicKey publicKey = (RSAPublicKey) map.get("public");
//        RSAPrivateKey privateKey = (RSAPrivateKey) map.get("private");
//
//        //模
//        String modulus = publicKey.getModulus().toString();
//        //公钥指数
//        String public_exponent = publicKey.getPublicExponent().toString();
//        //私钥指数
//        String private_exponent = privateKey.getPrivateExponent().toString();
//        //明文
//        String ming = "123456789";
//        //使用模和指数生成公钥和私钥
//        RSAPublicKey pubKey = RSAUtils.getPublicKey(modulus, public_exponent);
//        RSAPrivateKey priKey = RSAUtils.getPrivateKey(modulus, private_exponent);
//
//
//        System.out.println("公钥----------------");
//        System.out.println(RSAUtils.encryptBASE64(publicKey.getEncoded()));
////        RSAUtils.printPublicKeyInfo(pubKey);
//        System.out.println("私钥------------------");
////        RSAUtils.printPrivateKeyInfo(priKey);
//        System.out.println(RSAUtils.encryptBASE64(privateKey.getEncoded()));
//
//        //加密后的密文
//        String mi = RSAUtils.encryptByPublicKey(ming, pubKey);
//        System.err.println(mi);
//        //解密后的明文
//        ming = RSAUtils.decryptByPrivateKey(mi, priKey);
//        System.err.println(ming);
//
////        String pubStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCI2ox9zBapEX5iS1aEdLcZmWjrWf/rZrOiQKIg\n" +
////                "9GqjJlHvZQxLzi14QLktUtXiyrae4Mw5b8IVv2WUk9xmWypklrK7raYMEaBkQfdbd7XJh8LjZL0A\n" +
////                "mLKS+pAyXUHQMv1O4VsOHdEpgpCh4MPB7M8HbndawF2x3s2GAe1RosGXXwIDAQAB";
////        String priStr = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIjajH3MFqkRfmJLVoR0txmZaOtZ\n" +
////                "/+tms6JAoiD0aqMmUe9lDEvOLXhAuS1S1eLKtp7gzDlvwhW/ZZST3GZbKmSWsrutpgwRoGRB91t3\n" +
////                "tcmHwuNkvQCYspL6kDJdQdAy/U7hWw4d0SmCkKHgw8Hszwdud1rAXbHezYYB7VGiwZdfAgMBAAEC\n" +
////                "gYEAhyB0NAn+HfXd+qQ0yxiLakVlqWz2GrMPj5eN/kgXuvwtTtlRaNAtaV1O+oeeFQ7ffsSsIdJN\n" +
////                "qzeOz0OoDZDaq4El+vb6FcjzEaZRxQYhQYlnKnK1cwareyVOMZmlyc6Xc4XxKJclByVBVLeNTBSy\n" +
////                "0OCNb5C+J9HN0lTmvms5uvECQQDG/D8fUgquQzv30AyN4My5tY7zf3sCe3/zUdx3Pjjvqm7s/vQO\n" +
////                "7EJ8FaQ1yaX0IrYiebzDomnMOGqGH7bvErhVAkEAsBDkI5aNV+dTgDA8YB5d5q4XTBO29WWtlSLS\n" +
////                "N0pybhmjKM6lGz0dW4DYlnPfNqNazUp/75co9j4GXvV1+zuU4wJAJ/W2E2sdW/uCinSMYN2ZH143\n" +
////                "k+yw3kHA4zM4S/YB5xdZ6VwV1P1bKKIL9QVGv21NGEVMRBlUl79onQC3cvFWdQJBAJE67aS5bA/F\n" +
////                "XehQYYz7n4NHuSnk3TcgzwVEutgmzasp9J8VjbWhPNUcvAumusnMB3ttZ4iPTJeJ5CFyIgbBR/cC\n" +
////                "QC8OO+8TDDRtYhC1tKQv6/bOG8viRXpgxNRTwpWJzPfjRBuf1gKP9mkuL6t3rdZsKCAPSo0WM1jr\n" +
////                "9hsJnlw0nW0=";
////        RSAPublicKey publicKey = (RSAPublicKey) RSAUtils.getPublicKey(pubStr);
////        RSAPrivateKey privateKey = (RSAPrivateKey) RSAUtils.getPrivateKey(priStr);
////
////        //模
////        String modulus = publicKey.getModulus().toString();
////        //公钥指数
////        String public_exponent = publicKey.getPublicExponent().toString();
////        //私钥指数
////        String private_exponent = privateKey.getPrivateExponent().toString();
////        //使用模和指数生成公钥和私钥
////        RSAPublicKey pubKey = RSAUtils.getPublicKey(modulus, public_exponent);
////        RSAPrivateKey priKey = RSAUtils.getPrivateKey(modulus, private_exponent);
////
////
////        String mingwen = "待加密的明文内容66666666，你不懂";
////        System.out.println("要加密的明文：" + mingwen);
////        String miStr = RSAUtils.encryptByPublicKey(mingwen, pubKey);
////        System.out.println("公钥加密后的数据：" + miStr);
////
////        String jiemihou = RSAUtils.decryptByPrivateKey(miStr, priKey);
////        System.out.println("用私钥解密后的数据：" + jiemihou);
//    }
//}
