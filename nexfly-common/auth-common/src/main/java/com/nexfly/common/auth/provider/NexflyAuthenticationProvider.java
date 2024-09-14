package com.nexfly.common.auth.provider;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMDecryptorProvider;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.password.CompromisedPasswordException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.Cipher;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.Security;
import java.util.Base64;

/**
 * @Author wangjun
 * @Date 2024/9/13
 **/
public class NexflyAuthenticationProvider extends DaoAuthenticationProvider {

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            this.logger.debug("Failed to authenticate since no credentials provided");
            throw new BadCredentialsException(this.messages
                    .getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }
        String presentedPassword = authentication.getCredentials().toString();
        String decryptPassword;
        try {
            decryptPassword = RSADecrypt.decrypt(presentedPassword);
        } catch (Exception e) {
            throw new CompromisedPasswordException(e.getMessage());
        }
        if (!getPasswordEncoder().matches(decryptPassword, userDetails.getPassword())) {
            this.logger.debug("Failed to authenticate since password does not match stored value");
            throw new BadCredentialsException(this.messages
                    .getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }
    }

    /**
     * rsa密钥对生成
     * 1、生成私钥
     *    openssl genrsa -out nexfly_private_key.pem 1024
     * 2、根据私钥生成公钥
     *    openssl rsa -in nexfly_private.pem -pubout -out nexfly_public.pem
     * 3、对私钥进行加密
     *    openssl rsa -in nexfly_private.pem -out encrypted_private.pem -des3 -passout pass:Nexfly -traditional
     */
    public static class RSADecrypt {

        private static final PrivateKey privateKey;

        static {
            Security.addProvider(new BouncyCastleProvider());

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ClassPathResource("nexfly_private.pem").getInputStream()))) {
                PEMParser pemParser = new PEMParser(reader);
                Object object = pemParser.readObject();

                if (object instanceof PEMEncryptedKeyPair encryptedKeyPair) {
                    PEMDecryptorProvider decryptProvider = new JcePEMDecryptorProviderBuilder().build("Nexfly".toCharArray());
                    PEMKeyPair keyPair = encryptedKeyPair.decryptKeyPair(decryptProvider);
                    JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
                    privateKey = converter.getPrivateKey(keyPair.getPrivateKeyInfo());
                } else {
                    throw new IllegalArgumentException("无效的密钥格式");
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        // 解密函数
        public static String decrypt(String encryptedText) throws Exception {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            // Base64 解码密文
            byte[] decodedEncryptedText = Base64.getDecoder().decode(encryptedText.getBytes(StandardCharsets.UTF_8));
            byte[] decryptedBytes = cipher.doFinal(decodedEncryptedText);

            return new String(Base64.getDecoder().decode(new String(decryptedBytes, StandardCharsets.UTF_8)));
        }

    }

}
