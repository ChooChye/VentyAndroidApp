package com.choochyemeilin.lamlam.helpers


class Products {
    var id: String? = null
    var category: String? = null
    var product_name: String? = null
    var desc: String? = null
    var price: String = ""
    var qty: String = ""
    var image: String = ""
    var status: String? = null
    var loanDate: String? = null
    var returnDate: String? = null
    var remarks: String = ""
    var staffID : Int = 0
    var scannedQty : Int = 0

    constructor() {

    }

    constructor(
        id: String?,
        category: String?,
        product_name: String?,
        desc: String?,
        price: String,
        qty: String,
        image: String,
        status: String?,
        loanDate: String?,
        returnDate: String?,
        remarks: String
    ) {
        this.id = id
        this.category = category
        this.product_name = product_name
        this.desc = desc
        this.price = price
        this.qty = qty
        this.image = image
        this.status = status
        this.loanDate = loanDate
        this.returnDate = returnDate
        this.remarks=remarks
    }

    //Constructor for Scan History
    constructor(
        id: String?,
        category: String?,
        product_name: String?,
        desc: String?,
        price: String,
        qty: String,
        image: String,
        remarks: String,
        staffID : Int,
        scannedQty : Int
    ) {
        this.id = id
        this.category = category
        this.product_name = product_name
        this.desc = desc
        this.price = price
        this.qty = qty
        this.image = image
        this.remarks=remarks
        this.staffID = staffID
        this.scannedQty = scannedQty
    }
}