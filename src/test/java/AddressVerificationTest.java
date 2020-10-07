import com.google.gson.Gson;
import com.payworks.handler.impl.RestAssuredHandler;
import com.payworks.request.impl.AddressVerificationRequest;
import com.payworks.response.RestResponse;
import com.payworks.response.impl.AddressResponse;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;

public class AddressVerificationTest {
    private Gson g = new Gson();
    private RestAssuredHandler handler = new RestAssuredHandler();

        /*
    Things to clarify with business


    what is the expectations when address contains unicode?
    what is the expectations when address contains whitespace?
    what is the expectations when no address line1 is provided?
    what is the expectations happens when no address line2 is provided?

    Current assertions are simply based on the response from the server, assuming values send by the server is correct.
    In ideal case expectations of the return values should be coming from business analyst or product owner in user story/epic.

    There are some values in response. Logic for populating some values in response is unknown,
    Hence this is not covered as part of the assertion. for example zip, zip4, value of latitude, longitude and many more.
    */
    
    @Test
    public void test_valid_address() {
        String addressLine1 = "506 Fourth Avenue Unit 1";
        String addressLine2 = "Asbury Prk NJ";
        AddressVerificationRequest request = createAddressVerificationRequest(addressLine1, addressLine2);

        RestResponse rsp = handler.sendRequest(request);
        System.out.println(rsp);
        assertEquals(200,rsp.getStatusCode());
        
        AddressResponse response = g.fromJson(rsp.getResponseBody(), AddressResponse.class);

        assertEquals(200,rsp.getStatusCode());
        assertEquals("0", response.getErrorCode());
        assertEquals("", response.getErrorMessage());
        assertEquals("506 4TH AVE APT 1", response.getAddressLine1());
        assertEquals("ASBURY PARK, NJ 07712-6086", response.getAddressLine2());
        assertEquals("506", response.getNumber());
        assertEquals("ASBURY PARK", response.getCity());
        assertEquals("NJ", response.getState());

        assertAddressLine1(response);
        assertAddressLine2(response);
    }

    @Test
    public void test_valid_address_in_CAPITAL_letters() {
        /* Test Data */
        String addressLine1 = "506 FOURTH AVENUE UNIT 1";
            String addressLine2 = "ASBURY PRK NJ";
        AddressVerificationRequest request = createAddressVerificationRequest(addressLine1, addressLine2);
        RestResponse rsp = handler.sendRequest(request);
        System.out.println(rsp);
        assertEquals(200,rsp.getStatusCode());
        AddressResponse response = g.fromJson(rsp.getResponseBody(), AddressResponse.class);

        assertEquals(200,rsp.getStatusCode());
        assertEquals("0", response.getErrorCode());
        assertEquals("", response.getErrorMessage());
        assertEquals("506 4TH AVE APT 1", response.getAddressLine1());
        assertEquals("ASBURY PARK, NJ 07712-6086", response.getAddressLine2());
        assertEquals("506", response.getNumber());
        assertEquals("ASBURY PARK", response.getCity());
        assertEquals("NJ", response.getState());
        assertAddressLine1(response);
        assertAddressLine2(response);
    }

    //TODO ask business about the expectations on white space
    @Test
    public void test_address_corner_case_whitespace() {
        /* Test Data */
        String addressLine1 = "       506             Fourth         Avenue Unit 1  ";
        String addressLine2 = "       Asbury                 Prk      NJ     ";
        AddressVerificationRequest request = createAddressVerificationRequest(addressLine1, addressLine2);
        RestResponse rsp = handler.sendRequest(request);
        System.out.println(rsp);
        assertEquals(200,rsp.getStatusCode());
        AddressResponse response = g.fromJson(rsp.getResponseBody(), AddressResponse.class);

        assertEquals(200,rsp.getStatusCode());
        assertEquals("0", response.getErrorCode());
        assertEquals("", response.getErrorMessage());
        assertEquals("506 4TH AVE APT 1", response.getAddressLine1());
        assertEquals("ASBURY PARK, NJ 07712-6086", response.getAddressLine2());
    }


