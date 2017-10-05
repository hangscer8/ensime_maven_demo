package win.hangscer.encryption
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.spec.{DESKeySpec, IvParameterSpec, SecretKeySpec}
import javax.crypto.SecretKeyFactory
import javax.crypto.SecretKey
import javax.crypto.Cipher
//DES 一种对称加密算法 加密解密都使用同一秘钥 秘钥长度必须是8的倍数
object DES {
  //秘钥 长度必须是8的倍数
  private[this] val password:String="12345678"
  private[this] val iv:Array[Byte]=new Array[Byte](8)
  def encrypt(dataSource:String)={//传入明文，输出密文
    val zeroIv=new IvParameterSpec(iv)
    val key=new SecretKeySpec(password.getBytes,"DES")
    val ciper=Cipher.getInstance("DES/CBC/PKCS5Padding")
    ciper.init(Cipher.ENCRYPT_MODE,key,zeroIv)
    val encryptData=ciper.doFinal(dataSource.getBytes)
    Base64.getEncoder.encodeToString(encryptData)
  }
  def decrypt(encryptedString:String)={
    val byteMi=Base64.getDecoder.decode(encryptedString)
    val zeroIv=new IvParameterSpec(iv)
    val key=new SecretKeySpec(password.getBytes(),"DES")
    val cipher=Cipher.getInstance("DES/CBC/PKCS5Padding")
    cipher.init(Cipher.DECRYPT_MODE,key,zeroIv)
    val data=cipher.doFinal(byteMi)
    new String(data)
  }
}