package org.jclouds.openstack.nova.v1_1.features;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.net.URI;

import org.jclouds.http.HttpRequest;
import org.jclouds.http.HttpResponse;
import org.jclouds.openstack.nova.v1_1.NovaClient;
import org.jclouds.openstack.nova.v1_1.internal.BaseNovaRestClientExpectTest;
import org.jclouds.openstack.nova.v1_1.parse.ParseFlavorListTest;
import org.jclouds.openstack.nova.v1_1.parse.ParseFlavorTest;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;

/**
 * Tests annotation parsing of {@code FlavorAsyncClient}
 * 
 * @author Jeremy Daggett
 */
@Test(groups = "unit", testName = "FlavorAsyncClientTest")
public class FlavorClientExpectTest extends BaseNovaRestClientExpectTest {

   public void testListFlavorsWhenResponseIs2xx() throws Exception {
      HttpRequest listServers = HttpRequest.builder().method("GET").endpoint(
               URI.create("https://compute.north.host/v1.1/3456/flavors")).headers(
               ImmutableMultimap.<String, String> builder().put("Accept", "application/json").put("X-Auth-Token",
                        authToken).build()).build();

      HttpResponse listFlavorsResponse = HttpResponse.builder().statusCode(200).payload(
               payloadFromResource("/flavor_list.json")).build();

      NovaClient clientWhenFlavorsExist = requestsSendResponses(keystoneAuthWithAccessKeyAndSecretKey,
               responseWithKeystoneAccess, listServers, listFlavorsResponse);

      assertEquals(clientWhenFlavorsExist.getConfiguredRegions(), ImmutableSet.of("North"));

      assertEquals(clientWhenFlavorsExist.getFlavorClientForRegion("North").listFlavors().toString(),
               new ParseFlavorListTest().expected().toString());
   }

   public void testListFlavorsWhenReponseIs404IsEmpty() throws Exception {
      HttpRequest listFlavors = HttpRequest.builder().method("GET").endpoint(
               URI.create("https://compute.north.host/v1.1/3456/flavors")).headers(
               ImmutableMultimap.<String, String> builder().put("Accept", "application/json").put("X-Auth-Token",
                        authToken).build()).build();

      HttpResponse listFlavorsResponse = HttpResponse.builder().statusCode(404).build();

      NovaClient clientWhenNoServersExist = requestsSendResponses(keystoneAuthWithAccessKeyAndSecretKey,
               responseWithKeystoneAccess, listFlavors, listFlavorsResponse);

      assertTrue(clientWhenNoServersExist.getFlavorClientForRegion("North").listFlavors().isEmpty());
   }

   // TODO: gson deserializer for Multimap
   public void testGetFlavorWhenResponseIs2xx() throws Exception {
      HttpRequest getFlavor = HttpRequest.builder().method("GET").endpoint(
               URI.create("https://compute.north.host/v1.1/3456/flavors/foo")).headers(
               ImmutableMultimap.<String, String> builder().put("Accept", "application/json").put("X-Auth-Token",
                        authToken).build()).build();

      HttpResponse getFlavorResponse = HttpResponse.builder().statusCode(200).payload(
               payloadFromResource("/flavor_details.json")).build();

      NovaClient clientWhenServersExist = requestsSendResponses(keystoneAuthWithAccessKeyAndSecretKey,
               responseWithKeystoneAccess, getFlavor, getFlavorResponse);

      assertEquals(clientWhenServersExist.getFlavorClientForRegion("North").getFlavor("foo").toString(),
               new ParseFlavorTest().expected().toString());
   }

}
