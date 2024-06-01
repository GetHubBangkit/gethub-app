package com.entre.gethub.data.remote.response.ml

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProjectDetectorResponse(
    @SerializedName("error_code")
    val errorCode: Int,
    @SerializedName("data")
    val data: Data,
    @SerializedName("message")
    val message: String
) : Parcelable {
    @Parcelize
    data class Data(
        @SerializedName("contents")
        val contents: String,
        @SerializedName("results")
        val results: List<Result>,
        @SerializedName("totals")
        val totals: Totals,
        @SerializedName("conclusion")
        val conclusion: Conclusion,
        @SerializedName("insight")
        val insight: List<Insight>
    ) : Parcelable

    @Parcelize
    data class Result(
        @SerializedName("prediction")
        val prediction: String,
        @SerializedName("accuracy")
        val accuracy: Double,
        @SerializedName("text")
        val text: String,
        @SerializedName("sentiment")
        val sentiment: String,
        @SerializedName("sentiment_accuracy")
        val sentimentAccuracy: Double
    ) : Parcelable

    @Parcelize
    data class Totals(
        @SerializedName("total_fraud")
        val totalFraud: Int,
        @SerializedName("total_real_job")
        val totalRealJob: Int,
        @SerializedName("total_netral")
        val totalNeutral: Int,
        @SerializedName("total_positif")
        val totalPositive: Int,
        @SerializedName("total_negatif")
        val totalNegative: Int
    ) : Parcelable

    @Parcelize
    data class Conclusion(
        @SerializedName("conclusion_flag")
        val conclusionFlag: String,
        @SerializedName("conclusion_text")
        val conclusionText: String
    ) : Parcelable

    @Parcelize
    data class Insight(
        @SerializedName("text")
        val text: String
    ) : Parcelable
}
