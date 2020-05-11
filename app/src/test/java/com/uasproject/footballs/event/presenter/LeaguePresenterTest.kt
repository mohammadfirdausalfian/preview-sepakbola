package com.uasproject.footballs.event.presenter

import com.google.gson.Gson
import com.uasproject.footballs.api.ApiRepository
import com.uasproject.footballs.league.model.League
import com.uasproject.footballs.league.model.LeagueResponse
import com.uasproject.footballs.league.presenter.LeaguePresenter
import com.uasproject.footballs.util.CoroutineContextProviderTest
import com.uasproject.footballs.league.interfaces.LeagueView
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class LeaguePresenterTest {

    @Mock
    private lateinit var view: LeagueView

    @Mock
    private lateinit var gson: Gson

    @Mock
    private lateinit var apiRepo: ApiRepository

    @Mock
    private lateinit var apiResponse: Deferred<String>

    private lateinit var presenter: LeaguePresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = LeaguePresenter(
            view,
            apiRepo,
            gson,
            CoroutineContextProviderTest()
        )
    }

    @Test
    fun getData() {
        val league: MutableList<League> = mutableListOf()
        val response = LeagueResponse(league)

        runBlocking {
            Mockito.`when`(apiRepo.doRequestAsync(ArgumentMatchers.anyString()))
                .thenReturn(apiResponse)

            Mockito.`when`(apiResponse.await()).thenReturn("")

            Mockito.`when`(
                gson.fromJson(
                    "",
                    LeagueResponse::class.java
                )
            ).thenReturn(response)

            presenter.getData()
            Mockito.verify(view).showLoading()
            Mockito.verify(view).hideLoading()
            Mockito.verify(view).showData(league)
        }
    }
}