package ru.sber.services.processors

import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.beans.factory.config.BeanFactoryPostProcessor
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.stereotype.Component
import ru.sber.services.CombinedBean
import java.lang.Exception
import javax.annotation.PostConstruct

@Component
class MyBeanFactoryPostProcessor : BeanFactoryPostProcessor {
    override fun postProcessBeanFactory(beanFactory: ConfigurableListableBeanFactory) {
        beanFactory.beanNamesIterator.forEach { name ->
            try {
                val beanDefinition: BeanDefinition = beanFactory.getBeanDefinition(name)
                val beanClassName = beanDefinition.beanClassName

                // не подпирал java-config (можно брать ретерн тайп например)
                if (beanClassName != null && beanClassName.isNotEmpty()) {
                    val beanClass = Class.forName(beanClassName)

                    val map: Set<String> = beanClass.interfaces
                        .flatMap { it.declaredMethods.asSequence() }
                        .filter  { it.isAnnotationPresent(PostConstruct::class.java) }
                        .map     { it.name }
                        .toSet()

                    // тут еще какая-то логика по выбору метода
                    val initMethodName: String? = if(map.iterator().hasNext()) map.iterator().next() else null

                    beanDefinition.initMethodName = initMethodName
                }
            }catch (e: Exception){
                // TODO залогировать ошибку
            }
        }

    }
}