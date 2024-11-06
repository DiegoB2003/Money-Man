package com.example.money_man_group1

//this class is used as a template to help store data on the userSpendingInfoData
data class UserSpendingInfo(
    var numberOfSpendingCategories: Int = 0,
    var categoryOne: Category = Category(),
    var categoryTwo: Category = Category(),
    var categoryThree: Category = Category(),
    var categoryFour: Category = Category(),
    var categoryFive: Category = Category(),
    var categorySix: Category = Category()

) {
    data class Category(
        var isCategoryActive: String = "No",
        var categoryName: String = "",
        var categoryDescription: String = "",
        var maxSpendingLimit: Double = 0.0,
        var currentMoneySpent: Double = 0.0
    )
}