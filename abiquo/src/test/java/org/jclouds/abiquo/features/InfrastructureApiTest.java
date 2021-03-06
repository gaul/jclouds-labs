/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jclouds.abiquo.features;

import static org.jclouds.abiquo.domain.DomainUtils.withHeader;
import static org.jclouds.reflect.Reflection2.method;

import java.io.IOException;

import javax.ws.rs.core.MediaType;

import org.jclouds.Fallbacks.NullOnNotFoundOr404;
import org.jclouds.abiquo.AbiquoFallbacks.FalseIfNotAvailable;
import org.jclouds.abiquo.AbiquoFallbacks.PropagateAbiquoExceptionOnNotFoundOr4xx;
import org.jclouds.abiquo.domain.EnterpriseResources;
import org.jclouds.abiquo.domain.InfrastructureResources;
import org.jclouds.abiquo.domain.NetworkResources;
import org.jclouds.abiquo.domain.infrastructure.options.DatacenterOptions;
import org.jclouds.abiquo.domain.infrastructure.options.IpmiOptions;
import org.jclouds.abiquo.domain.infrastructure.options.MachineOptions;
import org.jclouds.abiquo.domain.infrastructure.options.StoragePoolOptions;
import org.jclouds.abiquo.domain.network.options.NetworkOptions;
import org.jclouds.fallbacks.MapHttp4xxCodesToExceptions;
import org.jclouds.http.functions.ParseXMLWithJAXB;
import org.jclouds.http.functions.ReleasePayloadAndReturn;
import org.jclouds.http.functions.ReturnStringIf2xx;
import org.jclouds.http.functions.ReturnTrueIf2xx;
import org.jclouds.reflect.Invocation;
import org.jclouds.rest.internal.GeneratedHttpRequest;
import org.testng.annotations.Test;

import com.abiquo.model.enumerator.HypervisorType;
import com.abiquo.model.enumerator.NetworkType;
import com.abiquo.model.enumerator.RemoteServiceType;
import com.abiquo.server.core.cloud.HypervisorTypesDto;
import com.abiquo.server.core.cloud.VirtualMachineWithNodeExtendedDto;
import com.abiquo.server.core.cloud.VirtualMachinesWithNodeExtendedDto;
import com.abiquo.server.core.enterprise.DatacentersLimitsDto;
import com.abiquo.server.core.enterprise.EnterpriseDto;
import com.abiquo.server.core.infrastructure.DatacenterDto;
import com.abiquo.server.core.infrastructure.DatacentersDto;
import com.abiquo.server.core.infrastructure.MachineDto;
import com.abiquo.server.core.infrastructure.MachineIpmiStateDto;
import com.abiquo.server.core.infrastructure.MachineStateDto;
import com.abiquo.server.core.infrastructure.MachinesDto;
import com.abiquo.server.core.infrastructure.RackDto;
import com.abiquo.server.core.infrastructure.RacksDto;
import com.abiquo.server.core.infrastructure.RemoteServiceDto;
import com.abiquo.server.core.infrastructure.RemoteServicesDto;
import com.abiquo.server.core.infrastructure.network.ExternalIpDto;
import com.abiquo.server.core.infrastructure.network.PublicIpDto;
import com.abiquo.server.core.infrastructure.network.UnmanagedIpDto;
import com.abiquo.server.core.infrastructure.network.VLANNetworkDto;
import com.abiquo.server.core.infrastructure.network.VLANNetworksDto;
import com.abiquo.server.core.infrastructure.network.VlanTagAvailabilityDto;
import com.abiquo.server.core.infrastructure.storage.StorageDeviceDto;
import com.abiquo.server.core.infrastructure.storage.StorageDevicesDto;
import com.abiquo.server.core.infrastructure.storage.StorageDevicesMetadataDto;
import com.abiquo.server.core.infrastructure.storage.StoragePoolDto;
import com.abiquo.server.core.infrastructure.storage.StoragePoolsDto;
import com.abiquo.server.core.infrastructure.storage.TierDto;
import com.abiquo.server.core.infrastructure.storage.TiersDto;
import com.google.common.collect.ImmutableList;
import com.google.common.reflect.Invokable;

/**
 * Tests annotation parsing of {@code InfrastructureApi}
 * 
 * @author Ignasi Barrera
 */
@Test(groups = "unit", testName = "InfrastructureApiTest")
public class InfrastructureApiTest extends BaseAbiquoApiTest<InfrastructureApi> {
   /*********************** Datacenter ***********************/

