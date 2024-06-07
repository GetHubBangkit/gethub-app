package com.entre.gethub.data.remote.response.projects

import com.google.gson.annotations.SerializedName

data class AllBanksResponse(

	@field:SerializedName("data")
	val data: List<DataItem>,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("error_code")
	val errorCode: Int? = null
) {
	data class DataItem(

		@field:SerializedName("bank_name")
		val bankName: String? = null
	)
}