    //TODO ask business about the expectations on unicode chars
    @Test
    public void test_address_with_unicode_chars() {
        String addressLine1 = "       506             Fourth         Avenue Unit 1  ^^•Ü";
        String addressLine2 = "Asbury Prk NJ æÜ…";
        AddressVerificationRequest request = createAddressVerificationRequest(addressLine1, addressLine2);
        RestResponse rsp = handler.sendRequest(request);
        System.out.println(rsp);
        assertEquals(200,rsp.getStatusCode());
        AddressResponse response = g.fromJson(rsp.getResponseBody(), AddressResponse.class);

        assertEquals(200,rsp.getStatusCode());
        assertEquals("0", response.getErrorCode());
        assertEquals("", response.getErrorMessage());
        assertEquals("506 4TH AVE APT 1", response.getAddressLine1());
        assertEquals("ASBURY PARK, NJ 07712-6086", response.getAddressLine2());
    }

    @Test
    public void test_invalid_address() {
        AddressVerificationRequest request = createAddressVerificationRequest("34 Kirchheim Bei Munchen 1", "Germany Munich DE");
        RestResponse rsp = handler.sendRequest(request);
        System.out.println(rsp);
        AddressResponse response = g.fromJson(rsp.getResponseBody(), AddressResponse.class);

        assertEquals(200,rsp.getStatusCode());
        assertEquals("4", response.getErrorCode());
        assertEquals("City not found in state", response.getErrorMessage());
        assertEquals("34 KIRCHHEIM BEI MUNCHEN 1", response.getAddressLine1());
        assertEquals("GERMANY MUNICH DE", response.getAddressLine2());
        assertEquals("34", response.getNumber());
        assertEquals("GERMANY MUNICH", response.getCity());
        assertEquals("DE", response.getState());
    }


    @Test
    public void test_invalid_address_missing_addressLine1() {
        AddressVerificationRequest request = new AddressVerificationRequest("api/address");
        request.addHeader("accept", "application/json");
        request.addParameter("AddressLine2","Germany Munich DE")
                .addParameter("userkey", "");
        RestResponse rsp = handler.sendRequest(request);
        System.out.println(rsp);
        AddressResponse response = g.fromJson(rsp.getResponseBody(), AddressResponse.class);

        assertEquals(200,rsp.getStatusCode());
        assertEquals("4", response.getErrorCode());
        assertEquals("City not found in state", response.getErrorMessage());
        assertEquals("", response.getAddressLine1());
        assertEquals("GERMANY MUNICH DE", response.getAddressLine2());
        assertEquals("", response.getNumber());
        assertEquals("GERMANY MUNICH", response.getCity());
        assertEquals("DE", response.getState());
    }
    @Test
    public void test_invalid_address_missing_addressLine2() {
        AddressVerificationRequest request = new AddressVerificationRequest("api/address");
        request.addHeader("accept", "application/json");
        request.addParameter("AddressLine1","34 KIRCHHEIM BEI MUNCHEN 1")
                .addParameter("userkey", "");
        RestResponse rsp = handler.sendRequest(request);
        System.out.println(rsp);
        AddressResponse response = g.fromJson(rsp.getResponseBody(), AddressResponse.class);

        assertEquals(200,rsp.getStatusCode());
        assertEquals("2", response.getErrorCode());
        assertEquals("Invalid address: invalid City-State-Zip line", response.getErrorMessage());
        assertEquals("34 KIRCHHEIM BEI MUNCHEN 1", response.getAddressLine1());
        assertEquals("", response.getAddressLine2());
        assertEquals("", response.getNumber()); //TODO check with business is it ok to not have Number in response in this error case
        assertEquals("", response.getCity());
        assertEquals("", response.getState());
    }

    @Test
    public void test_invalid_address_empty_addressLine1() {
        AddressVerificationRequest request = createAddressVerificationRequest("", "Germany Munich DE");
        RestResponse rsp = handler.sendRequest(request);
        System.out.println(rsp);
        AddressResponse response = g.fromJson(rsp.getResponseBody(), AddressResponse.class);

        assertEquals(200,rsp.getStatusCode());
        assertEquals("4", response.getErrorCode());
        assertEquals("City not found in state", response.getErrorMessage());
        assertEquals("", response.getAddressLine1());
        assertEquals("GERMANY MUNICH DE", response.getAddressLine2());
        assertEquals("", response.getNumber());
        assertEquals("GERMANY MUNICH", response.getCity());
        assertEquals("DE", response.getState());
    }

