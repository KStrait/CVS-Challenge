package com.kls.cvschallenge

import com.kls.cvschallenge.data.FlickrImage
import com.kls.cvschallenge.data.FlickrResponse
import com.kls.cvschallenge.data.Media
import com.kls.cvschallenge.network.WebService
import com.kls.cvschallenge.repo.FlickrRepository
import io.mockk.*
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test
import com.kls.cvschallenge.data.Result
import junit.framework.TestCase.fail
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest

@ExperimentalCoroutinesApi
class FlickrRepositoryTest {

    private lateinit var flickrRepository: FlickrRepository
    private val mockWebService: WebService = mockk()

    @Before
    fun setUp() {
        flickrRepository = FlickrRepository(mockWebService)
    }

    @Test
    fun `getFlickrItems returns success when data is fetched successfully`() = runTest {
        val search = "Some test search"
        val expectedResponse = goodResponse

        coEvery { mockWebService.getFlickrImages(search) } returns expectedResponse

        val resultList = flickrRepository.getFlickrItems(search).toList()

        when (val finalResult = resultList.last()) {
            is Result.Success -> {
                // Assert that the data inside Success is what you expect
                assertEquals(goodResponse, finalResult.data)
            }
            else -> fail("Expected Success but got $finalResult")
        }
    }
    @Test
    fun `getFlickrItems returns error when an exception occurs`() = runTest {
        val search = "test search"
        val exception = Exception("Network error")

        coEvery { mockWebService.getFlickrImages(search) } throws exception

        val result = flickrRepository.getFlickrItems(search)
            .catch { e -> emit(Result.Error(e)) }
            .toList()

        //First should be Loading, second should be Error
        assertTrue(result[1] is Result.Error)
        assertEquals(exception, (result[1] as Result.Error).exception)
    }


    @Test
    fun `getFlickrItems emits loading state before making network call`() = runTest {
        val search = "test search"
        val expectedResponse = goodResponse
        coEvery { mockWebService.getFlickrImages(search) } returns expectedResponse

        val flow = flickrRepository.getFlickrItems(search)

        var emittedLoading = false
        flow.collect { result ->
            if (result is Result.Loading) {
                emittedLoading = true
                assertTrue(result is Result.Loading) // Assert Loading state
            } else if (result is Result.Success) {
                assertTrue(result is Result.Success) // Assert Success state
                assertEquals(expectedResponse, result.data)
            }
        }

        assertTrue(emittedLoading)
    }

    private val goodResponse = FlickrResponse(
        title = "Test Response",
        link = "https://www.flickr.com/",
        description = "This is a test response description",
        modified = "2025-01-01T00:00:00Z",
        generator = "Flickr API",
        items = listOf(
            FlickrImage(
                title = "Test Image 1",
                link = "https://www.flickr.com/photos/owner1/12345",
                media = Media(m = "https://live.staticflickr.com/65535/12345_abcde_s.jpg"),
                date_taken = "2025-01-01T12:00:00Z",
                description = "Description of Test Image 1",
                published = "2025-01-01T12:01:00Z",
                author = "owner1",
                author_id = "12345",
                tags = "test, image1"
            ),
            FlickrImage(
                title = "Test Image 2",
                link = "https://www.flickr.com/photos/owner2/67890",
                media = Media(m = "https://live.staticflickr.com/65535/67890_fghij_s.jpg"),
                date_taken = "2025-01-02T12:00:00Z",
                description = "Description of Test Image 2",
                published = "2025-01-02T12:01:00Z",
                author = "owner2",
                author_id = "67890",
                tags = "test, image2"
            )
        )
    )
}
