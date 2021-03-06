package com.choochyemeilin.lamlam.Loans.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.choochyemeilin.lamlam.Loans.Classes.LoanApplication
import com.choochyemeilin.lamlam.R
import com.choochyemeilin.lamlam.helpers.FbCallback
import com.choochyemeilin.lamlam.helpers.Utils
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.loans_pending_list_layout.view.*

class LoansPendingAdapter(
    private val dataList: List<LoanApplication>
) : RecyclerView.Adapter<LoansPendingAdapter.ViewHolder>() {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var role = "staff"

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.loan_tv_title
        val date = itemView.loan_tv_date
        val status = itemView.loan_tv_status
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.loans_pending_list_layout, parent, false)
        return ViewHolder(itemView)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Utils.getUserRole(object : FbCallback {
            override fun onCallbackGetUserEmail(user: String) {
                super.onCallbackGetUserEmail(user)
                role = user
            }
        })
        val currentItem = dataList[position]
        val context = holder.itemView.context
        holder.title.text = "#${currentItem.loanID}"
        holder.date.text = currentItem.loanDate
        holder.status.text = currentItem.status.toUpperCase()
        if (holder.status.text.toString() == "PENDING") {
            holder.status.setTextColor(ContextCompat.getColor(context, R.color.colorAccent))
        }
        holder.itemView.setOnClickListener { showDialog(context, position) }

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    //Show Dialog
    private fun showDialog(context: Context, position: Int) {

        var builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(context)
        val data = dataList[position]
        var msg = "Date Applied : ${data.loanDate}\n\n" +
                    "Products Requested : \n"
        val x = data.productName
        var index = 1
        x.forEach {
            val key = it.key
            val qty = it.value
            msg += "$index - $key ($qty)\n"
            index++
        }
        //Build Alert Dialog
        builder
            .setTitle("LOAN ID #${dataList[position].loanID}")
            .setMessage(msg)
        if (role == "staff") {
            builder.setNegativeButton("OK") { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            }
        } else {
            builder
                .setPositiveButton("APPROVE") { dialogInterface: DialogInterface, _: Int ->
                    actionLoan(dataList[position].loanID!!.toInt(), "approved")
                    updateProdQTY(position)
                    dialogInterface.dismiss()
                }
                .setNeutralButton("CANCEL") { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                }
                .setNegativeButton("REJECT") { dialogInterface: DialogInterface, _: Int ->
                    actionLoan(dataList[position].loanID!!.toInt(), "rejected")
                    dialogInterface.dismiss()
                }
        }
        builder.show()
    }

    private fun actionLoan(loanID: Int, status: String) {

        val myRef: DatabaseReference = database.getReference("Loans")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dss in snapshot.children) {
                    dss.children.forEach {
                        val key = dss.key.toString()
                        val dbLoanID = it.child("loanID").value
                        if (dbLoanID.toString().toInt() == loanID.toString().toInt()) {
                            //update value
                            myRef.child(key).child(it.key.toString()).child("status")
                                .setValue(status.toLowerCase())
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun updateProdQTY(position: Int){
        val myRef: DatabaseReference = database.getReference("Products")
        val map = dataList[position].productName
        myRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dss in snapshot.children){
                    val key = dss.key.toString()
                    val oldValue = dss.child("qty").value.toString().toInt()
                    val productName = dss.child("product_name").value.toString()

                    if (map.keys.contains(productName)) {
                        val newValue = oldValue - map[productName].toString().toInt()
                        myRef.child(key).child("qty").setValue(newValue.toString())
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Utils.log("An error has occurred")
            }

        })
    }
}