    @Test
    public void test_invalid_address_empty_addressLine2() {
        AddressVerificationRequest request = createAddressVerificationRequest("34 KIRCHHEIM BEI MUNCHEN 1", "");
        RestResponse rsp = handler.sendRequest(request);
        System.out.println(rsp);
        AddressResponse response = g.fromJson(rsp.getResponseBody(), AddressResponse.class);

        assertEquals(200,rsp.getStatusCode());
        assertEquals("2", response.getErrorCode());
        assertEquals("Invalid address: invalid City-State-Zip line", response.getErrorMessage());
        assertEquals("34 KIRCHHEIM BEI MUNCHEN 1", response.getAddressLine1());
        assertEquals("", response.getAddressLine2());
        assertEquals("", response.getNumber());
        assertEquals("", response.getCity());
        assertEquals("", response.getState());
    }

    @Test
    public void test_invalid_address_missing_addressLine1_and_addressLine2() {
        AddressVerificationRequest request = new AddressVerificationRequest("api/address");
        request.addHeader("accept", "application/json");
        request.addParameter("userkey", "");

        RestResponse rsp = handler.sendRequest(request);
        System.out.println(rsp);
        AddressResponse response = g.fromJson(rsp.getResponseBody(), AddressResponse.class);

        assertEquals(200,rsp.getStatusCode());
        assertEquals("2", response.getErrorCode());
        assertEquals("Invalid address: invalid City-State-Zip line", response.getErrorMessage());
        assertEquals("", response.getAddressLine1());
        assertEquals("", response.getAddressLine2());
        assertEquals("", response.getNumber());
        assertEquals("", response.getCity());
        assertEquals("", response.getState());
    }

    @Test
    public void test_invalid_address_empty_addressLine1_and_addressLine2() {
        AddressVerificationRequest request = createAddressVerificationRequest("", "");

        RestResponse rsp = handler.sendRequest(request);
        System.out.println(rsp);
        AddressResponse response = g.fromJson(rsp.getResponseBody(), AddressResponse.class);

        assertEquals(200,rsp.getStatusCode());
        assertEquals("2", response.getErrorCode());
        assertEquals("Invalid address: invalid City-State-Zip line", response.getErrorMessage());
        assertEquals("", response.getAddressLine1());
        assertEquals("", response.getAddressLine2());
        assertEquals("", response.getNumber());
        assertEquals("", response.getCity());
        assertEquals("", response.getState());
    }

    @Test
    public void test_invalid_address_cross_site_scripting() {
        String addressLine1 = "<script type=\"text/javascript\">alert(\"Hello World\")</script>";
        String addressLine2 = "ASBURY PRK NJ";
        AddressVerificationRequest request = createAddressVerificationRequest(addressLine1, addressLine2);
        RestResponse rsp = handler.sendRequest(request);
        System.out.println(rsp);
        AddressResponse response = g.fromJson(rsp.getResponseBody(), AddressResponse.class);

        assertEquals(200,rsp.getStatusCode());
        assertEquals("2", response.getErrorCode());
        assertEquals("Invalid address: invalid street address line", response.getErrorMessage());
        assertEquals(addressLine1.toUpperCase(), response.getAddressLine1());
        assertEquals("ASBURY PARK, NJ", response.getAddressLine2()); // due to transformation we cannot simply use the variable, We should know hoe to transform.
        assertEquals("", response.getNumber());
        assertEquals("ASBURY PARK", response.getCity());
        assertEquals("NJ", response.getState());
    }
    private AddressVerificationRequest createAddressVerificationRequest(String addressLine1, String addressLine2) {
        AddressVerificationRequest request = new AddressVerificationRequest("api/address");
        request.addHeader("accept", "application/json");
        request.addParameter("AddressLine1", addressLine1)
                .addParameter("AddressLine2", addressLine2)
                .addParameter("userkey", "");
        return request;
    }

    private void assertAddressLine1 (AddressResponse response) {
        String expectedAddressLine1 = response.getNumber()+" "+response.getStreet()+" "+response.getSuffix()+" "+response.getSec()+" "+response.getSecNumber();
        assertEquals(expectedAddressLine1, response.getAddressLine1());
    }
    private void assertAddressLine2 (AddressResponse response) {
        String expectedAddressLine2 = response.getCity()+", "+response.getState()+" "+response.getZip()+"-"+response.getZip4();
        assertEquals(expectedAddressLine2, response.getAddressLine2());
    }

}
