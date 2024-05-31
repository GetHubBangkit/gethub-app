package com.entre.gethub.data.remote.response.ml

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProjectDetectorResponse(

    @field:SerializedName("results")
    val results: List<Result>? = null,

    @field:SerializedName("totals")
    val totals: Totals? = null,

    @field:SerializedName("conclusion")
    val conclusion: Conclusion? = null,

    @field:SerializedName("insight")
    val insight: List<Insight>? = null,

    @field:SerializedName("error_code")
    val errorCode: Int? = null,

    @field:SerializedName("message")
    val message: String? = null
) : Parcelable {

    @Parcelize
    data class Result(
        @field:SerializedName("prediction")
        val prediction: String? = null,

        @field:SerializedName("accuracy")
        val accuracy: Double? = null,

        @field:SerializedName("text")
        val text: String? = null,

        @field:SerializedName("sentiment")
        val sentiment: String? = null,

        @field:SerializedName("sentiment_accuracy")
        val sentimentAccuracy: Double? = null
    ) : Parcelable

    @Parcelize
    data class Totals(
        @field:SerializedName("total_fraud")
        val totalFraud: Int? = null,

        @field:SerializedName("total_real_job")
        val totalRealJob: Int? = null,

        @field:SerializedName("total_netral")
        val totalNeutral: Int? = null,

        @field:SerializedName("total_positif")
        val totalPositive: Int? = null,

        @field:SerializedName("total_negatif")
        val totalNegative: Int? = null
    ) : Parcelable

    @Parcelize
    data class Conclusion(
        @field:SerializedName("conclusion_flag")
        val conclusionFlag: String? = null,

        @field:SerializedName("conclusion_text")
        val conclusionText: String? = null
    ) : Parcelable

    @Parcelize
    data class Insight(
        @field:SerializedName("text")
        val text: String? = null
    ) : Parcelable
}
