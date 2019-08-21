package com.openclassrooms.realestatemanager

import android.util.Log
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import com.openclassrooms.realestatemanager.models.RealEstate
import kotlinx.android.synthetic.main.activity_search.*

/**
 * Creates Query and arguments for raw query search in activity
 */

class SearchManager {

    //RETURN VALUES
    private var query: String? = null
    private val bindArgs = arrayListOf<String>()

    fun getQueryFromUI(agent_et: EditText?, type: String?, checkbox: List<String>?, city_et: EditText?,
                       surface_min: EditText?, surface_max: EditText?,
                       price_min: EditText?, price_max: EditText?,
                       rooms_min: EditText?, rooms_max: EditText?,
                       start_date_et: TextView?, end_date_et: TextView?,
                       photos_min_et: EditText?, photos_max_et: EditText?,
                       cb_sold: CheckBox?) {

        val sb: StringBuilder = java.lang.StringBuilder()
        var sold = false

        sb.append("SELECT * FROM RealEstate WHERE ")

        //AGENT
        if (agent_et!!.text.isNotEmpty()) {
            sb.append("agent = ? AND ")
            bindArgs.add(agent_et.text.toString())
        }

        //TYPE
        if (type!!.isNotEmpty()) {
            sb.append("type =? AND ")
            bindArgs.add(type)
        }

        //POI
        if (checkbox!!.isNotEmpty()) {
            for (box in checkbox) {
                sb.append("pointsOfInterest LIKE ? AND ")
                bindArgs.add("%$box%")
                Log.e("search ", "poi is not empty $box")
            }
        }

        //CITY
        if (city_et!!.text.isNotEmpty()) {
            sb.append("city LIKE ? AND ")
            bindArgs.add(city_et.text.toString().toLowerCase())
        }


        //SURFACE
        if (surface_min!!.text.isNotEmpty() && surface_max!!.text.isNotEmpty()) {
            sb.append("surface BETWEEN ? AND ? AND ")
            bindArgs.add(surface_min.text.toString())
            bindArgs.add(surface_max.text.toString())
        } else if (surface_min.text.isNotEmpty()) {
            sb.append("surface >= ? AND ")
            bindArgs.add(surface_min.text.toString())
        } else if (surface_max!!.text.isNotEmpty()) {
            sb.append("surface <= ? AND ")
            bindArgs.add(surface_max.text.toString())
        }

        //PRICE
        if (price_min!!.text.isNotEmpty() && price_max!!.text.isNotEmpty()) {
            sb.append("price BETWEEN ? AND ? AND ")
            bindArgs.add(price_min.text.toString())
            bindArgs.add(price_max.text.toString())
        } else if (price_min.text.isNotEmpty()) {
            sb.append("price >= ? AND ")
            bindArgs.add(surface_min.text.toString())
        } else if (price_max!!.text.isNotEmpty()) {
            sb.append("price <= ? AND ")
            bindArgs.add(price_max.text.toString())
        }

        //ROOMS
        if (rooms_min!!.text.isNotEmpty() && rooms_max!!.text.isNotEmpty()) {
            sb.append("rooms BETWEEN ? AND ? AND ")
            bindArgs.add(rooms_min.text.toString())
            bindArgs.add(rooms_max.text.toString())
        } else if (rooms_min.text.isNotEmpty()) {
            sb.append("rooms >= ? AND ")
            bindArgs.add(rooms_max!!.text.toString())
        } else if (rooms_max!!.text.isNotEmpty()) {
            sb.append("rooms <= ? AND ")
            bindArgs.add(rooms_max.text.toString())
        }

        //DATES
        val startDate = if (start_date_et!!.text == "Click to pick a date") {
            ""
        } else {
            start_date_et.text
        }
        val endDate = if (end_date_et!!.text == "Click to pick a date") {
            ""
        } else {
            end_date_et.text
        }

        Log.e("searchManager", "dates = " + startDate + endDate)
        if (startDate.isNotEmpty() && endDate.isNotEmpty()) {
            sold = true

            sb.append("startDate >= ? AND endDate <= ? AND ")
            bindArgs.add(startDate.toString())
            bindArgs.add(endDate.toString())

        } else if (startDate.isNotEmpty()) {
            if (!cb_sold!!.isChecked) sold = false
            sb.append("startDate >= ? AND ")
            bindArgs.add(startDate.toString())
        } else if (endDate.isNotEmpty()) {
            sold = true
            sb.append("endDate <= ? AND ")
            bindArgs.add(endDate.toString())
        }

        sb.append("sold = ? ")
        bindArgs.add(sold.toString())

        //END STRING
        sb.append(";")

        query = sb.toString()

        Log.e("search", query + bindArgs)

    }

    fun getQuery(): String? {
        return query
    }

    fun getArgs(): Array<String> {
        return bindArgs.toTypedArray()
    }

}