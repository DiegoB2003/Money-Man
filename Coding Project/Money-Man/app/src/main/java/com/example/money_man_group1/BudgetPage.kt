package com.example.money_man_group1

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Pie

class BudgetPage : AppCompatActivity() {
    private val pieChartData: ArrayList<DataEntry> = ArrayList() //ArrayList to hold data for pie chart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_budget_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val anyChartView = findViewById<AnyChartView>(R.id.any_chart_view) //gets pie chart object from xml page using its id
        val pieChart = AnyChart.pie() //creates pie chart from library
        pieChart.data(pieChartData) //sets the array of data to the pie chart to display
        anyChartView.setChart(pieChart)

        //Calls addToChart function using name and cost of what the user spent
        addToChart("Education", 1000)
        addToChart("Food", 250)
        addToChart("Child", 439)
        addToChart("Hobbies", 75)
    }

    //function adds name and cost of what user spend into the
    //pieChartData arraylist gets the pie chart object,
    // creates a pie chart, and updates pie chart view with new values
    private fun addToChart (name: String, cost: Int) {
        pieChartData.add(ValueDataEntry(name, cost)) //adds values to arraylist

        val anyChartView = findViewById<AnyChartView>(R.id.any_chart_view) //gets piechart object from xml page using its id
        val pieChart = AnyChart.pie() //creates pie chart from library
        pieChart.data(pieChartData)//sets the array of data to the pie chart to display
        anyChartView.setChart(pieChart) //updates pie chart view
    }
}