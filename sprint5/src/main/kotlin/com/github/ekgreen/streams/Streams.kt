package com.github.ekgreen.streams

import java.util.function.Function
import java.util.stream.Collectors
import java.util.stream.Collectors.toMap


// 1. Используя withIndex() посчитать сумму элементов листа, индекс которых кратен 3. (нулевой индекс тоже входит)
fun getSumWithIndexDivisibleByThree(list: List<Long>): Long {
    return list.withIndex().filter { it.index % 3 == 0 }.map { it.value }.reduce { acc, it -> acc + it }
}

// 2. Используя функцию generateSequence() создать последовательность, возвращающую числа Фибоначчи.
fun generateFibonacciSequence(): Sequence<Int> {
    return generateSequence(Pair(0, 1)) { Pair(it.second, it.second + it.first) }.map { it.first }
}

// 3. Получить города, в которых есть покупатели.
fun Shop.getCustomersCities(): Set<City> {
    return customers.asSequence()
        .map { it.city }.toSet()
}

// 4. Получить все когда-либо заказанные продукты.
fun Shop.allOrderedProducts(): Set<Product> {
    return customers.asSequence()
        .flatMap { it.orders }
        .flatMap { it.products }
        .toSet()
}

// 5. Получить покупателя, который сделал больше всего заказов.
fun Shop.getCustomerWithMaximumNumberOfOrders(): Customer? {
    return customers
        .maxWithOrNull { o1, o2 -> o1.orders.size - o2.orders.size }
}

// 6. Получить самый дорогой продукт, когда-либо приобретенный покупателем.
fun Customer.getMostExpensiveProduct(): Product? {
    return orders.asSequence()
        .flatMap { it.products }
        .maxWithOrNull { o1, o2 -> o1.price.compareTo(o2.price) }
}

// 7. Получить соответствие в мапе: город - количество заказанных и доставленных продуктов в данный город.
fun Shop.getNumberOfDeliveredProductByCity(): Map<City, Int> {
    return customers.groupBy(
        { it.city },
        { it.orders.filter { it.isDelivered }.sumOf { it.products.size } })
        .mapValues { it.value.sum() }
}

// 8. Получить соответствие в мапе: город - самый популярный продукт в городе.
fun Shop.getMostPopularProductInCity(): Map<City, Product> {
    return customers.groupBy(
        { it.city },
        {
            it.orders.asSequence()
                .flatMap { it.products }
                .groupingBy { it }
                .eachCount()
                .maxOfWith({ e1, e2 -> e1.value.compareTo(e2.value) }, { entry -> entry })
        })
        .mapValues { entry -> entry.value.maxOfWith({ e1, e2 -> e1.value.compareTo(e2.value) }, { e -> e }).key }
}

// 9. Получить набор товаров, которые заказывали все покупатели.
fun Shop.getProductsOrderedByAll(): Set<Product> {
    return customers.asSequence()
        .map { it.orders.asSequence().flatMap { it.products }.toSet() }
        .reduce { s1, s2 -> s1.intersect(s2) }
}

