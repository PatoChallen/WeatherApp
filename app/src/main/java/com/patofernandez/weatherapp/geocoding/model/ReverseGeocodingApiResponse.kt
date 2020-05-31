package com.patofernandez.weatherapp.geocoding.model


import com.google.gson.annotations.SerializedName

data class ReverseGeocodingApiResponse(
    var state: String?,
    @SerializedName("latt")
    var latitude: String?,
    @SerializedName("city")
    var city: String?,
    @SerializedName("prov")
    var prov: String?,
    @SerializedName("country")
    var country: String?,
    @SerializedName("inlatt")
    var inlatt: String?,
    @SerializedName("timezone")
    var timezone: String?,
    @SerializedName("region")
    var region: String?,
    @SerializedName("longt")
    var longitude: String?,
    @SerializedName("inlongt")
    var inlongt: String?
)