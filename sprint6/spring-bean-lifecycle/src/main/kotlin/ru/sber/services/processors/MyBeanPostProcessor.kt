package ru.sber.services.processors

import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.stereotype.Component
import ru.sber.services.CombinedBean

@Component
class MyBeanPostProcessor : BeanPostProcessor {

    private val combinedBeans: MutableMap<String, Class<in CombinedBean>> = HashMap()

    override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any? {

        // condition
        if(bean.javaClass == CombinedBean::class.java){
            // save origin bean class
            combinedBeans.put(beanName, bean.javaClass)

            bean as CombinedBean
            // call
            bean.postProcessBeforeInitializationOrderMessage = "postProcessBeforeInitialization() is called"
        }

        return bean
    }

    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any? {

        if(combinedBeans.containsKey(beanName)){
            bean as CombinedBean
            // call
            bean.postProcessAfterInitializationOrderMessage = "postProcessAfterInitialization() is called"
        }
        return bean
    }
}