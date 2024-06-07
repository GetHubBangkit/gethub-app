package com.entre.gethub.data.remote.response.projects

import com.google.gson.annotations.SerializedName

data class FreelancerSettlementResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("error_code")
	val errorCode: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
) {

	data class Data(

		@field:SerializedName("job_status")
		val jobStatus: String? = null,

		@field:SerializedName("offer_received")
		val offerReceived: Int? = null,

		@field:SerializedName("total")
		val total: Int? = null,

		@field:SerializedName("service_fee")
		val serviceFee: Int? = null
	)

}
