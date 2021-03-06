@file:Suppress("unused")

package com.apboutos.moneytrack.viewmodel.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.apboutos.moneytrack.model.database.entity.Entry
import com.apboutos.moneytrack.view.LedgerActivity


class LedgerReceiver(private val parentActivity: LedgerActivity) : BroadcastReceiver() {

    private val tag = "LedgerReceiver"
    override fun onReceive(context: Context?, intent: Intent?) {

        if(intent?.action == SERVER_PULL_DATA_RESPONSE){
            Log.d(tag, "in pull action")
            val list = intent.getParcelableArrayListExtra<Entry>("entryList")
            if(list != null) {
                for (i in list){
                    Log.d(tag, "${i.id} ${i.description} ${i.date.date} ${i.lastUpdate.datetime}")
                }
                parentActivity.viewModel.updateDatabaseWithReceivedRemoteEntries(list)
            }
            parentActivity.updateLastPullRequestDatetime()
            parentActivity.hideSynchronizeProgressBar()
            parentActivity.viewModel.loadEntries()
            parentActivity.adapter.notifyDataSetChanged()
        }
        if(intent?.action == SERVER_PUSH_DATA_RESPONSE){
            if (intent.getStringExtra("error") == "NO_ERROR"){
                parentActivity.updateLastPushRequestDatetime()
                Log.d(tag, "in push action")
                Log.d(tag,"Got no error at push receive")
            }
        }

    }

    companion object {
        const val SERVER_PULL_DATA_RESPONSE = "server"
        const val SERVER_PUSH_DATA_RESPONSE = "server"
    }
}