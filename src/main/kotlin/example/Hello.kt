package example

import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import kotlin.browser.document
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun <T> watching(initValue: T): ReadWriteProperty<Any, T> {
    return object : ReadWriteProperty<Any, T> {
        private var value = initValue
        override fun getValue(thisRef: Any, property: KProperty<*>): T {
            println("getValue: ${property.name}")
            return value
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
            println("setValue: ${property.name} = $value")
            this.value = value
            propagateChanges()
        }
    }
}

fun propagateChanges() {
    showResult()
}

fun showResult() {
    document.getElementById("result")?.innerHTML = "${foo.a} + ${foo.b} = ${foo.a + foo.b}"
}


class Foo {
    var a by watching(111)
    var b by watching(222)
}

val foo = Foo()

fun init() {
    fun getValue(event: Event) = (event.target as HTMLInputElement).value

    val a = (document.getElementById("A") as HTMLInputElement?)
    a?.apply {
        this.value = foo.a.toString()
        this.onchange = { event ->
            foo.a = getValue(event).toInt()
            Unit
        }
    }

    val b = (document.getElementById("B") as HTMLInputElement?)
    b?.apply {
        this.value = foo.b.toString()
        this.onchange = { event ->
            foo.b = getValue(event).toInt()
            Unit
        }
    }
}

fun main(args: Array<String>) {
    init()
    showResult()
}
