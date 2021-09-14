package ru.maggy.markersmap.util

import android.os.Bundle
import com.google.android.gms.maps.model.LatLng
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object PositionArg: ReadWriteProperty<Bundle, LatLng?> {
    override fun setValue(thisRef: Bundle, property: KProperty<*>, value: LatLng?) {
        thisRef.putParcelable(property.name, value)
    }

    override fun getValue(thisRef: Bundle, property: KProperty<*>): LatLng? {
        return thisRef.getParcelable(property.name)
    }
}