@file:Suppress("unused")
package net.wohlfart.charon.config

import com.nimbusds.jose.jwk.Curve
import com.nimbusds.jose.jwk.ECKey
import com.nimbusds.jose.jwk.OctetSequenceKey
import com.nimbusds.jose.jwk.RSAKey
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.spec.ECFieldFp
import java.security.spec.EllipticCurve
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import java.math.BigInteger
import java.security.interfaces.ECPrivateKey
import java.security.interfaces.ECPublicKey
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.ECParameterSpec
import java.security.spec.ECPoint
import java.util.*


fun generateRsa(): RSAKey {
    val keyPair: KeyPair = generateRsaKey()
    val publicKey: RSAPublicKey = keyPair.public as RSAPublicKey
    val privateKey: RSAPrivateKey = keyPair.private as RSAPrivateKey
    return RSAKey.Builder(publicKey)
        .privateKey(privateKey)
        .keyID(UUID.randomUUID().toString())
        .build()
}

fun generateEc(): ECKey {
    val keyPair: KeyPair = generateEcKey()
    val publicKey = keyPair.public as ECPublicKey
    val privateKey = keyPair.private as ECPrivateKey
    return ECKey.Builder(Curve.forECParameterSpec(publicKey.params), publicKey)
        .privateKey(privateKey)
        .keyID(UUID.randomUUID().toString())
        .build()
}

fun generateSecret(): OctetSequenceKey {
    return OctetSequenceKey.Builder(generateSecretKey())
        .keyID(UUID.randomUUID().toString())
        .build()
}

fun generateSecretKey(): SecretKey {
    return try {
        KeyGenerator.getInstance("HmacSha256").generateKey()
    } catch (ex: Exception) {
        throw IllegalStateException(ex)
    }
}

fun generateRsaKey(): KeyPair {
    return try {
        val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
        keyPairGenerator.initialize(2048)
        keyPairGenerator.generateKeyPair()
    } catch (ex: java.lang.Exception) {
        throw IllegalStateException(ex)
    }
}

fun generateEcKey(): KeyPair {
    val ellipticCurve = EllipticCurve(
        ECFieldFp(
            BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951")
        ),
        BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853948"),
        BigInteger("41058363725152142129326129780047268409114441015993725554835256314039467401291")
    )
    val ecPoint = ECPoint(
        BigInteger("48439561293906451759052585252797914202762949526041747995844080717082404635286"),
        BigInteger("36134250956749795798585127919587881956611106672985015071877198253568414405109")
    )
    val ecParameterSpec = ECParameterSpec(
        ellipticCurve,
        ecPoint,
        BigInteger("115792089210356248762697446949407573529996955224135760342422259061068512044369"),
        1
    )
    return try {
        val keyPairGenerator = KeyPairGenerator.getInstance("EC")
        keyPairGenerator.initialize(ecParameterSpec)
        keyPairGenerator.generateKeyPair()
    } catch (ex: java.lang.Exception) {
        throw IllegalStateException(ex)
    }
}

