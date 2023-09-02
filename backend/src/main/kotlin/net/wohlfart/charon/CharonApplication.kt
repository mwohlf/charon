package net.wohlfart.charon

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication



@SpringBootApplication
@EnableConfigurationProperties(CharonProperties::class)
class CharonApplication

fun main(args: Array<String>) {
	runApplication<CharonApplication>(*args)
}



/*
links and references:

Spring security
https://www.jianshu.com/p/6cfac79b482b


 */
