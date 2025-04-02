package com.example.skysync.models

data class LocalNames(
	val tk: String? = null,
	val es: String? = null
)

data class SearchLocationsResponseItem(
	val country: String? = null,
	val name: String? = null,
	val lon: Any? = null,
	val state: String? = null,
	val lat: Any? = null,
	val localNames: LocalNames? = null
)

