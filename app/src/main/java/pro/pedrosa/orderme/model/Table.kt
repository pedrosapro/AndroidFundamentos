package pro.pedrosa.orderme.model

import java.io.Serializable

data class Table (var name: String, var order: MutableList<Order>) : Serializable{

    constructor(name : String) :  this(name, mutableListOf())

    override fun toString() = name

    fun joinOrder (orderTojoin:Order){

        // Compruebo si existe sino lo añado
        if(order.contains(orderTojoin)){
           val iOrder = order.indexOf(orderTojoin)
            val oNumber = orderTojoin.number

            val orderOriginal = order.get(iOrder)
            orderOriginal.number = orderOriginal.number + oNumber
            orderOriginal.joinComments(orderTojoin)

            // Delete & insert
            order.removeAt(iOrder)
            order.add(iOrder, orderOriginal)

        } else {
            order.add(orderTojoin)
        }
    }

    //TODO Que devuelva precios con decimales y price no puede ser nunca null
    fun priceOrder () : Int {
        var totalPrice = 0
        order.forEach {totalPrice += it.dish.price!! * it.number}
        return totalPrice
    }

    fun restart() {
        order = mutableListOf()
    }
}