//import com.fasterxml.jackson.databind.ObjectMapper
//import com.ngumo.inventoryapi.Controllers.Inventory.RegisterSale
//import com.ngumo.inventoryapi.DTOs.SaleDto
//import com.ngumo.inventoryapi.Services.SaleService
//import com.ngumo.inventoryapi.Utils.Commons
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.extension.ExtendWith
//import org.mockito.InjectMocks
//import org.mockito.Mock
//import org.mockito.Mockito.`when`
//import org.mockito.junit.jupiter.MockitoExtension
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
//import org.springframework.http.MediaType
//import org.springframework.security.core.userdetails.User
//import org.springframework.security.core.userdetails.UserDetails
//import org.springframework.test.web.servlet.MockMvc
//import org.springframework.test.web.servlet.post
//
//@ExtendWith(MockitoExtension::class)
//@WebMvcTest(RegisterSale::class)
//class SaleTest(
//
//       val commons: Commons
//) {
//
//    @Autowired
//    private lateinit var mockMvc: MockMvc
//
//
//    @Mock
//    private lateinit var saleService: SaleService
//
//    @InjectMocks
//    private lateinit var saleController: RegisterSale
//
//    @Test
//    fun `test stageSale endpoint`() {
//        val saleDto = SaleDto()
//        // Mock behavior of dependencies
//        val user = User.withUsername()
//        `when`(commons.getLoggedInUser()).thenReturn(user)
//        `when`(saleService.registerSale(user, saleDto)).thenReturn(/*provide expected return value*/)
//
//        // Perform the POST request to the endpoint
//        mockMvc.post("/sale") {
//            contentType = MediaType.APPLICATION_JSON
//            content = ObjectMapper().writeValueAsString(saleDto)
//        }.andExpect {
//            status { isOk() }
//            // Add additional assertions as needed
//        }
//    }
//}