   public void testListDatacenters() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "listDatacenters");
      GeneratedHttpRequest request = processor.apply(Invocation.create(method, ImmutableList.of()));

      assertRequestLineEquals(request, "GET http://localhost/api/admin/datacenters HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "Accept: " + DatacentersDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, null);

      checkFilters(request);
   }

   public void testCreateDatacenter() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "createDatacenter", DatacenterDto.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method,
            ImmutableList.<Object> of(InfrastructureResources.datacenterPost())));

      assertRequestLineEquals(request, "POST http://localhost/api/admin/datacenters HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "Accept: " + DatacenterDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, withHeader(InfrastructureResources.datacenterPostPayload()), DatacenterDto.class,
            DatacenterDto.BASE_MEDIA_TYPE, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, null);

      checkFilters(request);
   }

   public void testGetDatacenter() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "getDatacenter", Integer.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method, ImmutableList.<Object> of(1)));

      assertRequestLineEquals(request, "GET http://localhost/api/admin/datacenters/1 HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "Accept: " + DatacenterDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, NullOnNotFoundOr404.class);

      checkFilters(request);
   }

   public void testUpdateDatacenter() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "updateDatacenter", DatacenterDto.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method,
            ImmutableList.<Object> of(InfrastructureResources.datacenterPut())));

      assertRequestLineEquals(request, "PUT http://localhost/api/admin/datacenters/1 HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "Accept: " + DatacenterDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, withHeader(InfrastructureResources.datacenterPutPayload()), DatacenterDto.class,
            DatacenterDto.BASE_MEDIA_TYPE, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, null);

      checkFilters(request);
   }

   public void testDeleteDatacenter() throws SecurityException, NoSuchMethodException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "deleteDatacenter", DatacenterDto.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method,
            ImmutableList.<Object> of(InfrastructureResources.datacenterPut())));

      assertRequestLineEquals(request, "DELETE http://localhost/api/admin/datacenters/1 HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ReleasePayloadAndReturn.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, null);

      checkFilters(request);
   }

   public void testListLimitsDatacenter() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "listLimits", DatacenterDto.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method,
            ImmutableList.<Object> of(InfrastructureResources.datacenterPut())));

      assertRequestLineEquals(request, "GET http://localhost/api/admin/datacenters/1/action/getLimits HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "Accept: " + DatacentersLimitsDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, null);

      checkFilters(request);
   }

   /*********************** Hypervisor ***********************/

   public void testGetHypervisorTypeFromMachine() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "getHypervisorTypeFromMachine", DatacenterDto.class,
            DatacenterOptions.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(
            method,
            ImmutableList.<Object> of(InfrastructureResources.datacenterPut(),
                  DatacenterOptions.builder().ip("10.60.1.120").build())));

      assertRequestLineEquals(request,
            "GET http://localhost/api/admin/datacenters/1/action/hypervisor?ip=10.60.1.120 HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "Accept: " + MediaType.TEXT_PLAIN + "\n");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ReturnStringIf2xx.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, null);

      checkFilters(request);
   }

   public void testGetHypervisorTypesFromDatacenter() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "getHypervisorTypes", DatacenterDto.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method,
            ImmutableList.<Object> of(InfrastructureResources.datacenterPut())));

      assertRequestLineEquals(request, "GET http://localhost/api/admin/datacenters/1/hypervisors HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "Accept: " + HypervisorTypesDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, null);

      checkFilters(request);
   }

   /*********************** Managed Rack ***********************/

   public void testListRacks() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "listRacks", DatacenterDto.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method,
            ImmutableList.<Object> of(InfrastructureResources.datacenterPut())));

      assertRequestLineEquals(request, "GET http://localhost/api/admin/datacenters/1/racks HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "Accept: " + RacksDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, null);

      checkFilters(request);
   }

   public void testCreateRack() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "createRack", DatacenterDto.class, RackDto.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method,
            ImmutableList.<Object> of(InfrastructureResources.datacenterPut(), InfrastructureResources.rackPost())));

      assertRequestLineEquals(request, "POST http://localhost/api/admin/datacenters/1/racks HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "Accept: " + RackDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, withHeader(InfrastructureResources.rackPostPayload()), RackDto.class,
            RackDto.BASE_MEDIA_TYPE, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, null);

      checkFilters(request);
   }

   public void testGetRack() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "getRack", DatacenterDto.class, Integer.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method,
            ImmutableList.<Object> of(InfrastructureResources.datacenterPut(), 1)));

      assertRequestLineEquals(request, "GET http://localhost/api/admin/datacenters/1/racks/1 HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "Accept: " + RackDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, NullOnNotFoundOr404.class);

      checkFilters(request);
   }

   public void testUpdateRack() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "updateRack", RackDto.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method,
            ImmutableList.<Object> of(InfrastructureResources.rackPut())));

      assertRequestLineEquals(request, "PUT http://localhost/api/admin/datacenters/1/racks/1 HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "Accept: " + RackDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, withHeader(InfrastructureResources.rackPutPayload()), RackDto.class,
            RackDto.BASE_MEDIA_TYPE, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, null);

      checkFilters(request);
   }

   public void testDeleteRack() throws SecurityException, NoSuchMethodException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "deleteRack", RackDto.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method,
            ImmutableList.<Object> of(InfrastructureResources.rackPut())));

      assertRequestLineEquals(request, "DELETE http://localhost/api/admin/datacenters/1/racks/1 HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ReleasePayloadAndReturn.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, null);

      checkFilters(request);
   }

   /*********************** Remote Service **********************/

   public void testListRemoteServices() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "listRemoteServices", DatacenterDto.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method,
            ImmutableList.<Object> of(InfrastructureResources.datacenterPut())));

      assertRequestLineEquals(request, "GET http://localhost/api/admin/datacenters/1/remoteservices HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "Accept: " + RemoteServicesDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, null);

      checkFilters(request);
   }

   public void testCreateRemoteService() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "createRemoteService", DatacenterDto.class,
            RemoteServiceDto.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(
            method,
            ImmutableList.<Object> of(InfrastructureResources.datacenterPut(),
                  InfrastructureResources.remoteServicePost())));

      assertRequestLineEquals(request, "POST http://localhost/api/admin/datacenters/1/remoteservices HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "Accept: " + RemoteServiceDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, withHeader(InfrastructureResources.remoteServicePostPayload()),
            RemoteServiceDto.class, RemoteServiceDto.BASE_MEDIA_TYPE, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, null);

      checkFilters(request);
   }

   public void testGetRemoteService() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "getRemoteService", DatacenterDto.class,
            RemoteServiceType.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method, ImmutableList.<Object> of(
            InfrastructureResources.datacenterPut(), RemoteServiceType.STORAGE_SYSTEM_MONITOR)));

      assertRequestLineEquals(request,
            "GET http://localhost/api/admin/datacenters/1/remoteservices/storagesystemmonitor HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "Accept: " + RemoteServiceDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, NullOnNotFoundOr404.class);

      checkFilters(request);
   }

   public void testUpdateRemoteService() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "updateRemoteService", RemoteServiceDto.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method,
            ImmutableList.<Object> of(InfrastructureResources.remoteServicePut())));

      assertRequestLineEquals(request,
            "PUT http://localhost/api/admin/datacenters/1/remoteservices/nodecollector HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "Accept: " + RemoteServiceDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, withHeader(InfrastructureResources.remoteServicePutPayload()),
            RemoteServiceDto.class, RemoteServiceDto.BASE_MEDIA_TYPE, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, null);

      checkFilters(request);
   }

   public void testDeleteRemoteService() throws SecurityException, NoSuchMethodException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "deleteRemoteService", RemoteServiceDto.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method,
            ImmutableList.<Object> of(InfrastructureResources.remoteServicePut())));

      assertRequestLineEquals(request,
            "DELETE http://localhost/api/admin/datacenters/1/remoteservices/nodecollector HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ReleasePayloadAndReturn.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, null);

      checkFilters(request);
   }

   public void testIsAvailableRemoteService() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "isAvailable", RemoteServiceDto.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method,
            ImmutableList.<Object> of(InfrastructureResources.remoteServicePut())));

      String checkUri = InfrastructureResources.remoteServicePut().searchLink("check").getHref();
      assertRequestLineEquals(request, String.format("GET %s HTTP/1.1", checkUri));
      assertNonPayloadHeadersEqual(request, "");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ReturnTrueIf2xx.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, FalseIfNotAvailable.class);

      checkFilters(request);
   }

   /*********************** Machine ***********************/

   public void testDiscoverSingleMachineWithoutOptions() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "discoverSingleMachine", DatacenterDto.class,
            String.class, HypervisorType.class, String.class, String.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method, ImmutableList.<Object> of(
            InfrastructureResources.datacenterPut(), "10.60.1.222", HypervisorType.XENSERVER, "user", "pass")));

      String baseUrl = "http://localhost/api/admin/datacenters/1/action/discoversingle";
      String query = "ip=10.60.1.222&hypervisor=XENSERVER&user=user&password=pass";
      String expectedRequest = String.format("GET %s?%s HTTP/1.1", baseUrl, query);

      assertRequestLineEquals(request, expectedRequest);
      assertNonPayloadHeadersEqual(request, "Accept: " + MachineDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, PropagateAbiquoExceptionOnNotFoundOr4xx.class);

      checkFilters(request);
   }

   public void testDiscoverSingleMachineAllParams() throws SecurityException, NoSuchMethodException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "discoverSingleMachine", DatacenterDto.class,
            String.class, HypervisorType.class, String.class, String.class, MachineOptions.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method, ImmutableList.<Object> of(
            InfrastructureResources.datacenterPut(), "80.80.80.80", HypervisorType.KVM, "user", "pass", MachineOptions
                  .builder().port(8889).build())));

      String baseUrl = "http://localhost/api/admin/datacenters/1/action/discoversingle";
      String query = "ip=80.80.80.80&hypervisor=KVM&user=user&password=pass&port=8889";
      String expectedRequest = String.format("GET %s?%s HTTP/1.1", baseUrl, query);

      assertRequestLineEquals(request, expectedRequest);
      assertNonPayloadHeadersEqual(request, "Accept: " + MachineDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, PropagateAbiquoExceptionOnNotFoundOr4xx.class);

      checkFilters(request);
   }

   public void testDiscoverSingleMachineDefaultValues() throws SecurityException, NoSuchMethodException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "discoverSingleMachine", DatacenterDto.class,
            String.class, HypervisorType.class, String.class, String.class, MachineOptions.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method, ImmutableList.<Object> of(
            InfrastructureResources.datacenterPut(), "80.80.80.80", HypervisorType.KVM, "user", "pass", MachineOptions
                  .builder().build())));

      String baseUrl = "http://localhost/api/admin/datacenters/1/action/discoversingle";
      String query = "ip=80.80.80.80&hypervisor=KVM&user=user&password=pass";
      String expectedRequest = String.format("GET %s?%s HTTP/1.1", baseUrl, query);

      assertRequestLineEquals(request, expectedRequest);
      assertNonPayloadHeadersEqual(request, "Accept: " + MachineDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, PropagateAbiquoExceptionOnNotFoundOr4xx.class);

      checkFilters(request);
   }

   public void testDiscoverMultipleMachinesWithoutOptions() throws SecurityException, NoSuchMethodException,
         IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "discoverMultipleMachines", DatacenterDto.class,
            String.class, String.class, HypervisorType.class, String.class, String.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method, ImmutableList.<Object> of(
            InfrastructureResources.datacenterPut(), "10.60.1.222", "10.60.1.250", HypervisorType.XENSERVER, "user",
            "pass")));

      String baseUrl = "http://localhost/api/admin/datacenters/1/action/discovermultiple";
      String query = "ipFrom=10.60.1.222&ipTo=10.60.1.250&hypervisor=XENSERVER&user=user&password=pass";
      String expectedRequest = String.format("GET %s?%s HTTP/1.1", baseUrl, query);

      assertRequestLineEquals(request, expectedRequest);
      assertNonPayloadHeadersEqual(request, "Accept: " + MachinesDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, PropagateAbiquoExceptionOnNotFoundOr4xx.class);

      checkFilters(request);
   }

   public void testDiscoverMultipleMachinesAllParams() throws SecurityException, NoSuchMethodException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "discoverMultipleMachines", DatacenterDto.class,
            String.class, String.class, HypervisorType.class, String.class, String.class, MachineOptions.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method, ImmutableList.<Object> of(
            InfrastructureResources.datacenterPut(), "80.80.80.80", "80.80.80.86", HypervisorType.KVM, "user", "pass",
            MachineOptions.builder().port(8889).build())));

      String baseUrl = "http://localhost/api/admin/datacenters/1/action/discovermultiple";
      String query = "ipFrom=80.80.80.80&ipTo=80.80.80.86&hypervisor=KVM&user=user&password=pass&port=8889";
      String expectedRequest = String.format("GET %s?%s HTTP/1.1", baseUrl, query);

      assertRequestLineEquals(request, expectedRequest);
      assertNonPayloadHeadersEqual(request, "Accept: " + MachinesDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, PropagateAbiquoExceptionOnNotFoundOr4xx.class);

      checkFilters(request);
   }

   public void testCheckMachineStateWithoutOptions() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "checkMachineState", DatacenterDto.class, String.class,
            HypervisorType.class, String.class, String.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method, ImmutableList.<Object> of(
            InfrastructureResources.datacenterPut(), "10.60.1.222", HypervisorType.XENSERVER, "user", "pass")));

      String baseUrl = "http://localhost/api/admin/datacenters/1/action/checkmachinestate";
      String query = "ip=10.60.1.222&hypervisor=XENSERVER&user=user&password=pass";
      String expectedRequest = String.format("GET %s?%s HTTP/1.1", baseUrl, query);

      assertRequestLineEquals(request, expectedRequest);
      assertNonPayloadHeadersEqual(request, "Accept: " + MachineStateDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, PropagateAbiquoExceptionOnNotFoundOr4xx.class);

      checkFilters(request);
   }

   public void testCheckMachineStateAllParams() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "checkMachineState", DatacenterDto.class, String.class,
            HypervisorType.class, String.class, String.class, MachineOptions.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method, ImmutableList.<Object> of(
            InfrastructureResources.datacenterPut(), "10.60.1.222", HypervisorType.XENSERVER, "user", "pass",
            MachineOptions.builder().port(8889).build())));

      String baseUrl = "http://localhost/api/admin/datacenters/1/action/checkmachinestate";
      String query = "ip=10.60.1.222&hypervisor=XENSERVER&user=user&password=pass&port=8889";
      String expectedRequest = String.format("GET %s?%s HTTP/1.1", baseUrl, query);

      assertRequestLineEquals(request, expectedRequest);
      assertNonPayloadHeadersEqual(request, "Accept: " + MachineStateDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, PropagateAbiquoExceptionOnNotFoundOr4xx.class);

      checkFilters(request);
   }

   public void testCheckMachineIpmiStateWithoutOptions() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "checkMachineIpmiState", DatacenterDto.class,
            String.class, String.class, String.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method,
            ImmutableList.<Object> of(InfrastructureResources.datacenterPut(), "10.60.1.222", "user", "pass")));

      String baseUrl = "http://localhost/api/admin/datacenters/1/action/checkmachineipmistate";
      String query = "ip=10.60.1.222&user=user&password=pass";
      String expectedRequest = String.format("GET %s?%s HTTP/1.1", baseUrl, query);

      assertRequestLineEquals(request, expectedRequest);
      assertNonPayloadHeadersEqual(request, "Accept: " + MachineIpmiStateDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, PropagateAbiquoExceptionOnNotFoundOr4xx.class);

      checkFilters(request);
   }

   public void testCheckMachineIpmiStateWithALLOptions() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "checkMachineIpmiState", DatacenterDto.class,
            String.class, String.class, String.class, IpmiOptions.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method, ImmutableList.<Object> of(
            InfrastructureResources.datacenterPut(), "10.60.1.222", "user", "pass", IpmiOptions.builder().port(8889)
                  .build())));

      String baseUrl = "http://localhost/api/admin/datacenters/1/action/checkmachineipmistate";
      String query = "ip=10.60.1.222&user=user&password=pass&port=8889";
      String expectedRequest = String.format("GET %s?%s HTTP/1.1", baseUrl, query);

      assertRequestLineEquals(request, expectedRequest);
      assertNonPayloadHeadersEqual(request, "Accept: " + MachineIpmiStateDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, PropagateAbiquoExceptionOnNotFoundOr4xx.class);

      checkFilters(request);
   }

   public void testListMachines() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "listMachines", RackDto.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method,
            ImmutableList.<Object> of(InfrastructureResources.rackPut())));

      assertRequestLineEquals(request, "GET http://localhost/api/admin/datacenters/1/racks/1/machines HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "Accept: " + MachinesDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, null);

      checkFilters(request);
   }

   public void testGetMachine() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "getMachine", RackDto.class, Integer.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method,
            ImmutableList.<Object> of(InfrastructureResources.rackPut(), 1)));

      assertRequestLineEquals(request, "GET http://localhost/api/admin/datacenters/1/racks/1/machines/1 HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "Accept: " + MachineDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, NullOnNotFoundOr404.class);

      checkFilters(request);
   }

   public void testCheckMachineState() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "checkMachineState", MachineDto.class, boolean.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method,
            ImmutableList.<Object> of(InfrastructureResources.machinePut(), true)));

      assertRequestLineEquals(request,
            "GET http://localhost/api/admin/datacenters/1/racks/1/machines/1/action/checkstate?sync=true HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "Accept: " + MachineStateDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, null);

      checkFilters(request);
   }

   public void testCheckMachineIpmiState() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "checkMachineIpmiState", MachineDto.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method,
            ImmutableList.<Object> of(InfrastructureResources.machinePut())));

      assertRequestLineEquals(request,
            "GET http://localhost/api/admin/datacenters/1/racks/1/machines/1/action/checkipmistate HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "Accept: " + MachineIpmiStateDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, null);

      checkFilters(request);
   }

   public void testCreateMachine() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "createMachine", RackDto.class, MachineDto.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method,
            ImmutableList.<Object> of(InfrastructureResources.rackPut(), InfrastructureResources.machinePost())));

      assertRequestLineEquals(request, "POST http://localhost/api/admin/datacenters/1/racks/1/machines HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "Accept: " + MachineDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, withHeader(InfrastructureResources.machinePostPayload()), MachineDto.class,
            MachineDto.BASE_MEDIA_TYPE, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, null);

      checkFilters(request);
   }

   public void testUpdateMachine() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "updateMachine", MachineDto.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method,
            ImmutableList.<Object> of(InfrastructureResources.machinePut())));

      assertRequestLineEquals(request, "PUT http://localhost/api/admin/datacenters/1/racks/1/machines/1 HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "Accept: " + MachineDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, withHeader(InfrastructureResources.machinePutPayload()), MachineDto.class,
            MachineDto.BASE_MEDIA_TYPE, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, null);

      checkFilters(request);
   }

   public void testDeleteMachine() throws SecurityException, NoSuchMethodException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "deleteMachine", MachineDto.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method,
            ImmutableList.<Object> of(InfrastructureResources.machinePut())));

      assertRequestLineEquals(request, "DELETE http://localhost/api/admin/datacenters/1/racks/1/machines/1 HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ReleasePayloadAndReturn.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, null);

      checkFilters(request);
   }

   public void testReserveMachine() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "reserveMachine", EnterpriseDto.class, MachineDto.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method,
            ImmutableList.<Object> of(EnterpriseResources.enterprisePut(), InfrastructureResources.machinePut())));

      assertRequestLineEquals(request, "POST http://localhost/api/admin/enterprises/1/reservedmachines HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "Accept: " + MachineDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, withHeader(InfrastructureResources.machinePutPayload()), MachineDto.class,
            MachineDto.BASE_MEDIA_TYPE, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, null);

      checkFilters(request);
   }

   public void testCancelReservation() throws SecurityException, NoSuchMethodException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "cancelReservation", EnterpriseDto.class,
            MachineDto.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method,
            ImmutableList.<Object> of(EnterpriseResources.enterprisePut(), InfrastructureResources.machinePut())));

      assertRequestLineEquals(request, "DELETE http://localhost/api/admin/enterprises/1/reservedmachines/1 HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ReleasePayloadAndReturn.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, null);

      checkFilters(request);
   }

   public void testListVirtualMachinesByMachine() throws SecurityException, NoSuchMethodException, IOException {
      MachineOptions options = MachineOptions.builder().sync(true).build();

      Invokable<?, ?> method = method(InfrastructureApi.class, "listVirtualMachinesByMachine", MachineDto.class,
            MachineOptions.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method,
            ImmutableList.<Object> of(InfrastructureResources.machinePut(), options)));

      assertRequestLineEquals(request,
            "GET http://localhost/api/admin/datacenters/1/racks/1/machines/1/virtualmachines?sync=true HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "Accept: " + VirtualMachinesWithNodeExtendedDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, null);

      checkFilters(request);
   }

   public void testGetVirtualMachineByMachine() throws SecurityException, NoSuchMethodException, IOException {

      Invokable<?, ?> method = method(InfrastructureApi.class, "getVirtualMachine", MachineDto.class, Integer.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method,
            ImmutableList.<Object> of(InfrastructureResources.machinePut(), 1)));

      assertRequestLineEquals(request,
            "GET http://localhost/api/admin/datacenters/1/racks/1/machines/1/virtualmachines/1 HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "Accept: " + VirtualMachineWithNodeExtendedDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, NullOnNotFoundOr404.class);

      checkFilters(request);
   }

   /*********************** Storage Device ***********************/

   public void testListStorageDevices() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "listStorageDevices", DatacenterDto.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method,
            ImmutableList.<Object> of(InfrastructureResources.datacenterPut())));

      assertRequestLineEquals(request, "GET http://localhost/api/admin/datacenters/1/storage/devices HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "Accept: " + StorageDevicesDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, null);

      checkFilters(request);
   }

   public void testListSupportedStorageDevices() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "listSupportedStorageDevices", DatacenterDto.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method,
            ImmutableList.<Object> of(InfrastructureResources.datacenterPut())));

      assertRequestLineEquals(request,
            "GET http://localhost/api/admin/datacenters/1/storage/devices/action/supported HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "Accept: " + StorageDevicesMetadataDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, null);

      checkFilters(request);
   }

   public void testCreateStorageDevice() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "createStorageDevice", DatacenterDto.class,
            StorageDeviceDto.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(
            method,
            ImmutableList.<Object> of(InfrastructureResources.datacenterPut(),
                  InfrastructureResources.storageDevicePost())));

      assertRequestLineEquals(request, "POST http://localhost/api/admin/datacenters/1/storage/devices HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "Accept: " + StorageDeviceDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, withHeader(InfrastructureResources.storageDevicePostPayload()),
            StorageDeviceDto.class, StorageDeviceDto.BASE_MEDIA_TYPE, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, null);

      checkFilters(request);
   }

   public void testDeleteStorageDevice() throws SecurityException, NoSuchMethodException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "deleteStorageDevice", StorageDeviceDto.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method,
            ImmutableList.<Object> of(InfrastructureResources.storageDevicePut())));

      assertRequestLineEquals(request, "DELETE http://localhost/api/admin/datacenters/1/storage/devices/1 HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ReleasePayloadAndReturn.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, null);

      checkFilters(request);
   }

   public void testUpdateStorageDevice() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "updateStorageDevice", StorageDeviceDto.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method,
            ImmutableList.<Object> of(InfrastructureResources.storageDevicePut())));

      assertRequestLineEquals(request, "PUT http://localhost/api/admin/datacenters/1/storage/devices/1 HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "Accept: " + StorageDeviceDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, withHeader(InfrastructureResources.storageDevicePutPayload()),
            StorageDeviceDto.class, StorageDeviceDto.BASE_MEDIA_TYPE, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, null);

      checkFilters(request);
   }

   public void testGetStorageDevice() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "getStorageDevice", DatacenterDto.class, Integer.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method,
            ImmutableList.<Object> of(InfrastructureResources.datacenterPut(), 1)));

      assertRequestLineEquals(request, "GET http://localhost/api/admin/datacenters/1/storage/devices/1 HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "Accept: " + StorageDeviceDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, NullOnNotFoundOr404.class);

      checkFilters(request);
   }

   /*********************** Tier ***********************/

   public void testListTiers() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "listTiers", DatacenterDto.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method,
            ImmutableList.<Object> of(InfrastructureResources.datacenterPut())));

      assertRequestLineEquals(request, "GET http://localhost/api/admin/datacenters/1/storage/tiers HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "Accept: " + TiersDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, null);

      checkFilters(request);
   }

   public void testUpdateTier() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "updateTier", TierDto.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method,
            ImmutableList.<Object> of(InfrastructureResources.tierPut())));

      assertRequestLineEquals(request, "PUT http://localhost/api/admin/datacenters/1/storage/tiers/1 HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "Accept: " + TierDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, withHeader(InfrastructureResources.tierPutPayload()), TierDto.class,
            TierDto.BASE_MEDIA_TYPE, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, null);

      checkFilters(request);
   }

   public void testGetTier() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "getTier", DatacenterDto.class, Integer.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method,
            ImmutableList.<Object> of(InfrastructureResources.datacenterPut(), 1)));

      assertRequestLineEquals(request, "GET http://localhost/api/admin/datacenters/1/storage/tiers/1 HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "Accept: " + TierDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, NullOnNotFoundOr404.class);

      checkFilters(request);
   }

   /*********************** StoragePool ***********************/

   public void testListSyncStoragePools() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "listStoragePools", StorageDeviceDto.class,
            StoragePoolOptions.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(
            method,
            ImmutableList.<Object> of(InfrastructureResources.storageDevicePut(),
                  StoragePoolOptions.builder().sync(true).build())));

      assertRequestLineEquals(request,
            "GET http://localhost/api/admin/datacenters/1/storage/devices/1/pools?sync=true HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "Accept: " + StoragePoolsDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, null);

      checkFilters(request);
   }

   public void testListStoragePoolsFromTier() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "listStoragePools", TierDto.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(
            method,
            ImmutableList.<Object> of(InfrastructureResources.tierPut(), StoragePoolOptions.builder().sync(true)
                  .build())));

      assertRequestLineEquals(request, "GET http://localhost/api/admin/datacenters/1/storage/tiers/1/pools HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "Accept: " + StoragePoolsDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, null);

      checkFilters(request);
   }

   public void testListStoragePoolsNoParams() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "listStoragePools", StorageDeviceDto.class,
            StoragePoolOptions.class);
      GeneratedHttpRequest request = processor
            .apply(Invocation.create(method, ImmutableList.<Object> of(InfrastructureResources.storageDevicePut(),
                  StoragePoolOptions.builder().build())));

      assertRequestLineEquals(request, "GET http://localhost/api/admin/datacenters/1/storage/devices/1/pools HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "Accept: " + StoragePoolsDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, null);

      checkFilters(request);
   }

   public void testCreateStoragePool() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "createStoragePool", StorageDeviceDto.class,
            StoragePoolDto.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(
            method,
            ImmutableList.<Object> of(InfrastructureResources.storageDevicePut(),
                  InfrastructureResources.storagePoolPost())));

      assertRequestLineEquals(request, "POST http://localhost/api/admin/datacenters/1/storage/devices/1/pools HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "Accept: " + StoragePoolDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, withHeader(InfrastructureResources.storagePoolPostPayload()), StoragePoolDto.class,
            StoragePoolDto.BASE_MEDIA_TYPE, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, null);

      checkFilters(request);
   }

   public void testUpdateStoragePool() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "updateStoragePool", StoragePoolDto.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method,
            ImmutableList.<Object> of(InfrastructureResources.storagePoolPut())));

      assertRequestLineEquals(request,
            "PUT http://localhost/api/admin/datacenters/1/storage/devices/1/pools/tururututu HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "Accept: " + StoragePoolDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, withHeader(InfrastructureResources.storagePoolPutPayload()), StoragePoolDto.class,
            StoragePoolDto.BASE_MEDIA_TYPE, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, null);

      checkFilters(request);
   }

   public void testDeleteStoragePool() throws SecurityException, NoSuchMethodException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "deleteStoragePool", StoragePoolDto.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method,
            ImmutableList.<Object> of(InfrastructureResources.storagePoolPut())));

      assertRequestLineEquals(request,
            "DELETE http://localhost/api/admin/datacenters/1/storage/devices/1/pools/tururututu HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ReleasePayloadAndReturn.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, null);

      checkFilters(request);
   }

   public void testGetStoragePool() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "getStoragePool", StorageDeviceDto.class, String.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method, ImmutableList.<Object> of(
            InfrastructureResources.storageDevicePut(), InfrastructureResources.storagePoolPut().getIdStorage())));

      assertRequestLineEquals(request,
            "GET http://localhost/api/admin/datacenters/1/storage/devices/1/pools/tururututu HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "Accept: " + StoragePoolDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, NullOnNotFoundOr404.class);

      checkFilters(request);
   }

   public void testRefreshStoragePool() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "refreshStoragePool", StoragePoolDto.class,
            StoragePoolOptions.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(
            method,
            ImmutableList.<Object> of(InfrastructureResources.storagePoolPut(), StoragePoolOptions.builder().sync(true)
                  .build())));

      assertRequestLineEquals(request,
            "GET http://localhost/api/admin/datacenters/1/storage/devices/1/pools/tururututu?sync=true HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "Accept: " + StoragePoolDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, MapHttp4xxCodesToExceptions.class);

      checkFilters(request);
   }

   /*********************** Network ***********************/

   public void testListNetworks() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "listNetworks", DatacenterDto.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method,
            ImmutableList.<Object> of(InfrastructureResources.datacenterPut())));

      assertRequestLineEquals(request, "GET http://localhost/api/admin/datacenters/1/network HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "Accept: " + VLANNetworksDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, null);

      checkFilters(request);
   }

   public void testListNetworksWithOptions() throws SecurityException, NoSuchMethodException, IOException {
      NetworkOptions options = NetworkOptions.builder().type(NetworkType.PUBLIC).build();

      Invokable<?, ?> method = method(InfrastructureApi.class, "listNetworks", DatacenterDto.class,
            NetworkOptions.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method,
            ImmutableList.<Object> of(InfrastructureResources.datacenterPut(), options)));

      assertRequestLineEquals(request, "GET http://localhost/api/admin/datacenters/1/network?type=PUBLIC HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "Accept: " + VLANNetworksDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, null);

      checkFilters(request);
   }

   public void testGetNetworks() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "getNetwork", DatacenterDto.class, Integer.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method,
            ImmutableList.<Object> of(InfrastructureResources.datacenterPut(), 1)));

      assertRequestLineEquals(request, "GET http://localhost/api/admin/datacenters/1/network/1 HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "Accept: " + VLANNetworkDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, NullOnNotFoundOr404.class);

      checkFilters(request);
   }

   public void testCreateNetwork() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "createNetwork", DatacenterDto.class,
            VLANNetworkDto.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method,
            ImmutableList.<Object> of(InfrastructureResources.datacenterPut(), NetworkResources.vlanPost())));

      assertRequestLineEquals(request, "POST http://localhost/api/admin/datacenters/1/network HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "Accept: " + VLANNetworkDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, withHeader(NetworkResources.vlanNetworkPostPayload()), VLANNetworkDto.class,
            VLANNetworkDto.BASE_MEDIA_TYPE, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, null);

      checkFilters(request);
   }

   public void testUpdateNetwork() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "updateNetwork", VLANNetworkDto.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method,
            ImmutableList.<Object> of(NetworkResources.publicNetworkPut())));

      assertRequestLineEquals(request, "PUT http://localhost/api/admin/datacenters/1/network/1 HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "Accept: " + VLANNetworkDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, withHeader(NetworkResources.publicNetworkPutPayload()), VLANNetworkDto.class,
            VLANNetworkDto.BASE_MEDIA_TYPE, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, null);

      checkFilters(request);
   }

   public void testDeleteNetwork() throws SecurityException, NoSuchMethodException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "deleteNetwork", VLANNetworkDto.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method,
            ImmutableList.<Object> of(NetworkResources.publicNetworkPut())));

      assertRequestLineEquals(request, "DELETE http://localhost/api/admin/datacenters/1/network/1 HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ReleasePayloadAndReturn.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, null);

      checkFilters(request);
   }

   public void testCheckTagAvailability() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "checkTagAvailability", DatacenterDto.class,
            Integer.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method,
            ImmutableList.<Object> of(InfrastructureResources.datacenterPut(), 2)));

      assertRequestLineEquals(request,
            "GET http://localhost/api/admin/datacenters/1/network/action/checkavailability?tag=2 HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "Accept: " + VlanTagAvailabilityDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, MapHttp4xxCodesToExceptions.class);

      checkFilters(request);
   }

   /*********************** Network IPs ***********************/

   public void testGetPublicIp() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "getPublicIp", VLANNetworkDto.class, Integer.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method,
            ImmutableList.<Object> of(NetworkResources.publicNetworkPut(), 1)));

      assertRequestLineEquals(request, "GET http://localhost/api/admin/datacenters/1/network/1/ips/1 HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "Accept: " + PublicIpDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, null);

      checkFilters(request);
   }

   public void testGetExternalIp() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "getExternalIp", VLANNetworkDto.class, Integer.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method,
            ImmutableList.<Object> of(NetworkResources.externalNetworkPut(), 1)));

      assertRequestLineEquals(request,
            "GET http://localhost/api/admin/enterprises/1/limits/1/externalnetworks/1/ips/1 HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "Accept: " + ExternalIpDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, null);

      checkFilters(request);
   }

   public void testGetUnmanagedIp() throws SecurityException, NoSuchMethodException, IOException {
      Invokable<?, ?> method = method(InfrastructureApi.class, "getUnmanagedIp", VLANNetworkDto.class, Integer.class);
      GeneratedHttpRequest request = processor.apply(Invocation.create(method,
            ImmutableList.<Object> of(NetworkResources.externalNetworkPut(), 1)));

      assertRequestLineEquals(request,
            "GET http://localhost/api/admin/enterprises/1/limits/1/externalnetworks/1/ips/1 HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "Accept: " + UnmanagedIpDto.BASE_MEDIA_TYPE + "\n");
      assertPayloadEquals(request, null, null, false);

      assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
      assertSaxResponseParserClassEquals(method, null);
      assertFallbackClassEquals(method, null);

      checkFilters(request);
   }
}
